package com.sunjet.model.admin;


import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 2015/9/6.
 * 操作实体
 */
@Entity
@Table(name = "SysOperations")
public class OperationEntity extends DocEntity implements Comparable {

    private static final long serialVersionUID = 9150516330799632348L;
    private String optCode; //  操作编码
    private String optName; //  操作名称
    private Integer seq; // 序号
    //  private MenuEntity menuEntity;
//  private PermissionEntity permissionEntity;
    private List<ResourceEntity> resourceEntityList = new ArrayList<>();

//  @ManyToOne
//  @JoinColumn(name = "MENU_ID", referencedColumnName = "OBJ_ID")
//  public MenuEntity getMenuEntity() {
//    return menuEntity;
//  }
//
//  public void setMenuEntity(MenuEntity menuEntity) {
//    this.menuEntity = menuEntity;
//  }
//
//  @ManyToOne()
//  @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "OBJ_ID")
//  public PermissionEntity getPermissionEntity() {
//    return permissionEntity;
//  }
//
//  public void setPermissionEntity(PermissionEntity permissionEntity) {
//    this.permissionEntity = permissionEntity;
//  }


    @Column(name = "OptCode", length = 20)
    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    @Column(name = "OptName", length = 20)
    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    @Column(name = "Seq")
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @ManyToMany(cascade = {CascadeType.REFRESH},
            mappedBy = "operationEntityList",
            fetch = FetchType.LAZY)
    public List<ResourceEntity> getResourceEntityList() {
        return resourceEntityList;
    }

    public void setResourceEntityList(List<ResourceEntity> resourceEntityList) {
        this.resourceEntityList = resourceEntityList;
    }

    public OperationEntity() {
    }

    public OperationEntity(String optCode, String optName, Integer seq) {
        this.optCode = optCode;
        this.optName = optName;
        this.seq = seq;
    }

    @Override
    public String toString() {
        return this.optName;
    }

    @Override
    public int compareTo(Object o) {
        OperationEntity operationEntity = (OperationEntity) o;
        return this.seq.compareTo(operationEntity.getSeq());
    }
}
