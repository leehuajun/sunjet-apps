package com.sunjet.vm.supply;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.SupplyNoticeItemEntity;
import com.sunjet.model.asm.SupplyWaitingItemEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.SupplyNoticeItemService;
import com.sunjet.service.asm.SupplyWaitingItemService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 待发货清单
 * Created by zyh on 2016/11/14.
 */
public class SupplyWaitingListVM extends FlowListBaseVM {

    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private SupplyNoticeItemService supplyNoticeItemService;

    @WireVariable
    private UserService userService;
    private SupplyWaitingItemEntity supplyWaitingItem = new SupplyWaitingItemEntity();
    private List<SupplyWaitingItemEntity> supplyWaitingItems = new ArrayList<SupplyWaitingItemEntity>();
    private AgencyEntity agency;
    private List<AgencyEntity> agencies = new ArrayList<>();

    private boolean readonly;

    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableSaveSupply(true);
        if ("wuling".equals(CommonHelper.getActiveUser().getUserType())) {
            UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
            Set<RoleEntity> roles = userEntity.getRoles();
            for (RoleEntity role : roles) {
                if ("二次分配专员".equals(role.getName()) || "配件分配专员".equals(role.getName())) {
                    this.setReadonly(true);
                }
            }

        } else {
            System.out.println(CommonHelper.getActiveUser().getUserType());
            if ("agency".equals(CommonHelper.getActiveUser().getUserType())) {
                this.setReadonly(true);
            }
        }
        this.setBaseService(supplyWaitingItemService);
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
        Specification<SupplyWaitingItemEntity> specification = (root, query, cb) -> {
            Predicate p01 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p02 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p03 = CustomRestrictions.gt("surplusAmount", 0, true).toPredicate(root, query, cb);


            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p01, p02, p03);

            //合作商
            if (agency != null && StringUtils.isNotBlank(agency.getCode())) {
                Predicate p06 = CustomRestrictions.like("agencyCode", agency.getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            //服务站
            if (CommonHelper.getActiveUser().getDealer() != null) {
                Predicate p04 = CustomRestrictions.eq("dealerCode", CommonHelper.getActiveUser().getDealer().getCode(), true).toPredicate(root, query, cb);
                p = cb.and(p, p04);
            } else {
                // 服务站编号
                if (this.getDealer() != null) {
                    Predicate pp = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                    p = cb.and(p, pp);
                }
            }


            //判断权限
            UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
            Set<RoleEntity> roles = userEntity.getRoles();
            boolean Permissions = false;
            for (RoleEntity role : roles) {
                if ("服务经理".equals(role.getName())) {
                    Permissions = true;
                }
            }
            //服务经理
            if (Permissions) {
                Predicate p04 = CustomRestrictions.like("serviceManager", CommonHelper.getActiveUser().getUsername(), true).toPredicate(root, query, cb);
                p = cb.and(p, p04);
            }
            //料品
            if (StringUtils.isNotBlank(this.supplyWaitingItem.getPartCode())) {
                Predicate p07 = CustomRestrictions.like("partCode", this.supplyWaitingItem.getPartCode().trim(), true).toPredicate(root, query, cb);
                Predicate p08 = CustomRestrictions.like("partName", this.supplyWaitingItem.getPartCode().trim(), true).toPredicate(root, query, cb);
                Predicate item = cb.or(p07, p08);
                p = cb.and(p, item);
            }


            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Command
//    @NotifyChange("*")
    public void selectSupplyWaiting(@BindingParam("model") SupplyWaitingItemEntity supplyWaitingItem, @BindingParam("check") boolean check) {
        boolean result = true;
        if (check == result && supplyWaitingItem != null) {
            if (this.supplyWaitingItems.size() > 0) {
                for (SupplyWaitingItemEntity supplyWaitingItemEntity : this.supplyWaitingItems) {
                    if (!supplyWaitingItemEntity.getSupplyNoticeItem().getSupplyNotice().getDealerCode().equals(supplyWaitingItem.getSupplyNoticeItem().getSupplyNotice().getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个服务站的单据", "提示");
                        return;
                    }
                }
                this.supplyWaitingItems.add(supplyWaitingItem);
            } else {
                this.supplyWaitingItems.add(supplyWaitingItem);
            }

        } else {
            this.supplyWaitingItems.remove(supplyWaitingItem);
        }
    }

    @Command
    public void createSupply() {
        if (supplyWaitingItems.isEmpty()) {
            ZkUtils.showInformation("没有选择物料！", "提示");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("supplyWaitingItems", supplyWaitingItems);
            Window window = (Window) ZkUtils.createComponents("/views/asm/supply_form.zul", null, map);
            window.doModal();
        }

        supplyWaitingItems.clear();
    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void returnSupply(@BindingParam("entity") SupplyWaitingItemEntity Entity) {
        SupplyWaitingItemEntity supplyWaitingItem = supplyWaitingItemService.findSupplyWaitingItemById(Entity.getObjId());
        if (supplyWaitingItem != null) {
            if (supplyWaitingItem.getSentAmount() > 0) {
                ZkUtils.showInformation("此待供货请求已开始供货，不能退回！", "提示");
                return;
            }
            if (supplyWaitingItem.getSupplyNoticeItem() != null) {
                SupplyNoticeItemEntity supplyNoticeItem = supplyWaitingItem.getSupplyNoticeItem();
                supplyNoticeItem.setSurplusAmount(supplyNoticeItem.getSurplusAmount() + supplyWaitingItem.getRequestAmount());
                supplyNoticeItem.setSentAmount(supplyNoticeItem.getRequestAmount() - supplyNoticeItem.getSurplusAmount());
                supplyNoticeItem.setModifiedTime(new Date());
                supplyNoticeItem.setModifierName(CommonHelper.getActiveUser().getUsername());
                supplyNoticeItem.setModifierId(CommonHelper.getActiveUser().getUserId());
                supplyNoticeItemService.getRepository().save(supplyNoticeItem);
            }
            supplyWaitingItemService.getRepository().delete(supplyWaitingItem.getObjId());
            initList();
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_LIST, null);
        }
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgencies(@BindingParam("model") String keyword) {
        if (CommonHelper.getActiveUser().getAgency() != null) {   // 合作库用户
            this.agencies.clear();
            this.agencies.add(CommonHelper.getActiveUser().getAgency());
        } else if (CommonHelper.getActiveUser().getDealer() != null) {  // 服务站用户
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword);
        }
    }

    @Command
    @NotifyChange("agency")
    public void clearSelectedAgency() {
        if (CommonHelper.getActiveUser().getAgency() != null) {
            this.agency = CommonHelper.getActiveUser().getAgency();
        } else {
            this.agency = null;
        }
        this.setKeyword("");

    }

    @Command
    @NotifyChange({"agency", "agencySettlement"})
    public void selectAgency(@BindingParam("model") AgencyEntity agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agency = agency;

    }

    @GlobalCommand(GlobalCommandValues.REFRESH_SUPPLY_WAITING)
    @NotifyChange("*")
    private void callback_supply_waiting() {
        initList();
        supplyWaitingItems.clear();
    }


    public SupplyWaitingItemEntity getSupplyWaitingItem() {
        return supplyWaitingItem;
    }

    public void setSupplyWaitingItem(SupplyWaitingItemEntity supplyWaitingItem) {
        this.supplyWaitingItem = supplyWaitingItem;
    }


    @Override
    public AgencyEntity getAgency() {
        return agency;
    }

    @Override
    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    @Override
    public List<AgencyEntity> getAgencies() {
        return agencies;
    }

    @Override
    public void setAgencies(List<AgencyEntity> agencies) {
        this.agencies = agencies;
    }


    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
}
