package com.sunjet.repository.basic;

import com.sunjet.model.basic.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 配件
 * Created by zyh on 2016/10/24.
 */
public interface PartRepository extends JpaRepository<PartEntity, String>, JpaSpecificationExecutor<PartEntity> {

    @Query("select p from PartEntity p where p.enabled = true and (p.code like ?1 or p.name like ?1)")
    List<PartEntity> findAllByKeyword(String productNo);

    @Query("select p from PartEntity p where p.enabled = true and p.code like ?1")
    List<PartEntity> findAllByCode(String code);
}
