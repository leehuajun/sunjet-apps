package com.sunjet.service.asm;

import com.sunjet.model.asm.RecycleNoticeEntity;
import com.sunjet.service.base.BaseService;

/**
 * 故障件返回通知
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticeService extends BaseService {
    void save(RecycleNoticeEntity recycleNoticeRequest);

    RecycleNoticeEntity findOneWithOthersId(String objId);

    void deleteEntity(RecycleNoticeEntity entity);

    void deleteEntityByObjId(String objId);
}
