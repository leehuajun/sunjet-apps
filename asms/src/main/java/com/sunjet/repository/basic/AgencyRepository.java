package com.sunjet.repository.basic;

import com.sunjet.model.basic.AgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by lhj on 16/9/17.
 */
public interface AgencyRepository extends JpaRepository<AgencyEntity, String>, JpaSpecificationExecutor<AgencyEntity> {
    @Query("select ae from AgencyEntity ae where ae.code like ?1 or ae.name like ?1")
    List<AgencyEntity> findAllByKeyword(String keyword);

    @Query("select ae from AgencyEntity ae where ae.code like ?1 or ae.name=?1")
    AgencyEntity findOneByName(String name);

    @Query("select ae from AgencyEntity ae where ae.code=?1")
    AgencyEntity findOneByCode(String code);
}
