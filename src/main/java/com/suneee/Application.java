package com.suneee;

import com.suneee.common.Enums;
import com.suneee.service.HiveTableService;

import java.sql.SQLException;

import static com.suneee.migration.DataMigration.doDataMigration;

/**
 * @ClassName Application
 * @Description TODO 数据迁移 pg --> hive
 * @Author suneee
 * @Date 2019/6/28 15:29
 * @Version 1.0
 */
public class Application {
    public static void main(String[] args) throws SQLException {
        if (args[0] == null || "".equals(args[0])) {
            throw new RuntimeException("参数 ip:端口 不能为空");
        } else if (args[1] == null || "".equals(args[1])) {
            throw new RuntimeException("参数 pg数据库的库名 不能为空");
        }
        final String hiveDB = HiveTableService.getHiveDB(args[0], args[1]);

        //yarn为集群模式,test为local模式
        Enums.moudles moudle = Enums.moudles.yarn;
        //Enums.moudles moudle = Enums.moudles.test;

        //开始数据迁移
        doDataMigration(args[0],args[1],hiveDB,moudle);
    }
}
