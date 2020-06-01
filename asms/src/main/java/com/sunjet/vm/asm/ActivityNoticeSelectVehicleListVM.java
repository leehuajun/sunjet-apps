package com.sunjet.vm.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
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
import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ActivityNoticeSelectVehicleListVM extends ListBaseVM {
    //private String mc;
    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private List<String> selectedVehicleList = new ArrayList<>();
    private List<VehicleEntity> vehicleList = new ArrayList<>();  //推送车辆列表

    private VehicleEntity vehicleEntity = new VehicleEntity();
    private Boolean choice;

    private Date purchaseDateStart;
    private Date purchaseDateEnd;
    private String province;

    public Boolean getChoice() {
        return choice;
    }

    public void setChoice(Boolean choice) {
        this.choice = choice;
    }

    //    @Init(superclass = true)
    @Init
    public void init() {
        selectedVehicleList = (List<String>) Executions.getCurrent().getArg().get("vins");
        vehicleList = (List<VehicleEntity>) Executions.getCurrent().getArg().get("vehcles");
        this.setEnableAdd(false);
        this.setBaseService(vehicleService);
        this.setFormUrl("/views/basic/vehicle_form.zul");
        this.setStartDate(null);
        this.setEndDate(null);

        if (selectedVehicleList != null && selectedVehicleList.size() != 0) {
            this.choice = true;
        }
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     *
     * @param searchDTO
     */
    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        //如果查询排序条件不为空，则把该 查询排序列表 赋给 searchDTO查询对象
        searchDTO.setSearchOrderList(Arrays.asList(new SearchOrder("createdTime", SearchOrder.OrderType.ASC, 1)
                // new SearchOrder("name",SearchOrder.OrderType.ASC,2)
        ));
    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.vehicleEntity = new VehicleEntity();
        this.setStartDate(null);
        this.setEndDate(null);
        this.setPurchaseDateStart(null);
        this.setPurchaseDateEnd(null);

    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     *
     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<VehicleEntity> specification = (root, query, cb) -> {
            Predicate p = null;

            // vin
            if (StringUtils.isNotBlank(this.vehicleEntity.getVin())) {
                p = CustomRestrictions.like("vin", this.vehicleEntity.getVin().trim(), true).toPredicate(root, query, cb);
//                p = cb.and(p,p01);
            }
            // vsn
            if (StringUtils.isNotBlank(this.vehicleEntity.getVsn())) {
                if (p == null) {
                    p = CustomRestrictions.like("vsn", this.vehicleEntity.getVsn().trim(), true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("vsn", this.vehicleEntity.getVsn().trim(), true).toPredicate(root, query, cb));
                }
            }
            // 车辆型号
            if (StringUtils.isNotBlank(this.vehicleEntity.getVehicleModel())) {
                if (p == null) {
                    p = CustomRestrictions.like("vehicleModel", this.vehicleEntity.getVehicleModel().trim(), true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("vehicleModel", this.vehicleEntity.getVehicleModel().trim(), true).toPredicate(root, query, cb));
                }
            }

            // 销售商
            if (StringUtils.isNotBlank(this.vehicleEntity.getSeller())) {
                if (p == null) {
                    p = CustomRestrictions.like("seller", this.vehicleEntity.getSeller().trim(), true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("seller", this.vehicleEntity.getSeller().trim(), true).toPredicate(root, query, cb));
                }
            }
            //生产日期
            if (this.getStartDate() != null && this.getEndDate() != null) {
                Predicate p04 = CustomRestrictions.gte("manufactureDate", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
                Predicate p05 = CustomRestrictions.lt("manufactureDate", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);

                logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
                logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

                p = cb.and(p, p04, p05);
            }
            //购买日期
            if (this.getPurchaseDateStart() != null && this.getPurchaseDateEnd() != null) {
                Predicate p06 = CustomRestrictions.gte("purchaseDate", DateHelper.getStartDate(this.getPurchaseDateStart()), true).toPredicate(root, query, cb);
                Predicate p07 = CustomRestrictions.lt("purchaseDate", DateHelper.getEndDate(this.getPurchaseDateEnd()), true).toPredicate(root, query, cb);


                p = cb.and(p, p06, p07);
            }
            //省份
            if (this.getProvince() != null) {
                if (p == null) {
                    p = CustomRestrictions.like("province.name", this.getProvince(), true).toPredicate(root, query, cb);
                } else {
                    p = CustomRestrictions.like("province.name", this.getProvince(), true).toPredicate(root, query, cb);
                    p = cb.and(p);
                }
            }


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
            String vin = ((VehicleEntity) de).getVin();
            if (this.selectedVehicleList.contains(vin)) {
                this.choice = true;
                return;
            }
        }

        this.choice = false;

    }

    @Command
    @NotifyChange({"resultDTO", "choice"})
    public void checkAll(@BindingParam("chk") Boolean chk) {   //全选
        List<DocEntity> pageContent = this.getResultDTO().getPageContent();
        if (chk == true) {

            for (DocEntity de : pageContent) {
                VehicleEntity vehicle = ((VehicleEntity) de);
                if (!this.selectedVehicleList.contains(vehicle.getVin())) {
                    this.selectedVehicleList.add(vehicle.getVin());
                    this.vehicleList.add(vehicle);
                }
            }
        } else {
            if (pageContent != null) {
                for (DocEntity de : pageContent) {
                    VehicleEntity vehicle = ((VehicleEntity) de);
                    if (this.selectedVehicleList.contains(vehicle.getVin())) {
                        this.selectedVehicleList.remove(vehicle.getVin());
                        this.vehicleList.remove(vehicle);
                    }
                }
            }
        }
        this.choice = chk;
    }

    @Command
    @NotifyChange("*")
    public void selectVehicle(@BindingParam("entity") VehicleEntity entity) {
        if (this.selectedVehicleList.contains(entity.getVin())) {
            //ZkUtils.showInformation("当前车辆已在选择列表之中，无需重复增加！","提示");
            unSelectVehicle(entity);
        }else {
            this.selectedVehicleList.add(entity.getVin());
            this.vehicleList.add(entity);
        }

    }

    @Command
    @NotifyChange("*")
    public void unSelectVehicle(@BindingParam("entity") VehicleEntity entity) {
        if (this.selectedVehicleList.contains(entity.getVin())) {
            this.selectedVehicleList.remove(entity.getVin());

            //迭代器remove车辆
            Iterator<VehicleEntity> iterator = this.vehicleList.iterator();
            while (iterator.hasNext()){
                VehicleEntity vehicleEntity = iterator.next();
                if (vehicleEntity.getObjId().equals(entity.getObjId())){
                    iterator.remove();
                }
            }

        }
    }

    public Boolean chkIsExist(VehicleEntity vehicle) {

        if (this.selectedVehicleList.contains(vehicle.getVin())) {
            return true;
        } else {
            return false;
        }


    }

    /**
     * 计算每页显示的记录数
     *
     * @return
     */
    @Override
    protected Integer calculatePageSize() {
        return 12;
    }

    @Command
    public void submit() {
        Map<String, Object> vins = new HashMap<>();
//        vins.put("vins", this.getSelectedVehicleList());
        vins.put("vehicleList", this.vehicleList);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.ACTIVITY_NOTICE_SELECT_VEHICLE, vins);
    }

    public List<String> getSelectedVehicleList() {
        return selectedVehicleList;
    }

    public void setSelectedVehicleList(List<String> selectedVehicleList) {
        this.selectedVehicleList = selectedVehicleList;
    }

    public VehicleEntity getVehicleEntity() {
        return vehicleEntity;
    }

    public void setVehicleEntity(VehicleEntity vehicleEntity) {
        this.vehicleEntity = vehicleEntity;
    }

    public Date getPurchaseDateStart() {
        return purchaseDateStart;
    }

    public void setPurchaseDateStart(Date purchaseDateStart) {
        this.purchaseDateStart = purchaseDateStart;
    }

    public Date getPurchaseDateEnd() {
        return purchaseDateEnd;
    }

    public void setPurchaseDateEnd(Date purchaseDateEnd) {
        this.purchaseDateEnd = purchaseDateEnd;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
