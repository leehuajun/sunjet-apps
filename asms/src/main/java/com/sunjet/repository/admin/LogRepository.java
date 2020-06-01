package com.sunjet.repository.admin;

import com.sunjet.model.admin.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * ConfigRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface LogRepository extends JpaRepository<LogEntity, String>, JpaSpecificationExecutor<LogEntity> {


}
