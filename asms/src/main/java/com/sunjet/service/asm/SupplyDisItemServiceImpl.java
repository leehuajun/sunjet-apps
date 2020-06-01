package com.sunjet.service.asm;

import com.sunjet.repository.asm.SupplyDisItemRepository;
import com.sunjet.repository.asm.SupplyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zyh on 2016/11/21.
 */
@Transactional
@Service("supplyDisItemService")
public class SupplyDisItemServiceImpl implements SupplyDisItemService {
    @Autowired
    private SupplyDisItemRepository supplyDisItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyDisItemRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return supplyDisItemRepository;
    }

}
