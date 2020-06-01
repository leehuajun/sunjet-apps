package com.sunjet.repository.asm;

import com.sunjet.model.asm.ServiceProxyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务委托 dao
 * Created by lhj on 16/9/17.
 */
public interface ServiceProxyRepository extends JpaRepository<ServiceProxyEntity, String>, JpaSpecificationExecutor<ServiceProxyEntity> {
}
