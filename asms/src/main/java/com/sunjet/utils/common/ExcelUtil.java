package com.sunjet.utils.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lhj on 16/6/19.
 */
public class ExcelUtil {
    public static String putListToExcel() throws Exception {
        SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
        Sheet sh = wb.createSheet();
        for (int rownum = 0; rownum < 50000; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 20; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }

        }

        // Rows with rownum < 900 are flushed and not accessible
//    for(int rownum = 0; rownum < 900; rownum++){
//      Assert.assertNull(sh.getRow(rownum));
//    }
//
//    // ther last 100 rows are still in memory
//    for(int rownum = 900; rownum < 1000; rownum++){
//      Assert.assertNotNull(sh.getRow(rownum));
//    }
//    System.out.println(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/"));
        String fileName = UUIDHelper.newUuid() + ".xlsx";
        FileOutputStream out = new FileOutputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/files/" + fileName);
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
        return "/files/" + fileName;
    }

    /**
     * 通过List<Map>导出excel
     *
     * @param titleList 标题列表
     * @param keyList   key 列表
     * @param maps      map
     */
    public static void ListMapToExcel(List<String> titleList, List<String> keyList, List<Map<String, Object>> maps) {

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
        // 设置表头
        int titleLength = 1;
        for (String title : titleList) {
            if (titleLength > titleList.size()) {
                continue;
            }
            cell.setCellValue(title);
            cell.setCellStyle(style);
            cell = row.createCell(titleLength);
            titleLength++;
        }


        int index = 1;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(index);
            int size = 0;
            for (String key : keyList) {
                if (size > map.size()) {
                    continue;
                }
                datarow.createCell(size).setCellValue(map.get(key) == null ? "" : map.get(key).toString());
                size++;
            }
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


}
