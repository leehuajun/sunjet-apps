package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.base.IdEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 行政关系基础信息实体
 */
@Entity
@Table(name = "BasicRegions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "R")
public class RegionEntity extends DocEntity {
    private static final long serialVersionUID = -7531693176868504168L;

    private String code;    // 编号
    private String name;    // 名称


    @Column(name = "Code", length = 10, nullable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "Name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
