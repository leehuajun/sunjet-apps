package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhj on 16/6/30.
 * 车辆信息实体
 */
@Entity
@Table(name = "BasicVehicles")
public class VehicleEntity extends DocEntity {
    private static final long serialVersionUID = -7508708530323132518L;
    private String vin;                     // 车辆VIN码
    private String vsn;                     // 车辆VSN码
    private String vehicleModel;  // 车型型号
    private String engineModel;   // 发动机型号
    private String engineNo;                // 发动机号
    private String brand;           // 品牌
    private Double displacement;    // 排量
    private Date manufactureDate;               // 生产日期
    private Date productDate;                   // 出厂日期
    private Date purchaseDate;                  // 购买日期
    private Date fmDate;                // 首保日期
    private String plate;               // 车牌号
    private String licensePlate;        // 车辆牌照
    private String seller;              // 销售商
    private String mileage;             // 行驶里程
    private String vmt;                 // 里程
    private String ownerName;           // 车主姓名
    private String sex;                 // 性别
    private ProvinceEntity province;    // 省份
    private CityEntity city;            // 城市
    private CountyEntity county;        // 县/区
    private String address;             // 详细地址
    private String phone;               // 固定电话
    private String mobile;              // 手机
    private String email;               // 电子邮件
    private String postcode;            // 邮政编码
    private String dealerName;          // 服务站名称
    private String dealerAddress;       // 服务站地址
    private String serviceManager;      // 服务经理名称


    @Column(length = 50)
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Column(length = 50)
    public String getVsn() {
        return vsn;
    }

    public void setVsn(String vsn) {
        this.vsn = vsn;
    }

    @Column(length = 50)
    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    @Column(length = 50)
    public String getEngineModel() {
        return engineModel;
    }

    public void setEngineModel(String engineModel) {
        this.engineModel = engineModel;
    }

    @Column(length = 50)
    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    @Column(length = 50)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }


    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    public Date getFmDate() {
        return fmDate;
    }

    public void setFmDate(Date fmDate) {
        this.fmDate = fmDate;
    }

    @Column(length = 50)
    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Column(length = 50)
    public Double getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Double displacement) {
        this.displacement = displacement;
    }

    @Column(length = 100)
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Column(length = 5)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(length = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(length = 50)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 10)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(length = 20)
    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    @Column(length = 20)
    public String getVmt() {
        return vmt;
    }

    public void setVmt(String vmt) {
        this.vmt = vmt;
    }

    @Column(length = 50)
    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Column(length = 200)
    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    @Column(length = 50)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }


    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ProvinceId")
    public ProvinceEntity getProvince() {
        return province;
    }

    public void setProvince(ProvinceEntity province) {
        this.province = province;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "CityId")
    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "CountyId")
    public CountyEntity getCounty() {
        return county;
    }

    public void setCounty(CountyEntity county) {
        this.county = county;
    }

    public VehicleEntity() {
    }

    public VehicleEntity(String vin, String vsn) {
        this.vin = vin;
        this.vsn = vsn;

    }
}
