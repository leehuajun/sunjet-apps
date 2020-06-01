package com.sunjet.model.helper;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.admin.OrgEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lhj on 2015/9/6.
 */
public class ActiveUser implements Serializable {

    private static final long serialVersionUID = -5809095705388430051L;
    private String userId;//用户id
    private String logId;//登录id
    private String username;//用户姓名

    private OrgEntity orgEntity; // 用户所属组织

    private List<MenuEntity> menus;//菜单
    private List<String> permissions;//权限
    private DealerEntity dealer;        // 服务站
    private AgencyEntity agency;        // 合作商
    private String userType;        // 用户类别  wuling /  agency  / dealer
    private String phone;           // 用户联系电话
    private Double price;           // 服务站 工时单价


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OrgEntity getOrgEntity() {
        return orgEntity;
    }

    public void setOrgEntity(OrgEntity orgEntity) {
        this.orgEntity = orgEntity;
    }

    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<MenuEntity> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuEntity> menus) {
        this.menus = menus;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "ActiveUser{" +
                "userId='" + userId + '\'' +
                ", logId='" + logId + '\'' +
                ", username='" + username + '\'' +
                ", ortId='" + this.orgEntity + '\'' +
                ", menus=" + menus +
                ", permissions=" + permissions +
                '}';
    }
}
