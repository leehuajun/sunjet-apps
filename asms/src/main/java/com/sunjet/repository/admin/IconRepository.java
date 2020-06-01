package com.sunjet.repository.admin;

import com.sunjet.model.admin.IconEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * IconRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface IconRepository extends JpaRepository<IconEntity, String>, JpaSpecificationExecutor<IconEntity> {


}
