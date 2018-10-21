package com.anonymization.mondrian;

import Anom.Mondrian;
import com.anonymization.mondrian.Record;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;

public class  Partition implements Serializable {

    private List<Record> recordArrayList = new ArrayList<>();


    private ArrayList<Integer> quid = new ArrayList<>();
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
        long min = (long)recordArrayList.get(0).getQuidForDim(dimension).getValue();
//        int max = recordArrayList.get(0).getForDim(dimension);
        long max = (long) recordArrayList.get(0).getQuidForDim(dimension).getValue();
        for (Record r : recordArrayList) {
//            if (r.getForDim(dimension) > max) {
            if ((long) r.getQuidForDim(dimension).getValue() > max) {
//                max = r.getForDim(dimension);
                max = (long) r.getQuidForDim(dimension).getValue();

            }
//            if (r.getForDim(dimension) < min) {
            if ((long) r.getQuidForDim(dimension).getValue() < min) {
//                min = r.getForDim(dimension);
                min = (long) r.getQuidForDim(dimension).getValue();
            }
        }
        String s = "[" + min + "," + max + "]";
        rangeFordim.put(dimension, s);
    }

    public ArrayList<Partition> splitToPartitions(int k) {

        ArrayList<Partition> tmp = new ArrayList<>();
        updateStatistics();
        if (data.count() < 2 * k) {
            recordArrayList = data.collect();
            getRangeForDim(1);
            getRangeForDim(2);
            for (Record mr : recordArrayList
            ) {
                mr.setFinalData(1, rangeFordim.get(1));
                mr.setFinalData(2, rangeFordim.get(2));
            }

            return null;
        }

        int dimension = getDimension();
        System.out.println("The dimension is: "+dimension);
        JavaRDD<Record> dimRecordJavaPairRDD;

        dimRecordJavaPairRDD = data.mapToPair((PairFunction<Record, Long, Record>)
                //record -> new Tuple2<>(record.getForDim(dimension), record)).sortByKey(true).values().cache();
                record -> {
                    long val=(long)record.getQuidForDim(dimension).getValue();
                    return new Tuple2<>( val, record);


                }).sortByKey(true).values().cache();

        long count = dimRecordJavaPairRDD.count();

        List<Record> left = dimRecordJavaPairRDD.take((int) count / 2);
        long median=(long)left.get(left.size()-1).getQuidForDim(dimension).getValue();
        Partition lf = new Partition(Mondrian.sc.parallelize(left));
        //Partition rf = new Partition(dimRecordJavaPairRDD.subtract(Mondrian.sc.parallelize(left)));
        Partition rf=new Partition(dimRecordJavaPairRDD.filter((Function<Record, Boolean>) record -> (long)record.getQuidForDim(dimension).getValue()>median));
        dimRecordJavaPairRDD.unpersist();
        System.out.println("The left side is :\n" + lf.data.count());
        System.out.println("\n\nThe right side is: \n" + rf.data.count());

        tmp.add(lf);
        tmp.add(rf);

        return tmp;
    }

    private void updateStatistics() {
        if (data == null) return;
        for (int i = 0; i < quid.size(); i++) {

            final int finalI = i;

            long countForDim = data.mapToPair(new PairFunction<Record, Record, Integer>() {
                @Override
                public Tuple2<Record, Integer> call(Record medicalRecord) throws Exception {
                    //return new Tuple2<>(medicalRecord, medicalRecord.getForDim(quid.get(finalI)));
                    return new Tuple2<>(medicalRecord, (int) medicalRecord.getQuidForDim(quid.get(finalI)).getValue());
                }
            }).values().distinct().count();

            if (statForDim.containsKey(quid.get(i)))
                statForDim.replace(quid.get(i), countForDim);
            else
                statForDim.put(quid.get(i), countForDim);
        }
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
