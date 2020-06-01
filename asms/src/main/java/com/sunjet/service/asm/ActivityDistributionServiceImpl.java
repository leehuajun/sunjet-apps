package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityDistributionEntity;
import com.sunjet.repository.asm.ActivityDistributionRepository;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/11/5.
 */
@Transactional
@Service("activityDistributionService")
public class ActivityDistributionServiceImpl implements ActivityDistributionService {
    @Autowired
    private ActivityDistributionRepository activityDistributionRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityDistributionRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return activityDistributionRepository;
    }

    @Override
    public ActivityDistributionEntity findOneWithVehicles(String objId) {
        return activityDistributionRepository.findOneWithVehicles(objId);
    }

    @Override
    public List<ActivityDistributionEntity> findAllByKeyword(String keyword) {
        return activityDistributionRepository.findAllByKeyword("%" + keyword + "%");
    }

    @Override
    public List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(DocStatus status, String keyword, String dealerCode) {
        return activityDistributionRepository.findAllByStatusAndKeywordAndDealerCode(status.getIndex(), "%" + keyword + "%", dealerCode);
    }

    @Override
    public ActivityDistributionEntity findAllWithVehicleByDocNo(String s, String docNo) {
        return activityDistributionRepository.findAllWithVehicleByDocNo(s, docNo);
    }

    @Override
    public ActivityDistributionEntity save(ActivityDistributionEntity activityDistribution) {
        return activityDistributionRepository.save(activityDistribution);
    }

    @Override
    public void deleteEntity(ActivityDistributionEntity entity) {
        activityDistributionRepository.delete(entity.getObjId());
    }
}
