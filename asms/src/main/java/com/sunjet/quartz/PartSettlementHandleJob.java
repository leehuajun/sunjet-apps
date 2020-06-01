package com.sunjet.quartz;

import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.asm.SupplyEntity;
import com.sunjet.repository.asm.PendingSettlementDetailsRepository;
import com.sunjet.repository.asm.SupplyRepository;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的调拨供货单的结算信息写入待结算列表
 */
public class PartSettlementHandleJob implements Executor {
    @Autowired
    private SupplyRepository supplyRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;

    @Override
    public void doExecute() {
        try {
            List<SupplyEntity> supplys = supplyRepository.findAllbySettlement(DocStatus.CLOSED.getIndex());
            for (SupplyEntity model : supplys) {
                PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(model.getObjId());
                if (pendingSettlementDetail != null) {
                    continue;
                }
                PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
                entity.setAgencyName(model.getAgencyName());
                entity.setAgencyCode(model.getAgencyCode());
                entity.setDealerCode(model.getDealerCode());
                entity.setDealerName(model.getDealerName());
                entity.setOtherExpense(model.getOtherExpense());
                entity.setOutExpense(0.0);
                entity.setExpenseTotal(model.getExpenseTotal());
                entity.setPartExpense(model.getPartExpense());
                entity.setFreightExpense(model.getTransportExpense());
                entity.setBusinessDate(model.getCreatedTime());
                entity.setSrcDocID(model.getObjId());
                entity.setSrcDocNo(model.getDocNo());
                entity.setSrcDocType("调拨供货单");
                entity.setSettlementDocType("配件结算单");
                entity.setCreatedTime(new Date());
                entity.setCreaterName("job");
                entity.setEnabled(true);
                entity.setSettlement(false);
                pendingSettlementDetailsRepository.save(entity);
                model.setSettlement(true);
                model.setModifiedTime(new Date());
                model.setModifierName("job");
                // 把数据取过来后，把单据状态修改为待结算状态
                model.setStatus(DocStatus.WAITING_SETTLE.getIndex());
                supplyRepository.save(model);
            }
            LoggerUtil.getLogger().info("PartSettlementHandleJob");
        } catch (Exception ex) {
            LoggerUtil.getLogger().error(ex.getMessage());
        }
    }
}
