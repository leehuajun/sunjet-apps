package com.sunjet.vm.settlement;

import com.sunjet.model.asm.FreightSettlementEntity;
import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.asm.FreightSettlementService;
import com.sunjet.service.asm.PendingSettlementDetailsService;
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
import java.util.Date;
import java.util.List;

/**
 * 返回件运费单列表
 * Created by zyh on 2016/11/14.
 */
public class FreightCostListVM extends FlowListBaseVM {
    @WireVariable
    private FreightSettlementService freightSettlementService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;

    @Init(superclass = true)
    public void init() {
        this.setFormUrl("/views/settlement/freight_cost_form.zul");
        this.setBaseService(freightSettlementService);
        initList();
        this.setEnableDelete(hasPermission(FreightSettlementEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(FreightSettlementEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(FreightSettlementEntity.class.getSimpleName() + ":modify"));
        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

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
        Specification<FreightSettlementEntity> specification = (root, query, cb) -> {
            Predicate p01 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p02 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p08 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            Predicate p = cb.and(p01, p02, p08);

            if (CommonHelper.getActiveUser().getDealer() != null) {
                Predicate p0 = CustomRestrictions.eq("dealerCode", CommonHelper.getActiveUser().getDealer().getCode(), true).toPredicate(root, query, cb);
                p = cb.and(p, p0);
            }

            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }

            // 服务站
            if (this.getDealer() != null) {
                Predicate p04 = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p04);
            }
            //单据编号
            if (StringUtils.isNotBlank(this.getDocNo())) {
                Predicate p05 = CustomRestrictions.like("docNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p05);
            }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange("resultDTO")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        if (flowEntity.getStatus().equals(0)) {
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(flowEntity.getObjId());
            if (details != null) {
                for (PendingSettlementDetailsEntity detail : details) {
                    detail.setSettlementDocType("运费结算单");
                    detail.setSettlementDocID(null);
                    detail.setSettlementDocNo(null);
                    detail.setSettlement(false);
                    detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                    detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                    detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                    detail.setModifiedTime(new Date());
                    pendingSettlementDetailsService.getRepository().save(detail);
                }
            }

            this.getBaseService().getRepository().delete(flowEntity.getObjId());
            initList();
        } else {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
        }
    }
}
