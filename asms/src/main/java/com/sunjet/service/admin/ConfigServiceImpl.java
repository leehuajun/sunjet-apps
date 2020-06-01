package com.sunjet.service.admin;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.ConfigEntity;
import com.sunjet.repository.admin.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private CacheManager customCacheManager;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return configRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return configRepository;
    }

    @Override
    public ConfigEntity save(ConfigEntity configEntity) {
//        getRepositoryByEntity(configEntity.getClass().getSimpleName())
        return configRepository.save(configEntity);
    }

    /**
     * 获取所有参数设置列表
     *
     * @return
     */
    @Override
    public List<ConfigEntity> findAll() {
        return configRepository.findAll();
    }

    /**
     * 修改配置参数
     *
     * @param configEntity
     */
    @Override
    public void update(ConfigEntity configEntity) {
        configRepository.save(configEntity);
    }

    /**
     * 恢复默认值
     *
     * @param configEntity
     */
    @Override
    public void restore(ConfigEntity configEntity) {
        ConfigEntity model = configRepository.findOne(configEntity.getObjId());
        model.setConfigValue(model.getConfigDefaultValue());
        configRepository.save(model);
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    @Override
    public String getValueByKey(String key) {
        if (CacheManager.getConfigKeySet().size() <= 0) {
            customCacheManager.initConfig();
        }
        return CacheManager.getConfigValue("frigid_subsidy");
    }

    /**
     * 通过key获取一个configEntity
     *
     * @param key
     * @return
     */
    @Override
    public ConfigEntity findOne(String key) {
        return configRepository.getValueByKey(key);
    }
}