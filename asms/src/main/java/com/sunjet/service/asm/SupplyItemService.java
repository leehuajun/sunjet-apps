package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyItemEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface SupplyItemService extends BaseService {
    List<SupplyItemEntity> findSupplyItemsByDocID(String docid);
}
