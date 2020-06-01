package com.sunjet.service.asm;

import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface RecycleNoticeItemService extends BaseService, FlowBaseService {

    List<RecycleNoticeItemEntity> findAllPart(String partCode);

    List<RecycleNoticeItemEntity> findCanReturnPart(String partCode, String dealerCode);
}
