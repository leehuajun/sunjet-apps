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
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import net.sf.json.JSONArray;

/**
 * Created by lhj on 16/4/27.
 */
public class agencySupplySummaryDetalListVM extends FlowListBaseVM {

    @WireVariable
    private JdbcTemplate jdbcTemplate;
    private List<Map<String, Object>> maps;
    private String agencyCode = "";


    @Init(superclass = true)
    public void init() {
        this.setEnableExport(true);
        this.setEnableAdd(false);
//        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        maps = jdbcTemplate.queryForList(
                "SELECT agency_name," +
                        "created_time," +
                        "doc_no," +
                        "part_expense," +
                        "freight," +
                        "expense_total" +
                        " FROM asm_agency_settlement_docs " +
                        "WHERE agency_name LIKE  '%" + this.getAgencyCode() + "%' " +
                        "AND status = '3'" +
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
                    "SELECT agency_name," +
                            "created_time," +
                            "doc_no," +
                            "part_expense," +
                            "freight," +
                            "expense_total" +
                            " FROM asm_agency_settlement_docs " +
                            "WHERE agency_name LIKE  '%" + this.getAgencyCode() + "%' " +
                            "AND status = '3'" +
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
        this.agencyCode = "";

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
        cell.setCellValue("结算单号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("配件费用");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("运费");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("总费用");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("提交时间");
        cell.setCellStyle(style);

        int index = 1;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(index);
            DecimalFormat df = new DecimalFormat("######0.00");
            datarow.createCell(0).setCellValue(map.get("agency_name") == null ? "" : map.get("agency_name").toString());
            datarow.createCell(1).setCellValue(map.get("doc_no") == null ? "" : map.get("doc_no").toString());
            datarow.createCell(2).setCellValue(map.get("part_expense") == null ? "" : df.format(map.get("part_expense")));
            datarow.createCell(3).setCellValue(map.get("freight") == null ? "" : df.format(map.get("freight")));
            Double sum = null;
            if (map.get("part_expense") != null && map.get("freight") != null) {
                sum = (Double) map.get("part_expense") + (Double) map.get("freight");
            }

            datarow.createCell(4).setCellValue(sum == null ? "" : df.format(sum));
            datarow.createCell(5).setCellValue(map.get("created_time") == null ? "" : map.get("created_time").toString());

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


    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }


    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
    }

}
