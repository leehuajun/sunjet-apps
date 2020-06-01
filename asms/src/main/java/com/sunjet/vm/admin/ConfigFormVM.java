package com.sunjet.vm.admin;

import com.sunjet.model.admin.ConfigEntity;
import com.sunjet.service.admin.ConfigService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ConfigFormVM extends FormBaseVM {
    @WireVariable
    private ConfigService configService;
    private ConfigEntity configEntity = new ConfigEntity();

    public ConfigEntity getConfigEntity() {
        return configEntity;
    }

    public void setConfigEntity(ConfigEntity configEntity) {
        this.configEntity = configEntity;
    }

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            configEntity = (ConfigEntity) configService.getRepository().findOne(objId);
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    public void submit() {
        configEntity = configService.save(configEntity);
        cacheManager.initConfig();
        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", configEntity);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_CONFIG_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_CONFIG_LIST, null);
        }
    }
}
