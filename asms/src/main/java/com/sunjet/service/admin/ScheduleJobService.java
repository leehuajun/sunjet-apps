package com.sunjet.service.admin;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.service.base.BaseService;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Created by lhj on 16/6/20.
 */
public interface ScheduleJobService extends BaseService {

    List<ScheduleJobEntity> findAll();

    void save(ScheduleJobEntity job);

    void runAll() throws SchedulerException, ClassNotFoundException;

    void run(ScheduleJobEntity job) throws SchedulerException, ClassNotFoundException;

    void pause(ScheduleJobEntity job);

    void edit(ScheduleJobEntity job);

    void delete(ScheduleJobEntity job);

    void runSingle(ScheduleJobEntity job);

    void pauseAll() throws SchedulerException;

    void resumeAll() throws SchedulerException;
}
