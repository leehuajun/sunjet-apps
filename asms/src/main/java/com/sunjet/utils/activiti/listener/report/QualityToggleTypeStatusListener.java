package com.sunjet.utils.activiti.listener.report;

import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.asm.QualityReportService;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("qualityToggleTypeStatusListener")
public class QualityToggleTypeStatusListener implements TaskListener {
    @Autowired
    private QualityReportService qualityReportService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        QualityReportEntity reportEntity = qualityReportService.findOneById(businessId);
        reportEntity.setCanEditType(!reportEntity.getCanEditType());

        qualityReportService.getRepository().save(reportEntity);
        delegateTask.setVariable("type", reportEntity.getReportType());

        System.out.println("切换服务站编号可编辑状态为:" + reportEntity.getCanEditType());

    }
}
