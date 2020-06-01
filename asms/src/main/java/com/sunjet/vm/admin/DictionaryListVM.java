package com.sunjet.vm.admin;

import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.service.admin.DictionaryService;
import com.sunjet.utils.zk.CommonTreeUtil;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.ListBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;


/**
 * 字典 列表
 *
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class DictionaryListVM extends ListBaseVM {

    /*    @Wire
        private Paging pagingUser;*/
//    @Wire("#dictionaryTree")
    private Tree dictionaryTree;

    @WireVariable
    private DictionaryService dictionaryService;
    private TreeModel treeModel;

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    @Init
    public void init() {
        this.setEnableSearch(false);
        this.setEnableAdd(true);
//        userEntityListEntity = systemService.findAllUserEntity();
//        userEntityList = (List<UserEntity>)((ArrayList)userEntityListEntity).clone();
        //initList();
        this.setBaseService(dictionaryService);
        this.setFormUrl("/views/admin/dictionary_form.zul");
//    this.modelName = UserEntity.class.getSimpleName();

        CommonTreeUtil<DictionaryEntity> commonTreeUtil = new CommonTreeUtil<>();

        this.treeModel = new DefaultTreeModel(commonTreeUtil.getRoot(this.dictionaryService.findDictionaryList()));
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
//        win = (Window) view;
    }

    /**
     * 重写刷新按钮功能
     */
    @Override
    @Command
    @NotifyChange("treeModel")
    public void refreshData() {
        CommonTreeUtil<DictionaryEntity> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(commonTreeUtil.getRoot(this.dictionaryService.findDictionaryList()));
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_DICTIONARY_LIST)
    @NotifyChange("treeModel")
    public void refreshDictionaryTree() {
        CommonTreeUtil<DictionaryEntity> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(commonTreeUtil.getRoot(this.dictionaryService.findDictionaryList()));
        if (formDialog != null) {
            formDialog.detach();
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_DICTIONARY_ENTITY)
    @NotifyChange("*")
    public void refreshDictionaryEntity(@BindingParam("entity") DictionaryEntity entity) {
//        this.selectedEntity = entity;
//        BeanUtils.copyProperties(entity, this.selectedEntity);
////        System.out.println(entity.getEnabled());
//        if (formDialog != null) {
//            formDialog.detach();
//        }
        this.refreshEntity(entity);
    }

//    /**
//     * 全部展开
//     */
//    @Command
//    public void expandAll() {
//        handleTreeitemStatus(true);
//    }
//
//    /**
//     * 全部折叠
//     */
//    @Command
//    public void collapseAll() {
//        handleTreeitemStatus(false);
//    }
//
//    private void handleTreeitemStatus(Boolean status) {
//        Collection<Treeitem> items = dictionaryTree.getItems();
//        if (items == null)
//            return;
//        Object[] treeitems = items.toArray();
//        for (int i = 0; i < treeitems.length; i++) {
//            ((Treeitem) treeitems[i]).setOpen(status);
//        }
//    }

    /**
     * 删除对象
     *
     * @param model
     */
    @Command
    @NotifyChange("treeModel")
    public void deleteEntity(@BindingParam("model") DictionaryEntity model) {
        dictionaryService.delete(model);
        CommonTreeUtil<DictionaryEntity> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(commonTreeUtil.getRoot(this.dictionaryService.findDictionaryList()));
    }
}
