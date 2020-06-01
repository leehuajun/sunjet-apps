package com.sunjet.service.dealer;

import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface DealerLevelChangeService extends FlowBaseService, BaseService {
    void saveAlteration(DealerLevelChangeRequestEntity dealerLevelChangeRequestEntity);


    List<DealerEntity> findAllByKeyword(String keyword);

    DealerLevelChangeRequestEntity findOneById(String businessId);

    void deleteEntity(DealerLevelChangeRequestEntity entity);
}
