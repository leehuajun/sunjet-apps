package com.sunjet.vm.asm;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.*;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.ActivityDistributionService;
import com.sunjet.service.asm.ActivityMaintenanceService;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.service.asm.ActivityVehicleService;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 服务活动 服务单 表单 VM
 */
public class ActivityMaintenanceFormVM extends FlowFormBaseVM {
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private ActivityMaintenanceService activityMaintenanceService;
    @WireVariable
    private ActivityDistributionService activityDistributionService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private VehicleService vehicleService;
    private Window window;

    private ActivityVehicleEntity activityVehicle;
    private List<ActivityVehicleEntity> activityVehicles = new ArrayList<>();
    private ActivityNoticeEntity noticeEntity = new ActivityNoticeEntity();
    private ActivityMaintenanceEntity activityMaintenanceRequest;
    private List<ActivityMaintenanceEntity> activityMaintenanceEntities;
    private List<PartEntity> reportPartlist = new ArrayList<>();
    private List<PartEntity> partListDelete = new ArrayList<>();
    private List<DealerEntity> dealerEntityList = new ArrayList<DealerEntity>();
    private List<GoOutEntity> goOutDeleteList = new ArrayList<>();
    private List<ActivityDistributionEntity> activityDistributionEntities = new ArrayList<>();
    private ActivityDistributionEntity activityDistributionEntity = new ActivityDistributionEntity();

    private List<ActivityDistributionEntity> distributionsEntities = new ArrayList<>();
    private List<ActivityVehicleEntity> distributionVehicles = new ArrayList<>();
    private Map<String, Object> variables = new HashMap<>();


    @Init(superclass = true)
    public void init() {
        activityMaintenanceRequest = new ActivityMaintenanceEntity();
        this.setBaseService(activityMaintenanceService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            activityMaintenanceRequest = activityMaintenanceService.findOneWithOthers(this.getBusinessId());
        } else {
            if (CommonHelper.getActiveUser().getDealer() != null) {
                this.activityMaintenanceRequest.setProvinceName(CommonHelper.getActiveUser().getDealer().getProvince().getName());
                this.activityMaintenanceRequest.setDealerCode(CommonHelper.getActiveUser().getDealer().getCode());
                this.activityMaintenanceRequest.setDealerName(CommonHelper.getActiveUser().getDealer().getName());
                this.activityMaintenanceRequest.setDealerStar(CommonHelper.getActiveUser().getDealer().getStar());
                this.activityMaintenanceRequest.setServiceManager(CommonHelper.getActiveUser().getDealer().getServiceManager() == null ? "" : CommonHelper.getActiveUser().getDealer().getServiceManager().getName());
//                this.activityMaintenanceRequest.setHourPrice(this.getHourPriceByDealer(CommonHelper.getActiveUser().getDealer()));
                this.activityMaintenanceRequest.setHourPrice(this.getHourPriceByDealer(CommonHelper.getActiveUser().getDealer()));
            }
        }
        this.setEntity(activityMaintenanceRequest);

//        ZkUtils.getCurrentDesktop().getExecution().
//        this.setBaseService(configService);
//        this.setKeyword("");
//        this.formUrl = "/views/asm/quality_report_form.zul";
//    this.modelName = ConfigEntity.class.getSimpleName();
//        initList();
    }


//    @Command
//    public void saveActivity() {
//        activityMaintenanceService.save(activityMaintenanceRequest);
//    }

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

        if (this.activityMaintenanceRequest.getActivityVehicle() != null) {
            vehicleService.getRepository().save(this.activityMaintenanceRequest.getActivityVehicle().getVehicle());
        }
//        ActivityVehicleEntity ve = this.activityMaintenanceRequest.getActivityVehicle();
        if (activityVehicle != null) {
            ActivityVehicleEntity ave = (ActivityVehicleEntity) activityVehicleService.getRepository().findOne(activityVehicle.getObjId());
            ave.setRepair(true);
            ave.setRepairDate(new Date());
            activityVehicleService.getRepository().save(ave);

            FlowDocEntity entity = this.saveEntity(this.activityMaintenanceRequest);
            this.activityMaintenanceRequest = activityMaintenanceService.findOneWithOthers(entity.getObjId());
            this.setEntity(this.activityMaintenanceRequest);
            //      this.setEntity(this.saveEntity(this.activityMaintenanceRequest));
            showDialog();
        } else {
            FlowDocEntity entity = this.saveEntity(this.activityMaintenanceRequest);
            this.activityMaintenanceRequest = activityMaintenanceService.findOneWithOthers(entity.getObjId());
            this.setEntity(this.activityMaintenanceRequest);
            //      this.setEntity(this.saveEntity(this.activityMaintenanceRequest));
            showDialog();
        }


    }

    @Override
    protected Boolean checkValid() {
        if (this.activityMaintenanceRequest.getEndDate().getTime() <= this.activityMaintenanceRequest.getStartDate().getTime()) {
            ZkUtils.showExclamation("【完工日期】必须大于【开工日期】！", "系统提示");
            return false;
        }
        if (this.activityMaintenanceRequest.getPullOutDate().getTime() <= this.activityMaintenanceRequest.getPullInDate().getTime()) {
            ZkUtils.showExclamation("【出站时间】必须大于【进站时间】！", "系统提示");
            return false;
        }
        if (this.activityMaintenanceRequest.getActivityVehicle() == null) {
            ZkUtils.showExclamation("请选择车辆！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.activityMaintenanceRequest.getSender())
                || StringUtils.isBlank(this.activityMaintenanceRequest.getSenderPhone())
                || StringUtils.isBlank(this.activityMaintenanceRequest.getRepairer())) {
            ZkUtils.showExclamation("请填写维修信息！", "系统提示");
            return false;
        }
        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
        if (!org.apache.commons.lang3.StringUtils.isNotEmpty(this.getEntity().getObjId())) {
            ZkUtils.showExclamation("请先保存数据再提交！", "系统提示");
            return;
        }
        if (activityVehicle != null) {
            ActivityVehicleEntity ave = (ActivityVehicleEntity) activityVehicleService.getRepository().findOne(activityVehicle.getObjId());
            ave.setRepair(true);
            ave.setRepairDate(new Date());
            activityVehicleService.getRepository().save(ave);
            FlowDocEntity entity = this.saveEntity(this.activityMaintenanceRequest);
            this.activityMaintenanceRequest = activityMaintenanceService.findOneWithOthers(entity.getObjId());
            this.setEntity(this.activityMaintenanceRequest);
            //      this.setEntity(this.saveEntity(this.activityMaintenanceRequest));
//      showDialog();
        } else {
            FlowDocEntity entity = this.saveEntity(this.activityMaintenanceRequest);
            this.activityMaintenanceRequest = activityMaintenanceService.findOneWithOthers(entity.getObjId());
            this.setEntity(this.activityMaintenanceRequest);
            //      this.setEntity(this.saveEntity(this.activityMaintenanceRequest));
//      showDialog();
        }


        variables.put("level", this.getDealer().getLevel());
        variables.put("serviceManager", this.getDealer().getServiceManager().getLogId());
        if (this.getDealer().getParent() != null) {
            List<UserEntity> list = userService.findAllByDealerCode(this.getDealer().getParent().getCode());
            List<String> users = new ArrayList<>();
            for (UserEntity userEntity : list) {
                System.out.println(userEntity.getLogId());
                users.add(userEntity.getLogId());
            }
            variables.put("firstLevelUsers", users);
        }
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

    @Command
    @NotifyChange("*")
    public void addGoOut() {
        System.out.println("增加一行");
        this.activityMaintenanceRequest.getGoOuts().add(new GoOutEntity());
    }

    @Command
    @NotifyChange("*")
    public void deleteGoOut(@BindingParam("model") GoOutEntity entity) {
        this.activityMaintenanceRequest.getGoOuts().remove(entity);
        computeCost();
    }

    @Command
    @NotifyChange("*")
    public void searchVehicles() {
        if (this.activityMaintenanceRequest.getActivityDistribution() == null) {
            ZkUtils.showExclamation("请先选择活动分配单!", "系统提示");
            return;
        }
//        this.setKeyword("");

        String filter = this.getKeyword().toLowerCase().trim();

        this.activityVehicles.clear();
        this.activityDistributionEntity = activityDistributionService.findOneWithVehicles(this.activityMaintenanceRequest.getActivityDistribution().getObjId());
//        this.activityVehicles.addAll(activityDistributionEntity.getActivityVehicles());

        Stream<ActivityVehicleEntity> stream = activityDistributionEntity.getActivityVehicles().parallelStream().filter(new Predicate<ActivityVehicleEntity>() {
            @Override
            public boolean test(ActivityVehicleEntity activityVehicleEntity) {
                if (activityVehicleEntity.getRepair() == true)
                    return false;
                if (activityVehicleEntity.getVehicle().getVin() != null && activityVehicleEntity.getVehicle().getVin().toLowerCase().contains(filter))
                    return true;
                if (activityVehicleEntity.getVehicle().getVsn() != null && activityVehicleEntity.getVehicle().getVsn().toLowerCase().contains(filter))
                    return true;

                return false;
            }
        });
        this.activityVehicles.clear();
        this.activityVehicles.addAll(stream.collect(Collectors.toList()));
    }

    @Command
    @NotifyChange("*")
    public void selectVehicle(@BindingParam("model") ActivityVehicleEntity activityVehicleEntity) {
//        if (activityVehicleEntity.getObjId()!=null) {
        this.setKeyword("");
        activityVehicle = activityVehicleEntity;
        ActivityVehicleEntity ave = new ActivityVehicleEntity();
        ave.setVehicle(activityVehicleEntity.getVehicle());

        activityMaintenanceRequest.setActivityVehicle(ave);
        activityMaintenanceRequest.setVmt(activityVehicleEntity.getVehicle().getVmt() == null ? "0" : activityVehicleEntity.getVehicle().getVmt());
    }

    @Command
    @NotifyChange("distributionsEntities")
    public void searchActivityDistributions() {
        if (CommonHelper.getActiveUser().getDealer() == null) {
            ZkUtils.showExclamation("请以服务站用户登录", "系统提示");
            return;
        }

        this.distributionsEntities = activityDistributionService.findAllByStatusAndKeywordAndDealerCode(DocStatus.CLOSED, this.getKeyword().trim(), CommonHelper.getActiveUser().getDealer().getCode());
        System.out.println(this.distributionsEntities.size());
    }

    @Command
    @NotifyChange("*")
    public void selectActivityDistribution(@BindingParam("model") ActivityDistributionEntity distributionEntity) {
//        this.activityMaintenanceRequest.getActivityParts().clear();
        this.activityVehicles.clear();
        this.activityMaintenanceRequest.setActivityVehicle(null);
        activityMaintenanceRequest.setStandardExpense(distributionEntity.getActivityNotice().getPerLaberCost());
        this.activityMaintenanceRequest.setActivityDistribution(distributionEntity);
        this.activityMaintenanceRequest.getCommissionParts().clear();
        List<ActivityPartEntity> activityParts = activityNoticeService.findActivityPartListByNoticeId(distributionEntity.getActivityNotice().getObjId());
        for (ActivityPartEntity part : activityParts) {
            CommissionPartEntity commissionPart = new CommissionPartEntity();
            if (part.getPart() != null) {
                commissionPart.setPartCode(part.getPart().getCode());
                commissionPart.setPartName(part.getPart().getName());
                commissionPart.setPartType(part.getPart().getPartType());
                commissionPart.setUnit(part.getPart().getUnit());
                commissionPart.setPrice(part.getPart().getPrice());
                commissionPart.setAmount(part.getAmount());
                commissionPart.setRecycle(true);
                this.activityMaintenanceRequest.getCommissionParts().add(commissionPart);
            }

        }
        this.setKeyword("");
        this.distributionsEntities.clear();
        this.computeCost();

    }

    /**
     * 统计费用
     */
    @Command
    @NotifyChange("*")
    public void computeCost() {
        if (this.activityMaintenanceRequest.getOtherExpense() == null) {
            this.activityMaintenanceRequest.setOtherExpense(0.0);
        }
        this.activityMaintenanceRequest.setExpenseTotal(0.0);           // 总合计费用
//    this.activityMaintenanceRequest.setOtherExpense(0.0);           // 其它费用
        this.activityMaintenanceRequest.setOutExpense(0.0);             // 外出费用合计
        this.activityMaintenanceRequest.setOutHours(0.0);

        Double outKm = 0.0;
        Boolean isOut = false;

        this.activityMaintenanceRequest.setNightExpense(0.0);
        if (this.activityMaintenanceRequest.getNightWork()) {  // 夜间作业
            this.activityMaintenanceRequest.setNightExpense(Double.parseDouble(CacheManager.getConfigValue("cost_night")));
        }

        for (GoOutEntity entity : this.activityMaintenanceRequest.getGoOuts()) {
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
                entity.setGoOutSubsidy(entity.getTimeSubsidy()
                        * this.activityMaintenanceRequest.getHourPrice());     // 外出补贴费用
                // 单项外出费用统计
                entity.setAmountCost(entity.getTranCosts()  // 交通费用
                        + entity.getTrailerCost()           // 拖车费用
                        + entity.getPersonnelSubsidy()      // 人员补贴
                        + entity.getNightSubsidy());          // 住宿补贴
//                        + entity.getGoOutSubsidy());        // 外出补贴费用

                // 工时补贴合计
//                this.activityMaintenanceRequest.setOutHours(this.activityMaintenanceRequest.getOutHours()
//                        + entity.getTimeSubsidy());

                // 外出费用合计
                this.activityMaintenanceRequest.setOutExpense(this.activityMaintenanceRequest.getOutExpense()
                        + entity.getAmountCost());
            }
        }

        // 计算出来的工时补贴费用合计
        Double oriHourExpense = this.activityMaintenanceRequest.getHourPrice() * this.activityMaintenanceRequest.getOutHours();
        this.activityMaintenanceRequest.setHourExpense(oriHourExpense);
        if (isOut) {    // 有外出记录


            // 工时补贴费用合计
            if (outKm <= Double.parseDouble(CacheManager.getConfigValue("km_interval"))) {   // 外出总里程 < 50 公里
//        if (this.activityMaintenanceRequest.getHourExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))) {
//          this.activityMaintenanceRequest.setHourExpense(Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval")));
//          this.activityMaintenanceRequest.setOutExpense(
//              this.activityMaintenanceRequest.getOutExpense()
//                  - oriHourExpense
//                  + this.activityMaintenanceRequest.getHourExpense());
//        }
                if (this.activityMaintenanceRequest.getStandardExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))) {
                    this.activityMaintenanceRequest.setHourExpense(Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))
                            - this.activityMaintenanceRequest.getStandardExpense());
                }
            } else {   // 外出里程 > 50
//        if (this.activityMaintenanceRequest.getHourExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))) {
//          this.activityMaintenanceRequest.setHourExpense(Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval")));
//          this.activityMaintenanceRequest.setOutExpense(
//              this.activityMaintenanceRequest.getOutExpense()
//                  - oriHourExpense
//                  + this.activityMaintenanceRequest.getHourExpense());
//        }
                if (this.activityMaintenanceRequest.getStandardExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))) {
                    this.activityMaintenanceRequest.setHourExpense(Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))
                            - this.activityMaintenanceRequest.getStandardExpense());
                }
            }
        }

        this.activityMaintenanceRequest.setOutExpense(this.activityMaintenanceRequest.getOutExpense()
                + this.activityMaintenanceRequest.getHourExpense());

        // 总费用
        this.activityMaintenanceRequest.setExpenseTotal(
                +this.activityMaintenanceRequest.getStandardExpense()  // 活动标准费用
                        + this.activityMaintenanceRequest.getNightExpense()
                        + this.activityMaintenanceRequest.getOutExpense()             // 外出费用
//            + this.activityMaintenanceRequest.getHourExpense()            // 外出工时补贴费用
                        + this.activityMaintenanceRequest.getOtherExpense()           // 其它费用
        );

//    this.activityMaintenanceRequest.setExpenseTotal(MathHelper.getDoubleAndTwoDecimalPlaces(this.activityMaintenanceRequest.getExpenseTotal()));
//    this.activityMaintenanceRequest.setHourExpense(MathHelper.getDoubleAndTwoDecimalPlaces(this.activityMaintenanceRequest.getHourExpense()));
//    this.activityMaintenanceRequest.setOutExpense(MathHelper.getDoubleAndTwoDecimalPlaces(this.activityMaintenanceRequest.getOutExpense()));
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
        for (GoOutEntity outEntity : this.activityMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture(fileName);
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
     * 删除外出凭证
     *
     * @param goOutitem
     */
    @Command
    @NotifyChange("*")
    public void deleteOutGoFile(@BindingParam("each") GoOutEntity goOutitem) {
        for (GoOutEntity outEntity : this.activityMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture("");
            }

        }

    }

    /**
     * 打印报表
     */
    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();

        map.put("objId", this.activityMaintenanceRequest.getObjId() == null ? "" : this.activityMaintenanceRequest.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/asm/activity_maintenance.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }


    /**
     * 单据作废
     */
    @Command
    @NotifyChange("*")
    @Override
    public void desert() {
        ActivityMaintenanceEntity activityMaintenance = activityMaintenanceService.findOneWithVehicles(this.activityMaintenanceRequest.getObjId());
        if (activityMaintenance.getActivityDistribution() != null) {
            ActivityDistributionEntity distributionEntity = activityDistributionService.findOneWithVehicles(activityMaintenance.getActivityDistribution().getObjId());
            for (ActivityVehicleEntity ave : distributionEntity.getActivityVehicles()) {
                if (activityMaintenance.getActivityVehicle() != null && ave.getVehicle().getVin().equals(activityMaintenance.getActivityVehicle().getVehicle().getVin())) {
                    ave.setRepair(false);
                    ave.setRepairDate(null);
                    activityVehicleService.getRepository().save(ave);
                }
            }
        }
        super.desert();
    }

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }


    public List<ActivityVehicleEntity> getDistributionVehicles() {
        return distributionVehicles;
    }

    public void setDistributionVehicles(List<ActivityVehicleEntity> distributionVehicles) {
        this.distributionVehicles = distributionVehicles;
    }

    public List<ActivityDistributionEntity> getDistributionsEntities() {
        return distributionsEntities;
    }


    public ActivityDistributionEntity getActivityDistributionEntity() {
        return activityDistributionEntity;
    }

    public void setActivityDistributionEntity(ActivityDistributionEntity activityDistributionEntity) {
        this.activityDistributionEntity = activityDistributionEntity;
    }

    public List<ActivityDistributionEntity> getActivityDistributionEntities() {
        return activityDistributionEntities;
    }

    public void setActivityDistributionEntities(List<ActivityDistributionEntity> activityDistributionEntities) {
        this.activityDistributionEntities = activityDistributionEntities;
    }

    public List<DealerEntity> getDealerEntityList() {
        return dealerEntityList;
    }

    public List<PartEntity> getReportPartlist() {
        return reportPartlist;
    }

    public List<PartEntity> getPartListDelete() {
        return partListDelete;
    }

    public List<ActivityMaintenanceEntity> getActivityMaintenanceEntities() {
        return activityMaintenanceEntities;
    }

    public ActivityMaintenanceEntity getActivityMaintenanceRequest() {
        return activityMaintenanceRequest;
    }

    public void setActivityMaintenanceRequest(ActivityMaintenanceEntity activityMaintenanceRequest) {
        this.activityMaintenanceRequest = activityMaintenanceRequest;
    }

    public ActivityNoticeEntity getNoticeEntity() {
        return noticeEntity;
    }

    public void setNoticeEntity(ActivityNoticeEntity noticeEntity) {
        this.noticeEntity = noticeEntity;
    }

    public List<ActivityVehicleEntity> getActivityVehicles() {
        return activityVehicles;
    }

    public void setActivityVehicles(List<ActivityVehicleEntity> activityVehicles) {
        this.activityVehicles = activityVehicles;
    }
}

