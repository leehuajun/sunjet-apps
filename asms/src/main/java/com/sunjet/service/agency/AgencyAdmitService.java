package com.sunjet.service.agency;

import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.model.agency.AgencyAlterRequestEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface AgencyAdmitService extends FlowBaseService, BaseService {
    void save(AgencyAdmitRequestEntity agencyAdmitRequest);

    AgencyAdmitRequestEntity findOneById(String businessId);

    void deleteEntity(AgencyAdmitRequestEntity entity);

    AgencyAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId);
//    void save(AgencyAdmitRequestEntity entity);
    // void save(AgencyAdmitRequestEntity agencyAdmitRequestEntity);
}
