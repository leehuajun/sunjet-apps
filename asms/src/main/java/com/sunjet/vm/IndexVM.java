package com.sunjet.vm;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.model.admin.MessageEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.NoticeEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.service.admin.MenuService;
import com.sunjet.service.admin.UserService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.exception.TabDuplicateException;
import com.sunjet.utils.zk.CustomTreeNode;
import com.sunjet.utils.zk.MenuTreeUtil;
import com.sunjet.utils.zk.ZkTabboxUtil;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.BaseVM;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.time.LocalDate;
import java.util.*;
import java.util.Timer;

/**
 * Created by lhj on 15/11/6.
 */
public class IndexVM extends BaseVM {

    private static Logger logger = LoggerFactory.getLogger(IndexVM.class);
    @WireVariable
    private MenuService menuService;
    @WireVariable
    private UserService userService;

    private String themeStyle = "sapphire";
    private String westTitle = "功能导航";
    private Boolean westVisible = false;
    private LocalDate currentDate;
    private TreeModel treeModel;
    private String targetUrl = "welcome.zul";

    private List<NoticeEntity> showNotices = new ArrayList<>();

    //    @Wire("#tbx1")
//    private Tabbox tbx1;
    @Wire("#menuTree")
    private Tree menuTree;
    @Wire
    A amodule, atask, anoti, amsg;
    @Wire
    West sidebar;


//    public String getTargetUrl() {
//        return targetUrl;
//    }


    public List<NoticeEntity> getShowNotices() {
        return showNotices;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public ActiveUser getActiveUser() {
        return CommonHelper.getActiveUser();
    }

    public String getThemeStyle() {
        return themeStyle;
    }

    public String getWestTitle() {
        return westTitle;
    }

    public Boolean getWestVisible() {
        return westVisible;
    }

    @Init
    public void init() {
        this.setDealer(CommonHelper.getActiveUser().getDealer());
        this.setAgency(CommonHelper.getActiveUser().getAgency());
        this.setUserType(CommonHelper.getActiveUser().getUserType());
//        Executions.getCurrent().getDesktop().getPage("homepage").setTitle(Labels.getLabel("application"));
        SecurityUtils.getSubject().isPermitted("home:test");
        System.out.println("测试权限[test:import]:" + SecurityUtils.getSubject().isPermitted("test:import"));
        System.out.println("测试权限[test:save]:" + SecurityUtils.getSubject().isPermitted("test:save"));
        this.currentDate = LocalDate.now();
        this.westVisible = true;

        this.themeStyle = ZkUtils.getTheme(Executions.getCurrent(), CommonHelper.getActiveUser().getLogId());
//        Getting application's root directory
        System.out.println(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/"));
        System.out.println(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/WEB-INF"));
        System.out.println(CommonHelper.getActiveUser().getMenus());
//    this.moduleEntityList = moduleService.findAll();
        if (CommonHelper.getActiveUser().getLogId().equals("admin")) {
            this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAllByUserId(CommonHelper.getActiveUser().getLogId())));
        } else {
            this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(CommonHelper.getActiveUser().getMenus()));
        }
        initProcessDefinition();
        this.setTasks(this.getTaskList());
        this.setNotices(this.getNoticeList());


        for (int i = 0; i < this.getNotices().size() && i < CommonHelper.LEN_SHOW; i++) {
            showNotices.add(this.getNotices().get(i));
        }
//        listTask();
//        this.targetUrl = "welcome.zul";
        initRequestOrg();

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        //System.out.println(sidebar.toString());
//        include.setSrc(null);
//        include.setSrc("/tab_window.zul");
//        include.setDynamicProperty("item", null);
//        this.targetUrl = "/welcome.zul";
        showIndex();
    }

    @Command
    public void showIndex() {
        try {
//            ZkTabboxUtil.newTab("welcome", "欢迎", "z-icon-home", false, ZkTabboxUtil.OverFlowType.AUTO, "/welcome.zul", null);
            ZkTabboxUtil.newTab("portal", "首页", "z-icon-home", false, ZkTabboxUtil.OverFlowType.AUTO, "/portal.zul", null);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        }
    }

//  @Command
//  @NotifyChange({"westTitle", "westVisible", "treeModel"})
//  public void changeModule(@BindingParam("moduleId") String moduleId) {
//    System.out.println(moduleId);
//    ModuleEntity moduleEntity = moduleService.findOne(moduleId);
//    this.moduleIdSelected = moduleId;
//    this.westTitle = moduleEntity.getName();
//    this.westVisible = true;
//    System.out.println("当前用户:" + currentUser.getLogId());
////        this.treeModel = null;
//    this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAllByUserId(moduleEntity, currentUser.getLogId())));
//  }

    /**
     * 改变主题
     *
     * @param themeName
     */
    @Command
    public void changeTheme(@BindingParam("themeName") String themeName) {
        this.themeStyle = themeName;
        ZkUtils.setTheme(Executions.getCurrent(), themeName, CommonHelper.getActiveUser().getLogId());
        Library.setProperty("org.zkoss.theme.preferred", themeName);
        Executions.sendRedirect(null);
    }

    @Command
    public void logout() {
        Executions.sendRedirect("/logout.zul");
    }


    @Command
    public void showMessageBox() {
        Messagebox.show("操作成功！", "Information", Messagebox.YES | Messagebox.NO | Messagebox.IGNORE, Messagebox.INFORMATION, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if ((int) event.getData() == Messagebox.YES)
                    Messagebox.show("Messagebox.YES");
                else if ((int) event.getData() == Messagebox.NO)
                    Messagebox.show("Messagebox.NO");
                else
                    Messagebox.show("Messagebox.IGNORE");

//                BindUtils.postGlobalCommand(null, null, "refreshRoleList", null);
            }
        });
    }

    @Command
    public void openNode(@BindingParam("e") Event e) {
        Treecell treecell = (Treecell) e.getTarget();
        Treeitem treeitem = (Treeitem) treecell.getParent().getParent();
//        通过treeitem.getValue()方法也可以获得TreeNode对象
        CustomTreeNode currentNode = (CustomTreeNode) treeitem.getValue();
        logger.info("子节点数量:" + currentNode.getChildCount());
        if (currentNode.getChildCount() > 0) {
            treeitem.setOpen(treeitem.isOpen() ? false : true);
        } else {


//        MenuEntity menuEntity = (MenuEntity)this.selectedNode.getData();
            MenuEntity menuEntity = (MenuEntity) currentNode.getData();
            logger.info("MenuEntity对象内容:" + menuEntity);
        }
    }


    @Command
//    @NotifyChange("targetUrl")
    // @NotifyChange("userMenuTree")
//    public void openTab(@BindingParam("e") Event e, @BindingParam("node") WdTreeNode node) {
//  @RequiresPermissions({"items:add:1"})
    public void openTab(@BindingParam("e") Event e) {

//        Treecell treecell = (Treecell) e.getTarget();
//        Treeitem treeitem = (Treeitem) treecell.getParent().getParent();

        System.out.println("权限验证结果:" + SecurityUtils.getSubject().isPermitted("items:add:1"));
        Treeitem treeitem = (Treeitem) e.getTarget();


//        通过treeitem.getValue()方法也可以获得TreeNode对象
        CustomTreeNode node = (CustomTreeNode) treeitem.getValue();
        logger.info("子节点数量:" + node.getChildCount());
        if (node.getChildCount() > 0) {
            if (treeitem.isOpen()) {
                treeitem.setOpen(false);
//                ((Treecell)treeitem.getTreerow().getChildren().get(0)).setIconSclass("z-icon-chevron-right");
            } else {
                treeitem.setOpen(true);
//                ((Treecell)treeitem.getTreerow().getChildren().get(0)).setIconSclass("z-icon-chevron-down");
            }

        } else {


//        MenuEntity menuEntity = (MenuEntity)this.selectedNode.getData();
            MenuEntity menuEntity = (MenuEntity) node.getData();
            logger.info("MenuEntity对象内容:" + menuEntity);
//        logger.info("当前节点状态:" + treeitem.isOpen());
//
//        if (menuEntity == null || menuEntity.getChildren().size() > 0) {
//            logger.info("menoEntity为null,或者menuEntity不是叶接点");
//            return;
//        }

//            System.out.println("contentDiv = " + contentDiv);
//            Executions.createComponents(this.targetUrl,contentDiv, null);
//            System.out.println("当前打开的页面为:" + url);
//            this.targetUrl = url;
            //Include include = (Include) item.getFellow("include");
//            Executions.createComponents(url, include, null);
//            include.setVisible(true);s
//            include.setSrc(null);
//            include.setSrc(url);
//            include.setAttribute("item", menuEntity);
//            include.i
            //include.setDynamicProperty("ite", item.getValue());

            try {
                //    Clients.showBusy("处理中，请稍候...");
                //Clients.showBusy(Executions.getCurrent().,"处理中，请稍候...");


                String url = (menuEntity.getUrl() == null || menuEntity.getUrl().trim().equalsIgnoreCase("null") || menuEntity.getUrl().trim().equals("")) ?
                        "/sorry.zul"
                        : menuEntity.getUrl();

                if (url.contains("http://")) {
//                    Executions.getCurrent().sendRedirect(url,false);
                    ZkUtils.sendRedirect(url, "_blank");
                    return;
                }

                String iconSclass = menuEntity.getIcon();
                ZkTabboxUtil.newTab(menuEntity.getObjId(), menuEntity.getName(), iconSclass, true, ZkTabboxUtil.OverFlowType.AUTO, url, null);
                //Messagebox.show(menuEntity.getName() + "-->" + url);
            } catch (TabDuplicateException ex) {
                //  Clients.clearBusy();
                ex.printStackTrace();
                //Messagebox.show(ex.getMessage(), "WARN", Messagebox.OK, Messagebox.INFORMATION);
            }
        }

    }

    @GlobalCommand("myGlobalCommand")
    @Command
    public void myGlobalCommand() {
        Messagebox.show("myGlobalCommand");
    }


    @Listen("onOpen = #modulepp")
    public void toggleModulePopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), amodule);
    }

    @Listen("onOpen = #taskpp")
    public void toggleTaskPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), atask);
    }

    @Listen("onOpen = #notipp")
    public void toggleNotiPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), anoti);
    }

    @Listen("onOpen = #msgpp")
    public void toggleMsgPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), amsg);
    }

    // Toggle open class to the component

    public void toggleOpenClass(Boolean open, Component component) {
        HtmlBasedComponent comp = (HtmlBasedComponent) component;
        String scls = comp.getSclass();
        if (open) {
            comp.setSclass(scls + " open");
        } else {
            comp.setSclass(scls.replace(" open", ""));
        }
    }

    /**
     * 获取客户端信息
     *
     * @param event
     */
    @Command
    public void showClientInfo(@BindingParam("evt") ClientInfoEvent event) {
        CommonHelper.windowHeight = event.getDesktopHeight();
        CommonHelper.windowWidth = event.getDesktopWidth();
        CommonHelper.baseGridHeight = CommonHelper.windowHeight - 49 - 29 - 32 - 30 - 30;
        logger.info(String.format("Desktop: width:%s x height:%s", event.getDesktopWidth(), event.getDesktopHeight()));
        logger.info(String.format("Screen: width:%s x height:%s", event.getScreenWidth(), event.getScreenHeight()));
    }

    @Command
    public void changePassword() {
        Window window = (Window) ZkUtils.createComponents("/views/admin/change_password_form.zul", null, null);
        window.doModal();
    }

    @Command
    public void enterTaskList() throws TabDuplicateException {
        enterList("/views/flow/task_list.zul");
    }

    @Command
    public void enterNoticeList() throws TabDuplicateException {
        enterList("/views/basic/notice_list.zul");
    }

    private void enterList(String url) throws TabDuplicateException {
        MenuEntity menu = menuService.findMenuByUrl(url);
        ZkTabboxUtil.newTab(menu.getObjId(), menu.getName(), null, true,
                ZkTabboxUtil.OverFlowType.SCROLL, url, null);
    }

//    @Command
//    public void taskClicked(@BindingParam("task") Task task) throws TabDuplicateException {
//        String processDefinitionName = this.getMapProcessDefinition().get(task.getProcessDefinitionId()).getName();
//        String formUrl = processService.findFormUrl(task);
////        this.handleTask(task, formUrl, processDefinitionName);
//        this.handleTask(task);
//    }

    @Command
    public void noticeClicked(@BindingParam("notice") NoticeEntity notice) throws TabDuplicateException {
        Map<String, DocEntity> paramMap = new HashMap<>();
        paramMap.put("model", notice);
        Window dialog = (Window) Executions.createComponents("/views/basic/notice_form.zul", null, paramMap);
        dialog.setStyle("background:rgb(78,116,149)");
        dialog.setStyle("padding:0px;");
        dialog.doModal();
    }

    @Command
    public void testHourPrice() {
        UserEntity entity = userService.findOne(CommonHelper.getActiveUser().getUserId());
        if (entity.getDealer() == null) {
            ZkUtils.showExclamation("不是服务站用户", "系统提示");
        } else {
            ZkUtils.showInformation("工时单价为: " + this.getHourPriceByDealer(entity.getDealer()), "系统提示");
        }
    }

    @Command
    public void showVehicleMaintainance() {
        Window dialog = (Window) ZkUtils.createComponents("/views/report/vehicle_maintainance.zul", null, null);
        dialog.setStyle("background:rgb(78,116,149)");
        dialog.setStyle("padding:0px;");
        dialog.doModal();
    }

    @Command
    public void showDealerInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("objId", CommonHelper.getActiveUser().getDealer().getObjId());
        String url = "/views/basic/dealer_form.zul";
        showWindow(url, paramMap);
    }

    @Command
    public void showAgencyInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("objId", CommonHelper.getActiveUser().getAgency().getObjId());
        String url = "/views/basic/agency_form.zul";
        showWindow(url, paramMap);
    }

    private void showWindow(String url, Map map) {
        Window wd = (Window) ZkUtils.createComponents(url, null, map);
        wd.setStyle("background:rgb(78,116,149)");
        wd.setStyle("padding:0px;");
        wd.doModal();
    }

    @Command
    @NotifyChange("messages")
    public void enterMessageList() {
//        this.getMessages().add(new MessageEntity("test","test","1234567",""));
        this.getMessages().clear();
    }
}
