package com.sunjet.service.asm;

import com.sunjet.repository.asm.CommissionPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("commissionPartService")
public class CommissionPartServiceImpl implements CommissionPartService {
    @Autowired
    private CommissionPartRepository commissionPartRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return commissionPartRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return commissionPartRepository;
    }
}
