package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.PartEntity;

import javax.persistence.*;

/**
 * 供货单物料列表
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmSupplyItems")
public class SupplyItemEntity extends DocEntity {
    private static final long serialVersionUID = 6763932315816003206L;

    private Integer rowNum;         //行号
    private PartEntity part;        //零件
    private String partCode;        //零件号
    private String partName;        //零件编号
    private Double price;           //价格
    private String logisticsNum;    //物流单号
    private Double amount = 0.0;        //发货数量
    private Double rcvamount = 0.0;     //收货数量
    private Double money = 0.0;         //金额
    private String comment;         //备注

    private SupplyNoticeItemEntity supplyNoticeItem;   // 调拨通知单子行对象
    private SupplyEntity supply;        // 供货单主体
    private SupplyWaitingItemEntity supplyWaitingItem;  // 待发货对象

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "SupplyId")
    public SupplyEntity getSupply() {
        return supply;
    }

    public void setSupply(SupplyEntity supply) {
        this.supply = supply;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "SupplyNoticeItemId")
    public SupplyNoticeItemEntity getSupplyNoticeItem() {
        return supplyNoticeItem;
    }

    public void setSupplyNoticeItem(SupplyNoticeItemEntity supplyNoticeItem) {
        this.supplyNoticeItem = supplyNoticeItem;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PartId")
    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "SupplyWaitingItemId")
    public SupplyWaitingItemEntity getSupplyWaitingItem() {
        return supplyWaitingItem;
    }

    public void setSupplyWaitingItem(SupplyWaitingItemEntity supplyWaitingItem) {
        this.supplyWaitingItem = supplyWaitingItem;
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

    @Column(length = 50)
    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    public Double getRcvamount() {
        return rcvamount;
    }

    public void setRcvamount(Double rcvamount) {
        this.rcvamount = rcvamount;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
