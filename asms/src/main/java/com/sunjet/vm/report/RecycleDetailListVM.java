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
 * 故障件明细VM
 */
public class RecycleDetailListVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String dealerName = "";
    private String partCode = "";
    private String partName = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableExport(true);
        this.setEnableAdd(false);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM (SELECT arni.obj_id, arn.`status`, ardl.`status` AS ard_status, arn.dealer_code, arn.dealer_name, arn.doc_no, arn.src_doc_no, ardl.doc_no AS ard_doc_no, ardl.service_manager, arni.part_code, arni.part_name, acp.pattern, acp.reason, arni.amount, arni.back_amount AS back_amount, ( arni.amount - arni.back_amount ) AS wait_amount, acp.part_supply_type, arni.warranty_time, arni.warranty_mileage, arn.return_date, ardl.arrive_date, ardl.logistics_num, ardl.logistics, arn.created_time, ardl.created_time AS ard_create_time FROM asm_recycle_notice_items AS arni LEFT JOIN asm_recycle_notices arn ON arn.obj_id = arni.recycle_notice_id LEFT JOIN asm_commission_parts acp ON acp.warranty_maintenance = arn.src_docid LEFT JOIN ( SELECT arl.obj_id, ard.`status`, ard.doc_no, ard.service_manager, ard.arrive_date, ard.logistics, arl.logistics_num, ard.created_time, arl.notice_item_id FROM asm_recycle_list AS arl LEFT JOIN asm_recycle_docs AS ard ON ard.obj_id = arl.recycle WHERE ard.`status` <> -3 )AS ardl ON ardl.notice_item_id = arni.obj_id GROUP BY arni.obj_id ) AS recycleDetail " +
                        "WHERE dealer_name LIKE  '%" + this.getDealerName() + "%' " +
                        "AND part_code LIKE '%" + this.getPartCode() + "%' " +
                        "AND part_name LIKE '%" + this.getPartName() + "%' " +
                        "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                        "AND '" + DateHelper.dateToString(DateHelper.getEndDate(this.getEndDate())) + "' " +
                        "ORDER BY created_time DESC ");



    }


    @Command
    @NotifyChange("maps")
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");

        } else {
            maps.clear();
            maps = jdbcTemplate.queryForList(
                    "SELECT * FROM (SELECT arni.obj_id, arn.`status`, ardl.`status` AS ard_status, arn.dealer_code, arn.dealer_name, arn.doc_no, arn.src_doc_no, ardl.doc_no AS ard_doc_no, ardl.service_manager, arni.part_code, arni.part_name, acp.pattern, acp.reason, arni.amount, arni.back_amount AS back_amount, ( arni.amount - arni.back_amount ) AS wait_amount, acp.part_supply_type, arni.warranty_time, arni.warranty_mileage, arn.return_date, ardl.arrive_date, ardl.logistics_num, ardl.logistics, arn.created_time, ardl.created_time AS ard_create_time FROM asm_recycle_notice_items AS arni LEFT JOIN asm_recycle_notices arn ON arn.obj_id = arni.recycle_notice_id LEFT JOIN asm_commission_parts acp ON acp.warranty_maintenance = arn.src_docid LEFT JOIN ( SELECT arl.obj_id, ard.`status`, ard.doc_no, ard.service_manager, ard.arrive_date, ard.logistics, arl.logistics_num, ard.created_time, arl.notice_item_id FROM asm_recycle_list AS arl LEFT JOIN asm_recycle_docs AS ard ON ard.obj_id = arl.recycle WHERE ard.`status` <> -3 )AS ardl ON ardl.notice_item_id = arni.obj_id GROUP BY arni.obj_id ) AS recycleDetail  " +
                            "WHERE dealer_name LIKE  '%" + this.getDealerName() + "%' " +
                            "AND part_code LIKE '%" + this.getPartCode() + "%' " +
                            "AND part_name LIKE '%" + this.getPartName() + "%' " +
                            "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                            "AND '" + DateHelper.dateToString(DateHelper.getEndDate(this.getEndDate())) + "'" +
                            "ORDER BY created_time DESC ");
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
        this.partCode = "";
        this.partName = "";
    }

    @Command
    public void exportExcel() {


        if (maps.size() == 0) {
            ZkUtils.showInformation("没有可以导出的数据", "提示");
            return;
        }
        //添加标题
        List<String> titleList = new ArrayList<>();
        titleList.add("服务站编号");
        titleList.add("服务站名称");
        titleList.add("返回通知单单号");
        titleList.add("通知单提交时间");
        titleList.add("故障件返回单号");
        titleList.add("返回单提交时间");
        titleList.add("服务单号");
        titleList.add("服务经理");
        titleList.add("配件件号");
        titleList.add("配件名称");
        titleList.add("故障模式");
        titleList.add("换件原因");
        titleList.add("需返回数量");
        titleList.add("已返回数量");
        titleList.add("供货方式");
        titleList.add("三包时间");
        titleList.add("三包里程");
        titleList.add("应返时间");
        titleList.add("实返时间");
        titleList.add("物流单号");
        titleList.add("物流公司");
        //添加key
        List<String> keyList = new ArrayList<>();
        keyList.add("dealer_code");
        keyList.add("dealer_name");
        keyList.add("doc_no");
        keyList.add("created_time");
        keyList.add("ard_doc_no");
        keyList.add("ard_create_time");
        keyList.add("src_doc_no");
        keyList.add("service_manager");
        keyList.add("part_code");
        keyList.add("part_name");
        keyList.add("pattern");
        keyList.add("reason");
        keyList.add("amount");
        keyList.add("back_amount");
        keyList.add("part_supply_type");
        keyList.add("warranty_time");
        keyList.add("warranty_mileage");
        keyList.add("return_date");
        keyList.add("arrive_date");
        keyList.add("logistics_num");
        keyList.add("logistics");


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

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }
}


