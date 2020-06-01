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
 * <p>
 * 三包配件列表
 */
public class WarrantyDetailListVM extends FlowListBaseVM {


    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String partCode = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableExport(true);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM activity_warranty_part_accessory_detail " +
                        "WHERE part_code LIKE  '%" + this.getPartCode() + "%' " +
                        "AND amcreated_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
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
                    "SELECT * FROM activity_warranty_part_accessory_detail " +
                            "WHERE part_code LIKE  '%" + this.getPartCode() + "%' " +
                            "AND amcreated_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
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
        this.partCode = "";
    }


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
        titleList.add("是否返回旧件");
        titleList.add("单据类型");
        titleList.add("服务站名称");
        titleList.add("服务站联系人");
        titleList.add("服务站联系电话");
        titleList.add("服务经理");
        titleList.add("质量速报单号");
        titleList.add("费用速报单号");
        titleList.add("省份");
        titleList.add("申请时间");
        titleList.add("进站时间");
        titleList.add("服务站星级");
        titleList.add("调拨单号");
        titleList.add("调拨单申请时间");
        titleList.add("备注");
        titleList.add("供货单号");
        titleList.add("合作商");
        titleList.add("供货数量");
        titleList.add("配件费用");
        titleList.add("应到货时间");
        titleList.add("到货时间");
        titleList.add("发运方式");
        titleList.add("物流单号");
        titleList.add("物流公司");
        titleList.add("提交时间");
        titleList.add("收货地址");
        titleList.add("收货人");
        titleList.add("收货电话");
        titleList.add("三包单状态");
        titleList.add("调拨通知单状态");
        titleList.add("供货通知单状态");

        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("part_code");
        keyList.add("part_name");
        keyList.add("part_type");
        keyList.add("pattern");
        keyList.add("reason");
        keyList.add("acp_amount");
        keyList.add("price");
        keyList.add("part_supply_type");
        keyList.add("warranty_time");
        keyList.add("warranty_mileage");
        keyList.add("src_doc_no");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("seller");
        keyList.add("vehicle_model");
        keyList.add("purchase_date");
        keyList.add("mileage");
        keyList.add("engine_no");
        keyList.add("plate");
        //keyList.add("hour_expense");
        keyList.add("owner_name");
        keyList.add("mobile");
        keyList.add("amcomment");
        keyList.add("part_classify");
        keyList.add("recycle");
        keyList.add("src_doc_type");
        keyList.add("dealer_name");
        keyList.add("submitter_name");
        keyList.add("submitter_phone");
        keyList.add("service_manager");
        keyList.add("qualityreport");
        keyList.add("expensereports");
        keyList.add("province_name");
        keyList.add("amcreated_time");
        keyList.add("pull_in_date");
        keyList.add("dealer_star");
        keyList.add("asn_doc_no");
        keyList.add("asncreated_time");
        keyList.add("asncomment");
        keyList.add("doc_no");
        keyList.add("agency_name");
        keyList.add("amount");
        keyList.add("money");
        keyList.add("arrival_time");
        keyList.add("rcv_date");
        keyList.add("transportmodel");
        keyList.add("logistics_num");
        keyList.add("logistics");
        keyList.add("asdcreated_time");
        keyList.add("dealer_adderss");
        keyList.add("receive");
        keyList.add("operator_phone");
        keyList.add("awmStatus");
        keyList.add("asupplynoticStatus");
        keyList.add("asdstatus");


        ExcelUtil.ListMapToExcel(titleList, keyList, maps);

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }


    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }
}


