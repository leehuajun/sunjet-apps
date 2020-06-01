package com.sunjet.repository.asm;

import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.model.asm.FreightExpenseEntity;
import com.sunjet.model.asm.FreightSettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 运输费用结算单
 * Created by Administrator on 2016/10/26.
 */
public interface FreightSettlementRepository extends JpaRepository<FreightSettlementEntity, String>, JpaSpecificationExecutor<FreightSettlementEntity> {
    @Query("select u from FreightSettlementEntity u left join fetch u.items where u.objId=?1 ")
    public FreightSettlementEntity findOneById(String objId);

    @Query("select u from FreightExpenseEntity u  where u.freightSettlement.objId=?1 ")
    List<FreightExpenseEntity> findFreightExpenseById(String objId);
}
