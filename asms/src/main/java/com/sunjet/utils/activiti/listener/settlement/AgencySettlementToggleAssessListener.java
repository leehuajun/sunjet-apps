package com.sunjet.utils.activiti.listener.settlement;

import com.sunjet.model.asm.AgencySettlementEntity;
import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.service.asm.AgencySettlementService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("agencySettlementToggleAssessListener")
public class AgencySettlementToggleAssessListener implements TaskListener {
    @Autowired
    private AgencySettlementService agencySettlementService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        AgencySettlementEntity agencySettlementEntity = agencySettlementService.findOneById(businessId);
        // 切换状态
        if (agencySettlementEntity.getCanEditAssess() == null || agencySettlementEntity.getCanEditAssess() == false) {
            agencySettlementEntity.setCanEditAssess(true);
        } else {
            agencySettlementEntity.setCanEditAssess(false);
        }
        agencySettlementService.getRepository().save(agencySettlementEntity);

    }
}
