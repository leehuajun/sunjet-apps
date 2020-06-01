package com.sunjet.utils.activiti.listener.dealer;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.model.dealer.DealerQuitRequestEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.dealer.DealerAlterService;
import com.sunjet.service.dealer.DealerQuitService;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.BeanHelper;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务站退出申请审批完成监听器
 * <p>
 * Created by lhj on 16/10/21.
 */
@Component("dealerQuitCompleteListener")
public class DealerQuitCompleteListener implements ExecutionListener {
    @Autowired
    private ProcessService processService;
    @Autowired
    private DealerQuitService dealerQuitService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private UserService userService;

//    @Override
//    public void notify(DelegateTask delegateTask) {
//        System.out.println("关闭业务对象！！");
//        // 1. 获取业务ID
//        String businessId = processService.findBusinessIdByProcessInstanceId(delegateTask.getProcessInstanceId());
//        // 2. 获取业务对象
//        DealerAdmitRequestEntity admitRequest = dealerAdmitService.findOneById(businessId);
//        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
//        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
////        delegateTask.getProcessInstanceId().
//    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("服务站退出申请审批完成监听器--关闭业务对象？？");
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(execution.getProcessInstanceId());
        // 2. 获取业务对象DealerAdmitRequestEntity
        DealerQuitRequestEntity quitRequest = dealerQuitService.findOneById(businessId);
        // 3. 获取业务对象的某个属性
        DealerEntity dealerEntity = quitRequest.getDealer();
        // 赋值
//        BeanUtils.copyProperties(alterRequest,dealerEntity,BeanHelper.getNullPropertyNames(alterRequest));

        List<UserEntity> users = userService.findAllByDealerCode(dealerEntity.getCode());
        for (UserEntity user : users) {
            user.setEnabled(false);   // 禁用用户
            userService.save(user);
        }

        dealerEntity.setEnabled(false);
        dealerService.save(dealerEntity);
        // 3. 设置业务对象为 已关闭 状态
//        admitRequest.setStatus(DocStatus.CLOSED.getIndex());
        // 4. 更新业务对象
//        dealerAdmitService.save(admitRequest);
    }
}
