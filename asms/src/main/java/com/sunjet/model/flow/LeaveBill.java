package com.sunjet.model.flow;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhj on 16/10/17.
 */
@Entity
@Table(name = "flowLeaveBill")
public class LeaveBill extends FlowDocEntity {
    private static final long serialVersionUID = -6839029990447194465L;

    private Integer days;    // 请假天数
    private Date startDate = new Date();  // 开始日期
    private String reason = "";   // 请假原因
    private String comment = "";  // 备注


    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    @Temporal(TemporalType.DATE)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
