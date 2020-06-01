package com.sunjet.utils.zk;

import com.sunjet.model.base.TreeNodeEntity;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class TreeUtil {
//  private static String ROOT_BACKGROUND = "rgb(230,230,230)";
//  private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public static CustomTreeNode getRoot(List<TreeNodeEntity> nodeList) {
        CustomTreeNode<TreeNodeEntity> root = new CustomTreeNode<TreeNodeEntity>(null, new ArrayList<TreeNode<TreeNodeEntity>>(), true);
        for (TreeNodeEntity node : nodeList) {
            if (node.getParent() == null) {    // 表示是根节点
                //node.setBackground(ROOT_BACKGROUND);
//        if (node.getIcon().trim().equals(""))
//          node.setIcon(Config.getConfigValue("treenode_icon"));
                CustomTreeNode<TreeNodeEntity> nn;
                if (isLeaf(node, nodeList)) {     // 叶节点
                    nn = new CustomTreeNode<TreeNodeEntity>(node);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(Config.DEFAULT_TREENODE_ICON);
                    nn = new CustomTreeNode<TreeNodeEntity>(node, new ArrayList<TreeNode<TreeNodeEntity>>(), true);
                }
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);
//                menuEntity.setChildrenCount(childrenCount);
//                if(childrenCount==0) {     // 叶节点
//                    node = new CustomTreeNode<MenuEntity>(menuEntity);
//                    //menuEntity.setIcon("");
//                }else{   // 非 页节点
//                    //menuEntity.setIcon(Config.DEFAULT_TREENODE_ICON);
//                    node = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(),menuEntity.getOpen());
//                }
                root.add(nn);
                getNode(nn, nodeList);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param node
     * @param nodeList
     * @return 叶节点, 返回true, 否则返回false
     */
    public static Boolean isLeaf(TreeNodeEntity node, List<TreeNodeEntity> nodeList) {
        for (TreeNodeEntity nn : nodeList) {
            if (nn.getParent() == node) {
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
    private static void getNode(CustomTreeNode<TreeNodeEntity> parent, List<TreeNodeEntity> nodeList) {
        for (TreeNodeEntity node : nodeList) {
            if (node.getParent() == ((TreeNodeEntity) parent.getData())) {
//        node.setBackground(NODE_BACKGROUND);
//        if (node.getIcon().trim().equals(""))
//          node.setIcon(Config.getConfigValue("treenode_icon"));
                CustomTreeNode<TreeNodeEntity> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                if (isLeaf(node, nodeList)) {
                    child = new CustomTreeNode<TreeNodeEntity>(node);
                } else {
                    child = new CustomTreeNode<TreeNodeEntity>(node, new ArrayList<TreeNode<TreeNodeEntity>>(), true);
                }
                parent.add(child);
                if (!isLeaf(node, nodeList))
                    getNode(child, nodeList);
            }
        }
    }
}
