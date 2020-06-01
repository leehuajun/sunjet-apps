package com.sunjet.vm.basic;

import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

/**
 * 车辆 表单
 * Created by Administrator on 2016/9/5.
 */
public class VehicleFormVM extends FormBaseVM {
    @WireVariable
    private VehicleService vehicleService;
    private VehicleEntity vehicle;

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            vehicle = (VehicleEntity) vehicleService.getRepository().findOne(objId);
        } else {
            vehicle = new VehicleEntity();
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
    }
}
