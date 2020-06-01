package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.*;

/**
 * 费用结算单合作库
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmAgencySettlementDocs")
public class AgencySettlementEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714142924946835458L;
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String serviceManager;  // 服务经理
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private String provinceName;    // 省份
    private Date startDate;        // 开始日期
    private Date endDate;        // 截至时间
    private Date requestDate;       // 申请时间  日历控件，选择项，默认当前时间，可改
    private Double partExpense;      // 配件费用
    private Double freight;      // 运费
    private Double rewardExpense;      // 奖励费用
    private Double punishmentExpense;      // 惩罚费用
    private Double otherExpense;      // 其他费用
    private Double expenseTotal;    // 费用合计    系统带出，不能更改，计算公式：外出费用合计+标准费用
    private List<PartExpenseListEntity> items = new ArrayList<>();     // 费用列表
    private Boolean canEditAssess = false;  // 是否允许编辑考核内容

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
    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    @Column(length = 200)
    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
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
    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "AgencySettlementId")
    public List<PartExpenseListEntity> getItems() {
        return items;
    }

    public void setItems(List<PartExpenseListEntity> items) {
        this.items = items;
    }

    public void addPartExpense(PartExpenseListEntity partExpenseList) {
        partExpenseList.setAgencySettlement(this);
        this.items.add(partExpenseList);
    }


    public Boolean getCanEditAssess() {
        return canEditAssess;
    }

    public void setCanEditAssess(Boolean canEditAssess) {
        this.canEditAssess = canEditAssess;
    }

}
