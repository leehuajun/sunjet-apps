package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.PartEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 待返回故障件
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmRecycleList")
public class RecycleItemEntity extends DocEntity {
    private static final long serialVersionUID = 6763932315816003206L;
    private String logisticsNum;    // 物流单号
    private Date requestDate;       // 开单时间
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private PartEntity part;        // 零件
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private Integer waitAmount = 0;           // 待返数量
    private Integer backAmount = 0;          // 本次返回数量
    private Integer acceptAmount = 0;        // 收货数量
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private String comment;     // 备注
    private RecycleEntity recycleEntity;
    private RecycleNoticeItemEntity recycleNoticeItem;      // 返回通知单的行对象

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PartId")
    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    @Column(length = 50)
    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    @Column(length = 20)
    public String getSrcDocType() {
        return srcDocType;
    }

    public void setSrcDocType(String srcDocType) {
        this.srcDocType = srcDocType;
    }

    @Column(length = 32)
    public String getSrcDocNo() {
        return srcDocNo;
    }

    public void setSrcDocNo(String srcDocNo) {
        this.srcDocNo = srcDocNo;
    }

    @Column(length = 200)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @Column(length = 200)
    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    public Integer getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(Integer waitAmount) {
        this.waitAmount = waitAmount;
    }

    public Integer getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(Integer backAmount) {
        this.backAmount = backAmount;
    }

    public Integer getAcceptAmount() {
        return acceptAmount;
    }

    public void setAcceptAmount(Integer acceptAmount) {
        this.acceptAmount = acceptAmount;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(String warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public String getWarrantyMileage() {
        return warrantyMileage;
    }

    public void setWarrantyMileage(String warrantyMileage) {
        this.warrantyMileage = warrantyMileage;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "Recycle")
    public RecycleEntity getRecycleEntity() {
        return recycleEntity;
    }

    public void setRecycleEntity(RecycleEntity recycleEntity) {
        this.recycleEntity = recycleEntity;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "NoticeItemId")
    public RecycleNoticeItemEntity getRecycleNoticeItem() {
        return recycleNoticeItem;
    }

    public void setRecycleNoticeItem(RecycleNoticeItemEntity recycleNoticeItem) {
        this.recycleNoticeItem = recycleNoticeItem;
    }
}
