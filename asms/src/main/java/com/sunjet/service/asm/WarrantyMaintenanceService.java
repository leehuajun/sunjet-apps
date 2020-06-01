package com.sunjet.service.asm;

import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * 三包维修
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintenanceService extends BaseService, FlowBaseService {
    void save(WarrantyMaintenanceEntity warrantyMaintenanceRequest);

    WarrantyMaintenanceEntity findOneWithOthersById(String objId);

    void deleteEntity(WarrantyMaintenanceEntity entity);

    WarrantyMaintenanceEntity findOneWithVehicleById(String objId);

    WarrantyMaintenanceEntity findOneByProcessInstanceId(String processInstanceId);

    VehicleEntity findOneVehicleByDocNo(String docNo);

    WarrantyMaintenanceEntity findOneWithOthersBySrcDocNo(String srcDocNo);
//    WarrantyMaintenanceEntity findVin(String vin);
}
