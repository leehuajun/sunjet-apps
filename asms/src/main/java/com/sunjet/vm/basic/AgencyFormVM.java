package com.sunjet.vm.basic;

import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * 合作商  表单
 * Created by Administrator on 2016/9/5.
 */
public class AgencyFormVM extends FormBaseVM {

    @WireVariable
    private AgencyService agencyService;

    private AgencyEntity agency;
    private String selectEnabled;
    private List<String> enableds = new ArrayList<>();

    public AgencyEntity getAgency() {
        return agency;
    }


    @Init(superclass = true)
    public void init() {
        this.enableds.add("是");
        this.enableds.add("否");
        if (StringUtils.isNotBlank(objId)) {
            agency = (AgencyEntity) agencyService.getRepository().findOne(objId);
        } else {
            agency = new AgencyEntity();
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
    }

    @Command
    public void saveAgency() {
        if ("是".equals(this.getSelectEnabled())) {
            agency.setEnabled(true);
        } else if ("否".equals(this.getSelectEnabled())) {
            agency.setEnabled(false);
        } else {
            //默认设置
            agency.setEnabled(agency.getEnabled());
        }

        agencyService.saveAgency(agency);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_LIST, null);
        ZkUtils.showInformation("保存成功", "提示");
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_AGENCY + filename;
    }

    public List<String> getEnableds() {
        return enableds;
    }

    public String getSelectEnabled() {
        return selectEnabled;
    }

    public void setSelectEnabled(String selectEnabled) {
        this.selectEnabled = selectEnabled;
    }

    public void setEnableds(List<String> enableds) {
        this.enableds = enableds;
    }
}
