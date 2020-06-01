package com.sunjet.vm.asm;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.*;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.MaintainEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.repository.asm.SupplyItemRepository;
import com.sunjet.repository.asm.SupplyRepository;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.*;
import com.sunjet.service.basic.MaintainService;
import com.sunjet.service.basic.PartService;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.BeanHelper;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 三包服务单 表单 VM
 */
public class WarrantyMaintenanceFormVM extends FlowFormBaseVM {
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private MaintainService maintainService;
    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private QualityReportService qualityReportService;
    @WireVariable
    private ExpenseReportService expenseReportService;
    @WireVariable
    private SupplyNoticeService supplyNoticeService;
    @WireVariable
    private RecycleNoticeService recycleNoticeService;

    @WireVariable
    private SupplyService supplyService;
    @WireVariable
    private ProcessService processService;
    @WireVariable
    private SupplyItemRepository supplyItemRepository;
    @WireVariable
    private SupplyRepository supplyRepository;

    //@WireVariable
    //private JdbcTemplate jdbcTemplate;

    //    private String keyword = "";
    private WarrantyMaintenanceEntity warrantyMaintenanceRequest;
    private CommissionPartEntity commissionPartEntity;
    private WarrantyMaintainEntity warrantyMaintainEntity;
    private List<DictionaryEntity> docTypes = new ArrayList<>();
    private List<DictionaryEntity> repairTypes = new ArrayList<>();//维修类别
    private List<PartEntity> parts = new ArrayList<>();
    private List<MaintainEntity> maintains = new ArrayList<>();
    private VehicleEntity vehicle;
    private Window window;
    private List<VehicleEntity> vehicles = new ArrayList<>();
    private QualityReportEntity qualityReport;
    private List<QualityReportEntity> qualityReports = new ArrayList<>();
    private ExpenseReportEntity expenseReport;
    private List<ExpenseReportEntity> expenseReports = new ArrayList<>();
    private Map<String, Object> variables = new HashMap<>();


    @Init(superclass = true)
    public void init() {
        this.setBaseService(warrantyMaintenanceService);

        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.warrantyMaintenanceRequest = warrantyMaintenanceService.findOneWithOthersById(this.getBusinessId());
            if (this.qualityReport == null && this.expenseReport == null) {
                this.qualityReport = qualityReportService.findOneWithVehiclesAndParts(warrantyMaintenanceRequest.getQualityReportId());
                this.expenseReport = expenseReportService.findOneWithVehiclesAndParts(warrantyMaintenanceRequest.getExpenseReportId());
            }

        } else {
            this.warrantyMaintenanceRequest = new WarrantyMaintenanceEntity();
//            DealerEntity dealerEntity = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();

            if (CommonHelper.getActiveUser().getDealer() != null) {
                this.warrantyMaintenanceRequest.setDealerCode(CommonHelper.getActiveUser().getDealer().getCode());
                this.warrantyMaintenanceRequest.setDealerName(CommonHelper.getActiveUser().getDealer().getName());
                this.warrantyMaintenanceRequest.setDealerStar(CommonHelper.getActiveUser().getDealer().getStar());
                this.warrantyMaintenanceRequest.setDealerPhone(CommonHelper.getActiveUser().getDealer().getPhone());
                this.warrantyMaintenanceRequest.setProvinceName(CommonHelper.getActiveUser().getDealer().getProvince().getName());
                this.warrantyMaintenanceRequest.setServiceManager(CommonHelper.getActiveUser().getDealer().getServiceManager() == null ? "" : CommonHelper.getActiveUser().getDealer().getServiceManager().getName());
                this.warrantyMaintenanceRequest.setHourPrice(this.getHourPriceByDealer(CommonHelper.getActiveUser().getDealer()));


            }
//            this.warrantyMaintenanceRequest.setOperator(CommonHelper.getActiveUser().getUsername());
//            this.warrantyMaintenanceRequest.setOperatorPhone(CommonHelper.getActiveUser().getPhone());
            this.warrantyMaintenanceRequest.setDocType("三包维修");
        }

        this.setEntity(this.warrantyMaintenanceRequest);

//        this.fmExpenseStandards = CacheManager.getDictionariesByParentCode("13000");
//
//        if (StringUtils.isNotBlank(this.firstMaintenanceRequest.getStandardExpenseId())) {
//            for (DictionaryEntity entity : this.fmExpenseStandards) {
//                if (entity.getObjId().equals(this.firstMaintenanceRequest.getStandardExpenseId())) {
//                    this.selectedExpenseStandard = entity;
//                    break;
//                }
//            }
//        }
        this.repairTypes = CacheManager.getDictionariesByParentCode("10070");
        docTypes = CacheManager.getDictionariesByParentCode("14000");
        if (this.warrantyMaintenanceRequest.getOtherExpense() == null) {
            this.warrantyMaintenanceRequest.setOtherExpense(0.0);
        }

        //List<Map<String, Object>> maps = jdbcTemplate.queryForList(
        //        "SELECT PROC_INST_ID_ FROM ACT_RU_EXECUTION WHERE BUSINESS_KEY_ = 'WarrantyMaintenanceEntity.8a9982485bd85e0b015bd8a784a1000a.WMFW201705050042.陈章南.25161'");
        //if (maps.size() > 1) {
        //    processService.deleteProcessInstance("238968");
        //
        //}
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
//        if (win != null) {
//            win.setTitle(win.getTitle() + titleMsg);
//        }
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        computeCost();
        if (this.warrantyMaintenanceRequest.getVehicle() != null) {
            vehicleService.getRepository().save(this.warrantyMaintenanceRequest.getVehicle());
        }
        FlowDocEntity entity = this.saveEntity(this.warrantyMaintenanceRequest);
        this.warrantyMaintenanceRequest = warrantyMaintenanceService.findOneWithOthersById(entity.getObjId());
        this.setEntity(this.warrantyMaintenanceRequest);
//        this.setEntity(this.saveEntity(this.warrantyMaintenanceRequest));
        showDialog();
    }

    @Override
    protected Boolean checkValid() {
        if (this.warrantyMaintenanceRequest.getEndDate().getTime() <= this.warrantyMaintenanceRequest.getStartDate().getTime()) {
            ZkUtils.showExclamation("【完工日期】必须大于【开工日期】！", "系统提示");
            return false;
        }
        if (this.warrantyMaintenanceRequest.getPullOutDate().getTime() <= this.warrantyMaintenanceRequest.getPullInDate().getTime()) {
            ZkUtils.showExclamation("【出站时间】必须大于【进站时间】！", "系统提示");
            return false;
        }
        if (this.warrantyMaintenanceRequest.getVehicle() == null) {
            ZkUtils.showExclamation("请选择车辆！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getAmeplate())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getManual())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getOdometer())
//        || StringUtils.isBlank(this.warrantyMaintenanceRequest.getInvoice())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getFaultlocation())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getFront45())) {
            ZkUtils.showExclamation("请上传所有必填凭证文件！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getSender())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getSenderPhone())
//        || StringUtils.isBlank(this.warrantyMaintenanceRequest.getFault())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getRepairType())
                || StringUtils.isBlank(this.warrantyMaintenanceRequest.getRepairer())) {
            ZkUtils.showExclamation("请填写维修信息！", "系统提示");
            return false;
        }
        for (CommissionPartEntity parts : this.warrantyMaintenanceRequest.getCommissionParts()) {
            if (parts.getAmount().equals(0)) {
                ZkUtils.showExclamation("配件需求数量小于0不能提交", "系统提示");
                return false;
            } else if (this.warrantyMaintenanceRequest.getSupplyNoticeId() == null && parts.getPartSupplyType().contains("调拨")) {
                ZkUtils.showExclamation("请生成调拨单再提交", "系统提示");
                return false;
            }

        }
        return true;
    }

    /**
     * 提交、启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        computeCost();
        if (this.getDealer().getParent() != null) {
            List<UserEntity> list = userService.findAllByDealerCode(this.getDealer().getParent().getCode());
            List<String> users = new ArrayList<>();
            for (UserEntity userEntity : list) {
                System.out.println(userEntity.getLogId());
                users.add(userEntity.getLogId());
            }
            variables.put("firstLevelUsers", users);
        }
        //检查是否禁用生成调拨单按钮
        if (this.warrantyMaintenanceRequest.getCommissionParts().size() > 0) {
            for (CommissionPartEntity parts : this.warrantyMaintenanceRequest.getCommissionParts()) {
                if (parts.getPartSupplyType().contains("调拨") && this.warrantyMaintenanceRequest.getSupplyNoticeId() != null) {
                    this.warrantyMaintenanceRequest.setCanEditSupply(false);
                }
            }
        } else {
            this.warrantyMaintenanceRequest.setCanEditSupply(false);
        }

        if (this.warrantyMaintenanceRequest.getSupplyNoticeId() != null) {
            SupplyNoticeEntity supplyNotice = this.supplyNoticeService.getSupplyNoticeByID(this.warrantyMaintenanceRequest.getSupplyNoticeId());
            if (!supplyNotice.getStatus().equals(DocStatus.CLOSED.getIndex())) {
                ZkUtils.showInformation("调拨单没有审核通过不能提交", "提示");
                return;
            } else {
                //请求数量
                Double requestAmount = 0.0;
                List<SupplyItemEntity> supplyItemEntityList = new ArrayList<>();
                //取出所有调拨供货单清单配件
                for (SupplyNoticeItemEntity supplyNoticeItemEntity : supplyNotice.getItems()) {
                    List<SupplyItemEntity> item = this.supplyItemRepository.findAllByNoticeItemId(supplyNoticeItemEntity.getObjId());
                    if(item!=null){
                        for (SupplyItemEntity supplyItemEntity : item) {
                            if(supplyItemEntity.getSupply()!=null){
                                supplyItemEntityList.add(supplyItemEntity);
                            }
                        }
                    }
                    requestAmount += supplyNoticeItemEntity.getRequestAmount();
                }
                if (supplyItemEntityList != null || supplyItemEntityList.size() == 0) {
                    //已发货数量
                    Double amount = 0.0;

                    for (SupplyItemEntity supplyItemEntity : supplyItemEntityList) {
                        amount += supplyItemEntity.getAmount();
                    }
                    for (SupplyItemEntity supplyItemEntity : supplyItemEntityList) {
                        //通知单清单配件是否已发完
                        if (amount < requestAmount) {
                            ZkUtils.showInformation("调拨配件未收货不能提交", "提示");
                            return;
                        } else {
                            if (supplyItemEntity.getSupply() != null) {
                                //判断调拨供货单的单据状态
                                if (!supplyItemEntity.getSupply().getStatus().equals(DocStatus.CLOSED) && supplyItemEntity.getSupply().getRcvDate() == null) {
                                    ZkUtils.showInformation("调拨配件未收货不能提交", "提示");
                                    return;
                                }
                            } else {
                                ZkUtils.showInformation("调拨配件未发货不能提交", "提示");
                                return;
                            }

                        }
                    }
                } else {
                    ZkUtils.showInformation("调拨配件未发货不能提交", "提示");
                    return;
                }

            }


        }


        //校验待返回故障件配件信息
        for (CommissionPartEntity part : this.warrantyMaintenanceRequest.getCommissionParts()) {
            if (StringUtils.isBlank(part.getPartCode())) {
                ZkUtils.showError("配件需求信息为0或者信息不全", "提示");
                return;
            }
            if (part.getAmount().equals(0)) {
                ZkUtils.showError("配件需求配件信息为0或者信息不全", "提示");
                return;
            }
        }

        variables.put("level", this.getDealer().getLevel());
        variables.put("serviceManager", this.getDealer().getServiceManager().getLogId());
//        variables.put("days", this.leaveBill.getDays());
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

    /**
     * 校验是否生成返回件
     */
    @Command
    @Override
    public void showHandleForm() {
        UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
        Set<RoleEntity> roles = userEntity.getRoles();
        String position = "";
        Boolean recycle = false;
        //检查用户职务
        for (RoleEntity role : roles) {
            if ("审单员".equals(role.getName())) {
                position = role.getName();
            }
        }
        //检查是否有返回件
        for (CommissionPartEntity part : this.warrantyMaintenanceRequest.getCommissionParts()) {
            if (part.getRecycle()) {
                recycle = true;
            }
        }
        if ("审单员".equals(position) && recycle == true && this.warrantyMaintenanceRequest.getRecycleNoticeId() == null) {
            ZkUtils.showInformation("请生成故障件返回单", "提示");
        } else {
            super.showHandleForm();
        }


    }

    /**
     * 上传文件
     *
     * @param event
     * @param type
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t01")) {
            this.warrantyMaintenanceRequest.setAmeplate(fileName);  // 车辆名牌
        } else if (type.equalsIgnoreCase("t02")) {
            this.warrantyMaintenanceRequest.setManual(fileName);    // 保养手册首页
        } else if (type.equalsIgnoreCase("t03")) {
            this.warrantyMaintenanceRequest.setOdometer(fileName);  // 里程表
        } else if (type.equalsIgnoreCase("t04")) {
            this.warrantyMaintenanceRequest.setInvoice(fileName);   // 购买发票
        } else if (type.equalsIgnoreCase("t05")) {
            this.warrantyMaintenanceRequest.setFront45(fileName);   // 车位前侧45度照片
        } else if (type.equalsIgnoreCase("t06")) {
            this.warrantyMaintenanceRequest.setFaultlocation(fileName); // 故障部位照片
        }
    }

    @Command
    @NotifyChange("*")
    public void dltUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {
            this.warrantyMaintenanceRequest.setAmeplate("");  // 车辆名牌
        } else if (type.equalsIgnoreCase("t02")) {
            this.warrantyMaintenanceRequest.setManual("");    // 保养手册首页
        } else if (type.equalsIgnoreCase("t03")) {
            this.warrantyMaintenanceRequest.setOdometer("");  // 里程表
        } else if (type.equalsIgnoreCase("t04")) {
            this.warrantyMaintenanceRequest.setInvoice("");   // 购买发票
        } else if (type.equalsIgnoreCase("t05")) {
            this.warrantyMaintenanceRequest.setFront45("");   // 车位前侧45度照片
        } else if (type.equalsIgnoreCase("t06")) {
            this.warrantyMaintenanceRequest.setFaultlocation(""); // 故障部位照片
        }

    }

    /**
     * 上传外出凭证
     *
     * @param event
     * @param goOutitem
     */
    @Command
    @NotifyChange("*")
    public void outGoUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("each") GoOutEntity goOutitem) {
        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW);
        for (GoOutEntity outEntity : this.warrantyMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture(fileName);
            }

        }
    }

    /**
     * 删除外出凭证
     *
     * @param goOutitem
     */
    @Command
    @NotifyChange("*")
    public void deleteOutGoFile(@BindingParam("each") GoOutEntity goOutitem) {
        for (GoOutEntity outEntity : this.warrantyMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture("");
            }

        }

    }


    /**
     * 获取图片附件路径
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_FLOW + filename;
    }

    /**
     * 查询车辆列表
     */
    @Command
    @NotifyChange("vehicles")
    public void searchVehicles() {
        this.vehicles.clear();

        if (this.qualityReport == null && this.expenseReport == null) {
            if (this.getKeyword().trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
                this.vehicles = vehicleService.findAllByKeyword(this.getKeyword().trim());
            } else {
                ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
            }
        } else {
            if (this.qualityReport != null) {
                Set<ReportVehicleEntity> qrVehicles = this.qualityReport.getReportVehicles();
                for (ReportVehicleEntity entity : qrVehicles) {
                    vehicles.add(entity.getVehicle());
                }
            } else {
                Set<ReportVehicleEntity> qrVehicles = this.expenseReport.getReportVehicles();
                for (ReportVehicleEntity entity : qrVehicles) {
                    vehicles.add(entity.getVehicle());
                }
            }
        }
    }

    /**
     * 选中车辆
     *
     * @param vehicleEntity
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Command
    @NotifyChange("*")
    public void selectVehicle(@BindingParam("model") VehicleEntity vehicleEntity) throws InvocationTargetException, IllegalAccessException {
        this.warrantyMaintenanceRequest.setVehicle(vehicleEntity);
        this.vehicle = vehicleEntity;
        BeanUtils.copyProperties(vehicleEntity, this.warrantyMaintenanceRequest, BeanHelper.getIgnorePropertyNames());
        this.setKeyword("");
        this.vehicles.clear();
    }


    /**
     * 更新行驶里程
     * @param vmt
     */
    @Command
    @NotifyChange("vmt")
    public void updateVehiceleMileage(@BindingParam("vmt")String vmt){
        if(this.warrantyMaintenanceRequest.getVehicle()!=null){
            this.warrantyMaintenanceRequest.getVehicle().setMileage(vmt);
        }
    }

    /**
     * 查询质量速报列表
     */
    @Command
    @NotifyChange("qualityReports")
    public void searchQualityReports() {
        // 查当前服务站的质量速报
        qualityReports = qualityReportService.findAllByKeywordAndDealerCode(this.getKeyword().trim(), this.warrantyMaintenanceRequest.getDealerCode());
        // 查当前服务站的质量速报
//        qualityReports = qualityReportService.findAllByStatusAndKeyword("%" + this.keyword + "%");
    }

    /**
     * 选中质量速报
     *
     * @param qualityReport
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Command
    @NotifyChange("*")
    public void selectQualityReport(@BindingParam("model") QualityReportEntity qualityReport) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showError("已经生成了调拨单,不能选择质量速报", "系统提示");
            return;
        }
        this.vehicles.clear();
        this.qualityReport = qualityReport;
        this.expenseReport = null;
        this.warrantyMaintenanceRequest.setVehicle(null);
        this.warrantyMaintenanceRequest.setQualityReportId(this.qualityReport.getObjId());
        this.warrantyMaintenanceRequest.setQualityReportDocNo(this.qualityReport.getDocNo());
        this.warrantyMaintenanceRequest.setExpenseReportId(null);
        this.warrantyMaintenanceRequest.setExpenseReportDocNo(null);
        this.setKeyword("");
        this.qualityReports.clear();

        this.qualityReport = qualityReportService.findOneWithVehiclesAndParts(qualityReport.getObjId());

//        Set<CommissionPartEntity> partItems = this.warrantyMaintenanceRequest.getActivityParts();
        this.warrantyMaintenanceRequest.getCommissionParts().clear();
        for (ReportPartEntity rpe : this.qualityReport.getReportParts()) {
            CommissionPartEntity part = new CommissionPartEntity();
            part.setPartCode(rpe.getPart().getCode());
            part.setPartName(rpe.getPart().getName());
            part.setPrice(rpe.getPart().getPrice());
            part.setPartType(rpe.getPart().getPartType());
            part.setAmount(rpe.getAmount());
            part.setWarrantyMileage(rpe.getPart().getWarrantyMileage());
            part.setWarrantyTime(rpe.getPart().getWarrantyTime());
            part.setPartSupplyType("自购");
            this.warrantyMaintenanceRequest.getCommissionParts().add(part);
        }
        computeCost();
    }

    /**
     * 查询费用速报列表
     */
    @Command
    @NotifyChange("expenseReports")
    public void searchExpenseReports() {
        // 查当前服务站的费用速报
        expenseReports = expenseReportService.findAllByKeywordAndDealerCode(this.getKeyword().trim(), this.warrantyMaintenanceRequest.getDealerCode());
        // 查所有服务站的费用速报
//        expenseReports = expenseReportService.findAllByStatusAndKeyword("%" + this.keyword + "%");
    }

    /**
     * 选中费用速报
     *
     * @param expenseReport
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Command
    @NotifyChange("*")
    public void selectExpenseReport(@BindingParam("model") ExpenseReportEntity expenseReport) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showError("已经生成了调拨单,不能选择费用速报", "系统提示");
            return;
        }


        this.vehicles.clear();
        this.expenseReport = expenseReport;
        this.qualityReport = null;
        this.warrantyMaintenanceRequest.setVehicle(null);
        this.warrantyMaintenanceRequest.setQualityReportId(null);
        this.warrantyMaintenanceRequest.setQualityReportDocNo(null);
        this.warrantyMaintenanceRequest.setExpenseReportId(this.expenseReport.getObjId());
        this.warrantyMaintenanceRequest.setExpenseReportDocNo(this.expenseReport.getDocNo());
        this.setKeyword("");
        this.expenseReports.clear();


        this.expenseReport = expenseReportService.findOneWithVehiclesAndParts(expenseReport.getObjId());

//        Set<CommissionPartEntity> partItems = this.warrantyMaintenanceRequest.getActivityParts();
        this.warrantyMaintenanceRequest.getCommissionParts().clear();
        for (ReportPartEntity rpe : this.expenseReport.getReportParts()) {
            CommissionPartEntity part = new CommissionPartEntity();
            part.setPartCode(rpe.getPart().getCode());
            part.setPartName(rpe.getPart().getName());
            part.setPrice(rpe.getPart().getPrice());
            part.setPartType(rpe.getPart().getPartType());
            part.setAmount(rpe.getAmount());
            part.setWarrantyMileage(rpe.getPart().getWarrantyMileage());
            part.setWarrantyTime(rpe.getPart().getWarrantyTime());
            part.setPartSupplyType("自购");
            this.warrantyMaintenanceRequest.getCommissionParts().add(part);
        }
        computeCost();
    }

    /**
     * 清除选中的质量速报对象
     */
    @Command
    @NotifyChange("*")
    public void clearQualityReport() {
        if (this.qualityReport != null) {
            this.qualityReport = null;
            this.vehicles.clear();
            this.warrantyMaintenanceRequest.setQualityReportDocNo("");
            this.warrantyMaintenanceRequest.setVehicle(null);
            this.warrantyMaintenanceRequest.getCommissionParts().clear();
            this.parts.clear();
            this.qualityReports.clear();
            computeCost();
        }
    }

    /**
     * 清除选中的费用速报
     */
    @Command
    @NotifyChange("*")
    public void clearExpenseReport() {
        if (this.expenseReport != null) {
            this.expenseReport = null;
            this.vehicles.clear();
            this.warrantyMaintenanceRequest.setExpenseReportDocNo("");
            this.warrantyMaintenanceRequest.setVehicle(null);
            this.warrantyMaintenanceRequest.getCommissionParts().clear();
            this.parts.clear();
            this.expenseReports.clear();
            computeCost();
        }
    }

    /**
     * 清除选中的车辆
     */
    @Command
    @NotifyChange("*")
    public void clearVehicle() {
        this.vehicle = null;
        this.warrantyMaintenanceRequest.setVehicle(null);
    }


    /**
     * 选中配件行
     *
     * @param commissionPart
     */
    @Command
    @NotifyChange("parts")
    public void selectCommissionPart(@BindingParam("model") CommissionPartEntity commissionPart) {
        this.commissionPartEntity = commissionPart;
    }

    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("parts")
    public void searchParts() {
        if (this.getKeyword().trim().length() >= CommonHelper.FILTER_PARTS_LEN) {
            this.parts = partService.findAllByKeyword(this.getKeyword().trim());
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
        commissionPartEntity.setPartCode(partEntity.getCode());
        commissionPartEntity.setPartName(partEntity.getName());
        commissionPartEntity.setPrice(partEntity.getPrice());
        commissionPartEntity.setPartType(partEntity.getPartType());
        commissionPartEntity.setWarrantyTime(partEntity.getWarrantyTime());
        commissionPartEntity.setPartClassify(partEntity.getPartClassify());
        commissionPartEntity.setWarrantyMileage(partEntity.getWarrantyMileage());

        this.setKeyword("");
        this.parts.clear();
        computeCost();
    }

    /**
     * 选中维修项目
     *
     * @param warrantyMaintainEntity
     */
    @Command
    public void selectWarrantMaintain(@BindingParam("model") WarrantyMaintainEntity warrantyMaintainEntity) {
        this.warrantyMaintainEntity = warrantyMaintainEntity;
    }

    /**
     * 查询维修项目列表
     */
    @Command
    @NotifyChange("maintains")
    public void searchMaintains() {
        this.maintains = maintainService.findAllByFilter(this.getKeyword().trim());
    }

    /**
     * 选中维修项目
     *
     * @param maintainEntity
     */
    @Command
    @NotifyChange("*")
    public void selectMaintain(@BindingParam("model") MaintainEntity maintainEntity) {
        warrantyMaintainEntity.setCode(maintainEntity.getCode());
        warrantyMaintainEntity.setName(maintainEntity.getName());
        warrantyMaintainEntity.setWorkTime(maintainEntity.getWorkTime());
        warrantyMaintainEntity.setHourPrice(this.warrantyMaintenanceRequest.getHourPrice());
        warrantyMaintainEntity.setTotal(warrantyMaintainEntity.getHourPrice() * warrantyMaintainEntity.getWorkTime());
        this.setKeyword("");
        this.maintains.clear();
        computeCost();
    }

    /**
     * 增加维修项目行
     */
    @Command
    @NotifyChange("*")
    public void addMaintain() {
        this.warrantyMaintenanceRequest.getWarrantyMaintains().add(new WarrantyMaintainEntity());
    }

    /**
     * 删除维修项目行
     *
     * @param maintainEntity
     */
    @Command
    @NotifyChange("*")
    public void deleteMaintain(@BindingParam("model") WarrantyMaintainEntity maintainEntity) {
        this.warrantyMaintenanceRequest.getWarrantyMaintains().remove(maintainEntity);
        computeCost();
    }

    /**
     * 增加配件行
     */
    @Command
    @NotifyChange("*")
    public void addPart() {
        CommissionPartEntity partEntity = new CommissionPartEntity();
//        partEntity.setPartType("配件");
        if (partEntity.getPartType() == null) {
            partEntity.setPartType("配件");
        }
        if (partEntity.getPartType().equals("配件")) {
            partEntity.setPartSupplyType("调拨");
        } else {
            partEntity.setPartSupplyType("自购");
        }
        partEntity.setRecycle(true);
        this.warrantyMaintenanceRequest.getCommissionParts().add(partEntity);
    }

    /**
     * 删除配件行
     *
     * @param partEntity
     */
    @Command
    @NotifyChange("*")
    public void deletePart(@BindingParam("model") CommissionPartEntity partEntity) {
        this.warrantyMaintenanceRequest.getCommissionParts().remove(partEntity);
        computeCost();
    }

    /**
     * 增加外出行
     */
    @Command
    @NotifyChange("*")
    public void addGoOut() {
        this.warrantyMaintenanceRequest.getGoOuts().add(new GoOutEntity());
    }

    /**
     * 删除外出行
     *
     * @param goOutEntity
     */
    @Command
    @NotifyChange("*")
    public void deleteGoOut(@BindingParam("model") GoOutEntity goOutEntity) {
        this.warrantyMaintenanceRequest.getGoOuts().remove(goOutEntity);
        computeCost();
    }

    /**
     * 统计费用
     */
    @Command
    @NotifyChange("*")
    public void computeCost() {
        this.warrantyMaintenanceRequest.setExpenseTotal(0.0);           // 总合计费用
        this.warrantyMaintenanceRequest.setPartExpense(0.0);            // 配件费用合计
        this.warrantyMaintenanceRequest.setAccessoriesExpense(0.0);     // 辅料费用合计
        this.warrantyMaintenanceRequest.setOutExpense(0.0);             // 外出费用合计

        this.warrantyMaintenanceRequest.setSettlementAccesoriesExpense(0.0);// 应结算辅料费用合计
        this.warrantyMaintenanceRequest.setSettlementPartExpense(0.0);      // 应结算配件费用
        this.warrantyMaintenanceRequest.setSettlementTotleExpense(0.0);     // 应结算配件费用
        this.warrantyMaintenanceRequest.setOutHours(0.0);                   // 外出工时合计
        this.warrantyMaintenanceRequest.setMaintainHours(0.0);              // 维修工时合计
        if (this.warrantyMaintenanceRequest.getOtherExpense() == null) {
            this.warrantyMaintenanceRequest.setOtherExpense(0.0);               //其他费用
        }
        Double outKm = 0.0;
        Boolean isOut = false;
        this.warrantyMaintenanceRequest.setNightExpense(0.0);
        if (this.warrantyMaintenanceRequest.getNightWork()) {  // 夜间作业
            this.warrantyMaintenanceRequest.setNightExpense(Double.parseDouble(CacheManager.getConfigValue("cost_night")));
        }

        // 维修工时
        for (WarrantyMaintainEntity entity : this.warrantyMaintenanceRequest.getWarrantyMaintains()) {
            this.warrantyMaintenanceRequest.setMaintainHours(this.warrantyMaintenanceRequest.getMaintainHours()
                    + entity.getWorkTime());
        }
        // 维修工时费用
        this.warrantyMaintenanceRequest.setMaintainWorkTimeExpense(this.warrantyMaintenanceRequest.getMaintainHours()
                * this.warrantyMaintenanceRequest.getHourPrice());

        for (GoOutEntity entity : this.warrantyMaintenanceRequest.getGoOuts()) {
            if (entity.getPlace() != null && StringUtils.isNotBlank(entity.getPlace().trim())) {   // 外出地点不为空时，才进行计算
                isOut = true;       // 目的地不为空，表示确实有外出服务
                outKm = outKm + entity.getMileage();        // 外出里程累加
                entity.setTranCosts(entity.getMileage()
                        * Double.parseDouble(CacheManager.getConfigValue("cost_per_km")));          // 交通费用 = 外出单向里程 * 3.0 元
                entity.setTrailerCost(entity.getTrailerMileage()
                        * Double.parseDouble(CacheManager.getConfigValue("cost_per_km_trailer")));  // 拖车费用 = 拖车里程 * 2.8元
                entity.setPersonnelSubsidy(entity.getOutGoDay()
                        * entity.getOutGoNum()
                        * Double.parseDouble(CacheManager.getConfigValue("cost_person_day")));      // 人员补贴 = 外出天数 * 外出人员 * 55
                entity.setNightSubsidy(entity.getOutGoNum()
                        * ((entity.getOutGoDay() - 1) >= 0 ? (entity.getOutGoDay() - 1) : 0)
                        * Double.parseDouble(CacheManager.getConfigValue("cost_person_night")));    // 住宿补贴 = 外出天数 * 外出人员 * 80
//                entity.setGoOutSubsidy(entity.getTimeSubsidy()
//                        * this.warrantyMaintenanceRequest.getHourPrice());     // 外出补贴费用
                // 单项外出费用统计
                entity.setAmountCost(entity.getTranCosts()  // 交通费用
                        + entity.getTrailerCost()           // 拖车费用
                        + entity.getPersonnelSubsidy()      // 人员补贴
                        + entity.getNightSubsidy());        // 住宿补贴
//                        + entity.getGoOutSubsidy());        // 外出补贴费用

                // 工时补贴合计
//                this.warrantyMaintenanceRequest.setOutHours(this.warrantyMaintenanceRequest.getOutHours()
//                        + entity.getTimeSubsidy());

                // 外出费用合计
                this.warrantyMaintenanceRequest.setOutExpense(this.warrantyMaintenanceRequest.getOutExpense()
                        + entity.getAmountCost());
            }
        }

        // 计算出来的工时补贴费用合计
        Double oriOutHourExpense = this.warrantyMaintenanceRequest.getHourPrice() * this.warrantyMaintenanceRequest.getOutHours();
        this.warrantyMaintenanceRequest.setOutWorkTimeExpense(oriOutHourExpense);
        if (isOut) {    // 有外出记录
            // 工时补贴费用合计
            if (outKm <= Double.parseDouble(CacheManager.getConfigValue("km_interval"))) {   // 外出总里程 < 50 公里
//                if (this.warrantyMaintenanceRequest.getOutWorkTimeExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))) {
//                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval")));
//                    this.warrantyMaintenanceRequest.setOutExpense(
//                            this.warrantyMaintenanceRequest.getOutExpense()
//                                    - oriOutHourExpense
//                                    + this.warrantyMaintenanceRequest.getOutWorkTimeExpense());
//                }

                if (this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))) {
                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))
                            - this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense());
                }

            } else {   // 外出里程 > 50
//                if (this.warrantyMaintenanceRequest.getOutWorkTimeExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))) {
//                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval")));
//                    this.warrantyMaintenanceRequest.setOutExpense(
//                            this.warrantyMaintenanceRequest.getOutExpense()
//                                    - oriOutHourExpense
//                                    + this.warrantyMaintenanceRequest.getOutWorkTimeExpense());
//                }
                if (this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))) {
                    this.warrantyMaintenanceRequest.setOutWorkTimeExpense(Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))
                            - this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense());
                }
            }
        }

        this.warrantyMaintenanceRequest.setOutExpense(this.warrantyMaintenanceRequest.getOutExpense()
                + this.warrantyMaintenanceRequest.getOutWorkTimeExpense());


        // 配件费用
        for (CommissionPartEntity entity : this.warrantyMaintenanceRequest.getCommissionParts()) {
            entity.setTotal(entity.getPrice() * entity.getAmount());   // 单项费用
//      entity.setTotal(MathHelper.getDoubleAndTwoDecimalPlaces(entity.getTotal()));
            if (entity.getPartSupplyType().equals("调拨") || entity.getPartSupplyType().equals("寄销")) {
                entity.setSettlementTotal(0.0);
            } else {
                entity.setSettlementTotal(entity.getPrice() * entity.getAmount());  // 结算费用
//        entity.setSettlementTotal(MathHelper.getDoubleAndTwoDecimalPlaces(entity.getSettlementTotal()));
            }

            if (entity.getPartType().equals("配件")) {   // 配件
                // 叠加配件结算费用
                this.warrantyMaintenanceRequest.setSettlementPartExpense(this.warrantyMaintenanceRequest.getSettlementPartExpense()
                        + entity.getSettlementTotal());
                // 叠加配件费用
                this.warrantyMaintenanceRequest.setPartExpense(this.warrantyMaintenanceRequest.getPartExpense()
                        + entity.getTotal());
            } else {
                // 叠加辅料结算费用
                this.warrantyMaintenanceRequest.setSettlementAccesoriesExpense(this.warrantyMaintenanceRequest.getSettlementAccesoriesExpense()
                        + entity.getSettlementTotal());
                // 叠加辅料费用
                this.warrantyMaintenanceRequest.setAccessoriesExpense(this.warrantyMaintenanceRequest.getAccessoriesExpense()
                        + entity.getTotal());
            }
        }


        // 总费用
        this.warrantyMaintenanceRequest.setExpenseTotal(
                this.warrantyMaintenanceRequest.getAccessoriesExpense()      // 辅料费用
                        + this.warrantyMaintenanceRequest.getNightExpense()
//                        +this.warrantyMaintenanceRequest.getWorkTimeExpense()        // 外出工时费用
                        + this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense() // 维修工时费用
                        + this.warrantyMaintenanceRequest.getOutExpense()             // 外出费用
                        + this.warrantyMaintenanceRequest.getPartExpense()            // 配件费用
                        + this.warrantyMaintenanceRequest.getOtherExpense()           // 其它费用
        );
        // 应结算费用
        this.warrantyMaintenanceRequest.setSettlementTotleExpense(
                this.warrantyMaintenanceRequest.getSettlementAccesoriesExpense()        // 应结算辅料费用
                        + this.warrantyMaintenanceRequest.getNightExpense()
//          + this.warrantyMaintenanceRequest.getWorkTimeExpense()          // 应结算工时费用
                        + this.warrantyMaintenanceRequest.getMaintainWorkTimeExpense()  // 维修工时费用
                        + this.warrantyMaintenanceRequest.getOutExpense()               // 应结算外出费用
                        + this.warrantyMaintenanceRequest.getSettlementPartExpense()    // 应结算配件费用
                        + this.warrantyMaintenanceRequest.getOtherExpense()             // 应结算其它费用

        );
    }

    /**
     * 生成供货通知单/调拨单
     */
    @Command
    @NotifyChange("warrantyMaintenanceRequest")
    public void generateSupplyNotice() {
        if (StringUtils.isBlank(warrantyMaintenanceRequest.getObjId())) {
            ZkUtils.showError("三包服务单未保存，不能生成调拨单", "系统提示");
            return;
        }
        if (warrantyMaintenanceRequest.getVehicle() == null) {
            ZkUtils.showError("没有选择车辆,不能生成调拨单", "系统提示");
            return;
        }
        for (CommissionPartEntity commissionParts : warrantyMaintenanceRequest.getCommissionParts()) {
            if (commissionParts.getAmount() == 0) {
                ZkUtils.showError("配件需求数量为0或者没有选择配件信息,不能生成调拨单", "系统提示");
                return;
            }
            if (StringUtils.isBlank(commissionParts.getPartCode())) {
                ZkUtils.showError("配件需求数量为0或者没有选择配件信息,不能生成调拨单", "系统提示");
                return;
            }

        }
        if (StringUtils.isNotBlank(warrantyMaintenanceRequest.getSupplyNoticeId())) {
            ZkUtils.showError("不允许重复生成调拨通知单/供货通知单", "系统提示");
            return;
        }
        //校验附件信息
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getAmeplate())) {
            ZkUtils.showError("请先上传车辆铭牌图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.warrantyMaintenanceRequest.getManual())) {
            ZkUtils.showError("请先上传保养手册首页图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.getWarrantyMaintenanceRequest().getOdometer())) {
            ZkUtils.showError("请先上传里程表图片", "提示");
            return;
        }
        if (StringUtils.isBlank(this.getWarrantyMaintenanceRequest().getFront45())) {
            ZkUtils.showError("请先上传前侧45度图片", "提示");
            return;
        }


        Set<CommissionPartEntity> parts = warrantyMaintenanceRequest.getCommissionParts();

        SupplyNoticeEntity supplyNoticeEntity = new SupplyNoticeEntity();
        supplyNoticeEntity.setSrcDocNo(warrantyMaintenanceRequest.getDocNo());
        supplyNoticeEntity.setSrcDocType("三包服务单");
        supplyNoticeEntity.setSrcDocID(warrantyMaintenanceRequest.getObjId());
        supplyNoticeEntity.setDealerCode(warrantyMaintenanceRequest.getDealerCode());
        supplyNoticeEntity.setDealerName(warrantyMaintenanceRequest.getDealerName());
        supplyNoticeEntity.setProvinceName(warrantyMaintenanceRequest.getProvinceName());
        supplyNoticeEntity.setServiceManager(warrantyMaintenanceRequest.getServiceManager());
        supplyNoticeEntity.setSubmitter(CommonHelper.getActiveUser().getLogId());
        supplyNoticeEntity.setSubmitterName(CommonHelper.getActiveUser().getUsername());
        supplyNoticeEntity.setSubmitterPhone(CommonHelper.getActiveUser().getPhone());
//        supplyNoticeEntity.setOperatorPhone(warrantyMaintenanceRequest.getOperatorPhone());

        List<SupplyNoticeItemEntity> noticeParts = new ArrayList<>();

        for (CommissionPartEntity entity : parts) {
            if (entity.getPartSupplyType().equals("调拨")) {
                SupplyNoticeItemEntity item = new SupplyNoticeItemEntity();
                item.setPartCode(entity.getPartCode());
                item.setPartName(entity.getPartName());
                item.setRequestAmount(Double.valueOf(entity.getAmount()));
                item.setSurplusAmount(entity.getAmount());
                item.setWarrantyTime(entity.getWarrantyTime());
                item.setWarrantyMileage(entity.getWarrantyMileage());
                item.setPattern(entity.getPattern());
                noticeParts.add(item);
            }
        }
        if (noticeParts.size() <= 0) {
            ZkUtils.showExclamation("没有需调拨的物料！", "系统提示");
            return;
        }

        for (SupplyNoticeItemEntity item : noticeParts) {
            supplyNoticeEntity.addNoticeItem(item);
        }

        this.setBaseService(supplyNoticeService);
        this.setEntity(supplyNoticeEntity);

        supplyNoticeEntity = (SupplyNoticeEntity) this.saveEntity(supplyNoticeEntity);

        this.setBaseService(this.warrantyMaintenanceService);
        this.setEntity(this.warrantyMaintenanceRequest);
        this.warrantyMaintenanceRequest.setSupplyNoticeId(supplyNoticeEntity.getObjId());
        warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        ZkUtils.showInformation("已生成调拨单,请到调拨单列表中查看和提交。", "系统提示");

    }

    /**
     * 生成故障件返回通知单
     */
    @Command
    public void generateRecycleNotice() {
        //判断当前登录用户的操作权限
        UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
        Set<RoleEntity> roles = userEntity.getRoles();
        boolean Permissions = true;
        for (RoleEntity role : roles) {
            if (role.getName().equals("审单员")) {
                Permissions = false;
            }
        }
        if (Permissions) {
            ZkUtils.showError("你没有此操作权限", "提示");
            return;
        }

        if (StringUtils.isBlank(warrantyMaintenanceRequest.getObjId())) {
            ZkUtils.showError("三包服务单未保存，不能生成故障件返回通知单", "系统提示");
            return;
        }
        WarrantyMaintenanceEntity warrantyMaintenance = warrantyMaintenanceService.findOneWithOthersById(warrantyMaintenanceRequest.getObjId());

        if (StringUtils.isNotBlank(warrantyMaintenance.getRecycleNoticeId())) {
            ZkUtils.showError("不允许重复生成故障件返回单", "系统提示");
            return;
        }

        Set<CommissionPartEntity> parts = warrantyMaintenanceRequest.getCommissionParts();
        //校验待返回故障件配件信息
        for (CommissionPartEntity part : parts) {
            if (StringUtils.isBlank(part.getPartCode())) {
                ZkUtils.showError("配件需求信息为0或者信息不全", "提示");
                return;
            }

            if (part.getAmount().equals(0)) {
                ZkUtils.showError("配件需求配件信息为0或者信息不全", "提示");
                return;
            }
        }

        RecycleNoticeEntity recycleNoticeEntity = new RecycleNoticeEntity();

        recycleNoticeEntity.setSrcDocNo(warrantyMaintenanceRequest.getDocNo());
        recycleNoticeEntity.setSrcDocType("三包服务单");
        recycleNoticeEntity.setSrcDocID(warrantyMaintenanceRequest.getObjId());
        recycleNoticeEntity.setDealerCode(warrantyMaintenanceRequest.getDealerCode());
        recycleNoticeEntity.setDealerName(warrantyMaintenanceRequest.getDealerName());
        recycleNoticeEntity.setProvinceName(warrantyMaintenanceRequest.getProvinceName());
//        supplyNoticeEntity.setServiceManager(warrantyMaintenanceRequest.getServiceManager());
//        supplyNoticeEntity.setOperatorPhone(warrantyMaintenanceRequest.getOperatorPhone());

        List<RecycleNoticeItemEntity> noticeParts = new ArrayList<>();
        for (CommissionPartEntity entity : parts) {
            if (entity.getRecycle()) {
                RecycleNoticeItemEntity part = new RecycleNoticeItemEntity();
                part.setPartCode(entity.getPartCode());
                part.setPartName(entity.getPartName());
                part.setAmount(entity.getAmount());
                part.setWarrantyMileage(entity.getWarrantyMileage());
                part.setWarrantyTime(entity.getWarrantyTime());
                part.setPattern(entity.getPattern());
                part.setReason(entity.getReason());

                noticeParts.add(part);
            }
        }
        if (noticeParts.size() <= 0) {
            ZkUtils.showExclamation("没有需返回的故障件的物料！", "系统提示");
            return;
        }
        for (RecycleNoticeItemEntity item : noticeParts) {
            recycleNoticeEntity.addNoticeItem(item);
        }

        this.setBaseService(recycleNoticeService);
        this.setEntity(recycleNoticeEntity);
        recycleNoticeEntity = (RecycleNoticeEntity) this.saveEntity(recycleNoticeEntity);
        this.setBaseService(this.warrantyMaintenanceService);
        this.setEntity(this.warrantyMaintenanceRequest);
        this.warrantyMaintenanceRequest.setRecycleNoticeId(recycleNoticeEntity.getObjId());
        warrantyMaintenanceService.save(this.warrantyMaintenanceRequest);
        ZkUtils.showInformation("已生成故障件返回通知单,请到障件返回通知单列表中查看和提交。", "系统提示");

    }

    //打印服务单
    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.warrantyMaintenanceRequest.getObjId() == null ? "" : this.warrantyMaintenanceRequest.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/asm/warrantyMaintenancePrint.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();

    }

    @Command
    @Override
    public void desert() {
        if (this.warrantyMaintenanceRequest.getStatus().equals(DocStatus.REJECT.getIndex()) && CommonHelper.getActiveUser().getLogId().equals(this.warrantyMaintenanceRequest.getSubmitter())) {
            if (this.warrantyMaintenanceRequest.getSupplyNoticeId() != null) {
                ZkUtils.showError("请删除调拨单在作废", "提示");
                return;
            }
            if (this.warrantyMaintenanceRequest.getRecycleNoticeId() != null) {
                ZkUtils.showError("已经生成返回通知单不能作废", "提示");
                return;

            }

            this.warrantyMaintenanceRequest.setStatus(DocStatus.OBSOLETE.getIndex());

            ZkUtils.showInformation("单据已作废", "提示");
            processService.deleteProcessInstance(this.warrantyMaintenanceRequest.getProcessInstanceId());
            processService.deleteHistoricProcessInstance(this.warrantyMaintenanceRequest.getProcessInstanceId());
        } else {
            ZkUtils.showError("没有此操作权限", "提示");
        }
        this.warrantyMaintenanceRequest.setProcessInstanceId(null);
        warrantyMaintenanceService.save(warrantyMaintenanceRequest);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }

    @Override
    public Boolean checkCanEditSupply() {
        return this.warrantyMaintenanceRequest.getCanEditSupply();
    }

    @Override
    public Boolean checkCanEditRecycle() {
        return this.warrantyMaintenanceRequest.getCanEditRecycle();
    }


    public List<DictionaryEntity> getRepairTypes() {
        return repairTypes;
    }

    public void setRepairTypes(List<DictionaryEntity> repairTypes) {
        this.repairTypes = repairTypes;
    }

    public CommissionPartEntity getCommissionPartEntity() {
        return commissionPartEntity;
    }

    public void setCommissionPartEntity(CommissionPartEntity commissionPartEntity) {
        this.commissionPartEntity = commissionPartEntity;
    }

    public WarrantyMaintenanceEntity getWarrantyMaintenanceRequest() {
        return warrantyMaintenanceRequest;
    }

    public void setWarrantyMaintenanceRequest(WarrantyMaintenanceEntity warrantyMaintenanceRequest) {
        this.warrantyMaintenanceRequest = warrantyMaintenanceRequest;
    }

    public List<DictionaryEntity> getDocTypes() {
        return docTypes;
    }

    public void setDocTypes(List<DictionaryEntity> docTypes) {
        this.docTypes = docTypes;
    }

    public List<PartEntity> getParts() {
        return parts;
    }

    public void setParts(List<PartEntity> parts) {
        this.parts = parts;
    }

    public WarrantyMaintainEntity getWarrantyMaintainEntity() {
        return warrantyMaintainEntity;
    }

    public void setWarrantyMaintainEntity(WarrantyMaintainEntity warrantyMaintainEntity) {
        this.warrantyMaintainEntity = warrantyMaintainEntity;
    }

    public List<MaintainEntity> getMaintains() {
        return maintains;
    }

    public void setMaintains(List<MaintainEntity> maintains) {
        this.maintains = maintains;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }

    public QualityReportEntity getQualityReport() {
        return qualityReport;
    }

    public void setQualityReport(QualityReportEntity qualityReport) {
        this.qualityReport = qualityReport;
    }

    public List<QualityReportEntity> getQualityReports() {
        return qualityReports;
    }

    public ExpenseReportEntity getExpenseReport() {
        return expenseReport;
    }

    public void setExpenseReport(ExpenseReportEntity expenseReport) {
        this.expenseReport = expenseReport;
    }

    public List<ExpenseReportEntity> getExpenseReports() {
        return expenseReports;
    }
}
