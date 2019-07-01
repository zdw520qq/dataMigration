import com.suneee.common.Enums;
import com.suneee.service.HiveTableService;
import com.suneee.utils.DateUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.suneee.common.SparkUtils.getSparkSession;
import static com.suneee.service.FetchPgTables.getPgConnection;
import static com.suneee.service.FetchPgTables.getPgtables;

/**
 * @ClassName Test001
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 14:19
 * @Version 1.0
 */
public class Test001 {
    public static void main(String[] args) {

        String ip = "172.16.54.10:5440";
        String dbname = "mall-trade";
        //获取sparkSession
        final SparkSession spark = getSparkSession(Enums.moudles.test, "test001");
        spark.sql("set hive.exec.dynamic.partition=true");
        spark.sql("set hive.exec.dynamic.partition.mode=nonstrict");
        //hive库的名字为
        final String hiveDB = HiveTableService.getHiveDB(ip, dbname);

        //获取该数据库下非备份表
        final List<String> pgtables = getPgtables(ip, dbname);
        for (String pgtable : pgtables) {
            System.out.println(pgtable + "  ttttttttttttttttttttttttttttttttttttttttttttttttt");
        }
        String pgUrl = "jdbc:postgresql://" + ip + "/" + dbname;

        //获取pg连接
        final Properties connectionProperties = getPgConnection(ip);

/*        //单表写
        //for循环写入hive
        String table = "system_config_t_10-16";
        Dataset<Row> jdbcDF = spark.read().jdbc(pgUrl, table, connectionProperties).select("*");
        System.out.println(jdbcDF.count() + "   ccccccccccccccccccccccccccccccccccccc");
        jdbcDF.show();

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
                System.out.println(table + "  finished========================================================");
                //MysqlUtils.writeLog(ip,dbname,table,0,"successed");

            } catch (Exception e) {
                e.printStackTrace();
                final String message = e.getMessage();
                //MysqlUtils.writeLog(ip, dbname, table, 1, message);

                System.out.println(table + " failed --------------------------------------------------");
                System.out.println(message + " failed");
            }

        }*/



        //for循环写入hive
        for (String table : pgtables) {
            Dataset<Row> jdbcDF = null;
            try {
                jdbcDF = spark.read().jdbc(pgUrl, table, connectionProperties).select("*");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(table+" readFailed rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
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
                    System.out.println(table + "  finished========================================================");
                    //MysqlUtils.writeLog(ip,dbname,table,0,"successed");

                } catch (Exception e) {
                    //e.printStackTrace();
                    final String message = e.getMessage();
                    //MysqlUtils.writeLog(ip, dbname, table, 1, message);

                    System.out.println(table + " failed --------------------------------------------------");
                    System.out.println(message + " failed");
                }

            }

        }
        spark.stop();
    }


}

