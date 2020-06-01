package com.sunjet.vm.report;

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

//import net.sf.json.JSONArray;

/**
 * Created by lhj on 16/4/27.
 */
public class agencySettlementSummaryVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String agencyName = "";
    private String type;


    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        this.setEnableExport(true);
        this.setType("配件费用");
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM agency_part_expense_summary " +
                        "WHERE agency_name LIKE  '%" + this.getAgencyName() + "%' ");

    }


    @Command
    @NotifyChange("maps")
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");

        } else {
            maps.clear();
            if ("配件费用".equals(this.getType())) {
                maps = jdbcTemplate.queryForList(
                        "SELECT * FROM agency_part_expense_summary " +
                                "WHERE agency_name LIKE  '%" + this.getAgencyName() + "%' ");
            } else {
                maps = jdbcTemplate.queryForList(
                        "SELECT * FROM agency_freight_summary " +
                                "WHERE agency_name LIKE  '%" + this.getAgencyName() + "%' ");

            }


        }
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
        cell.setCellValue("合作商");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("1月");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("2月");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("3月");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("4月");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("5月");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("6月");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("7月");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("8月");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("9月");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("10月");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("11月");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("12月");
        cell.setCellStyle(style);

        int index = 1;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(index);
            datarow.createCell(0).setCellValue(map.get("agency_name") == null ? "" : map.get("agency_name").toString());
            datarow.createCell(1).setCellValue(map.get("January") == null ? "" : map.get("January").toString());
            datarow.createCell(2).setCellValue(map.get("February") == null ? "" : map.get("February").toString());
            datarow.createCell(3).setCellValue(map.get("March") == null ? "" : map.get("March").toString());
            datarow.createCell(4).setCellValue(map.get("April") == null ? "" : map.get("April").toString());
            datarow.createCell(5).setCellValue(map.get("May") == null ? "" : map.get("May").toString());
            datarow.createCell(6).setCellValue(map.get("June") == null ? "" : map.get("June").toString());
            datarow.createCell(7).setCellValue(map.get("July") == null ? "" : map.get("July").toString());
            datarow.createCell(8).setCellValue(map.get("August") == null ? "" : map.get("August").toString());
            datarow.createCell(9).setCellValue(map.get("September") == null ? "" : map.get("September").toString());
            datarow.createCell(10).setCellValue(map.get("October") == null ? "" : map.get("October").toString());
            datarow.createCell(11).setCellValue(map.get("November") == null ? "" : map.get("November").toString());
            datarow.createCell(12).setCellValue(map.get("December") == null ? "" : map.get("December").toString());
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
        this.type = "配件费用";
        this.agencyName = "";

    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }


    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
