package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 待结算清单
 * Created by zyh on 16/11/14.
 */
@Entity
@Table(name = "AsmPendingSettlementDetails")
public class PendingSettlementDetailsEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6714141824946835458L;
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单，供货单
    private String srcDocID;       // 对应单据ID
    private String srcDocNo;      //单据编号
    private String dealerCode;      // 服务站编号　
    private String dealerName;      // 服务站名称
    private String secondDealerCode;      // 二级服务站编号　
    private String secondDealerName;      // 二级服务站名称
    private String agencyCode;      // 经销商编号　
    private String agencyName;      // 经销商名称
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private Date businessDate = new Date();   // 单据时间
    private Double workingExpense = 0.0;   // 工时费用
    private Double partExpense = 0.0;      // 配件费用
    private Double freightExpense = 0.0;    // 运输费用
    private Double outExpense = 0.00;      // 外出费用
    private Double otherExpense = 0.0;      // 其他费用
    private Double expenseTotal = 0.0;    // 费用合计
    private boolean settlement = false;//是否结算
    private String settlementDocType;    // 结算单据类型： 服务站结算单、配件结算单
    private String settlementDocID;       // 结算单ID
    private String settlementDocNo;      //结算单编号
    private Integer settlementStatus = 1000;       // 服务单结算状态:1000：待结算    1001：正在结算   1002：已结算


    public String getSrcDocType() {
        return srcDocType;
    }

    public void setSrcDocType(String srcDocType) {
        this.srcDocType = srcDocType;
    }

    public String getSrcDocID() {
        return srcDocID;
    }

    public void setSrcDocID(String srcDocID) {
        this.srcDocID = srcDocID;
    }

    public String getSrcDocNo() {
        return srcDocNo;
    }

    public void setSrcDocNo(String srcDocNo) {
        this.srcDocNo = srcDocNo;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getSecondDealerCode() {
        return secondDealerCode;
    }

    public void setSecondDealerCode(String secondDealerCode) {
        this.secondDealerCode = secondDealerCode;
    }

    public String getSecondDealerName() {
        return secondDealerName;
    }

    public void setSecondDealerName(String secondDealerName) {
        this.secondDealerName = secondDealerName;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public Double getWorkingExpense() {
        return workingExpense;
    }

    public void setWorkingExpense(Double workingExpense) {
        this.workingExpense = workingExpense;
    }

    public Double getPartExpense() {
        return partExpense;
    }

    public void setPartExpense(Double partExpense) {
        this.partExpense = partExpense;
    }

    public Double getFreightExpense() {
        return freightExpense;
    }

    public void setFreightExpense(Double freightExpense) {
        this.freightExpense = freightExpense;
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

    public boolean isSettlement() {
        return settlement;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }

    public String getSettlementDocType() {
        return settlementDocType;
    }

    public void setSettlementDocType(String settlementDocType) {
        this.settlementDocType = settlementDocType;
    }

    public String getSettlementDocID() {
        return settlementDocID;
    }

    public void setSettlementDocID(String settlementDocID) {
        this.settlementDocID = settlementDocID;
    }

    public String getSettlementDocNo() {
        return settlementDocNo;
    }

    public void setSettlementDocNo(String settlementDocNo) {
        this.settlementDocNo = settlementDocNo;
    }

    public Integer getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(Integer settlementStatus) {
        this.settlementStatus = settlementStatus;
    }
}
