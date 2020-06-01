package com.sunjet.model.helper;


import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.admin.ResourceEntity;

import java.util.List;

/**
 * @author lhj
 * @create 2016-02-25 下午5:26
 */
public class ResourceWrapper {
    private ResourceEntity entity;
    private List<EntityWrapper<PermissionEntity>> entityWrappers;

    public ResourceEntity getEntity() {
        return entity;
    }

    public void setEntity(ResourceEntity model) {
        this.entity = model;
    }

    public List<EntityWrapper<PermissionEntity>> getEntityWrappers() {
        return entityWrappers;
    }

    public void setEntityWrappers(List<EntityWrapper<PermissionEntity>> entityWrappers) {
        this.entityWrappers = entityWrappers;
    }

    public ResourceWrapper() {
    }

    public ResourceWrapper(ResourceEntity entity, List<EntityWrapper<PermissionEntity>> entityWrappers) {
        this.entity = entity;
        this.entityWrappers = entityWrappers;
    }
}
