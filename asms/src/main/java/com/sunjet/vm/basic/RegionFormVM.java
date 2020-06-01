package com.sunjet.vm.basic;

import com.sunjet.service.admin.RoleService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

/**
 * 区域 表单
 * Created by Administrator on 2016/9/12.
 */
public class RegionFormVM extends FormBaseVM {
    @Wire
    private Window provinceForm;
    @WireVariable
    private RegionService provinceService;
    @WireVariable
    private RoleService roleService;

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

