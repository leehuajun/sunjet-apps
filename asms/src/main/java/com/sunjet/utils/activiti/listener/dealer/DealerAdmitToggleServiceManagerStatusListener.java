package com.sunjet.utils.activiti.listener.dealer;

import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Created by lhj on 16/11/23.
 */
@Transactional
@Component("dealerAdmitToggleServiceManagerStatusListener")
public class DealerAdmitToggleServiceManagerStatusListener implements TaskListener {
    @Autowired
    private DealerAdmitService dealerAdmitService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];
        DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitService.findOneById(businessId);

        dealerAdmitRequest.setCanEditServiceManager(!dealerAdmitRequest.getCanEditServiceManager());  // 切换状态
        dealerAdmitService.save(dealerAdmitRequest);
        System.out.println("切换服务经理可编辑状态为:" + dealerAdmitRequest.getCanEditServiceManager());
    }
}
