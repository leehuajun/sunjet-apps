package com.sunjet.vm.base;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.exception.TabDuplicateException;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础列表型 ViewEntity
 * <p>
 * Created by lhj on 15/11/6.
 */
public class FlowListBaseVM extends ListBaseVM {


    @WireVariable
    private UserService userService;
    private Window win;
    private List<DocStatus> documentStatuses = DocStatus.getListWithAll();
    private DocStatus selectedStatus = DocStatus.ALL;
    private String docNo;
    private String srcDocNo;
    private String srcDocType;

    private Map<String, Task> mapTasks = new HashMap<>();
    private Map<String, User> mapUsers = new HashMap<>();
//    private String serviceManager;

    @Init(superclass = true)
    public void flowListInit() {
//        if (CommonHelper.getActiveUser().getUserType().equalsIgnoreCase("wuling")) {
//
//        } else if (CommonHelper.getActiveUser().getUserType().equalsIgnoreCase("dealer")) {
//            this.dealer = CommonHelper.getActiveUser().getDealer();
//        } else if (CommonHelper.getActiveUser().getUserType().equalsIgnoreCase("agency")) {
//            this.agency = CommonHelper.getActiveUser().getAgency();
//        }

        this.setDealer(CommonHelper.getActiveUser().getDealer());
        this.setAgency(CommonHelper.getActiveUser().getAgency());
        this.setUserType(CommonHelper.getActiveUser().getUserType());

        List<Task> waitingTasks = processService.findWaitingTasks();
        for (Task task : waitingTasks) {
            mapTasks.put(task.getProcessInstanceId(), task);
        }
        List<User> users = processService.findUsers();
        for (User user : users) {
            mapUsers.put(user.getId(), user);
        }
    }

    @Command
    public void handleTask(@BindingParam("entity") FlowDocEntity flowDocEntity) throws TabDuplicateException {
        Map<String, Object> map = new HashMap<>();
        if (flowDocEntity == null) {   // 新增
            map.put("status", DocStatus.DRAFT);
        } else {
            // 0:草稿  1:审核中  2:已审核  3:正常关闭  -1:异常关闭
            map.put("status", DocStatus.getDocStatus(flowDocEntity.getStatus()));
            map.put("businessId", flowDocEntity.getObjId());
        }

        win = (Window) ZkUtils.createComponents(this.getFormUrl(), null, map);
        win.doModal();
    }

    @GlobalCommand(GlobalCommandValues.LIST_TASK)
    @NotifyChange("*")
    public void listTask() {
        if (win != null) {
            win.detach();
        }
        initList();
    }


    @Command
    @NotifyChange("*")
    public void reset() {
//        List<DealerEntity> dealers = new ArrayList<>();
//        UserEntity user = userService.findOne(CommonHelper.getActiveUser().getUserId());
//        if (user.getDealer() != null) {
//            this.dealer = user.getDealer();
//            this.dealers.clear();
//            this.dealers.add(this.dealer);
//        } else {
//            this.dealer = null;
//            this.dealers.clear();
//        }

        this.setDealer(CommonHelper.getActiveUser().getDealer());

        this.getDealers().clear();
//        if (this.dealer != null) {
//            this.dealers.add(this.dealer);
//        }

        this.setAgency(CommonHelper.getActiveUser().getAgency());
        this.getAgencies().clear();


        this.docNo = "";
        this.srcDocNo = "";
        this.srcDocType = "";
        this.setSelectedStatus(DocStatus.ALL);
        this.setEndDate(new Date());
        this.setStartDate(DateHelper.getFirstOfMonth());
//        this.setServiceManager(CommonHelper.getActiveUser().getUsername());
    }


    @Command
    @NotifyChange("resultDTO")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        if (flowEntity.getStatus().equals(0)) {
            this.getBaseService().getRepository().delete(flowEntity.getObjId());
            initList();
        } else {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
        }
    }

    public String getCurrentNode(String processInstanceId) {
        Task task = mapTasks.get(processInstanceId);
        if (task == null)
            return "";
        if (task.getAssignee() != null && !task.getAssignee().toString().trim().equals("")) {
            return mapUsers.get(task.getAssignee()) == null ? task.getName() : mapUsers.get(task.getAssignee()).getFirstName();
        } else {
            return task.getName();
        }
    }

    public DocStatus getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(DocStatus selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public List<DocStatus> getDocumentStatuses() {
        return documentStatuses;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getSrcDocNo() {
        return srcDocNo;
    }

    public void setSrcDocNo(String srcDocNo) {
        this.srcDocNo = srcDocNo;
    }

    public String getSrcDocType() {
        return srcDocType;
    }

    public void setSrcDocType(String srcDocType) {
        this.srcDocType = srcDocType;
    }

//    public String getServiceManager() {
//        return serviceManager;
//    }
//
//    public void setServiceManager(String serviceManager) {
//        this.serviceManager = serviceManager;
//    }


    public Map<String, Task> getMapTasks() {
        return mapTasks;
    }

    public void setMapTasks(Map<String, Task> mapTasks) {
        this.mapTasks = mapTasks;
    }

    public void setDocumentStatuses(List<DocStatus> documentStatuses) {
        this.documentStatuses = documentStatuses;
    }
}
