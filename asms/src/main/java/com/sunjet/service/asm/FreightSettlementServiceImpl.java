package com.sunjet.service.asm;

import com.sunjet.model.asm.FreightExpenseEntity;
import com.sunjet.model.asm.FreightSettlementEntity;
import com.sunjet.repository.asm.FreightSettlementRepository;
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
@Service("freightSettlementService")
public class FreightSettlementServiceImpl implements FreightSettlementService {
    @Autowired
    private FreightSettlementRepository freightSettlementRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return freightSettlementRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return freightSettlementRepository;
    }

    @Override
    public FreightSettlementEntity findOneById(String objId) {
        return freightSettlementRepository.findOneById(objId);
    }

    @Override
    public List<FreightExpenseEntity> findFreightExpenseById(String objId) {
        return freightSettlementRepository.findFreightExpenseById(objId);
    }
}
