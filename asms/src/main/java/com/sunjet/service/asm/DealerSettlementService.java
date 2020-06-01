package com.sunjet.service.asm;

import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.model.asm.ExpenseListEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface DealerSettlementService extends BaseService {
    DealerSettlementEntity findOneById(String objId);

    List<ExpenseListEntity> findExpenseListById(String objId);

}
