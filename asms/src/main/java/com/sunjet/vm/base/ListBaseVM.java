package com.sunjet.vm.base;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.base.DocEntity;
import com.sunjet.service.base.BaseService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.ResultDTO;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomQuery;
import com.sunjet.utils.exception.TabDuplicateException;
import com.sunjet.utils.zk.ZkTabboxUtil;
import com.sunjet.utils.zk.ZkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import java.util.*;

/**
 * 基础列表型 ViewEntity
 * <p>
 * Created by lhj on 15/11/6.
 */
public class ListBaseVM extends BaseVM {
    protected final static Logger logger = LoggerFactory.getLogger(ListBaseVM.class);
    private BaseService baseService = null;

    // 按钮显示开关
    private Boolean enableAdd = true;         // 增加按钮状态
    private Boolean enableUpdate = true;      // 编辑按钮状态
    private Boolean enableDelete = true;      // 删除按钮状态
    private Boolean enableRefresh = true;     // 刷新按钮状态
    private Boolean enableSearch = true;      // 查询按钮状态
    private Boolean enableReset = true;       // 重置按钮状态
    private Boolean enableAddRecycleDoc = false;  // 创建故障件返回单按钮状态
    private Boolean enableSaveAllocation = false;  // 保存分配信息按钮状态
    private Boolean enableSaveSupply = false; // 创建调拨供货单按钮状态
    private Boolean enableWarrantSettlement = false; // 创建服务结算单单按钮状态
    private Boolean enableFreightSettlement = false; // 创建运费结算单按钮状态
    private Boolean enablePartSettlement = false; // 创建配件结算单按钮状态
    private Boolean enableExport = false; //导出excel按钮

    private Integer headerRows = 1;         // 页面搜索栏的行数，默认是1行

    private String formUrl = "";        // 设置编辑对话框的Url
    protected Window formDialog;        // 用于显示编辑对话框
    protected DocEntity selectedEntity; // 当前选中的实体对象
    private Date startDate = new Date();    // 开始日期，绑定页面搜索的开始日期
    private Date endDate = new Date();      // 结束日期，绑定页面搜索的结束日期
    protected Window dialog;            // 对话框


    //    private String keyword = "";        // 搜索关键字
    private Integer currentPageNo = 0;  // 列表的当前页码
    private ResultDTO<DocEntity> resultDTO = new ResultDTO<>();  // 查询到的的列表数据源


    @Init
    public void listBaseInit() {
        startDate = DateHelper.getFirstOfYear();
        endDate = new Date();
    }

    /**
     * 搜索、刷新列表
     */
    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void filterList() {
        logger.info("filterList()");
        this.resultDTO = query(0);
        currentPageNo = 0;
        System.out.println(resultDTO);
    }

    /**
     * 初始化列表内容，也就是第一次加载列表内容，加载首页
     */
    @Command
    @NotifyChange({"resultDTO"})
    public void initList() {
        this.resultDTO = query(0);
        currentPageNo = 0;
    }

    /**
     * 刷新当前页
     * <p>
     * 所有list页面的『刷新』按钮执行的方法
     */
    @Command
    @NotifyChange("resultDTO")
    public void refreshData() {
        this.resultDTO = query(currentPageNo);
//        currentPageNo = 0;
    }


    @Command
    @NotifyChange("resultDTO")
    public void gotoPageNo(@BindingParam("e") Event event) {
//        System.out.println("Event类型:" + event.getClass().getName().toString());
        PagingEvent pagingEvent = (PagingEvent) event;
        Integer pageNo = pagingEvent.getActivePage();
        this.resultDTO = query(pageNo);
    }

    /**
     * 根据SearchDTO 查询对象,执行分页排序查询,并返回 ResultDTO<DocEntity> 对象
     *
     * @param pageNo 第几页
     * @return
     */
    protected ResultDTO<DocEntity> query(Integer pageNo) {

        ResultDTO _resultDTO = new ResultDTO();
        SearchDTO _searchDTO = new SearchDTO();

        Integer page_size = Integer.parseInt(CacheManager.getConfigValue("page_size"));
        if (page_size <= 0) {
            page_size = calculatePageSize();
        }


//        String cookieValue = ZkUtils.getCookie(CommonHelper.getActiveUser().getUserId(), "poms_page_size");
//        if (!cookieValue.equals("")) {
//            page_size = Integer.parseInt(cookieValue);
//        }
        _searchDTO.setPageSize(page_size);
        _searchDTO.setPageNumber(pageNo);

        // 设置排序条件
        this.configSearchOrder(_searchDTO);
        // 设置查询条件
        this.configSpecification(_searchDTO);


        if (baseService != null) {
            CustomQuery customQuery = new CustomQuery<>(baseService.getJpaSpecificationExecutor());
            logger.info("开始查询数据！！");
            Page page = customQuery.getPageContent(_searchDTO);
            _resultDTO.setTotal(page.getTotalElements());
            _resultDTO.setPageNumber(page.getNumber());
            _resultDTO.setPageSize(page.getSize());
            _resultDTO.setNumberOfCurrentPage(page.getNumberOfElements());

            List<DocEntity> modelList = page.getContent();
            _resultDTO.setPageContent(new ArrayList<>());
            for (DocEntity model : modelList) {
                _resultDTO.getPageContent().add(model);
            }
        }
//        System.out.println(_resultDTO);
        return _resultDTO;
    }


    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     */
    protected void configSearchOrder(SearchDTO searchDTO) {
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
    }

    /**
     * 计算每页显示的记录数
     *
     * @return
     */
    protected Integer calculatePageSize() {
        // 页头：49    tab头：27  margin-top:10  toolbar: 30  paging:32
        // 49  33  8 10 30 32 24

        Integer region_height = CommonHelper.windowHeight - 49 - 33 - 10 - 30 - 32 - 24 - 26 * headerRows;
        Integer page_size = region_height / 28;
        return page_size;
    }

    /**
     * 编辑对象（待删除）
     *
     * @param model
     */
//    @Deprecated
//    @Command
//    @NotifyChange("resultDTO")
//    public void editEntity(@BindingParam("model") DocEntity model) {
//        Map<String, DocEntity> paramMap = new HashMap<>();
//        paramMap.put("model", model);
//        formDialog = (Window) Executions.createComponents(formUrl, null, paramMap);
//        formDialog.setStyle("background:rgb(78,116,149)");
//        formDialog.setStyle("padding:0px;");
//        formDialog.doModal();
//    }

    /**
     * 打开实体对象 编辑对话框  新建/编辑
     *
     * @param entity
     * @param url
     */
    @Command
    public void openDialog(@BindingParam("entity") DocEntity entity, @BindingParam("url") String url) {
        if (url == null || url == "") {
            ZkUtils.showError("URL为空！", "系统异常");
            return;
        }
        this.selectedEntity = entity;
        Map<String, Object> paramMap = new HashMap<>();
        if (entity != null) {
            paramMap.put("objId", entity.getObjId());
        }
        formDialog = (Window) ZkUtils.createComponents(url, null, paramMap);
        formDialog.setStyle("background:rgb(78,116,149)");
        formDialog.setStyle("padding:0px;");
        formDialog.doModal();
    }

    @Command
    public void openTab(@BindingParam("name") String name, @BindingParam("url") String url, @BindingParam("model") DocEntity model) throws TabDuplicateException {
        if (url == null || url == "") {
            ZkUtils.showError("URL为空！", "系统异常");
        }

        Map<String, DocEntity> map = new HashMap<>();
        map.put("model", model);

        name = (model == null ? "新增-" : "查阅-") + name;
        String tabId = CommonHelper.genMD5(name);

        ZkTabboxUtil.newTab(tabId, name, null, true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
    }

    public void refreshList() {
        initList();
        if (formDialog != null) {
            formDialog.detach();
        }
    }

    //public void refreshEntity(@BindingParam("entity") DocEntity entity) {
    public void refreshEntity(DocEntity entity) {
        BeanUtils.copyProperties(entity, this.selectedEntity);
        if (formDialog != null) {
            formDialog.detach();
        }
    }


    public Integer getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(Integer currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public ResultDTO<DocEntity> getResultDTO() {
        return resultDTO;
    }

    public void setResultDTO(ResultDTO<DocEntity> resultDTO) {
        this.resultDTO = resultDTO;
    }

//    public String getKeyword() {
//        return keyword;
//    }
//
//    public void setKeyword(String keyword) {
//        this.keyword = keyword;
//    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    public BaseService getBaseService() {
        return baseService;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public Boolean getEnableAdd() {
        return enableAdd;
    }

    public void setEnableAdd(Boolean enableAdd) {
        this.enableAdd = enableAdd;
    }

    public Boolean getEnableRefresh() {
        return enableRefresh;
    }

    public void setEnableRefresh(Boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    public Boolean getEnableSearch() {
        return enableSearch;
    }

    public void setEnableSearch(Boolean enableSearch) {
        this.enableSearch = enableSearch;
    }

    public Boolean getEnableReset() {
        return enableReset;
    }

    public void setEnableReset(Boolean enableReset) {
        this.enableReset = enableReset;
    }

    public Boolean getEnableUpdate() {
        return enableUpdate;
    }

    public void setEnableUpdate(Boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    public Boolean getEnableDelete() {
        return enableDelete;
    }

    public void setEnableDelete(Boolean enableDelete) {
        this.enableDelete = enableDelete;
    }

    public Boolean getEnableAddRecycleDoc() {
        return enableAddRecycleDoc;
    }

    public void setEnableAddRecycleDoc(Boolean enableAddRecycleDoc) {
        this.enableAddRecycleDoc = enableAddRecycleDoc;
    }

    public Boolean getEnableSaveAllocation() {
        return enableSaveAllocation;
    }

    public void setEnableSaveAllocation(Boolean enableSaveAllocation) {
        this.enableSaveAllocation = enableSaveAllocation;
    }

    public Boolean getEnableSaveSupply() {
        return enableSaveSupply;
    }

    public void setEnableSaveSupply(Boolean enableSaveSupply) {
        this.enableSaveSupply = enableSaveSupply;
    }

    public void setHeaderRows(Integer headerRows) {
        this.headerRows = headerRows;
    }

    public Boolean getEnableExport() {
        return enableExport;
    }

    public void setEnableExport(Boolean enableExport) {
        this.enableExport = enableExport;
    }


    public Boolean getEnableWarrantSettlement() {
        return enableWarrantSettlement;
    }

    public void setEnableWarrantSettlement(Boolean enableWarrantSettlement) {
        this.enableWarrantSettlement = enableWarrantSettlement;
    }

    public Boolean getEnableFreightSettlement() {
        return enableFreightSettlement;
    }

    public void setEnableFreightSettlement(Boolean enableFreightSettlement) {
        this.enableFreightSettlement = enableFreightSettlement;
    }

    public Boolean getEnablePartSettlement() {
        return enablePartSettlement;
    }

    public void setEnablePartSettlement(Boolean enablePartSettlement) {
        this.enablePartSettlement = enablePartSettlement;
    }
}
