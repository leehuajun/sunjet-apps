package com.sunjet.utils.activiti.listener.report;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.model.dealer.DealerQuitRequestEntity;
import com.sunjet.service.asm.QualityReportService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("qualityAssigneeListener")
public class QualityAssigneeListener implements TaskListener {
    @Autowired
    private QualityReportService qualityReportService;
    @Autowired
    private DealerService dealerService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        QualityReportEntity reportEntity = qualityReportService.findOneById(businessId);
        DealerEntity dealer = (DealerEntity) dealerService.getRepository().findOne(reportEntity.getDealerCode());
        delegateTask.setAssignee(dealer.getServiceManager().getLogId());
        System.out.println("分配任务给:" + dealer.getServiceManager().getLogId());

    }
}
