package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyWaitingItemEntity;
import com.sunjet.repository.asm.SupplyDisItemRepository;
import com.sunjet.repository.asm.SupplyWaitingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zyh on 2016/11/21.
 */

@Transactional
@Service("supplyWaitingItemService")
public class SupplyWaitingItemServiceImpl implements SupplyWaitingItemService {
    @Autowired
    private SupplyWaitingItemRepository supplyWaitingItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyWaitingItemRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return supplyWaitingItemRepository;
    }


    @Override
    public List<SupplyWaitingItemEntity> findSupplyWaitingItems(String supplyID) {
        return supplyWaitingItemRepository.findSupplyWaitingItems(supplyID);
    }

    @Override
    public SupplyWaitingItemEntity findSupplyWaitingItemById(String objId) {
        return supplyWaitingItemRepository.findSupplyWaitingItemById(objId);
    }
}
