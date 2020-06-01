package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityPartEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityPartService extends BaseService {
    ActivityPartEntity filter(String keyword);

    /**
     * 通过活动通知单objIdc查配件
     *
     * @param noticeId
     * @return
     */
    List<ActivityPartEntity> findActivityPartListByNoticeId(String noticeId);
}
