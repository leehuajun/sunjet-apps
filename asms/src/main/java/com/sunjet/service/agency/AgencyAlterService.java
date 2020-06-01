package com.sunjet.service.agency;

import com.sunjet.model.agency.AgencyAlterRequestEntity;
import com.sunjet.model.agency.AgencyQuitRequestEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyAlterService extends FlowBaseService, BaseService {
    void save(AgencyAlterRequestEntity agencyAlterRequest);

    AgencyAlterRequestEntity findOneById(String businessId);

    void deleteEntity(AgencyAlterRequestEntity entity);
    // void save(AgencyAlterRequestEntity entity);
    //  void save(AgencyAlterRequestEntity agencyAlterRequestEntity);

}
