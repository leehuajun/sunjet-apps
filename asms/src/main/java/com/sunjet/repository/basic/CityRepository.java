package com.sunjet.repository.basic;

import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 城市/地区（Region） dao
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface CityRepository extends JpaRepository<CityEntity, String>, JpaSpecificationExecutor<CityEntity> {

    @Query("select c from CityEntity c where c.province.objId=?1")
    List<CityEntity> findAllByProvinceId(String provinceId);
}
