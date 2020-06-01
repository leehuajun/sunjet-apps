package com.sunjet.vm.flow;


import com.sunjet.model.flow.LeaveBill;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.exception.TabDuplicateException;
import com.sunjet.utils.zk.ZkTabboxUtil;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程定义 列表
 */
public class ProcessDefinitionListVM extends ListBaseVM {
    @WireVariable
    private ProcessService processService;

    private String originFileName;
    private String realFilePath;
    private String flowName;

    private List<ProcessDefinition> processDefinitions = new ArrayList<>();

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public String getRealFilePath() {
        return realFilePath;
    }

    public void setRealFilePath(String realFilePath) {
        this.realFilePath = realFilePath;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public List<ProcessDefinition> getProcessDefinitions() {
        return processDefinitions;
    }

    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.processDefinitions = processService.findProcessDefinitionLastVersionList();
//        this.processDefinitions.get(0).isSuspended()
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void refreshData() {
        this.setKeyword("");
        this.processDefinitions = processService.findProcessDefinitionLastVersionList();
    }

    @Override
    @Command
    @NotifyChange("*")
    public void filterList() {
        this.processDefinitions = processService.findProcessDefinitionLastVersionListByFilter(this.getKeyword());
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        originFileName = event.getMedia().getName();

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW);
        realFilePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW + "/" + fileName;
//        ZkUtils.showInformation("上传文件成功！文件保存路径:\n" + realFilePath, "系统提示");
    }

    @Command
    @NotifyChange("*")
    public void deploy() throws IOException {
        if (this.flowName == null || this.flowName.trim().equals("")) {
            ZkUtils.showInformation("部署名称不能为空！", "系统提示");
            return;
        }
        Deployment deployment = processService.deploy(this.flowName, this.realFilePath);

//        deployment.getId();
//        List<ProcessDefinition> processDefinitions = processService.findProcessDefinitionsByDeploymentId(deployment.getId());

        /**将生成图片放到文件夹下*/
//        String deploymentId = "12501";
        // 获取图片资源名称
//        for(ProcessDefinition processDefinition : processDefinitions) {
//            String tmpPath = "/image-caches/" + processDefinition.getId().replace(":", "-") + ".png";
////        String tmpPath = "/image-caches/"+ UUIDHelper.newUuid() +".png";
//            String imageFile = Executions.getCurrent().getDesktop().getWebApp().getRealPath(tmpPath);
//            File file = new File(imageFile);
//            if (!file.exists()) {
//                // 获取图片的输入流
//                InputStream inputStream = processService.findImageInputStream(processDefinition);
//                // 将图片生成本地目录下
////        File file = new File("/Users/lhj/" + resourceName);
//                FileUtils.copyInputStreamToFile(inputStream, file);
////            this.imagePath = imageFile;
//            }
//        }

        this.flowName = "";
        this.originFileName = "";
        this.realFilePath = "";
//        ZkUtils.showInformation("流程部署成功！\n部署ID:" + deployment.getId() + "\n部署名称:" + deployment.getName(), "系统提示");
        this.processDefinitions = processService.findProcessDefinitionLastVersionList();
    }

    @Command
    public void startProcessInstance(@BindingParam("pd") ProcessDefinition processDefinition, @BindingParam("model") LeaveBill leaveBill) throws TabDuplicateException {

        logger.info("流程定义ID:" + processDefinition.getId());
        logger.info("流程定义KEY:" + processDefinition.getKey());

//        String url = "/views/flow/leavebill_form.zul";
        Map<String, Object> map = new HashMap<>();
        map.put("processDefinition", processDefinition);
        map.put("model", leaveBill);
        String tabId = processDefinition.getKey();
        String tabName = "新建请假申请";
        // 在ProcessDefinition中把Documention属性设置为URL
        String url = processDefinition.getDescription();

        if (StringUtils.isNotBlank(processDefinition.getDescription())) {
            ZkTabboxUtil.newTab(tabId, tabName, null, true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
        } else {
            ZkUtils.showInformation("没找到url参数", "系统提示");
        }
    }

    @Command
    public void showFlowImage(@BindingParam("model") ProcessDefinition processDefinition) {
        String imageViewerUrl = "/views/flow/image_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("processDefinition", processDefinition);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }

    @Command
    public void showDeploymentForm() {
        String deploymentListUrl = "/views/flow/deployment_list.zul";
        Window win = (Window) ZkUtils.createComponents(deploymentListUrl, null, null);
        win.doModal();
    }

//    @Command
//    @NotifyChange("*")
//    public void enableProcessDefinition(ProcessDefinition pd){
//
//    }
//    @Command
//    @NotifyChange("*")
//    public void disableProcessDefinition(ProcessDefinition pd){
//
//    }
}