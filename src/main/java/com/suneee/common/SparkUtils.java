package com.suneee.common;

import com.suneee.common.Enums;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

/**
 * @ClassName SparkConf
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 13:12
 * @Version 1.0
 */
public class SparkUtils {


    public static SparkConf getSparkConf(Enums.moudles moudle, String appName) {

        SparkConf conf = null;
        if ("yarn" .equals(moudle.toString())) {//yarn模式
            conf = new org.apache.spark.SparkConf().setAppName(appName)
                    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        } else {//local模式
            conf = new org.apache.spark.SparkConf().setMaster("local[1]").setAppName("test_rt_statistics_car_mid");
        }
        return conf;
    }

    public static SparkSession getSparkSession(Enums.moudles moudle, String appName) {

        SparkSession spark = null;
        if ("yarn".equals(moudle.toString()) ) {//yarn模式
            final SparkConf conf = getSparkConf(moudle, appName);
            spark = SparkSession.builder().config(conf)
                    .enableHiveSupport().getOrCreate();
        } else {//local模式
            final SparkConf conf = getSparkConf(moudle, appName);
            spark = SparkSession.builder().config(conf)
                    .config("spark.sql.warehouse.dir",
                            "file:///C:/IdeaProjects/spark2/spark-warehouse")
                    .enableHiveSupport().getOrCreate();
        }
        return spark;
    }

    public static JavaSparkContext getJavaSparkContext(Enums.moudles moudle, String appName) {
        JavaSparkContext jsc = null;
        if ("yarn" .equals(moudle.toString())) {//yarn模式
            final SparkSession spark = getSparkSession(moudle, appName);
            jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
        } else {//local模式
            final SparkSession spark = getSparkSession(moudle, appName);
            jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
        }
        return jsc;
    }

}
