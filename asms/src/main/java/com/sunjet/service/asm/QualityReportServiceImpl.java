package com.sunjet.service.asm;

import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.repository.asm.QualityReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 售后质量速报
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("qualityReportService")
public class QualityReportServiceImpl implements QualityReportService {
    @Autowired
    private QualityReportRepository qualityReportRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return qualityReportRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return this.qualityReportRepository;
    }

    @Override
    public QualityReportEntity findOneById(String businessId) {
        return qualityReportRepository.findOne(businessId);

    }

    @Override
    public QualityReportEntity findOneWithVehicles(String objId) {
        return this.qualityReportRepository.findOneWithVehicles(objId);
    }

    @Override
    public QualityReportEntity findOneWithPartes(String objId) {
        return this.qualityReportRepository.findOneWithPartes(objId);
    }

    @Override
    public QualityReportEntity findOneWithVehiclesAndParts(String objId) {
        return this.qualityReportRepository.findOneWithVehiclesAndParts(objId);
    }

    @Override
    public void deleteEntity(QualityReportEntity entity) {
        qualityReportRepository.delete(entity);
        return;
    }

    @Override
    public List<QualityReportEntity> findAllByKeyword(String keyword) {
        return qualityReportRepository.findAllByKeyword(keyword);
    }

    @Override
    public List<QualityReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode) {
        return qualityReportRepository.findAllByKeywordAndDealerCode("%" + keyword + "%", dealerCode);
    }
}
