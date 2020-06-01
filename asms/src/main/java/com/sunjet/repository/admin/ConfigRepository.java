package com.sunjet.repository.admin;

import com.sunjet.model.admin.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * ConfigRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface ConfigRepository extends JpaRepository<ConfigEntity, String>, JpaSpecificationExecutor<ConfigEntity> {
    @Query("select ce from ConfigEntity ce where ce.configKey =?1")
    ConfigEntity getValueByKey(String key);

}
