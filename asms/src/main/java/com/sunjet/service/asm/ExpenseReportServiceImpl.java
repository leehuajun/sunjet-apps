package com.sunjet.service.asm;

import com.sunjet.model.asm.ExpenseReportEntity;
import com.sunjet.repository.asm.ExpenseReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 售后费用速报
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("expenseReportService")
public class ExpenseReportServiceImpl implements ExpenseReportService {
    @Autowired
    private ExpenseReportRepository expenseReportRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return expenseReportRepository;
    }

    @Override
    public ExpenseReportEntity findOneById(String businessId) {
        return expenseReportRepository.findOne(businessId);
    }

    @Override
    public ExpenseReportEntity findOneWithVehicles(String objId) {
        return this.expenseReportRepository.findOneWithVehicles(objId);
    }

    @Override
    public ExpenseReportEntity findOneWithVehiclesAndParts(String objId) {
        return this.expenseReportRepository.findOneWithVehiclesAndParts(objId);
    }

    @Override
    public List<ExpenseReportEntity> findAllByKeyword(String keyword) {
        return expenseReportRepository.findAllByKeyword(keyword);
    }

    @Override
    public List<ExpenseReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode) {
        return expenseReportRepository.findAllByKeywordAndDealerCode("%" + keyword + "%", dealerCode);
    }

    @Override
    public void deleteEntity(ExpenseReportEntity entity) {
        expenseReportRepository.delete(entity.getObjId());
        return;
    }

    @Override
    public JpaRepository getRepository() {
        return this.expenseReportRepository;
    }
}
