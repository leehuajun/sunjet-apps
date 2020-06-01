package com.sunjet.repository.asm;

import com.sunjet.model.asm.SupplyDisItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface SupplyDisItemRepository extends JpaRepository<SupplyDisItemEntity, String>, JpaSpecificationExecutor<SupplyDisItemEntity> {
}
