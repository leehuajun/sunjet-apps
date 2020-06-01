package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 维修项目子项实体
 */
@Entity
@Table(name = "BasicMaintainItems")
public class MaintainItemEntity extends DocEntity {
    private static final long serialVersionUID = 2002478285473675298L;
    private PartEntity part;    // 配件
    private Double laborTime;   // 工时定额
    private Double partAmount;  // 配件数量
    private MaintainEntity maintain; // 维修项目

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "part_id")
    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    @Column(name = "LaborTime")
    public Double getLaborTime() {
        return laborTime;
    }

    public void setLaborTime(Double laborTime) {
        this.laborTime = laborTime;
    }

    @Column(name = "PartAmount")
    public Double getPartAmount() {
        return partAmount;
    }

    public void setPartAmount(Double partAmount) {
        this.partAmount = partAmount;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "maintain_id")
    public MaintainEntity getMaintain() {
        return maintain;
    }

    public void setMaintain(MaintainEntity maintain) {
        this.maintain = maintain;
    }
}
