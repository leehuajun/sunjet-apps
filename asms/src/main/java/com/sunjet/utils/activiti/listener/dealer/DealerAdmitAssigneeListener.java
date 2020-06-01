package com.sunjet.utils.activiti.listener.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerAdmitAssigneeListener")
public class DealerAdmitAssigneeListener implements TaskListener {
    @Autowired
    private DealerAdmitService dealerAdmitService;
    @Autowired
    private CacheManager customCacheManager;

    @Override
    public void notify(DelegateTask delegateTask) {
        DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitService.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        UserEntity serviceManager = dealerAdmitRequest.getDealer().getServiceManager();

        delegateTask.setAssignee(serviceManager.getLogId());

        // 重新更新dealer列表
//        customCacheManager.initDealerList();
        System.out.println("分配任务给:" + serviceManager.getLogId());

    }
}
