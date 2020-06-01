package com.sunjet.repository.asm;

import com.sunjet.model.asm.ExpenseListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务站结算单子行
 * Created by Administrator on 2016/10/26.
 */
public interface ExpenseListRepository extends JpaRepository<ExpenseListEntity, String>, JpaSpecificationExecutor<ExpenseListEntity> {
}
