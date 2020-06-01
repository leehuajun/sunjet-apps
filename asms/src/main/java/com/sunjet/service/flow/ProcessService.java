package com.sunjet.service.flow;

import com.sunjet.model.flow.CommentEntity;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lhj on 16/10/18.
 */
public interface ProcessService {
    Deployment deploy(String deployName, String zipFilePath) throws FileNotFoundException;

    List<Deployment> findDeploymentList();

    List<ProcessDefinition> findProcessDefinitionList();

    List<ProcessDefinition> findProcessDefinitionLastVersionList();

    ProcessDefinition findProcessDefinitionByProcessInstanceId(String processInstanceId);

    InputStream findImageInputStream(ProcessDefinition processDefinition);

    void deleteProcessDefinitionByDeploymentId(String deploymentId);

    ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, String userId);

    ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, String userId, Boolean firstAuto);

    Task findFirstTaskByProcessInstanceIdAndUserId(String processInstanceId, String userId);

    List<Task> findPersonalTaskList(String name);

    List<Task> findGroupTaskList(String userId);

    List<Task> findAllTaskList(String userId);

    Task findTaskByProcessInstanceIdAndUserId(String processInstanceId, String userId);

    String findTaskFormKeyByTaskId(String taskId);

    String findFormUrl(String taskId);

    String findFormUrl(Task task);

    String findBusinessIdByTaskId(String taskId);

    String findBusinessKeyByTaskId(String taskId);

    String findBusinessKeyByProcessInstanceId(String processInstanceId);

    String findBusinessIdByProcessInstanceId(String processInstanceId);

    void setVariableByTaskId(String taskId, String key, Object value);

    List<String> findOutComeListByTaskId(String taskId);

    List<Task> findTaskByBusinessKey(String businessId);
//    ProcessInstance completeTask(String taskId, String outcome, String comment, String businessId, String userId);

    ProcessInstance completeTask(String taskId, String outcome, String comment, String userId, Map<String, Object> variables);

    List<Comment> findCommentByTaskId(String taskId);

    List<Comment> findCommentByBusinessKey(String businessKey);

    ProcessDefinition findProcessDefinitionByTaskId(String taskId);

    Map<String, Object> findCoordingByTask(String taskId);

    ProcessDefinition findProcessDefinition(String processDefinitionId);

    Task findTaskById(String taskId);

    List<ProcessDefinition> findProcessDefinitionLastVersionListByFilter(String keyword);

    List<Deployment> findDeploymentListByFilter(String keyword);

    List<ProcessInstance> getRunningProcessInstancesByUserId(String userId);

    List<HistoricProcessInstance> getClosedProcessInstancesByUserId(String userId);

    ProcessInstance findProcessInstanceByBusinessKey(String businessKey);

    ProcessDefinition findProcessDefinitionByBusinessKey(String businessKey);

    HistoricProcessInstance findHistoricProcessInstanceByBusinessKey(String businessKey);

    ProcessDefinition findProcessDefinitionByKey(String key);

    List<ProcessDefinition> findProcessDefinitionsByDeploymentId(String deploymentId);

    List<ProcessInstance> findProcessInstanceByIds(Set<String> processInstanceIds);


    void saveComment(CommentEntity commentEntity);

    List<CommentEntity> findAllByProcessInstanceId(String processInstanceId);

    void deleteProcessInstance(String processInstanceId);

    List<Task> findWaitingTasks();

    List<User> findUsers();

    void deleteHistoricProcessInstance(String s);

    /**
     * 撤回流程
     *
     * @param taskId
     */
    void withdrawProcessInstance(String taskId);
}
