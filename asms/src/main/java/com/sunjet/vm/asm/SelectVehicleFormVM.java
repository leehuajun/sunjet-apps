package com.sunjet.vm.asm;

import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.basic.VehicleService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.activiti.engine.runtime.Execution;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class SelectVehicleFormVM extends FormBaseVM {
    //private String mc;
    private String keyword = "";
    @WireVariable
    private VehicleService vehicleService;
    @WireVariable
    private List selectedVehicleList = new ArrayList<>();
    private List<VehicleEntity> vehicleEntities = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
//        logger.info(Executions.getCurrent().getArg().get("id").toString());
//        logger.info(Executions.getCurrent().getArg().get("name").toString());
    }

    @Command
    @NotifyChange("*")
    public void searchVehicle() {
        if (this.keyword.trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
            this.vehicleEntities = vehicleService.findAllByKeyword(this.keyword.trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List getSelectedVehicleList() {
        return selectedVehicleList;
    }

    public void setSelectedVehicleList(List selectedVehicleList) {
        this.selectedVehicleList = selectedVehicleList;
    }

    public List<VehicleEntity> getVehicleEntities() {
        return vehicleEntities;
    }

    public void setVehicleEntities(List<VehicleEntity> vehicleEntities) {
        this.vehicleEntities = vehicleEntities;
    }

}
