package com.sunjet.service.basic;

import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/9/17.
 */
public interface DealerService extends BaseService {

    DealerEntity save(DealerEntity dealerEntity);

    List<DealerEntity> findAllByKeyword(String keyword);

    List<DealerEntity> findParentDealersByCity(CityEntity selectedCity);

    List<DealerEntity> findParentDealersByProvince(ProvinceEntity selectedProvince);

    List<DealerEntity> findParentDealers();

    List<DealerEntity> findWithoutChild(String keyword);

    List<DealerEntity> findChildrenByParentId(String objId);

    List<DealerEntity> findChildrenByParentIdAndFilter(String objId, String filter);

    List<DealerEntity> getDealersByUserIdAndKeyword(String userId, String keyword);

    Boolean checkCodeExists(String code);

    List<DealerEntity> findAllByServiceManager(String userId, String keyword);

    DealerEntity findDealerByCode(String dealerCode);

    List<DealerEntity> findPrimaryByKeyword(String filter);
}
