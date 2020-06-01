package com.sunjet.model.admin;


import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 2015/9/6.
 * 权限实体
 */
@Entity
@Table(name = "SysPermissions")
public class PermissionEntity extends DocEntity implements Comparable {
    private static final long serialVersionUID = -2461553527985758890L;
    private String accessName;
    private String resourceName;
    private String permissionCode;
    private Integer seq;
    private Set<RoleEntity> roles = new HashSet<RoleEntity>();

    @Column(name = "AccessName", length = 50)
    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    @Column(name = "ResourceName", length = 50)
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Column(name = "PermissionCode", length = 50)
    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    @Column(name = "Seq")
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @ManyToMany(cascade = {CascadeType.REFRESH},
            mappedBy = "permissions",
            fetch = FetchType.LAZY)
    public Set<RoleEntity> getRoles() {
        return roles;
    }


    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public PermissionEntity() {
    }

    public PermissionEntity(String accessName, String resourceName, String permissionCode, Integer seq) {
        this.accessName = accessName;
        this.resourceName = resourceName;
        this.permissionCode = permissionCode;
        this.seq = seq;
    }

    @Override
    public String toString() {
        return resourceName + "->" + accessName;
    }

    @Override
    public int compareTo(Object o) {
        PermissionEntity model = (PermissionEntity) o;
        return this.seq.compareTo(model.getSeq());
    }
}
