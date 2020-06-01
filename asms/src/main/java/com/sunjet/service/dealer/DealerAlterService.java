package com.sunjet.service.dealer;

import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface DealerAlterService extends FlowBaseService, BaseService {
    void savedealerAlter(DealerAlterRequestEntity dealerAlterRequest);

    List<DealerAlterRequestEntity> fineAll();


    List<DealerEntity> findAllByKeyword(String keyword);

    DealerAlterRequestEntity findobjId(String objId);

    DealerAlterRequestEntity findOneById(String businessId);

    void deleteEntity(DealerAlterRequestEntity entity);
}
