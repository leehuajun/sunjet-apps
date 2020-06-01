package com.sunjet.vm.basic;

import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.vm.base.ListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * 车辆 列表
 * Created by Administrator on 2016/9/5.
 */
public class VehicleListVM extends ListBaseVM {
    @WireVariable
    private VehicleService vehicleService;
    private String vin;             // 车辆VIN
    private String vsn;             // 车辆VSN
    private String vehicleModel;    // 车型型号
    private String seller;          // 经销商


    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVsn() {
        return vsn;
    }

    public void setVsn(String vsn) {
        this.vsn = vsn;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Init
    public void Init() {
        this.setBaseService(vehicleService);
        this.setFormUrl("/views/basic/vehicle_form.zul");
        initList();
    }

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
        searchDTO.setSearchOrderList(Arrays.asList(new SearchOrder("createdTime", SearchOrder.OrderType.ASC, 1)
                // new SearchOrder("name",SearchOrder.OrderType.ASC,2)
        ));
    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.vin = "";
        this.vsn = "";
        this.seller = "";
        this.vehicleModel = "";
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求

     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<VehicleEntity> specification = (root, query, cb) -> {
            Predicate p = null;
            // vin
            if (StringUtils.isNotBlank(this.vin)) {
                p = CustomRestrictions.like("vin", this.vin, true).toPredicate(root, query, cb);
//                p = cb.and(p,p01);
            }
            // vsn
            if (StringUtils.isNotBlank(this.vsn)) {
                if (p == null) {
                    p = CustomRestrictions.like("vsn", this.vsn, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("vsn", this.vsn, true).toPredicate(root, query, cb));
                }
            }
            // 车辆型号
            if (StringUtils.isNotBlank(this.vehicleModel)) {
                if (p == null) {
                    p = CustomRestrictions.like("vehicleModel", this.vehicleModel, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("vehicleModel", this.vehicleModel, true).toPredicate(root, query, cb));
                }
            }

            // 车辆型号
            if (StringUtils.isNotBlank(this.seller)) {
                if (p == null) {
                    p = CustomRestrictions.like("seller", this.seller, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("seller", this.seller, true).toPredicate(root, query, cb));
                }
            }

//        if(!this.selectedProvince.getName().equalsIgnoreCase("全部")){
//
//        }

            return p;
        };
        searchDTO.setSpecification(specification);
    }
}
