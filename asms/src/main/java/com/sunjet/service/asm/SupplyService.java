package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * 供货单/发货单
 * Created by lhj on 16/9/17.
 */
public interface SupplyService extends BaseService, FlowBaseService {
    void save(SupplyEntity supplyRequest);

    SupplyEntity getSupplyByID(String id);

    SupplyEntity findSupplyWithPartsById(String objId);

    boolean deleteEntity(SupplyEntity supplyRequest);
}
