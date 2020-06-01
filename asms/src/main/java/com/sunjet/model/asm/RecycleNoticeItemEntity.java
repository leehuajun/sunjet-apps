package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 故障件返回清单
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmRecycleNoticeItems")
public class RecycleNoticeItemEntity extends DocEntity {
    private static final long serialVersionUID = 6763942315816003206L;
    //    private PartEntity part;    // 零件
    private String partCode;    // 零件号
    private String partName;    // 零件编号
    private Integer amount = 0;    //数量
    private Integer backAmount = 0; // 已返回数量
    private Integer currentAmount = 0; // 未返回数量
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    private String pattern;         // 故障模式
    private String reason;          // 换件原因
    private String comment; //备注
    private RecycleNoticeEntity recycleNotice;
    private Set<RecycleItemEntity> recycleItems = new HashSet<>();


    //    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
//    @JoinColumn(name = "partId")
//    public PartEntity getPart() {
//        return part;
//    }
//
//    public void setPart(PartEntity part) {
//        this.part = part;
//    }
    @Column(length = 50)
    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    @Column(length = 200)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
        this.currentAmount = amount;
    }

    public Integer getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(Integer backAmount) {
        this.backAmount = backAmount;
    }

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }


    public String getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(String warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public String getWarrantyMileage() {
        return warrantyMileage;
    }

    public void setWarrantyMileage(String warrantyMileage) {
        this.warrantyMileage = warrantyMileage;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @ManyToOne
    @JoinColumn(name = "RecycleNoticeId")
    public RecycleNoticeEntity getRecycleNotice() {
        return recycleNotice;
    }

    public void setRecycleNotice(RecycleNoticeEntity recycleNotice) {
        this.recycleNotice = recycleNotice;
    }

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "recycleNoticeItem")
    public Set<RecycleItemEntity> getRecycleItems() {
        return recycleItems;
    }

    public void setRecycleItems(Set<RecycleItemEntity> recycleItems) {
        this.recycleItems = recycleItems;
    }
}
