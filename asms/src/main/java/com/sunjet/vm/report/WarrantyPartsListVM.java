package com.sunjet.vm.report;

import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.UUIDHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 三包配件报表VM
 */
public class WarrantyPartsListVM extends FlowListBaseVM {

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
                "SELECT * FROM warranty_parts " +
                        "WHERE  part_type LIKE '%" + this.getType() + "%' " +
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
                    "SELECT * FROM warranty_parts " +
                            "WHERE  part_type LIKE '%" + this.getType() + "%' " +
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


    @Command
    public void exportExcel() {
        //Clients.showBusy("正在生成数据,请稍候...");
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("Sheet1");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("配件件号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("配件名称");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("配件类型");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("故障模式");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("换件原因");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("数量");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("单价");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("供货方式");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("三包时间");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("三包里程");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("配件费用");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("服务单号");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("调拨单号");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("VIN");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("VSN");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("经销商");
        cell.setCellStyle(style);
        cell = row.createCell(16);
        cell.setCellValue("车辆型号");
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellValue("购买日期");
        cell.setCellStyle(style);
        cell = row.createCell(18);
        cell.setCellValue("行驶里程");
        cell.setCellStyle(style);
        cell = row.createCell(19);
        cell.setCellValue("发动机号");
        cell.setCellStyle(style);
        cell = row.createCell(20);
        cell.setCellValue("车牌号");
        cell.setCellStyle(style);
        cell = row.createCell(21);
        cell.setCellValue("车主姓名");
        cell.setCellStyle(style);
        cell = row.createCell(22);
        cell.setCellValue("电话");
        cell.setCellStyle(style);

        int index = 1;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(index);
            datarow.createCell(0).setCellValue(map.get("part_code") == null ? "" : map.get("part_code").toString());
            datarow.createCell(1).setCellValue(map.get("part_name") == null ? "" : map.get("part_name").toString());
            datarow.createCell(2).setCellValue(map.get("part_type") == null ? "" : map.get("part_type").toString());
            datarow.createCell(3).setCellValue(map.get("pattern") == null ? "" : map.get("pattern").toString());
            datarow.createCell(4).setCellValue(map.get("reason") == null ? "" : map.get("reason").toString());
            datarow.createCell(5).setCellValue(map.get("amount") == null ? "" : map.get("amount").toString());
            datarow.createCell(6).setCellValue(map.get("price") == null ? "" : map.get("price").toString());
            datarow.createCell(7).setCellValue(map.get("part_supply_type") == null ? "" : map.get("part_supply_type").toString());
            datarow.createCell(8).setCellValue(map.get("warranty_time") == null ? "" : map.get("warranty_time").toString());
            datarow.createCell(9).setCellValue(map.get("warranty_mileage") == null ? "" : map.get("warranty_mileage").toString());
            datarow.createCell(10).setCellValue(map.get("part_expense") == null ? "" : map.get("part_expense").toString());
            datarow.createCell(11).setCellValue(map.get("doc_no") == null ? "" : map.get("doc_no").toString());
            datarow.createCell(12).setCellValue(map.get("supplyDocNo") == null ? "" : map.get("supplyDocNo").toString());
            datarow.createCell(13).setCellValue(map.get("vin") == null ? "" : map.get("vin").toString());
            datarow.createCell(14).setCellValue(map.get("vsn") == null ? "" : map.get("vsn").toString());
            datarow.createCell(15).setCellValue(map.get("seller") == null ? "" : map.get("seller").toString());
            datarow.createCell(16).setCellValue(map.get("vehicle_model") == null ? "" : map.get("vehicle_model").toString());
            datarow.createCell(17).setCellValue(map.get("purchase_date") == null ? "" : map.get("purchase_date").toString());
            datarow.createCell(18).setCellValue(map.get("mileage") == null ? "" : map.get("mileage").toString());
            datarow.createCell(19).setCellValue(map.get("engine_no") == null ? "" : map.get("engine_no").toString());
            datarow.createCell(20).setCellValue(map.get("plate") == null ? "" : map.get("plate").toString());
            datarow.createCell(21).setCellValue(map.get("owner_name") == null ? "" : map.get("owner_name").toString());
            datarow.createCell(22).setCellValue(map.get("mobile") == null ? "" : map.get("mobile").toString());
            index++;
        }
        String fileName = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            fileName = UUIDHelper.newUuid() + ".xls";
            wb.write(out);
            Filedownload.save(out.toByteArray(), null, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }


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


