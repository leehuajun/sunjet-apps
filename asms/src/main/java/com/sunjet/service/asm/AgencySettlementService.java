package com.sunjet.service.asm;

import com.sunjet.model.asm.AgencySettlementEntity;
import com.sunjet.model.asm.PartExpenseListEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface AgencySettlementService extends BaseService {
    AgencySettlementEntity findOneById(String objId);

    List<PartExpenseListEntity> findPartExpenseListById(String objId);

}
