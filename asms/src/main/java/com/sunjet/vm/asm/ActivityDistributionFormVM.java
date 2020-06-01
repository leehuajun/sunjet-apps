package com.sunjet.vm.asm;

import com.sunjet.model.asm.*;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.asm.*;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.PartService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * 活动分配单
 * Created by Administrator on 2016/10/26.
 */
public class ActivityDistributionFormVM extends FlowFormBaseVM {
    @WireVariable
    private PartService partService;
    @WireVariable
    private ActivityDistributionService activityDistributionService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private ActivityPartService activityPartService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private SupplyNoticeService supplyNoticeService;

    private Window dialog;
    private ActivityDistributionEntity activityDistributionRequest;
    private ActivityVehicleEntity noticeVehicle;
    private List<DealerEntity> dealers = new ArrayList<DealerEntity>();
    private String keyword = "";
    private List<ActivityNoticeEntity> noticeEntities = new ArrayList<>();
    private ActivityNoticeEntity noticeEntity = new ActivityNoticeEntity();
    private List<VehicleEntity> vehicles = new ArrayList<>();
    private List<ActivityPartEntity> activityParts = new ArrayList<>();
    //private List<String> vinList = new ArrayList<>();


    @Init(superclass = true)
    public void init() {
        this.setBaseService(activityDistributionService);

        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.activityDistributionRequest = activityDistributionService.findOneWithVehicles(this.getBusinessId());
            if (activityDistributionRequest.getActivityNotice() != null) {
                this.noticeEntity = activityNoticeService.findOneWithVehicles(activityDistributionRequest.getActivityNotice().getObjId());
                this.noticeEntity.setActivityParts(activityNoticeService.findOneWithParts(activityDistributionRequest.getActivityNotice().getObjId()).getActivityParts());
            }
            if (this.activityDistributionRequest.getStatus() != 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }
        } else {
            activityDistributionRequest = new ActivityDistributionEntity();
            activityDistributionRequest.setSubmitter(CommonHelper.getActiveUser().getLogId());
            activityDistributionRequest.setSubmitterName(CommonHelper.getActiveUser().getUsername());
            activityDistributionRequest.setSubmitterPhone(CommonHelper.getActiveUser().getPhone());
            this.setReadonly(false);
        }
        this.setEntity(this.activityDistributionRequest);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
    }

    /**
     * 提交、启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {


        //保存活动分配单的VIN码
        Map<String, ActivityVehicleEntity> oldDistributionVIN = new HashMap<>();

        //将vin码保存在Map
        for (ActivityVehicleEntity ave : this.activityDistributionRequest.getActivityVehicles()) {  //获取活动分配车辆
            oldDistributionVIN.put(ave.getVehicle().getVin(), ave);
        }

        //遍历活动通知单,如果有活动分配单有车辆设置为已分配状态
        for (ActivityVehicleEntity ve : this.noticeEntity.getActivityVehicles()) { //获取活动通知车辆
            if (oldDistributionVIN.get(ve.getVehicle().getVin()) != null) {

                ve.setDistribute(true);  // 设置车辆为已分配状态
                activityVehicleService.getRepository().save(ve);
            }
//            } else {
//                ve.setDistribute(false);  // 设置车辆为未分配状态
//                activityVehicleService.getRepository().save(ve);
//            }
        }
        if (this.activityDistributionRequest.getSupplyNoticeId() != null) {
            this.activityDistributionRequest.setCanEditSupply(false);
        }

        //if (!StringUtils.isBlank(this.activityDistributionRequest.getSupplyNoticeId())) {
        //    SupplyNoticeEntity supplyNoticeEntity = supplyNoticeService.findOneById(this.activityDistributionRequest.getSupplyNoticeId());
        //    if (!supplyNoticeEntity.getStatus().equals(DocStatus.CLOSED)) {
        //


        //    }
        //}

        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    startProcessInstance(null);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                } else {
                    return;
                }
            }
        });

//    generateSupplyNotice();
    }

    @Command
    @NotifyChange("dealers")
    public void searchDealers(@BindingParam("model") String keyword) {
//    this.dealers = dealerService.findAllByKeyword(keyword.trim());
        this.dealers = dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword.trim());
    }

    @Command
    @NotifyChange("activityDistributionRequest")
    public void clearSelectedDealer() {
        activityDistributionRequest.setDealerCode("");
        activityDistributionRequest.setDealerName("");
//        activityDistributionRequest.setProvinceName("");
        activityDistributionRequest.setDealer(null);
    }

    @Command
    @NotifyChange("activityDistributionRequest")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        activityDistributionRequest.setDealer(dealer);
        activityDistributionRequest.setDealerCode(dealer.getCode());
        activityDistributionRequest.setDealerName(dealer.getName());
        activityDistributionRequest.setServiceManager(dealer.getServiceManager().getName());
//        activityDistributionRequest.setProvinceName(dealer.getProvince().getName());
        activityDistributionRequest.setSubmitterPhone(dealer.getPhone());
        //activityDistributionRequest.setDealer(dealer);
        this.keyword = "";
        this.dealers.clear();
    }

    @Command
    @NotifyChange("activityDistributionRequest")
    public void clearSelectedActivityNotice() {
        // Set<ActivityNoticeEntity> activityNoticeEntities=new HashSet<>();
        this.noticeEntities.clear();
        //activityDistributionRequest = new ActivityDistributionEntity();
        activityDistributionRequest.getActivityVehicles().clear();
        activityDistributionRequest.setActivityNotice(null);

    }

    @Command
    @NotifyChange("noticeEntities")
    public void searchActivityNotices() {
        this.noticeEntities = activityNoticeService.findAllByStatusAndKeyword(DocStatus.CLOSED, keyword.trim());
        this.activityDistributionRequest.getActivityVehicles().clear();

    }

    /**
     * 清除活动单
     */
    @Command
    @NotifyChange("activityDistributionRequest")
    public void clearSelectedActivity() {
//        activityDistributionRequest.getActivityNotice().setDocNo("");
        activityDistributionRequest.setActivityNotice(null);
//        activityDistributionRequest.setDealerName("");
//        activityDistributionRequest.setProvinceName("");
    }

    /**
     * 打开车辆选择框
     */
    @Command
    public void openSelectVehicleForm() {
        if (this.activityDistributionRequest.getActivityNotice() == null) {
            ZkUtils.showExclamation("请先选择活动通知单！", "系统提示");
            return;
        }
        //this.vinList.clear();
        this.vehicles.clear();
        //获取活动车辆
        for (ActivityVehicleEntity av : this.activityDistributionRequest.getActivityVehicles()) {
            //this.vinList.add(av.getVehicle().getVin());
            this.vehicles.add(av.getVehicle());
        }
        Map<String, Object> map = new HashMap<>();
        //map.put("vins", this.vinList);
        map.put("vehcles", this.vehicles);
        map.put("activityNoticeId", this.activityDistributionRequest.getActivityNotice().getObjId());
        dialog = (Window) ZkUtils.createComponents("/views/asm/activity_distribution_select_vehicle.zul", null, map);
        dialog.doModal();
    }

    @GlobalCommand(GlobalCommandValues.ACTIVITY_DISTRIBUTION_SELECT_VEHICLE)
    @NotifyChange("activityDistributionRequest")
    public void returnSelectVehicles(@BindingParam("vehicleList") List<VehicleEntity> vehicles) {
//        ZkUtils.showError(String.valueOf(vins.size()),"车辆数量");
        this.activityDistributionRequest.getActivityVehicles().clear();
        for (VehicleEntity vehice : vehicles) {
//      VehicleEntity v = this.vehicleService.findOneByVin(vin);
            ActivityVehicleEntity av = new ActivityVehicleEntity();
            av.setVehicle(vehice);
            this.activityDistributionRequest.getActivityVehicles().add(av);
        }

        dialog.detach();
    }

    /**
     * 选择活动通知单
     */
    @Command
    @NotifyChange({"*"})
    public void selectActivityNotice(@BindingParam("model") ActivityNoticeEntity notice) {
        this.noticeEntity = activityNoticeService.findOneWithVehicles(notice.getObjId());
        this.noticeEntity.setActivityParts(activityNoticeService.findOneWithParts(notice.getObjId()).getActivityParts());

        this.activityDistributionRequest.setActivityNotice(this.noticeEntity);
        this.keyword = "";
        this.noticeEntities.clear();
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (this.activityDistributionRequest.getStatus() != DocStatus.DRAFT.getIndex()) {
            ZkUtils.showInformation("单据状态非[" + DocStatus.DRAFT.getName() + "]状态,不能保存！", "提示");
            return;
        }
        if (this.activityDistributionRequest.getDealerCode() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        if (this.activityDistributionRequest.getActivityVehicles().size() < 1) {
            ZkUtils.showInformation("请添加车辆！", "提示");
            return;
        }

        //保存活动分配单的VIN码
        Map<String, ActivityVehicleEntity> oldDistributionVIN = new HashMap<>();

        //将vin码保存在Map
        for (ActivityVehicleEntity ave : this.activityDistributionRequest.getActivityVehicles()) {  //获取活动分配车辆
            oldDistributionVIN.put(ave.getVehicle().getVin(), ave);
        }

        //遍历活动通知单,如果有活动分配单有车辆设置为已分配状态
        for (ActivityVehicleEntity ve : this.noticeEntity.getActivityVehicles()) { //获取活动通知车辆
            if (oldDistributionVIN.get(ve.getVehicle().getVin()) != null) {
                ve.setDistribute(true);  // 设置车辆为已分配状态
                activityVehicleService.getRepository().save(ve);
            }

        }

        // 新增
        if (StringUtils.isBlank(this.activityDistributionRequest.getObjId())) {
            this.setEntity(this.saveEntity(this.activityDistributionRequest));
        } else {
            // 获取原始数据
            ActivityDistributionEntity oldDistribution = activityDistributionService.findOneWithVehicles(this.activityDistributionRequest.getObjId());
            FlowDocEntity entity = this.saveEntity(this.activityDistributionRequest);
            this.activityDistributionRequest = activityDistributionService.findOneWithVehicles(entity.getObjId());
            this.setEntity(this.activityDistributionRequest);



        }
        showDialog();

    }

    @Override
    protected Boolean checkValid() {
        if (this.activityDistributionRequest.getDealer() == null) {
            ZkUtils.showInformation("请选择服务站信息！", "提示");
            return false;
        }

        if (this.activityDistributionRequest.getActivityVehicles().size() == 0) {
            ZkUtils.showInformation("请添加车辆信息！", "提示");
            return false;
        }

        List<ActivityPartEntity> activityPartEntities = activityPartService.findActivityPartListByNoticeId(this.activityDistributionRequest.getSupplyNoticeId());
        if (activityPartEntities.size() > 0) {
            if (this.activityDistributionRequest.getSupplyNoticeId() == null) {
                ZkUtils.showInformation("请生成调拨单在提交", "提示");
                return false;
            }
        }

        return true;
    }

    /**
     * 删除车辆行
     *
     * @param vehicleEntity
     */
    @Command
    @NotifyChange("*")
    public void deleteVehicle(@BindingParam("model") ActivityVehicleEntity vehicleEntity) {
        if(StringUtils.isNotBlank(this.activityDistributionRequest.getObjId())){
            ActivityDistributionEntity activityDistributionEntity = this.activityDistributionService.findOneWithVehicles(this.activityDistributionRequest.getObjId());
            if(!activityDistributionEntity.getStatus().equals(DocStatus.DRAFT.getIndex())){
                ZkUtils.showInformation("非草稿单据不能删除","提示");
                return;
            }
            this.activityDistributionService.save(this.activityDistributionRequest);
            // 如果已有ID，表示已存在数据库了
            if (vehicleEntity.getObjId() != null) {
                activityVehicleService.getRepository().delete(vehicleEntity.getObjId());
            }

            ActivityDistributionEntity oneWithVehicles = this.activityDistributionService.findOneWithVehicles(this.activityDistributionRequest.getObjId());
            this.activityDistributionRequest.setActivityVehicles(oneWithVehicles.getActivityVehicles());
            this.activityDistributionService.save(oneWithVehicles);

            //遍历活动通知单,如果有活动分配单有车辆设置为未已分配状态
            for (ActivityVehicleEntity ve : this.noticeEntity.getActivityVehicles()) { //获取活动通知车辆
                if (ve.getVehicle().getVin().equals(vehicleEntity.getVehicle().getVin())) {
                    ve.setDistribute(false);  // 设置车辆为未分配状态
                    activityVehicleService.getRepository().save(ve);
                }
            }

        }

        this.activityDistributionRequest.getActivityVehicles().remove(vehicleEntity);




    }


    /**
     * 生成活动调拨单
     */
    @Command
    public void generateSupplyNotice() {
        ActivityDistributionEntity activityDistributionEntity = this.activityDistributionService.findOneWithVehicles(this.activityDistributionRequest.getObjId());
        Set<ActivityPartEntity> parts = this.noticeEntity.getActivityParts();
        if (parts.size() < 1) {
            ZkUtils.showInformation("没有配件不能生成调拨单", "提示");
            return;
        }
        if (activityDistributionEntity.getStatus().equals(DocStatus.DRAFT.getIndex())) {
            ZkUtils.showError("活动分配单未提交，不能生成调拨单", "系统提示");
            return;
        }
        if (StringUtils.isNotBlank(activityDistributionRequest.getSupplyNoticeId())) {
            ZkUtils.showError("不允许重复生成调拨通知单/供货通知单", "系统提示");
            return;
        }
        if (activityDistributionRequest.getDealer() == null) {
            return;
        }
        SupplyNoticeEntity supplyNoticeEntity = new SupplyNoticeEntity();
        supplyNoticeEntity.setSrcDocNo(activityDistributionRequest.getDocNo());
        supplyNoticeEntity.setSrcDocType("活动分配单");
        supplyNoticeEntity.setSrcDocID(activityDistributionRequest.getObjId());
        supplyNoticeEntity.setDealerCode(activityDistributionRequest.getDealerCode());
        supplyNoticeEntity.setDealerName(activityDistributionRequest.getDealerName());
        supplyNoticeEntity.setProvinceName(activityDistributionRequest.getDealer().getProvince().getName());
        supplyNoticeEntity.setServiceManager(activityDistributionRequest.getServiceManager());
        supplyNoticeEntity.setSubmitter(CommonHelper.getActiveUser().getLogId());
        supplyNoticeEntity.setSubmitterName(CommonHelper.getActiveUser().getUsername());
        supplyNoticeEntity.setSubmitterPhone(CommonHelper.getActiveUser().getPhone());
//        supplyNoticeEntity.setOperatorPhone(warrantyMaintenanceRequest.getOperatorPhone());

        List<SupplyNoticeItemEntity> noticeParts = new ArrayList<>();
        for (ActivityPartEntity entity : parts) {

            SupplyNoticeItemEntity item = new SupplyNoticeItemEntity();
            item.setPart(entity.getPart());
            item.setPartCode(entity.getPart().getCode());
            item.setPartName(entity.getPart().getName());
            item.setRequestAmount(Double.valueOf(entity.getAmount()) * this.activityDistributionRequest.getActivityVehicles().size());
            item.setSurplusAmount(Double.valueOf(entity.getAmount()) * this.activityDistributionRequest.getActivityVehicles().size());
            item.setWarrantyTime(entity.getPart().getWarrantyTime());
            item.setWarrantyMileage(entity.getPart().getWarrantyMileage());
            noticeParts.add(item);

        }

        for (SupplyNoticeItemEntity item : noticeParts) {
            supplyNoticeEntity.addNoticeItem(item);
        }

        this.setBaseService(supplyNoticeService);
        this.setEntity(supplyNoticeEntity);
        supplyNoticeService.getRepository().save(supplyNoticeEntity);
        supplyNoticeEntity = (SupplyNoticeEntity) this.saveEntity(supplyNoticeEntity);

        this.setBaseService(this.activityDistributionService);
        this.setEntity(this.activityDistributionRequest);
        this.activityDistributionRequest.setSupplyNoticeId(supplyNoticeEntity.getObjId());
        this.activityDistributionRequest.setCanEditSupply(false);
        activityDistributionService.save(this.activityDistributionRequest);

        ZkUtils.showInformation("已生成调拨单,请到调拨单列表中查看和提交。", "系统提示");
    }

    /**
     * 作废单据
     */
    @Override
    public void desert() {
        if(this.activityDistributionRequest.getCanEditSupply().equals(false)){
            ZkUtils.showInformation("已经生成调拨单不能作废","提示");
            return;
        }
        super.desert();
        //保存活动分配单的VIN码
        Map<String, ActivityVehicleEntity> oldDistributionVIN = new HashMap<>();
        for (ActivityVehicleEntity ave : activityDistributionRequest.getActivityVehicles()) {
            oldDistributionVIN.put(ave.getVehicle().getVin(), ave);
        }
        this.noticeEntity = activityNoticeService.findOneWithVehicles(activityDistributionRequest.getActivityNotice().getObjId());
        //遍历活动通知单,如果有活动分配单有车辆设置为已分配状态
        for (ActivityVehicleEntity ve : this.noticeEntity.getActivityVehicles()) { //获取活动通知车辆
            if (oldDistributionVIN.get(ve.getVehicle().getVin()) != null) {
                ve.setDistribute(false);  // 设置车辆为未已分配状态
                activityVehicleService.getRepository().save(ve);
            }
        }
    }

    @Override
    public Boolean checkCanEditSupply() {
        return this.activityDistributionRequest.getCanEditSupply();
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ActivityDistributionEntity getActivityDistributionRequest() {
        return activityDistributionRequest;
    }

    public void setActivityDistributionRequest(ActivityDistributionEntity activityDistributionRequest) {
        this.activityDistributionRequest = activityDistributionRequest;
    }

    public ActivityVehicleEntity getNoticeVehicle() {
        return noticeVehicle;
    }

    public void setNoticeVehicle(ActivityVehicleEntity noticeVehicle) {
        this.noticeVehicle = noticeVehicle;
    }

    public List<DealerEntity> getDealers() {
        return dealers;
    }

    public void setDealers(List<DealerEntity> dealers) {
        this.dealers = dealers;
    }

    public List<ActivityNoticeEntity> getNoticeEntities() {
        return noticeEntities;
    }

    public void setNoticeEntities(List<ActivityNoticeEntity> noticeEntities) {
        this.noticeEntities = noticeEntities;
    }

    public ActivityNoticeEntity getNoticeEntity() {
        return noticeEntity;
    }

    public void setNoticeEntity(ActivityNoticeEntity noticeEntity) {
        this.noticeEntity = noticeEntity;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }

    public List<ActivityPartEntity> getActivityParts() {
        return activityParts;
    }

    public void setActivityParts(List<ActivityPartEntity> activityParts) {
        this.activityParts = activityParts;
    }
}
