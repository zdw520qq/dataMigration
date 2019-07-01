package com.suneee.utils;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

import static com.suneee.common.Constant.NUM;

/**
 * @ClassName MysqlUtils
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 9:38
 * @Version 1.0
 */
public class MysqlUtils {
    private static LinkedList<Connection> connectionQueue;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection getConnection() {


        try {
            if (connectionQueue == null) {
                connectionQueue = new LinkedList<Connection>();
                for (int i = 0;i < NUM;i ++) {
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://172.21.5.1:3306/dfb_daq_yg?characterEncoding=utf8",
                            "dfb_daq",
                            "dfb_daq_abc"
                    );
                    connectionQueue.push(conn);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connectionQueue.poll();
    }

    public static void returnConnection(Connection conn) {
        connectionQueue.push(conn);
    }

    public static void writeLog(String ip,String database,String tablename,int status,String comments) throws SQLException {
        String sql = "insert into daq_yg_log values(?,?,?,?,?,?,?)";
        final Connection connection = getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,ip);
        pst.setString(2,database);
        pst.setString(3,tablename);
        pst.setInt(4,status);
        pst.setString(5,DateUtils.getReqDate(new Date()));
        pst.setString(6,DateUtils.getTimeStampStr(new Date()));
        pst.setString(7,comments);
        final int i = pst.executeUpdate();
        pst.close();
        returnConnection(connection);

    }

}
