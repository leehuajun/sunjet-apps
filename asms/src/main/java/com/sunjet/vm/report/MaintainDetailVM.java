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

//import net.sf.json.JSONArray;

/**
 * Created by lhj on 16/4/27.
 */
public class MaintainDetailVM extends FlowListBaseVM {
    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String dealerName = "";
    private String type = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableExport(true);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM maintain_detail " +
                        "WHERE dealer_name LIKE  '%" + this.getDealerName() + "%' " +
                        "AND type LIKE '%" + this.getType() + "%' " +
                        "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                        "AND '" + DateHelper.dateToString(this.getEndDate()) + "'");
//        maps = jdbcTemplate.queryForList("SELECT  * FROM maintain_detail");

    }


    @Command
    @NotifyChange("maps")
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");

        } else {
            maps.clear();
            if (!"".equals(this.getType())) {
                maps = jdbcTemplate.queryForList(
                        "SELECT * FROM maintain_detail " +
                                "WHERE CONCAT(dealer_code , dealer_name ) LIKE  '%" + this.getDealerName() + "%' " +
                                "AND type LIKE '%" + this.getType() + "%' " +
                                "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                                "AND '" + DateHelper.dateToString(this.getEndDate()) + "'");

            } else {
                maps = jdbcTemplate.queryForList(
                        "SELECT *  FROM maintain_detail " +
                                "WHERE CONCAT(dealer_code , dealer_name ) LIKE  '%" + this.getDealerName() + "%' " +
                                "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                                "AND '" + DateHelper.dateToString(this.getEndDate()) + "'");
            }

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
        this.dealerName = "";
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
        titleList.add("单据编号");
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("经办人");
        titleList.add("经办人电话");
        titleList.add("省份");
        titleList.add("服务经理");
        titleList.add("申请时间");
        titleList.add("进站时间");
        titleList.add("出站时间");
        titleList.add("服务站星级");
        titleList.add("单据类型");
        titleList.add("质量速报");
        titleList.add("费用速报");
        titleList.add("活动单");
        titleList.add("夜间补贴");
        titleList.add("首保费用标准");
        titleList.add("工时单价");
        titleList.add("项目工时费用");
        titleList.add("外出工时补贴");
        titleList.add("外出费用合计");
        titleList.add("其他费用");
        titleList.add("辅料费用合计");
        titleList.add("配件费用合计");
        titleList.add("费用合计");
        titleList.add("应结算辅料费");
        titleList.add("应结算配件费用");
        titleList.add("应结算费用");
        titleList.add("送修人");
        titleList.add("送修人电话");
        titleList.add("开工日期");
        titleList.add("完工日期");
        titleList.add("主修人");
        titleList.add("维修类别");
        titleList.add("故障描述");
        titleList.add("VIN");
        titleList.add("VSN");
        titleList.add("经销商");
        titleList.add("车型型号");
        titleList.add("生产日期");
        titleList.add("购车日期");
        titleList.add("发动机型号");
        titleList.add("发动机号/电动机号");
        titleList.add("行驶里程");
        titleList.add("车牌号");
        titleList.add("车主");
        titleList.add("服务里程");
        titleList.add("电话");
        titleList.add("详细地址");
        titleList.add("外出地点");
        titleList.add("单向里程");
        titleList.add("交通费用");
        titleList.add("拖车里程");
        titleList.add("拖车费用");
        titleList.add("外出人数");
        titleList.add("外出天数");
        titleList.add("人员补贴");
        titleList.add("住宿补贴");
        titleList.add("外出费用");
        titleList.add("当前状态");
        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("doc_no");
        keyList.add("dealer_code");
        keyList.add("dealer_name");
        keyList.add("submitter_name");
        keyList.add("submitter_phone");
        keyList.add("province_name");
        keyList.add("service_manager");
        keyList.add("created_time");
        keyList.add("pull_in_date");
        keyList.add("pull_out_date");
        keyList.add("dealer_star");
        keyList.add("type");
        keyList.add("quality_report_doc_no");
        keyList.add("expense_report_doc_no");
        keyList.add("activityDistributionDocNo");
        keyList.add("night_expense");
        keyList.add("firstExpense");
        keyList.add("hour_price");
        keyList.add("maintainWorkTimeExpense");
        //keyList.add("hour_expense");
        keyList.add("out_work_time_expense");
        keyList.add("amount_cost");
        keyList.add("other_expense");
        keyList.add("accessories_expense");
        keyList.add("part_expense");
        keyList.add("expense_Total");
        keyList.add("settlement_accesories_expense");
        keyList.add("settlement_part_expense");
        keyList.add("settlement_totle_expense");
        keyList.add("sender");
        keyList.add("sender_phone");
        keyList.add("start_date");
        keyList.add("end_date");
        keyList.add("repairer");
        keyList.add("repair_type");
        keyList.add("fault");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("seller");
        keyList.add("vehicle_model");
        keyList.add("manufacture_date");
        keyList.add("purchase_date");
        //keyList.add("product_date");
        keyList.add("engine_Model");
        keyList.add("engine_no");
        keyList.add("mileage");
        keyList.add("plate");
        keyList.add("owner_name");
        keyList.add("vmt");
        keyList.add("mobile");
        keyList.add("address");
        keyList.add("place");
        keyList.add("agoMileage");
        keyList.add("tran_costs");
        keyList.add("trailer_mileage");
        keyList.add("trailer_cost");
        keyList.add("out_go_num");
        keyList.add("out_go_day");
        keyList.add("personnel_subsidy");
        keyList.add("night_subsidy");
        keyList.add("amount_cost");
        keyList.add("status");

        ExcelUtil.ListMapToExcel(titleList, keyList, maps);


    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }


    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }

}
