package com.sunjet.vm.asm;

import com.sunjet.model.asm.SupplyNoticeEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.asm.ActivityDistributionService;
import com.sunjet.service.asm.SupplyNoticeService;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 配件供货单 列表 VM
 */
public class SupplyNoticeListVM extends FlowListBaseVM {
    @WireVariable
    private SupplyNoticeService supplyNoticeService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private ActivityDistributionService activityDistributionService;

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        this.setFormUrl("/views/asm/supply_notice_form.zul");
        this.setBaseService(supplyNoticeService);
        this.setEnableDelete(hasPermission(SupplyNoticeEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(SupplyNoticeEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(SupplyNoticeEntity.class.getSimpleName() + ":modify"));

        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
        } else {
            filterList();
        }
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     * @param searchDTO
     */
    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("createdTime", SearchOrder.OrderType.DESC, 1)
//        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<SupplyNoticeEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p07 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            Predicate p = cb.and(p04, p05, p07);
            if (CommonHelper.getActiveUser().getDealer() != null) {
                Predicate p00 = CustomRestrictions.eq("dealerCode", CommonHelper.getActiveUser().getDealer().getCode(), true).toPredicate(root, query, cb);
                p = cb.and(p, p00);
            }
            if (CommonHelper.getActiveUser().getAgency() != null) {
                Predicate p00 = CustomRestrictions.eq("agencyCode", CommonHelper.getActiveUser().getAgency().getCode(), true).toPredicate(root, query, cb);
                p = cb.and(p, p00);
            }

            // 服务站编号
            if (this.getDealer() != null) {
                Predicate p01 = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p01);
            }
            //单据编号
            if (StringUtils.isNotBlank(this.getDocNo())) {
                Predicate p06 = CustomRestrictions.like("docNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }
            return p;
        };
        searchDTO.setSpecification(specification);
    }


    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_NOTICE_LIST)
    @NotifyChange("*")
    public void supplyNoticeListRefresh() {
        initList();
    }

    @Override
    @Command
    @NotifyChange("resultDTO")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        if (!flowEntity.getStatus().equals(0)) {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
            return;
        }
        SupplyNoticeEntity entity = (SupplyNoticeEntity) flowEntity;
        if (entity.getSrcDocType() == null) {
            super.deleteFlowEntity(flowEntity);
        } else {
            ZkUtils.showInformation("有来源单据不能删除", "提示");
        }
        //if (entity.getSrcDocType() != null) {
        //    if (entity.getSrcDocType().contains("三包服务单")) {
        //        WarrantyMaintenanceEntity maintenanceEntity = (WarrantyMaintenanceEntity) warrantyMaintenanceService.getRepository().findOne(entity.getSrcDocID());
        //        if (maintenanceEntity != null) {
        //            maintenanceEntity.setSupplyNoticeId(null);
        //            maintenanceEntity.setCanEditSupply(true);
        //            warrantyMaintenanceService.getRepository().save(maintenanceEntity);
        //        }
        //    } else if (entity.getSrcDocType().contains("活动分配单")) {
        //        ActivityDistributionEntity activityDistributionEntity = (ActivityDistributionEntity) activityDistributionService.getRepository().findOne(entity.getSrcDocID());
        //        if (activityDistributionEntity != null) {
        //            activityDistributionEntity.setSupplyNoticeId(null);
        //            activityDistributionEntity.setCanEditSupply(true);
        //            warrantyMaintenanceService.getRepository().save(activityDistributionEntity);
        //        }
        //    }
        //}
        initList();
    }
}


