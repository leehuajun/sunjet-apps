package com.sunjet.service.asm;

import com.sunjet.model.asm.FreightExpenseEntity;
import com.sunjet.model.asm.FreightSettlementEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface FreightSettlementService extends BaseService {
    FreightSettlementEntity findOneById(String objId);

    List<FreightExpenseEntity> findFreightExpenseById(String objId);

}
