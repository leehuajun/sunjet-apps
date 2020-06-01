package com.sunjet.service.asm;

import com.sunjet.model.asm.QualityReportEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

import java.util.List;

/**
 * 售后质量速报
 * Created by lhj on 16/9/17.
 */
public interface QualityReportService extends BaseService, FlowBaseService {
    QualityReportEntity findOneById(String businessId);

    QualityReportEntity findOneWithVehicles(String objId);

    QualityReportEntity findOneWithPartes(String objId);

    QualityReportEntity findOneWithVehiclesAndParts(String objId);

    void deleteEntity(QualityReportEntity entity);

    List<QualityReportEntity> findAllByKeyword(String keyword);

    List<QualityReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode);
}
