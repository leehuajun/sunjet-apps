package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 活动发布单
 */
@Entity
@Table(name = "AsmActivityDistributions")
public class ActivityDistributionEntity extends FlowDocEntity {
    private static final long serialVersionUID = 4896392306966926761L;
    private String serviceManager;  // 服务经理
    private DealerEntity dealer;    // 服务站
    private String dealerCode;      // 服务站编号
    private String dealerName;      // 服务站名称
    private String comment;          //备注
    private ActivityNoticeEntity activityNotice; //服务活动通知单
    private Set<ActivityVehicleEntity> activityVehicles = new HashSet<>();   // 车辆列表
    private String supplyNoticeId;          // 供货通知单Id(调拨单)
    private Boolean canEditSupply = true;      // 是否允许编辑生成调拨通知单,流程提交后，变为false，不允许再生成调拨申请

    @Column(length = 50)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "Dealer")
    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
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

    // 活动通知单
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ActivityNoticeId")
    public ActivityNoticeEntity getActivityNotice() {
        return activityNotice;
    }

    public void setActivityNotice(ActivityNoticeEntity activityNotice) {
        this.activityNotice = activityNotice;
    }

    // 车辆列表
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ActivityDistributionId")
    public Set<ActivityVehicleEntity> getActivityVehicles() {
        return activityVehicles;
    }

    public void setActivityVehicles(Set<ActivityVehicleEntity> activityVehicles) {
        this.activityVehicles = activityVehicles;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    //调拨通知单
    @Column(length = 32)
    public String getSupplyNoticeId() {
        return supplyNoticeId;
    }

    public void setSupplyNoticeId(String supplyNoticeId) {
        this.supplyNoticeId = supplyNoticeId;
    }

    public Boolean getCanEditSupply() {
        return canEditSupply;
    }

    public void setCanEditSupply(Boolean canEditSupply) {
        this.canEditSupply = canEditSupply;
    }
}
