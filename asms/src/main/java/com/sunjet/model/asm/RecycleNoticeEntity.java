package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.utils.common.DateHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 故障件返回清单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmRecycleNotices")
public class RecycleNoticeEntity extends FlowDocEntity {
    private static final long serialVersionUID = 6763932315816003206L;
    private String srcDocNo;        //单据编号
    private String srcDocType;      // 来源类型：三包服务单、首保服务单、服务活动单、
    private String srcDocID;        // 来源对应单据ID
    private String dealerCode;      // 服务站编号  系统带出,服务站单选，选项内容是服务站清单的服务站编号；选择后，服务站名称、省份系统带出
    private String dealerName;      // 服务站名称
    private String provinceName;    // 省份
    private String comment; //备注
    private Date requestDate = new Date();        //提交时间
    private Date returnDate = DateHelper.nextMonthTenthDate();         // 返回日期要求
    private List<RecycleNoticeItemEntity> items = new ArrayList<>();

    @Column(length = 32)
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

    @Column(length = 200)
    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(length = 50)
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recycleNotice")
    public List<RecycleNoticeItemEntity> getItems() {
        return items;
    }

    public void setItems(List<RecycleNoticeItemEntity> items) {
        this.items = items;
    }

    public void addNoticeItem(RecycleNoticeItemEntity item) {
        item.setRecycleNotice(this);
        this.items.add(item);
    }
}
