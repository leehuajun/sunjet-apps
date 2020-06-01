package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyNoticeItemEntity;
import com.sunjet.repository.asm.SupplyNoticeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/11/7.
 */
@Transactional
@Service("supplyNoticeItemService")
public class SupplyNoticeItemServiceImpl implements SupplyNoticeItemService {
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyNoticeItemRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return supplyNoticeItemRepository;
    }

    @Override
    public SupplyNoticeItemEntity findOne(String objId) {

        return supplyNoticeItemRepository.findOne(objId);
    }
}
