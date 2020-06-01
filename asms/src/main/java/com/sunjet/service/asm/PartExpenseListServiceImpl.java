package com.sunjet.service.asm;

import com.sunjet.repository.asm.PartExpenseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("partExpenseListService")
public class PartExpenseListServiceImpl implements PartExpenseListService {
    @Autowired
    private PartExpenseListRepository partExpenseListRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return partExpenseListRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return partExpenseListRepository;
    }
}
