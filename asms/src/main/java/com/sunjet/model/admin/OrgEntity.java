package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by lhj on 2015/9/6.
 * 组织（服务商）实体
 */
//@Entity
//@Table(name = "SysOrgs")
public class OrgEntity extends DocEntity {

    private static final long serialVersionUID = -6056297580918896377L;
    private String name;    // 组织名称
    private String code;    // 组织编号
    //    private Integer orgType;   // 组织类型，1：服务站  2：合作商
    private Boolean isService;  // 是否服务站，服务站点
    private Boolean isAgency;   // 是否配件合作商，经销商
    private String location;

    @Column(name = "Name", length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Code", length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "IsService")
    public Boolean getService() {
        return isService;
    }

    public void setService(Boolean service) {
        isService = service;
    }

    @Column(name = "IsAgency")
    public Boolean getAgency() {
        return isAgency;
    }

    public void setAgency(Boolean agency) {
        isAgency = agency;
    }

    @Column(name = "Location", length = 200)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
