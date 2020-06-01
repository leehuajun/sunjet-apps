package com.sunjet.service.asm;

import com.sunjet.model.asm.RecycleEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * 故障件返回
 * Created by lhj on 16/9/17.
 */
public interface RecycleService extends BaseService, FlowBaseService {
    void save(RecycleEntity recycleRequest);

    RecycleEntity findRecycleListById(String objId);

    boolean deleteEntity(RecycleEntity entity);

    RecycleEntity findOneWithItems(String objId);
}
