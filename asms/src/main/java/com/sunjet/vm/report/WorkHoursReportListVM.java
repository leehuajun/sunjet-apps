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
 * @create 工时费用
 */
public class WorkHoursReportListVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String type = "";
    private String name = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableExport(true);
        this.setEnableAdd(false);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM work_hours " +
                        "WHERE name LIKE  '%" + this.getName() + "%' " +
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
            if ("".equals(this.getType())) {
                // 类型无输入查询全部
                maps = jdbcTemplate.queryForList(
                        "SELECT * FROM work_hours " +
                                "WHERE name LIKE  '%" + this.getName() + "%' " +
                                "AND created_time BETWEEN  '" + DateHelper.dateToString(this.getStartDate()) + "'" +
                                "AND '" + DateHelper.dateToString(this.getEndDate()) + "'");
            } else {
                maps = jdbcTemplate.queryForList(
                        "SELECT * FROM work_hours " +
                                "WHERE name LIKE  '%" + this.getName() + "%' " +
                                "AND night_work = '" + this.getType() + "' " +
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
        this.type = "";
        this.name = "";
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
        cell.setCellValue("项目编号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("项目名称");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("工时定额");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("维修措施");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("夜间工时补贴费用");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("省份");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("服务站");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("服务单号");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("VIN");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("车型");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("发动机型号");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("发动机号");
        cell.setCellStyle(style);


        int index = 1;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(index);
            datarow.createCell(0).setCellValue(map.get("code") == null ? "" : map.get("code").toString());
            datarow.createCell(1).setCellValue(map.get("name") == null ? "" : map.get("name").toString());
            datarow.createCell(2).setCellValue(map.get("work_time") == null ? "" : map.get("work_time").toString());
            datarow.createCell(3).setCellValue(map.get("measure") == null ? "" : map.get("measure").toString());
            datarow.createCell(4).setCellValue(map.get("night_expense") == null ? "" : map.get("night_expense").toString());
            datarow.createCell(5).setCellValue(map.get("province_name") == null ? "" : map.get("province_name").toString());
            datarow.createCell(6).setCellValue(map.get("dealer_name") == null ? "" : map.get("dealer_name").toString());
            datarow.createCell(7).setCellValue(map.get("doc_no") == null ? "" : map.get("doc_no").toString());
            datarow.createCell(8).setCellValue(map.get("vin") == null ? "" : map.get("vin").toString());
            datarow.createCell(9).setCellValue(map.get("vehicle_model") == null ? "" : map.get("vehicle_model").toString());
            datarow.createCell(10).setCellValue(map.get("engine_model") == null ? "" : map.get("engine_model").toString());
            datarow.createCell(11).setCellValue(map.get("engine_no") == null ? "" : map.get("engine_no").toString());
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


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }
}


