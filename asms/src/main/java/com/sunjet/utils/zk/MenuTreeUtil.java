package com.sunjet.utils.zk;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.MenuEntity;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class MenuTreeUtil {
    private static String ROOT_BACKGROUND = "rgb(230,230,230)";
    private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public static CustomTreeNode getRoot(List<MenuEntity> menuEntityList) {
        CustomTreeNode<MenuEntity> root = new CustomTreeNode<MenuEntity>(null, new ArrayList<TreeNode<MenuEntity>>(), true);
        for (MenuEntity menuEntity : menuEntityList) {
            if (menuEntity.getParent() == null) {    // 表示是根节点
                menuEntity.setBackground(ROOT_BACKGROUND);
                if (menuEntity.getIcon().trim().equals(""))
                    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                CustomTreeNode<MenuEntity> node;
                if (isLeaf(menuEntity, menuEntityList)) {     // 叶节点
                    node = new CustomTreeNode<MenuEntity>(menuEntity);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
                    node = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(), menuEntity.getOpen());
                }
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);
//                menuEntity.setChildrenCount(childrenCount);
//                if(childrenCount==0) {     // 叶节点
//                    node = new CustomTreeNode<MenuEntity>(menuEntity);
//                    //menuEntity.setIcon("");
//                }else{   // 非 页节点
//                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
//                    node = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(),menuEntity.getOpen());
//                }
                root.add(node);
                getNode(node, menuEntityList);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param menuEntity
     * @param menuEntityList
     * @return 叶节点, 返回true, 否则返回false
     */
    public static Boolean isLeaf(MenuEntity menuEntity, List<MenuEntity> menuEntityList) {
        for (MenuEntity menu : menuEntityList) {
            if (menu.getParent() == menuEntity) {
                return false;
            }
        }
        return true;
    }

    //    public static Integer getChildrenCount(MenuEntity menuEntity,List<MenuEntity> menuEntityList){
//        Integer childCount = 0;
//        for(MenuEntity menu: menuEntityList){
//            if(menu.getParent()==menuEntity){
//                childCount++;
//            }
//        }
//        return childCount;
//    }
    private static void getNode(CustomTreeNode<MenuEntity> parent, List<MenuEntity> menuEntityList) {
        for (MenuEntity menuEntity : menuEntityList) {
            if (menuEntity.getParent() == ((MenuEntity) parent.getData())) {
                menuEntity.setBackground(NODE_BACKGROUND);
                if (menuEntity.getIcon().trim().equals(""))
                    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                CustomTreeNode<MenuEntity> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                if (isLeaf(menuEntity, menuEntityList)) {
                    child = new CustomTreeNode<MenuEntity>(menuEntity);
                } else {
                    child = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(), menuEntity.getOpen());
                }
                parent.add(child);
                if (!isLeaf(menuEntity, menuEntityList))
                    getNode(child, menuEntityList);
            }
        }
    }
}
