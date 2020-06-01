package com.sunjet.vm.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityPartEntity;
import com.sunjet.model.asm.ActivityVehicleEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.service.basic.PartService;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.MathHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 服务活动通知单 表单 VM
 */
public class ActivityNoticeFormVM extends FlowFormBaseVM {
    @WireVariable
    private PartService partService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private VehicleService vehicleService;

    private ActivityNoticeEntity activityNoticeRequest;
    private ActivityPartEntity activityPartEntity;//配件
    private List<ActivityPartEntity> activityNoticePartlist = new ArrayList<>();
    private ActivityVehicleEntity activityVehicleEntity;
    private List<ActivityVehicleEntity> activityNoticeVehiclelist = new ArrayList<>();
    private List<ActivityPartEntity> activityNoticePartEntities = new ArrayList<>();
    private List<PartEntity> partList = new ArrayList<>();
    private List<VehicleEntity> vehicles = new ArrayList<>();
    private PartEntity currentproduct = null;
    private String type = "vehicle";
    private List<ActivityVehicleEntity> activityNoticeVehicleEntities = new ArrayList<>();
    //    private List<VehicleEntity> variables = new ArrayList<>();
    private List<VehicleEntity> vehicleListDelete = new ArrayList<>();
    private String keyword = "";
    private List<String> vinList = new ArrayList<>();
    private Window window;

    private Window dialog;

    @Init(superclass = true)
    public void init() {
        this.setBaseService(activityNoticeService);
//        activityVehicleEntity = new ActivityVehicleEntity();
//        activityNoticeRequest = (ActivityNoticeEntity) Executions.getCurrent().getArg().get("model");
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            //this.activityNoticeRequest = activityNoticeService.findOneWithVehiclesAndParts(this.getBusinessId());
            this.activityNoticeRequest = activityNoticeService.findOneWithVehicles(this.getBusinessId());
            this.activityNoticeRequest.setActivityParts(activityNoticeService.findOneWithParts(this.getBusinessId()).getActivityParts());

            if (this.activityNoticeRequest.getStatus() != 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }
        } else {        // 业务对象和业务对象Id都为空
            activityNoticeRequest = new ActivityNoticeEntity();
            activityNoticeRequest.setSubmitter(CommonHelper.getActiveUser().getLogId());
            activityNoticeRequest.setSubmitterName(CommonHelper.getActiveUser().getUsername());
            activityNoticeRequest.setSubmitterPhone(CommonHelper.getActiveUser().getPhone());
            this.activityNoticeRequest.setActivityParts(new HashSet<>());
            this.activityNoticeRequest.setActivityVehicles(new HashSet<ActivityVehicleEntity>());
            this.setReadonly(false);
        }
        this.setEntity(activityNoticeRequest);

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
        this.setEntity(activityNoticeRequest);
    }

    @Override
    @Command
//    @NotifyChange("activityNoticeRequest")
    public void saveFlowDocEntity() {
        FlowDocEntity entity = this.saveEntity(this.activityNoticeRequest);
        this.activityNoticeRequest = activityNoticeService.findOneWithVehiclesAndParts(entity.getObjId());
        this.setEntity(this.activityNoticeRequest);
//        this.setEntity(this.saveEntity(this.activityNoticeRequest));
        showDialog();
    }

    @Override
    protected Boolean checkValid() {
        if (this.activityNoticeRequest.getTitle() == null || this.activityNoticeRequest.getTitle().trim().length() < 1) {
            ZkUtils.showInformation("请填写标题信息！", "提示");
            return false;
        }

        if (this.activityNoticeRequest.getStartDate().getTime() > this.activityNoticeRequest.getEndDate().getTime()) {
            ZkUtils.showInformation("开始时间不能大于结束时间", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityNoticeRequest.getNoticeFile())) {
            ZkUtils.showInformation("活动文件为上传", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityNoticeRequest.getTitle())
                || StringUtils.isBlank(this.activityNoticeRequest.getComment())) {
            ZkUtils.showInformation("请填写基本信息！", "提示");
            return false;
        }
//        if (this.activityNoticeRequest.getActivityVehicles().size() < 1
//                || this.activityNoticeRequest.getActivityParts().size() < 1) {
//            ZkUtils.showInformation("请添加车辆和配件信息！", "提示");
//            return false;
//        }
        if (this.activityNoticeRequest.getActivityParts().size() > 0) {
            for (ActivityPartEntity partEntity : this.activityNoticeRequest.getActivityParts()) {
                if (partEntity.getPart() == null) {
                    this.activityNoticeRequest.getActivityParts().remove(partEntity);
                    activityNoticeService.save(this.activityNoticeRequest);
                }
            }
        }

        return true;

    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
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
    }

    @Command
    @NotifyChange("activityNoticeRequest")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_ASM);
        this.activityNoticeRequest.setNoticeFile(fileName);
    }

    @Command
    @NotifyChange("activityNoticeRequest")
    public void delUploadFile() {
        this.activityNoticeRequest.setNoticeFile("");
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_ASM + filename;
    }

    @Command
    @NotifyChange("*")
    public void addEntity() {
        if (type.equals("vehicle")) {
            addVehicle();
        } else {
            addPart();

        }
    }

    @Command
    public void checkTab(@BindingParam("type") String type) {
//        ZkUtils.showInformation(type,"ceshi");
        this.type = type;
    }

    /**
     * 增加配件行
     */
    @Command
    @NotifyChange("*")
    public void addVehicle() {
        ActivityVehicleEntity vehicleEntity = new ActivityVehicleEntity();
        this.activityNoticeRequest.getActivityVehicles().add(vehicleEntity);
    }

    /**
     * 删除车辆行
     *
     * @param vehicleEntity
     */
    @Command
    @NotifyChange("*")
    public void deleteVehicle(@BindingParam("model") ActivityVehicleEntity vehicleEntity) {
        this.activityNoticeRequest.getActivityVehicles().remove(vehicleEntity);
        computeCost();
    }

    /**
     * 选中配件行
     *
     * @param noticePart
     */
    @Command
    public void selectNoticePartEntity(@BindingParam("model") ActivityPartEntity noticePart) {
        this.activityPartEntity = noticePart;
    }

    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("partList")
    public void searchParts() {
        if (this.keyword.trim().length() >= CommonHelper.FILTER_PARTS_LEN) {
            this.partList = partService.findAllByKeyword(this.keyword.trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_PARTS_ERROR, "提示");
        }


    }

    /**
     * 选中配件
     *
     * @param partEntity
     */
    @Command
    @NotifyChange("*")
    public void selectPart(@BindingParam("model") PartEntity partEntity) {
        activityPartEntity.setPart(partEntity);
        this.keyword = "";
        this.partList.clear();
        computeCost();
    }

    /**
     * 增加配件行
     */
    @Command
    @NotifyChange("*")
    public void addPart() {
        ActivityPartEntity partEntity = new ActivityPartEntity();
        this.activityNoticeRequest.addActivityPart(partEntity);
        computeCost();
    }

    /**
     * 删除配件行
     *
     * @param partEntity
     */
    @Command
    @NotifyChange("activityNoticeRequest")
    public void deletePart(@BindingParam("model") ActivityPartEntity partEntity) {
        this.activityNoticeRequest.getActivityParts().remove(partEntity);
        computeCost();
    }

    /**
     * 打开车辆选择框
     */
    @Command
    public void openSelectVehicleForm() {
        this.vinList.clear();
        this.vehicles.clear();
        for (ActivityVehicleEntity av : this.activityNoticeRequest.getActivityVehicles()) {
            this.vinList.add(av.getVehicle().getVin());
            this.vehicles.add(av.getVehicle());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("vins", this.vinList);
        map.put("vehcles", this.vehicles);
        dialog = (Window) ZkUtils.createComponents("/views/asm/activity_notice_select_vehicle.zul", null, map);
        dialog.doModal();
    }

    @GlobalCommand(GlobalCommandValues.ACTIVITY_NOTICE_SELECT_VEHICLE)
    @NotifyChange("activityNoticeRequest")
    public void returnSelectVehicles(@BindingParam("vehicleList") List<VehicleEntity> vehicles) {
        this.activityNoticeRequest.getActivityVehicles().clear();
        for (VehicleEntity vehice : vehicles) {
            ActivityVehicleEntity av = new ActivityVehicleEntity();
            av.setVehicle(vehice);
            this.activityNoticeRequest.addActivityVehicle(av);

        }

        computeCost();
        dialog.detach();
    }


    @Command
//    @NotifyChange({"perLaberCost", "amountLaberCost", "perPartCost", "amountPartCost", "amount"})
    @NotifyChange("activityNoticeRequest")
    public void computeCost() {

        this.activityNoticeRequest.setPerLaberCost(MathHelper.getDoubleAndTwoDecimalPlaces(this.activityNoticeRequest.getPerLaberCost()));

        this.activityNoticeRequest.setPerPartCost(0.0);

        // 总人工成本
        Double amountLabelCost = this.activityNoticeRequest.getActivityVehicles().size() * this.activityNoticeRequest.getPerLaberCost();
        this.activityNoticeRequest.setAmountLaberCost(MathHelper.getDoubleAndTwoDecimalPlaces(amountLabelCost));

        // 计算单台车使用的配件成本
        Double perPartCost = 0.0;
        for (ActivityPartEntity part : this.activityNoticeRequest.getActivityParts()) {
            if (part.getPart() != null) {
                if (part.getAmount() == null) {
                    part.setAmount(0);
                }
                perPartCost = perPartCost + part.getAmount() * part.getPart().getPrice();
            }
        }
        this.activityNoticeRequest.setPerPartCost(MathHelper.getDoubleAndTwoDecimalPlaces(perPartCost));

        // 计算总配件成本
        this.activityNoticeRequest.setAmountPartCost(MathHelper.getDoubleAndTwoDecimalPlaces(this.activityNoticeRequest.getActivityVehicles().size()
                * this.activityNoticeRequest.getPerPartCost()));

        // 计算  总成本 = 总人工成本 + 总配件成本
        this.activityNoticeRequest.setAmount(
                MathHelper.getDoubleAndTwoDecimalPlaces(this.activityNoticeRequest.getAmountLaberCost()
                        + this.activityNoticeRequest.getAmountPartCost()));

    }


    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.activityNoticeRequest.getObjId() == null ? "" : this.activityNoticeRequest.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/asm/activityNotice_Print.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }


    public ActivityNoticeService getActivityNoticeService() {
        return activityNoticeService;
    }

    public void setActivityNoticeService(ActivityNoticeService activityNoticeService) {
        this.activityNoticeService = activityNoticeService;
    }

    public ActivityNoticeEntity getActivityNoticeRequest() {
        return activityNoticeRequest;
    }

    public void setActivityNoticeRequest(ActivityNoticeEntity activityNoticeRequest) {
        this.activityNoticeRequest = activityNoticeRequest;
    }

    public ActivityPartEntity getActivityPartEntity() {
        return activityPartEntity;
    }

    public void setActivityPartEntity(ActivityPartEntity activityPartEntity) {
        this.activityPartEntity = activityPartEntity;
    }

    public List<ActivityPartEntity> getActivityNoticePartlist() {
        return activityNoticePartlist;
    }

    public void setActivityNoticePartlist(List<ActivityPartEntity> activityNoticePartlist) {
        this.activityNoticePartlist = activityNoticePartlist;
    }

//    public ActivityVehicleEntity getActivityVehicleEntity() {
//        return activityVehicleEntity;
//    }
//
//    public void setActivityVehicleEntity(ActivityVehicleEntity activityVehicleEntity) {
//        this.activityVehicleEntity = activityVehicleEntity;
//    }

    public List<ActivityVehicleEntity> getActivityNoticeVehiclelist() {
        return activityNoticeVehiclelist;
    }

    public void setActivityNoticeVehiclelist(List<ActivityVehicleEntity> activityNoticeVehiclelist) {
        this.activityNoticeVehiclelist = activityNoticeVehiclelist;
    }

    public List<ActivityPartEntity> getActivityNoticePartEntities() {
        return activityNoticePartEntities;
    }

    public void setActivityNoticePartEntities(List<ActivityPartEntity> activityNoticePartEntities) {
        this.activityNoticePartEntities = activityNoticePartEntities;
    }

    public List<PartEntity> getPartList() {
        return partList;
    }

    public void setPartList(List<PartEntity> partList) {
        this.partList = partList;
    }

    public PartEntity getCurrentproduct() {
        return currentproduct;
    }

    public void setCurrentproduct(PartEntity currentproduct) {
        this.currentproduct = currentproduct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ActivityVehicleEntity> getActivityNoticeVehicleEntities() {
        return activityNoticeVehicleEntities;
    }

    public void setActivityNoticeVehicleEntities(List<ActivityVehicleEntity> activityNoticeVehicleEntities) {
        this.activityNoticeVehicleEntities = activityNoticeVehicleEntities;
    }

//    public List<VehicleEntity> getVariables() {
//        return variables;
//    }
//
//    public void setVariables(List<VehicleEntity> variables) {
//        this.variables = variables;
//    }

    public List<VehicleEntity> getVehicleListDelete() {
        return vehicleListDelete;
    }

    public void setVehicleListDelete(List<VehicleEntity> vehicleListDelete) {
        this.vehicleListDelete = vehicleListDelete;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

//    public List<VehicleEntity> getVehicles() {
//        return vehicles;
//    }
//
//    public void setVehicles(List<VehicleEntity> vehicles) {
//        this.vehicles = vehicles;
//    }
}


