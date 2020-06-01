package com.sunjet.service.asm;

import com.sunjet.model.asm.AgencySettlementEntity;
import com.sunjet.model.asm.PartExpenseListEntity;
import com.sunjet.repository.asm.AgencySettlementRepository;
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
@Service("agencySettlementService")
public class AgencSettlementServiceImpl implements AgencySettlementService {
    @Autowired
    private AgencySettlementRepository agencySettlementRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencySettlementRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return agencySettlementRepository;
    }

    @Override
    public AgencySettlementEntity findOneById(String objId) {
        return agencySettlementRepository.findOneById(objId);
    }

    @Override
    public List<PartExpenseListEntity> findPartExpenseListById(String objId) {
        return agencySettlementRepository.findPartExpenseListById(objId);
    }
}
