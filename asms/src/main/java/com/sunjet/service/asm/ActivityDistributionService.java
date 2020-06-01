package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityDistributionEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;
import com.sunjet.vm.base.DocStatus;

import java.util.List;

/**
 * Created by Administrator on 2016/11/5.
 */
public interface ActivityDistributionService extends BaseService, FlowBaseService {

    ActivityDistributionEntity findOneWithVehicles(String objId);

    List<ActivityDistributionEntity> findAllByKeyword(String keyword);

    List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(DocStatus status, String keyword, String dealerCode);

    ActivityDistributionEntity findAllWithVehicleByDocNo(String s, String docNo);

    ActivityDistributionEntity save(ActivityDistributionEntity activityDistribution);

    void deleteEntity(ActivityDistributionEntity entity);
}
