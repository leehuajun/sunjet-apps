package com.sunjet.quartz;

import com.sunjet.model.asm.*;
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

import java.util.List;

/**
 * Created by zyh on 16/11/25.
 * 将关闭的三包服务单的结算信息写入待结算列表
 */
public class WarrantyCheckStatusJob implements Executor {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;
    @Autowired
    private SupplyItemRepository supplyItemRepository;
    @Autowired
    private RecycleItemRepository recycleItemRepository;
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;
    @Autowired
    private ProcessService processService;


    @Override
    public void doExecute() {
        try {
            List<WarrantyMaintenanceEntity> auditedWarranties = warrantyMaintenanceRepository.findAudited(DocStatus.AUDITED.getIndex());
            LoggerUtil.getLogger().info("auditedWarranties.size():" + auditedWarranties.size());
            if (auditedWarranties.size() <= 0)
                return;

            // 存在已审核的三包单据
            for (WarrantyMaintenanceEntity maintenanceEntity : auditedWarranties) {
//                if (checkSupplyIsOk(maintenanceEntity)) {
                if (checkRecycleIsOk(maintenanceEntity)) {
                    maintenanceEntity.setStatus(DocStatus.CLOSED.getIndex());
                    warrantyMaintenanceRepository.save(maintenanceEntity);


                    String businessKey = maintenanceEntity.getClass().getSimpleName()
                            + "." + maintenanceEntity.getObjId()
                            + "." + maintenanceEntity.getDocNo()
                            + "." + maintenanceEntity.getSubmitterName();

                    List<Comment> comments = processService.findCommentByBusinessKey(businessKey);
                    if (comments == null || comments.size() == 0) {
                        businessKey = maintenanceEntity.getClass().getSimpleName()
                                + "." + maintenanceEntity.getObjId()
                                + "." + maintenanceEntity.getDocNo()
                                + "." + maintenanceEntity.getSubmitterName()
                                + "." + maintenanceEntity.getSubmitter();

                        comments = processService.findCommentByBusinessKey(businessKey);
                    }

                    for (Comment cmt : comments) {
                        CommentEntity commentEntity = new CommentEntity();
                        commentEntity.setFlowInstanceId(cmt.getProcessInstanceId());
                        commentEntity.setDoDate(cmt.getTime());
                        commentEntity.setUsername(maintenanceEntity.getSubmitterName());
                        commentEntity.setUserId(maintenanceEntity.getSubmitter());
                        commentEntity.setResult(this.getBeanFromJson(cmt.getFullMessage()).getResult());
                        commentEntity.setMessage(this.getBeanFromJson(cmt.getFullMessage()).getMessage());
                        processService.saveComment(commentEntity);
                    }
                }
//                }
            }
        } catch (Exception ex) {
            LoggerUtil.getLogger().error(ex.getMessage());
        }
    }

    private CustomComment getBeanFromJson(String json) {
        CustomComment comment;
        try {//        return JsonHelper.json2Bean(json,CustomComment.class);
            comment = JsonHelper.json2Bean(json, CustomComment.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            comment = new CustomComment("", json);
        }
        return comment;
    }

    /**
     * 根据三包维修单，判断其关联的故障件返回单是否已经关闭
     *
     * @param warrantyEntity
     * @return
     */
    private Boolean checkRecycleIsOk(WarrantyMaintenanceEntity warrantyEntity) {

        if (StringUtils.isBlank(warrantyEntity.getRecycleNoticeId())) {
            return true;
        }


        List<RecycleNoticeItemEntity> recycleNoticeItemEntities = recycleNoticeItemRepository.findByNoticeId(warrantyEntity.getRecycleNoticeId());
        for (RecycleNoticeItemEntity noticeItem : recycleNoticeItemEntities) {
            // 4. 需求数量大于已发送数量，表示还未全部发运回来，直接跳过
            if (noticeItem.getAmount() > noticeItem.getBackAmount()) {
                return false;
            }
        }


        for (RecycleNoticeItemEntity noticeItem : recycleNoticeItemEntities) {
            List<RecycleItemEntity> recycleItems = recycleItemRepository.findAllByNoticeItemId(noticeItem.getObjId());
            //如果没有生成故障件返回单返回false
            if(recycleItems.size() ==0){
                //recycleItems = recycleItemRepository.findAllBySrcDocNo(noticeItem.getRecycleNotice().getSrcDocNo());
                //if(recycleItems.size()==0)
                return false;
            }
            for (RecycleItemEntity item : recycleItems) {
                //如果故障件返回单的状态不等于关闭状态返回false
                if(item.getRecycleEntity()!=null){
                    if (item.getRecycleEntity().getStatus() != DocStatus.CLOSED.getIndex()
                            && item.getRecycleEntity().getStatus() != DocStatus.WAITING_SETTLE.getIndex()
                            && item.getRecycleEntity().getStatus() != DocStatus.SETTLED.getIndex()
                            && item.getRecycleEntity().getStatus() != DocStatus.SETTLING.getIndex()) {
                        //忽略作废单据
                        //if(item.getRecycleEntity().getStatus()==DocStatus.OBSOLETE.getIndex()){
                        //    break;
                        //}
                        return false;
                    }
                }

            }

        }
        return true;
    }

    /**
     * 根据三包维修单，判断其关联的供货单是否已经关闭
     *
     * @param warrantyEntity
     * @return
     */
    private Boolean checkSupplyIsOk(WarrantyMaintenanceEntity warrantyEntity) {
        Boolean supplyIsOk = true;     // 判断调拨供货单是否已关闭
        if (StringUtils.isBlank(warrantyEntity.getSupplyNoticeId())) {
            return true;
        }
        // 2. 获取该调拨通知单的所有子项。
        List<SupplyNoticeItemEntity> supplyNoticeItemEntities = supplyNoticeItemRepository.findByNoticeId(warrantyEntity.getSupplyNoticeId());
//                    SupplyNoticeEntity supplyNoticeEntity = supplyNoticeRepository.findOne(maintenanceEntity.getSupplyNoticeId());
        // 3. 判断子项的需求数量和已发数量是否一致。
        for (SupplyNoticeItemEntity noticeItem : supplyNoticeItemEntities) {
            // 4. 需求数量大于已发送数量，表示还未全部发运回来，直接跳过
            if (noticeItem.getRequestAmount() > noticeItem.getSentAmount()) {
                supplyIsOk = false;
                break;
            }
        }
        // 5. 判断已发送的是否已经全部接收（流程是否关闭）
        for (SupplyNoticeItemEntity noticeItem : supplyNoticeItemEntities) {
            List<SupplyItemEntity> supplyItems = supplyItemRepository.findAllByNoticeItemId(noticeItem.getObjId());
            //如果调拨供货单没有生成返回false
            if(supplyItems.size() ==0){
                supplyIsOk = false;
            }
            for (SupplyItemEntity item : supplyItems) {
                if (item.getSupply().getStatus() != DocStatus.CLOSED.getIndex() && item.getSupply().getStatus() != DocStatus.WAITING_SETTLE.getIndex()
                        && item.getSupply().getStatus() != DocStatus.SETTLED.getIndex() && item.getSupply().getStatus() != DocStatus.SETTLING.getIndex()) {
                    supplyIsOk = false;
                    break;
                }
            }
            if (supplyIsOk == false) {
                break;
            }
        }
        return supplyIsOk;
    }
}
