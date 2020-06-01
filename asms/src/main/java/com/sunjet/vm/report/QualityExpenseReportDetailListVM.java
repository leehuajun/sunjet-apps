package com.sunjet.vm.report;

import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.ExcelUtil;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowListBaseVM;
import org.springframework.jdbc.core.JdbcTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class QualityExpenseReportDetailListVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String costType = "";
    private String serviceManager = "";
    private String reportType = "";
    private String type = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableExport(true);
        this.setEnableAdd(false);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM quality_expense_reports_detail " +
                        "WHERE cost_type LIKE  '%" + this.getCostType() + "%' " +
                        "AND report_type LIKE '%" + this.getReportType() + "%' " +
                        "AND type LIKE '%" + this.getType() + "%' " +
                        "AND service_manager LIKE '%" + this.getServiceManager() + "%' " +
                        "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                        "AND '" + DateHelper.dateToString(this.getEndDate()) + "'");


    }


    @Command
    @NotifyChange("maps")
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");

        } else {
            maps.clear();
            maps = jdbcTemplate.queryForList(
                    "SELECT * FROM quality_expense_reports_detail " +
                            "WHERE cost_type LIKE  '%" + this.getCostType() + "%' " +
                            "AND report_type LIKE '%" + this.getReportType() + "%' " +
                            "AND type LIKE '%" + this.getType() + "%' " +
                            "AND service_manager LIKE '%" + this.getServiceManager() + "%' " +
                            "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                            "AND '" + DateHelper.dateToString(this.getEndDate()) + "'");
        }
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     * @param searchDTO
     */
    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("createdTime", SearchOrder.OrderType.DESC, 1)
//        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
        ));
    }

    @Command
    @Override
    @NotifyChange("*")
    public void reset() {
        super.reset();
        this.costType = "";
        this.serviceManager = "";
        this.reportType = "";
        this.type = "";
    }


    @Command
    public void exportExcel() {
        if (maps.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        }
        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("速报标题");
        titleList.add("单据编号");
        titleList.add("状态");
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("车辆分类");
        titleList.add("费用级别");
        titleList.add("速报级别");
        titleList.add("申请人");
        titleList.add("申请人电话");
        titleList.add("服务经理");
        titleList.add("服务经理电话");
        titleList.add("备注");
        titleList.add("故障时行驶状态");
        titleList.add("故障时路面情况");
        titleList.add("故障发生地点");
        titleList.add("初步原因分析");
        titleList.add("处理意见");
        titleList.add("预计费用");
        titleList.add("VIN");
        titleList.add("车主");
        titleList.add("车主电话");
        titleList.add("购车日期");
        titleList.add("行驶里程");
        titleList.add("申请时间");
        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("title");
        keyList.add("doc_no");
        keyList.add("status");
        keyList.add("dealer_code");
        keyList.add("dealer_name");
        keyList.add("vehicle_type");
        keyList.add("cost_type");
        keyList.add("report_type");
        keyList.add("submitter_name");
        keyList.add("submitter_phone");
        keyList.add("service_manager");
        keyList.add("service_manager_phone");
        keyList.add("comment");
        keyList.add("fault_status");
        keyList.add("fault_road");
        keyList.add("fault_address");
        keyList.add("initial_reason");
        keyList.add("decisions");
        keyList.add("estimated_cost");
        keyList.add("vin");
        keyList.add("owner_name");
        keyList.add("mobile");
        keyList.add("purchase_date");
        keyList.add("mileage");
        keyList.add("created_time");

        ExcelUtil.ListMapToExcel(titleList, keyList, maps);


    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }


    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


