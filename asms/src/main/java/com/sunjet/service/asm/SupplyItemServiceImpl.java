package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyItemEntity;
import com.sunjet.repository.asm.SupplyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("supplyItemService")
public class SupplyItemServiceImpl implements SupplyItemService {
    @Autowired
    private SupplyItemRepository supplyItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyItemRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return supplyItemRepository;
    }

    @Override
    public List<SupplyItemEntity> findSupplyItemsByDocID(String docid) {
        return supplyItemRepository.findSupplyItemsByDocID(docid);
    }
}
