package com.sunjet.repository.asm;

import com.sunjet.model.asm.ReportVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ReportVehicleRepository extends JpaRepository<ReportVehicleEntity, String>, JpaSpecificationExecutor<ReportVehicleEntity> {
    //    @Query("select u from ReportVehicleEntity u left join fetch u.vehicle where u.vin=?1 ")
    @Query("select u from ReportVehicleEntity u where u.vehicle.vin like ?1 ")
    List<ReportVehicleEntity> fitlter(String keyword);

}
