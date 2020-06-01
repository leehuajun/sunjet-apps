package com.sunjet.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.List;

/**
 * Created by lhj on 16/6/30.
 */

public class ProcessTest {
    private ApplicationContext context = null;

    @Before
    public void init() {
        context = new ClassPathXmlApplicationContext("classpath:spring/spring-config.xml");
    }

    @After
    public void destroy() {
        context = null;
    }

    @Test
    public void startProcess() {
        RuntimeService runtimeService = context.getBean(RuntimeService.class);
        String processDefinitionKey = "leaveProcess";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例ID:" + processInstance.getId());
        System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());
    }

    @Test
    public void queryMyTask() {
        TaskService taskService = context.getBean(TaskService.class);
        String assignee = "王五";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId());
            System.out.println("taskName:" + task.getName());
        }
    }

    @Test
    public void completeTask() {
        TaskService taskService = context.getBean(TaskService.class);
        String taskId = "52e547d9-9222-11e6-9671-566f685516af";
        taskService.complete(taskId);
        System.out.println("完成任务！");
    }

    @Test
    public void queryCurrentUser() {
        //      ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
//              .processDefinitionId(processInstanceId).singleResult();
//      Map<String, Object> processVariables = processInstance.getProcessVariables();
//      for(String key : processVariables.keySet()){
//        System.out.println(key + "----->" + processVariables.get(key));
//      }
        TaskService taskService = context.getBean(TaskService.class);
//        List<Task> tasks = taskService.createTaskQuery()
//                .processInstanceId("170517")
//                .list();
//        if(tasks.size()>1)
//            return;
//        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(tasks.get(0).getId());
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask("148806");

//      identityLinksForTask.size()
        for (IdentityLink identityLink : identityLinksForTask) {
            System.out.println(identityLink.getUserId() + ":" + identityLink.getGroupId());
        }

    }

    @Test
    public void queryTaskList() {
        TaskService taskService = context.getBean(TaskService.class);
        List<Task> tasks = taskService.createTaskQuery().list();
        System.out.println(tasks.size());
        for (Task task : tasks) {
            TaskEntity taskEntity = (TaskEntity) task;
            System.out.println(taskEntity.getAssignee() == null ? "NULL" : taskEntity.getAssignee()
                    + "--->"
                    + taskEntity.getCandidates() == null ? "NULL" : taskEntity.getCandidates().toString());
        }
    }
}
