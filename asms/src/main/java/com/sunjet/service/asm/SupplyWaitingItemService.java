package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyWaitingItemEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * Created by zyh on 2016/11/21.
 */
public interface SupplyWaitingItemService extends BaseService {
    List<SupplyWaitingItemEntity> findSupplyWaitingItems(String supplyID);

    SupplyWaitingItemEntity findSupplyWaitingItemById(String objId);
}

