package com.suneee.common;

/**
 * @ClassName Constant
 * @Description TODO
 * @Author suneee
 * @Date 2019/6/28 15:55
 * @Version 1.0
 */
public class Constant {
    /**
     * @Author xingyuan
     * @Description //TODO mysql连接池 初始化的数目
     */
    public static final  int NUM =2;

    /**
     * 查询pg库里面有那些表的SQL
     */
   public static final String QUERRYPGTABLE="select tablename from pg_tables where schemaname='public' " +
            "and tablename not like '%_201%' and tablename not like '%copy' and tablename not like '%-%'";
}
