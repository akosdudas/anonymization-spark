package com.anonymization.mondrian;

import com.anonymization.mondrian.Record;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Partition implements Serializable {

    private List<Record> recordArrayList = new ArrayList<>();


    /**
     * JavaRDD for storing the data in spark, this helps us to work over the data in parallel.
     */
    private JavaRDD<Record> data;

    private HashMap<Integer, Long> statForDim = new HashMap<>();
    private HashMap<Integer, String> rangeFordim = new HashMap<>();

    public Partition(JavaRDD<Record> data) {
        this.data = data;
    }

    public List<Record> getRecordArrayList() {
        return recordArrayList;
    }

    public void getRangeForDim(int dimension) {
//        int min = recordArrayList.get(0).getForDim(dimension);
        if (recordArrayList.get(0).getQuidForDim(dimension).getType().equals("java.lang.Long")) {
            long min = 0;
            try {
                min = (long) recordArrayList.get(0).getQuidForDim(dimension).getValue();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            long max = (long) recordArrayList.get(0).getQuidForDim(dimension).getValue();
            for (Record r : recordArrayList) {
                if ((long) r.getQuidForDim(dimension).getValue() > max) {
                    max = (long) r.getQuidForDim(dimension).getValue();

                }
                if ((long) r.getQuidForDim(dimension).getValue() < min) {
                    min = (long) r.getQuidForDim(dimension).getValue();
                }
            }
            String s = "[" + min + "," + max + "]";
            rangeFordim.put(dimension, s);
        }
        if (recordArrayList.get(0).getQuidForDim(dimension).getType().equals("java.lang.String")){
            StringComparator comparator=new StringComparator();
            String s=comparator.getLongestCommonSubstring(recordArrayList.stream()
                    .map(record -> (String)record.getQuidForDim(dimension).getValue()).collect(Collectors.toList()));
            rangeFordim.put(dimension,s);
        }

    }



    public ArrayList<Partition> splitToPartitions(int k) {

        ArrayList<Partition> tmp = new ArrayList<>();
        long l=data.count();
        Broadcast<Long> br=Mondrian.sc.broadcast(l);

        if (br.getValue() < 2 * k) {
            recordArrayList = data.collect();
            getRangeForDim(0);
            getRangeForDim(1);
            getRangeForDim(2);
            getRangeForDim(3);
            String value=getSuppresedForPartition(0);
            for (Record mr : recordArrayList
            ) {
                mr.setFinalData(0, rangeFordim.get(0));
                mr.setFinalData(1, rangeFordim.get(1));
                mr.setFinalData(2,rangeFordim.get(2));
                mr.setFinalData(3,rangeFordim.get(3));
                mr.setFinalPressed(0,value);
            }

            return null;
        }

        updateStatistics();
        int dimension = getDimension();

        Record first=data.first();
        Partition lf;
        Partition rf;
        String quidType=first.getQuidForDim(dimension).getType();
        if (quidType.equals("java.lang.Long")) {
            JavaRDD<Record> dimRecordJavaPairRDD;
            dimRecordJavaPairRDD = data.mapToPair((PairFunction<Record, Long, Record>)
                    //record -> new Tuple2<>(record.getForDim(dimension), record)).sortByKey(true).values().cache();
                    record -> {
                        long val = (long) record.getQuidForDim(dimension).getValue();
                        return new Tuple2<>(val, record);


                    }).sortByKey(true).values().cache();

            JavaPairRDD<Record,Long> indexedRDD=dimRecordJavaPairRDD.zipWithIndex();

            long count=br.getValue();
            long medIndex=count/2;
            JavaRDD<Record> left=indexedRDD.filter(recordLongTuple2 -> recordLongTuple2._2()<=medIndex).keys();
            JavaRDD<Record> right=indexedRDD.filter(recordLongTuple2 -> recordLongTuple2._2>medIndex).keys();
            lf = new Partition(left);
            rf=new Partition(right);
            dimRecordJavaPairRDD.unpersist();

        }
        else{
            data = data.cache();
            StringComparator stringComparator=new StringComparator();
            JavaPairRDD<Record, Record> pairRDD = data.cartesian(data);
            JavaRDD<String> stringJavaRDD = pairRDD.map(stringStringTuple2 -> stringComparator.getLongestCommonSubstring
                    ((String) stringStringTuple2._1.getQuidForDim(dimension).getValue(),
                            (String) stringStringTuple2._2.getQuidForDim(dimension)
                                    .getValue())
                    .trim());
            Map<String, Long> commonStrings = stringJavaRDD.countByValue();
            HashMap<String,Long> h=new HashMap<>();
            for (Map.Entry<String,Long> m:commonStrings.entrySet()
                 ) {
                h.put(m.getKey(),m.getValue()/2);
            }
            Long sum = h.values().stream().reduce(0L, Long::sum);
            if (statForDim.containsKey(dimension))
                statForDim.replace(dimension, sum);
            else
                statForDim.put(dimension, sum);
            String result = "";
            double diff = -1;
            int halfSize = (int) (br.getValue() / 2);

            for (Map.Entry<String, Long> m : commonStrings.entrySet()) {
                if (diff == -1)
                    diff = Math.abs(halfSize - m.getValue());
                if (Math.abs(halfSize - m.getValue()) < diff) {
                    diff = Math.abs(halfSize - m.getValue());
                    result = m.getKey();
                }
            }
            String median=result;

            JavaRDD<Record> cont=data.filter(record ->((String) record.getQuidForDim(dimension).getValue()).contains(median));
            JavaRDD<Record> notcont=data.filter(record -> !((String)record.getQuidForDim(dimension).getValue()).contains(median));
//            long count=data.count();
            if (cont.count()==br.getValue() || cont.count()==0){
                double[] weigths={2.0,2.0};
                JavaRDD<Record>[] recordJavaRDDs=data.randomSplit(weigths);
                cont=recordJavaRDDs[0];
                notcont=recordJavaRDDs[1];
            }
            lf=new Partition(cont);
            rf=new Partition(notcont);

            data.unpersist();
        }
        tmp.add(lf);
        tmp.add(rf);

        return tmp;
    }

    private void updateStatistics() {
        if (data == null) return;
        ArrayList<Quid> quids = data.first().getQuids();
        for (int i = 0; i < quids.size(); i++) {

            final int finalI = i;
            long countForDim=0L;
            if (quids.get(i).getType().equals("java.lang.Long")) {
                countForDim = data.mapToPair((PairFunction<Record, Record, Long>) record -> {
                    return new Tuple2<>(record, (long) record.getQuidForDim(finalI).getValue());
                }).values().distinct().count();

            }
            if (quids.get(i).getType().equals("java.lang.String")) {
                countForDim=data.mapToPair(record-> new Tuple2<>(record,(String)record.getQuidForDim(finalI).getValue())).values().distinct().count();

            }
            if (statForDim.containsKey(finalI))
                statForDim.replace(finalI, countForDim);
            else
                statForDim.put(finalI, countForDim);
        }
    }

    private String getSuppresedForPartition(int dim) {
        List<String> strings=recordArrayList.stream().map(record -> (String)record.getSuppressedDim(dim).getValue()).collect(Collectors.toList());
        List<String[]> words=strings.stream().map(s1 ->s1.split("\\s+")).collect(Collectors.toList());
        boolean stop=false;
        int i=0;
        String result="";
        while(!stop && i<words.get(0).length){
            int finalI = i;
            try {
                List<String> first = words.stream().map(array -> array[finalI]).collect(Collectors.toList());
                List<String> distinct = first.stream().distinct().collect(Collectors.toList());
                if (distinct.size() > 1) {
                    stop = true;
                    result += "*";
                } else {
                    result += distinct.get(0) + " ";
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                stop=true;
                result+="*";
            }
            i++;
        }
        return result;
    }



    private int getDimension() {
        long max = 0;
        int key = 1;
        for (Map.Entry<Integer, Long> m : statForDim.entrySet()
        ) {
            if (m.getValue() > max) {
                max = m.getValue();
                key = m.getKey();
            }
        }
        return key;
    }

    @Override
    public String toString() {
        return "Partition{" +
                "recordArrayList=" + recordArrayList +
                '}';
    }
}
