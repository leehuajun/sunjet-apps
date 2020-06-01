package com.sunjet.repository.basic;

import com.sunjet.model.basic.MaintainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 维修项目 主实体
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public interface MaintainRepository extends JpaRepository<MaintainEntity, String>, JpaSpecificationExecutor<MaintainEntity> {
    @Query("select me from MaintainEntity me where me.code like ?1 or me.name like ?1 order by me.code asc")
    List<MaintainEntity> findAllByFilter(String keyword);
}
