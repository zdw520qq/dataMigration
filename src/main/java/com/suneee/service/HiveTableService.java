package com.suneee.service;

/**
 * @ClassName HiveTableService
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 14:34
 * @Version 1.0
 */
public class HiveTableService {
    /**
     * @Author xingyuan
     * @Description //TODO 获取hive对应pg的数据库名称
     * @Date 14:42 2019/6/28
     * @Param [ip, dbname]
     * @return java.lang.String
     */
    public static String getHiveDB(String ip, String dbname) {

        String hiveDbName = "";
        if (ip.startsWith("10.6.251.26")) {
            hiveDbName = "dfb_yg_private_" + dbname.replaceAll("-", "_") + "_ods";
        }
        if (ip.startsWith("172.16.54.10") || ip.startsWith("172.16.54.11")) {
            hiveDbName = "dfb_yg_public_" + dbname.replaceAll("-", "_") + "_ods";
        }
        if (ip.startsWith("10.6.242.4")) {
            hiveDbName = "dfb_yg_jr_" + dbname.replaceAll("-", "_") + "_ods";
        }
        return hiveDbName;
    }

    /**
     * @Author xingyuan
     * @Description //TODO 获取hive对应pg的表名
     * @Date 14:45 2019/6/28
     * @Param [tableName]
     * @return java.lang.String
     */
    public static String getHiveTable(String tableName) {

        return tableName.replaceAll("-", "_");
    }


}
