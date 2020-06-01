package com.sunjet.quartz;

import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.asm.RecycleEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.repository.asm.PendingSettlementDetailsRepository;
import com.sunjet.repository.asm.RecycleRepository;
import com.sunjet.repository.basic.DealerRepository;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的故障件返回单的运费结算信息写入待结算列表
 */
public class FreightSettlementHandleJob implements Executor {
    @Autowired
    private RecycleRepository recycleRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;

    @Override
    public void doExecute() {
        try {
            List<RecycleEntity> recycles = recycleRepository.findAllbySettlement(DocStatus.CLOSED.getIndex());
            for (RecycleEntity model : recycles) {
                PendingSettlementDetailsEntity pendingSettlementDetail = pendingSettlementDetailsRepository.getOneBySrcDocID(model.getObjId());
                if (pendingSettlementDetail != null) {
                    continue;
                }
                PendingSettlementDetailsEntity entity = new PendingSettlementDetailsEntity();
                DealerEntity parentDealer = dealerRepository.findParentDealerByDealerCode(model.getDealerCode());
                if (parentDealer == null) {
                    entity.setDealerCode(model.getDealerCode());
                    entity.setDealerName(model.getDealerName());
                } else {
                    entity.setDealerCode(parentDealer.getCode());
                    entity.setDealerName(parentDealer.getName());
                    entity.setSecondDealerCode(model.getDealerCode());
                    entity.setSecondDealerName(model.getDealerName());
                }
                entity.setOtherExpense(model.getOtherExpense());
                entity.setOutExpense(0.0);
                entity.setExpenseTotal(model.getExpenseTotal());
                entity.setFreightExpense(model.getTransportExpense());
                entity.setBusinessDate(model.getCreatedTime());
                entity.setSrcDocID(model.getObjId());
                entity.setSrcDocNo(model.getDocNo());
                entity.setSrcDocType("故障件返回单");
                entity.setSettlementDocType("运费结算单");
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
                recycleRepository.save(model);

            }
            LoggerUtil.getLogger().info("FreightSettlementHandleJob");
        } catch (Exception ex) {
            LoggerUtil.getLogger().error(ex.getMessage());
        }
    }
}

