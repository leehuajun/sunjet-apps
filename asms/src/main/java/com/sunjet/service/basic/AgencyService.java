package com.sunjet.service.basic;

import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/9/17.
 */
public interface AgencyService extends BaseService {
    AgencyEntity saveAgency(AgencyEntity agencyEntity);

    List<AgencyEntity> findAllByKeyword(String keyword);

    Boolean checkCodeExists(String code);

    AgencyEntity findOneByCode(String code);
}
