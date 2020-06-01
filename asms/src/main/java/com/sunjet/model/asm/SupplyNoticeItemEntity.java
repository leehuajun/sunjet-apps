package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.PartEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 调拨通知单物料行
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmSupplyNoticeItems")
public class SupplyNoticeItemEntity extends DocEntity {
    private static final long serialVersionUID = 3390052374653452540L;
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private PartEntity part;        // 零件
    private String partCode;        // 零件号
    private String partName;        // 零件编号
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String pattern;             // 故障模式
    private Double requestAmount = 0.0;       // 需求数量
    private double sentAmount = 0;        // 已供货数量
    private double surplusAmount = 0;    //未供货数量
    private double distributionAmount = 0;    //本次分配数量
    private boolean secondaryDistribution = false; //是否二次分配
    private Date arrivalTime;  //到货时间
    private String comment;     // 备注
    private SupplyNoticeEntity supplyNotice;

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

    @Column(length = 200)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(Double requestAmount) {
        this.requestAmount = requestAmount;
    }

    public double getSentAmount() {
        return sentAmount;
    }

    public void setSentAmount(double sentAmount) {
        this.sentAmount = sentAmount;
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

    public boolean isSecondaryDistribution() {
        return secondaryDistribution;
    }

    public void setSecondaryDistribution(boolean secondaryDistribution) {
        this.secondaryDistribution = secondaryDistribution;
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SupplyNoticeId")
    public SupplyNoticeEntity getSupplyNotice() {
        return supplyNotice;
    }

    public void setSupplyNotice(SupplyNoticeEntity supplyNotice) {
        this.supplyNotice = supplyNotice;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
