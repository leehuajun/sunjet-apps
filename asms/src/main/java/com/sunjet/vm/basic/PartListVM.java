package com.sunjet.vm.basic;


import com.sunjet.model.basic.PartEntity;
import com.sunjet.service.basic.PartService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.ListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class PartListVM extends ListBaseVM {
    @WireVariable
    private PartService partService;

    private String partCode;
    private String partName;

    @Init
    public void init() {
        this.setEnableAdd(true);
        this.setBaseService(partService);
        this.setKeyword("");
        this.setFormUrl("/views/basic/part_form.zul");
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }


    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     */
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("code", SearchOrder.OrderType.ASC, 1)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<PartEntity> specification = new Specification<PartEntity>() {
            @Override
            public Predicate toPredicate(Root<PartEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //方法2：
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(partCode)) {
                    if (partCode.length() == 2) {
                        Predicate p = CustomRestrictions.eq("code", partCode, true).toPredicate(root, query, cb);
                        list.add(cb.and(p));
                    } else {
                        Predicate p = CustomRestrictions.like("code", partCode, true).toPredicate(root, query, cb);
                        list.add(cb.and(p));
                    }

                }
                if (StringUtils.isNotBlank(partName)) {
                    if (partName.length() == 2) {
                        Predicate p = CustomRestrictions.eq("name", partName, true).toPredicate(root, query, cb);
                        list.add(cb.and(p));
                    } else {
                        Predicate p = CustomRestrictions.like("name", partName, true).toPredicate(root, query, cb);
                        list.add(cb.and(p));
                    }

                }
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        };
        searchDTO.setSpecification(specification);

    }

    @Command
    @NotifyChange({"partCode", "partName"})
    public void reset() {
        this.partCode = "";
        this.partName = "";
    }

    //更新配件列表
    @GlobalCommand(GlobalCommandValues.REFRESH_PART_LIST)
    @NotifyChange("*")
    public void refreshPartList() {
        initList();
    }

    @Command
    public void exportToExcel() {
//
//        Clients.showBusy("正在生成数据,请稍候...");
//        // 第一步，创建一个webbook，对应一个Excel文件
//        HSSFWorkbook wb = new HSSFWorkbook();
//        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
//        HSSFSheet sheet = wb.createSheet("物料信息");
//        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
//        HSSFRow row = sheet.createRow(0);
//        // 第四步，创建单元格，并设置值表头 设置表头居中
//        HSSFCellStyle style = wb.createCellStyle();
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue("配件号");
//        cell.setCellStyle(style);
//        cell = row.createCell(1);
//        cell.setCellValue("配件名称");
//        cell.setCellStyle(style);
//        cell = row.createCell(2);
//        cell.setCellValue("计量单位");
//        cell.setCellStyle(style);
//        cell = row.createCell(3);
//        cell.setCellValue("最小销售量");
//        cell.setCellStyle(style);
//        cell = row.createCell(4);
//        cell.setCellValue("最大订购量");
//        cell.setCellStyle(style);
//        cell = row.createCell(5);
//        cell.setCellValue("价格");
//        cell.setCellStyle(style);
//        cell = row.createCell(6);
//        cell.setCellValue("配件属性");
//        cell.setCellStyle(style);
//        cell = row.createCell(7);
//        cell.setCellValue("物料类型");
//        cell.setCellStyle(style);
//        cell = row.createCell(8);
//        cell.setCellValue("供应商");
//        cell.setCellStyle(style);
//        cell = row.createCell(9);
//        cell.setCellValue("库点");
//        cell.setCellStyle(style);
//        cell = row.createCell(10);
//        cell.setCellValue("零件功能分类");
//        cell.setCellStyle(style);
//        cell = row.createCell(11);
//        cell.setCellValue("是否可用");
//        cell.setCellStyle(style);
//        int index = 1;
//        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
//        for (DocEntity model : this.getResultDTO().getPageContent()) {
//            PartEntity partEntity = (PartEntity) model;
//            HSSFRow datarow = sheet.createRow(index);
//            datarow.createCell(0).setCellValue(partEntity.getCode() == null ? "" : partEntity.getCode());
//            datarow.createCell(1).setCellValue(partEntity.getName() == null ? "" : partEntity.getName());
//            datarow.createCell(2).setCellValue(partEntity.getUnit() == null ? "" : partEntity.getUnit());
//            datarow.createCell(3).setCellValue(partEntity.getLowOrderLimit() == null ? 0 : partEntity.getLowOrderLimit());
//            datarow.createCell(4).setCellValue(partEntity.getHighOrderLimit() == null ? 0 : partEntity.getHighOrderLimit());
//            datarow.createCell(5).setCellValue(partEntity.getPrice() == null ? 0 : partEntity.getPrice());
//            datarow.createCell(6).setCellValue(partEntity.getProductCategories() == null ? "" : partEntity.getProductCategories());
//            datarow.createCell(7).setCellValue(partEntity.getProductTypeName() == null ? "" : partEntity.getProductTypeName());
//            datarow.createCell(8).setCellValue(partEntity.getProviderName() == null ? "" : partEntity.getProviderName());
//            datarow.createCell(9).setCellValue(partEntity.getStorageName() == null ? "" : partEntity.getStorageName());
//            datarow.createCell(10).setCellValue(partEntity.getUseageName() == null ? "" : partEntity.getUseageName());
//            datarow.createCell(11).setCellValue(partEntity.getEnabled() == null ? false : partEntity.getEnabled());
//            index++;
//        }
//
//        String fileName = null;
//        try {
//            fileName = UUIDHelper.newUuid() + ".xls";
//            FileOutputStream out = new FileOutputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/files/" + fileName);
//            wb.write(out);
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Clients.clearBusy();
//        try {
//            Filedownload.save("/files/" + fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
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
}

