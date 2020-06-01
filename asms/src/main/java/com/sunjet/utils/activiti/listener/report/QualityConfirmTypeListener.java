package com.sunjet.utils.activiti.listener.report;

import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.service.asm.QualityReportService;
import com.sunjet.service.basic.DealerService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("qualityConfirmTypeListener")
public class QualityConfirmTypeListener implements TaskListener {
    @Autowired
    private QualityReportService qualityReportService;


    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        QualityReportEntity reportEntity = qualityReportService.findOneById(businessId);
        ((TaskEntity) delegateTask).getProcessInstance().setVariable("type", reportEntity.getReportType());
        System.out.println("设置速报类型:" + reportEntity.getReportType());

    }
}
