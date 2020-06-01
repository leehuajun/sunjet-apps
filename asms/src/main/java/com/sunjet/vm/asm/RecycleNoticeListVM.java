package com.sunjet.vm.asm;

import com.sunjet.model.asm.*;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.RecycleNoticeService;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
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
 * 故障件返回通知单 列表 VM
 */
public class RecycleNoticeListVM extends FlowListBaseVM {
    @WireVariable
    private RecycleNoticeService recycleNoticeService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @WireVariable
    private UserService userService;

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        this.setBaseService(recycleNoticeService);
        this.setFormUrl("/views/asm/recycle_notice_form.zul");
        this.setEnableDelete(hasPermission(RecycleNoticeEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(RecycleNoticeEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(RecycleNoticeEntity.class.getSimpleName() + ":modify"));

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

    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("createdTime", SearchOrder.OrderType.DESC, 1)
                //        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
        ));
    }

    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<RecycleNoticeEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p08 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            Predicate p = cb.and(p04, p05, p08);
            if (CommonHelper.getActiveUser().getDealer() != null) {
                Predicate p00 = CustomRestrictions.eq("dealerCode", CommonHelper.getActiveUser().getDealer().getCode(), true).toPredicate(root, query, cb);
                p = cb.and(p, p00);
            }

            // 服务站编号
            if (this.getDealer() != null) {
                Predicate p01 = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p01);
            }
            //单据编号
            if (StringUtils.isNotBlank(this.getDocNo())) {
                Predicate p02 = CustomRestrictions.like("docNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p02);
            }
            //来源单据号
            if (StringUtils.isNotBlank(this.getSrcDocNo())) {
                Predicate p03 = CustomRestrictions.like("srcDocNo", this.getSrcDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }
            //来源单据
            if (StringUtils.isNotBlank(this.getSrcDocType())) {
                Predicate p06 = CustomRestrictions.like("srcDocType", this.getSrcDocType().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p07 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p07);
            }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange("resultDTO")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        if (!flowEntity.getStatus().equals(0)) {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
            return;
        }
        RecycleNoticeEntity entity = (RecycleNoticeEntity) flowEntity;
        if (entity.getSrcDocNo() == null) {
            //WarrantyMaintenanceEntity warrantyMaintenanceEntity = warrantyMaintenanceService.findOneWithOthersBySrcDocNo(entity.getSrcDocNo());
            //warrantyMaintenanceEntity.setRecycleNoticeId(null);
            //warrantyMaintenanceService.save(warrantyMaintenanceEntity);
            super.deleteFlowEntity(flowEntity);
        } else {
            ZkUtils.showInformation("单据不能删除！", "提示");
            return;
        }
        //else if(entity.getSrcDocType().contains("三包服务单")){
        //    WarrantyMaintenanceEntity maintenanceEntity = (WarrantyMaintenanceEntity) warrantyMaintenanceService.getRepository().findOne(entity.getSrcDocID());
        //    maintenanceEntity.setRecycleNoticeId(null);
        //    warrantyMaintenanceService.getRepository().save(maintenanceEntity);
        //}
        initList();
    }
}
