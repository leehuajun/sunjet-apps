package com.sunjet.vm.flow;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FormBaseVM;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class AuditLogViewerVM extends FormBaseVM {
    @WireVariable
    private ProcessService processService;

    //    private String imagePath;
    private BufferedImage flowBufferedImage;
    private List<Comment> comments = new ArrayList<>();
    private Map<String, Object> coording;
    private Boolean show = true;
//    private Map<String, String> users = new HashMap<>();;

    //    private String url;
//
//    public String getUrl() {
//        return url;
//    }


//    @Override
//    public Map<String, String> getUsers() {
//        return users;
//    }

//    public void setUsers(Map<String, String> users) {
//        this.users = users;
//    }

//    public String getImagePath() {
//        return imagePath;
//    }

    public Map<String, Object> getCoording() {
        return coording;
    }

    public Boolean getShow() {
        return show;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public BufferedImage getFlowBufferedImage() {
        return flowBufferedImage;
    }

    //    @Init(superclass = true)
//    public void init() throws IOException {
//        Task task = (Task) Executions.getCurrent().getArg().get("task");
//
//        url = "/diagram-viewer/index.html?processInstanceId=" + task.getProcessInstanceId()
//                + "&processDefinitionId=" + task.getProcessDefinitionId();
//    }
    @Init(superclass = true)
    public void init() throws IOException {
        initUserList();
        FlowDocEntity docEntity = (FlowDocEntity) Executions.getCurrent().getArg().get("entity");

        List<UserEntity> users = (List<UserEntity>) Executions.getCurrent().getArg().get("users");
//        String processInstanceId = docEntity.getProcessInstanceId();
//        String businessKey = processService.findBusinessKeyByProcessInstanceId(docEntity.getProcessInstanceId());

        String businessKey01 = docEntity.getClass().getSimpleName()
                + "." + docEntity.getObjId()
                + "." + docEntity.getDocNo()
                + "." + docEntity.getSubmitterName();
        String businessKey02 = docEntity.getClass().getSimpleName()
                + "." + docEntity.getObjId()
                + "." + docEntity.getDocNo()
                + "." + docEntity.getSubmitterName()
                + "." + docEntity.getSubmitter();

        String businessKey = "";
        ProcessInstance processInstance = processService.findProcessInstanceByBusinessKey(businessKey01);
        ProcessDefinition processDefinition = null;
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = processService.findHistoricProcessInstanceByBusinessKey(businessKey01);
            if (historicProcessInstance == null) {
                processInstance = processService.findProcessInstanceByBusinessKey(businessKey02);
                if (processInstance == null) {
                    historicProcessInstance = processService.findHistoricProcessInstanceByBusinessKey(businessKey02);
                    if (historicProcessInstance == null) {
                        ZkUtils.showError("找不到流程定义内容！", "警告！");
                    } else {
                        businessKey = businessKey02;
                        processDefinition = processService.findProcessDefinition(historicProcessInstance.getProcessDefinitionId());
                    }
                } else {
                    businessKey = businessKey02;
                    processDefinition = processService.findProcessDefinition(processInstance.getProcessDefinitionId());
                }
            } else {
                businessKey = businessKey01;
                processDefinition = processService.findProcessDefinition(historicProcessInstance.getProcessDefinitionId());
            }
//            processInstance = processService.findProcessInstanceByBusinessKey(businessKey);
        } else {
            businessKey = businessKey01;
            processDefinition = processService.findProcessDefinition(processInstance.getProcessDefinitionId());
        }

//        ProcessDefinition processDefinition = processService.findProcessDefinitionByBusinessKey(businessKey);
//        if (processDefinition == null) {
//            businessKey = docEntity.getClass().getSimpleName()
//                    + "." + docEntity.getObjId()
//                    + "." + docEntity.getDocNo()
//                    + "." + docEntity.getSubmitterName()
//                    + "." + docEntity.getSubmitter();
//            processDefinition = processService.findProcessDefinitionByBusinessKey(businessKey);
//        }

//        ProcessInstance processInstance = processService.findProcessInstanceByBusinessKey(businessKey);
//        String tmpPath = "/image-caches/"+processDefinition.getId().replace(":","-")+".png";
////        String tmpPath = "/image-caches/"+ UUIDHelper.newUuid() +".png";
//        String imageFile = Executions.getCurrent().getDesktop().getWebApp().getRealPath(tmpPath);
//        File file = new File(imageFile);
//        if (!file.exists()) {
//            // 获取图片的输入流
//            InputStream inputStream = processService.findImageInputStream(processDefinition);
//            // 将图片生成本地目录下
////        File file = new File("/Users/lhj/" + resourceName);
//            FileUtils.copyInputStreamToFile(inputStream, file);
////            FileUtils.copy
////            Thread.sleep(5000);
//
//        }
//        this.imagePath = tmpPath;

        InputStream inputStream = processService.findImageInputStream(processDefinition);
        this.flowBufferedImage = ImageIO.read(inputStream);

        // 如果流程已关闭
        if (docEntity.getStatus() == DocStatus.CLOSED.getIndex()
                || docEntity.getStatus() == DocStatus.AUDITED.getIndex()
                || docEntity.getStatus() == DocStatus.WAITING_SETTLE.getIndex()
                || docEntity.getStatus() == DocStatus.SETTLING.getIndex()
                || docEntity.getStatus() == DocStatus.SETTLED.getIndex()) {
            comments = processService.findCommentByBusinessKey(businessKey);
        } else {  // 未关闭
            List<Task> tasks = processService.findTaskByBusinessKey(businessKey);
            if (tasks.size() > 1 && tasks.size() <= 0) {
                LoggerUtil.getLogger().info("tasks.size():" + tasks.size());
            } else {
                Task task = tasks.get(0);
                Map<String, Object> tmpCoording = processService.findCoordingByTask(task.getId());
//            添加红色的边框为: height  width  x+3  y+31
//            tmpCoording.replace("x",(Integer)tmpCoording.get("x") + 3);
//            tmpCoording.replace("y",(Integer)tmpCoording.get("y") + 31);
                comments = processService.findCommentByTaskId(task.getId());
                coording = tmpCoording;
                System.out.println("x:" + coording.get("x"));
                System.out.println("y:" + coording.get("y"));
                System.out.println("width:" + coording.get("width"));
                System.out.println("height:" + coording.get("height"));
//                }else{
//                    coording = new HashMap<>();
//                    coording.put("x",0);
//                    coording.put("y",0);
//                    coording.put("width",0);
//                    coording.put("height",0);
//                    show = false;
//                }
            }
        }


//        Task task = (Task) Executions.getCurrent().getArg().get("task");
//        ProcessDefinition processDefinition = (ProcessDefinition) Executions.getCurrent().getArg().get("processDefinition");
//        if(processDefinition==null && task==null){
//            ZkUtils.showError("流程定义实体和任务实体不能同时为空，请与管理员联系！","系统错误");
//            return;
//        }
//        if(processDefinition==null){
//            processDefinition = processService.findProcessDefinition(task.getProcessDefinitionId());
//        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


}
