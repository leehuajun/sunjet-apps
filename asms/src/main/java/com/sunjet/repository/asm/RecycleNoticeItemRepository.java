package com.sunjet.repository.asm;

import com.sunjet.model.asm.RecycleNoticeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 故障件返回清单
 * <p>
 * Created by lhj on 16/9/17.
 */
public interface RecycleNoticeItemRepository extends JpaRepository<RecycleNoticeItemEntity, String>, JpaSpecificationExecutor<RecycleNoticeItemEntity> {
    @Query("select v from RecycleNoticeItemEntity v  where v.recycleNotice.status=2 and( v.partCode like ?1 or v.partName like ?1 )")
    List<RecycleNoticeItemEntity> findAllPart(String key);

    @Query("select v from RecycleNoticeItemEntity v  where v.recycleNotice.dealerCode=?2 and v.recycleNotice.status=3 and v.currentAmount>0 and( v.partCode like ?1 or v.partName like ?1 )")
    List<RecycleNoticeItemEntity> findCanReturnPart(String key, String dealerCode);

    @Query("select rn from RecycleNoticeItemEntity rn where rn.recycleNotice.objId=?1")
    List<RecycleNoticeItemEntity> findByNoticeId(String recycleNoticeId);
}
