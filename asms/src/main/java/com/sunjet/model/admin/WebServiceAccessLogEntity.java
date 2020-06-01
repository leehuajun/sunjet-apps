package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lhj on 16/6/27.
 * Web服务访问日志实体
 */
@Entity
@Table(name = "SysWebserviceAccessLogs")
public class WebServiceAccessLogEntity extends DocEntity {
    private static final long serialVersionUID = -12032028016000298L;
    /**
     * 接口类型
     */
    private Integer type;
    /**
     * 操作类型
     */
    private Integer operateType;
    /**
     * 调用开始时间
     */
    private Date beginTime;
    /**
     * 调用结束时间
     */
    private Date endTime;
    /**
     * 调用参数
     */
    private String param;
    /**
     * 失败原因
     */
    private String failResult;
    /**
     * 状态（成功/失败）
     */
    private Integer status;

    @Column(name = "ObjType")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "OperateType")
    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    @Column(name = "BeginTime")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Column(name = "EndTime")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "Param")
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Column(name = "FailResult")
    public String getFailResult() {
        return failResult;
    }

    public void setFailResult(String failResult) {
        this.failResult = failResult;
    }

    @Column(name = "Status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

//    @Override
//    public void save() {
//        if (this.failResult != null && this.failResult.length() > 1000) {
//            this.failResult = this.failResult.substring(0, 1000);
//        }
//        super.save();
//    }


    public WebServiceAccessLogEntity() {
    }

    public WebServiceAccessLogEntity(Integer type, Integer operateType, Date beginTime, Date endTime, String param, String failResult, Integer status) {
        this.type = type;
        this.operateType = operateType;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.param = param;
        this.failResult = failResult;
        this.status = status;
    }
}