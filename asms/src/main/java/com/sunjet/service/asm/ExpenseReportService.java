package com.sunjet.service.asm;

import com.sunjet.model.asm.ExpenseReportEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * 售后费用速报
 * Created by lhj on 16/9/17.
 */
public interface ExpenseReportService extends BaseService {
    ExpenseReportEntity findOneById(String businessId);

    ExpenseReportEntity findOneWithVehicles(String objId);

    ExpenseReportEntity findOneWithVehiclesAndParts(String objId);

    List<ExpenseReportEntity> findAllByKeyword(String keyword);

    List<ExpenseReportEntity> findAllByKeywordAndDealerCode(String keyword, String dealerCode);

    void deleteEntity(ExpenseReportEntity entity);

}
