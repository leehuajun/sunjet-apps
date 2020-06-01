package com.sunjet.service.basic;


import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public interface PartService extends BaseService {
    List<PartEntity> findAllByKeyword(String filter);

    List<PartEntity> findAllByCode(String code);
}
