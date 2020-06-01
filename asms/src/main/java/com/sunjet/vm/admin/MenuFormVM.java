package com.sunjet.vm.admin;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.service.admin.MenuService;
import com.sunjet.service.admin.PermissionService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.MenuTreeUtil;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class MenuFormVM extends FormBaseVM {
    @Wire
    private Window menuFormDialog;
    @WireVariable
    private PermissionService permissionService;
    @WireVariable
    private MenuService menuService;

    private MenuEntity menuEntity;
    private MenuEntity newEntity = new MenuEntity();
    private TreeModel treeModel;
    private ActiveUser activeUser;
    private PermissionEntity permissionEntity;
    private String permissionCode;
    private MenuEntity parentMenu;
    //    private List<ResourceEntity> resourceEntityList = new ArrayList<>();
//    private List<AccessEntity> accessEntityList = new ArrayList<>();
    private List<PermissionEntity> permissionEntityList = new ArrayList<>();

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public MenuEntity getMenuEntity() {
        return menuEntity;
    }

    public void setMenuEntity(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
    }

    public MenuEntity getNewEntity() {
        return newEntity;
    }

    public void setNewEntity(MenuEntity newEntity) {
        this.newEntity = newEntity;
    }


    public PermissionEntity getPermissionEntity() {
        return permissionEntity;
    }

    public void setPermissionEntity(PermissionEntity permissionEntity) {
        this.permissionEntity = permissionEntity;
    }

    public MenuEntity getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(MenuEntity parentMenu) {
        this.parentMenu = parentMenu;
    }


    public List<PermissionEntity> getPermissionEntityList() {
        return permissionEntityList;
    }

    public void setPermissionEntityList(List<PermissionEntity> permissionEntityList) {
        this.permissionEntityList = permissionEntityList;
    }

    @Init(superclass = true)
    public void init() {
//        MenuEntity menu = (MenuEntity)this.getEntity();
        if (StringUtils.isNotBlank(objId)) {
            menuEntity = menuService.findOneById(objId);
            parentMenu = (MenuEntity) menuEntity.getParent();
//      permissionEntity = menuEntity.getPermissionEntity();
//            BeanUtils.copyProperties(menuEntity,newEntity);
//            System.out.println(newEntity.getPermissionEntity()==null?"PermissionEntity is:NULL":"PermissionEntity is:Not NULL");
            //System.out.println(newEntity.getPermissionEntity().toString());
        } else {
            menuEntity = new MenuEntity();
            menuEntity.setParent(parentMenu);
//      menuEntity.setPermissionEntity(permissionEntity);
//            parentMenu = new MenuEntity();
//            permissionEntity = new PermissionEntity();
            //parentMenuEntity = new MenuEntity();
        }
//        moduleList = systemService.getModuleList();
//        resourceEntityList = systemService.getAllResources();
//        accessEntityList = systemService.getAllAccesses();
        permissionEntityList = permissionService.findAll();
        if (menuEntity.getPermissionCode() != null) {
            for (PermissionEntity model : permissionEntityList) {
                if (menuEntity.getPermissionCode() != null) {
                    if (menuEntity.getPermissionCode().equals(model.getPermissionCode())) {
                        this.permissionEntity = model;
                        break;
                    }
                }
            }
        }
        this.activeUser = CommonHelper.getActiveUser();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAllByUserId(activeUser.getLogId())));

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 初始化用户信息
     */
    @Command
    @NotifyChange({"menuEntity"})
    public void InitData() {
//        this.userEntity = systemService.findUserEntityById()
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("menuEntity")
    public void submit() {
        //BeanUtils.copyProperties(newEntity,menuEntity);
        menuEntity.setParent(parentMenu);
        if (permissionEntity != null) {
            menuEntity.setPermissionCode(permissionEntity.getPermissionCode());
            menuEntity.setPermissionName(permissionEntity.toString());
        }


        menuEntity = menuService.save(menuEntity);
//        BindUtils.postNotifyChange(null, null, menuEntity, "permissionCode");
//        BindUtils.postNotifyChange(null, null, menuEntity, "permissionName");
//        BindUtils.postNotifyChange(null, null, menuEntity, "parent");
//        currentMenu = menuEntity;
//        formDialog.detach();
//    menuEntity.setPermissionEntity(permissionEntity);
//        if(menuEntity.getParent()!=null){
//            BeanUtils.copyProperties(parentMenu,menuEntity.getParent());
//        }else{
//            menuEntity.setParent(parentMenu);
//        }
//        if(menuEntity.getPermissionEntity()!=null){
//            BeanUtils.copyProperties(permissionEntity,menuEntity.getPermissionEntity());
//        }else{
//            menuEntity.setPermissionEntity(permissionEntity);
//        }
        //  @global-command('saveMenuEntity',model=vm.menuEntity,action=vm.action)
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", menuEntity);
//        paramMap.put("action", this.getAction());
//        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.SAVE_MENU, paramMap);

        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", menuEntity);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MENU_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MENU_LIST, null);
        }
//        BindUtils.postGlobalCommand(null,null,);
        //systemService.saveUserEntity(this.userEntity);
    }

//    @Command
//    public void closeDialog() {
//        menuFormDialog.detach();
////        BindUtils.postGlobalCommand(null, null, "myGlobalCommand", null);
//    }

    @Command
    @NotifyChange({"parentMenu"})
    public void changeParent(@BindingParam("parent") MenuEntity parent, @BindingParam("component") Component
            component) {
//        menuEntity.setParent(parent);
        parentMenu = parent;
        ((Bandbox) component).close();
    }
}
