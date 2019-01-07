package com.anonymization.mondrian;


import com.anonymization.model.Report;
import com.datastax.spark.connector.japi.CassandraRow;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

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

        if (sc==null) {
            SparkConf conf = new SparkConf();
            conf.setAppName("Spark job");
            conf.setMaster("local[*]");
            conf.set("spark.cassandra.connection.host", CASSANDRA_ADDRESS_LOCAL);
            conf.set("spark.executor.instances", "4");
            conf.set("spark.executor.cores", "5");

            sc = new JavaSparkContext(conf);
            sc.setLogLevel("ERROR");
        }
    }

    public static void run() {

        JavaRDD<Report> unionTable = javaFunctions(sc).cassandraTable(KEYSPACE, TABLE, mapRowTo(Report.class)).where(
                "anom=false"
        ).map(report -> {
            report.addQuids();
            return report;
        });

        if (unionTable.count() == 0)
            return;
        Mondrian<Report> mondrian = new Mondrian<>(10, sc, unionTable);
        Result r = mondrian.runAlgortihm();
        javaFunctions(r.getResultJavaRDD()).writerBuilder(KEYSPACE, TABLE, mapToRow(Report.class)).saveToCassandra();

    }

    public static void stop() {
        sc.stop();
        sc.close();
    }
}
