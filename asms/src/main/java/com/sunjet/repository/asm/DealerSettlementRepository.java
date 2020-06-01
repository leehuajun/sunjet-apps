package com.sunjet.repository.asm;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.model.asm.ExpenseListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 三包费用结算单
 * Created by Administrator on 2016/10/26.
 */
public interface DealerSettlementRepository extends JpaRepository<DealerSettlementEntity, String>, JpaSpecificationExecutor<DealerSettlementEntity> {
    @Query("select u from DealerSettlementEntity u left join fetch u.items where u.objId=?1 ")
    public DealerSettlementEntity findOneById(String objId);

    @Query("select u from ExpenseListEntity u  where u.dealerSettlement.objId=?1 ")
    List<ExpenseListEntity> findExpenseListById(String objId);
}
