package com.sunjet.utils.zk;


import com.sunjet.utils.common.UUIDHelper;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuSendRedirect;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.ExecutionCtrl;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.impl.InputElement;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class ZkUtils {

    public final static String THEME_COOKIE_KEY = "zktheme";
    public final static String IS_AUTHORIZE_MENU_KEY = "_menu_id";//用户判断打开的菜单是否需要授权
    public static Map<String, List<Map>> selectData = new HashMap<String, List<Map>>();

    /**
     * 设置 cookie 值
     *
     * @param userId
     * @param cookieName
     * @param cookieValue
     */
    public static void setCookie(String userId, String cookieName, String cookieValue) {
        if (userId != null)
            cookieName = cookieName + "_" + userId;

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(60 * 60 * 24 * 30); // store 30 days
        String cp = getCurrentExecution().getContextPath();
        // if path is empty, cookie path will be request path, which causes
        // problems
        if (cp.length() == 0)
            cp = "/";
        cookie.setPath(cp);
        ((HttpServletResponse) getCurrentExecution().getNativeResponse()).addCookie(cookie);
    }

    /**
     * @param userId
     * @param cookieName
     * @return
     */
    public static String getCookie(String userId, String cookieName) {
        if (userId != null)
            cookieName = cookieName + "_" + userId;
        Cookie[] cookies = ((HttpServletRequest) getCurrentExecution().getNativeRequest())
                .getCookies();
        if (cookies == null)
            return "";
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if (cookieName.equals(c.getName())) {
                String cookieValue = c.getValue();
                if (cookieValue != null)
                    return cookieValue;
            }
        }
        return "";
    }

    /**
     * Sets the theme style in cookie
     */
    public static void setTheme(Execution exe, String theme, String userId) {
        String cookieString = THEME_COOKIE_KEY;
        if (userId != null)
            cookieString = THEME_COOKIE_KEY + "_" + userId;
        Cookie cookie = new Cookie(cookieString, theme);
        cookie.setMaxAge(60 * 60 * 24 * 30); // store 30 days
        String cp = exe.getContextPath();
        // if path is empty, cookie path will be request path, which causes
        // problems
        if (cp.length() == 0)
            cp = "/";
        cookie.setPath(cp);
        ((HttpServletResponse) exe.getNativeResponse()).addCookie(cookie);
    }

    /**
     * Returns the theme specified in cookies
     *
     * @param exe Execution
     * @return the name of the theme or "" for default theme.
     */
    public static String getTheme(Execution exe, String userId) {
        String cookieString = THEME_COOKIE_KEY;
        if (userId != null)
            cookieString = THEME_COOKIE_KEY + "_" + userId;
        Cookie[] cookies = ((HttpServletRequest) exe.getNativeRequest())
                .getCookies();
        if (cookies == null)
            return "";
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if (cookieString.equals(c.getName())) {
                String theme = c.getValue();
                if (theme != null)
                    return theme;
            }
        }
        return "";
    }


    public static enum WindowType {
        doModal, doEmbedded, doHighlighted, doOverlapped, doPopup, none
    }


    /**
     * 重新打开浏览器弹出页面
     *
     * @param url   网络地址
     * @param blank ；打开类型，可以是"_blank","_self"等
     */
    public static void sendRedirect(String url, String blank) {
        Executions.getCurrent().sendRedirect(url, blank);
    }

    /**
     * 据isCheck的值勾选或者不勾选制定列columnKey和值checkValue的节点
     *
     * @param listBox
     * @param isCheck
     * @param checkValue
     * @param columnKey
     */
    public static void checkListBoxItem(Component listBox, boolean isCheck,
                                        String checkValue, String columnKey) {
        if (listBox instanceof Listbox)
            ((Listbox) listBox).renderAll();//渲染控件，否则分页时勾选出错

        if (listBox instanceof Listitem) {
            Listitem item = (Listitem) listBox;
            Map itemMap = item.getValue();
            if ((itemMap.get(columnKey) + "").trim().equals(checkValue)) {
                item.setSelected(true);
            }
        }
        Collection<?> com = listBox.getChildren();
        if (com != null) {
            for (Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
                checkListBoxItem((Component) iterator.next(), isCheck, checkValue, columnKey);
            }
        }
    }

    /**
     * 根据isCheck的值勾选或者清空所有节点
     *
     * @param listBox
     * @param isCheck
     */
    public static void checkAllListBoxTtem(Component listBox, boolean isCheck) {
        if (listBox instanceof Listbox)
            ((Listbox) listBox).renderAll();//渲染控件，否则分页时勾选出错

        if (listBox instanceof Listitem) {
            Listitem item = (Listitem) listBox;
            item.setSelected(isCheck);
        }
        Collection<?> com = listBox.getChildren();
        if (com != null) {
            for (Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
                checkAllListBoxTtem((Component) iterator.next(), isCheck);
            }
        }
    }

    /**
     * 勾选或者不勾选指定节点名称为nodeName的节点；tree用于遍历渲染节点，treeComponent用于递归
     *
     * @param tree
     * @param treeComponent
     * @param isCheck
     * @param nodeName
     */
//  public static void checkTreeNode(Tree tree, Component treeComponent, boolean isCheck,
//                                   String nodeName) {
//    if (treeComponent instanceof Treeitem) {
//      Treeitem treeitem = (Treeitem) treeComponent;
//      CustomTreeNode node = (CustomTreeNode) treeitem.getValue();
//      tree.renderItem(treeitem);
//      if (node.getNodeName().equals(nodeName.trim())) {
//        treeitem.setSelected(isCheck);
//        treeitem.setOpen(isCheck);
//        setOpenParentTreeitem(treeitem, isCheck);
//      }
//    }
//    Collection<?> com = treeComponent.getChildren();
//    if (com != null) {
//      for (Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
//        checkTreeNode(tree, (Component) iterator.next(), isCheck, nodeName);
//      }
//    }
//  }

    /**
     * 展开treeitem节点的所有父节点
     *
     * @param treeitem
     * @param isCheck
     */
    public static void setOpenParentTreeitem(Treeitem treeitem, boolean isCheck) {
        if (treeitem != null && treeitem.getParentItem() != null) {
            treeitem.getParentItem().setOpen(isCheck);
            setOpenParentTreeitem(treeitem.getParentItem(), isCheck);
        }
    }

    /**
     * 全部勾选或者清空勾选
     *
     * @param tree
     * @param isCheck
     */
    public static void checkAllTreeNodes(Component tree, boolean isCheck) {
        if (tree instanceof Treeitem) {
            Treeitem treeitem = (Treeitem) tree;
            treeitem.setOpen(isCheck);
            treeitem.setSelected(isCheck);
        }
        Collection<?> com = tree.getChildren();
        if (com != null) {
            for (Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
                checkAllTreeNodes((Component) iterator.next(), isCheck);
            }
        }
    }

    /**
     * 展开或者收缩tree的节点
     *
     * @param tree
     * @param isOpen
     */
    public static void collapseExpandAllTreeNodes(Component tree, boolean isOpen) {
        if (tree instanceof Treeitem) {
            Treeitem treeitem = (Treeitem) tree;
            treeitem.setOpen(isOpen);
        }
        Collection<?> com = tree.getChildren();
        if (com != null) {
            for (Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
                collapseExpandAllTreeNodes((Component) iterator.next(), isOpen);
            }
        }
    }


    /**
     * 加载树形所有节点
     * 作者：邓清友
     *
     * @param tree
     * @param isOpen
     */
    public static void getAllTreeNodes(Component tree, boolean isOpen) {
        Collection<?> com = tree.getChildren();
        if (com != null) {
            for (Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
                collapseExpandAllTreeNodes((Component) iterator.next(), isOpen);
            }
        }
    }


    /**
     * 上传文件
     *
     * List<FcFile> list = new ArrayList(); public FcFile InitUploadData(Media
     * media, String tableName, String tag) throws IOException { FcFile file =
     * new FcFile(); if (media != null) { if (media.getName().endsWith("doc") ||
     * media.getName().endsWith("docx") || media.getName().endsWith("xlsx") ||
     * media.getName().endsWith("xls") || media.getName().endsWith("bmp") ||
     * media.getName().endsWith("jpg") || media.getName().endsWith("rar") ||
     * media.getName().endsWith("zip") || media.getName().endsWith("gif") ||
     * media.getName().endsWith("pdf")) { InputStream inputStream =
     * media.getStreamData(); String fileName = media.getName(); String kzm =
     * fileName.substring(fileName.lastIndexOf(".") + 1,
     * fileName.length()).toLowerCase();// 后缀名 String fileNewName
     * =DateTime.now().toString("yyyyMMddhhmmss")+ "." + kzm;
     * file.setInputStream(inputStream); file.setFileName(fileName);
     * file.setFilePath( "/upload/firstcamp/" + fileNewName);
     * file.setFileCreatTime(new Date()); file.setBusinessTableName(tableName);
     * file.setFileTag(tag); } else { Messagebox.show(
     * "文件类型有误！请上传doc、docx、xlsx、xls、pdf、bmp、jpg、rar、zip、gif类型的文件", "提示",
     * Messagebox.OK, Messagebox.INFORMATION); } }
     *
     * return file; }
     */

    /**
     * 上传文件
     */
    public Map InitUploadData(Media media, String tableName, String tag, String uploadpkg)
            throws IOException {
//		Map file = new HashMap();
//		if (media != null) {
//			if (media.getName().endsWith("doc")
//					|| media.getName().endsWith("docx")
//					|| media.getName().endsWith("xlsx")
//					|| media.getName().endsWith("xls")
//					|| media.getName().endsWith("bmp")
//					|| media.getName().endsWith("jpg")
//					|| media.getName().endsWith("rar")
//					|| media.getName().endsWith("zip")
//					|| media.getName().endsWith("gif")
//					|| media.getName().endsWith("pdf")) {
//				InputStream inputStream = media.getStreamData();
//				String fileName = media.getName();
//				String kzm = fileName.substring(fileName.lastIndexOf(".") + 1,
//						fileName.length()).toLowerCase();// 后缀名
//				LocalTime.
//				String fileNewName = DateTime.now().toString("yyyyMMddhhmmss")
//						+ "." + kzm;
//				file.put("inputStream", inputStream);
//				file.put("fileName", fileName );
//				file.put("fileNewName", fileNewName);
//				file.put("filePath", DateTime.now().toString("yyyyMMddhhmmss")+fileName);
//				file.put("fileCreatTime", new Date());
//				file.put("businessTableName", tableName);
//				file.put("fileTag", tag);
//			} else {
//				file = null;
//				Messagebox
//						.show("文件类型有误！请上传doc、docx、xlsx、xls、pdf、bmp、jpg、rar、zip、gif类型的文件",
//								"提示", Messagebox.OK, Messagebox.INFORMATION);
//			}
//		}
//		return file;
        return null;
    }

    public boolean save(InputStream inputStream, String fileName) {
        String strOS = System.getProperties().getProperty("os.name")
                .toLowerCase();
        try {
            String realPath = "";
            if ((System.getProperties().getProperty("os.name").toLowerCase()
                    .indexOf("windows")) > -1) {
                // Windows下上传路径
                Desktop dtp = Executions.getCurrent().getDesktop();
                realPath = dtp.getSession().getWebApp()
                        .getRealPath("/resource/upload/firstcamp/")
                        + "/";
            } else {
                // Linux下上传路径
                realPath = System.getProperty("catalina.home")
                        + "/webapps/com.elite.mis/resource/upload/firstcamp/";
            }
            File file = new File(realPath + fileName);
            Files.copy(file, inputStream);
            Files.close(inputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String onUploadFile(Media media, String path) {
        java.io.BufferedInputStream in = null;
        java.io.BufferedOutputStream out = null;
        String fileName = "";
        try {
            InputStream fin = media.getStreamData();
            in = new java.io.BufferedInputStream(fin);

            File baseDir = new File(path);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
//            System.out.println(media.getFormat());
            int lastIdx = media.getName().lastIndexOf(".");
//            String newName = "";
            // 不存在扩展名
            if (lastIdx < 0) {
                fileName = UUIDHelper.newUuid();
            } else {
                String ext = media.getName().substring(lastIdx);
                System.out.println(ext);
                fileName = UUIDHelper.newUuid() + ext;
            }
            File file = new File(path + "/" + fileName);
            java.io.OutputStream fout = new java.io.FileOutputStream(file);
            out = new java.io.BufferedOutputStream(fout);
            byte buffer[] = new byte[1024];//
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);
                ch = in.read(buffer);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileName;
    }

    public static String onUploadFileWithName(Media media, String path) {
        java.io.BufferedInputStream in = null;
        java.io.BufferedOutputStream out = null;
        String fileName = "";
        try {
            InputStream fin = media.getStreamData();
            in = new java.io.BufferedInputStream(fin);

            File baseDir = new File(path);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
//            System.out.println(media.getFormat());
//            int lastIdx = media.getName().lastIndexOf(".");
//            String newName = "";
            fileName = media.getName();
            // 不存在扩展名
//            if (lastIdx < 0) {
//                fileName = UUIDHelper.newUuid();
//            } else {
//                String ext = media.getName().substring(lastIdx);
//                System.out.println(ext);
//                fileName = UUIDHelper.newUuid() + ext;
//            }
            File file = new File(path + "/" + fileName);
            java.io.OutputStream fout = new java.io.FileOutputStream(file);
            out = new java.io.BufferedOutputStream(fout);
            byte buffer[] = new byte[1024];//
            int ch = in.read(buffer);
            while (ch != -1) {
                out.write(buffer, 0, ch);
                ch = in.read(buffer);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileName;
    }

    /**
     * 这个定义要跟
     *
     * @author Administrator
     */
    public static enum MenuOperations {
        add, edit, view, delete, print, excel
    }

    /**
     * 判断用户菜单授权
     *
     * @param menuOperation 要判断的操作
     * @param vm            要判断的窗口
     * @param showMsg       是否弹出提示信息
     * @return true 有权限，false 没有权限
     */
//  public static boolean checkOperationPermission(MenuOperations menuOperation, BaseVM vm, boolean showMsg) {
////		boolean isOk = false;
////		if (!StringUtils.isEmptyString(vm.getMenuInfo().get("menu_id") + "")) {
////			String key = menuOperation.name();
////			String mpString = ((Map)(vm.getMenuInfo().get("pms"))).get(key)+ "";
////
////			if (StringUtils.isEmptyString(mpString) || !mpString.equals(key)) {
////				if (showMsg)
////					Messagebox.show(MisUtils
////							.getMessageResource("permission_disable"), MisUtils
////							.getMessageResource("messagebox_title_info"),
////							Messagebox.OK, Messagebox.ERROR);
////				isOk = false;
////			} else {
////				isOk = true;
////			}
////		}
////		else {
////			isOk = true;
////		}
////		return isOk;
//    return true;
//  }

    /**
     * 打开流程引擎审核页面
     *
     * @Title: openActivitiVerifyPage
     * @Description: TODO
     * @param: @param taskId：任务ID
     * @param: @param businessKey：业务单据唯一号
     * @param: @param map：其他参数
     * @return: void
     * @author luxc
     * @Date 2014年7月2日 下午10:29:55
     */
    public static void openActivitiVerifyPage(String taskId, String businessKey, Map map) {
//		ProcessEngineCore processEngineCore=((ProcessEngineCore)MisUtils.getSpringContext().getBean("processEngineCore"));
//		//获取当前节点documentation属性的值
//		String verifyPageUrl=StringUtils.trim(processEngineCore.getActivityPropertiesByTaskId(taskId).get("documentation")+"");
//
//		if(map==null)
//			map=new HashMap();
//		map.put("taskId", taskId);
//		map.put("businessKey", businessKey);
//
//		//webFormId web菜单ID,格式 webFormId=9;winFormId c#应用程序菜单ID,格式 winFormId=87
//		if(verifyPageUrl.indexOf("|")>-1){
//			String[] a=verifyPageUrl.split("|");//例：webFormId=9|winFormId=87|webUrl=manage/activiti/user_task_list.zul?isUserTask=false
//			for(int i=0;i<a.length;i++){
//				if(a[i].trim().length()>0){
//					String[] b=a[i].split("=");
//					if(b.length>1){
//						if(b[0].trim().equals("webFormId")&&b[1].trim().length()>0){
//							ZkUtils.createComponent(b[1].trim(), WindowType.doModal,null,map);//打开授权页面
//						}else if(b[0].trim().equals("webUrl")&&b[1].trim().length()>0&&b[1].indexOf(".zul")>-1){
//							ZkUtils.openWindow(verifyPageUrl, map, WindowType.doModal,	null);
//						}else{
//							Messagebox.show("操作失败，参数设置错误！");
//						}
//					}
//				}
//			}
//		}else{
//			if(verifyPageUrl.startsWith("webFormId")){
//				String[] a=verifyPageUrl.split("=");//例：webFormId=9
//				if(a.length>1&&a[1].trim().length()>0){
//					ZkUtils.createComponent(a[1].trim(), WindowType.doModal,null,map);//打开授权页面
//				}else{
//					Messagebox.show("操作失败，webFormId参数设置错误！");
//				}
//			}else if (verifyPageUrl.startsWith("webUrl")) {
//				String url=verifyPageUrl.substring(verifyPageUrl.indexOf("=")+1);
//				if(url.trim().length()>0){
//					ZkUtils.openWindow(url, map, WindowType.doModal,null);
//				}else{
//					Messagebox.show("操作失败，webUrl参数设置错误！");
//				}
//			}else if (verifyPageUrl.indexOf(".zul") > -1) {
//				ZkUtils.openWindow(verifyPageUrl, map, WindowType.doModal,	null);
//			} else {
//
//			}
//		}
    }

    /**
     * 获取url的返回值
     *
     * @Title: getUrlMap
     * @Description: TODO
     * @param: @param url
     * @param: @param resultParameters
     * @param: @return
     * @return: Map
     * @author luxc
     * @Date 2014年7月4日 下午12:00:15
     */
    static Map getUrlMap(String url, Map resultParameters) {
        if (resultParameters == null)
            resultParameters = new HashMap();
        if (url.indexOf("?") > -1) {
            String urlParameter = url.substring(url.indexOf("?") + 1);
            if (urlParameter.trim().length() > 0) {
                String[] a = urlParameter.split("&");
                for (int i = 0; i < a.length; i++) {
                    if (a[i].trim().length() > 0) {
                        String[] b = a[i].trim().split("=");
                        if (b.length > 1)
                            resultParameters.put(b[0], a[i].trim().substring(a[i].trim().indexOf("=") + 1));
                        else
                            resultParameters.put(b[0], "");
                    }
                }
            }
        }
        return resultParameters;
    }

    /**
     * 发送消息给父窗口，基于Iframe
     *
     * @Title: sendMessageToParentIframe
     * @Description: TODO
     * @param: @param parameters
     * @param: @return
     * @return: boolean
     * @author luxc
     * @Date 2014年11月3日 上午10:22:18
     */
    public static boolean sendMessageToParentIframe(Map parameters) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		String jsonData;
//		try {
//			jsonData = objectMapper.writeValueAsString(parameters);
//			//System.out.println("jsonData!!:"+jsonData);
//			String jsCommand="window.parent.postMessage("+jsonData+",'*');";
//			Clients.evalJavaScript(jsCommand);
//			return true;
//		} catch ( IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
        return true;
    }


    /**
     * 获得当前Execution，Execution类似HttpServletRequest
     * <p>
     * 客户端请求的执行器（例如ServletRequest），当客户端发送一个请求后，
     * 服务端负责构造这个Execution对象，获取当前执行的信息，然后服务这个请求
     * <p>
     * 一个客户端请求, 例如, HttpServletRequest, 或许关联多个请求 request (
     * {@link org.zkoss.zk.au.AuRequest}). 但是, 这些 ZK 请求来自的页面 ({@link Page}
     * )必须具有相同的desktop
     * <p>
     * 因为一个请求也许来自http或其他协议，Execution通常一个隔离层
     *
     * @return 当前execution
     * @see Execution
     */
    public static final Execution getCurrentExecution() {
        return Executions.getCurrent();
    }

    /**
     * 返回当前Exection桌面对象
     * <p>
     * 在zk中，具有同一个dom document元素组成了desktop，注意这里的同一个document，
     * 例如iframe会创建另外一个document, 点击链接打开一个新浏览器窗口，这个窗口里的document与原窗口的document是不同的
     *
     * @return
     * @see Execution#getDesktop()
     */
    public static final Desktop getCurrentDesktop() {
        return getCurrentExecution().getDesktop();
    }

    /**
     * 获得当前Execution所属的会话
     * <p>
     * <b>注</b>该Session不同于HttpSession,该session为zk定义的session作用域
     *
     * @return
     * @see Session
     */
    public static final Session getCurrentSession() {
        return Sessions.getCurrent();
    }

    /**
     * 返回本地session对象，如果不可用返回null,返回的对象依赖客户端类型，如果是就http的
     * ，那么返回javax.servlet.http.HttpSession的一个实例
     * ，如果是portlet，那么返回javax.portlet.PortletSession的实例
     */
    public static final Object getNativeSession() {
        return getCurrentSession().getNativeSession();
    }

    /**
     * 返回本地请求对象，如果不可用返回null
     *
     * @return 返回的对象依赖web容器，如果web容器是一个servlet容器，那么返回的对象为ServletRequest
     */
    public static final Object getNativeRequest() {
        return getCurrentExecution().getNativeRequest();
    }

    /**
     * 返回本地响应对象，如果不可用返回null
     *
     * @return 返回的对象依赖web容器，如果web容器是一个servlet容器，那么返回的对象为ServletResponse
     */
    public static final Object getNativeResponse() {
        return getCurrentExecution().getNativeResponse();
    }

    /**
     * 获得WebApp对象,类似HttpServletContext
     *
     * @return
     * @see Desktop#getWebApp()
     * @see WebApp
     */
    public static final WebApp getWebApp() {
        return getCurrentDesktop().getWebApp();
    }

    /**
     * 返回给定虚拟路径的实际路径
     */
    public static final String getRealPath(String virtualPath) {
        return getWebApp().getRealPath(virtualPath);
    }

    /**
     * 获得当前请求来自的页面
     *
     * @return
     */
    public static final Page getCurrentPage() {
        return ((ExecutionCtrl) getCurrentExecution()).getCurrentPage();
    }

    /**
     * 返回execution作用域内参数
     *
     * @return
     * @see Execution#getArg()
     */
    @SuppressWarnings("rawtypes")
    public static final Map getExecutionArgs() {
        return getCurrentExecution().getArg();
    }

    /**
     * 获得当前Execution作用域内的属性，类似HttpServletRequest相应方法
     *
     * @see Execution#getAttributes()
     */
    @SuppressWarnings("rawtypes")
    public static final Map getExectionAttributes() {
        return getCurrentExecution().getAttributes();
    }

    /**
     * 设置Execution请求属性值，类似HttpServletRequest相应方法
     *
     * @param name  请求属性
     * @param value 属性值
     */
    public static final void setExecutionAttribute(String name, Object value) {
        getCurrentExecution().setAttribute(name, value);
    }

    /**
     * 设置Execution作用域属性值或其父作用域的值，类似HttpServletRequest相应方法
     *
     * @param name    请求属性
     * @param value   属性值
     * @param recurse 检查父作用域是否存在该属性，如果存在将替换父作用域的值
     */
    public static final void setExecutionAttribute(String name, Object value,
                                                   boolean recurse) {
        getCurrentExecution().setAttribute(name, value, recurse);
    }

    /**
     * 获得Execution请求参数，类似HttpServletRequest相应方法
     *
     * @return 参数map
     */
    @SuppressWarnings("rawtypes")
    public static final Map getExecutionParameterMap() {
        return getCurrentExecution().getParameterMap();
    }

    /**
     * 获得Execution请求参数值，类似HttpServletRequest相应方法
     *
     * @param name 请求参数的名字
     * @return 指定名字的参数值
     */
    public static final String getExecutionParameter(String name) {
        return getCurrentExecution().getParameter(name);
    }

    /**
     * 获得Execution请求参数值，类似HttpServletRequest相应方法
     *
     * @param name 参数的名字
     * @return 字符数组
     */
    public static final String[] getExecutionParameterValues(String name) {
        return getCurrentExecution().getParameterValues(name);
    }

    /**
     * 获得当前请求消息头，类似HttpServletRequest相应方法
     *
     * @param name 消息头名字
     * @return 消息头值
     */
    public static final String getRequestHeader(String name) {
        return getCurrentExecution().getHeader(name);
    }

    /**
     * 返回指定请求头名字的所有值，类似HttpServletRequest相应方法
     *
     * @param name 请求头的名字
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final Iterator getRequestHeaders(String name) {
        return getCurrentExecution().getHeaders(name).iterator();
    }

    /**
     * 返回所有请求头名字，类似HttpServletRequest相应方法
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final Iterator getRequestHeaderNames() {
        return getCurrentExecution().getHeaderNames().iterator();
    }

    /**
     * 添加一个指定名称和值的相应头，允许相应头具有多值，类似HttpServletResponse相应方法
     *
     * @param name
     * @param value
     */
    public static final void addResponseHeader(String name, String value) {
        getCurrentExecution().addResponseHeader(name, value);
    }

    /**
     * 添加一个指定名称和日期值的响应头，类似HttpServletResponse相应方法
     *
     * @param name
     * @param value
     */
    public static final void addResponseHeader(String name, Date value) {
        getCurrentExecution().addResponseHeader(name, value);
    }

    /**
     * 返回接收请求的本地ip地址，类似HttpServletRequest相应方法
     *
     * @return
     */
    public static final String getLocalAddr() {
        return getCurrentExecution().getLocalAddr();
    }

    /**
     * 获得接收请求的本地host name，类似HttpServletRequest相应方法
     *
     * @return
     */
    public static final String getLocalName() {
        return getCurrentExecution().getLocalName();
    }

    /**
     * 获得接收请求的本地端口，类似HttpServletRequest相应方法
     *
     * @return
     */
    public static final int getLocalPort() {
        return getCurrentExecution().getLocalPort();
    }

    /**
     * 获得发送请求的客户端ip，类似HttpServletRequest相应方法
     *
     * @return
     */
    public static final String getRemoteAddr() {
        return getCurrentExecution().getRemoteAddr();
    }

    /**
     * 获得发送请求的客户端的host name，类似HttpServletRequest相应方法
     *
     * @return
     */
    public static final String getRemoteHost() {
        return getCurrentExecution().getRemoteHost();
    }

    /**
     * 设置session 属性，类似HttpSession相应方法
     *
     * @param name  属性名
     * @param value 属性值
     */
    public static final void setSessionAttribute(String name, Object value) {
        getCurrentSession().setAttribute(name, value);
    }

    /**
     * 设置session或父作用域 属性，类似HttpSession相应方法
     *
     * @param name    属性名
     * @param value   属性值
     * @param recurse 是否查询父作用域包含name名字的属性，如果包含将替换该值
     */
    public static final void setSessionAttribute(String name, Object value,
                                                 boolean recurse) {
        getCurrentSession().setAttribute(name, value, recurse);
    }

    /**
     * 返回session作用域对象，类似HttpSession相应方法
     *
     * @param name    属性名
     * @param recurse 是否检索父作用域，如果为true， 并且当前作用域没声明这个属性，那么将搜索父作用域
     * @return
     */
    public static final Object getSessionAttribute(String name, boolean recurse) {
        return getCurrentSession().getAttribute(name, recurse);
    }

    /**
     * 返回session作用域对象，类似HttpSession相应方法
     *
     * @param name 属性名
     * @return
     */
    public static final Object getSessionAttribute(String name) {
        return getCurrentSession().getAttribute(name);
    }

    /**
     * 获得所有session作用域对象，类似HttpSession相应方法
     *
     * @return map 类型的作用域所有对象
     */
    @SuppressWarnings("rawtypes")
    public static final Map getSessionAttributes() {
        return getCurrentSession().getAttributes();
    }

    /**
     * 获得会话超时时间，单位秒，类似HttpSession相应方法
     *
     * @return
     */
    public static final int getSessionMaxInactiveInterval() {
        return getCurrentSession().getMaxInactiveInterval();
    }

    /**
     * 指定失效事件，单位秒，负值表示永不过期，类似HttpSession相应方法
     *
     * @param interval
     */
    public static final void setSessionMaxInactiveInterval(int interval) {
        getCurrentSession().setMaxInactiveInterval(interval);
    }

    /**
     * 销毁当前session
     * <p>
     * <p>
     * 表示解除绑定在session上的所有对象 这里我们要注意: 通常你使用 {@link Executions#sendRedirect}
     * 让客户端重定向另一个页面(或重加载同一页面) session并不立即销毁 ，而是在当前请求之后销毁，即重定向页面显示完毕之后
     * 如果想立即销毁((SessionCtrl)Sessions.getCurrent()).invalidateNow();
     * 在zk中这一点和通常所说的HttpSession.invalidate()有所不同
     * <p>
     * 由 天明ゞ破晓 (qq 513062844) 提出的疑问。great thanks
     */
    public static final void invalidateSession() {
        getCurrentSession().invalidate();
    }

    /**
     * 立即销毁当前session
     * <p>
     * 非立即销毁session情况见 {@link #invalidateSession()}
     */
    public static final void invlidateSessionNow() {
        ((SessionCtrl) getCurrentSession()).invalidateNow();
    }

    /**
     * 设置页面作用域属性
     *
     * @param name  属性名
     * @param value 属性值
     */
    public static final void setPageAttribute(String name, Object value) {
        getCurrentPage().setAttribute(name, value);
    }

    /**
     * 设置page或父作用域属性
     *
     * @param name
     * @param value
     * @param recurse 是否检索父作用域，如果为true， 并且当前作用域没声明这个属性，那么将搜索父作用域 ,并替换
     */
    public static final void setPageAttribute(String name, Object value,
                                              boolean recurse) {
        getCurrentPage().setAttribute(name, value, recurse);
    }

    /**
     * 获得当前请求来自的页面
     *
     * @return
     */
    public static final String getRequestPagePath() {
        return getCurrentPage().getRequestPath();
    }

    /**
     * 获得桌面作用域属性
     *
     * @param name
     * @return
     */
    public static final Object getDesktopAttribute(String name) {
        return getCurrentDesktop().getAttribute(name);
    }

    /**
     * 获得指定id的page
     * <p>
     * 需要注意到是：例如在page1中包含iframe，iframe包含的页面为page2，那么zk将为page2新建一个桌面对象desktop2，
     * 因此page1与page2属于不同的桌面， 当你在page2的一个按钮或所属的其他组件触发的事件中
     * 使用该方法获得page1的子页面的时候，当前动作请求所属桌面为desktop2,而不是page1所属的desktop1，
     * 因此你无法从desktop2中查找属于desktop1的页面
     *
     * @param pageId 页面的id
     * @return 页面对戏那个
     * @see Desktop#getPage(String)
     */
    public static final Page getPage(String pageId) {
        return getCurrentDesktop().getPage(pageId);
    }

    /**
     * 请求转发
     * <p>
     * 调用这个方法的时机：非事件处理内调用，在事件处理时必须调用{@link #sendRedirect(String)}
     *
     * @param pageUri
     * @throws IOException
     */
    public static final void forward(String pageUri) throws IOException {
        Executions.forward(pageUri);
    }

    /**
     * 重定向指定页面
     * <p>
     * 该方法的行为:如果用户尚未看到响应结果，该方法发送重定向状态码！ 如果用户已经看到响应，在用户事件中调用该方法时，zk向客户端发送一个
     * {@link AuSendRedirect}指令,客户端引擎调用redirect js函数（zAu.cmd0.redirect(String
     * uri,String target)），修改浏览器地址栏里的url,而不是传统我们说的重定向
     *
     * @param pageUri
     */
    public static final void sendRedirect(String pageUri) {
        Executions.sendRedirect(pageUri);
    }

    /**
     * 向当前execution提交一个事件
     * <p>
     * 将事件提交到事件队列末尾，然后立即返回。 队列中排在前面的事件处理完毕后执行该动作提交的事件。
     *
     * @param event
     */
    public static final void postEvent(Event event) {
        Events.postEvent(event);
    }

    /**
     * 向当前execution提交一个事件，可以设置事件的优先级
     * <p>
     * 将事件提交到事件队列末尾，然后立即返回。 队列中排在前面的事件处理完毕后执行该动作提交的事件。
     *
     * @param event
     * @priority
     */
    public static final void postEvent(int priority, Event event) {
        Events.postEvent(priority, event);

    }

    /**
     * 向目标组件发送指定名称的事件
     * <p>
     * 将事件提交到事件队列末尾，然后立即返回。 队列中排在前面的事件处理完毕后执行该动作提交的事件。
     */
    public static final void postEvent(String name, Component target,
                                       Object data) {
        Events.postEvent(name, target, data);
    }

    /**
     * 向当前execution发送一个事件
     * <p>
     * 事件处理线程和调用该方法的线程为同一线程，即二者为相同线程，所以必须等待事件处理完毕，该方法才会返回。
     * <p>
     * 如果目标事件的的处理器，是一个长操作，那么当前线程将长事件阻塞，而在客户端表现为：左上角一直出现"正在处理，请稍候..."等字样的提示，
     * 所以在使用前注意
     *
     * @param event
     */
    public static final void sendEvent(Event event) {
        Events.sendEvent(event);
    }

    /**
     * 向指定组件发送事件
     * <p>
     * 事件处理线程和调用该方法的线程为同一线程，即二者为相同线程，所以必须等待事件处理完毕，该方法才会返回
     * <p>
     * 如果目标事件的的处理器，是一个长操作，那么当前线程将长事件阻塞，而在客户端表现为：左上角一直出现"正在处理，请稍候..."等字样的提示，
     * 所以在使用前注意
     *
     * @param comp  目标组件
     * @param event
     */
    public static final void sendEvent(Component comp, Event event) {
        Events.sendEvent(comp, event);
    }

    /**
     * 向目标组件发送指定名称的事件
     * <p>
     * <p>
     * 事件处理线程和调用该方法的线程为同一线程，即二者为相同线程，所以必须等待事件处理完毕，该方法才会返回
     * <p>
     * 如果目标事件的的处理器，是一个长操作，那么当前线程将长事件阻塞，而在客户端表现为：左上角一直出现"正在处理，请稍候..."等字样的提示，
     * 所以在使用前注意
     *
     * @param name   事件名称
     * @param target 目标组件
     * @param data   事件携带的数据，可以调用在事件监听器中使用<code>Event.getData()</code>获得该数据
     */
    public static final void sendEvent(String name, Component target,
                                       Object data) {
        Events.sendEvent(name, target, data);
    }

    /**
     * 获取ForwardEvent事件的原始事件
     *
     * @param event
     * @return
     */
    public static final Event getRealOrigin(ForwardEvent event) {
        return Events.getRealOrigin(event);
    }

    /**
     * 给指定的组件添加controller对象中定义的onXxx事件处理器，该controller是一个
     * 包含onXxx方法的POJO对象，该工具方法将onXxx方法注册给指定组件，因此你不用通过{@link EventListener}
     * 一个一个的向组件注册了
     * <p>
     * 所有在controller对象中以"on"开头的公共方法被作为事件处理器，并且相关事件同时也被监听，例如，
     * 如果controller对象有一个名字为onOk的方法，那么 onOk事件将被监听，然后当接收到onOk事件的时候， onOk方法被调用
     *
     * @param comp       the component to be registered the events
     * @param controller a POJO file with onXxx methods(event handlers)
     * @see GenericEventListener
     * @since 3.0.6
     */
    public static final void addEventListeners(Component comp, Object controller) {
        Events.addEventListeners(comp, controller);
    }

    /**
     * 检测名称是否是一个合法的zk事件名
     *
     * @param name
     * @return
     */
    public static final boolean isValidEventName(String name) {
        return Events.isValid(name);
    }

    /**
     * 判断一个指定事件的组件是否有事件处理器或监听器
     *
     * @param comp
     * @param evtnm
     * @param asap  是否仅检测非延迟事件监听器，例如实现org.zkoss.zk.ui.event.Deferrable或
     *              org.zkoss.zk.ui.event.Deferrable.isDeferrable 返回 false的监听器
     * @return
     */
    public static final boolean isListened(Component comp, String evtnm,
                                           boolean asap) {
        return Events.isListened(comp, evtnm, asap);
    }

    /**
     * 从uri指定的文件创建组件
     * <p>
     * 如果uri的页面内包含&lt;?page id=&quot;pageId&quot;
     * title=&quot;这个是标题&quot;?&gt;页面指令
     * ，在createComponents中该语句指令失效，createComponents方法不会因此创建Page对象， 当然
     * <code>desktop.getPage("pageId")</code的时候返回的是null
     *
     * @param uri
     * @param parent 创建的组件所属的父组件F
     * @param args   创建组件传递的参数
     * @return 创建的组件，该组件对象为uri页面的第一个组件（zk节点除外）
     */
    public static final Component createComponents(String uri,
                                                   Component parent, @SuppressWarnings("rawtypes") Map args) {
        return Executions.createComponents(uri, parent, args);
    }

    /**
     * 从zul格式字符串创建组件，创建的组件将作为第二个参数的子组件，如果第二个参数为null,
     * 那么创建的组件将作为当前Execution关联的page的子组件；
     * <p>
     * 如果content内包含&lt;?page id=&quot;pageId&quot;
     * title=&quot;这个是标题&quot;?&gt;页面指令
     * ，在createComponents中该语句指令失效，createComponents方法不会因此创建Page对象， 当然
     * <code>desktop.getPage("pageId")</code的时候返回的是null
     *
     * @param content zul格式内容的字符串
     * @param parent  父组件，如果为null,那么组件所属的页面为当前页面，当前页面由execution上下文决定。
     *                另外新的组件将作为当前页面的根组件
     * @param args    一个map类型的参数， 传递的参数可以使用{@link Executions# getArgs()}获得
     * @return 根据content创建的组件第一个组件
     */
    public static final Component createComponentsDirectly(String content,
                                                           Component parent, @SuppressWarnings("rawtypes") Map args) {
        return Executions
                .createComponentsDirectly(content, "zul", parent, args);
    }

    /**
     * 重绘组件
     * <p>
     * 仅允许在<b>请求处理阶段</b>和<b>事件处理阶段</b>调用， 不允许在<b>呈现阶段</b>调用
     *
     * @param comp
     */
    public static final void redraw(Component comp) {
        comp.invalidate();
    }

    /**
     * 重绘页面
     * <p>
     * 仅允许在<b>请求处理阶段</b>和<b>事件处理阶段</b>调用， 不允许在<b>呈现阶段</b>调用
     *
     * @param page
     */
    public static final void redrawPage(Page page) {
        page.invalidate();
    }

    /**
     * 弹出消息提示框
     *
     * @param message 提示消息
     * @param title   提示框标题
     */
    public static final void showInformation(String message, String title) {
        try {
            Messagebox.show(message, title, Messagebox.OK, Messagebox.INFORMATION);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 弹出警告提示框
     *
     * @param message 提示消息
     * @param title   提示框标题
     *                InterruptedException e
     */
    public static final void showExclamation(String message, String title) {
        try {
            Messagebox.show(message, title, Messagebox.OK, Messagebox.EXCLAMATION);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 弹出消息提示框
     *
     * @param message 提示消息
     * @param title   提示框标题
     */
    public static final void showError(String message, String title) {
        try {
            Messagebox.show(message, title, Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 询问提示框
     * <p>
     * 如果禁用事件处理线程，该方法会立即返回，返回值永远为true。 如果作为if判断语句的条件，
     * 那么else部分永远不会执行，启用和开启事件处理请查看zk.xml配置: <br />
     * &lt;system-config&gt;<br />
     * &lt;disable-event-thread&gt;false&lt;/disable-event-thread&gt;<br />
     * &lt;/system-config&gt;
     *
     * @param message 提示消息 提示框标题
     * @return 禁用事件处理线程该方法永远返回true，启用事件处理相称时，如果用户点击ok按钮，返回true,反之false
     */
    public static final boolean showQuestion(String message, String title) {
        try {
            return Messagebox.OK == Messagebox.show(message, title,
                    Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
        } catch (Exception e) {
            // ignore
            return false;
        }
    }

    /**
     * 询问提示框
     * <p>
     * 该方法是一个类似 {@link #showQuestion(String, String)}
     * 的方法，但与其不同的是，当禁用事件处理线程时，该方法非常有用。
     * <p>
     * <p>
     * <p>
     * 示例:<br />
     * <hr>
     * <p>
     * <pre>
     * ZkUtils.showQuestion(&quot;您确定删除该记录吗？&quot;, &quot;询问&quot;, new EventListener() {
     *  &#064;Override
     *  public void onEvent(Event event) throws Exception {
     *      int clickedButton = (Integer) event.getData();
     *      if (clickedButton == Messagebox.OK) {
     *          // 用户点击的是确定按钮
     *      } else {
     *          // 用户点击的是取消按钮
     *      }
     *  }
     *
     * });
     * </pre>
     * <p>
     * <hr>
     * <p>
     * <p>
     * <table border="1">
     * <tr>
     * <td>按钮名称</td>
     * <td>事件名称</td>
     * </tr>
     * <tr>
     * <td>确定</td>
     * <td>onOK</td>
     * </tr>
     * <tr>
     * <td>取消</td>
     * <td>onCancel</td>
     * </tr>
     * </table>
     *
     * @param message
     * @param title
     * @param eventListener
     */
    public static final void showQuestion(String message, String title,
                                          EventListener eventListener) {
        try {
            Messagebox.show(message, title, Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, eventListener);
        } catch (Exception e) {
            // ignore
        }

    }

    /**
     * 给指定组件添加错误提示
     * <p>
     * 清除错误，需要使用{@link #clearWrongValue(Component)}
     *
     * @param comp
     * @param message 错误提示消息
     * @see #clearWrongValue(Component)
     */
    public static final void addWrongValue(Component comp, String message) {
        Clients.wrongValue(comp, message);
    }

    /**
     * 清除指定组件的错误提示
     * <p>
     * 例如在输入组件中指定constraint属性验证用户输入，输入错误时，弹出提示， 该 方法可以清除这个提示框
     *
     * @param comp
     */
    public static final void clearWrongValue(Component comp) {
        Clients.clearWrongValue(comp);
    }

    /**
     * 清除列表中组件的错误提示
     *
     * @see #clearWrongValue(Component)
     */
    public static final void clearWrongValue(
            @SuppressWarnings("rawtypes") List comps) {
        Clients.clearWrongValue(comps);

    }

    /**
     * 设置或删除widget的事件监听器，如果已经有同样的事件监听，那么上一个将被替换 *
     * <p>
     * <pre>
     * ZkUtils.addWidgetEventListener(txtAge, &quot;onKeyPress&quot;, &quot;&quot;
     *      + &quot;    if(event.keyCode&lt;48||event.keyCode&gt;57){         &quot;
     *      + &quot;       return false;                                 &quot; + &quot;     }   &quot;
     *      + &quot;   &quot;);
     * </pre>
     * <p>
     * *
     * <p>
     * 与comp.addEventListener()和<component
     * onClick=""/>中的事件处理（EventHandler）不同，该事件处理运行于客户端
     *
     * @param comp
     * @param evtName 事件名称,例如onClick
     * @param script  javascript脚本代码，书写格式可按照html事件中js代码格式,如果为空，那么事件处理程序被删除
     */
    public static final void setWidgetEventListener(Component comp,
                                                    String evtName, String script) {
        comp.setWidgetListener(evtName, script);
    }

    /**
     * 向指定组件事件追加事件监听器
     * <p>
     * <pre>
     *
     * ZkUtils.addWidgetEventListener(txtAge, &quot;onKeyPress&quot;, &quot;&quot;
     *      + &quot;    if(event.keyCode&lt;48||event.keyCode&gt;57){         &quot;
     *      + &quot;       return false;                                 &quot; + &quot;     }   &quot;
     *      + &quot;   &quot;);
     * </pre>
     * <p>
     * 与comp.addEventListener()和<component
     * onClick=""/>中的事件处理（EventHandler）不同，该事件处理运行于客户端
     *
     * @param comp
     * @param evtnm  事件名称,例如onClick
     * @param script javascript脚本代码，书写格式可按照html事件中js代码格式
     */
    public static final void addWidgetEventListener(Component comp,
                                                    String evtnm, String script) {
        if (script == null || "".equals(script.trim())) {
            return;
        }
        String oldScript = comp.getWidgetListener(evtnm);
        if (oldScript == null) {
            oldScript = "";
        }
        comp.setWidgetListener(evtnm, oldScript + script);

    }

    /**
     * 验证表单
     * <p>
     * 需要input元素的constraint属性的支持
     * <p>
     * <p>
     * 例如 年龄&lt;textbox constraint=&quot;/^[0-9]*$/:仅允许输入数字&quot;/&gt;
     *
     * @param formContainer Input元素公共组件，即需要验证的输入元素所在的公共容器组件，这个form在zk里是虚拟的，
     *                      任何容器组件都可以是一个form容器
     * @return 如果验证成功返回true, 否则返回false
     */
    public static boolean validateForm(Component formContainer) {
        return validateForm(formContainer, true);
    }

    /**
     * 验证表单
     * <p>
     * 需要input元素的constraint属性的支持
     * <p>
     * 例如 年龄 &lt;textbox constraint=&quot;/^[0-9]*$/:仅允许输入数字&quot;/&gt;
     *
     * @param formContainer Input元素公共组件，即需要验证的输入元素所在的公共容器组件，这个form在zk里是虚拟的，
     *                      任何容器组件都可以是一个form容器
     * @param showError     是否显示错误提示
     * @return 如果验证成功返回true, 否则返回false
     */
    public static boolean validateForm(Component formContainer,
                                       boolean showError) {
        try {
            validateForm0(formContainer, showError);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void validateForm0(Component formContainer, boolean showError) {
        @SuppressWarnings("unchecked")
        List<Component> cList = formContainer.getChildren();
        if (cList == null || cList.size() < 1) {
            return;
        } else {
            for (Component c : cList) {
                if (c instanceof InputElement && !((InputElement) c).isValid()) {
                    if (showError) {
                        ((InputElement) c).getText();
                    }
                    throw new RuntimeException("表单输入不正确！");
                } else {
                    validateForm0(c, showError);
                }
            }
        }
    }

    /**
     * 结束长操作处理
     * <p>
     * 一个业务操作可能要一段时间可以处理完成，在处理期间，又不想让用户操作界面，影响业务处理等，
     * 那么可以在前台事件中调用zk.startProcessing(),此时左上角出现提示框，"正在处理,请稍候..."， 那么待业务处理过后再try
     * catch finally{}块里调用该方法，通知客户端操作完毕
     */
    public static final void endProcessing() {
        Clients.evalJavaScript("zk.endProcessing();");
    }

    /**
     * 检查字段是否非空
     *
     * @param entity
     * @param ignoreFields
     * @return
     */
    public static Boolean checkFieldValid(Object entity, List<String> ignoreFields) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName() + ":" + field.isAccessible());
            if (field.isAccessible()) {
                System.out.println(field.getName());
                if (!ignoreFields.contains(field.getName())) {
                    try {
                        if (field.get(entity) == null || StringUtils.isBlank(field.get(entity).toString())) {
                            ZkUtils.showExclamation(field.getName() + "为空!\r蓝色边框的内容不能为空", "系统提示");
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        ZkUtils.showError(e.getMessage(), "系统提示");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}