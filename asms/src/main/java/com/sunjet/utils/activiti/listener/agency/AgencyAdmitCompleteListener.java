package com.sunjet.utils.activiti.listener.agency;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.agency.AgencyAdmitService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.dealer.DealerAdmitService;
import com.sunjet.service.flow.ProcessService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 合作商准入申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("agencyAdmitCompleteListener")
public class AgencyAdmitCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private AgencyAdmitService agencyAdmitService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private CacheManager customCacheManager;

    //    @Override
//    public void notify(DelegateTask delegateTask) {
//        System.out.println("关闭业务对象！！");
//        // 1. 获取业务ID
//        String businessId = processService.findBusinessIdByProcessInstanceId(delegateTask.getProcessInstanceId());
//        // 2. 获取业务对象
//        DealerAdmitRequestEntity admitRequest = dealerAdmitService.findOneById(businessId);
//        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
//        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
////        delegateTask.getProcessInstanceId().
//    }
//
    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("合作商准入申请审批完成监听器--关闭业务对象？？");
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        AgencyAdmitRequestEntity admitRequest = agencyAdmitService.findOneById(businessId);
        // 3. 获取业务对象的某个属性
        AgencyEntity agencyEntity = admitRequest.getAgency();
        agencyEntity.setEnabled(true);
        agencyService.saveAgency(agencyEntity);
//        customCacheManager.initAgencyList();
        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
    }

//    @Override
//    public void notify(DelegateTask delegateTask) {
//
//    }
}
