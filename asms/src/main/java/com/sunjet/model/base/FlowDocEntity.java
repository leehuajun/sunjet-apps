package com.sunjet.model.base;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 业务基础实体
 */
@MappedSuperclass
public class FlowDocEntity extends DocEntity {
    private static final long serialVersionUID = -3923096345637771677L;

    private String processInstanceId;   // 流程实例Id
    private Integer status = 0;         // 表单状态
    //    private Date submitDate;            // 流程提交时间
    private String submitter;           // 提交人LogId
    private String submitterName;       // 提交人姓名
    private String submitterPhone;      // 提交人电话
    private String handler;             // 当前处理人
    private String docNo;
//    private Boolean autoClose = false;    // 是否自动关闭, 默认为不自动关闭

    @Column(length = 100)
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态值
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(length = 50)
    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    @Column(length = 50)
    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    @Column(length = 50)
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    @Column(length = 32)
    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    @Column(length = 20)
    public String getSubmitterPhone() {
        return submitterPhone;
    }

    public void setSubmitterPhone(String submitterPhone) {
        this.submitterPhone = submitterPhone;
    }

    public FlowDocEntity() {
        super();
    }
}
