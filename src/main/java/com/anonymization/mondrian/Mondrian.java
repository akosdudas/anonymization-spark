package com.anonymization.mondrian;

import com.anonymization.mondrian.Partition;
import com.anonymization.mondrian.Record;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;

/**
 * Implementation of Mondrian multidimensional algorithm.
 */
public class Mondrian<T extends Record> {

    JavaRDD<Record> data;
    ArrayList<Integer> quid = new ArrayList<>();
    ArrayList<Integer> rangesForDimensions = new ArrayList<>();
    Partition startData;
    public static JavaSparkContext sc;


    private int k;

    /**
     * result arrayList of partitions.
     */
    ArrayList<Partition> result = new ArrayList<>();

    public Mondrian(int k, JavaSparkContext sc, JavaRDD<T > data) {
        this.k = k;
        this.sc = sc;

        //just for first version
        quid.add(1);
        quid.add(2);
        setData((JavaRDD<Record>) data);

    }

    public JavaRDD<Record> getData() {
        return data;
    }

    public void setData(JavaRDD<Record> data) {
        this.data = data;
        startData = new Partition(data);
    }


    public Result runAlgortihm() {

        ArrayList<Partition> temp = new ArrayList<>();

        temp.add(startData);

        while (!temp.isEmpty()) {

            Partition toView = temp.remove(0);
            ArrayList<Partition> t=toView.splitToPartitions(k);
            if (t==null) {
                result.add(toView);
            } else {
                temp.addAll(toView.splitToPartitions(k));
            }
        }

        Result res = new Result();
        res.addAll(result);
        return res;
    }




}
