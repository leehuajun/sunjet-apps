package com.sunjet.utils.activiti.listener;

import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.JsonHelper;
import com.sunjet.utils.activiti.CustomComment;
import com.sunjet.vm.base.DocStatus;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.identity.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 启动流程后，第一个userTask自动执行监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("initTaskListener")
public class InitTaskListener implements TaskListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        //1. 获取流程实例Id
        String processInstanceId = delegateTask.getProcessInstanceId();

//        delegateTask.getAssignee()

        Object status = delegateTask.getExecution().getVariable("status");
        if (status != null) {
            Integer iStatus = Integer.parseInt(status.toString());
            if (iStatus == DocStatus.REJECT.getIndex()) {
                System.out.println("当前是退回至当前节点，不能自动执行！");
                return;
            }
        }
        System.out.println("流程已启动，下面开始执行第一个userTask");

        System.out.println("任务ID:" + delegateTask.getId());
//        System.out.println("FormKey:" + delegateTask.getFormKey());

        String userId = CommonHelper.getActiveUser().getLogId();
        String taskId = delegateTask.getId();
        String comment = "提交申请";
        String outcome = "提交";
        CustomComment cc = new CustomComment(outcome, comment);
//        String json = JsonHelper.bean2Json(new CustomComment("提交",comment),CustomComment.class);
//        public ProcessInstance completeTask(String taskId, String outcome, String comment, String businessId, String userId)
//        taskService.complete(delegateTask.getId());
//        processService.completeTask(delegateTask.getId())


        Authentication.setAuthenticatedUserId(userId);
        try {
            //2. 添加批注
            taskService.addComment(delegateTask.getId(), processInstanceId, JsonHelper.bean2Json(cc));
            //3. 完成任务
            taskService.complete(taskId);
            System.out.println("第一个userTask自动执行完成！");
        } catch (IOException e) {
            System.out.println("第一个userTask自动执行失败！");
            e.printStackTrace();
        }
    }
}
