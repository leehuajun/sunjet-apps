package com.sunjet.utils.activiti.listener.settlement;

import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.service.asm.DealerSettlementService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerSettlementToggleAssessListener")
public class DealerSettlementToggleAssessListener implements TaskListener {
    @Autowired
    private DealerSettlementService dealerSettlementService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        DealerSettlementEntity dealerSettlementEntity = dealerSettlementService.findOneById(businessId);
        // 切换状态
        if (dealerSettlementEntity.getCanEditAssess() == null || dealerSettlementEntity.getCanEditAssess() == false) {
            dealerSettlementEntity.setCanEditAssess(true);
        } else {
            dealerSettlementEntity.setCanEditAssess(false);
        }
        dealerSettlementService.getRepository().save(dealerSettlementEntity);

    }
}
