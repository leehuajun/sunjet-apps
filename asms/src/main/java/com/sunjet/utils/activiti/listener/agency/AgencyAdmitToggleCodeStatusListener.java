package com.sunjet.utils.activiti.listener.agency;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.agency.AgencyAdmitService;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("agencyAdmitToggleCodeStatusListener")
public class AgencyAdmitToggleCodeStatusListener implements TaskListener {
    @Autowired
    private AgencyAdmitService agencyAdmitService;
    @Autowired
    private CacheManager customCacheManager;

    @Override
    public void notify(DelegateTask delegateTask) {
        AgencyAdmitRequestEntity admitRequest = agencyAdmitService.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        admitRequest.setCanEditCode(!admitRequest.getCanEditCode());  // 切换状态
        agencyAdmitService.save(admitRequest);
//        customCacheManager.initAgencyList();
        System.out.println("切换服务站编号可编辑状态为:" + admitRequest.getCanEditCode());

    }
}
