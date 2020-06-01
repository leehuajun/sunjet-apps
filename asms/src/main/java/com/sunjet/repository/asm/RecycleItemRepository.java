package com.sunjet.repository.asm;

import com.sunjet.model.asm.RecycleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface RecycleItemRepository extends JpaRepository<RecycleItemEntity, String>, JpaSpecificationExecutor<RecycleItemEntity> {
    @Query("select ri from RecycleItemEntity ri where ri.recycleEntity.objId=?1")
    List<RecycleItemEntity> findAllByNoticeItemId(String noticeItemId);

    @Query("select ri from RecycleItemEntity ri where ri.srcDocNo=?1")
    List<RecycleItemEntity> findAllBySrcDocNo(String srcDocNo);
}
