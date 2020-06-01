package com.sunjet.repository.asm;

import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 三包维修单
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintenanceRepository extends JpaRepository<WarrantyMaintenanceEntity, String>, JpaSpecificationExecutor<WarrantyMaintenanceEntity> {
    @Query("select wm from WarrantyMaintenanceEntity wm left join fetch wm.warrantyMaintains left join fetch wm.commissionParts left join fetch wm.goOuts where wm.objId=?1")
    WarrantyMaintenanceEntity findOneWithOthersById(String objId);

    @Query("select wm from WarrantyMaintenanceEntity wm left join fetch wm.warrantyMaintains left join fetch wm.commissionParts left join fetch wm.goOuts where wm.vehicle.objId=?1 order by wm.requestDate desc")
    WarrantyMaintenanceEntity findOneWithVehicleById(String objId);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.processInstanceId=?1")
    WarrantyMaintenanceEntity findOneByProcessInstanceId(String processInstanceId);

    @Query("select wm from WarrantyMaintenanceEntity wm where  wm.status=?1")
    List<WarrantyMaintenanceEntity> findAllbySettlement(int status);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.status=?1")
    List<WarrantyMaintenanceEntity> findAudited(Integer status);

    //    @Query("select wm from WarrantyMaintenanceEntity wm left join fetch wm.maintainItems left join fetch wm.commissionParts left join fetch wm.goOutEntities where wm.vin=?1")
//    WarrantyMaintenanceEntity findVin(String vin);
    @Query("select wm.vehicle from WarrantyMaintenanceEntity wm  where wm.docNo=?1")
    VehicleEntity findOneVehicleByDocNo(String docNo);

    @Query("select wm from WarrantyMaintenanceEntity wm where wm.docNo=?1")
    WarrantyMaintenanceEntity findOneWithOthersBySrcDocNo(String srcDocNo);
}
