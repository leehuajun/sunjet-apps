package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.VehicleEntity;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 首保服务单
 */
@Entity
@Table(name = "AsmFirstMaintenanceDocs")
public class FirstMaintenanceEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6991361911592663788L;
    private String operator;        // 经办人
    private String operatorPhone;   // 经办人电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String dealerStar;      //服务站星级
    private String provinceName;    // 省份
    private Date repairDate = new Date();        // 报修日期
    private Date pullInDate = new Date();        // 进站时间
    private Date pullOutDate = new Date();       // 出站时间
    private Date requestDate = new Date();       // 申请时间  日历控件，选择项，默认当前时间，可改

    private VehicleEntity vehicle;  // 车辆
    // 维修信息:    必输项：送修人、送修人电话
    private String vmt;                    // 行驶里程
    private String sender;          // 送修人
    private String senderPhone;     // 送修人电话
    private String repairer;        // 主修工
    private Date startDate = new Date();         // 开工日期
    private Date endDate = new Date();           // 完工日期
    private String fault;           // 故障描述
    private Boolean takeAwayOldPart; // 带走旧件  单选，选项内容：是、否；
    private Boolean washing;        // 洗车    单选，选项内容：是、否；
    //首保凭证文件
    private String ameplate;          // 铭牌
    private String manual;            // 保养手册
    private String odometer;          // 里程表
    private String invoice;           // 购买发票
    private String front45;           // 前侧45度
    //费用信息
    private Boolean nightWork = false;  // 夜间作业
    private Double nightExpense = 0.0;  // 夜间补贴
    private Double hourPrice = 0.0;     // 工时单价
    private Double hours = 0.0;         // 工时补贴合计
    private Double hourExpense = 0.0;   // 工时补贴合计费用
    private String standardExpenseId;   // 首保费用标准ID
    private Double standardExpense = 0.00; // 首保标准费用
    private Double outExpense = 0.00;      // 外出费用   系统带出，不能更改，计算公式：∑外出费用
    private Double otherExpense = 0.00;   //  其他费用
    private Double expenseTotal = 0.00;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private List<GoOutEntity> goOuts = new ArrayList<>(); //出外子行
    private boolean settlement = false;//是否结算
    private String comment; //备注

    @Column(length = 50)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(length = 20)
    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    @Column(length = 50)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Column(length = 50)
    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    @Column(length = 100)
    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Column(length = 50)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    public Date getPullInDate() {
        return pullInDate;
    }

    public void setPullInDate(Date pullInDate) {
        this.pullInDate = pullInDate;
    }

    public Date getPullOutDate() {
        return pullOutDate;
    }

    public void setPullOutDate(Date pullOutDate) {
        this.pullOutDate = pullOutDate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }


    @Column(length = 20)
    public String getVmt() {
        return vmt;
    }

    public void setVmt(String vmt) {
        this.vmt = vmt;
    }

    @Column(length = 50)
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Column(length = 20)
    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    @Column(length = 50)
    public String getRepairer() {
        return repairer;
    }

    public void setRepairer(String repairer) {
        this.repairer = repairer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(length = 200)
    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public Boolean getTakeAwayOldPart() {
        return takeAwayOldPart;
    }

    public void setTakeAwayOldPart(Boolean takeAwayOldPart) {
        this.takeAwayOldPart = takeAwayOldPart;
    }

    public Boolean getWashing() {
        return washing;
    }

    public void setWashing(Boolean washing) {
        this.washing = washing;
    }

    @Column(length = 200)
    public String getAmeplate() {
        return ameplate;
    }

    public void setAmeplate(String ameplate) {
        this.ameplate = ameplate;
    }

    @Column(length = 200)
    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    @Column(length = 200)
    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    @Column(length = 200)
    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    @Column(length = 200)
    public String getFront45() {
        return front45;
    }

    public void setFront45(String front45) {
        this.front45 = front45;
    }


    @Column(length = 32)
    public String getStandardExpenseId() {
        return standardExpenseId;
    }

    public void setStandardExpenseId(String standardExpenseId) {
        this.standardExpenseId = standardExpenseId;
    }


    public Double getNightExpense() {
        return nightExpense;
    }

    public void setNightExpense(Double nightExpense) {
        this.nightExpense = nightExpense;
    }

    public Boolean getNightWork() {
        return nightWork;
    }

    public void setNightWork(Boolean nightWork) {
        this.nightWork = nightWork;
    }

    public Double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Double hourPrice) {
        this.hourPrice = hourPrice;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Double getHourExpense() {
        return hourExpense;
    }

    public void setHourExpense(Double hourExpense) {
        this.hourExpense = hourExpense;
    }

    public Double getStandardExpense() {
        return standardExpense;
    }

    public void setStandardExpense(Double standardExpense) {
        this.standardExpense = standardExpense;
    }

    public Double getOutExpense() {
        return outExpense;
    }

    public void setOutExpense(Double outExpense) {
        this.outExpense = outExpense;
    }

    public Double getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(Double otherExpense) {
        this.otherExpense = otherExpense;
    }

    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "VehicleId")
    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "FirstMaintenanceId")
    public List<GoOutEntity> getGoOuts() {
        return goOuts;
    }

    public void setGoOuts(List<GoOutEntity> goOuts) {
        this.goOuts = goOuts;
    }

    public boolean isSettlement() {
        return settlement;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }

    public String getDealerStar() {
        return dealerStar;
    }

    public void setDealerStar(String dealerStar) {
        this.dealerStar = dealerStar;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
