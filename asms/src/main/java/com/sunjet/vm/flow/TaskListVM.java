package com.sunjet.vm.flow;

import com.sunjet.service.admin.UserService;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.BaseVM;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhj on 16/10/17.
 */
public class TaskListVM extends BaseVM {
    protected final static Logger logger = LoggerFactory.getLogger(TaskListVM.class);

    //    @WireVariable
//    private ProcessService processService;
    @WireVariable
    private UserService userService;

    private Date startDate;
    private Date endDate;
    private String processName;
    private String orgName;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    //    private List<UserEntity> userList = new ArrayList<>();
//    private Map<String, UserEntity> mapUser = new HashMap<>();
//    private Map<String, String> mapRequestOrg = new HashMap<>();
//
//    public Map<String, UserEntity> getMapUser() {
//        return mapUser;
//    }
//
//    public void setMapUser(Map<String, UserEntity> mapUser) {
//        this.mapUser = mapUser;
//    }
//
//    public Map<String, String> getMapRequestOrg() {
//        return mapRequestOrg;
//    }
//
//    public void setMapRequestOrg(Map<String, String> mapRequestOrg) {
//        this.mapRequestOrg = mapRequestOrg;
//    }


    @Init
    public void init() {
        startDate = DateHelper.getFirstOfYear();
        endDate = new Date();
//        initUserMap();
        initRequestOrg();
        initProcessDefinition();
        this.setTasks(this.getTaskList());
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

//    private void initUserMap() {
//        this.mapUser.clear();
//        List<UserEntity> userList = userService.findAll();
//        for (UserEntity userEntity : userList) {
//            mapUser.put(userEntity.getLogId(), userEntity);
//        }
//    }


    @Command
    @NotifyChange("*")
    public void refresh() {
        this.setTasks(this.getTaskList());
    }


    @Command
    public void showFlowImage(@BindingParam("task") Task task) {
        String imageViewerUrl = "/views/flow/image_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("task", task);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }

//    @Command
//    public void handleTask(@BindingParam("task")Task task){
//        String businessId = processService.findBusinessIdByProcessInstanceId(task.getProcessInstanceId());
//        handleTaskByTask
//    }

//    @Command
//    public void taskClicked(@BindingParam("task") Task task) throws TabDuplicateException {
//        String processDefinitionName = this.getMapProcessDefinition().get(task.getProcessDefinitionId()).getName();
////        String formKey = processService.findTaskFormKeyByTaskId(task.getId());
//        String formUrl = processService.findFormUrl(task);
//        //String businessId = processService.findBusinessIdByTaskId(task.getId());
//
////        this.handleTask(task,formUrl,processDefinitionName);
//        this.handleTask(task);

//        logger.info("任务formUrl:" + formUrl);
//
//        if (formUrl == null || formUrl.equals("")) {
//            ZkUtils.showError("流程定义文件中没有定义formUrl属性！", "系统提示");
//            return;
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        // 0:草稿  1:审核中  2:已审核  3:正常关闭  -1:异常关闭
////        map.put("status", DocStatus.AUDITING);
//        map.put("taskId", task.getId());
//        map.put("formUrl", formUrl);
//
////        processService.findBusinessIdByTaskId(task.getId());
//
//        String editorFormUrl = "/views/flow/editor_form.zul";
//
//        win = (Window) ZkUtils.createComponents(editorFormUrl, null, map);
//        win.setTitle(processDefinitionName);
//        win.doModal();

//        String tabId = task.getId();
//        String tabName = processDefinitionName;
//        ZkTabboxUtil.newTab(tabId,tabName,null, true, ZkTabboxUtil.OverFlowType.AUTO, formKey, map);
//    }

    @GlobalCommand(GlobalCommandValues.LIST_TASK)
    @NotifyChange("*")
    public void listTask() {
//        if (this.win!= null) {
//            this.win.detach();
//        }

        this.setTasks(this.getTaskList());
        searchTasks();

    }


//    public String getOrgName(String logId){
//        return this.mapRequestOrg.get(logId);
//    }

    @Command
    @NotifyChange("*")
    public void searchTasks() {
        List<Task> taskList = this.getTaskList();
        this.getTasks().clear();
        for (Task task : taskList) {
            boolean isOk = true;

            if (this.processName != null && !this.processName.trim().equals("")) {
                if (this.getMapProcessDefinition().get(task.getProcessDefinitionId()).getName().toString().contains(this.processName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                if (task.getCreateTime().after(startDate) && task.getCreateTime().before(endDate)) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }
            if (isOk && this.orgName != null && !this.orgName.trim().equals("")) {
                String orgname = this.getMapRequestOrg().get(this.getSubmitter(task.getProcessInstanceId()));

                if (orgname != null && orgname.contains(orgName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                this.getTasks().add(task);
            }
        }
    }
}
