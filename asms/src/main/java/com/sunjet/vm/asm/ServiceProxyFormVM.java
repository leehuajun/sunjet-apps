package com.sunjet.vm.asm;

import com.sunjet.model.asm.ServiceProxyEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.asm.ServiceProxyService;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 服务委托单 表单 VM
 */
public class ServiceProxyFormVM extends FlowFormBaseVM {

    private String type = "vehicle";
    private Window win;

    private String action;
    private List<PartEntity> qrPartlist = new ArrayList<>();
    private List<PartEntity> partListDelete = new ArrayList<>();
    private List<VehicleEntity> vehiccleListDelete = new ArrayList<>();
    private List<VehicleEntity> vehicles = new ArrayList<>();
    @WireVariable
    private ServiceProxyService serviceProxyService;
    private ServiceProxyEntity serviceProxyRequest;

    public ServiceProxyEntity getServiceProxyRequest() {
        return serviceProxyRequest;
    }

    public void setServiceProxyRequest(ServiceProxyEntity serviceProxyRequest) {
        this.serviceProxyRequest = serviceProxyRequest;
    }

    @Init(superclass = true)
    public void init() {
        serviceProxyRequest = new ServiceProxyEntity();
        if (this.getEntity() != null) {
            serviceProxyRequest = (ServiceProxyEntity) this.getEntity();
        }

    }

    @Command
    public void saveActivity() {
        serviceProxyService.save(serviceProxyRequest);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
    }

    /**
     * 提交、启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
        startProcessInstance(null);
    }

    @Command
    public void checkTab(@BindingParam("type") String type) {
//        ZkUtils.showInformation(type,"ceshi");
        this.type = type;
    }

    @Command
    @NotifyChange("*")
    public void selectVehicles() {
        this.vehicles.add(new VehicleEntity());
    }

    @Command
    public void selectObject() {
        if (type.equals("vehicle")) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", 101010);
            map.put("name", "张三s");
            win = (Window) ZkUtils.createComponents("/views/asm/select_vehicle_form.zul", null, map);
            win.setTitle("选择车辆");
            win.doModal();

        } else {
            System.out.print("11");
            Map<String, Object> map = new HashMap<>();
            map.put("id", 101010);
            map.put("name", "张三");
            win = (Window) ZkUtils.createComponents("/views/asm/checkResult.zul", null, map);
            win.setTitle("选择配件");
            win.doModal();

        }
    }

    @Command
    @NotifyChange("*")
    public void deleter() {
        if (type.equals("vehicle")) {
            for (VehicleEntity i : vehiccleListDelete) {
                vehicles.remove(i);
            }
        } else {
            for (PartEntity i : partListDelete) {
                qrPartlist.remove(i);
            }
        }
    }

    @Command
    public void deletereport(@BindingParam("name") VehicleEntity name, @BindingParam("check") String deleteVehcle) {

        if (deleteVehcle.equals("是")) {
            vehiccleListDelete.add(name);
        } else {
            vehiccleListDelete.remove(name);
        }

    }

    @GlobalCommand
    @NotifyChange("*")
    public void updateSelectedVehicle(@BindingParam("vehicle") VehicleEntity vehicle) {
        Boolean exists = false;
        for (VehicleEntity v : this.vehicles) {
            if (v.getObjId().equals(vehicle.getObjId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            vehicles.add(vehicle);
        }

        if (win != null) {
            win.detach();
        }
    }

    @GlobalCommand
    @NotifyChange("*")
    public void updateSelectedVehicleList(@BindingParam("vehicle") List<VehicleEntity> list) {

        for (VehicleEntity Vehicle : list) {
            Boolean exists = false;
            for (VehicleEntity v : this.vehicles) {
                if (v.getObjId().equals(Vehicle.getObjId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                vehicles.add(Vehicle);
            }
        }


        if (win != null) {
            win.detach();
        }
    }

    @GlobalCommand
    @NotifyChange("*")
    public void updateCheckResultList(@BindingParam("vehicle") List<PartEntity> lst) {
        for (PartEntity vv : lst) {
            Boolean exists = false;
            for (PartEntity v : this.qrPartlist) {
                if (v.getObjId().equals(vv.getObjId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                qrPartlist.add(vv);
            }
        }


        if (win != null) {
            win.detach();
        }
    }

    @Command
    public void deletereports(@BindingParam("name") PartEntity name, @BindingParam("check") String deleteVehcle) {

        if (type.equals("result")) {
            if (deleteVehcle.equals("是")) {
                partListDelete.add(name);
            } else {
                partListDelete.remove(name);
            }
        }
    }


    public List<PartEntity> getQrPartlist() {
        return qrPartlist;
    }

    public void setQrPartlist(List<PartEntity> qrPartlist) {
        this.qrPartlist = qrPartlist;
    }

    public List<PartEntity> getPartListDelete() {
        return partListDelete;
    }

    public void setPartListDelete(List<PartEntity> partListDelete) {
        this.partListDelete = partListDelete;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public List<VehicleEntity> getVehiccleListDelete() {
        return vehiccleListDelete;
    }


    public void setVehiccleListDelete(List<VehicleEntity> vehiccleListDelete) {
        this.vehiccleListDelete = vehiccleListDelete;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }
}
