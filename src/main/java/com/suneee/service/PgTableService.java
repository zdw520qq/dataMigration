package com.suneee.service;

import com.suneee.common.Constant;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * @ClassName PgUtils
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 9:38
 * @Version 1.0
 */
public class PgTableService {

    public static List<String> querryPgtables(String ip, String dbname, String username, String passwd) {
        final List<String> tnameList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        final Connection con;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://" + ip + "/" + dbname, username,
                    passwd);
            final Statement statement = con.createStatement();
            //2.要执行的SQL语句
            final String sql = Constant.QUERRYPGTABLE;
            //3.ResultSet类，用来存放获取的结果集！！
            final ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                final String tablename = rs.getString("tablename");
                tnameList.add(tablename);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tnameList;
    }

    /**
     * @Author xingyuan
     * @Description //TODO 根据传进来的ip,选择相应的账号密码
     * @Date 16:14 2019/6/28
     * @Param 
     * @return 
     */
    public static Map<String, String> getUserNamePasswd(String ip) {
        Properties conf = new Properties();
        try {
            InputStream in = PgTableService.class.getClassLoader().getResourceAsStream("erp.properties");
            conf.load(in);
        } catch (Exception e) {
            throw new RuntimeException("config file not found!");
        }
        String username = "";
        String passwd = "";
        if (ip.startsWith("10.6.251.26")) {
            username = conf.getProperty("erp_private_username");
            passwd = conf.getProperty("erp_private_passwd");
        }
        if (ip.startsWith("172.16.54.10") || ip.startsWith("172.16.54.11")) {
            username = conf.getProperty("erp_public_username");
            passwd = conf.getProperty("erp_public_passwd");
        }
        if (ip.startsWith("10.6.242.4")) {
            username = conf.getProperty("jr_username");
            passwd = conf.getProperty("jr_passwd");
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("passwd", passwd);
        
        return map;

    }

    //public static Map<String, Map<String, String>> getPgConfig() {
    //    Map<String, Map<String, String>> map = new HashMap<>();
    //
    //    Properties conf = new Properties();
    //    try {
    //        InputStream in = PgTableService.class.getClassLoader().getResourceAsStream("erp.properties");
    //        conf.load(in);
    //    } catch (Exception e) {
    //        throw new RuntimeException("config file not found!");
    //    }
    //    final Map<String, String> private0Map = new HashMap<>();
    //    final Map<String, String> private1Map = new HashMap<>();
    //    final Map<String, String> public0Map = new HashMap<>();
    //    final Map<String, String> public1Map = new HashMap<>();
    //
    //
    //    private0Map.put("username", conf.getProperty("erp_private_username"));
    //    private0Map.put("passwd", conf.getProperty("erp_private_passwd"));
    //    private0Map.put("ip", conf.getProperty("erp_private_ip5440"));
    //    //private0Map.put("dbname", conf.getProperty("erp_private_passwd"));
    //
    //    private1Map.put("username", conf.getProperty("erp_private_username"));
    //    private1Map.put("passwd", conf.getProperty("erp_private_passwd"));
    //    private1Map.put("ip", conf.getProperty("erp_private_ip5441"));
    //    //private1Map.put("dbname", conf.getProperty("erp_private_passwd"));
    //
    //    public0Map.put("username", conf.getProperty("erp_public_username"));
    //    public0Map.put("passwd", conf.getProperty("erp_public_passwd"));
    //    public0Map.put("ip", conf.getProperty("erp_public_ip5440"));
    //    //public0Map.put("dbname", conf.getProperty("erp_private_passwd"));
    //
    //    public1Map.put("username", conf.getProperty("erp_public_username"));
    //    public1Map.put("passwd", conf.getProperty("erp_public_passwd"));
    //    public1Map.put("ip", conf.getProperty("erp_public_ip5441"));
    //    //public1Map.put("dbname", conf.getProperty("erp_private_passwd"));
    //
    //    map.put("private0Map", private0Map);
    //    map.put("private1Map", private1Map);
    //    map.put("public0Map", public0Map);
    //    map.put("public1Map", public1Map);
    //
    //    return map;
    //}

    //public enum PGENUM {
    //    private0Map,
    //    private1Map,
    //    public0Map,
    //    public1Map;
    //}


}