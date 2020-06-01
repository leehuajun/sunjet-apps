package com.sunjet.utils.activiti.listener.activity;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/21.
 */
@Component("noticeCompleteListener")
public class NoticeCompleteListener implements ExecutionListener {
    @Autowired
    private ActivityNoticeService noticeService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey().split("\\.")[1];
        ActivityNoticeEntity noticeEntity = noticeService.findOneById(businessId);

        noticeEntity.setStatus(DocStatus.CLOSED.getIndex());
        noticeService.getRepository().save(noticeEntity);
        LoggerUtil.getLogger().info("保存实体对象状态为：" + DocStatus.CLOSED.getName());
//        System.out.println("CustomServiceTask");
    }
}
