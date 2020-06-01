package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityPartEntity;
import com.sunjet.repository.asm.ActivityPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("activityPartService")
public class ActivityPartServiceImpl implements ActivityPartService {
    @Autowired
    private ActivityPartRepository activityPartRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityPartRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return activityPartRepository;
    }

    @Override
    public ActivityPartEntity filter(String keyword) {
        return activityPartRepository.filter(keyword);
    }

    @Override
    public List<ActivityPartEntity> findActivityPartListByNoticeId(String noticeId) {
        return activityPartRepository.findActivityPartListByNoticeId(noticeId);
    }
}
