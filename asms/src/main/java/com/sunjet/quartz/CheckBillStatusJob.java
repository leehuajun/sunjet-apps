package com.sunjet.quartz;

import com.sunjet.model.asm.*;
import com.sunjet.model.base.DocEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.flow.CommentEntity;
import com.sunjet.repository.asm.*;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.activiti.CustomComment;
import com.sunjet.utils.common.JsonHelper;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.vm.base.DocStatus;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.Doc;
import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 检查服务单结算状态，并把状态反写到单据上
 */
public class CheckBillStatusJob implements Executor {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;
    @Autowired
    private RecycleRepository recycleRepository;
    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;


    @Override
    public void doExecute() {
        try {
            // 检查首保单，服务结算单
            List<FirstMaintenanceEntity> firstMaintenanceEntities = firstMaintenanceRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (FirstMaintenanceEntity firstMaintenanceEntity : firstMaintenanceEntities) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(firstMaintenanceEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=firstMaintenanceEntity.getStatus()){
//                        firstMaintenanceEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        firstMaintenanceRepository.save(firstMaintenanceEntity);
//                    }
//                }
                this.chkEntity(firstMaintenanceRepository, firstMaintenanceEntity);
            }
            List<FirstMaintenanceEntity> firstMaintenanceEntities2 = firstMaintenanceRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (FirstMaintenanceEntity firstMaintenanceEntity : firstMaintenanceEntities2) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(firstMaintenanceEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=firstMaintenanceEntity.getStatus()){
//                        firstMaintenanceEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        firstMaintenanceRepository.save(firstMaintenanceEntity);
//                    }
//                }
                this.chkEntity(firstMaintenanceRepository, firstMaintenanceEntity);
            }

            // 检查活动服务单，服务结算单
            List<ActivityMaintenanceEntity> activityMaintenanceEntities = activityMaintenanceRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (ActivityMaintenanceEntity activityMaintenanceEntity : activityMaintenanceEntities) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(activityMaintenanceEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=activityMaintenanceEntity.getStatus()){
//                        activityMaintenanceEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        activityMaintenanceRepository.save(activityMaintenanceEntity);
//                    }
//                }
                this.chkEntity(activityMaintenanceRepository, activityMaintenanceEntity);
            }
            List<ActivityMaintenanceEntity> activityMaintenanceEntities2 = activityMaintenanceRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (ActivityMaintenanceEntity activityMaintenanceEntity : activityMaintenanceEntities2) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(activityMaintenanceEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=activityMaintenanceEntity.getStatus()){
//                        activityMaintenanceEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        activityMaintenanceRepository.save(activityMaintenanceEntity);
//                    }
//                }
                this.chkEntity(activityMaintenanceRepository, activityMaintenanceEntity);
            }

            // 检查三包服务单，服务结算单
            List<WarrantyMaintenanceEntity> warrantyMaintainEntities = warrantyMaintenanceRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintainEntities) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(warrantyMaintenanceEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=warrantyMaintenanceEntity.getStatus()){
//                        warrantyMaintenanceEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        warrantyMaintenanceRepository.save(warrantyMaintenanceEntity);
//                    }
//                }
                this.chkEntity(warrantyMaintenanceRepository, warrantyMaintenanceEntity);
            }
            List<WarrantyMaintenanceEntity> warrantyMaintainEntities2 = warrantyMaintenanceRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (WarrantyMaintenanceEntity warrantyMaintenanceEntity : warrantyMaintainEntities2) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(warrantyMaintenanceEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=warrantyMaintenanceEntity.getStatus()){
//                        warrantyMaintenanceEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        warrantyMaintenanceRepository.save(warrantyMaintenanceEntity);
//                    }
//                }
                this.chkEntity(warrantyMaintenanceRepository, warrantyMaintenanceEntity);
            }

            // 检查回收件返回单，运费结算单
            List<RecycleEntity> recycleEntities = recycleRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (RecycleEntity recycleEntity : recycleEntities) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(recycleEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=recycleEntity.getStatus()){
//                        recycleEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        recycleRepository.save(recycleEntity);
//                    }
//                }
                this.chkEntity(recycleRepository, recycleEntity);
            }

            List<RecycleEntity> recycleEntities2 = recycleRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (RecycleEntity recycleEntity : recycleEntities2) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(recycleEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=recycleEntity.getStatus()){
//                        recycleEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        recycleRepository.save(recycleEntity);
//                    }
//                }
                this.chkEntity(recycleRepository, recycleEntity);
            }

            // 检查发货单,配件结算单
            List<SupplyEntity> supplyEntities = supplyRepository.findAllbySettlement(DocStatus.WAITING_SETTLE.getIndex());
            for (SupplyEntity supplyEntity : supplyEntities) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(supplyEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=supplyEntity.getStatus()){
//                        supplyEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        supplyRepository.save(supplyEntity);
//                    }
//                }
                this.chkEntity(supplyRepository, supplyEntity);
            }
            List<SupplyEntity> supplyEntities2 = supplyRepository.findAllbySettlement(DocStatus.SETTLING.getIndex());
            for (SupplyEntity supplyEntity : supplyEntities2) {
//                PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(supplyEntity.getObjId());
//                if(pendingSettlementDetailsEntity!=null){
//                    if(pendingSettlementDetailsEntity.getSettlementStatus()!=supplyEntity.getStatus()){
//                        supplyEntity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
//                        supplyRepository.save(supplyEntity);
//                    }
//                }
                this.chkEntity(supplyRepository, supplyEntity);
            }

//            LoggerUtil.getLogger().info("单据结算状态改变");

        } catch (Exception ex) {
            LoggerUtil.getLogger().error(ex.getMessage());
        }
    }

    private void chkEntity(JpaRepository repository, FlowDocEntity entity) {
        PendingSettlementDetailsEntity pendingSettlementDetailsEntity = pendingSettlementDetailsRepository.getOneBySrcDocID(entity.getObjId());
        if (pendingSettlementDetailsEntity != null) {
            if (pendingSettlementDetailsEntity.getSettlementStatus() != null
                    && pendingSettlementDetailsEntity.getSettlementStatus() != entity.getStatus()) {
                entity.setStatus(pendingSettlementDetailsEntity.getSettlementStatus());
                repository.save(entity);
            }
        }
    }
}
