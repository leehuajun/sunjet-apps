package com.sunjet.vm.report.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.basic.RegionService;
import com.sunjet.utils.common.ExcelUtil;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务站信息统计
 */
public class DealerMessageVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;

    @WireVariable
    private RegionService regionService;

    private List<Map<String, Object>> maps;

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


    @Init(superclass = true)
    public void init() {
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM dealer_message ");


        provinceEntities = regionService.findAllProvince();

        listStarDE = CacheManager.getDictionariesByParentCode("10010");

        for (DictionaryEntity de : listStarDE) {
            listStar.add(de.getName());
        }

        initList();
        this.setEnableAdd(false);
        this.setEnableExport(true);


    }


    @Command
    @NotifyChange("maps")
    public void search() {
        maps.clear();
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM dealer_message " +
                        "WHERE (bealerName  LIKE  '%" + this.getDealerName() + "%' OR  bealerName IS  NULL )" +
                        "AND (bealerCode  LIKE  '%" + this.getDealerCode() + "%' OR  bealerCode IS  NULL )" +
                        "AND (level LIKE  '%" + this.getDealerLevel() + "%' OR  level IS  NULL )" +
                        "AND (service_manager LIKE  '%" + this.getServiceManager() + "%' OR service_manager IS NULL )" +
                        "AND (provinceName LIKE  '%" + (this.getSelectedProvince() == null ? "" : this.getSelectedProvince().getName()) + "%'OR provinceName IS NULL )" +
                        "AND (city LIKE  '%" + (this.getSelectedCity() == null ? "" : this.getSelectedCity().getName()) + "%' OR city IS  NULL )" +
                        "AND star LIKE  '%" + this.getDealerStar() + "%'");

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
    public void exportExcel() {
        if (maps.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        }
        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("电话");
        titleList.add("传真");
        titleList.add("地址");
        titleList.add("所在省");
        titleList.add("市");
        titleList.add("SGMW体系");
        titleList.add("申请等级");
        titleList.add("维修资质");
        titleList.add("服务站级别");
        titleList.add("父级服务站");
        titleList.add("组织机构代码");
        titleList.add("纳税人识别号");
        titleList.add("开户银行");
        titleList.add("银行账号");
        titleList.add("营业执照号");
        titleList.add("服务经理");
        titleList.add("其他合作内容");
        titleList.add("法人代表");
        titleList.add("法人电话");
        titleList.add("站长");
        titleList.add("站长电话");
        titleList.add("技术主管");
        titleList.add("技术主管电话");
        titleList.add("索赔主管");
        titleList.add("索赔主管电话");
        titleList.add("配件主管");
        titleList.add("配件主管电话");
        titleList.add("财务经理");
        titleList.add("财务经理电话");
        titleList.add("员工总数");
        titleList.add("接待员数量");
        titleList.add("配件员数量");
        titleList.add("维修工数量");
        titleList.add("质检员数量");
        titleList.add("结算员数量");
        titleList.add("停车面积");
        titleList.add("接待室");
        titleList.add("综合维修区");
        titleList.add("总成维修区");
        titleList.add("配件库总面积");
        titleList.add("五菱库总面积");
        titleList.add("旧件库面积");
        titleList.add("五菱旧件库面积");
        titleList.add("其他车辆维修条件");
        titleList.add("其他品牌");
        titleList.add("维修产品");
        titleList.add("情况说明");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("bealerCode");
        keyList.add("bealerName");
        keyList.add("phone");
        keyList.add("fax");
        keyList.add("address");
        keyList.add("provinceName");
        keyList.add("city");
        keyList.add("sgmw_system");
        keyList.add("star");
        keyList.add("qualification");
        keyList.add("level");
        keyList.add("parentName");
        keyList.add("org_code");
        keyList.add("taxpayer_code");
        keyList.add("bank");
        keyList.add("bank_account");
        keyList.add("business_license_code");
        keyList.add("service_manager");
        keyList.add("other_collaboration");
        keyList.add("legal_person");
        keyList.add("legal_person_phone");
        keyList.add("station_master");
        keyList.add("station_master_phone");
        keyList.add("technical_director");
        keyList.add("technical_director_phone");
        keyList.add("claim_director");
        keyList.add("claim_director_phone");
        keyList.add("part_director");
        keyList.add("part_director_phone");
        keyList.add("finance_director");
        keyList.add("finance_director_phone");
        keyList.add("employee_count");
        keyList.add("receptionist_count");
        keyList.add("part_keeyper_count");
        keyList.add("maintainer_count");
        keyList.add("qc_inspector_count");
        keyList.add("clerk_count");
        keyList.add("parking_area");
        keyList.add("reception_area");
        keyList.add("general_area");
        keyList.add("assembly_area");
        keyList.add("storage_area");
        keyList.add("storage_wuling_area");
        keyList.add("storage_userd_part_area");
        keyList.add("storage_wuling_userd_part_area");
        keyList.add("other_maintain_condition");
        keyList.add("other_brand");
        keyList.add("products_of_maintain");
        keyList.add("other_products");

        ExcelUtil.ListMapToExcel(titleList, keyList, maps);


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

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
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

    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
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

    @Override
    public ProvinceEntity getSelectedProvince() {
        return selectedProvince;
    }

    @Override
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

    public DealerEntity getParentDealer() {
        return parentDealer;
    }

    public void setParentDealer(DealerEntity parentDealer) {
        this.parentDealer = parentDealer;
    }

    public List<String> getListStar() {
        return listStar;
    }

    public void setListStar(List<String> listStar) {
        this.listStar = listStar;
    }


    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public void setCityEntities(List<CityEntity> cityEntities) {
        this.cityEntities = cityEntities;
    }

    public List<CountyEntity> getCountyEntities() {
        return countyEntities;
    }

    public void setCountyEntities(List<CountyEntity> countyEntities) {
        this.countyEntities = countyEntities;
    }

    public List<DictionaryEntity> getListStarDE() {
        return listStarDE;
    }

    public void setListStarDE(List<DictionaryEntity> listStarDE) {
        this.listStarDE = listStarDE;
    }

    public List<DealerEntity> getParentDealers() {
        return parentDealers;
    }

    public void setParentDealers(List<DealerEntity> parentDealers) {
        this.parentDealers = parentDealers;
    }

    @Override
    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    @Override
    public void setProvinceEntities(List<ProvinceEntity> provinceEntities) {
        this.provinceEntities = provinceEntities;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }
}


