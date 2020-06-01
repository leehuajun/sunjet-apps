package com.sunjet.vm.base;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.*;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.NoticeEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.ServiceHelper;
import com.sunjet.service.admin.ConfigService;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.NoticeService;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.activiti.CustomComment;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.JsonHelper;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.utils.zk.NoEmptyConstraint;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.helper.IconVariable;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 基础 ViewEntity
 * <p>
 * Created by lhj on 15/11/6.
 */
public class BaseVM {
    protected final static Logger logger = LoggerFactory.getLogger(BaseVM.class);
    @WireVariable
    protected IconVariable iconVariable;        // 图标列表类
    @WireVariable
    protected ProcessService processService;    // 流程服务对象
    @WireVariable
    protected NoticeService noticeService;      // 通知公告服务对象
    @WireVariable
    protected ServiceHelper serviceHelper;      // 辅助服务类
    @WireVariable
    protected ConfigService configService;      // 配置服务
    @WireVariable
    protected UserService userService;          // 用户服务
    @WireVariable
    protected DealerService dealerService;      // 服务站服务
    @WireVariable
    protected AgencyService agencyService;      // 合作商服务
    protected Window win;
    @WireVariable("customCacheManager")
    protected CacheManager cacheManager;
    private NoEmptyConstraint noEmptyConst = new NoEmptyConstraint();       // 表单非空校验器
    private String emptyMessage = "暂时没有找到任何记录!";
    private String themeName;
    private String app_name;
    private String app_logo;
    private String app_version;   //版本号
    private String technicalSupport;   //技术支持
    private Map<String, String> users = new HashMap<>();
    private String keyword = "";
    private DealerEntity dealer;
    private AgencyEntity agency;
    private String userType = "";
    private List<DealerEntity> dealers = new ArrayList<>();
    private List<AgencyEntity> agencies = new ArrayList<>();
    private ProvinceEntity selectedProvince;
    private List<ProvinceEntity> provinceEntities;
    private List<Task> showTasks = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private List<NoticeEntity> notices = new ArrayList<>();
    private List<MessageEntity> messages = new ArrayList<>();
    private List<ProcessInstance> processInstances = new ArrayList<>();
    private Map<String, String> mapRequestOrg = new HashMap<>();
    private Map<String, ProcessDefinition> mapProcessDefinition = new HashMap<>();

    public Map<String, String> getMapRequestOrg() {
        return mapRequestOrg;
    }

    public void setMapRequestOrg(Map<String, String> mapRequestOrg) {
        this.mapRequestOrg = mapRequestOrg;
    }

    public List<Task> getShowTasks() {
        return showTasks;
    }

    public NoEmptyConstraint getNoEmptyConst() {
        return noEmptyConst;
    }

    public List<NoticeEntity> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticeEntity> notices) {
        this.notices = notices;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public Map<String, ProcessDefinition> getMapProcessDefinition() {
        return mapProcessDefinition;
    }

    public String getApp_name() {
        return CacheManager.getConfigValue("app_name");
    }

    public String getApp_logo() {
        return CacheManager.getConfigValue("app_logo");
    }

    public String getApp_version() {
        ConfigEntity app_version = configService.findOne("app_version");
        return app_version.getConfigValue();
    }

    public String getTechnicalSupport() {
        return CacheManager.getConfigValue("technicalSupport");
    }

    public String getThemeName() {
        return CacheManager.getConfigValue("theme_name");
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public String getWindow_position() {
        return CacheManager.getConfigValue("dialog_position");
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<DealerEntity> getDealers() {
        return dealers;
    }

    public void setDealers(List<DealerEntity> dealers) {
        this.dealers = dealers;
    }

    public List<AgencyEntity> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<AgencyEntity> agencies) {
        this.agencies = agencies;
    }

    public ProvinceEntity getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(ProvinceEntity selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public void setProvinceEntities(List<ProvinceEntity> provinceEntities) {
        this.provinceEntities = provinceEntities;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
        Selectors.wireEventListeners(view, this);
    }

    public CustomComment getBeanFromJson(String json) {
        CustomComment comment;
        try {//        return JsonHelper.json2Bean(json,CustomComment.class);
            comment = JsonHelper.json2Bean(json, CustomComment.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            comment = new CustomComment("", json);
        }
        return comment;
    }

    /**
     * 初始化用户列表
     */
    protected void initUserList() {
        List<UserEntity> userList = userService.findAll();
        users.clear();
        for (UserEntity entity : userList) {
            users.put(entity.getLogId(), entity.getName());
        }
    }

    /**
     * 根据状态索引，获取状态的名称
     *
     * @param index
     * @return
     */
    public String getStatusName(Integer index) {
        if (index != null) {
            return DocStatus.getName(index);
        }
        return null;
    }

    /**
     * 初始化流程定义列表
     */
    protected void initProcessDefinition() {
        this.mapProcessDefinition.clear();
        List<ProcessDefinition> list = processService.findProcessDefinitionList();
        for (ProcessDefinition pd : list) {
            mapProcessDefinition.put(pd.getId(), pd);
        }
    }

    /**
     * 判断当前用户是否具有 permission 权限
     * <p>
     * permission 字符串 :   资源:操作        user:save
     *
     * @param permission
     * @return
     */
    public Boolean hasPermission(String permission) {
        LoggerUtil.getLogger().info(CommonHelper.getActiveUser().getLogId());
        if (CommonHelper.getActiveUser().getLogId().equals("admin"))
            return true;
        else
            return SecurityUtils.getSubject().isPermitted(permission);
    }

    /**
     * 测试所有的判断都返回true，表示都有权限
     *
     * @return
     */
    public Boolean hasPermission() {
        return true;
    }


//    public void handleTask(Task task,String formUrl,String title) throws TabDuplicateException {

    /**
     * 根据业务对象，打开业务处理页面（如新增、编辑、流程处理）
     *
     * @param flowDocEntity
     */
    @Command
    public void handleTaskByEntity(@BindingParam("entity") FlowDocEntity flowDocEntity, @BindingParam("formUrl") String formUrl) {
        Map<String, Object> map = new HashMap<>();
        if (flowDocEntity == null) {   // 新增
            map.put("businessId", "");
//            map.put("status", DocStatus.DRAFT.getIndex());
        } else {
//            map.put("status", flowDocEntity.getStatus());
            map.put("businessId", flowDocEntity.getObjId());
        }
        win = (Window) ZkUtils.createComponents(formUrl, null, map);
        win.doModal();
    }

    /**
     * 根据任务对象，打开业务处理页面（如编辑、流程处理）
     *
     * @param task
     */
    @Command
    public void handleTaskByTask(@BindingParam("task") Task task) {
        String formUrl = processService.findFormUrl(task);
        String businessId = processService.findBusinessIdByTaskId(task.getId());
        if (formUrl == null || formUrl.equals("")) {
            ZkUtils.showError("流程定义文件中没有定义formUrl属性！", "系统提示");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", task.getId());
//        map.put("status", DocStatus.getDocStatus(flowDocEntity.getStatus()));
        map.put("businessId", businessId);
        win = (Window) ZkUtils.createComponents(formUrl, null, map);
        win.doModal();
    }

//    public void handleTaskByBusinessId(@BindingParam("businessId")String businessId){
//        Map<String, Object> map = new HashMap<>();
//        if (StringUtils.isBlank(businessId)) {   // 新增
//            map.put("status", DocStatus.DRAFT);
//        } else {
//            // 0:草稿  1:审核中  2:已审核  3:正常关闭  -1:异常关闭
////            map.put("status", DocStatus.getDocStatus(flowDocEntity.getStatus()));
//            map.put("businessId", businessId);
//        }
//        win = (Window) ZkUtils.createComponents(formUrl, null, map);
//        win.doModal();
//    }

    /**
     * 获取任务中心 任务列表
     *
     * @return
     */
    protected List<Task> getTaskList() {
        List<Task> tasks = processService.findAllTaskList(CommonHelper.getActiveUser().getLogId());
        Set<String> processInstanceIds = new HashSet<>();
        for (Task task : tasks) {
            processInstanceIds.add(task.getProcessInstanceId());
        }
        if (processInstanceIds.size() > 0)
            processInstances = processService.findProcessInstanceByIds(processInstanceIds);
//        for (Task task : tasks) {
//            if (task.getTaskLocalVariables() == null) {
//                System.out.println("流程变量为空");
//            } else {
//                System.out.println("流程变量不为空");
//                if (task.getProcessVariables().get("userId") == null) {
//                    System.out.println("不存在变量userId");
//
//                } else {
//                    System.out.println(task.getProcessVariables().get("userId").toString());
//                }
//            }
//
//
//            try {
//                TaskEntity taskEntity = (TaskEntity) task;
//                if (taskEntity.getProcessInstance() == null) {
//                    System.out.println("流程实例为空");
//                } else {
//                    System.out.println("流程实例不为空");
//                    if (taskEntity.getProcessInstance().getVariable("userId") == null) {
//                        System.out.println("不存在变量userId");
//
//                    } else {
//                        System.out.println(taskEntity.getProcessInstance().getVariable("userId").toString());
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return tasks;
    }

    /**
     * 获取通知公告 内容列表
     *
     * @return
     */
    protected List<NoticeEntity> getNoticeList() {
        return noticeService.findAll();
    }

    /**
     * 更新任务列表
     */
//  @GlobalCommand(GlobalCommandValues.LIST_TASK)
//  @NotifyChange({"tasks", "showTasks"})
//  public void listTask() {
//
////    if (this.win != null) {
////      this.win.detach();
////    }
//
////    this.showTasks.clear();
////    this.tasks = this.getTaskList();
////    for (int i = 0; i < this.tasks.size() && i < CommonHelper.LEN_SHOW; i++) {
////      showTasks.add(this.tasks.get(i));
////    }
////    System.out.println("showTasks.size():" + showTasks.size());
////        tasks = processService.findAllTaskList(CommonHelper.getActiveUser().getLogId());
//  }

    /**
     * 跟据服务站 获取服务站的当前工时单价
     *
     * @param dealer 服务站
     * @return
     */
    protected Double getHourPriceByDealer(DealerEntity dealer) {
        String result = "1";
        if (dealer.getCode() != null && dealer.getCode().length() > 4) {
            result = dealer.getCode().substring(3, 4);   // 返回城市等级编码
        }
        Double price = 0.0;
        /** 判断服务站的星级 (暂缺，待补充)*/
        if (StringUtils.isNotBlank(dealer.getStar())) {
            /** 下面判断城市 */
            List<DictionaryEntity> stars = CacheManager.getDictionariesByParentCode("10010");
            String prices = "";
            for (DictionaryEntity de : stars) {
                if (de.getName().equals(dealer.getStar())) {
                    prices = de.getValue();
                    break;
                }
            }

            if (result.equals("1")) { // 一类城市
                price = Double.parseDouble(prices.split(",")[1]);
            } else {
                price = Double.parseDouble(prices.split(",")[0]);
            }
        }


        /** 下面判断是否严寒月份 */
        if (!dealer.getProvince().getCold()) {
            return price;
        }

        List<String> months = Arrays.asList(CacheManager.getConfigValue("frigid_months").split(","));
        List<Integer> frigid_months = new ArrayList<>();
        for (String str : months) {
            frigid_months.add(Integer.parseInt(str));
        }

        if (frigid_months.contains(LocalDate.now().getMonth().getValue())) {
            price = price + Double.parseDouble(CacheManager.getConfigValue("frigid_subsidy"));
        }

        return price;
    }

    @Command
    @NotifyChange("dealers")
    public void searchDealers(@BindingParam("model") String keyword) {
//        this.dealers = dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword);
        if (CommonHelper.getActiveUser().getDealer() != null) {   // 服务站用户
            if (CommonHelper.getActiveUser().getDealer().getParent() != null) {  // 是二级服务站
                this.dealers.clear();
                this.dealers.add(CommonHelper.getActiveUser().getDealer());  // 二级服务站只能看自己的
//                if(CommonHelper.getActiveUser().getDealer().getCode().contains(keyword)
//                        || CommonHelper.getActiveUser().getDealer().getName().contains(keyword)){
//                    this.dealers.add(CommonHelper.getActiveUser().getDealer());
//                }
//                if(CommonHelper.getActiveUser().getDealer().getParent().getCode().contains(keyword)
//                        || CommonHelper.getActiveUser().getDealer().getParent().getName().contains(keyword)){
//                    this.dealers.add(CommonHelper.getActiveUser().getDealer().getParent());
//                }


            } else {   // 一级服务站
                this.dealers = dealerService.findChildrenByParentIdAndFilter(CommonHelper.getActiveUser().getDealer().getObjId(), keyword.trim());
                this.dealers.add(CommonHelper.getActiveUser().getDealer());
            }
        } else if (CommonHelper.getActiveUser().getAgency() != null) {   // 合作商
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
            this.dealers.clear();
        } else {   // 五菱用户
            UserEntity userEntity = userService.findOneWithRoles(CommonHelper.getActiveUser().getUserId());
            Set<RoleEntity> roles = userEntity.getRoles();
            boolean Permissions = false;
            for (RoleEntity role : roles) {
                if (role.getName().equals("服务经理")) {
                    Permissions = true;
                }
            }
            if (Permissions) {
                //服务经理
                this.dealers = dealerService.findAllByServiceManager(CommonHelper.getActiveUser().getUserId(), keyword.trim());
            } else {
                //五菱其他用户
                this.dealers = dealerService.findAllByKeyword(keyword);

            }
        }
    }


    @Command
    @NotifyChange({"dealer", "keyword"})
    public void clearSelectedDealer() {
        this.dealer = CommonHelper.getActiveUser().getDealer();
        this.setKeyword("");
    }

    @Command
    @NotifyChange({"dealer", "selectedProvince", "keyword", "dealers"})
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.setKeyword("");
        this.dealers.clear();
        this.dealer = dealer;
        this.selectedProvince = dealer.getProvince();
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgencies(@BindingParam("model") String keyword) {
        if (CommonHelper.getActiveUser().getAgency() != null) {   // 合作库用户
            this.agencies.clear();
            this.agencies.add(CommonHelper.getActiveUser().getAgency());
        } else if (CommonHelper.getActiveUser().getDealer() != null) {  // 服务站用户
            this.agencies.clear();
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword.trim());
        }
    }

    @Command
    @NotifyChange({"agency", "keyword"})
    public void clearSelectedAgency() {
        this.agency = CommonHelper.getActiveUser().getAgency();
        this.setKeyword("");
    }

    @Command
    @NotifyChange({"agency", "agencies", "keyword", "selectedProvince"})
    public void selectAgency(@BindingParam("model") AgencyEntity agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agency = agency;
        this.selectedProvince = agency.getProvince();
    }


    @Command
    @NotifyChange({"dealer", "dealers", "agency", "agencies", "keyword"})
    public void selectProvince() {
        this.dealer = null;
        this.dealers.clear();
        this.agency = null;
        this.agencies.clear();
        this.setKeyword("");
    }


    public String getDate(Date date) {
        String strDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            strDate = dateFormat.format(date);
        }
        return strDate;
    }

    public String getSubmitterName(String processInstanceId) {
        String result = "";
        for (ProcessInstance pi : this.processInstances) {
            if (pi.getId().equals(processInstanceId)) {
                String[] items = pi.getBusinessKey().split("\\.");
                if (items.length < 4) {
                    result = "";
                } else {
                    result = items[3];
                }
                break;
            }
        }
        return result;
    }

    public String getSubmitter(String processInstanceId) {
        String result = "";
        for (ProcessInstance pi : this.processInstances) {
            if (pi.getId().equals(processInstanceId)) {
                String[] items = pi.getBusinessKey().split("\\.");
                if (items.length < 5) {
                    result = "";
                } else {
                    result = items[4];
                }
                break;
            }
        }
        return result;
    }


    public String getDocNo(String processInstanceId) {
        String result = "";
        for (ProcessInstance pi : this.processInstances) {
            if (pi.getId().equals(processInstanceId)) {
                String[] items = pi.getBusinessKey().split("\\.");
                if (items.length < 3) {
                    result = "";
                } else {
                    result = items[2];
                }
                break;
            }
        }
        return result;
    }

    protected void initRequestOrg() {
//      System.out.println(this.getTasks().get(0).toString());
        this.mapRequestOrg.clear();
        List<UserEntity> userList = userService.findAll();
        for (UserEntity userEntity : userList) {
            if (userEntity.getUserType() == null || userEntity.getUserType() == "") {
                continue;
            }
            if (userEntity.getUserType().equals("dealer")) {
                if (userEntity.getDealer() != null) {
                    this.mapRequestOrg.put(userEntity.getLogId(), userEntity.getDealer().getName());
                }
            } else if (userEntity.getUserType().equals("agency")) {
                if (userEntity.getAgency() != null) {
                    this.mapRequestOrg.put(userEntity.getLogId(), userEntity.getAgency().getName());
                }

            } else if (userEntity.getUserType().equals("wuling")) {
                this.mapRequestOrg.put(userEntity.getLogId(), "五菱销售公司");
            }
        }
    }

    /**
     * 状态颜色
     *
     * @param colorIdx
     * @return
     */
    public String getColor(Integer colorIdx) {
        if (colorIdx == null) {
            return "";
        } else if (colorIdx == DocStatus.WAITING_SETTLE.getIndex()) {
            return "color:#F2DA0C;font-weight:700";
        } else if (colorIdx == DocStatus.SETTLING.getIndex()) {
            return "color:#D47616 ;font-weight:700";
        } else if (colorIdx == DocStatus.SETTLED.getIndex()) {
            return "color:#7BC144;font-weight:700";
        } else if (colorIdx == DocStatus.DRAFT.getIndex()) {
            return "color:#C7DD0E;font-weight:700";
        } else if (colorIdx == DocStatus.AUDITING.getIndex()) {
            return "color:#FF9900;font-weight:700";
        } else if (colorIdx == DocStatus.CLOSED.getIndex()) {
            return "color:#cccccc;font-weight:700";
        } else if (colorIdx == DocStatus.REJECT.getIndex()) {
            return "color:#FF3333;font-weight:700";
        } else if (colorIdx == DocStatus.SUSPEND.getIndex()) {
            return "color:#B20825;font-weight:700";
        } else if (colorIdx == DocStatus.OBSOLETE.getIndex()) {
            return "color:#999999;font-weight:700";
        } else {
            return "";
        }
    }
}
