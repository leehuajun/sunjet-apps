package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.VehicleEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 三包服务单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmWarrantyMaintenances")
public class WarrantyMaintenanceEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924946835458L;
    private String docType;         // 单据类型
    private String operator;        // 经办人
    private String operatorPhone;   // 经办人电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String dealerPhone;     // 服务站电话
    private String dealerStar;      //服务站星级
    private String provinceName;    // 省份
    private Date repairDate = new Date();        // 报修日期
    private Date pullInDate = new Date();        // 进站时间
    private Date pullOutDate = new Date();       // 出站时间
    private Date requestDate = new Date();       // 申请时间  日历控件，选择项，默认当前时间，可改
    //    private ExpenseReportEntity expenseReport; //费用速报
//    private QualityReportEntity qualityReport; //质量速报
    private String comment;          //备注


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
    private String repairType;      // 修理类别
    //三包凭证文件
    private String ameplate;           // 铭牌
    private String manual;           // 保养手册
    private String odometer;           // 里程表
    private String invoice;           // 购买发票
    private String faultlocation;           // 故障部位
    private String front45;           // 前侧45度

    //费用信息
    private Boolean nightWork = false;  // 夜间作业
    private Double nightExpense = 0.0;  // 夜间补贴
    private Double hourPrice = 0.0;     // 工时单价
    private Double outHours = 0.0;      // 外出工时
    private Double maintainHours = 0.0; // 维修工时
    private Double outWorkTimeExpense = 0.0; // 外出工时补贴费用合计
    private Double maintainWorkTimeExpense = 0.0; // 维修工时费用合计
    private Double workTimeExpense = 0.0; // 工 时费用合计系统带出，不能更改，计算公式：∑工时费用
    private Double partExpense = 0.0;     // 配件费用合计 系统带出，不能更改，计算公式：∑配件费用
    private Double outExpense = 0.0;      // 外出费用合计   系统带出，不能更改，计算公式：∑外出费用
    private Double accessoriesExpense = 0.0;   // 辅料费用合计 计算公式：∑物料类型为辅料费用
    private Double otherExpense = 0.0;    // 其他费用合计
    private Double expenseTotal = 0.0;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private Double settlementAccesoriesExpense = 0.0;   // 应结算辅料费用
    private Double settlementPartExpense = 0.0;  // 应结算配件费用  ∑供货方式为自购的配件费用
    private Double settlementTotleExpense = 0.0;    // 应结算总费用

    private VehicleEntity vehicle;  // 车辆
    private Set<WarrantyMaintainEntity> warrantyMaintains = new HashSet<>();             // 维修项目列表
    private Set<CommissionPartEntity> commissionParts = new HashSet<>();           // 检查结果，零件信息
    private Set<GoOutEntity> goOuts = new HashSet<>();                // 出外子行
    private String expenseReportId;     // 费用速报Id
    private String expenseReportDocNo;     // 费用速报单号
    private String qualityReportId;     // 质量速报Id
    private String qualityReportDocNo;     // 质量速报单号
    private String supplyNoticeId;          // 供货通知单Id(调拨单)
    private String recycleNoticeId;         // 故障件返回通知单Id

    private Boolean canEditSupply = true;      // 是否允许编辑生成调拨通知单,流程提交后，变为false，不允许再生成调拨申请
    private Boolean canEditRecycle = false;     // 是否允许编辑生成故障件返回通知单
    private boolean settlement = false;//是否结算

    @Column(length = 200)
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

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

    @Column(length = 50)
    public String getDealerStar() {
        return dealerStar;
    }

    public void setDealerStar(String dealerStar) {
        this.dealerStar = dealerStar;
    }

    @Column(length = 100)
    public String getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String dealerPhone) {
        this.dealerPhone = dealerPhone;
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

    @Column(length = 50)
    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
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
    public String getFaultlocation() {
        return faultlocation;
    }

    public void setFaultlocation(String faultlocation) {
        this.faultlocation = faultlocation;
    }

    @Column(length = 200)
    public String getFront45() {
        return front45;
    }

    public void setFront45(String front45) {
        this.front45 = front45;
    }

    public Boolean getNightWork() {
        return nightWork;
    }

    public void setNightWork(Boolean nightWork) {
        this.nightWork = nightWork;
    }

    public Double getNightExpense() {
        return nightExpense;
    }

    public void setNightExpense(Double nightExpense) {
        this.nightExpense = nightExpense;
    }

    public Double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Double hourPrice) {
        this.hourPrice = hourPrice;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    //    public Double getHours() {
//        return hours;
//    }
//
//    public void setHours(Double hours) {
//        this.hours = hours;
//    }

    public Double getOutHours() {
        return outHours;
    }

    public void setOutHours(Double outHours) {
        this.outHours = outHours;
    }

    public Double getMaintainHours() {
        return maintainHours;
    }

    public void setMaintainHours(Double maintainHours) {
        this.maintainHours = maintainHours;
    }

    public Double getOutWorkTimeExpense() {
        return outWorkTimeExpense;
    }

    public void setOutWorkTimeExpense(Double outWorkTimeExpense) {
        this.outWorkTimeExpense = outWorkTimeExpense;
    }

    public Double getMaintainWorkTimeExpense() {
        return maintainWorkTimeExpense;
    }

    public void setMaintainWorkTimeExpense(Double maintainWorkTimeExpense) {
        this.maintainWorkTimeExpense = maintainWorkTimeExpense;
    }

    public Double getWorkTimeExpense() {
        return workTimeExpense;
    }

    public void setWorkTimeExpense(Double workTimeExpense) {
        this.workTimeExpense = workTimeExpense;
    }

    public Double getPartExpense() {
        return partExpense;
    }

    public void setPartExpense(Double partExpense) {
        this.partExpense = partExpense;
    }

    public Double getAccessoriesExpense() {
        return accessoriesExpense;
    }

    public void setAccessoriesExpense(Double accessoriesExpense) {
        this.accessoriesExpense = accessoriesExpense;
    }

    public Double getSettlementAccesoriesExpense() {
        return settlementAccesoriesExpense;
    }

    public void setSettlementAccesoriesExpense(Double settlementAccesoriesExpense) {
        this.settlementAccesoriesExpense = settlementAccesoriesExpense;
    }

    public Double getSettlementPartExpense() {
        return settlementPartExpense;
    }

    public void setSettlementPartExpense(Double settlementPartExpense) {
        this.settlementPartExpense = settlementPartExpense;
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

    public Double getSettlementTotleExpense() {
        return settlementTotleExpense;
    }

    public void setSettlementTotleExpense(Double settlementTotleExpense) {
        this.settlementTotleExpense = settlementTotleExpense;
    }

    @Column(length = 20)
    public String getVmt() {
        return vmt;
    }

    public void setVmt(String vmt) {
        this.vmt = vmt;
    }

    @Column(length = 32)
    public String getExpenseReportId() {
        return expenseReportId;
    }

    public void setExpenseReportId(String expenseReportId) {
        this.expenseReportId = expenseReportId;
    }

    @Column(length = 32)
    public String getQualityReportId() {
        return qualityReportId;
    }

    public void setQualityReportId(String qualityReportId) {
        this.qualityReportId = qualityReportId;
    }

    @Column(length = 32)
    public String getExpenseReportDocNo() {
        return expenseReportDocNo;
    }

    public void setExpenseReportDocNo(String expenseReportDocNo) {
        this.expenseReportDocNo = expenseReportDocNo;
    }

    @Column(length = 32)
    public String getQualityReportDocNo() {
        return qualityReportDocNo;
    }

    public void setQualityReportDocNo(String qualityReportDocNo) {
        this.qualityReportDocNo = qualityReportDocNo;
    }

    @Column(length = 32)
    public String getSupplyNoticeId() {
        return supplyNoticeId;
    }

    public void setSupplyNoticeId(String supplyNoticeId) {
        this.supplyNoticeId = supplyNoticeId;
    }

    @Column(length = 32)
    public String getRecycleNoticeId() {
        return recycleNoticeId;
    }

    public void setRecycleNoticeId(String recycleNoticeId) {
        this.recycleNoticeId = recycleNoticeId;
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
    @JoinColumn(name = "WarrantyMaintenance")
    public Set<WarrantyMaintainEntity> getWarrantyMaintains() {
        return warrantyMaintains;
    }

    public void setWarrantyMaintains(Set<WarrantyMaintainEntity> warrantyMaintains) {
        this.warrantyMaintains = warrantyMaintains;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "WarrantyMaintenance")
    public Set<CommissionPartEntity> getCommissionParts() {
        return commissionParts;
    }

    public void setCommissionParts(Set<CommissionPartEntity> commissionParts) {
        this.commissionParts = commissionParts;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "WarrantyMaintenance")
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

    public Boolean getCanEditSupply() {
        return canEditSupply;
    }

    public void setCanEditSupply(Boolean canEditSupply) {
        this.canEditSupply = canEditSupply;
    }

    public Boolean getCanEditRecycle() {
        return canEditRecycle;
    }

    public void setCanEditRecycle(Boolean canEditRecycle) {
        this.canEditRecycle = canEditRecycle;
    }


}
