package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.MaintainEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 服务委托
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmServiceProxyDocs")
public class ServiceProxyEntity extends FlowDocEntity {
    private static final long serialVersionUID = -460031829677039034L;
    private String operator;        // 经办人
    private String operatorPhone;   // 经办人电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private Date repairDate;        // 报修日期
    private Date pullInDate;        // 进站时间
    private Date pullOutDate;       // 出站时间
    private Date requestDate;       // 申请时间  日历控件，选择项，默认当前时间，可改

    private String vin;             // VIN
    private String vsn;             // VSN
    private String agency;          // 经销商
    private String platform;        // 车型平台
    private Date manufactureDate;   // 生产日期
    private Date purchaseDate;      // 购买日期
    private String engineModel;     // 发动机型号
    private Date productDate;     // 出厂日期
    private Integer mileage;        // 行驶里程
    private String engineNo;        // 发动机/电动机号
    private String plate;           // 车牌号
    private String address;         // 地址
    private String ownerPhone;      // 车主电话

    // 维修信息:    必输项：送修人、送修人电话
    private String sender;          // 送修人
    private String senderPhone;     // 送修人电话
    private String repairer;        // 主修工
    private Date startDate;         // 开工日期
    private Date endDate;           // 完工日期
    private String fault;           // 故障描述
    private Boolean takeAwayOldPart; // 带走旧件  单选，选项内容：是、否；
    private Boolean washing;        // 洗车    单选，选项内容：是、否；
    private Double claimCostOfLabor;    // 索赔工费
    private Double estimateCostOfLabor;         // 预计工时费
    private Double favorableCostOfLabor;        // 工时优惠
    private Double claimCostOfMaterial;         // 索赔材料费
    private Double estimateCostOfMaterial;      // 预估材料费
    private Double favorableCostOfMaterial;     // 材料优惠

    private Double outExpense;      // 外出费用合计   系统带出，不能更改，计算公式：∑外出费用
    private String displacement;    // 排量   必输项，单选选项，选项内容：排量小于1.0L；排量大于等于1.0L
    private Double stdExpense;      // 标准费用
    private Double expenseTotal;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private Set<MaintainEntity> MaintainItems = new HashSet<>();     // 维修项目列表
    private Set<CommissionPartEntity> partItems = new HashSet<>();           // 检查结果，零件信息
    private Set<GoOutEntity> goOutEntities = new HashSet<>(); //出外子行

    @Column(length = 32)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(length = 16)
    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    @Column(length = 16)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Column(length = 32)
    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    @Column(length = 16)
    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Column(length = 16)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Column()
    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    @Column()
    public Date getPullInDate() {
        return pullInDate;
    }

    public void setPullInDate(Date pullInDate) {
        this.pullInDate = pullInDate;
    }

    @Column()
    public Date getPullOutDate() {
        return pullOutDate;
    }

    public void setPullOutDate(Date pullOutDate) {
        this.pullOutDate = pullOutDate;
    }

    @Column()
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Column(length = 17)
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Column(length = 32)
    public String getVsn() {
        return vsn;
    }

    public void setVsn(String vsn) {
        this.vsn = vsn;
    }

    @Column(length = 32)
    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    @Column(length = 32)
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Column()
    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    @Column()
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Column(length = 32)
    public String getEngineModel() {
        return engineModel;
    }

    public void setEngineModel(String engineModel) {
        this.engineModel = engineModel;
    }

    @Column()
    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    @Column()
    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    @Column(length = 32)
    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    @Column(length = 32)
    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }


    @Column(length = 2000)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(length = 32)
    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    @Column(length = 32)
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Column(length = 32)
    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    @Column(length = 32)
    public String getRepairer() {
        return repairer;
    }

    public void setRepairer(String repairer) {
        this.repairer = repairer;
    }

    @Column()
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column()
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(length = 32)
    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    @Column()
    public Boolean getTakeAwayOldPart() {
        return takeAwayOldPart;
    }

    public void setTakeAwayOldPart(Boolean takeAwayOldPart) {
        this.takeAwayOldPart = takeAwayOldPart;
    }

    @Column()
    public Boolean getWashing() {
        return washing;
    }

    public void setWashing(Boolean washing) {
        this.washing = washing;
    }

    @Column()
    public Double getClaimCostOfLabor() {
        return claimCostOfLabor;
    }

    public void setClaimCostOfLabor(Double claimCostOfLabor) {
        this.claimCostOfLabor = claimCostOfLabor;
    }

    @Column()
    public Double getEstimateCostOfLabor() {
        return estimateCostOfLabor;
    }

    public void setEstimateCostOfLabor(Double estimateCostOfLabor) {
        this.estimateCostOfLabor = estimateCostOfLabor;
    }

    @Column()
    public Double getFavorableCostOfLabor() {
        return favorableCostOfLabor;
    }

    public void setFavorableCostOfLabor(Double favorableCostOfLabor) {
        this.favorableCostOfLabor = favorableCostOfLabor;
    }

    @Column()
    public Double getClaimCostOfMaterial() {
        return claimCostOfMaterial;
    }

    public void setClaimCostOfMaterial(Double claimCostOfMaterial) {
        this.claimCostOfMaterial = claimCostOfMaterial;
    }

    @Column()
    public Double getEstimateCostOfMaterial() {
        return estimateCostOfMaterial;
    }

    public void setEstimateCostOfMaterial(Double estimateCostOfMaterial) {
        this.estimateCostOfMaterial = estimateCostOfMaterial;
    }

    @Column()
    public Double getFavorableCostOfMaterial() {
        return favorableCostOfMaterial;
    }

    public void setFavorableCostOfMaterial(Double favorableCostOfMaterial) {
        this.favorableCostOfMaterial = favorableCostOfMaterial;
    }

    @Column()
    public Double getOutExpense() {
        return outExpense;
    }

    public void setOutExpense(Double outExpense) {
        this.outExpense = outExpense;
    }

    @Column(length = 32)
    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    @Column()
    public Double getStdExpense() {
        return stdExpense;
    }

    public void setStdExpense(Double stdExpense) {
        this.stdExpense = stdExpense;
    }

    @Column()
    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ServiceProxy")
    public Set<MaintainEntity> getMaintainItems() {
        return MaintainItems;
    }

    public void setMaintainItems(Set<MaintainEntity> maintainItems) {
        MaintainItems = maintainItems;
    }

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ServiceProxy")
    public Set<CommissionPartEntity> getPartItems() {
        return partItems;
    }

    public void setPartItems(Set<CommissionPartEntity> partItems) {
        this.partItems = partItems;
    }

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ServiceProxy")
    public Set<GoOutEntity> getGoOutEntities() {
        return goOutEntities;
    }

    public void setGoOutEntities(Set<GoOutEntity> goOutEntities) {
        this.goOutEntities = goOutEntities;
    }
}
