package com.sunjet.utils.activiti.listener.report;

import com.sunjet.model.asm.ExpenseReportEntity;
import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.service.asm.ExpenseReportService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("expenseToggleTypeStatusListener")
public class ExpenseToggleTypeStatusListener implements TaskListener {
    @Autowired
    private ExpenseReportService expenseReportService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        ExpenseReportEntity reportEntity = expenseReportService.findOneById(businessId);
        reportEntity.setCanEditType(!reportEntity.getCanEditType());
        expenseReportService.getRepository().save(reportEntity);
        delegateTask.setVariable("type", reportEntity.getCostType());
        System.out.println("切换服务站编号可编辑状态为:" + reportEntity.getCanEditType());
    }
}
