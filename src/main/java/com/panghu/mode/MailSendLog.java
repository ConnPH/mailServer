package com.panghu.mode;

import java.io.Serializable;
import java.util.Date;

public class MailSendLog implements Serializable {

    private String msgId;

    private Integer userId;
    private Integer status;
    private String routeKey;
    private String exchange;
    private Integer tryCount;
    private Date tryTime;
    private Date createTime;
    private Date updateTIme;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public Date getTryTime() {
        return tryTime;
    }

    public void setTryTime(Date tryTime) {
        this.tryTime = tryTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTIme() {
        return updateTIme;
    }

    public void setUpdateTIme(Date updateTIme) {
        this.updateTIme = updateTIme;
    }


    @Override
    public String toString() {
        return "MailSendLog{" +
                "msgId='" + msgId + '\'' +
                ", userId=" + userId +
                ", status=" + status +
                ", routeKey='" + routeKey + '\'' +
                ", exchange='" + exchange + '\'' +
                ", tryCount=" + tryCount +
                ", tryTime=" + tryTime +
                ", createTime=" + createTime +
                ", updateTIme=" + updateTIme +
                '}';
    }
}
