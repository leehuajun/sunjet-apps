package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 配件调拨单/供货通知单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmSupplyNotices")
public class SupplyNoticeEntity extends FlowDocEntity {
    private static final long serialVersionUID = 3390052374643452540L;
    private String srcDocNo;     // 单据编号
    private String srcDocType;   // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocID;     // 来源对应单据ID
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String serviceManager;   // 服务经理
    private String operator;         // 经办人
    private String operatorPhone;    // 联系电话
    private String receive;         //收货人
    //    private String receivePhone;   // 收货人联系电话
    private String dealerAdderss;       // 服务站收货地址

    private String comment; //备注
    private List<SupplyNoticeItemEntity> items = new ArrayList<>();     // 物料列表

    @Column(length = 20)
    public String getSrcDocNo() {
        return srcDocNo;
    }

    public void setSrcDocNo(String srcDocNo) {
        this.srcDocNo = srcDocNo;
    }

    @Column(length = 20)
    public String getSrcDocType() {
        return srcDocType;
    }

    public void setSrcDocType(String srcDocType) {
        this.srcDocType = srcDocType;
    }

    @Column(length = 32)
    public String getSrcDocID() {
        return srcDocID;
    }

    public void setSrcDocID(String srcDocID) {
        this.srcDocID = srcDocID;
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

    @Column(length = 50)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Column(length = 50)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(length = 50)
    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }
//    @Column(length = 50)
//    public String getReceivePhone() {
//        return receivePhone;
//    }
//
//    public void setReceivePhone(String receivePhone) {
//        this.receivePhone = receivePhone;
//    }

    @Column(length = 200)
    public String getDealerAdderss() {
        return dealerAdderss;
    }

    public void setDealerAdderss(String dealerAdderss) {
        this.dealerAdderss = dealerAdderss;
    }

    @Column(length = 20)
    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplyNoticeId")
    public List<SupplyNoticeItemEntity> getItems() {
        return items;
    }

    public void setItems(List<SupplyNoticeItemEntity> items) {
        this.items = items;
    }

    public void addNoticeItem(SupplyNoticeItemEntity noticeItem) {
        noticeItem.setSupplyNotice(this);
        this.items.add(noticeItem);
    }
}
