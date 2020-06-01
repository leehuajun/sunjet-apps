package com.sunjet.vm.asm;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.admin.UserService;
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
import java.util.Set;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 三包服务单 列表 VM
 */
public class WarrantyMaintenanceListVM extends FlowListBaseVM {
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;

    @WireVariable
    private UserService userService;

    private String creator = "";
    private String serviceManager;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        this.setFormUrl("/views/asm/warranty_maintenance_form.zul");
        this.setBaseService(warrantyMaintenanceService);
        if (CommonHelper.getActiveUser().getDealer() == null) {
            UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
            Set<RoleEntity> roles = userEntity.getRoles();
            boolean Permissions = false;
            for (RoleEntity role : roles) {
                if (role.getName().equals("服务经理")) {
                    Permissions = true;
                }
            }
            if (Permissions) {
                this.setServiceManager(CommonHelper.getActiveUser().getUsername());

            }
        }

        this.setEnableDelete(hasPermission(WarrantyMaintenanceEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(WarrantyMaintenanceEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(WarrantyMaintenanceEntity.class.getSimpleName() + ":modify"));
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }

    @Command
    @Override
    @NotifyChange("*")
    public void reset() {
        super.reset();
        this.serviceManager = "";
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
        Specification<WarrantyMaintenanceEntity> specification = (root, query, cb) -> {
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
                Predicate p06 = CustomRestrictions.like("docNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            //服务经理
            if (StringUtils.isNotBlank(this.getServiceManager())) {
                Predicate p07 = CustomRestrictions.like("serviceManager", this.getServiceManager().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p07);
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

    @Command
    @Override
    @NotifyChange("*")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        //WarrantyMaintenanceEntity warrantyMaintenanceEntity = (WarrantyMaintenanceEntity) flowEntity;
        WarrantyMaintenanceEntity warrantyMaintenanceEntity = warrantyMaintenanceService.findOneWithOthersById(flowEntity.getObjId());
        if (warrantyMaintenanceEntity.getSupplyNoticeId() != null) {
            ZkUtils.showInformation("已生成调拨单不能删除", "提示");
            return;
        } else {
            super.deleteFlowEntity(warrantyMaintenanceEntity);
        }

    }

    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }
}
