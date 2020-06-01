package com.sunjet.model.admin;


import com.sunjet.model.base.TreeNodeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by lhj on 2015/9/6.
 * 菜单实体
 */
@Entity
@Table(name = "SysMenus")
public class MenuEntity extends TreeNodeEntity<MenuEntity> {
    private static final long serialVersionUID = -929747504110218495L;
    private String name;
    private String url;   // menu 使用
    private String icon = "z-icon-credit-card";
    private Integer seq; // 排序
    private Boolean open = true;  // 状态,true:打开   false:关闭
    private String background = "rgb(230,230,230)";
    //  private MenuEntity parent;
    //    private Integer childrenCount = 0;
//    private ModuleEntitycom moduleEntitycom;
    private String permissionCode;
    private String permissionName;
//  private PermissionEntity permissionEntitycom;


    @Column(name = "Name", length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Url", length = 200)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "Icon", length = 50)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Column(name = "Seq", length = 50)
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Column(name = "IsOpen")
    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Transient
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public MenuEntity() {
    }

    public MenuEntity(String name, Integer seq, Boolean open) {
        this.name = name;
        this.seq = seq;
        this.open = open;
    }

    public MenuEntity(String name, Integer seq, Boolean open, MenuEntity parent) {
        this.name = name;
        this.seq = seq;
        this.open = open;
        this.setParent(parent);
    }

    public MenuEntity(String name, Integer seq, String url, MenuEntity parent) {
        this.name = name;
        this.seq = seq;
        this.url = url;
        this.setParent(parent);
    }

    public MenuEntity(String name, String icon, Integer seq, Boolean open) {
        this.name = name;
        this.icon = icon;
        this.seq = seq;
        this.open = open;
    }


    @Column(name = "PermissionCode", length = 50)
    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    @Column(name = "PermissionName", length = 50)
    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return name;
    }
}
