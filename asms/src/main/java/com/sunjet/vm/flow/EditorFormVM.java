package com.sunjet.vm.flow;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.flow.LeaveBill;
import com.sunjet.service.ServiceHelper;
import com.sunjet.service.flow.LeaveBillService;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.activiti.engine.task.Task;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class EditorFormVM extends FlowFormBaseVM {

    @WireVariable
    private ProcessService processService;
    @WireVariable
    private ServiceHelper serviceHelper;
//    private LeaveBillService leaveBillService;
//    private LeaveBill leaveBill;


    private List<ProvinceEntity> provinceEntities;  // 省份/直辖市 集合
    private List<CityEntity> cityEntities;          // 选中的省份/直辖市的下属城市集合
    private List<CountyEntity> countyEntities;      // 选中的城市的下属县/区集合

    private ProvinceEntity selectedProvince;        // 选中的 省份/直辖市
    private CityEntity selectedCity;                // 选中的 城市
    private CountyEntity selectedCounty;            // 选中的 县/区

    private String taskId;
    private String formUrl;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public CountyEntity getSelectedCounty() {
        return selectedCounty;
    }

    public void setSelectedCounty(CountyEntity selectedCounty) {
        this.selectedCounty = selectedCounty;
    }

    public CityEntity getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(CityEntity selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public void setCityEntities(List<CityEntity> cityEntities) {
        this.cityEntities = cityEntities;
    }

    public ProvinceEntity getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(ProvinceEntity selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public List<CountyEntity> getCountyEntities() {
        return countyEntities;
    }

    //    public LeaveBill getLeaveBill() {
//        return leaveBill;
//    }
//
//    public void setLeaveBill(LeaveBill leaveBill) {
//        this.leaveBill = leaveBill;
//    }

    @Init(superclass = true)
    public void init() {
//        System.out.println("OKOKOKOK!");
//        map.put("status",1);
////        map.put("processDefinitionId",task.getProcessDefinitionId());
////        map.put("businessId",processService.findBusinessIdByTaskId(task.getId()));
//        map.put("taskId",task.getId());
//        map.put("formUrl",formKey);
        taskId = (String) Executions.getCurrent().getArg().get("taskId");
        formUrl = (String) Executions.getCurrent().getArg().get("formUrl");
//        formPath = (String) Executions.getCurrent().getArg().get("formUrl");
        String businessKey = processService.findBusinessKeyByTaskId(taskId);
        String[] values = businessKey.split("\\.");
//        FlowBaseService flowBaseService = this.serviceHelper.getService(values[0]);
//        this.setBaseService(flowBaseService);
////        this.flowDocEntity = (FlowDocEntity) flowBaseService.getRepository().findOne(values[1]);
//        this.setEntity((FlowDocEntity) flowBaseService.getRepository().findOne(values[1]));


//        this.setBaseService(leaveBillService);
//        initData();
//        if (this.getFlowDocEntity() == null) {
//            this.leaveBill = new LeaveBill();
//            this.setFlowDocEntity(this.leaveBill);
//        } else {
//            this.leaveBill = (LeaveBill) this.getFlowDocEntity();
//        }
//    }


//        System.out.println("taskId:" + taskId);
//        System.out.println("formUrl:" + formUrl);
//        this.formPath = "/views/forms/test/leavebill_form.zul";
//        this.setProcessDefinitionKey("LeaveBill");
//        this.setBaseService(leaveBillService);

//        initData();
//        this.flowDocEntity = (FlowDocEntity)this.getFlowDocEntity();
//        if (this.getFlowDocEntity() == null) {
//            this.leaveBill = new LeaveBill();
//            this.setFlowDocEntity(this.leaveBill);
//        } else {
//            this.leaveBill = (LeaveBill) this.getFlowDocEntity();
//        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
        if (win != null) {
            win.setTitle(win.getTitle() + titleMsg);
        }
    }

//    @GlobalCommand
//    public void commitTask(@BindingParam("outcome") String outcome,@BindingParam("comment")String comment) throws IOException {
//        Map<String,Object> variables = new HashMap<>();
////        variables.put("days", this.leaveBill.getDays());
////        variables.put("outcome",outcome);
////        variables.put("comment",comment);
//        completeTask(outcome,comment);
//        showDialog();
//    }

    @Command
    public void showFlowImage() {
        Task task = processService.findTaskById(taskId);
        String imageViewerUrl = "/views/flow/image_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("task", task);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }

//    @Command
//    public void showDiagram(){
//        Task task = processService.findTaskById(taskId);
//
//        String imageViewerUrl = "/diagram-viewer/index.html?processDefinitionId="
//                + task.getProcessDefinitionId()
//                + "&processInstanceId="
//                + task.getProcessInstanceId();
////        Map<String,Object> vars = new HashMap<>();
////        vars.put("task",task);
//        Window win = (Window) ZkUtils.createComponents(imageViewerUrl,null,null);
//        win.doModal();
//    }
}
