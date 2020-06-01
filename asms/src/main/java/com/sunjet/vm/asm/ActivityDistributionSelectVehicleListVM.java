package com.sunjet.vm.asm;

import com.sunjet.model.asm.ActivityVehicleEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.asm.ActivityVehicleService;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.ListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ActivityDistributionSelectVehicleListVM extends ListBaseVM {
    //private String mc;
    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private ActivityVehicleService activityVehicleService;


    private Map<String,VehicleEntity> selectedVehicleList = new HashMap<>();
    private String activityNoticeId = "";
    private VehicleEntity vehicleEntity = new VehicleEntity();

    private List<VehicleEntity> vehicleList = new ArrayList<>();  //推送车辆列表
    private Boolean choice;

    //    @Init(superclass = true)
    @Init
    public void init() {
        //selectedVehicleList = (List<String>) Executions.getCurrent().getArg().get("vins");
        activityNoticeId = (String) Executions.getCurrent().getArg().get("activityNoticeId");
        this.vehicleList = (List<VehicleEntity>) Executions.getCurrent().getArg().get("vehcles");
        this.setStartDate(null);
        this.setEndDate(null);
        this.setEnableAdd(false);
        this.setBaseService(activityVehicleService);
//        this.setFormUrl("/views/basic/vehicle_form.zul");
        for (VehicleEntity vehic : vehicleList) {
            selectedVehicleList.put(vehic.getObjId(),vehic);
        }
        if (selectedVehicleList != null && selectedVehicleList.size() != 0) {
            this.choice = true;
        }

        initList();
//        logger.info(Executions.getCurrent().getArg().get("id").toString());
//        logger.info(Executions.getCurrent().getArg().get("name").toString());
    }

    //    @Command
//    @NotifyChange("*")
//    public void searchVehicle() {
//        if (this.keyword.trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
//            this.vehicleEntities = vehicleService.findAllByStatusAndKeyword(this.keyword);
//        } else {
//            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
//        }
//    }
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求

     * @param searchDTO
     */
    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        //如果查询排序条件不为空，则把该 查询排序列表 赋给 searchDTO查询对象
        //searchDTO.setSearchOrderList(Arrays.asList(new SearchOrder("createdTime", SearchOrder.OrderType.ASC, 1)
        //        // new SearchOrder("name",SearchOrder.OrderType.ASC,2)
        //));
    }

    @Command
    @NotifyChange("vehicleEntity")
    public void reset() {
        this.vehicleEntity = new VehicleEntity();
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求

     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<ActivityVehicleEntity> specification = (root, query, cb) -> {

            Predicate p = CustomRestrictions.like("activityNotice.objId", activityNoticeId, true).toPredicate(root, query, cb);

            p = cb.and(p, CustomRestrictions.eq("distribute", false, true).toPredicate(root, query, cb));


            if (StringUtils.isNotBlank(this.vehicleEntity.getVin())) {
                p = cb.and(p, CustomRestrictions.like("vehicle.vin", this.vehicleEntity.getVin().trim(), true).toPredicate(root, query, cb));
            }
            // vsn
            if (StringUtils.isNotBlank(this.vehicleEntity.getVsn())) {
                p = cb.and(p, CustomRestrictions.like("vehicle.vsn", this.vehicleEntity.getVsn().trim(), true).toPredicate(root, query, cb));
            }
            // 车辆型号
            if (StringUtils.isNotBlank(this.vehicleEntity.getVehicleModel())) {
                p = cb.and(p, CustomRestrictions.like("vehicle.vehicleModel", this.vehicleEntity.getVehicleModel().trim(), true).toPredicate(root, query, cb));
            }

            // 车辆型号
            if (StringUtils.isNotBlank(this.vehicleEntity.getSeller())) {
                p = cb.and(p, CustomRestrictions.like("vehicle.seller", this.vehicleEntity.getSeller().trim(), true).toPredicate(root, query, cb));
            }

//        if(!this.selectedProvince.getName().equalsIgnoreCase("全部")){
//
//        }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange({"resultDTO", "choice"})
    public void gotoPageNo(@BindingParam("e") Event event) {       //判断下一页是否勾选
        super.gotoPageNo(event);
        List<DocEntity> pageContent = this.getResultDTO().getPageContent();
        for (DocEntity de : pageContent) {
            VehicleEntity vin = ((ActivityVehicleEntity) de).getVehicle();
            if (this.selectedVehicleList.get(vin.getObjId())!=null) {
                this.choice = true;
                return;
            }
        }

        this.choice = false;

    }

    @Command
    @NotifyChange("*")
    public void selectVehicle(@BindingParam("entity") ActivityVehicleEntity av) {
        if (this.selectedVehicleList.get(av.getVehicle().getObjId())!=null) {
            //ZkUtils.showInformation("当前车辆已在选择列表之中，无需重复增加！","提示");
            unSelectVehicle(av.getVehicle());
            return;
        }
        this.selectedVehicleList.put(av.getVehicle().getObjId(),av.getVehicle());
        //this.vehicleList.add(av.getVehicle());
    }

    @Command
    @NotifyChange("*")
    public void unSelectVehicle(@BindingParam("entity") VehicleEntity entity) {
        if (this.selectedVehicleList.get(entity.getObjId())!=null) {
            this.selectedVehicleList.remove(entity.getVin());
            //this.vehicleList.remove(entity);
        }
    }

    public Boolean chkIsExist(ActivityVehicleEntity av) {
        if (this.selectedVehicleList.get(av.getVehicle().getObjId())!=null) {
            return true;
        } else {
            return false;
        }
    }

    @Command
    @NotifyChange({"resultDTO", "choice"})
    public void checkAll(@BindingParam("chk") Boolean chk) {
//        this.selectedVehicleList.clear();
        List<DocEntity> pageContent = this.getResultDTO().getPageContent();
        if (chk == true) {
            for (DocEntity de : pageContent) {
                VehicleEntity vehicle = ((ActivityVehicleEntity) de).getVehicle();
                if (this.selectedVehicleList.get(vehicle.getObjId())==null) {
                    this.selectedVehicleList.put(vehicle.getObjId(),vehicle);
                    //this.vehicleList.add(vehicle);
                }
//                this.selectedVehicleList.add(vehicle.getVin());
            }
        } else {
            if (pageContent != null) {
                for (DocEntity de : pageContent) {
                    VehicleEntity vehicle = ((ActivityVehicleEntity) de).getVehicle();
                    if (this.selectedVehicleList.get(vehicle.getObjId()) !=null) {
                        this.selectedVehicleList.remove(vehicle.getObjId());
                        //this.vehicleList.remove(vehicle);
                    }
                }
            }
        }
        this.choice = chk;
    }


    @Command
    public void submit() {
        Map<String, Object> vins = new HashMap<>();
        this.vehicleList.clear();
        for (String VehicleId : this.selectedVehicleList.keySet()) {
            this.vehicleList.add(this.selectedVehicleList.get(VehicleId));
        }



        //vins.put("vins", this.selectedVehicleList);
        vins.put("vehicleList", this.getVehicleList());
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.ACTIVITY_DISTRIBUTION_SELECT_VEHICLE, vins);
    }



    public VehicleEntity getVehicleEntity() {
        return vehicleEntity;
    }

    public void setVehicleEntity(VehicleEntity vehicleEntity) {
        this.vehicleEntity = vehicleEntity;
    }

    public Boolean getChoice() {
        return choice;
    }

    public void setChoice(Boolean choice) {
        this.choice = choice;
    }

    public List<VehicleEntity> getVehicleList() {
        return vehicleList;
    }
}
