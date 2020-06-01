package com.sunjet.vm.basic;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.*;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.asm.SelectRecycleFormVM;
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
 * 服务站 列表
 * Created by Administrator on 2016/9/5.
 */
public class DealerListVM extends ListBaseVM {

    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private RegionService regionService;


    private List<ProvinceEntity> provinceEntities = new ArrayList<>();  // 省份/直辖市 集合
    private List<CityEntity> cityEntities = new ArrayList<>();          // 选中的省份/直辖市的下属城市集合
    private List<CountyEntity> countyEntities = new ArrayList<>();      // 选中的城市的下属县/区集合
    private List<DictionaryEntity> listStarDE = new ArrayList<>();           // 星级
    private List<String> listStar = new ArrayList<>();


    private List<DealerEntity> parentDealers = new ArrayList<>();       // 列出所有父级服务站
    private DealerEntity parentDealer;              // 父级服务站
    private String dealerCode = StringUtils.EMPTY;
    private String dealerName = StringUtils.EMPTY;
    private String serviceManager = "";
    private String dealerStar = "";
    private String dealerLevel = "";
    private ProvinceEntity selectedProvince;        // 选中的 省份/直辖市
    private CityEntity selectedCity;                // 选中的 城市
    private CountyEntity selectedCounty;            // 选中的 县/区

    @Init
    public void Init() {
        this.setHeaderRows(2);
        this.setBaseService(dealerService);
        this.setFormUrl("/views/basic/dealer_form.zul");
        provinceEntities = regionService.findAllProvince();

        listStarDE = CacheManager.getDictionariesByParentCode("10010");

        for (DictionaryEntity de : listStarDE) {
            listStar.add(de.getName());
        }

        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.dealerCode = StringUtils.EMPTY;
        this.dealerName = StringUtils.EMPTY;
        this.selectedCity = null;
        this.selectedProvince = null;
        this.cityEntities = new ArrayList<>();
        this.dealerStar = StringUtils.EMPTY;
        this.dealerLevel = StringUtils.EMPTY;
        this.parentDealer = null;
        this.serviceManager = StringUtils.EMPTY;
    }

    @Command
    @NotifyChange("*")
    public void selectProvince2() {
        this.parentDealers.clear();
        this.parentDealer = null;
        if (selectedProvince != null) {
            this.selectedCity = null;
            this.selectedCounty = null;
            this.cityEntities = regionService.findCitiesByProvinceId(this.selectedProvince.getObjId());
        }
    }

    @Command
    @NotifyChange("*")
    public void selectCity2() {
        this.parentDealers.clear();
        this.parentDealer = null;
        if (selectedCity != null) {
            this.selectedCounty = null;
            this.countyEntities = regionService.findCountiesByCityId(this.selectedCity.getObjId());
        }
    }

    @Command
    @NotifyChange("parentDealers")
    public void searchDealers2() {
        if (this.selectedCity != null) {  // 如果选择了城市
            this.parentDealers = dealerService.findParentDealersByCity(this.selectedCity);
        } else if (this.selectedProvince != null) {  // 如果选择了省份
            this.parentDealers = dealerService.findParentDealersByProvince(this.selectedProvince);
        } else {   // 否则选择所有有子服务站的 服务站信息
            this.parentDealers = dealerService.findParentDealers();
        }


//        this.parentDealers = dealerService.findParentDealers();
    }

    @Command
    @NotifyChange("parentDealer")
    public void selectDealer2(@BindingParam("model") DealerEntity dealer) {
        this.parentDealer = dealer;
    }

    @Command
    @NotifyChange("parentDealer")
    public void clearSelectedDealer2() {
        this.parentDealer = null;
    }

    @Override
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
        Specification<DealerEntity> specification = (root, query, cb) -> {
            Predicate p = null;
            // 服务站编号
            if (StringUtils.isNotBlank(this.dealerCode)) {
                p = CustomRestrictions.like("code", this.dealerCode, true).toPredicate(root, query, cb);
//                p = cb.and(p,p01);
            }
            // 服务站名称
            if (StringUtils.isNotBlank(this.dealerName)) {
                if (p == null) {
                    p = CustomRestrictions.like("name", this.dealerName, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("name", this.dealerName, true).toPredicate(root, query, cb));
                }
            }

            // 星级
            if (StringUtils.isNotBlank(this.dealerStar) && !this.dealerStar.equals("全部")) {
                if (p == null) {
                    p = CustomRestrictions.eq("star", this.dealerStar, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.eq("star", this.dealerStar, true).toPredicate(root, query, cb));
                }
            }
            // 等级
            if (StringUtils.isNotBlank(this.dealerLevel) && !this.dealerLevel.equals("全部")) {
                if (p == null) {
                    p = CustomRestrictions.eq("level", this.dealerLevel, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.eq("level", this.dealerLevel, true).toPredicate(root, query, cb));
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

            // 父级服务站
            if (this.parentDealer != null) {
                if (p == null) {
                    p = CustomRestrictions.eq("parent", this.parentDealer, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.eq("parent", this.parentDealer, true).toPredicate(root, query, cb));
                }
            }

//        if(!this.selectedProvince.getName().equalsIgnoreCase("全部")){
//
//        }
            // 服务经理
            if (StringUtils.isNotBlank(this.serviceManager)) {
                if (p == null) {
                    p = CustomRestrictions.like("serviceManager.name", this.serviceManager, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, p = CustomRestrictions.like("serviceManager.name", this.serviceManager.trim(), true).toPredicate(root, query, cb));
                }
            }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    //更新配件列表
    @GlobalCommand(GlobalCommandValues.REFRESH_DEALER_LIST)
    @NotifyChange("*")
    public void refreshDealerList() {
        initList();
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

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public List<CountyEntity> getCountyEntities() {
        return countyEntities;
    }

    public DealerEntity getParentDealer() {
        return parentDealer;
    }

    public void setParentDealer(DealerEntity parentDealer) {
        this.parentDealer = parentDealer;
    }

    public List<DealerEntity> getParentDealers() {
        return parentDealers;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerStar() {
        return dealerStar;
    }

    public void setDealerStar(String dealerStar) {
        this.dealerStar = dealerStar;
    }

    public String getDealerLevel() {
        return dealerLevel;
    }

    public void setDealerLevel(String dealerLevel) {
        this.dealerLevel = dealerLevel;
    }

    public List<DictionaryEntity> getListStarDE() {
        return listStarDE;
    }

    public List<String> getListStar() {
        return listStar;
    }

    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }
}
