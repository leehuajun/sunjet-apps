//package com.sunjet.utils.activiti.task;
//
///**
// * Created by lhj on 16/11/21.
// */
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.history.HistoricTaskInstance;
//import org.activiti.engine.impl.RepositoryServiceImpl;
//import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
//import org.activiti.engine.impl.pvm.PvmActivity;
//import org.activiti.engine.impl.pvm.PvmTransition;
//import org.activiti.engine.impl.pvm.process.ActivityImpl;
//import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
//import org.activiti.engine.impl.pvm.process.TransitionImpl;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
////import org.building.data.Result;
////
////import com.opensymphony.xwork2.ActionSupport;
//
///**
// * @author: Mars
// * @date: Aug 23, 2013 4:16:23 PM<br/>
// *        驳回任务
// */
//public class ModelRollBack {
//
//    private static final long serialVersionUID = 7802932113255596611L;
//    private Logger log = LogManager.getLogger(ModelRollBack.class);
//    private RuntimeService runtimeService;
//    private TaskService taskService;
//    private RepositoryService repositoryService;
//    private HistoryService historyService;
//    private String taskId;
////    private Result result;
//
////    public Result getResult() {
////        return result;
////    }
//
//    public void setTaskId(String taskId) {
//        this.taskId = taskId;
//    }
//
//    public void setRuntimeService(RuntimeService runtimeService) {
//        this.runtimeService = runtimeService;
//    }
//
//    public void setTaskService(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    public void setRepositoryService(RepositoryService repositoryService) {
//        this.repositoryService = repositoryService;
//    }
//
//    public void setHistoryService(HistoryService historyService) {
//        this.historyService = historyService;
//    }
//
//    @Override
//    public String execute() {
//        result = new Result();
//        try {
//            Map<String, Object> variables;
//            // 取得当前任务
//            HistoricTaskInstance currTask = historyService
//                    .createHistoricTaskInstanceQuery().taskId(taskId)
//                    .singleResult();
//            // 取得流程实例
//            ProcessInstance instance = runtimeService
//                    .createProcessInstanceQuery()
//                    .processInstanceId(currTask.getProcessInstanceId())
//                    .singleResult();
//            if (instance == null) {
//                result.setSuccess(Result.failure);
//                result.setError("流程已经结束");
//                log.error("流程已经结束");
//                return ERROR;
//            }
//            variables = instance.getProcessVariables();
//            // 取得流程定义
//            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//                    .getDeployedProcessDefinition(currTask
//                            .getProcessDefinitionId());
//            if (definition == null) {
//                result.setSuccess(Result.failure);
//                result.setError("流程定义未找到");
//                log.error("流程定义未找到");
//                return ERROR;
//            }
//            // 取得上一步活动
//            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
//                    .findActivity(currTask.getTaskDefinitionKey());
//            List<PvmTransition> nextTransitionList = currActivity
//                    .getIncomingTransitions();
//            // 清除当前活动的出口
//            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
//            List<PvmTransition> pvmTransitionList = currActivity
//                    .getOutgoingTransitions();
//            for (PvmTransition pvmTransition : pvmTransitionList) {
//                oriPvmTransitionList.add(pvmTransition);
//            }
//            pvmTransitionList.clear();
//
//            // 建立新出口
//            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
//            for (PvmTransition nextTransition : nextTransitionList) {
//                PvmActivity nextActivity = nextTransition.getSource();
//                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
//                        .findActivity(nextActivity.getId());
//                TransitionImpl newTransition = currActivity
//                        .createOutgoingTransition();
//                newTransition.setDestination(nextActivityImpl);
//                newTransitions.add(newTransition);
//            }
//            // 完成任务
//            List<Task> tasks = taskService.createTaskQuery()
//                    .processInstanceId(instance.getId())
//                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
//            for (Task task : tasks) {
//                taskService.complete(task.getId(), variables);
//                historyService.deleteHistoricTaskInstance(task.getId());
//            }
//            // 恢复方向
//            for (TransitionImpl transitionImpl : newTransitions) {
//                currActivity.getOutgoingTransitions().remove(transitionImpl);
//            }
//            for (PvmTransition pvmTransition : oriPvmTransitionList) {
//                pvmTransitionList.add(pvmTransition);
//            }
//
//            result.setMessage(Result.OK);
//            result.setSuccess(Result.complete);
//            return SUCCESS;
//        } catch (Exception e) {
//            result.setSuccess(Result.failure);
//            result.setError(e.getMessage());
//            log.error(e.getMessage());
//            return ERROR;
//        }
//    }
//}