package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 2015/9/6.
 * 用户实体
 */
@Entity
@Table(name = "SysUsers")
public class UserEntity extends DocEntity implements Comparable {
    private static final long serialVersionUID = -3786601921455785818L;
    private String logId;                   // 登录名
    private String password;                // 加密后的密码
    private String salt;                    // 密码加密 盐
    private String name;                    // 姓名
    private String phone;                   // 电话
    //    private String rolesDesc;               // 记录角色集合中文字符串
//    private OrgEntity orgEntity;            // 组织
    private Set<RoleEntity> roles = new HashSet<RoleEntity>();
    private String userType;            // 用户类型   wuling:五菱工业用户  agency: 合作商  dealer: 服务站
    private DealerEntity dealer;        // 用户所属 服务站
    private AgencyEntity agency;        // 用户所属 合作商

    @Column(name = "LogId", length = 20, nullable = false, unique = true)
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    @Column(name = "Password", length = 50, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "Salt", length = 10, nullable = false)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "Name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Phone", length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "SysUserRole",
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            joinColumns = @JoinColumn(name = "user_id"))
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

//    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "org_id")
//    public OrgEntity getOrgEntity() {
//        return orgEntity;
//    }
//
//    public void setOrgEntity(OrgEntity orgEntity) {
//        this.orgEntity = orgEntity;
//    }


    @Column(length = 10)
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "dealerId")
    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "agencyId")
    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    public UserEntity() {
    }

    public UserEntity(String name, String logId, String password, String salt, Boolean enabled) {
        this.name = name;
        this.logId = logId;
        this.password = password;
        this.salt = salt;
        this.setEnabled(enabled);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        UserEntity userEntity = (UserEntity) o;
        return this.getObjId().compareTo(userEntity.getObjId());
    }
}
