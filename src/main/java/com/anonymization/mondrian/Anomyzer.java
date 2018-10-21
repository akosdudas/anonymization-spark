package com.anonymization.mondrian;

import com.datastax.spark.connector.japi.CassandraRow;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;

public class Anomyzer {

    static final String CASSANDRA_ADDRESS_LOCAL = "127.0.0.1";
    /**
     * Cassandra keyspace name.
     */
    private static String KEYSPACE="szakdolgozat";
    /**
     * Cassandra table name.
     */
    private static String TABLE="reports";

    /**
     * Same spark context for each component of the anomyzer module.
     */
    public static JavaSparkContext sc;

    public static void main(String[] args){
        SparkConf conf = new SparkConf();
        conf.setAppName("Spark job");
        conf.setMaster("local[*]");
        conf.set("spark.cassandra.connection.host", CASSANDRA_ADDRESS_LOCAL);

        sc= new JavaSparkContext(conf);

        ArrayList<Integer> ids=new ArrayList<>();
        for (int i=1;i<22;i++){
            ids.add(i);
        }
//        ids.add(1);ids.add(2);ids.add(3);ids.add(4);ids.add(5);

        List<JavaRDD<CassandraRow>> c = ids.stream().map(id -> {
            return javaFunctions(sc).cassandraTable(KEYSPACE, TABLE).where(
                    "id = ?", id);
        }).collect(Collectors.toList());

        JavaRDD<CassandraRow> unionTable = sc.union(c.get(0), c.subList(1, c.size() - 1)).coalesce(8);
        JavaRDD<Report> reportJavaRDD=unionTable.map(new Function<CassandraRow, Report>() {
            @Override
            public Report call(CassandraRow cassandraRow) throws Exception {
                Report report=new Report();
                report.setId(cassandraRow.getInt("id"));
                report.setMessage(cassandraRow.getString("message"));
                report.setCpuname(cassandraRow.getString("cpuname"));
                report.setExceptionobjecttype(cassandraRow.getString("exceptionobjecttype"));
                report.setFreememory(cassandraRow.getLong("freememory"));
                report.setMqarch(cassandraRow.getString("mqarch"));
                report.setMqedition(cassandraRow.getInt("mqedition"));
                report.setMquilang(cassandraRow.getString("mquilang"));
                report.setMqversion(cassandraRow.getString("mqversion"));
                report.setNetfxversions(cassandraRow.getString("netfxversions"));
                report.setOriginalhash(cassandraRow.getString("originalhash"));
                report.setOs(cassandraRow.getString("os"));
                report.setOsarch(cassandraRow.getString("osarch"));
                report.setOsversion(cassandraRow.getString("osversion"));
                report.setTimestamp(cassandraRow.getString("timestamp"));
                report.setVisiblememory(cassandraRow.getLong("visiblememory"));
                report.setStacktrace(cassandraRow.getString("stacktrace"));
                report.setSerialhash(cassandraRow.getString("serialhash"));
                report.addQuids();
                return report;
            }
        });

//        System.out.println(reportJavaRDD.collect());

//        System.out.println("The number of elements in the rdd is: "+reportJavaRDD.count());
//        System.out.println(reportJavaRDD.collect() );
        Mondrian<Report> mondrian=new Mondrian<>(2,sc,reportJavaRDD);
        Result r=mondrian.runAlgortihm();
        System.out.println(r);
    }
}
