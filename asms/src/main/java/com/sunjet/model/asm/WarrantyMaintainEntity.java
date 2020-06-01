package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 维修项目
 */
@Entity
@Table(name = "AsmWarrantyMaintains")
public class WarrantyMaintainEntity extends DocEntity {
    private static final long serialVersionUID = -4637484257477614557L;
    private String code;      // 维修项目编号
    private String name;      // 维修项目名称
    private String measure;   // 维修措施
    private String type;      // 项目类型
    private Double workTime = 0.0;    // 工时定额
    private Double hourPrice = 0.0;   // 工时单价
    private Double total = 0.0;       // 工时费用
    private String comment;           // 备注
    private WarrantyMaintenanceEntity warrantyMaintenance;

    @Column(length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 20)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(length = 200)
    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Double hourPrice) {
        this.hourPrice = hourPrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
    }

    @Column(length = 500)
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
}
