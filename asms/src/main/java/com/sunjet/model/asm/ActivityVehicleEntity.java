package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.VehicleEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 活动通知车辆子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmActivityVehicles")
public class ActivityVehicleEntity extends DocEntity {
    private static final long serialVersionUID = -4210527209833371297L;
    private Integer rowNum;             // 行号
    private Integer mileage;            // 行驶里程
    private Date repairDate;            // 维修日期
    private VehicleEntity vehicle;      // 车辆
    private Boolean distribute = false; // 是否已分配，默认为false
    private Boolean repair = false;     // 是否已参加维修，默认为false
    private ActivityNoticeEntity activityNotice; //服务活动通知单
    private ActivityMaintenanceEntity activityMaintenance; //活动服务单

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    public Boolean getDistribute() {
        return distribute;
    }

    public void setDistribute(Boolean distribute) {
        this.distribute = distribute;
    }

    public Boolean getRepair() {
        return repair;
    }

    public void setRepair(Boolean repair) {
        this.repair = repair;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "VehicleId")
    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    @ManyToOne
    @JoinColumn(name = "ActivityNoticeId")
    public ActivityNoticeEntity getActivityNotice() {
        return activityNotice;
    }

    public void setActivityNotice(ActivityNoticeEntity activityNotice) {
        this.activityNotice = activityNotice;
    }

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public ActivityMaintenanceEntity getActivityMaintenance() {
        return activityMaintenance;
    }

    public void setActivityMaintenance(ActivityMaintenanceEntity activityMaintenance) {
        this.activityMaintenance = activityMaintenance;
    }
}
