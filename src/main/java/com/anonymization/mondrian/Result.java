package com.anonymization.mondrian;

import com.anonymization.model.Report;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Result implements Serializable {
    ArrayList<String> resultArrayList;
    JavaRDD<Report> resultJavaRDD;

    public Result() {
        this.resultArrayList = new ArrayList<>();
    }



    public void addAll(ArrayList<Partition> res) {
        for (Partition p : res) {
            resultArrayList.add(p.toString());

        }
        resultJavaRDD=Mondrian.sc.parallelize(res).flatMap(new FlatMapFunction<Partition, Report>() {
            @Override
            public Iterator<Report> call(Partition partition) throws Exception {
                List<Report> temp=new ArrayList<>();
                partition.getRecordArrayList().forEach(record -> {
                    temp.add((Report)record);
                });
                return temp.iterator();
            }
        });
    }

    public JavaRDD<Report> getResultJavaRDD() {
        return resultJavaRDD;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultArrayList=" + resultArrayList +
                '}';
    }
}
