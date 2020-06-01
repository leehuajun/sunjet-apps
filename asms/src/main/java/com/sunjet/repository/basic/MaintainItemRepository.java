package com.sunjet.repository.basic;


import com.sunjet.model.basic.MaintainItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 维修项目子项实体
 * Created by zyh on 2016/10/24.
 */
public interface MaintainItemRepository extends JpaRepository<MaintainItemEntity, String>, JpaSpecificationExecutor<MaintainItemEntity> {
}
