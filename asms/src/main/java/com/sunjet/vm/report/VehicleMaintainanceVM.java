package com.sunjet.vm.report;


import com.sunjet.model.asm.*;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.asm.ActivityMaintenanceService;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyehai02 on 2016-07-13.
 * 配件目录编辑
 */
public class VehicleMaintainanceVM extends FormBaseVM {
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private VehicleService vehicleService;
    private VehicleEntity vehicleEntity;
    private List<VehicleEntity> vehicleEntities = new ArrayList<>();
    private String keyword;
    private boolean warranty = false;    //首保
    private boolean activity = false;     //活动
    private WarrantyMaintainEntity warrantyMaintainEntity;
    private ActivityPartEntity activityNoticePartEntity;
    private ActivityNoticeEntity activityNoticeEntity = new ActivityNoticeEntity();
    private CommissionPartEntity commissionPartEntity = new CommissionPartEntity();

    private WarrantyMaintenanceEntity warrantyMaintenanceEntity;
    private List<WarrantyMaintenanceEntity> warrantyMaintenanceEntitys = new ArrayList<>();

    private ActivityMaintenanceEntity activityMaintenanceEntity;
    private List<ActivityMaintenanceEntity> activityMaintenanceEntitys = new ArrayList<>();

    public CommissionPartEntity getCommissionPartEntity() {
        return commissionPartEntity;
    }

    public void setCommissionPartEntity(CommissionPartEntity commissionPartEntity) {
        this.commissionPartEntity = commissionPartEntity;
    }

    public ActivityNoticeEntity getActivityNoticeEntity() {
        return activityNoticeEntity;
    }

    public void setActivityNoticeEntity(ActivityNoticeEntity activityNoticeEntity) {
        this.activityNoticeEntity = activityNoticeEntity;
    }

    public boolean isWarranty() {
        return warranty;
    }

    public void setWarranty(boolean warranty) {
        this.warranty = warranty;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public ActivityPartEntity getActivityNoticePartEntity() {
        return activityNoticePartEntity;
    }

    public void setActivityNoticePartEntity(ActivityPartEntity activityNoticePartEntity) {
        this.activityNoticePartEntity = activityNoticePartEntity;
    }

    public WarrantyMaintainEntity getWarrantyMaintainEntity() {
        return warrantyMaintainEntity;
    }

    public void setWarrantyMaintainEntity(WarrantyMaintainEntity warrantyMaintainEntity) {
        this.warrantyMaintainEntity = warrantyMaintainEntity;
    }

    public ActivityMaintenanceEntity getActivityMaintenanceEntity() {
        return activityMaintenanceEntity;
    }

    public void setActivityMaintenanceEntity(ActivityMaintenanceEntity activityMaintenanceEntity) {
        this.activityMaintenanceEntity = activityMaintenanceEntity;
    }

    public List<ActivityMaintenanceEntity> getActivityMaintenanceEntitys() {
        return activityMaintenanceEntitys;
    }

    public List<WarrantyMaintenanceEntity> getWarrantyMaintenanceEntitys() {
        return warrantyMaintenanceEntitys;
    }

    public WarrantyMaintenanceEntity getWarrantyMaintenanceEntity() {
        return warrantyMaintenanceEntity;
    }

    public void setWarrantyMaintenanceEntity(WarrantyMaintenanceEntity warrantyMaintenanceEntity) {
        this.warrantyMaintenanceEntity = warrantyMaintenanceEntity;
    }

    public List<VehicleEntity> getVehicleEntities() {
        return vehicleEntities;
    }

    public VehicleEntity getVehicleEntity() {
        return vehicleEntity;
    }

    public void setVehicleEntity(VehicleEntity vehicleEntity) {
        this.vehicleEntity = vehicleEntity;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Init(superclass = true)
    public void init() {
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    //查询车辆信息
    @Command
    @NotifyChange("vehicleEntities")
    public void searchVehicleMaintainance() {
        this.vehicleEntities = vehicleService.findAllByKeyword(this.keyword);
        // this.activityDistributionRequest.getVehicles().clear();
    }

    @Command
    @NotifyChange("*")
    public void selectVehicleMaintainance(@BindingParam("model") VehicleEntity notice) {
//        warrantyMaintenanceEntity=new WarrantyMaintenanceEntity();
//        activityMaintenanceEntity=new ActivityMaintenanceEntity();
        vehicleEntity = notice;
        warrantyMaintenanceEntity = warrantyMaintenanceService.findOneWithVehicleById(notice.getObjId());
        activityMaintenanceEntity = activityMaintenanceService.findOneWithVehicleById(notice.getObjId());
        ActivityNoticeEntity activityNoticeEntity = new ActivityNoticeEntity();
        if (activityMaintenanceEntity != null) {
            activityNoticeEntity = activityNoticeService.findOneWithPartById(activityMaintenanceEntity.getActivityDistribution().getActivityNotice().getObjId());
            activityMaintenanceEntity.getActivityDistribution().setActivityNotice(activityNoticeEntity);
        }
    }

    @Command
    @NotifyChange("*")
    public void selectWarrantyMaintenance(@BindingParam("entity") CommissionPartEntity entity) {
        commissionPartEntity = entity;
        warranty = true;
        activity = false;
    }

    @Command
    @NotifyChange("*")
    public void selectActivityMaintenance(@BindingParam("entity") ActivityPartEntity entity) {
        //activityMaintenanceEntity.getActivityDistribution().getActivityNotice().getActivityParts().clear();
        // ActivityNoticeEntity activityNoticeEntity=new ActivityNoticeEntity();
        this.activityNoticeEntity.getActivityParts().add(entity);
        // activityMaintenanceEntity.getActivityDistribution().getActivityNotice().getActivityParts().add(entity);
        // activityNoticePartEntity=entity;
        warranty = false;
        activity = true;
    }

    @Command
    @NotifyChange("activityNoticeEntity")
    public void deletePart(@BindingParam("model") ActivityPartEntity entity) {
        this.activityNoticeEntity.getActivityParts().remove(entity);
    }

    @Command
    @NotifyChange("keyword")
    public void clearVehicleMaintainance() {
        // Set<ActivityNoticeEntity> activityNoticeEntities=new HashSet<>();
        this.keyword = "";
        // this.noticeEntities.clear();
        //activityDistributionRequest = new ActivityDistributionEntity();

    }

}
