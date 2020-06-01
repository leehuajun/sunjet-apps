package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.*;

/**
 * 故障件返回单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmRecycleDocs")
public class RecycleEntity extends FlowDocEntity {
    private static final long serialVersionUID = 7507696661216826485L;
    private String operator;        // 经办人
    private String operatorPhone;   // 联系电话
    private String serviceManager;  // 服务经理
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String logistics;       // 物流名称
    private String logisticsNum;    // 物流单号
    private String recyclefile;     // 故障件附件
    private String logisticsfile;   // 物流附件
    private String recyclefileName;     // 故障件附件显示
    private String logisticsfileName;   // 物流附件显示
    private Double transportExpense = 0.0;  // 运输费用
    private Double otherExpense = 0.0;      // 其他费用
    private Double expenseTotal = 0.0;      // 费用合计
    private Date arriveDate; //预计送达
    private boolean received;//是否收货
    private Date rCVDate; //收货时间
    private List<RecycleItemEntity> items = new ArrayList<>();     // 清单
    private boolean settlement = false;//是否结算
    private String comment;   //备注

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

    @Column(length = 200)
    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    @Column(length = 200)
    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    @Column(length = 200)
    public String getRecyclefile() {
        return recyclefile;
    }

    public void setRecyclefile(String recyclefile) {
        this.recyclefile = recyclefile;
    }

    @Column(length = 200)
    public String getLogisticsfile() {
        return logisticsfile;
    }

    public void setLogisticsfile(String logisticsfile) {
        this.logisticsfile = logisticsfile;
    }

    @Column()
    public Double getTransportExpense() {
        return transportExpense;
    }

    public void setTransportExpense(Double transportExpense) {
        this.transportExpense = transportExpense;
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
    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }

    @Column()
    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    @Column()
    public Date getrCVDate() {
        return rCVDate;
    }

    public void setrCVDate(Date rCVDate) {
        this.rCVDate = rCVDate;
    }

    @Column(length = 200)
    public String getRecyclefileName() {
        return recyclefileName;
    }

    public void setRecyclefileName(String recyclefileName) {
        this.recyclefileName = recyclefileName;
    }

    @Column(length = 200)
    public String getLogisticsfileName() {
        return logisticsfileName;
    }

    @Column(length = 200)
    public void setLogisticsfileName(String logisticsfileName) {
        this.logisticsfileName = logisticsfileName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "Recycle")
    public List<RecycleItemEntity> getItems() {
        return items;
    }

    public void setItems(List<RecycleItemEntity> items) {
        this.items = items;
    }


    public void addItem(RecycleItemEntity recycleItemEntity) {
        recycleItemEntity.setRecycleEntity(this);
        this.items.add(recycleItemEntity);
    }
//    public void removeItem(RecycleItemEntity recycleItemEntity){
//        recycleItemEntity.set
//    }

    public boolean isSettlement() {
        return settlement;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }
}
