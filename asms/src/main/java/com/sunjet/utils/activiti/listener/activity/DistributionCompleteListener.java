package com.sunjet.utils.activiti.listener.activity;

import com.sunjet.model.asm.ActivityDistributionEntity;
import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.service.asm.ActivityDistributionService;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Component("distributionCompleteListener")
public class DistributionCompleteListener implements ExecutionListener {
    @Autowired
    private ActivityDistributionService distributionService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        ActivityDistributionEntity distributionEntity = (ActivityDistributionEntity) distributionService.getRepository().findOne(businessId);

        distributionEntity.setStatus(DocStatus.CLOSED.getIndex());
        distributionService.getRepository().save(distributionEntity);
        LoggerUtil.getLogger().info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
//        System.out.println("CustomServiceTask");
    }
}
