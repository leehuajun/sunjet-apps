package com.sunjet.service.asm;

import com.sunjet.repository.asm.ExpenseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("expenseListService")
public class ExpenseListServiceImpl implements ExpenseListService {
    @Autowired
    private ExpenseListRepository expenseListRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return expenseListRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return expenseListRepository;
    }
}
