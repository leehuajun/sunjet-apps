package com.sunjet.service.agency;

import com.sunjet.model.agency.AgencyQuitRequestEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyQuitService extends FlowBaseService, BaseService {
    // void save(AgencyQuitRequestEntity entity);
    void save(AgencyQuitRequestEntity agencyQuitRequestEntity);

    AgencyQuitRequestEntity findOneById(String businessId);


    void deleteEntity(AgencyQuitRequestEntity entity);
}
