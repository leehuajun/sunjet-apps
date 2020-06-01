package com.sunjet.service.dealer;

import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface DealerAdmitService extends FlowBaseService, BaseService {
    void save(DealerAdmitRequestEntity entity);

    void update(DealerAdmitRequestEntity dealerAdmitRequestEntity);

    DealerAdmitRequestEntity findOneById(String businessId);

    void deleteEntity(DealerAdmitRequestEntity entity);

    DealerAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId);
}
