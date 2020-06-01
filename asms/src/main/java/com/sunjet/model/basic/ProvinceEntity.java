package com.sunjet.model.basic;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 省份信息实体
 */
@Entity
@Table(name = "BasicRegions")
@DiscriminatorValue("PROVINCE")
public class ProvinceEntity extends RegionEntity {
    private static final long serialVersionUID = 5723499113546627518L;

    private Boolean Cold = false;  // 是否严寒省份，默认为：非严寒省份

    public Boolean getCold() {
        return Cold;
    }

    public void setCold(Boolean cold) {
        Cold = cold;
    }

    public ProvinceEntity() {
    }

    public ProvinceEntity(String code, String name) {
        this.setCode(code);
        this.setName(name);
    }

    public ProvinceEntity(String code, String name, Boolean cold) {
        this.setCode(code);
        this.setName(name);
        this.setCold(cold);
    }
}
