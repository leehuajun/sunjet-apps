package com.sunjet.utils.quartz;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.service.admin.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时任务运行工厂类
 * <p>
 * Created by lhj on 16/6/20.
 */

public class SampleJob implements Job {
    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务名称 = " + scheduleJob.getJobName());
        System.out.println("用户数量 = " + userService.findAll().size());
    }
}
