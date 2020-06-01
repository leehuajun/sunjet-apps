package com.sunjet.model.base;

import com.sunjet.utils.common.CommonHelper;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 业务基础实体
 */
@MappedSuperclass
public class DocEntity extends IdEntity {

    private String createrId;
    private String createrName;
    private String modifierId;
    private String modifierName;
    private Date createdTime = new Date();
    private Date modifiedTime = new Date();


    @Column(name = "CreaterId", length = 32)
    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    @Column(name = "CreaterName", length = 50)
    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    @Column(name = "ModifierId", length = 32)
    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "ModifierName", length = 50)
    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    /**
     * 日期属性设置
     * DATE: 日期，2001-04-08
     * TIME: 时间：11:54:23
     * TIMESTAMP: 时间戳：2001-04-08 11:54:23
     *
     * @return
     */
    @Column(name = "CreatedTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "ModifiedTime")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }


    public DocEntity() {
//        createrId = CommonHelper.getActiveUser().getUserId();
//        createrName = CommonHelper.getActiveUser().getUsername();
//        modifierId =  CommonHelper.getActiveUser().getUserId();
//        modifierName = CommonHelper.getActiveUser().getUsername();
    }
}
