package com.sunjet.vm.asm;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.model.asm.ReportPartEntity;
import com.sunjet.model.asm.ReportVehicleEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.QualityReportService;
import com.sunjet.service.basic.DealerService;
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
 */
public class QualityReportFormVM extends FlowFormBaseVM {

    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private QualityReportService qualityReportService;

    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private RoleService roleService;
    private String type = "vehicle";
    private String keyword = "";
    private Boolean manager = true;//服务经理
    private QualityReportEntity qualityReportEntity;
    private Window window;
    private List<PartEntity> qrPartlist = new ArrayList<>();
    private List<VehicleEntity> vehicles = new ArrayList<>();
    private List<DealerEntity> dealerEntityList = new ArrayList<DealerEntity>();
    private List<DictionaryEntity> fmvehicleTypes = new ArrayList<>();
    private DictionaryEntity selectedVehicleType;
    private Map<String, Object> variables = new HashMap<>();

    @Init(superclass = true)
    public void init() {
        this.setBaseService(this.qualityReportService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.qualityReportEntity = qualityReportService.findOneWithVehiclesAndParts(this.getBusinessId());
            if (this.qualityReportEntity.getStatus() != 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }
            this.setEntity(this.qualityReportEntity);

        } else {        // 业务对象和业务对象Id都为空
            qualityReportEntity = new QualityReportEntity();
            qualityReportEntity.setReportVehicles(new HashSet<ReportVehicleEntity>());
            qualityReportEntity.setReportParts(new HashSet<ReportPartEntity>());
//            DealerEntity dealerEntity = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
            if (this.getDealer() != null) {
                this.qualityReportEntity.setDealerCode(this.getDealer().getCode());
                this.qualityReportEntity.setDealerName(this.getDealer().getName());
                if (this.getDealer().getServiceManager() != null) {
                    this.qualityReportEntity.setServiceManager(this.getDealer().getServiceManager().getName());
                    this.qualityReportEntity.setServiceManagerPhone(this.getDealer().getServiceManager().getPhone());
                }
            }
//            qualityReportEntity.setReportType("普通");
            this.setReadonly(false);
        }
        this.setEntity(qualityReportEntity);

        this.fmvehicleTypes = CacheManager.getDictionariesByParentCode("15000");
        if (StringUtils.isNotBlank(this.qualityReportEntity.getVehicleType())) {
            for (DictionaryEntity entity : this.fmvehicleTypes) {
                if (entity.getName().equals(this.qualityReportEntity.getVehicleType())) {
                    this.selectedVehicleType = entity;
                    break;
                }
            }
        }
        RoleEntity role = roleService.findOneWithUsersByRoleId("role000");
        for (UserEntity i : role.getUsers()) {
            if (i.getName().equals(CommonHelper.getActiveUser().getUsername())) {
                manager = false;
                break;
            }
        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
        this.setEntity(qualityReportEntity);
    }

    @Command
    @Override
    public void showHandleForm() {
        if (this.qualityReportEntity.getCanEditType() == true && StringUtils.isBlank(this.qualityReportEntity.getReportType())) {
            ZkUtils.showExclamation("请选择速报类别", "系统提示");
            return;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getVehicleType())) {
            ZkUtils.showInformation("请选择车辆分类！", "提示");
            return;
        }
        super.showHandleForm();
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        FlowDocEntity entity = this.saveEntity(this.qualityReportEntity);
        this.qualityReportEntity = qualityReportService.findOneWithVehiclesAndParts(entity.getObjId());
        this.setEntity(this.qualityReportEntity);
//        this.setEntity(this.saveEntity(this.qualityReportEntity));
        showDialog();
    }

    @Override
    protected Boolean checkValid() {
        //if (this.qualityReportEntity.getStatus() > 0 && this.qualityReportEntity.getProcessInstanceId()==null) {
        //    ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
        //    return false;
        //}
        if (StringUtils.isBlank(this.qualityReportEntity.getTitle())) {
            ZkUtils.showInformation("请填写速报标题！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getSubmitterPhone())) {
            ZkUtils.showInformation("请填写申请人电话号码", "提示");
            return false;
        }
        if (dealerEntityList == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getFaultDesc())) {
            ZkUtils.showInformation("请填写故障描述！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getFaultStatus())) {
            ZkUtils.showInformation("请填写故障时行驶状态！", "提示");
            return false;
        }
        if (DocStatus.AUDITED.equals(this.getStatus())) {
            if (StringUtils.isBlank(this.qualityReportEntity.getReportType())) {
                ZkUtils.showInformation("请选择速报级别", "提示");
            }
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getServiceManagerPhone())) {
            ZkUtils.showInformation("请填写服务经理电话", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getFaultRoad())) {
            ZkUtils.showInformation("请填写故障时路面情况！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getFaultAddress())) {
            ZkUtils.showInformation("请填写故障发生地点！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getInitialReason())) {
            ZkUtils.showInformation("请填写初步原因分析！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getDecisions())) {
            ZkUtils.showInformation("请填写处置意见！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.qualityReportEntity.getFile())) {
            ZkUtils.showInformation("请上传附件！", "提示");
            return false;
        }

//        if (this.qualityReportEntity.getReportParts().size() < 1) {
//            ZkUtils.showInformation("请在检查结果选择配件！", "提示");
//            return false;
//        }
        if (this.qualityReportEntity.getReportVehicles().size() < 1) {
            ZkUtils.showInformation("请选择车辆！", "提示");
            return false;
        }


        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
        if (this.qualityReportEntity.getDealerCode() == null || this.qualityReportEntity.getDealerCode().trim().length() < 1) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        DealerEntity dealerEntity = (DealerEntity) dealerService.findDealerByCode(this.qualityReportEntity.getDealerCode());

        variables.put("serviceManager", dealerEntity.getServiceManager().getLogId());
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    startProcessInstance(variables);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                } else {
                    return;
                }
            }
        });
    }


    @Command
    public void selectVehicles() {
        Map<String, Object> map = new HashMap<>();
        window = (Window) ZkUtils.createComponents("/views/asm/select_vehicle_form.zul", null, map);
        window.setTitle("选择车辆");
        window.doModal();
    }

    @Command
    public void selectParts() {
        Map<String, Object> map = new HashMap<>();
        window = (Window) ZkUtils.createComponents("/views/asm/checkResult.zul", null, map);
        window.setTitle("选择配件");
        window.doModal();
    }

    @Command
    @NotifyChange("*")
    public void changevehicleType() {
        qualityReportEntity.setVehicleType(this.selectedVehicleType.getName());
    }

    @GlobalCommand
    @NotifyChange("*")
    public void updateSelectedVehicle(@BindingParam("vehicle") VehicleEntity vehicle) {
        Boolean exists = false;
        for (VehicleEntity v : this.vehicles) {
            if (v.getObjId().equals(vehicle.getObjId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            vehicles.add(vehicle);
        }

        if (window != null) {
            window.detach();
        }
    }


    @GlobalCommand
    @NotifyChange("*")
    public void updateSelectedVehicleList(@BindingParam("vehicle") List<VehicleEntity> list) {

        for (VehicleEntity Vehicle : list) {
            Boolean exists = false;

            for (VehicleEntity v : this.vehicles) {
                if (v.getObjId().equals(Vehicle.getObjId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                vehicles.add(Vehicle);
                ReportVehicleEntity qrVehicle = new ReportVehicleEntity();
                qrVehicle.setVehicle(Vehicle);
//                qrVehicle.setMileage(Integer.valueOf(Vehicle.getMileage()));
                qrVehicle.setMileage(Vehicle.getMileage());
                qualityReportEntity.getReportVehicles().add(qrVehicle);


            }
        }

        if (window != null) {
            window.detach();
        }

    }

    @GlobalCommand
    @NotifyChange("*")
    public void updateCheckResultList(@BindingParam("vehicle") List<PartEntity> lst) {
        for (PartEntity part : lst) {
            Boolean exists = false;
            for (PartEntity v : this.qrPartlist) {
                if (v.getObjId().equals(part.getObjId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                qrPartlist.add(part);
                ReportPartEntity reportPart = new ReportPartEntity();
                reportPart.setPart(part);
                qualityReportEntity.getReportParts().add(reportPart);
            }
        }


        if (window != null) {
            window.detach();
        }
    }

    @Command
    @NotifyChange("dealerEntityList")
    public void searchDealers(@BindingParam("model") String keyword) {
        this.dealerEntityList = dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword.trim());

    }

    @Command
    @NotifyChange("qualityReportEntity")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        qualityReportEntity.setDealerCode(dealer.getCode());
        qualityReportEntity.setDealerName(dealer.getName());
        if (dealer.getServiceManager() != null) {
            qualityReportEntity.setLinkman(dealer.getStationMaster());
            qualityReportEntity.setLinkmanPhone(dealer.getStationMasterPhone());
            qualityReportEntity.setServiceManager(dealer.getServiceManager().getName());
        }
    }

    @Command
    @NotifyChange("qualityReportEntity")
    public void clearSelectedDealer() {
        qualityReportEntity.setDealerCode("");
        qualityReportEntity.setDealerName("");
        qualityReportEntity.setServiceManagerPhone("");
        qualityReportEntity.setServiceManager("");

    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {

        qualityReportEntity.setOriginFile(event.getMedia().getName());
        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_ASM);
        qualityReportEntity.setFile(fileName);

    }

    @Command
    @NotifyChange("*")
    public void delUploadFile(@BindingParam("event") String type) {
        qualityReportEntity.setOriginFile("");
    }

    @Command
    @NotifyChange("*")
    public void deleteVehicle(@BindingParam("model") ReportVehicleEntity model) {
        for (ReportVehicleEntity item : qualityReportEntity.getReportVehicles()) {
            if (item == model) {
                vehicles.remove(model.getVehicle());
                qualityReportEntity.getReportVehicles().remove(item);

                return;
            }
        }
    }

    @Command
    @NotifyChange("*")
    public void deletePart(@BindingParam("model") ReportPartEntity model) {
        for (ReportPartEntity item : qualityReportEntity.getReportParts()) {
            if (item == model) {
                qrPartlist.remove(model.getPart());
                qualityReportEntity.getReportParts().remove(item);

                return;
            }
        }
    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();

        map.put("objId", this.qualityReportEntity.getObjId() == null ? "" : this.qualityReportEntity.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/asm/quality_report.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();


    }

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_ASM + filename;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }

    public QualityReportEntity getQualityReportEntity() {
        return qualityReportEntity;
    }

    public void setQualityReportEntity(QualityReportEntity qualityReportEntity) {
        this.qualityReportEntity = qualityReportEntity;
    }


    public List<PartEntity> getQrPartlist() {
        return qrPartlist;
    }

    public void setQrPartlist(List<PartEntity> qrPartlist) {
        this.qrPartlist = qrPartlist;
    }

    public List<DealerEntity> getDealerEntityList() {
        return dealerEntityList;
    }

    public void setDealerEntityList(List<DealerEntity> dealerEntityList) {
        this.dealerEntityList = dealerEntityList;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<DictionaryEntity> getFmvehicleTypes() {
        return fmvehicleTypes;
    }

    public void setFmvehicleTypes(List<DictionaryEntity> fmvehicleTypes) {
        this.fmvehicleTypes = fmvehicleTypes;
    }

    public DictionaryEntity getSelectedVehicleType() {
        return selectedVehicleType;
    }

    public void setSelectedVehicleType(DictionaryEntity selectedVehicleType) {
        this.selectedVehicleType = selectedVehicleType;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }
}
