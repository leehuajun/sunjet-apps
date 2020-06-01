package com.sunjet.utils.activiti.listener.agency;

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
@Component("agencyAdmitToggleUploadFileStatusListener")
public class AgencyAdmitToggleUploadFileStatusListener implements TaskListener {
    @Autowired
    private AgencyAdmitService agencyAdmitService;

    @Override
    public void notify(DelegateTask delegateTask) {
        AgencyAdmitRequestEntity admitRequest = agencyAdmitService.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        admitRequest.setCanUpload(!admitRequest.getCanUpload());  // 切换状态
        agencyAdmitService.save(admitRequest);
        System.out.println("切换上传文件可编辑状态为:" + admitRequest.getCanUpload());
    }
}
