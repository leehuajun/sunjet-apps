package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityMaintenanceEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * 服务活动维修
 * Created by lhj on 16/9/17.
 */
public interface ActivityMaintenanceService extends BaseService, FlowBaseService {
    List<ActivityMaintenanceEntity> findall();

    void save(ActivityMaintenanceEntity activityMaintenanceRequest);

    ActivityMaintenanceEntity findOneWithVehicles(String objId);

    ActivityMaintenanceEntity findOneWithOthers(String objId);

    void deleteEntity(ActivityMaintenanceEntity entity);

    ActivityMaintenanceEntity findOneWithVehicleById(String objId);
}
