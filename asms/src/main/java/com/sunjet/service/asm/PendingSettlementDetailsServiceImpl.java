package com.sunjet.service.asm;

import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.repository.asm.PendingSettlementDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 2016/11/14.
 */
@Transactional
@Service("pendingSettlementDetailsService")
public class PendingSettlementDetailsServiceImpl implements PendingSettlementDetailsService {
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return pendingSettlementDetailsRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return pendingSettlementDetailsRepository;
    }

    @Override
    public List<PendingSettlementDetailsEntity> getAgencySelttlements(String agencyCode, Date startDate, Date endDate) {
        return pendingSettlementDetailsRepository.getAgencySelttlements(agencyCode, startDate, endDate);
    }

    @Override
    public PendingSettlementDetailsEntity getOneBySrcDocID(String srcDocID) {
        return pendingSettlementDetailsRepository.getOneBySrcDocID(srcDocID);
    }

    @Override
    public List<PendingSettlementDetailsEntity> getOneBySettlementID(String objId) {
        return pendingSettlementDetailsRepository.getOneBySettlementID(objId);
    }

    @Override
    public List<PendingSettlementDetailsEntity> getDealerSelttlements(String dealerCode, Date startDate, Date endDate) {
        return pendingSettlementDetailsRepository.getDealerSelttlements(dealerCode, startDate, endDate);
    }

    @Override
    public List<PendingSettlementDetailsEntity> getFreightSelttlements(String code, Date startDate, Date endDate) {
        return pendingSettlementDetailsRepository.getFreightSelttlements(code, startDate, endDate);
    }
}
