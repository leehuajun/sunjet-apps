package com.sunjet.service.basic;

import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.repository.basic.CityRepository;
import com.sunjet.repository.basic.CountyRepository;
import com.sunjet.repository.basic.ProvinceRepository;
import com.sunjet.repository.basic.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
@Transactional
@Service("regionService")
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountyRepository countyRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return regionRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return countyRepository;
    }

    /**
     * 根据 Provice 的 ID 获取 Province 对象
     *
     * @param provinceId
     * @return
     */
    @Override
    public ProvinceEntity findProvinceById(String provinceId) {
        return provinceRepository.findOne(provinceId);
    }

    /**
     * 根据 City 的 ID 获取 City 对象
     *
     * @param cityId
     * @return
     */
    @Override
    public CityEntity findCityById(String cityId) {
        return cityRepository.findOne(cityId);
    }

    /**
     * 根据 County 的 ID 获取 County 对象
     *
     * @param countyId
     * @return
     */
    @Override
    public CountyEntity findCountyById(String countyId) {
        return countyRepository.findOne(countyId);
    }

    /**
     * 获取所有的 Province 对象
     *
     * @return
     */
    @Override
    public List<ProvinceEntity> findAllProvince() {
        return provinceRepository.findAll();
    }

    /**
     * 获取所有的 City 对象
     *
     * @return
     */
    @Override
    public List<CityEntity> findAllCity() {
        return cityRepository.findAll();
    }

    /**
     * 获取所有的 County 对象
     *
     * @return
     */
    @Override
    public List<CountyEntity> findAllCounty() {
        return countyRepository.findAll();
    }

    /**
     * 根据 City 的 ID 获取所有 County 对象
     *
     * @param cityId
     * @return
     */
    @Override
    public List<CountyEntity> findCountiesByCityId(String cityId) {
        return countyRepository.findAllByCityId(cityId);
    }

    /**
     * 根据 Province 的 ID 获取所有 City 对象
     *
     * @param provinceId
     * @return
     */
    @Override
    public List<CityEntity> findCitiesByProvinceId(String provinceId) {
        return cityRepository.findAllByProvinceId(provinceId);
    }
}
