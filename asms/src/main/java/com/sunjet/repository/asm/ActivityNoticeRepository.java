package com.sunjet.repository.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务活动通知单
 * Created by lhj on 16/9/17.
 */
public interface ActivityNoticeRepository extends JpaRepository<ActivityNoticeEntity, String>, JpaSpecificationExecutor<ActivityNoticeEntity> {

    @Query("select an from ActivityNoticeEntity an left join fetch an.activityVehicles where an.objId=?1")
    ActivityNoticeEntity findOneWithVehicles(String objId);

    @Query("select ex from ActivityNoticeEntity ex left join fetch ex.activityVehicles left join fetch ex.activityParts where ex.objId=?1")
    ActivityNoticeEntity findOneWithVehiclesAndParts(String objId);

    @Query("select de from ActivityNoticeEntity de where de.status=?1 and (de.docNo like ?2 or de.title like ?2)")
    List<ActivityNoticeEntity> findAllByStatusAndKeyword(Integer status, String keyword);

    @Query("select de from ActivityNoticeEntity de where de.docNo like ?2 or de.title like ?1")
    List<ActivityNoticeEntity> findAllByKeywordcurrent(String keyword, String docNo);

    @Query("select an from ActivityNoticeEntity an left join fetch an.activityVehicles where an.objId=?1")
    ActivityNoticeEntity findcurrent(String objId);

    @Query("select de from ActivityNoticeEntity de left join fetch de.activityVehicles left join fetch de.activityParts where de.docNo=?2 or de.title like ?1")
    ActivityNoticeEntity findallDocNo(String s, String docNo);

    @Query("select de from ActivityNoticeEntity de left join fetch de.activityParts where  de.objId=?1")
    ActivityNoticeEntity findOneWithParts(String objId);
}
