package com.sunjet.model.basic;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 县区信息实体
 */
@Entity
@Table(name = "BasicRegions")
@DiscriminatorValue("COUNTY")
public class CountyEntity extends RegionEntity {
    private static final long serialVersionUID = 9214165516563769460L;
    private CityEntity city;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "City")
    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public CountyEntity() {
    }

    public CountyEntity(String code, String name, CityEntity city) {
        this.setCode(code);
        this.setName(name);
        this.setCity(city);
    }
}
