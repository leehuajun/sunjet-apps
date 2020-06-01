package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityVehicleEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityVehicleService extends BaseService {
    List<ActivityVehicleEntity> filter(String keyword);

//    ActivityNoticeEntity findallDocNo(String s, String docNo);


//    List<ActivityVehicleEntity> findAllByKeywordcurrent(String c, String b);

    //   List<ActivityVehicleEntity> findAllcurrent(String s);
}
