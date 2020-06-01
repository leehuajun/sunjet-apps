package com.sunjet.service.basic;

import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface VehicleService extends BaseService {
    List<VehicleEntity> findAllByKeyword(String s);

    List<VehicleEntity> findAllByKeywordAndFmDateIsNull(String keyWord);

    VehicleEntity findOneByVin(String vin);
}
