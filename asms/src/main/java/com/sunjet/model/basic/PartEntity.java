package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 配件信息实体
 */
@Entity
@Table(name = "BasicParts")
public class PartEntity extends DocEntity {
    private static final long serialVersionUID = -6704173665780561623L;
    private String code;//物料编号     50
    private String name;//物料名称          100
    private Double price = 0.0;//价格
    private String unit;//计量单位          20
//    private String useageId;//零件功能分类
//    private String useageName;//零件功能分类名称
//    private String storageId;//库位
//    private String storageName;//库位名称

    private String partType = "配件";    // 物料类型  配件/辅料，默认是：配件， 20
    private String warrantyTime;        // 三包时间 50
    private String warrantyMileage;     // 三包里程 50
    //    private String productTypeId;//物料类型
//    private String productTypeName;//物料类型名称
//    private String providerId;//供应商
//    private String providerName;//供应商名称
//    private String productCategories;//物料属性
//    private Integer lowOrderLimit = 0;//最小销售量
//    private Integer highOrderLimit = 99999999;//最大订购量
    private String partClassify;      //配件分类
    private String comment;    // 备注

    @Column(length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(length = 20)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(length = 20)
    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    @Column(length = 50)
    public String getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(String warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    @Column(length = 50)
    public String getWarrantyMileage() {
        return warrantyMileage;
    }

    public void setWarrantyMileage(String warrantyMileage) {
        this.warrantyMileage = warrantyMileage;
    }

    @Column(length = 500)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(length = 50)
    public String getPartClassify() {
        return partClassify;
    }

    public void setPartClassify(String partClassify) {
        this.partClassify = partClassify;
    }


    public PartEntity() {

    }

    public PartEntity(String productNo) {
        this.code = productNo;
    }


}
