package com.yitaqi.model;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-16 9:45
 */
public class JdbcStatistics extends BaseStatistics {

    private long begin;
    private long end;
    private long useTime;
    /**
     * jdbcUrl
     */
    private String jdbcUrl;
    /**
     * sql 语句
     */
    private String sql;
    /**
     * 数据库名称
     */
    private String dataBaseName;
    private String errorMsg;
    private String errorType;
    /**
     * 是否经过预处理
     */
    private boolean prepared;

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }
}
