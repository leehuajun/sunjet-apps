package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.PartEntity;

import javax.persistence.*;

/**
 * 服务活动配件子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmActivityParts")
public class ActivityPartEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    private PartEntity part;    // 零件
    private Integer amount = 1;     // 数量
    private String comment;     // 备注
    private ActivityNoticeEntity activityNotice;                // 活动通知单

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "partId")
    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "ActivityNoticeId")
    public ActivityNoticeEntity getActivityNotice() {
        return activityNotice;
    }

    public void setActivityNotice(ActivityNoticeEntity activityNotice) {
        this.activityNotice = activityNotice;
    }
}
