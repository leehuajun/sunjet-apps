package com.sunjet.repository.asm;

import com.sunjet.model.asm.ReportPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 速报配件子行
 * Created by Administrator on 2016/10/26.
 */
public interface ReportPartRepository extends JpaRepository<ReportPartEntity, String>, JpaSpecificationExecutor<ReportPartEntity> {
    //@Query("select u from QrVehicleEntity u where u.vehicle.vin like ?1 ")
    @Query("select r from ReportPartEntity r  where r.part.code like ?1")
    List<ReportPartEntity> filter(String keyword);


}
