package com.sunjet.vm.base;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DocumentNoEntity;
import com.sunjet.model.flow.CommentEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.basic.DocumentNoService;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.activiti.CustomComment;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.JsonHelper;
import com.sunjet.utils.common.StringHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * 基础表单型 ViewEntity
 * <p>
 * Created by lhj on 15/11/6.
 */
public class FlowFormBaseVM extends BaseVM {
    // 固定的长度
    private final static Integer FIXED_LENGTH = 4;
    protected Window win;           // 当前窗体
    protected String titleMsg = "";
    // 按钮显示开关
    private Boolean enableCommit = true;   // 提交按钮状态
    private Boolean enableHandleTask = true;   // 任务办理按钮状态
    private Boolean enableSave = true;         // 保存按钮状态
    private Boolean enableShowFlowImage = true;  // 查看流程图按钮状态
    @WireVariable
    private UserService userService;
    @WireVariable
    private ProcessService processService;
    @WireVariable
    private DocumentNoService documentNoService;
    private String processDefinitionKey = "";           // 流程定义Id
    private BaseService baseService = null;     // 流程基础服务
    private Window handleForm;      // 流程处理窗体
    private String taskId = "";
    private String businessId = "";
    private Integer status = DocStatus.DRAFT.getIndex();
    private Boolean readonly = true;
    //    private Map<String, String> users = new HashMap<>();
    private FlowDocEntity entity;
//    private ProcessDefinition processDefinition;

    private List<String> outcomes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();


    /**
     * 新增或编辑（create 或 save ）
     */
    @Init(superclass = true)
    public void flowFormBaseInit() {
        businessId = (String) Executions.getCurrent().getArg().get("businessId");
        taskId = (String) Executions.getCurrent().getArg().get("taskId");

        this.initUserList();
        this.setDealer(CommonHelper.getActiveUser().getDealer());
        this.setAgency(CommonHelper.getActiveUser().getAgency());
        this.setUserType(CommonHelper.getActiveUser().getUserType());
    }

    // 获取Task对象
//    protected Task getTaskByBusinessId(String businessId) {
//        String businessKey = entity.getClass().getSimpleName() + "." + entity.getObjId();
//        return processService.findTaskByBusinessKey(businessKey);
//    }

//    // 获取所有出口
//    protected List<String> getOutcomesByTaskId(String taskId){
//        return processService.findOutComeListByTaskId(taskId);
//    }
//    // 获取所有批注（流程审批意见）
//    protected List<Comment> getCommentsByTaskId(String taskId){
//        return processService.findCommentByTaskId(taskId);
//    }

    @Deprecated
    protected void initStatus() {
        // 草稿状态 和 驳回状态，允许修改
        if (status == DocStatus.DRAFT.getIndex() || status == DocStatus.REJECT.getIndex()) {
            if (this.entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId())) {
                this.setReadonly(false);
            } else {
                this.setReadonly(true);
            }
//            this.canSubmit = true;
        } else { // 其余状态不允许修改
            this.setReadonly(true);
//            this.canSubmit = false;
        }
    }

    /**
     * 保存对象
     */
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        entity = saveEntity(entity);
//        ZkUtils.showInformation("保存成功！","系统提示");
//        showDialog();
    }

    /**
     * 保存对象
     *
     * @param entity
     */
    @Command
    @NotifyChange("*")
    public FlowDocEntity saveEntity(@BindingParam("entity") FlowDocEntity entity) {
        if (StringUtils.isBlank(entity.getObjId())) {
//            entity.setSubmitter(CommonHelper.getActiveUser().getLogId());
//            entity.setSubmitterPhone(CommonHelper.getActiveUser().getPhone());
            entity.setStatus(DocStatus.DRAFT.getIndex());
            entity.setCreaterId(CommonHelper.getActiveUser().getLogId());
            entity.setCreaterName(CommonHelper.getActiveUser().getUsername());
            entity.setCreatedTime(new Date());
        } else {
            entity.setModifierId(CommonHelper.getActiveUser().getLogId());
            entity.setModifierName(CommonHelper.getActiveUser().getUsername());
            entity.setModifiedTime(new Date());
        }

        if (StringUtils.isBlank(entity.getDocNo())) {
            entity.setDocNo(documentNoService.getDocumentNo(entity.getClass().getSimpleName()));
        }

        //if(baseService!=null)
//        showDialog();
        return (FlowDocEntity) baseService.getRepository().save(entity);

        //     return entity;
    }

    protected Boolean checkValid() {
        return false;
    }

    /**
     * 启动流程
     *
     * @param variables
     * @return
     */
    protected ProcessInstance startProcessInstance(Map<String, Object> variables) {
        if (!StringUtils.isNotEmpty(entity.getObjId())) {
            ZkUtils.showExclamation("请先保存数据再提交！", "系统提示");
            return null;
        }
        FlowDocEntity one = (FlowDocEntity) this.baseService.getRepository().findOne(entity.getObjId());
        if (one.getProcessInstanceId() != null) {
            ZkUtils.showError("此单据已经提交", "提示");
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);
            return null;
        }

        if (checkValid() == false) {
            return null;
        }
        if (!entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId())) {
            ZkUtils.showExclamation("只能经办人提交流程！", "系统提示");
            return null;
        }

        this.entity = saveEntity(entity);

        String businessKey = entity.getClass().getSimpleName() + "."
                + entity.getObjId() + "."
                + entity.getDocNo() + "."
                + entity.getSubmitterName() + "."
                + entity.getSubmitter();
        String userId = CommonHelper.getActiveUser().getLogId();

//        Map<String, Object> variables = new HashMap<>();
//        variables.put("days", flowDocEntity.getDays());
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("userId", userId);
        variables.put("status", DocStatus.AUDITING.getIndex());
//        variables.put("businessKey", businessKey);


        ProcessInstance processInstance = processService.startProcessInstance(processDefinitionKey, businessKey, variables);

        entity = (FlowDocEntity) baseService.getRepository().findOne(entity.getObjId());

        if (entity.getStatus() != DocStatus.CLOSED.getIndex()) {
            entity.setStatus(DocStatus.AUDITING.getIndex());
        }
        entity.setProcessInstanceId(processInstance.getId());
        entity = (FlowDocEntity) baseService.getRepository().save(entity);
//        LoggerUtil.getLogger().info("更改实体对象状态为：" + DocStatus.AUDITING.getName());
//        LoggerUtil.getLogger().info("ProcessInstance:" + processInstance==null?"NULL":processInstance.getId() + "->" + entity.getObjId());
        showDialog();

        return processInstance;
    }

    /**
     * 审批流程
     *
     * @param outcome
     * @param comment
     * @throws IOException
     */
    protected void completeTask(String outcome, String comment) throws IOException {
//        Task task = processService.findTaskById(this.taskId);
//        if (task == null) {
//            return;
//        }
        // 在同意的时候做下一步的检查

        //if ("同意".equals(outcome)) {
        //    if (checkValid() == false) {
        //        return;
        //    }
        //}


        CustomComment cc = new CustomComment(outcome, comment);
        Map<String, Object> variables = new HashMap<>();
        if (outcome.equals("退回") || outcome.equals("驳回")) {
            variables.put("status", DocStatus.REJECT.getIndex());
        } else {
            variables.put("status", DocStatus.AUDITING.getIndex());
        }

//        if (outcome.equals("退回") || outcome.equals("驳回")) {
////            variables.put("status", DocStatus.REJECT.getIndex());
//            processService.setVariableByTaskId(this.getTaskId(), "status", DocStatus.REJECT.getIndex());
//        }
        this.entity = (FlowDocEntity) baseService.getRepository().save(entity);

        ProcessInstance processInstance = processService.completeTask(this.taskId, outcome, JsonHelper.bean2Json(cc), CommonHelper.getActiveUser().getLogId(), variables);
        entity = (FlowDocEntity) baseService.getRepository().findOne(entity.getObjId());
        if (processInstance == null) {
//            Boolean autoClose = (entity.getAutoClose() == null ? false : entity.getAutoClose());
            Boolean autoClose = CommonHelper.mapAutoClose.get(entity.getClass().getSimpleName());
            if (autoClose) {   // 如果流程自动关闭
                entity.setStatus(DocStatus.CLOSED.getIndex());  // 审批结束,并关闭流程

//                processService.findCommentByBusinessKey()
//                FlowDocEntity docEntity = (FlowDocEntity) Executions.getCurrent().getArg().get("entity");
//                List<UserEntity> users = (List<UserEntity>) Executions.getCurrent().getArg().get("users");
                String businessKey01 = entity.getClass().getSimpleName()
                        + "." + entity.getObjId()
                        + "." + entity.getDocNo()
                        + "." + entity.getSubmitterName();
                String businessKey02 = entity.getClass().getSimpleName()
                        + "." + entity.getObjId()
                        + "." + entity.getDocNo()
                        + "." + entity.getSubmitterName()
                        + "." + entity.getSubmitter();


                String businessKey = "";
                ProcessInstance pi = processService.findProcessInstanceByBusinessKey(businessKey01);
//                ProcessDefinition processDefinition = null;
                if (pi == null) {
                    HistoricProcessInstance historicProcessInstance = processService.findHistoricProcessInstanceByBusinessKey(businessKey01);
                    if (historicProcessInstance == null) {
                        pi = processService.findProcessInstanceByBusinessKey(businessKey02);
                        if (pi == null) {
                            historicProcessInstance = processService.findHistoricProcessInstanceByBusinessKey(businessKey02);
                            if (historicProcessInstance == null) {
                                ZkUtils.showError("找不到流程定义内容！", "警告！");
                            } else {
                                businessKey = businessKey02;
//                                processDefinition = processService.findProcessDefinition(historicProcessInstance.getProcessDefinitionId());
                            }
                        } else {
                            businessKey = businessKey02;
//                            processDefinition = processService.findProcessDefinition(processInstance.getProcessDefinitionId());
                        }
                    } else {
                        businessKey = businessKey01;
//                        processDefinition = processService.findProcessDefinition(historicProcessInstance.getProcessDefinitionId());
                    }
//            processInstance = processService.findProcessInstanceByBusinessKey(businessKey);
                } else {
                    businessKey = businessKey01;
//                    processDefinition = processService.findProcessDefinition(processInstance.getProcessDefinitionId());
                }


                List<Comment> comments = processService.findCommentByBusinessKey(businessKey);
//                if(comments==null)
//                    comments = processService.findCommentByBusinessKey(businessKey02);

                for (Comment cmt : comments) {
                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setFlowInstanceId(cmt.getProcessInstanceId());
                    commentEntity.setDoDate(cmt.getTime());
                    commentEntity.setUsername(userService.findOneWithUserId(cmt.getUserId()));
                    commentEntity.setUserId(entity.getSubmitter());
                    commentEntity.setResult(this.getBeanFromJson(cmt.getFullMessage()).getResult());
                    commentEntity.setMessage(this.getBeanFromJson(cmt.getFullMessage()).getMessage());
                    processService.saveComment(commentEntity);
                }

            } else {
                entity.setStatus(DocStatus.AUDITED.getIndex());  // 审批结束
            }

//            baseService.getRepository().save(entity);
        } else {
            if (outcome.equals("退回") || outcome.equals("驳回")) {
                entity.setStatus(DocStatus.REJECT.getIndex());  // 审批结束
//                baseService.getRepository().save(entity);
            } else {
                entity.setStatus(DocStatus.AUDITING.getIndex());  // 审批结束
            }
        }
        this.entity = (FlowDocEntity) baseService.getRepository().save(entity);

        if (handleForm != null)
            handleForm.detach();
//        if(win !=null)
//            win.detach();

        this.taskId = null;
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

    protected void showDialog() {
        Messagebox.show("操作成功！", "系统提示", Messagebox.OK,
                Messagebox.INFORMATION, new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        int clickedButton = (Integer) event.getData();
                        if (clickedButton == Messagebox.OK) {
                            // 取消自动关闭对话框
//                            if(win !=null)
//                                win.detach();
                            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
                        }
                    }
                });
    }

    @Command
    public void showHandleForm() {
//        baseService.getRepository().save(this.entity);

//        if(processService.findTaskById(taskId)==null){
//            ZkUtils.showInformation("任务已办理！","系统提示");
//            return;
//        }

        if (entity.getProcessInstanceId() == null) {
            ZkUtils.showError("单据无流程或已结束", "警告");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("outcomes", StringUtils.isNotBlank(taskId) ? processService.findOutComeListByTaskId(taskId) : outcomes);
        map.put("comments", StringUtils.isNotBlank(taskId) ? processService.findCommentByTaskId(taskId) : comments);
        map.put("users", this.getUsers());
        map.put("key", processDefinitionKey);
        handleForm = (Window) ZkUtils.createComponents("/views/flow/handle_form.zul", null, map);
        handleForm.doModal();
    }

    @Command
    public void deleteFlowDocEntity(@BindingParam("entity") FlowDocEntity entity) {
        // 草稿状态
        if (entity.getStatus() == 0) {
            baseService.getRepository().delete(entity);
            ZkUtils.showQuestion("确定删除？", "询问", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        // 用户点击的是确定按钮
                        baseService.getRepository().delete(entity);
                        ZkUtils.showInformation("删除成功", "系统提示");
                    } else {
                        // 用户点击的是取消按钮
                    }
                }
            });
        } else {
            ZkUtils.showExclamation("删除拒绝\n只允许删除处于草稿状态的单据！", "系统提示");
        }
    }

    @Command
    public void checkClientInfo(@BindingParam("evt") Event event) {
        if (win != null) {
            win.setHeight(CommonHelper.windowHeight + "px");
//            win.setWidth((CommonHelper.windowWidth - 100) + "px");
            if ((CommonHelper.windowWidth - 100) > 900) {
                win.setWidth((CommonHelper.windowWidth - 100) + "px");
            }
        } else {
            ZkUtils.showError("Win is Null", "error");
        }
    }

    protected String genDocNo(DocumentNoEntity entity) {

        String strToday = LocalDate.now().toString().replace("-", "");

        if (strToday.equalsIgnoreCase(entity.getLastNoDate())) {
            Integer currentSn = Integer.parseInt(entity.getLastNoSerialNumber()) + 1;
            return entity.getDocCode().toUpperCase()
                    + strToday
                    + StringHelper.genFixedString(currentSn, FIXED_LENGTH);
        } else {
            return entity.getDocCode().toUpperCase()
                    + strToday
                    + StringHelper.genFixedString(1, FIXED_LENGTH);
        }
    }

    /**
     * 提交任务，比如同意/驳回
     *
     * @param outcome
     * @param comment
     * @throws IOException
     */
    @GlobalCommand(GlobalCommandValues.COMMIT_TASK)
    @NotifyChange("*")
    public void commitTask(@BindingParam("outcome") String outcome, @BindingParam("comment") String comment) throws IOException {
        Map<String, Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
//        variables.put("outcome",outcome);
//        variables.put("comment",comment);
        completeTask(outcome, comment);
        showDialog();
    }

    @Command
    public void showFlowImage(@BindingParam("entity") FlowDocEntity entity) {
        logger.info("显示流程图");
//        Task task = processService.findTaskById(taskId);
        String imageViewerUrl = "/views/flow/audit_log_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("entity", entity);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }

    /**
     * 检查是否允许提交
     * 不允许提交：
     * 1、
     *
     * @return
     */
    protected Boolean chkCanCommit() {
        return StringUtils.isBlank(this.entity.getProcessInstanceId());
    }

    protected Boolean chkIsAuditing() {
        if (this.entity.getStatus() == DocStatus.AUDITING.getIndex()) {
            return true;
        } else {
            if (this.entity.getStatus() == DocStatus.REJECT.getIndex()
                    && this.entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId())) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 回收流程
     */
    @Command
    public void recoveryProcess() {
        processService.withdrawProcessInstance("");
    }

    /**
     * 作废单据,删除
     */
    @Command
    @NotifyChange("*")
    public void desert() {
        if (entity.getStatus().equals(DocStatus.REJECT.getIndex()) && CommonHelper.getActiveUser().getLogId().equals(entity.getSubmitter())) {
            entity.setStatus(DocStatus.OBSOLETE.getIndex());
            this.entity = (FlowDocEntity) baseService.getRepository().save(entity);
            ZkUtils.showInformation("单据已作废", "提示");
            processService.deleteProcessInstance(entity.getProcessInstanceId());
            processService.deleteHistoricProcessInstance(entity.getProcessInstanceId());
        } else {
            ZkUtils.showError("没有此操作权限", "提示");
        }
        entity.setProcessInstanceId(null);
        this.baseService.getRepository().save(entity);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }


    public Boolean checkCanEditSupply() {
        return false;
    }

    public Boolean checkCanEditRecycle() {
        return false;
    }

    public Boolean checkCanPrintReport() {
        return false;
    }

    public boolean checkCanExpressPrintReport() {
        return false;
    }

    public boolean checkCanShowFlowImage() {
        if (!this.entity.getStatus().equals(DocStatus.DRAFT.getIndex()) && this.entity.getProcessInstanceId() != null) {
            return true;
        }
        return false;
    }

//    private Boolean getButtonStatus(String property) {
//        if (this.entity.getClass().getSimpleName().equals("WarrantyMaintenanceEntity")) {
//            try {
//                Field[] fields = this.entity.getClass().getDeclaredFields();
//                System.out.println("变量长度:" + fields.length);
//                System.out.println(fields);
//
////                for(Field field : fields ){
////                    System.out.println(field.getName() + ":" + field.get(entity));
////                }
//                Field field = this.entity.getClass().getDeclaredField(property);
//                field.getBoolean(this.entity);
//                Object o = field.get(this.entity);
//                System.out.println(o.toString());
//                if (o == null)
//                    return false;
//                LoggerUtil.getLogger().info(o==null?"Null":"Not Null");
//                return (Boolean) o;
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        return false;
//    }

    public Boolean checkCanHandleTask() {
        if (StringUtils.isBlank(taskId))
            return false;
        return true;
//        vm.entity.status eq 1 or vm.entity.status eq -1
    }

    /**
     * 作废按钮
     *
     * @return
     */
    public boolean checkCanDesert() {
        if (entity.getStatus().equals(DocStatus.REJECT.getIndex()) && CommonHelper.getActiveUser().getLogId().equals(entity.getSubmitter())) {
            return true;
        }
        return false;
    }

    public Boolean checkCanEdit() {
//        if(entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId())
//        && entity.getStatus()==DocStatus.DRAFT.getIndex() || entity.getStatus()==DocStatus.REJECT.getIndex()){
//            return true;
//        }
//        return false;

        if (entity.getStatus() == DocStatus.DRAFT.getIndex()) {
            if (entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId())) {
//                this.readonly = false;
                return true;
            }
        } else if (entity.getStatus() == DocStatus.REJECT.getIndex()) {
            if (entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId()) && StringUtils.isNotBlank(this.taskId)) {
//                this.readonly = false;
                return true;
            }
        }
        return false;
    }

    public Boolean checkCanCommit() {
        if (entity.getStatus() == DocStatus.DRAFT.getIndex() && entity.getSubmitter().equals(CommonHelper.getActiveUser().getLogId())) {
            return true;
        }

        return false;
    }


    public List<String> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<String> outcomes) {
        this.outcomes = outcomes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public ProcessService getProcessService() {
        return processService;
    }

    public FlowDocEntity getEntity() {
        return entity;
    }

    public void setEntity(FlowDocEntity entity) {
        this.entity = entity;
        this.processDefinitionKey = entity.getClass().getSimpleName();

        if (StringUtils.isBlank(this.entity.getObjId())) {
            this.entity.setSubmitter(CommonHelper.getActiveUser().getLogId());
            this.entity.setSubmitterName(CommonHelper.getActiveUser().getUsername());
            this.entity.setSubmitterPhone(CommonHelper.getActiveUser().getPhone());

            this.entity.setCreaterId(CommonHelper.getActiveUser().getUserId());
            this.entity.setCreaterName(CommonHelper.getActiveUser().getUsername());
            this.entity.setCreatedTime(new Date());

        } else {
            this.entity.setModifierId(CommonHelper.getActiveUser().getUserId());
            this.entity.setModifierName(CommonHelper.getActiveUser().getUsername());
            this.entity.setModifiedTime(new Date());
        }
//        } else {
//            Task task = getTaskByBusinessId(entity.getObjId());
//            if (task != null)
//                this.taskId = getTaskByBusinessId(entity.getObjId()).getId();
//        }
        // 草稿状态 和 驳回状态，允许修改
        if (this.entity.getStatus() == DocStatus.DRAFT.getIndex() || this.entity.getStatus() == DocStatus.REJECT.getIndex()) {
            this.setReadonly(false);
//            this.canSubmit = true;
        } else { // 其余状态不允许修改
            this.setReadonly(true);
//            this.canSubmit = false;
        }
    }

    @Command
    public void printReport() {
        System.out.println("打印报表");
    }


    @GlobalCommand(GlobalCommandValues.REFRESH_FORM)
    @NotifyChange("*")
    public void refresh_form() {

    }


    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

//    public String getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }

    public String getBusinessId() {
        return businessId;
    }

    public Integer getStatus() {
        return status;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Boolean getEnableCommit() {
        return enableCommit;
    }

    public void setEnableCommit(Boolean enableCommit) {
        this.enableCommit = enableCommit;
    }

    public Boolean getEnableHandleTask() {
        return enableHandleTask;
    }

    public void setEnableHandleTask(Boolean enableHandleTask) {
        this.enableHandleTask = enableHandleTask;
    }

    public Boolean getEnableSave() {
        return enableSave;
    }

    public void setEnableSave(Boolean enableSave) {
        this.enableSave = enableSave;
    }

    public Boolean getEnableShowFlowImage() {
        return enableShowFlowImage;
    }

    public void setEnableShowFlowImage(Boolean enableShowFlowImage) {
        this.enableShowFlowImage = enableShowFlowImage;
    }
}
