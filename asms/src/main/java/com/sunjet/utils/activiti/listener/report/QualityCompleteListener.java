package com.sunjet.utils.activiti.listener.report;

import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.asm.QualityReportService;
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
 * 服务站准入申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("qualityCompleteListener")
public class QualityCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private QualityReportService qualityReportService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        QualityReportEntity reportEntity = qualityReportService.findOneById(businessId);
        // 3. 获取业务对象的某个属性
        reportEntity.setEnabled(true);
        qualityReportService.getRepository().save(reportEntity);

        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);

        System.out.println("设置速报Enabled状态为:" + reportEntity.getEnabled());
    }
}
