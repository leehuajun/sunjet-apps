package com.sunjet.utils.activiti.listener.dealer;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.model.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.service.dealer.DealerAdmitService;
import com.sunjet.service.dealer.DealerLevelChangeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerLevelChangeAssigneeListener")
public class DealerLevelChangeAssigneeListener implements TaskListener {
    @Autowired
    private DealerLevelChangeService dealerLevelChangeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        DealerLevelChangeRequestEntity dealerLevelChangeRequestEntity = dealerLevelChangeService.findOneById(businessId);
        UserEntity serviceManager = dealerLevelChangeRequestEntity.getDealer().getServiceManager();
        delegateTask.setAssignee(serviceManager.getLogId());
        System.out.println("分配任务给:" + serviceManager.getLogId());
    }
}
