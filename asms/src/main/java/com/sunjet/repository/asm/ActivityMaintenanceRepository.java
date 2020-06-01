package com.sunjet.repository.asm;

import com.sunjet.model.asm.ActivityMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务活动维修 dao
 * Created by lhj on 16/9/17.
 */
public interface ActivityMaintenanceRepository extends JpaRepository<ActivityMaintenanceEntity, String>, JpaSpecificationExecutor<ActivityMaintenanceEntity> {
    @Query("select an from ActivityMaintenanceEntity an where an.objId=?1")
    ActivityMaintenanceEntity findOneById(String objId);

    @Query("select an from ActivityMaintenanceEntity an left join fetch an.goOuts left join fetch an.commissionParts where an.objId=?1")
    ActivityMaintenanceEntity findOneWithOthers(String objId);

    @Query("select an from ActivityMaintenanceEntity an left join fetch an.goOuts where an.activityVehicle.vehicle.objId=?1 order by an.repairDate desc")
    ActivityMaintenanceEntity findOneWithVehicleById(String objId);

    @Query("select an from ActivityMaintenanceEntity an where  an.status=?1")
    List<ActivityMaintenanceEntity> findAllbySettlement(int status);
}
