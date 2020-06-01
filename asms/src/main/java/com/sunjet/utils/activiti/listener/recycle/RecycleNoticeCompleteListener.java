package com.sunjet.utils.activiti.listener.recycle;

import com.sunjet.model.asm.RecycleNoticeEntity;
import com.sunjet.service.asm.RecycleNoticeService;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Component("recycleNoticeCompleteListener")
public class RecycleNoticeCompleteListener implements ExecutionListener {
    @Autowired
    private RecycleNoticeService noticeService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        RecycleNoticeEntity noticeEntity = (RecycleNoticeEntity) noticeService.getRepository().findOne(businessId);

        noticeEntity.setStatus(DocStatus.CLOSED.getIndex());
        noticeService.getRepository().save(noticeEntity);
        LoggerUtil.getLogger().info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
//        System.out.println("CustomServiceTask");
    }
}
