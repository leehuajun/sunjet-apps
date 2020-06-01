package com.sunjet.vm.supply;

import com.sunjet.model.asm.SupplyDisItemEntity;
import com.sunjet.model.asm.SupplyNoticeItemEntity;
import com.sunjet.model.asm.SupplyWaitingItemEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.service.asm.SupplyDisItemService;
import com.sunjet.service.asm.SupplyNoticeItemService;
import com.sunjet.service.asm.SupplyWaitingItemService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
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

/**
 * 二次分配列表
 * Created by zyh on 2016/11/14.
 */
public class SupplySecondAllocationListVM extends FlowListBaseVM {

    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private SupplyDisItemService supplyDisItemService;
    @WireVariable
    private SupplyNoticeItemService supplyNoticeItemService;

    private SupplyDisItemEntity supplyDisItem = new SupplyDisItemEntity();
    private SupplyDisItemEntity currentSupplyDisItem;

    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableSaveAllocation(true);
        this.setBaseService(supplyDisItemService);
        initList();
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
        Specification<SupplyDisItemEntity> specification = (root, query, cb) -> {
            Predicate p01 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p02 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p03 = CustomRestrictions.gt("surplusAmount", 0, true).toPredicate(root, query, cb);
            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p01, p02, p03);

            //料品
            if (StringUtils.isNotBlank(this.supplyDisItem.getPartCode())) {
                Predicate p07 = CustomRestrictions.like("partCode", this.supplyDisItem.getPartCode().trim(), true).toPredicate(root, query, cb);
                Predicate p08 = CustomRestrictions.like("partName", this.supplyDisItem.getPartCode().trim(), true).toPredicate(root, query, cb);
                Predicate item = cb.or(p07, p08);
                p = cb.and(p, item);

            }


            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void createSupplyAllocation() {
        for (DocEntity Item : this.getResultDTO().getPageContent()) {
            SupplyDisItemEntity supplyDisItem = (SupplyDisItemEntity) Item;
            if (StringUtils.isNotBlank(supplyDisItem.getAgencyCode()) && supplyDisItem.getDistributionAmount() > 0) {
                SupplyWaitingItemEntity supplyWaitingItem = new SupplyWaitingItemEntity();
                supplyWaitingItem.setAgencyName(supplyDisItem.getAgencyName());
                supplyWaitingItem.setAgencyCode(supplyDisItem.getAgencyCode());
                supplyWaitingItem.setPartCode(supplyDisItem.getPartCode());
                supplyWaitingItem.setPartName(supplyDisItem.getPartName());
                supplyWaitingItem.setRequestAmount(supplyDisItem.getDistributionAmount());
                //supplyWaitingItem.setSentAmount(supplyNoticeItem.getSentAmount());
                supplyWaitingItem.setSurplusAmount(supplyDisItem.getDistributionAmount());
                supplyWaitingItem.setArrivalTime(supplyDisItem.getArrivalTime());
                supplyWaitingItem.setComment(supplyDisItem.getComment());
                supplyWaitingItem.setSupplyNoticeItem(supplyDisItem.getSupplyNoticeItem());
                supplyWaitingItem.setSupplyDisItem(supplyDisItem);
                supplyWaitingItem.setEnabled(true);
                supplyWaitingItem.setCreatedTime(new Date());
                supplyWaitingItem.setCreaterName(CommonHelper.getActiveUser().getUsername());
                supplyWaitingItem.setCreaterId(CommonHelper.getActiveUser().getUserId());
                supplyWaitingItem.setDealerName(supplyDisItem.getSupplyNoticeItem().getSupplyNotice().getDealerName());
                supplyWaitingItem.setDealerName(supplyDisItem.getSupplyNoticeItem().getSupplyNotice().getDealerCode());
                supplyWaitingItem.setServiceManager(supplyDisItem.getSupplyNoticeItem().getSupplyNotice().getServiceManager());
                supplyWaitingItemService.getRepository().save(supplyWaitingItem);
            }

            if (StringUtils.isNotBlank(supplyDisItem.getAgencyCode()) && supplyDisItem.getDistributionAmount() > 0) {
                supplyDisItem.setModifiedTime(new Date());
                supplyDisItem.setSurplusAmount(supplyDisItem.getSurplusAmount() - supplyDisItem.getDistributionAmount());
                supplyDisItem.setSentAmount(supplyDisItem.getRequestAmount() - supplyDisItem.getSurplusAmount());
                supplyDisItem.setDistributionAmount(0.0);
                supplyDisItemService.getRepository().save(supplyDisItem);
            }
        }
        filterList();
    }

    @Command
    public void selectSupplyDisItem(@BindingParam("model") SupplyDisItemEntity item) {
        if (item != null) {
            currentSupplyDisItem = item;
        }
    }


    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void clearSelectedAgencyByItem() {
        currentSupplyDisItem.setAgencyName(null);
        currentSupplyDisItem.setAgencyCode(null);
        currentSupplyDisItem.setDistributionAmount(0);
        this.setKeyword("");
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void sentAmountChange(@BindingParam("model") SupplyDisItemEntity item) {
        if (item != null) {
            currentSupplyDisItem = item;
            if (currentSupplyDisItem.getSurplusAmount() < currentSupplyDisItem.getDistributionAmount()) {
                ZkUtils.showInformation("本次分配数不能答应可分配数量", "提示");
                currentSupplyDisItem.setDistributionAmount(0);
            }
        }
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void selectAgencyByItem(@BindingParam("model") AgencyEntity agency) {
        currentSupplyDisItem.setAgencyName(agency.getName());
        currentSupplyDisItem.setAgencyCode(agency.getCode());
        currentSupplyDisItem.setDistributionAmount(currentSupplyDisItem.getSurplusAmount());
        this.setKeyword("");
    }


    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void returnSecondSupply(@BindingParam("entity") SupplyDisItemEntity supplyDisItemEntity) {
        SupplyNoticeItemEntity supplyNoticeItemEntity = supplyNoticeItemService.findOne(supplyDisItemEntity.getSupplyNoticeItem().getObjId());

        double SurplusAmount = supplyNoticeItemEntity.getSurplusAmount() + supplyDisItemEntity.getSurplusAmount();
        supplyNoticeItemEntity.setSurplusAmount(SurplusAmount);
        supplyNoticeItemEntity.setSecondaryDistribution(false);
        supplyNoticeItemService.getRepository().save(supplyNoticeItemEntity);
        supplyDisItemService.getRepository().delete(supplyDisItemEntity.getObjId());
        filterList();
    }

    public SupplyDisItemEntity getSupplyDisItem() {
        return supplyDisItem;
    }

    public void setSupplyDisItem(SupplyDisItemEntity supplyDisItem) {
        this.supplyDisItem = supplyDisItem;
    }
}
