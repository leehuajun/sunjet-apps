package com.sunjet.repository.asm;

import com.sunjet.model.asm.SupplyItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface SupplyItemRepository extends JpaRepository<SupplyItemEntity, String>, JpaSpecificationExecutor<SupplyItemEntity> {
    @Query("select u from SupplyItemEntity u where u.supply.objId like ?1 ")
    List<SupplyItemEntity> findSupplyItemsByDocID(String docid);

    @Query("select si from SupplyItemEntity si where si.supplyNoticeItem.objId=?1")
    List<SupplyItemEntity> findAllByNoticeItemId(String noticeItemId);
}
