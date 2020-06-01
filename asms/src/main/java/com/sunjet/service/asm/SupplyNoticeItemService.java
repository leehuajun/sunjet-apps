package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyNoticeItemEntity;
import com.sunjet.service.base.BaseService;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface SupplyNoticeItemService extends BaseService {


    SupplyNoticeItemEntity findOne(String objId);
}
