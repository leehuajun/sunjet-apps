package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;

/**
 * 服务委托单配件子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmCommissionParts")
public class CommissionPartEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    //    private PartEntity part = new PartEntity();    // 零件
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String partCode;
    private String partName;
    private String partClassify;      //配件分类
    private String unit;            // 计量单位
    private String partSupplyType;  // 供货方式，单选项：调拨、储备、自购
    private String partType;        //零件类型  单选项：配件、辅料
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private Double price = 0.0;     // 单价
    private Integer amount = 0;     // 数量
    private Double total = 0.0;     // 合计
    private Double settlementTotal = 0.0;   // 结算合计
    private String chargeMode;    // 收费方式
    private String comment;     // 备注
    private Boolean recycle = false;    // 是否回收
    private WarrantyMaintenanceEntity warrantyMaintenance;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
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

    @Column(length = 20)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(length = 20)
    public String getPartSupplyType() {
        return partSupplyType;
    }

    public void setPartSupplyType(String partSupplyType) {
        this.partSupplyType = partSupplyType;
    }

    @Column(length = 20)
    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    @Column(length = 50)
    public String getPartClassify() {
        return partClassify;
    }

    public void setPartClassify(String partClassify) {
        this.partClassify = partClassify;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSettlementTotal() {
        return settlementTotal;
    }

    public void setSettlementTotal(Double settlementTotal) {
        this.settlementTotal = settlementTotal;
    }

    @Column(length = 200)
    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
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

    public Boolean getRecycle() {
        return recycle;
    }

    public void setRecycle(Boolean recycle) {
        this.recycle = recycle;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "WarrantyMaintenance")
    public WarrantyMaintenanceEntity getWarrantyMaintenance() {
        return warrantyMaintenance;
    }

    public void setWarrantyMaintenance(WarrantyMaintenanceEntity warrantyMaintenance) {
        this.warrantyMaintenance = warrantyMaintenance;
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
}
