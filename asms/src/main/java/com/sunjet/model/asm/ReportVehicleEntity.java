package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.VehicleEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 车辆子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmReportVehicles")
public class ReportVehicleEntity extends DocEntity {

    private static final long serialVersionUID = -4210527209833371297L;
    private VehicleEntity vehicle;      // 车辆
    private String mileage;            // 行驶里程
    private Date repairDate = new Date();            // 报修日期
    private String comment;             // 其它，备注

    private ExpenseReportEntity expenseReportEntity; //费用速报
    private QualityReportEntity qualityReportEntity; //质量速报

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "VehicleId")
    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "CrId")
    public ExpenseReportEntity getExpenseReportEntity() {
        return expenseReportEntity;
    }

    public void setExpenseReportEntity(ExpenseReportEntity expenseReportEntity) {
        this.expenseReportEntity = expenseReportEntity;
    }

    @ManyToOne
    @JoinColumn(name = "QrId")
    public QualityReportEntity getQualityReportEntity() {
        return qualityReportEntity;
    }

    public void setQualityReportEntity(QualityReportEntity qualityReportEntity) {
        this.qualityReportEntity = qualityReportEntity;
    }

}
