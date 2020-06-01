package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxf on 2015/11/05.
 * 资源实体
 */
@Entity
@Table(name = "SysResources")
public class ResourceEntity extends DocEntity {
    private static final long serialVersionUID = -5833217238641539363L;

    private String name;
    private String code;
    private List<OperationEntity> operationEntityList = new ArrayList<>();

    @Column(name = "Name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Code", length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "SysResourceOperation",
            inverseJoinColumns = @JoinColumn(name = "opt_id"),
            joinColumns = @JoinColumn(name = "res_id"))
    public List<OperationEntity> getOperationEntityList() {
        return operationEntityList;
    }

    public void setOperationEntityList(List<OperationEntity> operationEntityList) {
        this.operationEntityList = operationEntityList;
    }

    public ResourceEntity() {
    }

    public ResourceEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
