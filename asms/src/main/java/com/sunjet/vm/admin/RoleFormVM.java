package com.sunjet.vm.admin;

import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.model.helper.ResourceWrapper;
import com.sunjet.service.admin.PermissionService;
import com.sunjet.service.admin.ResourceService;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.admin.UserService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class RoleFormVM extends FormBaseVM {
    // 这个用于测试当前选中的checkbox
    @Wire("#pgrid")
    private Grid pgrid;

    @WireVariable
    private RoleService roleService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PermissionService permissionService;
    @WireVariable
    private ResourceService resourceService;

    private Map<String, PermissionEntity> permissionMap = new HashMap<String, PermissionEntity>();

    private List<EntityWrapper<UserEntity>> userWrappers = new ArrayList<>();
    private List<EntityWrapper<UserEntity>> userWrapperSelectedItems = new ArrayList<>();
    private List<EntityWrapper<PermissionEntity>> permissionWrapperSelectedItems = new ArrayList<>();
    private List<ResourceWrapper> resourceWrappers = new ArrayList<>();
    private List<EntityWrapper<ResourceWrapper>> resourceWrapperSelectedItems = new ArrayList<>();
    private RoleEntity roleEntity = new RoleEntity();

    private List<ResourceEntity> resourcesWithOperations = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            roleEntity = roleService.findOneWithUsersAndPermissionsById(objId);
        }
        permissionMap.clear();

        InitUserWrapperList();
        InitResourceWrapperList();
    }

    private void InitUserWrapperList() {
        userWrappers.clear();
        userWrapperSelectedItems.clear();
        List<UserEntity> users = userService.findAll();
        if (roleEntity != null) {
            for (UserEntity userEntity : users) {
                Boolean found = false;
                for (UserEntity model : roleEntity.getUsers()) {
                    if (model.getObjId().equals(userEntity.getObjId())) {
                        found = true;
                    }
                }
                if (found) {
                    EntityWrapper<UserEntity> userWrapper = new EntityWrapper<UserEntity>(userEntity, true);
                    userWrappers.add(userWrapper);
                    userWrapperSelectedItems.add(userWrapper);
                } else {
                    userWrappers.add(new EntityWrapper<UserEntity>(userEntity, false));
                }
            }
        } else {
//            userWrappers.addAll(users.stream().map(userEntity -> new EntityWrapper<UserEntity>(userEntity, false)).collect(Collectors.toList()));
            for (UserEntity userEntity : users) {
                userWrappers.add(new EntityWrapper<UserEntity>(userEntity, false));
            }
        }
    }

    private void InitResourceWrapperList() {
        resourceWrappers.clear();
//        permissionWrapperSelectedItems.clear();

        List<PermissionEntity> permissionEntities = permissionService.findAll();
        Collections.sort(permissionEntities);
        for (PermissionEntity model : permissionEntities) {
            permissionMap.put(model.getPermissionCode(), model);
        }

        List<ResourceEntity> resourceEntities = resourceService.findAll();
        List<ResourceEntity> resourcesWithOperations = resourceService.findAllWithOperations();

        if (roleEntity != null) {
            for (ResourceEntity resourceEntity : resourceEntities) {
                ResourceWrapper resourceWrapper = new ResourceWrapper();

                List<EntityWrapper<PermissionEntity>> permissionWrappers = new ArrayList<>();

                resourceWrapper.setEntity(resourceEntity);
                for (PermissionEntity permissionEntity : permissionEntities) {
//                    permissionEntity.getPermissionCode().split(":")[1]
                    if (permissionEntity.getPermissionCode().split(":")[0].equalsIgnoreCase(resourceEntity.getCode())) {
//                    if (permissionEntity.getPermissionCode().contains(resourceEntity.getCode() + ":")) {
                        //EntityWrapper<PermissionEntity> modelWrapper = new EntityWrapper<>(permissionEntity,false);
                        Boolean found = false;
                        for (PermissionEntity model : roleEntity.getPermissions()) {
                            if (model.getPermissionCode().equals(permissionEntity.getPermissionCode())) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
//                            EntityWrapper<PermissionEntity> permissionEntityWrapper = new EntityWrapper<>(permissionEntity, true);
                            permissionWrappers.add(new EntityWrapper<>(permissionEntity, true));
//                            permissionWrapperSelectedItems.add(permissionEntityWrapper);
                        } else {
                            permissionWrappers.add(new EntityWrapper<>(permissionEntity, false));
                        }
                        //resourceWrapper.setEntityWrapper(modelWrapper);
                    }
                }

                resourceWrapper.setEntityWrappers(permissionWrappers);
                resourceWrappers.add(resourceWrapper);
            }
        } else {
            for (ResourceEntity resourceEntity : resourceEntities) {
                ResourceWrapper resourceWrapper = new ResourceWrapper();

                List<EntityWrapper<PermissionEntity>> modelWrappers = new ArrayList<>();

                resourceWrapper.setEntity(resourceEntity);
                for (PermissionEntity permissionEntity : permissionEntities) {
                    if (permissionEntity.getPermissionCode().contains(resourceEntity.getCode() + ":")) {
                        //EntityWrapper<PermissionEntity> modelWrapper = new EntityWrapper<>(permissionEntity,false);
                        modelWrappers.add(new EntityWrapper<>(permissionEntity, false));
                        //resourceWrapper.setEntityWrapper(modelWrapper);
                    }
                }

                resourceWrapper.setEntityWrappers(modelWrappers);
                resourceWrappers.add(resourceWrapper);
            }
        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
    }

//    private void initCheckbox() {
//
//        Integer col_size = pgrid.getColumns().getChildren().size();
//        Integer row_size = pgrid.getRows().getChildren().size();
//
////        ZkUtils.showInformation("列:" + col_size + "    行:" + row_size,"测试");
//        List<Checkbox> chks = new ArrayList<>();
//        for (int i = 0; i < row_size; i++) {
//            int count = pgrid.getCell(i, 1).getChildren().size();
//            for (int j = 0; j < count; j++) {
//                Checkbox checkbox = (Checkbox) pgrid.getCell(i, 1).getChildren().get(j);
//
//            }
//        }
//    }

    @Command
    public void test() {
        Integer col_size = pgrid.getColumns().getChildren().size();
        Integer row_size = pgrid.getRows().getChildren().size();

//        ZkUtils.showInformation("列:" + col_size + "    行:" + row_size,"测试");
        List<Checkbox> chks = new ArrayList<>();
        for (int i = 0; i < row_size; i++) {
            int count = pgrid.getCell(i, 1).getChildren().size();
            for (int j = 0; j < count; j++) {
                Checkbox checkbox = (Checkbox) pgrid.getCell(i, 1).getChildren().get(j);
                if (checkbox.isChecked())
                    chks.add(checkbox);

            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chks.size(); i++) {
            if (i == chks.size() - 1)
                sb.append(chks.get(i).getValue().toString());
            else
                sb.append(chks.get(i).getValue() + " | ");
        }
        ZkUtils.showInformation("选中的checkbox数量:" + chks.size() + "\r\n" + sb.toString(), "测试");
    }

    @Command
    @NotifyChange("roleEntity")
    public void submit() {
        roleEntity.getUsers().clear();
        roleEntity.getPermissions().clear();

        for (EntityWrapper<UserEntity> userWrapper : userWrapperSelectedItems) {
            roleEntity.getUsers().add(userWrapper.getEntity());
        }
        List<String> chkValues = getCheckboxValue();
        for (String value : chkValues) {
            roleEntity.getPermissions().add(permissionMap.get(value));
        }


//        roleEntity.setUsersDesc(roleEntity.getUsers().toString().replace("[", "").replace("]", ""));
//        roleEntity.setPermissionsDesc(roleEntity.getPermissions().toString().replace("[", "").replace("]", ""));

        roleEntity = roleService.save(roleEntity);
        roleService.removeUsersFromRole(roleEntity.getObjId());
        roleService.addUsersToRole(roleEntity);
        List<UserEntity> users = userService.findAllWithRoles();
        for (UserEntity entity : users) {
            userService.save(entity);
        }

        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", roleEntity);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ROLE_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ROLE_LIST, null);
        }
    }

    /**
     * 获取当前所有选中的权限,permissionCode
     *
     * @return
     */
    private List<String> getCheckboxValue() {
        Integer col_size = pgrid.getColumns().getChildren().size();
        Integer row_size = pgrid.getRows().getChildren().size();
        List<String> chkValues = new ArrayList<>();

//        ZkUtils.showInformation("列:" + col_size + "    行:" + row_size,"测试");
        for (int i = 0; i < row_size; i++) {
            int count = pgrid.getCell(i, 1).getChildren().size();
            for (int j = 0; j < count; j++) {
                Checkbox checkbox = (Checkbox) pgrid.getCell(i, 1).getChildren().get(j);
                if (checkbox.isChecked()) {
                    chkValues.add(checkbox.getValue());
                }

            }
        }
        return chkValues;
    }
//    @Command
//    public void closeDialog(){
//        roleFormDialog.detach();
////        BindUtils.postGlobalCommand(null, null, "myGlobalCommand", null);
//    }


    public List<EntityWrapper<UserEntity>> getUserWrappers() {
        return userWrappers;
    }

    public void setUserWrappers(List<EntityWrapper<UserEntity>> userWrappers) {
        this.userWrappers = userWrappers;
    }

    public List<EntityWrapper<UserEntity>> getUserWrapperSelectedItems() {
        return userWrapperSelectedItems;
    }

    public void setUserWrapperSelectedItems(List<EntityWrapper<UserEntity>> userWrapperSelectedItems) {
        this.userWrapperSelectedItems = userWrapperSelectedItems;
    }

    public List<EntityWrapper<PermissionEntity>> getPermissionWrapperSelectedItems() {
        return permissionWrapperSelectedItems;
    }

    public void setPermissionWrapperSelectedItems(List<EntityWrapper<PermissionEntity>> permissionWrapperSelectedItems) {
        this.permissionWrapperSelectedItems = permissionWrapperSelectedItems;
    }

    public List<ResourceWrapper> getResourceWrappers() {
        return resourceWrappers;
    }

    public void setResourceWrappers(List<ResourceWrapper> resourceWrappers) {
        this.resourceWrappers = resourceWrappers;
    }

    public List<EntityWrapper<ResourceWrapper>> getResourceWrapperSelectedItems() {
        return resourceWrapperSelectedItems;
    }

    public void setResourceWrapperSelectedItems(List<EntityWrapper<ResourceWrapper>> resourceWrapperSelectedItems) {
        this.resourceWrapperSelectedItems = resourceWrapperSelectedItems;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public void setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }
}
