package com.sunjet.service.asm;

import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.model.asm.ExpenseListEntity;
import com.sunjet.repository.asm.DealerSettlementRepository;
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
@Service("dealerSettlementService")
public class DealerSettlementServiceImpl implements DealerSettlementService {
    @Autowired
    private DealerSettlementRepository dealerSettlementRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerSettlementRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerSettlementRepository;
    }

    @Override
    public DealerSettlementEntity findOneById(String objId) {
        return dealerSettlementRepository.findOneById(objId);
    }

    @Override
    public List<ExpenseListEntity> findExpenseListById(String objId) {
        return dealerSettlementRepository.findExpenseListById(objId);
    }
}
