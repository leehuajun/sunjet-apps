package com.sunjet.model.basic;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 城市信息实体
 */
@Entity
@Table(name = "BasicRegions")
@DiscriminatorValue("CITY")
public class CityEntity extends RegionEntity {
    private static final long serialVersionUID = -6481266000109625884L;
    private ProvinceEntity province;    // 省份
    private Integer category;           // 1. 一类城市   2. 二类城市

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "Province")
    public ProvinceEntity getProvince() {
        return province;
    }

    public void setProvince(ProvinceEntity province) {
        this.province = province;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public CityEntity() {
    }

    public CityEntity(Integer category, String code, String name, ProvinceEntity province) {
        this.category = category;
        this.setCode(code);
        this.setName(name);
        this.setProvince(province);
    }
}
