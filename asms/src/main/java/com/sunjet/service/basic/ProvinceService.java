package com.sunjet.service.basic;

import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/11/3.
 */
public interface ProvinceService extends BaseService {
    List<ProvinceEntity> findAllSortCold();

    ProvinceEntity saveProvince(ProvinceEntity normalProvince);
}
