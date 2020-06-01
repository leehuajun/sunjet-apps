package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.PartEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 二次分配列表
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmSupplyDisItems")
public class SupplyDisItemEntity extends DocEntity {
    private static final long serialVersionUID = 3390052374653452540L;
    private String agencyCode;      // 合作商编号　
    private String agencyName;      // 合作商
    // private PartEntity part;        // 零件
    private String partCode;        // 零件号
    private String partName;        // 零件名称
    private double requestAmount;       // 需求数量
    private double sentAmount;          // 已供货数量
    private double surplusAmount;    //未供货数量
    private double distributionAmount = 0;    //本次分配数量
    private Date arrivalTime;     // 到货时间
    private String comment;         // 备注
    private SupplyNoticeItemEntity supplyNoticeItem;                // 调拨需求单 子行对象

    @Column(length = 50)
    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    @Column(length = 100)
    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    @Column(length = 50)
    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    @Column(length = 200)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(double requestAmount) {
        this.requestAmount = requestAmount;
        this.surplusAmount = this.requestAmount - this.sentAmount;
    }

    public double getSentAmount() {
        return sentAmount;
    }

    public void setSentAmount(double sentAmount) {
        this.sentAmount = sentAmount;
        this.surplusAmount = this.requestAmount - this.sentAmount;
    }

    public double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public double getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(double distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


//    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
//    @JoinColumn(name = "partId")
//    public PartEntity getPart() {
//        return part;
//    }
//
//    public void setPart(PartEntity part) {
//        this.part = part;
//    }

    @ManyToOne
    @JoinColumn(name = "SupplyNoticeItemId")
    public SupplyNoticeItemEntity getSupplyNoticeItem() {
        return supplyNoticeItem;
    }

    public void setSupplyNoticeItem(SupplyNoticeItemEntity supplyNoticeItem) {
        this.supplyNoticeItem = supplyNoticeItem;
    }
}

