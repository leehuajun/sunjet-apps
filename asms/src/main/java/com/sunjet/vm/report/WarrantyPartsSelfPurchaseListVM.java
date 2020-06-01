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
 * @author
 * @create 2015-12-30 上午11:38
 * <p>
 * 三包配件自购报表
 */
public class WarrantyPartsSelfPurchaseListVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String dealerName = "";
    private String docNo = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableExport(true);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM warranty_parts_self_purchase " +
                        "WHERE  doc_no LIKE '%" + this.getDocNo() + "%' " +
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
                    "SELECT * FROM warranty_parts_self_purchase " +
                            "WHERE  doc_no LIKE '%" + this.getDocNo() + "%' " +
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

    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }

    //导出excel
    @Command
    public void exportExcel() {
        if (maps.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        }
        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("配件件号");
        titleList.add("配件名称");
        titleList.add("配件类型");
        titleList.add("故障模式");
        titleList.add("换件原因");
        titleList.add("数量");
        titleList.add("单价");
        titleList.add("供货方式");
        titleList.add("三包时间");
        titleList.add("三包里程");
        titleList.add("服务单号");
        titleList.add("VIN");
        titleList.add("VSN");
        titleList.add("经销商");
        titleList.add("车辆型号");
        titleList.add("购买日期");
        titleList.add("行驶里程");
        titleList.add("发动机号");
        titleList.add("车牌号");
        titleList.add("车主姓名");
        titleList.add("电话");
        titleList.add("备注");
        titleList.add("配件分类");
        titleList.add("是否返回件");
        titleList.add("单据类型");
        titleList.add("服务站");
        titleList.add("服务站联系人");
        titleList.add("服务站联系电话");
        titleList.add("服务经理");
        titleList.add("质量速报单号");
        titleList.add("费用速报单号");
        titleList.add("省份");
        titleList.add("申请时间");
        titleList.add("进站时间");
        titleList.add("服务站星级");
        //添加字段
        List<String> keyList = new ArrayList<>();
        keyList.add("part_code");
        keyList.add("part_name");
        keyList.add("part_type");
        keyList.add("pattern");
        keyList.add("reason");
        keyList.add("amount");
        keyList.add("price");
        keyList.add("part_supply_type");
        keyList.add("warranty_time");
        keyList.add("warranty_mileage");
        keyList.add("doc_no");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("seller");
        keyList.add("vehicle_model");
        keyList.add("purchase_date");
        keyList.add("mileage");
        keyList.add("engine_no");
        keyList.add("plate");
        keyList.add("owner_name");
        keyList.add("mobile");
        keyList.add("comment");
        keyList.add("part_classify");
        keyList.add("recycle");
        keyList.add("doc_type");
        keyList.add("dealer_name");
        keyList.add("submitter_name");
        keyList.add("dealer_phone");
        keyList.add("service_manager");
        keyList.add("quality_report_doc_no");
        keyList.add("expense_report_doc_no");
        keyList.add("province_name");
        keyList.add("created_time");
        keyList.add("pull_in_date");
        keyList.add("dealer_star");
        ExcelUtil.ListMapToExcel(titleList, keyList, maps);

    }


    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
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
}


