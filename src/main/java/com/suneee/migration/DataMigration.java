package com.suneee.migration;

import com.suneee.common.Enums;
import com.suneee.service.HiveTableService;
import com.suneee.utils.DateUtils;
import com.suneee.utils.MysqlUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.suneee.service.FetchPgTables.getPgConnection;
import static com.suneee.common.SparkUtils.getSparkSession;
import static com.suneee.service.FetchPgTables.getPgtables;

/**
 * @ClassName DataMigration
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 10:25
 * @Version 1.0
 */
public class DataMigration {

    public static void doDataMigration(String ip, String dbname, String appName, Enums.moudles moudle) throws SQLException {

        //获取sparkSession
        final SparkSession spark = getSparkSession(moudle, appName);

        spark.sql("set hive.exec.dynamic.partition=true");
        spark.sql("set hive.exec.dynamic.partition.mode=nonstrict");
        spark.sql("set hive.merge.mapfiles=true");
        spark.sql("set hive.merge.mapredfiles=true");
        spark.sql("set hive.merge.smallfiles.avgsize=16000000");
        spark.sql("set hive.merge.size.per.task=256000000");
        spark.sql("set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat");
        spark.sql("set mapred.max.split.size=256000000");
        spark.sql("set mapred.min.split.size.per.node=100000000");
        spark.sql("set mapred.min.split.size.per.rack=100000000");


        //hive库的名字为
        final String hiveDB = HiveTableService.getHiveDB(ip, dbname);

        //获取该数据库下非备份表
        final List<String> pgtables = getPgtables(ip, dbname);

        String pgUrl = "jdbc:postgresql://" + ip + "/" + dbname;

        //获取pg连接
        final Properties connectionProperties = getPgConnection(ip);

        //for循环写入hive
        for (String table : pgtables) {
            Dataset<Row> jdbcDF = null;
            try {
                jdbcDF = spark.read().jdbc(pgUrl, table, connectionProperties).select("*");
            } catch (Exception e) {
                //e.printStackTrace();
                final String message = e.getMessage();
                MysqlUtils.writeLog(ip, dbname, table, 1, message);

            }
            if (null == jdbcDF || jdbcDF.count() == 0) {
                continue;
            }
            if (jdbcDF.count() != 0) {
                //得到hive相对应pg的表名
                final String hiveTable = HiveTableService.getHiveTable(table);
                //分区字段
                final String data_date = DateUtils.getReqDate(new Date());
                //记录时间
                String record_time = DateUtils.getTimeStampStr(new Date());

                String tmpTableName = "tmp_" + table;
                //注册为临时表
                jdbcDF.createOrReplaceTempView(tmpTableName);
                try {
                    spark.sql("insert overwrite table " + hiveDB + "." + hiveTable + " PARTITION (data_date) select *,'"
                            + record_time + "','" + data_date + "' from " + tmpTableName);
                    //System.out.println(table + "  finished========================================================");
                    //MysqlUtils.writeLog(ip,dbname,table,0,"successed");

                } catch (Exception e) {
                    //e.printStackTrace();
                    final String message = e.getMessage();
                    MysqlUtils.writeLog(ip, dbname, table, 1, message);

                    //System.out.println(table + " failed --------------------------------------------------");
                    //System.out.println(message + " failed");
                }

            }

        }

        spark.stop();
    }

    //private static writeHive
}
