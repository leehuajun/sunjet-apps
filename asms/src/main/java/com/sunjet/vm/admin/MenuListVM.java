package com.sunjet.vm.admin;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.service.admin.MenuService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.CommonTreeUtil;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.MenuTreeUtil;
import com.sunjet.vm.base.ListBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class MenuListVM extends ListBaseVM {
    @WireVariable
    private MenuService menuService;

    private TreeModel treeModel;
//    private MenuEntity currentMenu;

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    @Init
    public void init() {
        this.setEnableSearch(false);
        this.setBaseService(menuService);
        this.setFormUrl("/views/admin/menu_form.zul");
//        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAllByUserId(CommonHelper.getActiveUser().getLogId())));
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll02()));
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 重写刷新按钮功能
     */
    @Override
    @Command
    @NotifyChange("treeModel")
    public void refreshData() {
        CommonTreeUtil<DictionaryEntity> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll02()));
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_MENU_LIST)
    @NotifyChange("treeModel")
    public void refreshDictionaryTree() {
        CommonTreeUtil<DictionaryEntity> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll02()));
        if (formDialog != null) {
            formDialog.detach();
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_MENU_ENTITY)
    @NotifyChange("*")
    public void refreshDictionaryEntity(@BindingParam("entity") MenuEntity entity) {
//        this.selectedEntity = entity;
//        BeanUtils.copyProperties(entity, this.selectedEntity);
////        System.out.println(entity.getEnabled());
//        if (formDialog != null) {
//            formDialog.detach();
//        }
        this.refreshEntity(entity);
    }

    /**
     * 展开所有节点
     *
     * @param e
     */
    @Command
    public void expandTreeNode(@BindingParam("evt") Event e) {
        Treeitem treeitem = (Treeitem) e.getTarget();
        if (treeitem.isOpen()) {
            treeitem.setOpen(false);
        } else {
            treeitem.setOpen(true);
        }
    }


    /**
     * 删除对象
     *
     * @param model
     */
    @Command
    @NotifyChange("treeModel")
    public void deleteEntity(@BindingParam("model") MenuEntity model) {
//    systemService.deleteEntity(model);
//        List<PermissionEntity> permissionEntities = systemService.getPermissionsByResourceCode(((ResourceEntity)model).getCode());
//        if(permissionEntities.size()>0){
//            for(PermissionEntity permissionEntity : permissionEntities){
//                systemService.deleteEntity(permissionEntity);
//            }
//        }
//        initList();
        menuService.delete(model);
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll02()));

    }

}
