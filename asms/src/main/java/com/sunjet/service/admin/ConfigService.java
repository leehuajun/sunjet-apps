package com.sunjet.service.admin;


import com.sunjet.model.admin.ConfigEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface ConfigService extends BaseService {
    ConfigEntity save(ConfigEntity configEntity);

    List<ConfigEntity> findAll();

    void update(ConfigEntity configEntity);

    void restore(ConfigEntity configEntity);

    String getValueByKey(String key);

    ConfigEntity findOne(String key);
}
