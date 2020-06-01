package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityVehicleEntity;
import com.sunjet.repository.asm.ActivityVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("activityVehicleService")
public class ActivityVehicleServiceImpl implements ActivityVehicleService {
    @Autowired
    private ActivityVehicleRepository activityVehicleRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityVehicleRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return activityVehicleRepository;
    }

    @Override
    public List<ActivityVehicleEntity> filter(String keyword) {
        return activityVehicleRepository.filter(keyword);
    }

//    @Override
//    public ActivityNoticeEntity findallDocNo(String s, String docNo) {
//        return activityVehicleRepository.findallDocNo(s,docNo);
//    }
    //    @Override
//    public List<ActivityVehicleEntity> findAllByKeywordcurrent(String keyword, String current) {
//        return activityNoticeVehicleRepository.findAllByKeywordcurrent(keyword,current);
//    }

//    @Override
//    public List<ActivityVehicleEntity> findAllcurrent(String s) {
//        return activityNoticeVehicleRepository.findAllcurrent(s);
//    }
}
