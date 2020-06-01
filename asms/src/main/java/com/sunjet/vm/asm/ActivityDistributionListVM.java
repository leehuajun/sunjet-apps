package com.sunjet.vm.asm;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.ActivityDistributionEntity;
import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityVehicleEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.ActivityDistributionService;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.service.asm.ActivityVehicleService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ActivityDistributionListVM extends FlowListBaseVM {

    @WireVariable
    private ActivityDistributionService activityDistributionService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;

    @WireVariable
    UserService userService;

    private ActivityNoticeEntity noticeEntity = new ActivityNoticeEntity(); //活动车辆

    private String serviceManager;  // 服务经理

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        this.setBaseService(activityDistributionService);
        this.setFormUrl("/views/asm/activity_distribution_form.zul");
        this.setEnableDelete(hasPermission(ActivityDistributionEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(ActivityDistributionEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(ActivityDistributionEntity.class.getSimpleName() + ":modify"));
        //if(CommonHelper.getActiveUser().getDealer()==null){
        UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
        Set<RoleEntity> roles = userEntity.getRoles();
        //boolean Permissions = false;
        for (RoleEntity role : roles) {
            if (!role.getName().equals("服务经理")) {
                continue;
                //Permissions = true;
            }
            this.setServiceManager(CommonHelper.getActiveUser().getUsername());
        }
        //if (Permissions) {
        //
        //}
        //}

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
        Specification<ActivityDistributionEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p07 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            Predicate p = cb.and(p04, p05, p07);
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
            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }
            //服务经理
            if (StringUtils.isNotBlank(this.getServiceManager())) {
                //if(CommonHelper.getActiveUser().getUsername().equals()){
                Predicate p08 = CustomRestrictions.eq("serviceManager", CommonHelper.getActiveUser().getUsername(), true).toPredicate(root, query, cb);
                p = cb.and(p, p08);
                //}

            }


            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange("resultDTO")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        ActivityDistributionEntity activityDistributionRequest = activityDistributionService.findOneWithVehicles(flowEntity.getObjId());
        if (activityDistributionRequest.getSupplyNoticeId() != null) {
            ZkUtils.showInformation("已经生成调拨单不能删除！", "提示");
            return;
        } else if (activityDistributionRequest.getStatus().equals(0)) {
            //保存活动分配单的VIN码
            Map<String, ActivityVehicleEntity> oldDistributionVIN = new HashMap<>();
            for (ActivityVehicleEntity ave : activityDistributionRequest.getActivityVehicles()) {
                oldDistributionVIN.put(ave.getVehicle().getVin(), ave);
            }
            this.noticeEntity = activityNoticeService.findOneWithVehicles(activityDistributionRequest.getActivityNotice().getObjId());
            //遍历活动通知单,如果有活动分配单有车辆设置为已分配状态
            for (ActivityVehicleEntity ve : this.noticeEntity.getActivityVehicles()) { //获取活动通知车辆
                if (oldDistributionVIN.get(ve.getVehicle().getVin()) != null) {
                    ve.setDistribute(false);  // 设置车辆为已分配状态
                    activityVehicleService.getRepository().save(ve);
                }
            }


            activityDistributionService.getRepository().delete(activityDistributionRequest.getObjId());
            initList();
        } else {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
        }


    }

    public ActivityNoticeEntity getNoticeEntity() {
        return noticeEntity;
    }

    public void setNoticeEntity(ActivityNoticeEntity noticeEntity) {
        this.noticeEntity = noticeEntity;
    }

    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }
}


