package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.*;

/**
 * 供货单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmSupplyDocs")
public class SupplyEntity extends FlowDocEntity {
    private static final long serialVersionUID = 3746737280166924832L;
    private String operator;            // 经办人
    private String operatorPhone;       // 联系电话
    private String agencyCode;          // 经销商编号　
    private String agencyName;          // 经销商名称
    private String agencyAddress;       //经销商地址
    private String agencyPhone;         //经销商电话
    private Date supplyDate = new Date();            // 供应时间
    private String dealerCode;          // 服务站编号
    private String dealerName;          // 服务站名称
    private String receive;             //收货人
    private String dealerAdderss;       // 服务站收货地址
    private String transportmodel;      // 运输方式
    private Date arrivalTime;     //到货时间
    private String logistics;       //物流名称
    private String logisticsNum;    //物流单号
    private String logisticsfile;       //物流附件
    private String logisticsfilename;   //物流附件
    private Double partExpense = 0.0;  //配件费用
    private Double transportExpense = 0.0;        // 运输费用
    private Double otherExpense = 0.0;            // 其他费用
    private Double expenseTotal = 0.0;
    ;         // 费用合计
    private Boolean received;   //是否收货
    private Date rcvDate;       //收货时间
    private String comment;     // 备注
    private List<SupplyItemEntity> items = new ArrayList<>();     // 物料列表
    private boolean settlement = false;//是否结算

    @Column(length = 50)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(length = 60)
    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    @Column(length = 50)
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

    public Date getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(Date supplyDate) {
        this.supplyDate = supplyDate;
    }

    @Column(length = 50)
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
    public String getDealerAdderss() {
        return dealerAdderss;
    }

    public void setDealerAdderss(String dealerAdderss) {
        this.dealerAdderss = dealerAdderss;
    }

    @Column(length = 100)
    public String getTransportmodel() {
        return transportmodel;
    }

    public void setTransportmodel(String transportmodel) {
        this.transportmodel = transportmodel;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Column(length = 200)
    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    @Column(length = 50)
    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    @Column(length = 50)
    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    @Column(length = 200)
    public String getLogisticsfile() {
        return logisticsfile;
    }

    public void setLogisticsfile(String logisticsfile) {
        this.logisticsfile = logisticsfile;
    }

    @Column(length = 200)
    public String getLogisticsfilename() {
        return logisticsfilename;
    }

    public void setLogisticsfilename(String logisticsfilename) {
        this.logisticsfilename = logisticsfilename;
    }

    public Double getPartExpense() {
        return partExpense;
    }

    public void setPartExpense(Double partExpense) {
        this.partExpense = partExpense;
    }

    @Column(length = 200)
    public String getAgencyAddress() {
        return agencyAddress;
    }

    public void setAgencyAddress(String agencyAddress) {
        this.agencyAddress = agencyAddress;
    }

    public Double getTransportExpense() {
        return transportExpense;
    }

    public void setTransportExpense(Double transportExpense) {
        this.transportExpense = transportExpense;

    }

    public Double getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(Double otherExpense) {
        this.otherExpense = otherExpense;

    }

    @Column(length = 100)
    public String getAgencyPhone() {
        return agencyPhone;
    }

    public void setAgencyPhone(String agencyPhone) {
        this.agencyPhone = agencyPhone;
    }

    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Date getRcvDate() {
        return rcvDate;
    }

    public void setRcvDate(Date rcvDate) {
        this.rcvDate = rcvDate;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplyId")
    public List<SupplyItemEntity> getItems() {
        return items;
    }

    public void setItems(List<SupplyItemEntity> items) {
        this.items = items;
    }

    public boolean isSettlement() {
        return settlement;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }
}
