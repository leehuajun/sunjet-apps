package com.sunjet.repository.asm;

import com.sunjet.model.asm.SupplyWaitingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface SupplyWaitingItemRepository extends JpaRepository<SupplyWaitingItemEntity, String>, JpaSpecificationExecutor<SupplyWaitingItemEntity> {

    @Query("select u from SupplyWaitingItemEntity u where u.supplyNoticeItem.objId like ?1 ")
    List<SupplyWaitingItemEntity> findSupplyWaitingItems(String supplyID);

    @Query("select u from SupplyWaitingItemEntity u where u.objId like ?1 ")
    SupplyWaitingItemEntity findSupplyWaitingItemById(String objId);
}
