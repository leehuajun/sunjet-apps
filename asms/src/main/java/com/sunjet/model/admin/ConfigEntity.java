package com.sunjet.model.admin;


import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by lhj on 2015/9/6.
 * 系统配置信息实体
 */
@Entity
@Table(name = "SysConfigs")
public class ConfigEntity extends DocEntity {

    private static final long serialVersionUID = 7374987575666673359L;
    private String configKey;
    private String configValue;
    private String configDefaultValue;
    private String comment;

    @Column(name = "ConfigKey", length = 50)
    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }


    @Column(name = "ConfigName", length = 200)
    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Column(name = "ConfigDefaultValue", length = 200)
    public String getConfigDefaultValue() {
        return configDefaultValue;
    }

    public void setConfigDefaultValue(String configDefaultValue) {
        this.configDefaultValue = configDefaultValue;
    }

    @Column(name = "Comment", length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ConfigEntity() {
    }

    public ConfigEntity(String configKey, String configValue, String configDefaultValue) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.configDefaultValue = configDefaultValue;
    }

    public ConfigEntity(String configKey, String configValue, String configDefaultValue, String comment) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.configDefaultValue = configDefaultValue;
        this.comment = comment;
    }
}
