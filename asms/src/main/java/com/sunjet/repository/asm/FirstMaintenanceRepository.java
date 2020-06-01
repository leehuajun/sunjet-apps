package com.sunjet.repository.asm;

import com.sunjet.model.asm.FirstMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 首保 dao
 * Created by lhj on 16/9/17.
 */
public interface FirstMaintenanceRepository extends JpaRepository<FirstMaintenanceEntity, String>, JpaSpecificationExecutor<FirstMaintenanceEntity> {
    @Query("select fm from FirstMaintenanceEntity fm left join fetch fm.goOuts where fm.objId=?1")
    FirstMaintenanceEntity findOneWithGoOutsById(String objId);

    @Query("select fm from FirstMaintenanceEntity fm where  fm.status=?1")
    List<FirstMaintenanceEntity> findAllbySettlement(int status);
//    @Query("select fm from FirstMaintenanceEntity fm where fm.settlement=false and fm.status=?1")
//    List<FirstMaintenanceEntity> findAllbySettlement(int status);
//    @Query("select fm from FirstMaintenanceEntity fm where fm.vin=?1")
//    FirstMaintenanceEntity findVin(String vin);
}

