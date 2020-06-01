package com.sunjet.repository.admin;

import com.sunjet.model.admin.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * ModuleRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, String>, JpaSpecificationExecutor<ScheduleJobEntity> {

}
