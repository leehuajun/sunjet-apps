package com.sunjet.repository.asm;

import com.sunjet.model.asm.FreightExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 运费结算单子行
 * Created by Administrator on 2016/10/26.
 */
public interface FreightExpenseRepository extends JpaRepository<FreightExpenseEntity, String>, JpaSpecificationExecutor<FreightExpenseEntity> {
}
