package com.sunjet.vm.asm;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.FirstMaintenanceEntity;
import com.sunjet.model.asm.GoOutEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.FirstMaintenanceService;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.common.CommonHelper;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

//import org.apache.commons.beanutils.BeanUtils;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 首保服务单 表单 VM
 */
public class FirstMaintenanceFormVM extends FlowFormBaseVM {
    @WireVariable
    private FirstMaintenanceService firstMaintenanceService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private VehicleService vehicleService;


    private FirstMaintenanceEntity firstMaintenanceRequest;
    private VehicleEntity vehicle;
    private List<VehicleEntity> vehicles = new ArrayList<>();
    private String keyword = "";
    private List<DictionaryEntity> fmExpenseStandards = new ArrayList<>();
    private DictionaryEntity selectedExpenseStandard;
    private Window window;
    private Map<String, Object> variables = new HashMap<>();

    @Init(superclass = true)
    public void init() {

        this.setBaseService(firstMaintenanceService);


        if (StringUtils.isNotBlank(this.getBusinessId())) {
//            this.firstMaintenanceRequest = firstMaintenanceService.findOneById(this.getBusinessId());
            this.firstMaintenanceRequest = firstMaintenanceService.findOneWithGoOutsById(this.getBusinessId());
//            this.setEntity(this.firstMaintenanceRequest);
        } else {
            this.firstMaintenanceRequest = new FirstMaintenanceEntity();

            DealerEntity dealerEntity = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
            if (dealerEntity != null) {
                this.firstMaintenanceRequest.setDealerCode(dealerEntity.getCode());
                this.firstMaintenanceRequest.setDealerName(dealerEntity.getName());
                this.firstMaintenanceRequest.setDealerStar(CommonHelper.getActiveUser().getDealer().getStar());
//                this.firstMaintenanceRequest.setServiceManager(dealerEntity.getServiceManager()==null?"":dealerEntity.getServiceManager().getName());
                this.firstMaintenanceRequest.setServiceManager(dealerEntity.getServiceManager() == null ? "" : dealerEntity.getServiceManager().getName());
                this.firstMaintenanceRequest.setHourPrice(this.getHourPriceByDealer(dealerEntity));
            }
//            this.setEntity(this.firstMaintenanceRequest);
        }


//        this.firstMaintenanceRequest.setServiceManager(this);

        this.setEntity(this.firstMaintenanceRequest);

        this.fmExpenseStandards = CacheManager.getDictionariesByParentCode("13000");

        if (StringUtils.isNotBlank(this.firstMaintenanceRequest.getStandardExpenseId())) {
            for (DictionaryEntity entity : this.fmExpenseStandards) {
                if (entity.getObjId().equals(this.firstMaintenanceRequest.getStandardExpenseId())) {
                    this.selectedExpenseStandard = entity;
                    break;
                }
            }
        }
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
        if (this.firstMaintenanceRequest.getVehicle() != null) {
            vehicleService.getRepository().save(this.firstMaintenanceRequest.getVehicle());
        }
        FlowDocEntity entity = this.saveEntity(this.firstMaintenanceRequest);
        this.firstMaintenanceRequest = firstMaintenanceService.findOneWithGoOutsById(entity.getObjId());
        this.setEntity(this.firstMaintenanceRequest);
//        this.setEntity(this.saveEntity(this.firstMaintenanceRequest));
        showDialog();
    }

    @Override
    protected Boolean checkValid() {
        if (this.firstMaintenanceRequest.getEndDate().getTime() <= this.firstMaintenanceRequest.getStartDate().getTime()) {
            ZkUtils.showExclamation("【完工日期】必须大于【开工日期】！", "系统提示");
            return false;
        }
        if (this.firstMaintenanceRequest.getPullOutDate().getTime() <= this.firstMaintenanceRequest.getPullInDate().getTime()) {
            ZkUtils.showExclamation("【出站时间】必须大于【进站时间】！", "系统提示");
            return false;
        }
        if (this.selectedExpenseStandard == null) {
            ZkUtils.showExclamation("请选择【首保费用标准】！", "系统提示");
            return false;
        }
        if (this.firstMaintenanceRequest.getVehicle() == null) {
            ZkUtils.showExclamation("请选择车辆！", "系统提示");
            return false;
        }
//        if (StringUtils.isBlank(this.firstMaintenanceRequest.getAmeplate())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getManual())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getOdometer())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getInvoice())
//                || StringUtils.isBlank(this.firstMaintenanceRequest.getFront45())) {
//            ZkUtils.showExclamation("请上传所有凭证文件！", "系统提示");
//            return false;
//        }
        if (StringUtils.isBlank(this.firstMaintenanceRequest.getAmeplate())) {
            ZkUtils.showExclamation("请上传车辆铭牌凭证文件！", "系统提示");
            return false;
        } else if (StringUtils.isBlank(this.firstMaintenanceRequest.getManual())) {
            ZkUtils.showExclamation("请上传保养手册首页凭证文件！", "系统提示");
            return false;
        } else if (StringUtils.isBlank(this.firstMaintenanceRequest.getOdometer())) {
            ZkUtils.showExclamation("请上传里程表凭证文件！", "系统提示");
            return false;
        } else if (StringUtils.isBlank(this.firstMaintenanceRequest.getFront45())) {
            ZkUtils.showExclamation("请上传前侧45度照片凭证文件！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.firstMaintenanceRequest.getSender())
                || StringUtils.isBlank(this.firstMaintenanceRequest.getSenderPhone())
                || StringUtils.isBlank(this.firstMaintenanceRequest.getRepairer())) {
            ZkUtils.showExclamation("请填写维修信息！", "系统提示");
            return false;
        }

        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();

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


    @Override
    protected void completeTask(String outcome, String comment) throws IOException {
        super.completeTask(outcome, comment);
        UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
        Set<RoleEntity> roles = userEntity.getRoles();
        for (RoleEntity role : roles) {
            if (("审单员".equals(role.getName()) || "结算主管".equals(role.getName())) && "同意".equals(outcome)) {
                this.firstMaintenanceRequest.getVehicle().setFmDate(new Date());
                vehicleService.getRepository().save(this.firstMaintenanceRequest.getVehicle());
                this.setEntity(this.firstMaintenanceRequest);
            }
        }


    }

    //@Override
    //protected void showDialog() {
    //    super.showDialog();
    //    if(this.firstMaintenanceRequest.getStatus().equals(DocStatus.CLOSED.getIndex() )){
    //        this.firstMaintenanceRequest.getVehicle().setFmDate(new Date());
    //        vehicleService.getRepository().save(this.firstMaintenanceRequest.getVehicle());
    //        this.setEntity(this.firstMaintenanceRequest);
    //    }
    //
    //}

    @Command
    @NotifyChange("*")
    public void selectExpenseStandard() {
        firstMaintenanceRequest.setStandardExpense(Double.parseDouble(this.selectedExpenseStandard.getValue()));
    }

    @Command
    @NotifyChange("*")
    public void changeStandardExpense() {
        firstMaintenanceRequest.setStandardExpense(Double.parseDouble(this.selectedExpenseStandard.getValue()));

        this.firstMaintenanceRequest.setExpenseTotal(this.firstMaintenanceRequest.getOutExpense()
                + this.firstMaintenanceRequest.getStandardExpense()
                + this.firstMaintenanceRequest.getOtherExpense());

        firstMaintenanceRequest.setStandardExpenseId(this.selectedExpenseStandard.getObjId());

        this.computeCost();
    }

    @Command
    @NotifyChange("*")
    public void computeCost() {
        this.firstMaintenanceRequest.setOutExpense(0.0);
        this.firstMaintenanceRequest.setHours(0.0);
        if (this.firstMaintenanceRequest.getOtherExpense() == null) {
            this.firstMaintenanceRequest.setOtherExpense(0.0);
        }
        Double outKm = 0.0;
        Boolean isOut = false;
        Double outHourExpressTmp = 0.0;    // 外出工时补贴费用
        this.firstMaintenanceRequest.setNightExpense(0.0);
        if (this.firstMaintenanceRequest.getNightWork()) {  // 夜间作业
            this.firstMaintenanceRequest.setNightExpense(Double.parseDouble(CacheManager.getConfigValue("cost_night")));
        }

        for (GoOutEntity entity : this.firstMaintenanceRequest.getGoOuts()) {


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
//            entity.setGoOutSubsidy(entity.getTimeSubsidy()
//                    * this.firstMaintenanceRequest.getHourPrice());     // 外出补贴费用

//                this.firstMaintenanceRequest.setHours(this.firstMaintenanceRequest.getHours() + entity.getTimeSubsidy());

//                // 临时统计所有行的外出补贴费用合计
//                outHourExpressTmp = outHourExpressTmp + entity.getGoOutSubsidy();

            // 单项外出费用统计
            entity.setAmountCost(entity.getTranCosts()   // 交通费用
                    + entity.getTrailerCost()            // 拖车费用
                    + entity.getPersonnelSubsidy()       // 人员补贴
                    + entity.getNightSubsidy());           // 住宿补贴
//                    + entity.getGoOutSubsidy());         // 外出补贴费用 （此处不计算工时补贴，应有特殊情况处理）

            if (StringUtils.isNotBlank(entity.getPlace())) {   // 外出地点不为空时，才进行计算
                isOut = true;       // 目的地不为空，表示确实有外出服务
                // 工时补贴合计
//                this.firstMaintenanceRequest.setHours(this.firstMaintenanceRequest.getHours()
//                        + entity.getTimeSubsidy());

                // 外出费用合计
                this.firstMaintenanceRequest.setOutExpense(this.firstMaintenanceRequest.getOutExpense()
                        + entity.getAmountCost());
            }
        }
        // 计算出来的工时补贴费用合计
//        Double oriHourExpense = this.firstMaintenanceRequest.getHourPrice() * this.firstMaintenanceRequest.getHours();
//        this.firstMaintenanceRequest.setHourExpense(oriHourExpense);
//        if (isOut) {    // 有外出记录
//            // 工时补贴费用合计
//            if (outKm <= Double.parseDouble(CacheManager.getConfigValue("km_interval"))) {   // 外出总里程 < 50 公里
//                if (this.firstMaintenanceRequest.getHourExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval"))) {
//                    this.firstMaintenanceRequest.setHourExpense(Double.parseDouble(CacheManager.getConfigValue("cost_less_km_interval")));
//                    this.firstMaintenanceRequest.setOutExpense(
//                            this.firstMaintenanceRequest.getOutExpense()
//                                    - oriHourExpense
//                                    + this.firstMaintenanceRequest.getHourExpense());
//                }
//            } else {   // 外出里程 > 50
//                if (this.firstMaintenanceRequest.getHourExpense() < Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval"))) {
//                    this.firstMaintenanceRequest.setHourExpense(Double.parseDouble(CacheManager.getConfigValue("cost_greater_km_interval")));
//                    this.firstMaintenanceRequest.setOutExpense(
//                            this.firstMaintenanceRequest.getOutExpense()
//                                    - oriHourExpense
//                                    + this.firstMaintenanceRequest.getHourExpense());
//                }
//            }
//        }
        // 所有费用统计
//        if (needSub) {
//            this.firstMaintenanceRequest.setOutExpense(this.firstMaintenanceRequest.getOutExpense() - oriHourExpense);
//        }

        this.firstMaintenanceRequest.setExpenseTotal(
                this.firstMaintenanceRequest.getOutExpense()
                        + this.firstMaintenanceRequest.getNightExpense()
                        + this.firstMaintenanceRequest.getStandardExpense()
                        + this.firstMaintenanceRequest.getOtherExpense());


        // 全部转换成2位小数
//        this.firstMaintenanceRequest.setHourExpense(MathHelper.getDoubleAndTwoDecimalPlaces(this.firstMaintenanceRequest.getHourExpense()));
//        this.firstMaintenanceRequest.setOutExpense(MathHelper.getDoubleAndTwoDecimalPlaces(this.firstMaintenanceRequest.getOutExpense()));
//        this.firstMaintenanceRequest.setExpenseTotal(MathHelper.getDoubleAndTwoDecimalPlaces(this.firstMaintenanceRequest.getExpenseTotal()));
//
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

//    @Command
//    public void saveFirstMaintenance() {
//        firstMaintenanceService.save(firstMaintenanceRequest);
//
//    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t01")) {  // 车辆名牌
            this.firstMaintenanceRequest.setAmeplate(fileName);
        } else if (type.equalsIgnoreCase("t02")) {   //保养手册首页
            this.firstMaintenanceRequest.setManual(fileName);
        } else if (type.equalsIgnoreCase("t03")) {   //里程表
            this.firstMaintenanceRequest.setOdometer(fileName);
        } else if (type.equalsIgnoreCase("t04")) {   //购买发票
            this.firstMaintenanceRequest.setInvoice(fileName);
        } else if (type.equalsIgnoreCase("t05")) {   //车位前侧45度照片
            this.firstMaintenanceRequest.setFront45(fileName);
        }
    }

    @Command
    @NotifyChange("firstMaintenanceRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {  // 车辆名牌
            this.firstMaintenanceRequest.setAmeplate("");
        } else if (type.equalsIgnoreCase("t02")) {   //保养手册首页
            this.firstMaintenanceRequest.setManual("");
        } else if (type.equalsIgnoreCase("t03")) {   //里程表
            this.firstMaintenanceRequest.setOdometer("");
        } else if (type.equalsIgnoreCase("t04")) {   //购买发票
            this.firstMaintenanceRequest.setInvoice("");
        } else if (type.equalsIgnoreCase("t05")) {   //车位前侧45度照片
            this.firstMaintenanceRequest.setFront45("");
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
        for (GoOutEntity outEntity : this.firstMaintenanceRequest.getGoOuts()) {
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
        for (GoOutEntity outEntity : this.firstMaintenanceRequest.getGoOuts()) {
            if (outEntity.equals(goOutitem)) {
                outEntity.setOutGoPicture("");
            }

        }

    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_FLOW + filename;
    }

    @Command
    @NotifyChange("*")
    public void searchVehicles() {
        if (this.keyword.trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
            this.vehicles = vehicleService.findAllByKeywordAndFmDateIsNull(this.keyword.trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
        }
    }

    @Command
    @NotifyChange("*")
    public void selectVehicle(@BindingParam("model") VehicleEntity vehicleEntity) throws InvocationTargetException, IllegalAccessException {
        this.firstMaintenanceRequest.setVehicle(vehicleEntity);
        this.firstMaintenanceRequest.setVmt(vehicleEntity.getVmt());


        //if(this.firstMaintenanceRequest.getVehicle().getFmDate()==null){
        //    this.firstMaintenanceRequest.getVehicle().setFmDate(new Date());
        //}else {
        //    VehicleEntity  vehicleEnti = (VehicleEntity) vehicleService.getRepository().findOne(this.firstMaintenanceRequest.getVehicle());
        //    vehicleEntity.setFmDate(null);
        //    vehicleService.getRepository().save(vehicleEnti);
        //    this.firstMaintenanceRequest.getVehicle().setFmDate(new Date());
        //
        //
        //}

    }

    @Command
    @NotifyChange("*")
    public void addGoOut() {
        System.out.println("增加一行");
        this.firstMaintenanceRequest.getGoOuts().add(new GoOutEntity());
    }

    @Command
    @NotifyChange("*")
    public void deleteGoOut(@BindingParam("model") GoOutEntity entity) {
        this.firstMaintenanceRequest.getGoOuts().remove(entity);
        this.computeCost();
    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();

        map.put("objId", this.firstMaintenanceRequest.getObjId() == null ? "" : this.firstMaintenanceRequest.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/asm/first_maintenance.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public FirstMaintenanceEntity getFirstMaintenanceRequest() {
        return firstMaintenanceRequest;
    }

    public void setFirstMaintenanceRequest(FirstMaintenanceEntity firstMaintenanceRequest) {
        this.firstMaintenanceRequest = firstMaintenanceRequest;
    }

    public DictionaryEntity getSelectedExpenseStandard() {
        return selectedExpenseStandard;
    }

    public void setSelectedExpenseStandard(DictionaryEntity selectedExpenseStandard) {
        this.selectedExpenseStandard = selectedExpenseStandard;
    }

    public List<DictionaryEntity> getFmExpenseStandards() {
        return fmExpenseStandards;
    }

}
