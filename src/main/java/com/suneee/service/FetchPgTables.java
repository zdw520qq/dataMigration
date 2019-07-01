package com.suneee.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.suneee.service.PgTableService.getUserNamePasswd;
import static com.suneee.service.PgTableService.querryPgtables;

/**
 * @ClassName FetchPgTables
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 10:34
 * @Version 1.0
 */
public class FetchPgTables {
    public static List<String> getPgtables(String ip, String dbname) {
        Properties conf = new Properties();
        try {
            InputStream in = FetchPgTables.class.getClassLoader().getResourceAsStream("erp.properties");
            conf.load(in);
        } catch (Exception e) {
            throw new RuntimeException("config file not found!");
        }

        //根据ip判断选择账号密码
        final Map<String, String> userNamePasswd = getUserNamePasswd(ip);
        String username = userNamePasswd.get("username");
        String passwd = userNamePasswd.get("passwd");

        //查询pg库里有哪些表
        final List<String> tableList = querryPgtables(ip, dbname, username, passwd);

        return tableList;
    }

    public static Properties getPgConnection(String ip){
        Properties conf = new Properties();
        try {
            InputStream in = FetchPgTables.class.getClassLoader().getResourceAsStream("erp.properties");
            conf.load(in);
        } catch (Exception e) {
            throw new RuntimeException("config file not found!");
        }

        //根据ip判断选择账号密码
        final Map<String, String> userNamePasswd = getUserNamePasswd(ip);
        String username = userNamePasswd.get("username");
        String passwd = userNamePasswd.get("passwd");

        Properties connectionProperties = new Properties();
        connectionProperties.put("user",username);
        connectionProperties.put("password",passwd);
        connectionProperties.put("driver","org.postgresql.Driver");

        return connectionProperties;
    }



}
