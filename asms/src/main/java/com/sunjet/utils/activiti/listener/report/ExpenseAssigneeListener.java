package com.sunjet.utils.activiti.listener.report;

import com.sunjet.model.asm.ExpenseReportEntity;
import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.service.asm.ExpenseReportService;
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
@Component("expenseAssigneeListener")
public class ExpenseAssigneeListener implements TaskListener {
    @Autowired
    private ExpenseReportService expenseReportService;
    @Autowired
    private DealerService dealerService;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskEntity taskEntity = (TaskEntity) delegateTask;
        String businessId = taskEntity.getProcessInstance().getBusinessKey().split("\\.")[1];

        ExpenseReportEntity reportEntity = expenseReportService.findOneById(businessId);
        DealerEntity dealer = (DealerEntity) dealerService.getRepository().findOne(reportEntity.getDealerCode());
        delegateTask.setAssignee(dealer.getServiceManager().getLogId());
        System.out.println("分配任务给:" + dealer.getServiceManager().getLogId());

    }
}
