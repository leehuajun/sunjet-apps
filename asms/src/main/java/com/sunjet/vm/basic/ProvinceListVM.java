package com.sunjet.vm.basic;

import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.basic.ProvinceService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ProvinceListVM extends ListBaseVM {
    @WireVariable
    private ProvinceService provinceService;

    private List<ProvinceEntity> provinces = new ArrayList<>();
    private List<ProvinceEntity> coldProvinces = new ArrayList<>();
    private List<ProvinceEntity> normalProvinces = new ArrayList<>();

    private ProvinceEntity coldProvince = null;
    private ProvinceEntity normalProvince = null;

    public ProvinceEntity getColdProvince() {
        return coldProvince;
    }

    public void setColdProvince(ProvinceEntity coldProvince) {
        this.coldProvince = coldProvince;
    }

    public ProvinceEntity getNormalProvince() {
        return normalProvince;
    }

    public void setNormalProvince(ProvinceEntity normalProvince) {
        this.normalProvince = normalProvince;
    }

    public List<ProvinceEntity> getColdProvinces() {
        return coldProvinces;
    }

    public void setColdProvinces(List<ProvinceEntity> coldProvinces) {
        this.coldProvinces = coldProvinces;
    }

    public List<ProvinceEntity> getNormalProvinces() {
        return normalProvinces;
    }

    public void setNormalProvinces(List<ProvinceEntity> normalProvinces) {
        this.normalProvinces = normalProvinces;
    }

    @Init
    public void init() {
        this.setKeyword("");
        this.setFormUrl("/views/basic/province_form.zul");
        initProvince();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange("*")
    public void refreshData() {
        initProvince();
    }

    private void initProvince() {
        this.provinces = provinceService.findAllSortCold();
        this.coldProvinces.clear();
        this.normalProvinces.clear();
        for (ProvinceEntity pe : provinces) {
            if (pe.getCold()) {
                coldProvinces.add(pe);
            } else {
                normalProvinces.add(pe);
            }
        }
    }

    @Command
    @NotifyChange("*")
    public void addColdProvince() {
        if (this.normalProvince == null) {
            ZkUtils.showExclamation("没有选择常规省份", "系统提示");
        } else {
            normalProvince.setCold(true);
            provinceService.saveProvince(normalProvince);
            initProvince();
            this.coldProvince = null;
            this.normalProvince = null;
        }
    }

    @Command
    @NotifyChange("*")
    public void removeColdProvince() {
        if (this.coldProvince == null) {
            ZkUtils.showExclamation("没有选择严寒省份", "系统提示");
        } else {
            coldProvince.setCold(false);
            provinceService.saveProvince(coldProvince);
            initProvince();
            this.coldProvince = null;
            this.normalProvince = null;
        }
    }
}
