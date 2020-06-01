package com.sunjet.service.dealer;

import com.sunjet.model.dealer.DealerQuitRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface DealerQuitService extends FlowBaseService, BaseService {
    void saveQuit(DealerQuitRequestEntity dealerQuitRequestEntity);

    DealerQuitRequestEntity findOneById(String businessId);

    void deleteEntity(DealerQuitRequestEntity entity);
}
