package com.sunjet.utils.activiti.listener.dealer;

import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("dealerAdmitToggleCodeStatusListener")
public class DealerAdmitToggleCodeStatusListener implements TaskListener {
    @Autowired
    private DealerAdmitService dealerAdmitService;

    @Override
    public void notify(DelegateTask delegateTask) {
        DealerAdmitRequestEntity dealerAdmitRequest = dealerAdmitService.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        dealerAdmitRequest.setCanEditCode(!dealerAdmitRequest.getCanEditCode());
        dealerAdmitService.save(dealerAdmitRequest);
        System.out.println("切换服务站编号可编辑状态为:" + dealerAdmitRequest.getCanEditCode());

    }
}
