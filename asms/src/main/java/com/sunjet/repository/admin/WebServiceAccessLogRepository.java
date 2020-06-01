package com.sunjet.repository.admin;

import com.sunjet.model.admin.WebServiceAccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface WebServiceAccessLogRepository extends JpaRepository<WebServiceAccessLogEntity, String>, JpaSpecificationExecutor<WebServiceAccessLogEntity> {

}
