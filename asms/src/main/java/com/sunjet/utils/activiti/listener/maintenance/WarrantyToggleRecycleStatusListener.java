package com.sunjet.utils.activiti.listener.maintenance;

import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.service.dealer.DealerAdmitService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lhj on 16/11/23.
 */
@Component("warrantyToggleRecycleStatusListener")
public class WarrantyToggleRecycleStatusListener implements TaskListener {
    @Autowired
    private WarrantyMaintenanceService maintenanceService;

    @Override
    public void notify(DelegateTask delegateTask) {
        WarrantyMaintenanceEntity maintenanceEntity = maintenanceService.findOneByProcessInstanceId(delegateTask.getProcessInstanceId());
        maintenanceEntity.setCanEditRecycle(!maintenanceEntity.getCanEditRecycle());
        maintenanceService.save(maintenanceEntity);
        System.out.println("切换三包服务单可编辑返回单状态为:" + maintenanceEntity.getCanEditRecycle());

    }
}
