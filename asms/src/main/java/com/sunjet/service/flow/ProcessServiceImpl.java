package com.sunjet.service.flow;

import com.sunjet.model.flow.CommentEntity;
import com.sunjet.repository.flow.CommentRepository;
import com.sunjet.utils.common.CommonHelper;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * Created by lhj on 16/10/18.
 */
@Service("processService")
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 部署流程定义
     *
     * @param deployName  部署对象名称
     * @param zipFilePath 流程定义zip文件（包括bpmn和png文件）
     * @return 部署对象
     * @throws FileNotFoundException
     */
    @Override
    public Deployment deploy(String deployName, String zipFilePath) throws FileNotFoundException {
        Deployment deploy = repositoryService.createDeployment().
                addZipInputStream(new ZipInputStream(new FileInputStream(zipFilePath)))
                .name(deployName)
                .deploy();

        return deploy;
    }

    /**
     * 查询部署对象信息
     * 查询部署对象信息，对应表（act_re_deployment）
     *
     * @return 所有部署对象集合
     */
    @Override
    public List<Deployment> findDeploymentList() {
        List<Deployment> list = repositoryService.createDeploymentQuery()//创建部署对象查询
                .orderByDeploymenTime().desc()//
                .list();
        return list;
    }

    /**
     * 查询流程定义的信息，对应表（act_re_procdef）
     *
     * @return 所有流程定义集合，包括老版本的流程定义
     */
    @Override
    public List<ProcessDefinition> findProcessDefinitionList() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
                .orderByProcessDefinitionVersion().desc()//
                .list();
        return list;
    }

    /**
     * 查询流程定义最后版本的信息，对应表（act_re_procdef）
     *
     * @return 最新版版本的流程定义集合
     */
    @Override
    public List<ProcessDefinition> findProcessDefinitionLastVersionList() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();
        return list;
    }

    /**
     * 根据流程定义Id，获取流程定义
     *
     * @param processDefinitionId
     * @return
     */
    @Override
    public ProcessDefinition findProcessDefinition(String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
    }

    /**
     * 根据任务id获取任务对象
     *
     * @param taskId
     * @return
     */
    @Override
    public Task findTaskById(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public List<ProcessDefinition> findProcessDefinitionLastVersionListByFilter(String keyword) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .processDefinitionNameLike("%" + keyword + "%")
                .list();
        return list;
    }

    @Override
    public List<Deployment> findDeploymentListByFilter(String keyword) {
        List<Deployment> list = repositoryService.createDeploymentQuery()//创建部署对象查询
                .orderByDeploymenTime().desc()//
                .deploymentNameLike("%" + keyword + "%")
                .list();
        return list;
    }


    /**
     * 根据流程实例id，获取流程定义
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public ProcessDefinition findProcessDefinitionByProcessInstanceId(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().
                processInstanceId(processInstanceId).
                singleResult();
        return findProcessDefinition(processInstance.getProcessDefinitionId());
    }

    /**
     * 使用流程定义id，获取流程图输入流
     *
     * @param processDefinition
     * @return
     */
    @Override
    public InputStream findImageInputStream(ProcessDefinition processDefinition) {
        List<String> list = repositoryService.getDeploymentResourceNames(processDefinition.getDeploymentId());


        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getDiagramResourceName());

//        String resourceName = "";
//        // 定义图片资源的名称
//        if (list != null && list.size() > 0) {
//            for (String name : list) {
//                if (name.indexOf(".png") >= 0) {
//                    resourceName = name;
//                }
//            }
//        }
//        // 获取图片的输入流
//        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return inputStream;
    }

    /**
     * 使用部署对象ID，删除流程定义
     *
     * @param deploymentId 部署对象
     */
    @Override
    public void deleteProcessDefinitionByDeploymentId(String deploymentId) {
        /**级联删除*/
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 启动流程实例
     *
     * @param businessKey          业务数据id
     * @param processDefinitionKey 流程定义key
     * @param userId               设置下一节点处理人
     * @return
     */
    @Override
    public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, String userId) {
        Map<String, Object> map = new HashMap<>();
//        map.put("businessKey", businessKey);
        map.put("userId", userId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
        return processInstance;
    }

    /**
     * 启动流程实例
     *
     * @param businessKey          业务数据id
     * @param processDefinitionKey 流程定义key
     * @param variables            流程属性
     * @return
     */
    @Override
    public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        return processInstance;
    }

    /**
     * 启动流程实例
     *
     * @param businessKey          业务数据id
     * @param processDefinitionKey 流程定义key
     * @param userId               设置下一节点处理人
     * @param firstAuto            是否自动执行第一个用户？
     * @return
     */
    @Deprecated
    @Override
    public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, String userId, Boolean firstAuto) {
        ProcessInstance processInstance = startProcessInstance(processDefinitionKey, businessKey, userId);
        if (firstAuto) {
            Task task = taskService.createTaskQuery()
                    .taskAssignee(userId)
                    .processInstanceId(processInstance.getId())
                    .singleResult();
            taskService.complete(task.getId());
        }
        return processInstance;
    }

    /**
     * 根据用户Id和流程实例Id，获取流程第一个任务对象
     *
     * @param userId
     * @param processInstanceId
     * @return
     */
    @Override
    public Task findFirstTaskByProcessInstanceIdAndUserId(String processInstanceId, String userId) {
        Task task = taskService.createTaskQuery()
                .taskAssignee(userId)
                .processInstanceId(processInstanceId)
                .singleResult();
//        taskService.complete(task.getId());
        return task;
    }

    /**
     * 使用当前用户名查询正在执行的个人任务表，获取当前个人任务的集合List<Task>
     *
     * @param userId 用户名
     * @return 当前用户的所有个人任务
     */
    @Override
    public List<Task> findPersonalTaskList(String userId) {
        List<Task> list = taskService.createTaskQuery()//
                .taskAssignee(userId)//指定个人任务查询
                .orderByTaskCreateTime().desc()
                .list();

        return list;
    }

    /**
     * 使用当前用户名查询正在执行的组任务表，获取当前组任务的集合List<Task>
     *
     * @param userId
     * @return 当前用户的所有组任务
     */
    @Override
    public List<Task> findGroupTaskList(String userId) {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser(CommonHelper.getActiveUser().getLogId())
                .orderByTaskCreateTime().desc()
                .list();
        return list;
    }

    /**
     * 获取当前人所有个人和组任务
     *
     * @param userId
     * @return
     */
    @Override
    public List<Task> findAllTaskList(String userId) {
        List<Task> personalTasks = this.findPersonalTaskList(userId);
        List<Task> groupTasks = this.findGroupTaskList(userId);

        List<Task> list = new ArrayList<>();

        list.addAll(personalTasks);
        list.addAll(groupTasks);

        //排序
        Collections.sort(list, new Comparator<Task>() {
            public int compare(Task task01, Task task02) {
                //比较每个ArrayList的第二个元素
                if (task01.getCreateTime().getTime() == task02.getCreateTime().getTime()) {
                    return 0;
                } else if (task01.getCreateTime().getTime() > task02.getCreateTime().getTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });


        Collections.reverse(list);

        return list;
    }

//    @Override
//    public List<TaskEntity> findAllTaskEntityList(String userId) {
//
//        taskService.createTaskQuery().
//    }

    /**
     * 根据流程实例id和处理人id，获取任务
     *
     * @param processInstanceId
     * @param userId
     */
    @Override
    public Task findTaskByProcessInstanceIdAndUserId(String processInstanceId, String userId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(userId)
                .singleResult();
    }

    /**
     * 使用任务ID，获取当前任务节点中对应的Form key中的连接的值
     *
     * @param taskId
     * @return
     */
    @Override
    public String findTaskFormKeyByTaskId(String taskId) {
        TaskFormData formData = formService.getTaskFormData(taskId);
        //获取Form key的值
        String url = formData.getFormKey();
        return url;
    }

    /**
     * 根据任务Id，获取流程实例的FormUrl
     *
     * @param taskId
     * @return
     */
    @Override
    public String findFormUrl(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return findFormUrl(task);
    }

    /**
     * 根据任务对象，获取流程实例的FormUrl
     *
     * @param task
     * @return
     */
    @Override
    public String findFormUrl(Task task) {
        String processDefinitionId = task.getProcessDefinitionId();
        String formUrl = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult().getDescription();
        return formUrl;
    }

    /**
     * 使用任务ID，查找业务ID
     *
     * @param taskId
     * @return 业务ID
     */
    @Override
    public String findBusinessIdByTaskId(String taskId) {
        String buniness_key = findBusinessKeyByTaskId(taskId);
        //5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
        String id = "";
        if (StringUtils.isNotBlank(buniness_key)) {
            //截取字符串，取buniness_key小数点的第2个值
            id = buniness_key.split("\\.")[1];
        }
        return id;
    }

    /**
     * 使用任务ID，找到业务Key
     *
     * @param taskId
     * @return
     */
    @Override
    public String findBusinessKeyByTaskId(String taskId) {
        //1：使用任务ID，查询任务对象Task
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2：使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //4：使用流程实例对象获取BUSINESS_KEY
        String buniness_key = pi.getBusinessKey();
        return buniness_key;
    }

    /**
     * 使用流程实例ID，找到业务Key
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public String findBusinessKeyByProcessInstanceId(String processInstanceId) {
        //3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //4：使用流程实例对象获取BUSINESS_KEY
        String buniness_key = pi.getBusinessKey();
        return buniness_key;
    }

    /**
     * 使用流程实例ID，查找业务ID
     *
     * @param processInstanceId
     * @return 业务ID
     */
    @Override
    public String findBusinessIdByProcessInstanceId(String processInstanceId) {
        String buniness_key = findBusinessKeyByProcessInstanceId(processInstanceId);
        //5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
        String id = "";
        if (StringUtils.isNotBlank(buniness_key)) {
            //截取字符串，取buniness_key小数点的第2个值
            id = buniness_key.split("\\.")[1];
        }
        return id;
    }

    /**
     * 根据任务Id，设置任务的流程变量
     *
     * @param taskId
     * @param key
     * @param value
     */
    @Override
    public void setVariableByTaskId(String taskId, String key, Object value) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String executionId = task.getExecutionId();
        runtimeService.setVariable(executionId, key, value);
    }

    /**
     * 已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
     *
     * @param taskId
     * @return
     */
    @Override
    public List<String> findOutComeListByTaskId(String taskId) {
        //返回存放连线的名称集合
        List<String> list = new ArrayList<String>();
        //1:使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2：获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
        //3：查询ProcessDefinitionEntiy对象
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //获取当前活动的id
        String activityId = pi.getActivityId();
        //4：获取当前的活动
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
        //5：获取当前活动完成之后连线的名称
        List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
        if (pvmList != null && pvmList.size() > 0) {
            for (PvmTransition pvm : pvmList) {
                String name = (String) pvm.getProperty("name");
                if (StringUtils.isNotBlank(name)) {
                    list.add(name);
                } else {
                    list.add("提交");
                }
            }
        }
        return list;
    }

    @Override
    public List<Task> findTaskByBusinessKey(String businessKey) {
        return taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .active()
                .list();

    }

    /**指定连线的名称完成任务*/
    /**
     * @param taskId  任务ID
     * @param outcome 连线的名称
     * @param comment 批注信息
     *                //     * @param businessId 业务ID
     * @param userId  用户Id
     * @return 流程实例
     */

    @Override
//    public ProcessInstance completeTask(String taskId, String outcome, String comment, String businessId, String userId,Map<String,Object> variables) {
    public ProcessInstance completeTask(String taskId, String outcome, String comment, String userId, Map<String, Object> variables) {
        /**
         * 1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
         */
        //使用任务ID，查询任务对象，获取流程流程实例ID
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();

        if (task == null) {
            System.out.println("任务对象为空，操作异常！");
            return null;
        }

        if (StringUtils.isBlank(task.getAssignee())) {   // 组任务，需要先拾取，然后才能办理
            taskService.claim(taskId, userId);
        }

        //获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        /**
         * 注意：添加批注的时候，由于Activiti底层代码是使用：
         * 		String userId = Authentication.getAuthenticatedUserId();
         CommentEntity comment = new CommentEntity();
         comment.setUserId(userId);
         所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，不过不添加审核人，该字段为null
         所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
         * */
//        CommonHelper.getActiveUser().getUsername();
//        Authentication.setAuthenticatedUserId(CommonHelper.getActiveUser().getUsername());
        Authentication.setAuthenticatedUserId(userId);
        taskService.addComment(taskId, processInstanceId, comment);
        /**
         * 2：如果连线的名称是“默认提交”，那么就不需要设置，如果不是，就需要设置流程变量
         * 在完成任务之前，设置流程变量，按照连线的名称，去完成任务
         流程变量的名称：outcome
         流程变量的值：连线的名称
         */
//        Map<String, Object> variables = new HashMap<String, Object>();
//        if (outcome != null && !outcome.equals("提交")) {
        if (StringUtils.isNotBlank(outcome)) {
            variables.put("outcome", outcome);
        }

        //3：使用任务ID，完成当前人的个人任务，同时流程变量
        taskService.complete(taskId, variables);
        //4：当任务完成之后，需要指定下一个任务的办理人（使用类）-----已经开发完成

        /**
         * 5：在完成任务之后，判断流程是否结束
         如果流程结束了，更新请假单表的状态从1变成2（审核中-->审核完成）
         */
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        return processInstance;
    }

    /**
     * 获取批注信息，传递的是当前任务ID，获取历史任务ID对应的批注
     *
     * @param taskId
     * @return
     */
    @Override
    public List<Comment> findCommentByTaskId(String taskId) {
        List<Comment> list = new ArrayList<Comment>();
        //使用当前的任务ID，查询当前流程对应的历史任务ID
        //使用当前任务ID，获取当前任务对象
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
//		//使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
//		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
//						.processInstanceId(processInstanceId)//使用流程实例ID查询
//						.list();
//		//遍历集合，获取每个任务ID
//		if(htiList!=null && htiList.size()>0){
//			for(HistoricTaskInstance hti:htiList){
//				//任务ID
//				String htaskId = hti.getId();
//				//获取批注信息
//				List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
//				list.addAll(taskList);
//			}
//		}
        list = taskService.getProcessInstanceComments(processInstanceId);
        return list;
    }

    /**使用请假单ID，查询历史批注信息*/
    /**
     * @param businessKey 实体类名称:objId
     * @return
     */
    @Override
    public List<Comment> findCommentByBusinessKey(String businessKey) {
//        //使用请假单ID，查询请假单对象
//        LeaveBill leaveBill = leaveBillDao.findLeaveBillById(id);
//        //获取对象的名称
//        String objectName = leaveBill.getClass().getSimpleName();
//        //组织流程表中的字段中的值
//        String objId = objectName+"."+id;

        /**1:使用历史的流程实例查询，返回历史的流程实例对象，获取流程实例ID*/
//		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//对应历史的流程实例表
//						.processInstanceBusinessKey(objId)//使用BusinessKey字段查询
//						.singleResult();
//		//流程实例ID
//		String processInstanceId = hpi.getId();
        /**2:使用历史的流程变量查询，返回历史的流程变量的对象，获取流程实例ID（在流程变量中设置businessKey）*/
//        HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()//对应历史的流程变量表
//                .variableValueEquals("businessKey", businessKey)//使用流程变量的名称和流程变量的值查询
//                .singleResult();
        /**2.使用历史流程属性查询，返回历史流程实例，获取流程实例ID ( 在流程启动时设置businessKey ) */
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        List<Comment> list = null;
        if(hpi !=null){
            String processInstanceId = hpi.getId();
            list= taskService.getProcessInstanceComments(processInstanceId);
        }
        //流程实例ID


//        String processInstanceId = hvi.getProcessInstanceId();

        return list;
    }

    /**
     * 获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
     *
     * @param taskId
     * @return
     */
    @Override
    public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
        //使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
        //查询流程定义的对象
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef
                .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                .singleResult();
        return pd;
    }

    /**
     * 查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中
     * map集合的key：表示坐标x,y,width,height
     * map集合的value：表示坐标对应的值
     */
    @Override
    public Map<String, Object> findCoordingByTask(String taskId) {
        //存放坐标
        Map<String, Object> map = new HashMap<String, Object>();
        //使用任务ID，查询任务对象
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //获取流程定义的ID
        String processDefinitionId = task.getProcessDefinitionId();
        //获取流程定义的实体对象（对应.bpmn文件中的数据）
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //获取当前活动的ID
        String activityId = pi.getActivityId();
        //获取当前活动对象
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
        //获取坐标
        map.put("x", activityImpl.getX());
        map.put("y", activityImpl.getY());
        map.put("width", activityImpl.getWidth());
        map.put("height", activityImpl.getHeight());
        return map;
    }

    /**
     * 用户参与过，且未结束的流程列表
     * involvedUser()会通过identity_link搜索指定用户参与过的所有流程实例，可能是“发起人”，“任务的负责人”，“任务的候选人”等等。默认能搜到的流程实例都是未完成的。
     *
     * @param userId
     * @return
     */
    @Override
    public List<ProcessInstance> getRunningProcessInstancesByUserId(String userId) {
        return runtimeService.createProcessInstanceQuery().involvedUser(userId).list();
    }

    /**
     * 用户参与过，已结束的流程列表
     * involvedUser()会通过identity_link搜索指定用户参与过的所有流程实例，可能是“发起人”，“任务的负责人”，“任务的候选人”等等。默认能搜到的流程实例都是未完成的。
     *
     * @param userId
     * @return
     */
    @Override
    public List<HistoricProcessInstance> getClosedProcessInstancesByUserId(String userId) {
        return historyService.createHistoricProcessInstanceQuery().involvedUser(userId).list();
    }

    /**
     * 根据BusinessKey查找ProcessInstance
     *
     * @param businessKey
     * @return
     */
    @Override
    public ProcessInstance findProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    @Override
    public ProcessDefinition findProcessDefinitionByBusinessKey(String businessKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey(businessKey).singleResult();
        String processDefinitionId = "";
        //  已关闭
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return processDefinition;
    }

    @Override
    public ProcessDefinition findProcessDefinitionByKey(String key) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
    }

    @Override
    public List<ProcessDefinition> findProcessDefinitionsByDeploymentId(String deploymentId) {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).list();
        return definitions;
    }

    @Override
    public HistoricProcessInstance findHistoricProcessInstanceByBusinessKey(String businessKey) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        return historicProcessInstance;
    }

    @Override
    public List<ProcessInstance> findProcessInstanceByIds(Set<String> processInstanceIds) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceIds(processInstanceIds)
                .list();
    }

    @Override
    public void saveComment(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
    }

    @Override
    public List<CommentEntity> findAllByProcessInstanceId(String processInstanceId) {
        return commentRepository.findAllByProcessInstanceId(processInstanceId);
    }

    /**
     * 中止正在运行的流程实例
     *
     * @param processInstanceId
     */
    @Override
    public void deleteProcessInstance(String processInstanceId) {
        runtimeService.deleteProcessInstance(processInstanceId, "中止流程");
    }

    @Override
    public List<Task> findWaitingTasks() {
        List<Task> tasks = taskService.createTaskQuery().list();
        return tasks;
    }

//  @Override
//    public String findCurrentUserOrGroup(String processInstanceId) {
////      ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
////              .processDefinitionId(processInstanceId).singleResult();
////      Map<String, Object> processVariables = processInstance.getProcessVariables();
////      for(String key : processVariables.keySet()){
////        System.out.println(key + "----->" + processVariables.get(key));
////      }
//      List<Task> tasks = taskService.createTaskQuery()
//              .processInstanceId(processInstanceId)
//              .list();
//      if(tasks.size()>1)
//        return "多用户任务";
//      List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(tasks.get(0).getId());
//
////      identityLinksForTask.size()
//      for (IdentityLink identityLink : identityLinksForTask){
//        System.out.println(identityLink.getUserId() + ":" + identityLink.getGroupId());
//      }
//      return null;
//    }

    @Override
    public List<User> findUsers() {
        List<User> users = identityService.createUserQuery().list();
        return users;
    }


    /**
     * 删除流程和记录
     *
     * @param s
     */
    @Override
    public void deleteHistoricProcessInstance(String s) {
        historyService.deleteHistoricProcessInstance(s);
    }


    /**
     * 撤回流程
     *
     * @param taskId
     */
    @Override
    public void withdrawProcessInstance(String taskId) {
        Map<String, Object> variables;
        //当前任务的ID

        // 取得当前任务

        HistoricTaskInstance currTask = historyService
                .createHistoricTaskInstanceQuery().taskId(taskId)
                .singleResult();
        System.out.println("流程ProcessInstanceId:" + currTask.getProcessInstanceId());

        // 取得流程实例
        ProcessInstance instance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(currTask.getProcessInstanceId())
                .singleResult();
        variables = instance.getProcessVariables();
        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask
                        .getProcessDefinitionId());
        if (definition == null) {
            System.out.println("流程定义未找到");

        }

        // 取得下一步活动
        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                .findActivity(currTask.getTaskDefinitionKey());
        System.out.println(currActivity);
        List<PvmTransition> nextTransitionList = currActivity
                .getOutgoingTransitions();

        System.out.println("下一步活动:" + nextTransitionList);

        for (PvmTransition nextTransition : nextTransitionList) {
            PvmActivity nextActivity = nextTransition.getDestination();
            List<HistoricTaskInstance> completeTasks = historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(nextActivity.getId()).finished()
                    .list();
            System.out.println(completeTasks);
            int finished = completeTasks.size();
            if (finished > 0) {
                System.out.println("存在已经完成的下一步，流程不能取回");
            }

            List<Task> nextTasks = taskService.createTaskQuery().processInstanceId(instance.getId())
                    .taskDefinitionKey(nextActivity.getId()).list();
            System.out.println("下一步流程:" + nextTasks);
            for (Task nextTask : nextTasks) {
                //取活动，清除活动方向
                List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
                List<PvmTransition> pvmTransitionList = nextActivity
                        .getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    oriPvmTransitionList.add(pvmTransition);
                }
                pvmTransitionList.clear();
                //建立新方向
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                        .findActivity(nextTask.getTaskDefinitionKey());
                TransitionImpl newTransition = nextActivityImpl
                        .createOutgoingTransition();
                newTransition.setDestination(currActivity);
                //完成任务
                taskService.complete(nextTask.getId(), variables);
                historyService.deleteHistoricTaskInstance(nextTask.getId());
                //恢复方向
                currActivity.getIncomingTransitions().remove(newTransition);
                List<PvmTransition> pvmTList = nextActivity
                        .getOutgoingTransitions();
                pvmTList.clear();
                for (PvmTransition pvmTransition : oriPvmTransitionList) {
                    pvmTransitionList.add(pvmTransition);
                }

            }
            historyService.deleteHistoricTaskInstance(currTask.getId());


        }

    }


}
