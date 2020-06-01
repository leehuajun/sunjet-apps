package com.sunjet.repository.asm;

import com.sunjet.model.asm.GoOutEntity;
import com.sunjet.model.asm.ServiceProxyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务委托单出外子行
 * Created by lhj on 16/9/17.
 */
public interface GoOutRepository extends JpaRepository<GoOutEntity, String>, JpaSpecificationExecutor<GoOutEntity> {
}
