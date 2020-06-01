package com.sunjet.vm.basic;

import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.ListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 合作商 列表
 * Created by Administrator on 2016/9/5.
 */
public class AgencyListVM extends ListBaseVM {

    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private RegionService regionService;

    private List<ProvinceEntity> provinceEntities = new ArrayList<>();  // 省份/直辖市 集合
    private List<CityEntity> cityEntities = new ArrayList<>();          // 选中的省份/直辖市的下属城市集合
    private List<CountyEntity> countyEntities = new ArrayList<>();      // 选中的城市的下属县/区集合

    private ProvinceEntity selectedProvince;        // 选中的 省份/直辖市
    private CityEntity selectedCity;                // 选中的 城市
    private CountyEntity selectedCounty;            // 选中的 县/区

    private String agencyCode = StringUtils.EMPTY;
    private String agencyName = StringUtils.EMPTY;

    @Init
    public void Init() {
        this.setBaseService(agencyService);
        this.setFormUrl("/views/basic/agency_form.zul");
        provinceEntities = regionService.findAllProvince();
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange("*")
    public void selectProvince() {
        if (selectedProvince != null) {
            this.selectedCity = null;
            this.selectedCounty = null;
            this.cityEntities = regionService.findCitiesByProvinceId(this.selectedProvince.getObjId());
        }
    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.agencyCode = StringUtils.EMPTY;
        this.agencyName = StringUtils.EMPTY;
        this.selectedCity = null;
        this.selectedProvince = null;
        this.cityEntities = new ArrayList<>();
    }

//    @Command
//    @NotifyChange("*")
//    public void selectCity() {
//        if (selectedCity != null) {
//            this.selectedCounty = null;
////            this.countyEntities = regionService.findCountiesByCityId(this.selectedCity.getObjId());
//        }
//    }

    //更新合作商列表
    @GlobalCommand(GlobalCommandValues.REFRESH_AGENCY_LIST)
    @NotifyChange("*")
    public void refreshPartList() {
        initList();
    }

    protected void configSearchOrder(SearchDTO searchDTO) {
        //如果查询排序条件不为空，则把该 查询排序列表 赋给 searchDTO查询对象
        searchDTO.setSearchOrderList(Arrays.asList(new SearchOrder("province", SearchOrder.OrderType.ASC, 1)
                // new SearchOrder("name",SearchOrder.OrderType.ASC,2)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求

     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<AgencyEntity> specification = (root, query, cb) -> {
            Predicate p = null;
            // 服务站编号
            if (StringUtils.isNotBlank(this.agencyCode)) {
                p = CustomRestrictions.like("code", this.agencyCode, true).toPredicate(root, query, cb);
//                p = cb.and(p,p01);
            }
            // 服务站名称
            if (StringUtils.isNotBlank(this.agencyName)) {
                if (p == null) {
                    p = CustomRestrictions.like("name", this.agencyName, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("name", this.agencyName, true).toPredicate(root, query, cb));
                }
            }

            // 省份、市
            if (this.selectedProvince != null) {
                if (this.selectedCity != null) {
                    if (p == null) {
                        p = CustomRestrictions.eq("city", this.selectedCity, true).toPredicate(root, query, cb);
                    } else {
                        p = cb.and(p, CustomRestrictions.eq("city", this.selectedCity, true).toPredicate(root, query, cb));
                    }
                } else {
                    if (p == null) {
                        p = CustomRestrictions.eq("province", this.selectedProvince, true).toPredicate(root, query, cb);
                    } else {
                        p = cb.and(p, CustomRestrictions.eq("province", this.selectedProvince, true).toPredicate(root, query, cb));
                    }
                }
            }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    public ProvinceEntity getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(ProvinceEntity selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public CityEntity getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(CityEntity selectedCity) {
        this.selectedCity = selectedCity;
    }

    public CountyEntity getSelectedCounty() {
        return selectedCounty;
    }

    public void setSelectedCounty(CountyEntity selectedCounty) {
        this.selectedCounty = selectedCounty;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public List<CountyEntity> getCountyEntities() {
        return countyEntities;
    }
}
