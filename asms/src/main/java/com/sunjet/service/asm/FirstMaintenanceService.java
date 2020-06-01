package com.sunjet.service.asm;

import com.sunjet.model.asm.FirstMaintenanceEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * 首保
 * Created by lhj on 16/9/17.
 */
public interface FirstMaintenanceService extends BaseService, FlowBaseService {
    void save(FirstMaintenanceEntity firstMaintenanceRequest);

    List<FirstMaintenanceEntity> firsAll();

    FirstMaintenanceEntity findOneById(String businessId);

    FirstMaintenanceEntity findOneWithGoOutsById(String objId);

    void deleteEntity(FirstMaintenanceEntity entity);
}
