package com.sunjet.repository.asm;

import com.sunjet.model.asm.ActivityDistributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/11/5.
 */
public interface ActivityDistributionRepository extends JpaRepository<ActivityDistributionEntity, String>, JpaSpecificationExecutor<ActivityDistributionEntity> {
    @Query("select ex from ActivityDistributionEntity ex left join fetch ex.activityVehicles where ex.objId=?1")
    ActivityDistributionEntity findOneWithVehicles(String objId);

    // @Query("select distinct  de from ActivityDistributionEntity de left join fetch de.vehicles left join fetch de.parts where de.dealerCode like ?1 or de.dealerNames like ?1")
    @Query("select de from ActivityDistributionEntity de left join fetch de.activityVehicles where de.dealerCode=?1 or de.dealerName like ?1")
    List<ActivityDistributionEntity> findAllByKeyword(String keyword);

    @Query("select de from ActivityDistributionEntity de left join fetch de.activityVehicles where de.dealerCode=?1 or de.docNo=?2")
    ActivityDistributionEntity findAllWithVehicleByDocNo(String s, String docNo);

    @Query("select de from ActivityDistributionEntity de where status=?1 and docNo like ?2 and de.dealerCode=?3")
    List<ActivityDistributionEntity> findAllByStatusAndKeywordAndDealerCode(Integer status, String keyword, String dealerCode);
}
