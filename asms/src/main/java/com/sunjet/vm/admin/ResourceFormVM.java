package com.sunjet.vm.admin;

import com.sunjet.model.admin.OperationEntity;
import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.service.admin.OperationService;
import com.sunjet.service.admin.PermissionService;
import com.sunjet.service.admin.ResourceService;
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

import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ResourceFormVM extends FormBaseVM {
    @WireVariable
    private ResourceService resourceService;
    @WireVariable
    private OperationService operationService;
    @WireVariable
    private PermissionService permissionService;

    private ResourceEntity resourceEntity = new ResourceEntity();
    private List<EntityWrapper<OperationEntity>> entityWrappers = new ArrayList<>();
    private List<EntityWrapper<OperationEntity>> entityWrapperSelectedItems = new ArrayList<>();

//  private List<ModuleEntity> moduleList = new ArrayList<>();

    private List<OperationEntity> operationEntityList = new ArrayList<>();

    public ResourceEntity getResourceEntity() {
        return resourceEntity;
    }

    public void setResourceEntity(ResourceEntity resourceEntity) {
        this.resourceEntity = resourceEntity;
    }

    public List<EntityWrapper<OperationEntity>> getEntityWrappers() {
        return entityWrappers;
    }

    public void setEntityWrappers(List<EntityWrapper<OperationEntity>> entityWrappers) {
        this.entityWrappers = entityWrappers;
    }

    public List<EntityWrapper<OperationEntity>> getEntityWrapperSelectedItems() {
        return entityWrapperSelectedItems;
    }

    public void setEntityWrapperSelectedItems(List<EntityWrapper<OperationEntity>> entityWrapperSelectedItems) {
        this.entityWrapperSelectedItems = entityWrapperSelectedItems;
    }

    @Init(superclass = true)
    public void init() {
        operationEntityList = operationService.findAll();
        Collections.sort(operationEntityList);

        if (StringUtils.isNotBlank(objId)) {
            ResourceEntity resource = resourceService.findOneWithOperationsById(objId);
            ResourceEntity rm = resourceService.findOneWithOperationsById(objId);
            System.out.println(rm.getOperationEntityList());
            resource.setOperationEntityList(new ArrayList<>());
            resource.setOperationEntityList(rm.getOperationEntityList());
            resourceEntity = resource;
            for (OperationEntity operationEntity : this.operationEntityList) {
                Boolean found = false;
                for (OperationEntity entity : resourceEntity.getOperationEntityList()) {
                    if (entity.getObjId().equals(operationEntity.getObjId())) {
                        found = true;
                    }
                }
                if (found) {
                    EntityWrapper<OperationEntity> modelWrapper = new EntityWrapper<>(operationEntity, true);
                    entityWrappers.add(modelWrapper);
                    entityWrapperSelectedItems.add(modelWrapper);
                } else {
                    entityWrappers.add(new EntityWrapper(operationEntity, false));
                }
            }
        } else {
            for (OperationEntity operationEntity : this.operationEntityList) {
                entityWrappers.add(new EntityWrapper(operationEntity, false));
            }
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存信息
     */
    @Command
//    @NotifyChange("resourceEntity")
    public void submit() {
        this.resourceEntity.getOperationEntityList().clear();
        for (EntityWrapper<OperationEntity> modelWrapper : entityWrapperSelectedItems) {
            this.resourceEntity.getOperationEntityList().add(modelWrapper.getEntity());
        }

//        resourceEntity.setOptDesc(resourceEntity.getOperationEntityList().toString().replace("[", "").replace("]", ""));
        permissionService.deleteAllByResourceCode(resourceEntity.getCode());
        for (OperationEntity operationEntity : resourceEntity.getOperationEntityList()) {
            permissionService.save(new PermissionEntity(operationEntity.getOptName(), resourceEntity.getName(), resourceEntity.getCode() + ":" + operationEntity.getOptCode(), operationEntity.getSeq()));

        }
        resourceEntity = resourceService.save(resourceEntity);

        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", resourceEntity);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RESOURCE_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RESOURCE_LIST, null);
        }
    }
}
