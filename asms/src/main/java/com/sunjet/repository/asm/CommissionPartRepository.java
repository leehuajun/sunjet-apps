package com.sunjet.repository.asm;

import com.sunjet.model.asm.CommissionPartEntity;
import com.sunjet.model.asm.GoOutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务委托单配件子行
 * Created by lhj on 16/9/17.
 */
public interface CommissionPartRepository extends JpaRepository<CommissionPartEntity, String>, JpaSpecificationExecutor<CommissionPartEntity> {
}
