package com.yitaqi.model;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-20 9:32
 */
public class C3p0Statistics extends BaseStatistics {

    /**
     * 初始化连接池中的连接数
     */
    private int initialPoolSize;
    private int minPoolSize;
    private int maxPoolSize;
    /**
     * 最大空闲时间
     */
    private long maxIdleTime;
    /**
     * c3p0全局的PreparedStatements缓存的大小
     */
    private int maxStatements;
    /**
     * 定义了连接池内单个连接所拥有的最大缓存statements数
     */
    private int maxStatementsPerConnection;
    /**
     * 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException
     */
    private String checkoutTimeout;
    /**
     * 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数
     */
    private int acquireIncrement;
    /**
     * 定义在从数据库获取新连接失败后重复尝试的次数。
     */
    private int acquireRetryAttempts;
    /**
     * 重新尝试的时间间隔
     */
    private int acquireRetryDelay;
    /**
     * 关闭连接时，是否提交未提交的事务
     */
    private boolean autoCommitOnClose;

}
