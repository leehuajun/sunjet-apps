package com.sunjet.utils.quartz;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.service.admin.RoleService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时任务运行工厂类
 * <p>
 * Created by lhj on 16/6/20.
 */
public class SampleJob2 implements Job {
    @Autowired
    private RoleService roleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务名称 = " + scheduleJob.getJobName());
        System.out.println("角色数量 = " + roleService.findAll().size());
    }
}
