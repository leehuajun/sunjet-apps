package com.sunjet.vm.asm;

import com.sunjet.model.asm.SupplyEntity;
import com.sunjet.model.asm.SupplyItemEntity;
import com.sunjet.model.asm.SupplyWaitingItemEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.repository.asm.SupplyItemRepository;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.SupplyService;
import com.sunjet.service.asm.SupplyWaitingItemService;
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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 配件供货单 列表 VM
 */
public class SupplyListVM extends FlowListBaseVM {
    @WireVariable
    private SupplyService supplyService;
    @WireVariable
    private UserService userService;
    @WireVariable
    SupplyWaitingItemService supplyWaitingItemService;

    @WireVariable
    private SupplyItemRepository supplyItemRepository;

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        this.setFormUrl("/views/asm/supply_form.zul");
        this.setBaseService(supplyService);
        this.setEnableDelete(hasPermission(SupplyEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(SupplyEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(SupplyEntity.class.getSimpleName() + ":modify"));

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
        Specification<SupplyEntity> specification = (root, query, cb) -> {
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

            Subject subject = SecurityUtils.getSubject();
            ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
            AgencyEntity agency = userService.findOne(activeUser.getUserId()).getAgency();

            //合作商
            if (agency != null && !agency.getCode().isEmpty()) {
                Predicate p02 = CustomRestrictions.like("agencyCode", agency.getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p02);
            }


            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_LIST)
    @NotifyChange("*")
    public void supplyRefresh() {
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
        SupplyEntity supplyEntity = supplyService.findSupplyWithPartsById(flowEntity.getObjId());
        for (SupplyItemEntity item : supplyEntity.getItems()) {
            SupplyWaitingItemEntity supplyWaitingItem = item.getSupplyWaitingItem();
            if (supplyWaitingItem != null) {
                supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + item.getAmount());
                supplyWaitingItem.setSentAmount(supplyWaitingItem.getRequestAmount() - supplyWaitingItem.getSurplusAmount());
                supplyWaitingItem.setModifiedTime(new Date());
                supplyWaitingItem.setModifierName(CommonHelper.getActiveUser().getUsername());
                supplyWaitingItem.setModifierId(CommonHelper.getActiveUser().getUserId());
                supplyWaitingItemService.getRepository().save(supplyWaitingItem);
            }
        }
        supplyItemRepository.delete(supplyEntity.getItems());
        supplyService.deleteEntity(supplyEntity);
        initList();
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_WAITING, null);
    }
}


