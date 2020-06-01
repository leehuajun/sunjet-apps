package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 维修项目 主实体
 */
@Entity
@Table(name = "BasicMaintains")
public class MaintainEntity extends DocEntity {
    private static final long serialVersionUID = -4637484257477614557L;
    private String code;      // 维修项目编号
    private String name;      // 维修项目名称
    private Double workTime;  // 工时定额
    private Boolean claim = true;   // 是否索赔
    private String comment; // 备注

//    private Set<MaintainItemEntity> items = new HashSet<>();

    @Column(name = "Code", length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "Name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getClaim() {
        return claim;
    }

    public void setClaim(Boolean claim) {
        this.claim = claim;
    }

    @Column()
    public Double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
    }

    @Column(length = 500)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    @OneToMany(cascade = CascadeType.REFRESH)
//    public Set<MaintainItemEntity> getItems() {
//        return items;
//    }
//
//    public void setItems(Set<MaintainItemEntity> items) {
//        this.items = items;
//    }

    public MaintainEntity() {
    }

    public MaintainEntity(String code, String name, Boolean claim, Double workTime) {
        this.code = code;
        this.name = name;
        this.claim = claim;
        this.workTime = workTime;
    }
}
