package com.sunjet.service.asm;

import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.service.base.BaseService;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 2016/11/14.
 */
public interface PendingSettlementDetailsService extends BaseService {
    List<PendingSettlementDetailsEntity> getAgencySelttlements(String agencyCode, Date startDate, Date endDate);

    PendingSettlementDetailsEntity getOneBySrcDocID(String srcDocID);

    List<PendingSettlementDetailsEntity> getOneBySettlementID(String objId);

    List<PendingSettlementDetailsEntity> getDealerSelttlements(String code, Date startDate, Date endDate);

    List<PendingSettlementDetailsEntity> getFreightSelttlements(String code, Date startDate, Date endDate);

}
