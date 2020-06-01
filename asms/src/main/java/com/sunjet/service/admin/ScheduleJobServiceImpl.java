package com.sunjet.service.admin;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.repository.admin.ScheduleJobRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return scheduleJobRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return scheduleJobRepository;
    }

    /**
     * 获取所有的 ScheduleJobEntity 列表
     *
     * @return
     */
    @Override
    public List<ScheduleJobEntity> findAll() {
        return scheduleJobRepository.findAll();
    }

    @Override
    public void save(ScheduleJobEntity job) {
        scheduleJobRepository.save(job);
    }

    @Override
    public void runAll() throws SchedulerException, ClassNotFoundException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        if(scheduler.isShutdown())
//            scheduler.start();
        /** schedulerFactoryBean 由spring创建注入 */
        // 获取任务信息数据
        List<ScheduleJobEntity> jobList = scheduleJobRepository.findAll();
        for (ScheduleJobEntity job : jobList) {
            if (job.getEnabled() == true)
                run(job);
        }
    }

    @Override
    public void run(ScheduleJobEntity job) throws SchedulerException, ClassNotFoundException {
        /** schedulerFactoryBean 由spring创建注入 */
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        // 获取任务信息数据
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        // 获取 trigger,即在spring配置文件众定义的 bean id="myTrigger"
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 不存在,创建一个
//      Class.forName()
        if (trigger == null) {
//            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(job.getJobClass())).withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            // 按新的 cronExpression 表达式构建一个新的 trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // trigger 已存在,那么更新相应的定时设置
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            // 按新的 cronExpression 表达式重新构建 trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的 trigger 重新设置 job 执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    @Override
    public void pauseAll() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        if(!scheduler.isShutdown())
//            scheduler.shutdown();
        scheduler.pauseAll();

        /** schedulerFactoryBean 由spring创建注入 */
        // 获取任务信息数据
//        List<ScheduleJobEntity> jobList = scheduleJobRepository.findAll();
//        for (ScheduleJobEntity job : jobList) {
//            pause(job);
//        }
    }

    @Override
    public void resumeAll() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeAll();
    }

    @Override
    public void pause(ScheduleJobEntity job) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        try {
            System.out.println("停止定时任务:" + job.getJobName());
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(ScheduleJobEntity job) {


    }

    @Override
    public void delete(ScheduleJobEntity job) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runSingle(ScheduleJobEntity job) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}