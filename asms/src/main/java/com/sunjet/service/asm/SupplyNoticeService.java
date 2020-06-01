package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyNoticeEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * 供货/发货  通知单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeService extends BaseService, FlowBaseService {

    SupplyNoticeEntity getSupplyNoticeByID(String id);

    SupplyNoticeEntity save(SupplyNoticeEntity supplyNotice);

    boolean audit(SupplyNoticeEntity supplyNotice);

    boolean delete(SupplyNoticeEntity supplyNotice);

    boolean close(SupplyNoticeEntity supplyNotice);

    SupplyNoticeEntity findOneById(String objId);

}
