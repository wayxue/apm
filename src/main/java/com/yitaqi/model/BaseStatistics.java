package com.yitaqi.model;

import com.yitaqi.interfaces.Model;

import java.io.Serializable;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-15 8:11
 */
public class BaseStatistics implements Model,Serializable {

    private long recordTime;
    private String recordModel;
    private String hostIp;
    private String hostName;
    private String traceId;

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordModel() {
        return recordModel;
    }

    public void setRecordModel(String recordModel) {
        this.recordModel = recordModel;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
