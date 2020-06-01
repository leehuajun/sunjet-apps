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
 * 费用速报明细列表VM
 */
public class QualityReportDetailListVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String reportType = "";
    private String serviceManager = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableExport(true);
        this.setEnableAdd(false);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT * FROM quality_report_detail " +
                        "WHERE report_type LIKE  '%" + this.getReportType() + "%' " +
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
                    "SELECT * FROM quality_report_detail " +
                            "WHERE report_type LIKE  '%" + this.getReportType() + "%' " +
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
        this.reportType = "";
        this.serviceManager = "";
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
        cell.setCellValue("单据编号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("速报级别");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("服务经理");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("联系人");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("联系人电话");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("服务站编号");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("服务站名称");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("故障描述");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("VIN");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("车型");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("发动机号");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("车主姓名");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("电话");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("购车日期");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("行驶里程");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("提交时间");
        cell.setCellStyle(style);


        int index = 1;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(index);
            datarow.createCell(0).setCellValue(map.get("doc_no") == null ? "" : map.get("doc_no").toString());
            datarow.createCell(1).setCellValue(map.get("report_type") == null ? "" : map.get("report_type").toString());
            datarow.createCell(2).setCellValue(map.get("service_manager") == null ? "" : map.get("service_manager").toString());
            datarow.createCell(3).setCellValue(map.get("submitter_name") == null ? "" : map.get("submitter_name").toString());
            datarow.createCell(4).setCellValue(map.get("submitter_phone") == null ? "" : map.get("submitter_phone").toString());
            datarow.createCell(5).setCellValue(map.get("dealer_code") == null ? "" : map.get("dealer_code").toString());
            datarow.createCell(6).setCellValue(map.get("dealer_name") == null ? "" : map.get("dealer_name").toString());
            datarow.createCell(7).setCellValue(map.get("fault_desc") == null ? "" : map.get("fault_desc").toString());
            datarow.createCell(8).setCellValue(map.get("vin") == null ? "" : map.get("vin").toString());
            datarow.createCell(9).setCellValue(map.get("vehicle_model") == null ? "" : map.get("vehicle_model").toString());
            datarow.createCell(10).setCellValue(map.get("engine_no") == null ? "" : map.get("engine_no").toString());
            datarow.createCell(11).setCellValue(map.get("owner_name") == null ? "" : map.get("owner_name").toString());
            datarow.createCell(12).setCellValue(map.get("mobile") == null ? "" : map.get("mobile").toString());
            datarow.createCell(13).setCellValue(map.get("purchase_date") == null ? "" : map.get("purchase_date").toString());
            datarow.createCell(14).setCellValue(map.get("mileage") == null ? "" : map.get("mileage").toString());
            datarow.createCell(15).setCellValue(map.get("created_time") == null ? "" : map.get("created_time").toString());
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


    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }
}


