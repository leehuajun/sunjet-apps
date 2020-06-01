package com.sunjet.vm.report;

import com.sunjet.cache.CacheManager;
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
 * @author ws
 * @create 2017-6-19 上午11:38
 * <p>
 * 活动通知统计报表
 */
public class activityNoticeReportVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String docNo = "";
    private String vin = "";
    private Integer pageSize;


    @Init(superclass = true)
    public void init() {
        this.pageSize = Integer.parseInt(CacheManager.getConfigValue("report_page_size"));
        this.setEnableAdd(false);
        this.setEnableExport(true);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM activity_notice_report " +
                        "WHERE doc_no LIKE  '%" + this.getDocNo() + "%' " +
                        "AND vin LIKE  '%" + this.getVin() + "%' " +
                        "AND publish_date BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
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
                    "SELECT * FROM activity_notice_report " +
                            "WHERE doc_no LIKE  '%" + this.getDocNo() + "%' " +
                            "AND vin LIKE  '%" + this.getVin() + "%' " +
                            "AND publish_date BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
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


    }

    /**
     * 导出excel
     */
    @Command
    public void exportExcel() {
        if (maps.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        }
        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("VIN");
        titleList.add("VSN");
        titleList.add("车型型号");
        titleList.add("用户");
        titleList.add("电话");
        titleList.add("用户地址");
        titleList.add("购车日期");
        titleList.add("行驶里程");
        titleList.add("状态");
        titleList.add("单据编号");
        titleList.add("开始日期");
        titleList.add("结束日期");
        titleList.add("状态");
        titleList.add("标题");
        titleList.add("发布时间");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("vehicle_model");
        keyList.add("owner_name");
        keyList.add("phone");
        keyList.add("address");
        keyList.add("purchase_date");
        keyList.add("mileage");
        keyList.add("distribute");
        keyList.add("doc_no");
        keyList.add("start_date");
        keyList.add("end_date");
        keyList.add("aanStatus");
        keyList.add("title");
        keyList.add("publish_date");

        ExcelUtil.ListMapToExcel(titleList, keyList, maps);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String getDocNo() {
        return docNo;
    }

    @Override
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}


