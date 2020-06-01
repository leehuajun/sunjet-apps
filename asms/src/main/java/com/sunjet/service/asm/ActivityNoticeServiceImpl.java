package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityPartEntity;
import com.sunjet.repository.asm.ActivityNoticeRepository;
import com.sunjet.repository.asm.ActivityPartRepository;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务活动通知单
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("activityNoticeService")
public class ActivityNoticeServiceImpl implements ActivityNoticeService {
    @Autowired
    private ActivityNoticeRepository activityNoticeRepository;
    @Autowired
    private ActivityPartRepository activityPartRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityNoticeRepository;
    }

    @Override
    public void save(ActivityNoticeEntity activityNoticeRequest) {
        activityNoticeRepository.save(activityNoticeRequest);
    }

    @Override
    public List<ActivityNoticeEntity> findall() {
        return activityNoticeRepository.findAll();
    }

    @Override
    public ActivityNoticeEntity findOneById(String businessId) {
        return activityNoticeRepository.findOne(businessId);
    }

    @Override
    public ActivityNoticeEntity findOneWithVehicles(String objId) {
        return this.activityNoticeRepository.findOneWithVehicles(objId);
    }

    @Override
    public JpaRepository getRepository() {
        return activityNoticeRepository;
    }

    @Override
    public ActivityNoticeEntity findOneWithVehiclesAndParts(String objId) {
        return activityNoticeRepository.findOneWithVehiclesAndParts(objId);
    }

    @Override
    public ActivityNoticeEntity findOneWithParts(String objId) {
        return activityNoticeRepository.findOneWithParts(objId);
    }

    @Override
    public List<ActivityNoticeEntity> findAllByStatusAndKeyword(DocStatus status, String keyword) {
        return activityNoticeRepository.findAllByStatusAndKeyword(status.getIndex(), "%" + keyword + "%");
    }

    @Override
    public List<ActivityNoticeEntity> findAllByKeywordcurrent(String keyword, String current) {
        return activityNoticeRepository.findAllByKeywordcurrent(keyword, current);
    }

    @Override
    public ActivityNoticeEntity findcurrent(String current) {
        return activityNoticeRepository.findcurrent(current);
    }

    @Override
    public ActivityNoticeEntity findallDocNo(String s, String docNo) {
        return activityNoticeRepository.findallDocNo(s, docNo);
    }

    @Override
    public void deleteEntity(ActivityNoticeEntity entity) {
        activityNoticeRepository.delete(entity.getObjId());
        return;
    }

    @Override
    public List<ActivityPartEntity> findActivityPartListByNoticeId(String noticeId) {
        return activityPartRepository.findActivityPartListByNoticeId(noticeId);
    }

    @Override
    public ActivityNoticeEntity findOneWithPartById(String objId) {
        return activityPartRepository.findOneWithPartById(objId);
    }
}
