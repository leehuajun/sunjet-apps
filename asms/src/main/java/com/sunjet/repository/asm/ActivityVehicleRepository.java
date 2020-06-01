package com.sunjet.repository.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 活动通知车辆子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityVehicleRepository extends JpaRepository<ActivityVehicleEntity, String>, JpaSpecificationExecutor<ActivityVehicleEntity> {
    @Query("select u from ActivityVehicleEntity u where u.vehicle.vin like ?1 ")
    List<ActivityVehicleEntity> filter(String keyword);
//    @Query("select de from ActivityNoticeEntity de left join fetch de.vehicles left join fetch de.parts where de.docNo=?2 or de.title like ?1")
//    ActivityNoticeEntity findallDocNo(String s, String docNo);
//    @Query("select de from ActivityVehicleEntity de where de.docNo like ?2 or de.rowNum like ?1")
//    List<ActivityVehicleEntity> findAllByKeywordcurrent(String keyword, String current);
//    @Query("select de from ActivityVehicleEntity de where de.docNo like ?1")
//    List<ActivityVehicleEntity> findAllcurrent(String s);
}
