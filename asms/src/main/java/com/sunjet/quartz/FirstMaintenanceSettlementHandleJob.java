package com.sunjet.quartz;

import com.sunjet.model.asm.FirstMaintenanceEntity;
import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.repository.asm.FirstMaintenanceRepository;
import com.sunjet.repository.asm.PendingSettlementDetailsRepository;
import com.sunjet.repository.basic.DealerRepository;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的首保服务单的结算信息写入待结算列表
 */
public class FirstMaintenanceSettlementHandleJob implements Executor {
    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;
    @Autowired
    private DealerRepository dealerRepository;

    @Override
    public void doExecute() {
        try {
            List<FirstMaintenanceEntity> firstMaintenanceEntities = firstMaintenanceRepository.findAllbySettlement(DocStatus.CLOSED.getIndex());
            for (FirstMaintenanceEntity model : firstMaintenanceEntities) {
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
                entity.setWorkingExpense(model.getStandardExpense());
                entity.setOtherExpense((model.getOtherExpense() == null ? 0.0 : model.getOtherExpense()) + (model.getNightExpense() == null ? 0.0 : model.getNightExpense()));
                entity.setOutExpense(model.getOutExpense());
                entity.setExpenseTotal(model.getExpenseTotal());
                entity.setPartExpense(0.0);
                entity.setFreightExpense(0.0);
                entity.setBusinessDate(model.getCreatedTime());
                entity.setSrcDocID(model.getObjId());
                entity.setSrcDocNo(model.getDocNo());
                entity.setSrcDocType("首保服务单");
                entity.setSettlementDocType("服务结算单");
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
                firstMaintenanceRepository.save(model);
            }
            LoggerUtil.getLogger().info("FirstMaintenanceSettlementHandleJob");
        } catch (Exception ex) {
            LoggerUtil.getLogger().error(ex.getMessage());
        }
    }
}
