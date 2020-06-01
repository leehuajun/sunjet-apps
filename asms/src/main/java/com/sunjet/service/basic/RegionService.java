package com.sunjet.service.basic;

import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface RegionService extends BaseService {

    /**
     * 根据 Provice 的 ID 获取 Province 对象
     *
     * @param provinceId
     * @return
     */
    ProvinceEntity findProvinceById(String provinceId);

    /**
     * 根据 City 的 ID 获取 City 对象
     *
     * @param cityId
     * @return
     */
    CityEntity findCityById(String cityId);

    /**
     * 根据 County 的 ID 获取 County 对象
     *
     * @param countyId
     * @return
     */
    CountyEntity findCountyById(String countyId);

    /**
     * 获取所有的 Province 对象
     *
     * @return
     */
    List<ProvinceEntity> findAllProvince();

    /**
     * 获取所有的 City 对象
     *
     * @return
     */
    List<CityEntity> findAllCity();

    /**
     * 获取所有的 County 对象
     *
     * @return
     */
    List<CountyEntity> findAllCounty();

    /**
     * 根据 City 的 ID 获取所有 County 对象
     *
     * @param cityId
     * @return
     */
    List<CountyEntity> findCountiesByCityId(String cityId);

    /**
     * 根据 Province 的 ID 获取所有 City 对象
     *
     * @param provinceId
     * @return
     */
    List<CityEntity> findCitiesByProvinceId(String provinceId);


}
