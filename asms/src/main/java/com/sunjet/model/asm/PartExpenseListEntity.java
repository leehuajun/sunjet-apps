package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;

/**
 * 配件结算单子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmPartExpenseList")
public class PartExpenseListEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    private String srcDocType;    // 单据类型：三包服务单、首保服务单、服务活动单、故障件运输单
    private String srcDocID;     // 对应单据ID
    private String srcDocNo;//单据编号
    private Double workingExpense = 0.0;   // 工时费用
    private Double partExpense = 0.0;      // 配件费用
    private Double freightExpense = 0.0;    // 运输费用
    private Double otherExpense = 0.0;      // 其他费用
    private Double expenseTotal = 0.0;    // 费用合计
    private AgencySettlementEntity agencySettlement;
    private String comment;     // 备注

    @Column()
    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(length = 200)
    public String getSrcDocType() {
        return srcDocType;
    }

    public void setSrcDocType(String srcDocType) {
        this.srcDocType = srcDocType;
    }

    @Column(length = 200)
    public String getSrcDocID() {
        return srcDocID;
    }

    public void setSrcDocID(String srcDocID) {
        this.srcDocID = srcDocID;
    }

    @Column(length = 200)
    public String getSrcDocNo() {
        return srcDocNo;
    }

    public void setSrcDocNo(String srcDocNo) {
        this.srcDocNo = srcDocNo;
    }

    @Column()
    public Double getWorkingExpense() {
        return workingExpense;
    }

    public void setWorkingExpense(Double workingExpense) {
        this.workingExpense = workingExpense;
    }

    @Column()
    public Double getPartExpense() {
        return partExpense;
    }

    public void setPartExpense(Double partExpense) {
        this.partExpense = partExpense;
    }

    @Column()
    public Double getFreightExpense() {
        return freightExpense;
    }

    public void setFreightExpense(Double freightExpense) {
        this.freightExpense = freightExpense;
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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "AgencySettlementId")
    public AgencySettlementEntity getAgencySettlement() {
        return agencySettlement;
    }

    public void setAgencySettlement(AgencySettlementEntity agencySettlement) {
        this.agencySettlement = agencySettlement;
    }
}

