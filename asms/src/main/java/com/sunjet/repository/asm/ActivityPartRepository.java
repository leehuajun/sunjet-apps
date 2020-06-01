package com.sunjet.repository.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 服务活动配件子行
 * Created by Administrator on 2016/10/26.
 */
public interface ActivityPartRepository extends JpaRepository<ActivityPartEntity, String>, JpaSpecificationExecutor<ActivityPartEntity> {
    @Query("select r from ActivityPartEntity r  where r.part.code like ?1")
    ActivityPartEntity filter(String keyword);

    @Query("select pe from ActivityPartEntity pe where pe.activityNotice.objId=?1")
    List<ActivityPartEntity> findActivityPartListByNoticeId(String noticeId);

    @Query("select an from ActivityNoticeEntity an left join fetch an.activityParts where an.objId=?1")
    ActivityNoticeEntity findOneWithPartById(String objId);
}
