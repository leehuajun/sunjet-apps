package com.sunjet.repository.asm;

import com.sunjet.model.asm.WarrantyMaintainEntity;
import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 三包维修项目
 * Created by lhj on 16/9/17.
 */
public interface WarrantyMaintenanRepository extends JpaRepository<WarrantyMaintainEntity, String>, JpaSpecificationExecutor<WarrantyMaintenanceEntity> {

}
