package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityPartEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.vm.base.DocStatus;

import java.util.List;

/**
 * 服务活动通知单
 * Created by lhj on 16/9/17.
 */
public interface ActivityNoticeService extends BaseService {
    void save(ActivityNoticeEntity activityNoticeRequest);

    List<ActivityNoticeEntity> findall();

    ActivityNoticeEntity findOneById(String businessId);

    ActivityNoticeEntity findOneWithVehicles(String objId);

    ActivityNoticeEntity findOneWithVehiclesAndParts(String objId);

    ActivityNoticeEntity findOneWithParts(String objId);

    List<ActivityNoticeEntity> findAllByStatusAndKeyword(DocStatus status, String keyword);

    List<ActivityNoticeEntity> findAllByKeywordcurrent(String keyword, String current);

    ActivityNoticeEntity findcurrent(String current);


    ActivityNoticeEntity findallDocNo(String s, String docNo);

    void deleteEntity(ActivityNoticeEntity entity);

    List<ActivityPartEntity> findActivityPartListByNoticeId(String noticeId);

    ActivityNoticeEntity findOneWithPartById(String objId);
}
