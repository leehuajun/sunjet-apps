package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 故障件运费结算单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmFreightSettlementDocs")
public class FreightSettlementEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924996835458L;
    private String settlementType;        // 结算类型  故障件运费结算单
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private Date startDate;         // 开始日期
    private Date endDate;           // 截至时间
    private Date requestDate;       // 申请时间  日历控件，选择项，默认当前时间，可改
    private Double freightExpense = 0.0;      // 故障件运费
    private Double rewardExpense = 0.0;       // 奖励费用
    private Double punishmentExpense = 0.0;      // 惩罚费用
    private Double otherExpense = 0.0;        // 其他费用
    private Double expenseTotal = 0.0;        // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private List<FreightExpenseEntity> items = new ArrayList<>();     // 费用列表

    private Boolean canEditAssess = false;  // 是否允许编辑考核内容

    @Column(length = 200)
    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    @Column(length = 200)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(length = 200)
    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    @Column(length = 200)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Column(length = 200)
    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    @Column(length = 200)
    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Column(length = 200)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    @Column()
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Column()
    public Double getFreightExpense() {
        return freightExpense;
    }

    public void setFreightExpense(Double freightExpense) {
        this.freightExpense = freightExpense;
    }

    @Column()
    public Double getRewardExpense() {
        return rewardExpense;
    }

    public void setRewardExpense(Double rewardExpense) {
        this.rewardExpense = rewardExpense;
    }

    @Column()
    public Double getPunishmentExpense() {
        return punishmentExpense;
    }

    public void setPunishmentExpense(Double punishmentExpense) {
        this.punishmentExpense = punishmentExpense;
    }

    @Column()
    public Double getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(Double otherExpense) {
        this.otherExpense = otherExpense;
    }

    @Column()
    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    @Column()
    public Boolean getCanEditAssess() {
        return canEditAssess;
    }

    public void setCanEditAssess(Boolean canEditAssess) {
        this.canEditAssess = canEditAssess;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "FreightSettlementId")
    public List<FreightExpenseEntity> getItems() {
        return items;
    }

    public void setItems(List<FreightExpenseEntity> items) {
        this.items = items;
    }


}
