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
 * 调拨分配列表
 * Created by zyh on 2016/11/14.
 */
public class SupplyAllocationListVM extends FlowListBaseVM {

    @WireVariable
    private SupplyNoticeItemService supplyNoticeItemService;
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private SupplyDisItemService supplyDisItemService;
    private SupplyNoticeItemEntity supplyNoticeItem = new SupplyNoticeItemEntity();
    private SupplyNoticeItemEntity currentsupplyNoticeItem;

    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableSaveAllocation(true);
        this.setBaseService(supplyNoticeItemService);
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
        Specification<SupplyNoticeItemEntity> specification = (root, query, cb) -> {
            Predicate p01 = CustomRestrictions.gte("supplyNotice.createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p02 = CustomRestrictions.lt("supplyNotice.createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p03 = CustomRestrictions.gt("surplusAmount", 0, true).toPredicate(root, query, cb);
            Predicate p04 = CustomRestrictions.eq("supplyNotice.status", 3, true).toPredicate(root, query, cb);
            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p01, p02, p03, p04);


            //单据编号
            if (StringUtils.isNotBlank(this.getDocNo())) {
                Predicate p05 = CustomRestrictions.like("supplyNotice.srcDocNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p05);
            }

            //料品
            if (StringUtils.isNotBlank(this.supplyNoticeItem.getPartCode())) {
                Predicate p07 = CustomRestrictions.like("partCode", this.supplyNoticeItem.getPartCode().trim(), true).toPredicate(root, query, cb);
                Predicate p08 = CustomRestrictions.like("partName", this.supplyNoticeItem.getPartCode().trim(), true).toPredicate(root, query, cb);
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
            SupplyNoticeItemEntity supplyNoticeItem = (SupplyNoticeItemEntity) Item;
            if (StringUtils.isNotBlank(supplyNoticeItem.getAgencyCode()) && !supplyNoticeItem.isSecondaryDistribution() && supplyNoticeItem.getDistributionAmount() > 0) {
                SupplyWaitingItemEntity supplyWaitingItem = new SupplyWaitingItemEntity();
                supplyWaitingItem.setAgencyName(supplyNoticeItem.getAgencyName());
                supplyWaitingItem.setAgencyCode(supplyNoticeItem.getAgencyCode());
                supplyWaitingItem.setPartCode(supplyNoticeItem.getPartCode());
                supplyWaitingItem.setPartName(supplyNoticeItem.getPartName());
                supplyWaitingItem.setRequestAmount(supplyNoticeItem.getDistributionAmount());
                //supplyWaitingItem.setSentAmount(supplyNoticeItem.getSentAmount());
                supplyWaitingItem.setSurplusAmount(supplyNoticeItem.getDistributionAmount());
                supplyWaitingItem.setArrivalTime(supplyNoticeItem.getArrivalTime());
                supplyWaitingItem.setComment(supplyNoticeItem.getComment());
                supplyWaitingItem.setSupplyNoticeItem(supplyNoticeItem);
                supplyWaitingItem.setEnabled(true);
                supplyWaitingItem.setCreatedTime(new Date());
                supplyWaitingItem.setCreaterName(CommonHelper.getActiveUser().getUsername());
                supplyWaitingItem.setCreaterId(CommonHelper.getActiveUser().getUserId());
                supplyWaitingItem.setDealerCode(supplyNoticeItem.getSupplyNotice().getDealerCode());
                supplyWaitingItem.setDealerName(supplyNoticeItem.getSupplyNotice().getDealerName());
                supplyWaitingItem.setServiceManager(supplyNoticeItem.getSupplyNotice().getServiceManager());
                supplyWaitingItemService.getRepository().save(supplyWaitingItem);
            }

            if (!StringUtils.isNotBlank(supplyNoticeItem.getAgencyCode()) && supplyNoticeItem.isSecondaryDistribution() && supplyNoticeItem.getDistributionAmount() > 0) {
                SupplyDisItemEntity supplyDisItem = new SupplyDisItemEntity();
                supplyDisItem.setAgencyName(supplyNoticeItem.getAgencyName());
                supplyDisItem.setAgencyCode(supplyNoticeItem.getAgencyCode());
                supplyDisItem.setPartCode(supplyNoticeItem.getPartCode());
                supplyDisItem.setPartName(supplyNoticeItem.getPartName());
                supplyDisItem.setRequestAmount(supplyNoticeItem.getDistributionAmount());
                //supplyDisItem.setSentAmount(supplyNoticeItem.getSentAmount());
                supplyDisItem.setSurplusAmount(supplyNoticeItem.getDistributionAmount());
                supplyDisItem.setArrivalTime(supplyNoticeItem.getArrivalTime());
                supplyDisItem.setComment(supplyNoticeItem.getComment());
                supplyDisItem.setSupplyNoticeItem(supplyNoticeItem);
                supplyDisItem.setEnabled(true);
                supplyDisItem.setCreatedTime(new Date());
                supplyDisItem.setCreaterName(CommonHelper.getActiveUser().getUsername());
                supplyDisItem.setCreaterId(CommonHelper.getActiveUser().getUserId());
                supplyDisItemService.getRepository().save(supplyDisItem);
            }
            if (StringUtils.isNotBlank(supplyNoticeItem.getAgencyCode()) || supplyNoticeItem.isSecondaryDistribution() && supplyNoticeItem.getDistributionAmount() > 0) {
                supplyNoticeItem.setModifiedTime(new Date());
                supplyNoticeItem.setSurplusAmount(supplyNoticeItem.getSurplusAmount() - supplyNoticeItem.getDistributionAmount());
                supplyNoticeItem.setSentAmount(supplyNoticeItem.getRequestAmount() - supplyNoticeItem.getSurplusAmount());
                supplyNoticeItem.setDistributionAmount(0.0);
                supplyNoticeItemService.getRepository().save(supplyNoticeItem);
            }
        }
        filterList();
    }

    @Command
    public void selectSupplyNoticeItem(@BindingParam("model") SupplyNoticeItemEntity item) {
        if (item != null) {
            currentsupplyNoticeItem = item;
        }
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void secondAllocation(@BindingParam("model") SupplyNoticeItemEntity item) {
        if (item != null) {
            currentsupplyNoticeItem = item;
            if (item.isSecondaryDistribution()) {
                currentsupplyNoticeItem.setAgencyName(null);
                currentsupplyNoticeItem.setAgencyCode(null);
                currentsupplyNoticeItem.setDistributionAmount(currentsupplyNoticeItem.getSurplusAmount());
                this.setKeyword("");
            } else {
                currentsupplyNoticeItem.setDistributionAmount(0);
            }
        }
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void clearSelectedAgencyByItem() {
        currentsupplyNoticeItem.setAgencyName(null);
        currentsupplyNoticeItem.setAgencyCode(null);
        currentsupplyNoticeItem.setDistributionAmount(0);
        this.setKeyword("");
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void sentAmountChange(@BindingParam("model") SupplyNoticeItemEntity item) {
        if (item != null) {
            currentsupplyNoticeItem = item;
            if (currentsupplyNoticeItem.getSurplusAmount() < currentsupplyNoticeItem.getDistributionAmount()) {
                ZkUtils.showInformation("本次分配数不能答应可分配数量", "提示");
                currentsupplyNoticeItem.setDistributionAmount(0);
            }
        }
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void selectAgencyByItem(@BindingParam("model") AgencyEntity agency) {
        currentsupplyNoticeItem.setAgencyName(agency.getName());
        currentsupplyNoticeItem.setAgencyCode(agency.getCode());
        currentsupplyNoticeItem.setSecondaryDistribution(false);
        currentsupplyNoticeItem.setDistributionAmount(currentsupplyNoticeItem.getSurplusAmount());
        this.setKeyword("");
    }

    public SupplyNoticeItemEntity getSupplyNoticeItem() {
        return supplyNoticeItem;
    }

    public void setSupplyNoticeItem(SupplyNoticeItemEntity supplyNoticeItem) {
        this.supplyNoticeItem = supplyNoticeItem;
    }
}
