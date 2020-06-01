package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 2015/9/6.
 * 角色实体
 */
@Entity
@Table(name = "SysRoles")
public class RoleEntity extends DocEntity {

    private static final long serialVersionUID = 8512757228891325156L;
    private String roleId;  // 角色Id
    private String name;    // 角色名称
    private Set<UserEntity> users = new HashSet<UserEntity>();
    private Set<PermissionEntity> permissions = new HashSet<PermissionEntity>();

    @Column(length = 64, unique = true, nullable = false)
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "Name", length = 20, unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(cascade = {CascadeType.REFRESH},
            mappedBy = "roles",
            fetch = FetchType.EAGER)
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "SysRolePermission",
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            joinColumns = @JoinColumn(name = "role_id"))
    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public RoleEntity() {
    }

    public RoleEntity(String name, String roleId, Boolean enabled) {
        this.name = name;
        this.roleId = roleId;
        this.setEnabled(enabled);
    }


    @Override
    public String toString() {
        return name;
    }
}
