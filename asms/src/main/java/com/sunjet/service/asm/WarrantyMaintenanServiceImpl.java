package com.sunjet.service.asm;

import com.sunjet.repository.asm.WarrantyMaintenanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 三包维修项目
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("warrantyMaintenanService")
public class WarrantyMaintenanServiceImpl implements WarrantyMaintenanService {
    @Autowired
    private WarrantyMaintenanRepository warrantyMaintenanRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return warrantyMaintenanRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return warrantyMaintenanRepository;
    }


}
