package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.*;

/**
 * 三包费用结算单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmDealerSettlementDocs")
public class DealerSettlementEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924946835458L;
    private String settlementType;        // 结算类型  服务结算单，返回件结算单
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private Date startDate;         // 开始日期
    private Date endDate;           // 截至时间
    private Date requestDate;       // 申请时间  日历控件，选择项，默认当前时间，可改
    private Double partExpense = 0.0;
    private Double recycleExpense = 0.0;          // 故障件运费
    private Double workingExpense = 0.0;          // 工时费用
    private Double outExpense = 0.0;              // 外出费用
    private Double rewardExpense = 0.0;           // 奖励费用
    private Double punishmentExpense = 0.0;       // 惩罚费用
    private Double otherExpense = 0.0;            // 其他费用
    private Double expenseTotal = 0.0;            // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private List<ExpenseListEntity> items = new ArrayList<>();     // 费用列表

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
    public Double getPartExpense() {
        return partExpense;
    }

    public void setPartExpense(Double partExpense) {
        this.partExpense = partExpense;
    }

    @Column()
    public Double getRecycleExpense() {
        return recycleExpense;
    }

    public void setRecycleExpense(Double recycleExpense) {
        this.recycleExpense = recycleExpense;
    }

    @Column()
    public Double getWorkingExpense() {
        return workingExpense;
    }

    public void setWorkingExpense(Double workingExpense) {
        this.workingExpense = workingExpense;
    }

    @Column()
    public Double getOutExpense() {
        return outExpense;
    }

    public void setOutExpense(Double outExpense) {
        this.outExpense = outExpense;
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

    public Boolean getCanEditAssess() {
        return canEditAssess;
    }

    public void setCanEditAssess(Boolean canEditAssess) {
        this.canEditAssess = canEditAssess;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "DealerSettlementId")
    public List<ExpenseListEntity> getItems() {
        return items;
    }

    public void setItems(List<ExpenseListEntity> items) {
        this.items = items;
    }

    public void addExpense(ExpenseListEntity ExpenseList) {
        ExpenseList.setDealerSettlement(this);
        this.items.add(ExpenseList);
    }


}
