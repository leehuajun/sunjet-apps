package com.sunjet.repository.asm;

import com.sunjet.model.asm.SupplyNoticeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 供货/发货 通知单
 * Created by lhj on 16/9/17.
 */
public interface SupplyNoticeItemRepository extends JpaRepository<SupplyNoticeItemEntity, String>, JpaSpecificationExecutor<SupplyNoticeItemEntity> {
    @Query("select p from SupplyNoticeItemEntity p where p.supplyNotice.objId like ?1")
    List<SupplyNoticeItemEntity> findByNoticeId(String supplyNoticeEntityID);
}
