package com.anonymization.mondrian;

import com.anonymization.model.Report;
import com.datastax.spark.connector.japi.CassandraRow;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

public class Anomyzer {

    static final String CASSANDRA_ADDRESS_LOCAL = "127.0.0.1";
    /**
     * Cassandra keyspace name.
     */
    private static String KEYSPACE = "szakdolgozat";
    /**
     * Cassandra table name.
     */
    private static String TABLE = "reports";

    /**
     * Same spark context for each component of the anomyzer module.
     */
    public static JavaSparkContext sc;

    public static void init() {
        SparkConf conf = new SparkConf();
        conf.setAppName("Spark job");
        conf.setMaster("local[*]");
        conf.set("spark.cassandra.connection.host", CASSANDRA_ADDRESS_LOCAL);

        sc = new JavaSparkContext(conf);
    }

    public static void run() {


        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 1; i < 22; i++) {
            ids.add(i);
        }
//        ids.add(1);ids.add(2);ids.add(3);ids.add(4);ids.add(5);

//        List<JavaRDD<Report>> c = ids.stream().map(id -> {
//            return javaFunctions(sc).cassandraTable(KEYSPACE, TABLE,mapRowTo(Report.class)).where(
////                    "id = ?", id);
//                    "anom=false");
//        }).collect(Collectors.toList());

        JavaRDD<Report> unionTable = javaFunctions(sc).cassandraTable(KEYSPACE, TABLE, mapRowTo(Report.class)).where(
                "anom=false"
        ).map(report -> {
            report.addQuids();
            return report;
        });

        Mondrian<Report> mondrian = new Mondrian<>(2, sc, unionTable.coalesce(1));
        Result r = mondrian.runAlgortihm();
        System.out.println(r);
        javaFunctions(r.getResultJavaRDD()).writerBuilder(KEYSPACE, TABLE, mapToRow(Report.class)).saveToCassandra();
//
//        System.out.println(unionTable.coalesce(1).count());
//        System.out.println();
//        List<Report> that=unionTable.coalesce(1).collect();
//        that.get(0).setFreememory_final("11111111");
//        javaFunctions(sc.parallelize(that)).writerBuilder(KEYSPACE,TABLE,mapToRow(Report.class)).saveToCassandra();
        sc.stop();
        sc.close();
    }
}
