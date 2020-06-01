package com.sunjet.repository.asm;

import com.sunjet.model.asm.PartExpenseListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 配件结算单子行
 * Created by Administrator on 2016/10/26.
 */
public interface PartExpenseListRepository extends JpaRepository<PartExpenseListEntity, String>, JpaSpecificationExecutor<PartExpenseListEntity> {
}
