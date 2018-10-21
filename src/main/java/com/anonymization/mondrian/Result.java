

import Anom.Mondrian;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.ArrayList;

public class Result {
    ArrayList<String> resultArrayList;
    JavaRDD<Report> resultJavaRDD;

    public Result() {
        this.resultArrayList = new ArrayList<>();
    }



    public void addAll(ArrayList<Partition> res) {
        for (Partition p : res) {
            resultArrayList.add(p.toString());

        }
        resultJavaRDD=Mondrian.sc.parallelize(res).flatMap((FlatMapFunction<Partition, Report>) partition -> {
            ArrayList<Report> temp=new ArrayList<>();
            partition.getRecordArrayList().forEach(record -> {
                temp.add((Report)record);
            });
            return temp;
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
