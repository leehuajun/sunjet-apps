package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.*;

/**
 * 活动通知
 * <p>
 * Created by lhj on 16/9/17.
 */
@Entity
@Table(name = "AsmActivityNotices")
public class ActivityNoticeEntity extends FlowDocEntity {
    private static final long serialVersionUID = -3966752937369885755L;
    private String title;               // 标题
    private Date publishDate = new Date();          // 发布时间  日历控件，选择项，默认当前时间，可改
    private String activityType;        // 活动类型
    private String noticeFile;          // 活动文件
    private Date startDate = new Date();            // 开始日期
    private Date endDate = new Date();              // 结束时间
    private String comment;                         // 备注/活动概述

    private Double perLaberCost = 0.0;        // 单台人工成本
    private Double amountLaberCost = 0.0;     // 人工成本合计
    private Double perPartCost = 0.0;         // 单台配件成本
    private Double amountPartCost = 0.0;      // 配件成本合计
    private Double amount = 0.0;              // 总成本合计

    private Set<ActivityVehicleEntity> activityVehicles = new HashSet<>();      // 活动车辆列表
    private Set<ActivityPartEntity> activityParts = new HashSet<>();            // 配件列表
    private Set<ActivityDistributionEntity> activityDistributions;              // 活动分配单列表

    @Column(length = 100)
    public String getNoticeFile() {
        return noticeFile;
    }

    public void setNoticeFile(String noticeFile) {
        this.noticeFile = noticeFile;
    }

    @Column(length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 50)
    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPerLaberCost() {
        return perLaberCost;
    }

    public void setPerLaberCost(Double perLaberCost) {
        this.perLaberCost = perLaberCost;
    }

    public Double getAmountLaberCost() {
        return amountLaberCost;
    }

    public void setAmountLaberCost(Double amountLaberCost) {
        this.amountLaberCost = amountLaberCost;
    }

    public Double getPerPartCost() {
        return perPartCost;
    }

    public void setPerPartCost(Double perPartCost) {
        this.perPartCost = perPartCost;
    }

    public Double getAmountPartCost() {
        return amountPartCost;
    }

    public void setAmountPartCost(Double amountPartCost) {
        this.amountPartCost = amountPartCost;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ActivityNoticeId")
    public Set<ActivityVehicleEntity> getActivityVehicles() {
        return activityVehicles;
    }

    public void setActivityVehicles(Set<ActivityVehicleEntity> activityVehicles) {
        this.activityVehicles = activityVehicles;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ActivityNoticeId")
    public Set<ActivityPartEntity> getActivityParts() {
        return activityParts;
    }

    public void setActivityParts(Set<ActivityPartEntity> activityParts) {
        this.activityParts = activityParts;
    }


    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ActivityNoticeId")
    public Set<ActivityDistributionEntity> getActivityDistributions() {
        return activityDistributions;
    }

    public void setActivityDistributions(Set<ActivityDistributionEntity> activityDistributions) {
        this.activityDistributions = activityDistributions;
    }

    public void addActivityVehicle(ActivityVehicleEntity activityVehicle) {
        activityVehicle.setActivityNotice(this);
        this.activityVehicles.add(activityVehicle);
    }

    public void addActivityPart(ActivityPartEntity activityPart) {
        activityPart.setActivityNotice(this);
        this.activityParts.add(activityPart);
    }

    public void addActivityDistribution(ActivityDistributionEntity activityDistribution) {
        activityDistribution.setActivityNotice(this);
        this.activityDistributions.add(activityDistribution);
    }
}
