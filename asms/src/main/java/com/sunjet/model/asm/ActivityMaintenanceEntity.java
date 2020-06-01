package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.VehicleEntity;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 活动服务单
 */
@Entity
@Table(name = "AsmActivityMaintenances")
public class ActivityMaintenanceEntity extends FlowDocEntity {
    private static final long serialVersionUID = -157423414398173934L;
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
    private ActivityDistributionEntity activityDistribution;   //活动发布单
    private ActivityVehicleEntity activityVehicle; // 车辆
    private Set<CommissionPartEntity> commissionParts = new HashSet<>();           // 检查结果，零件信息
    private String vmt;             // 行驶里程
    // 维修信息:    必输项：送修人、送修人电话
    private String sender;          // 送修人
    private String senderPhone;     // 送修人电话
    private String repairer;        // 主修工
    private Date startDate = new Date();         // 开工日期
    private Date endDate = new Date();           // 完工日期
    private String fault;           // 故障描述
    private Boolean takeAwayOldPart = false; // 带走旧件  单选，选项内容：是、否；
    private Boolean washing;        // 洗车    单选，选项内容：是、否；
    private String comment; //备注
    //费用信息

    //费用信息
//    private Double hourPrice = 0.0;     // 工时单价
//    private Double hours = 0.0;         // 总工时
//    private Double workTimeExpense = 0.0; // 工时费用合计 系统带出，不能更改，计算公式：∑工时费用
//    private Double partExpense = 0.0;     // 配件费用合计 系统带出，不能更改，计算公式：∑配件费用
//    private Double outExpense = 0.0;      // 外出费用合计   系统带出，不能更改，计算公式：∑外出费用
//    private Double accessoriesExpense = 0.0;   // 辅料费用合计 计算公式：∑物料类型为辅料费用
//    private Double otherExpense = 0.0;    // 其他费用合计
//    private Double expenseTotal = 0.0;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
//    private Double settlementAccesoriesExpense = 0.0;   // 应结算辅料费用
//    private Double settlementPartExpense = 0.0;  // 应结算配件费用  ∑供货方式为自购的配件费用
//    private Double settlementTotleExpense = 0.0;    // 应结算总费用

//    private Double laberCost = 0.0; // 工时费用标准/台

    //费用信息
    private Boolean nightWork = false;  // 夜间作业
    private Double nightExpense = 0.0;  // 夜间补贴
    private Double hourPrice = 0.0;     // 工时单价
    private Double outHours = 0.0;      // 外出工时
    private Double hourExpense = 0.0;   // 工时补贴合计费用
    private Double standardExpense = 0.0; // 活动标准费用
    private Double outExpense = 0.0;      // 外出费用   系统带出，不能更改，计算公式：∑外出费用
    private Double otherExpense = 0.0;   //  其他费用
    private Double expenseTotal = 0.0;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private Set<GoOutEntity> goOuts = new HashSet<>(); //出外子行
    private boolean settlement = false;//是否结算

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


    @Column(name = "Vmt", length = 20)
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

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    @Column(length = 500)
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

    public Double getOutHours() {
        return outHours;
    }

    public void setOutHours(Double outHours) {
        this.outHours = outHours;
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
    @JoinColumn(name = "ActivityDistributionId")
    public ActivityDistributionEntity getActivityDistribution() {
        return activityDistribution;
    }

    public void setActivityDistribution(ActivityDistributionEntity activityDistribution) {
        this.activityDistribution = activityDistribution;
    }

    //    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "VehicleId")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ActivityVehicleId")
    public ActivityVehicleEntity getActivityVehicle() {
        return activityVehicle;
    }

    public void setActivityVehicle(ActivityVehicleEntity activityVehicle) {
        this.activityVehicle = activityVehicle;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ActivityMaintenanceId")
    public Set<CommissionPartEntity> getCommissionParts() {
        return commissionParts;
    }

    public void setCommissionParts(Set<CommissionPartEntity> commissionParts) {
        this.commissionParts = commissionParts;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ActivityMaintenanceId")
    public Set<GoOutEntity> getGoOuts() {
        return goOuts;
    }

    public void setGoOuts(Set<GoOutEntity> goOuts) {
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
}