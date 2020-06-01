package com.sunjet.model.dealer;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.ProvinceEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 服务站资质变更申请单实体
 */
@Entity
@Table(name = "DealerAlterRequests")
public class DealerAlterRequestEntity extends FlowDocEntity {
    private static final long serialVersionUID = 2631321993226332675L;
    private DealerEntity dealer;      // 服务站信息

    private String fileAlteration; //  变更函

    private String otherCollaboration;//其他合作内容
    private String productsOfMaintain; //拟维修我公司产
    private String otherMaintainCondition; //其他车辆维修条件
    private String otherBrand;  //兼做的品牌服务

    //    =========================
    private String phone;           // 联系电话
    private String fax;             // 传真
    private String address;         // 地址
    //    private String provinceId;        // 省份id
//    private String provinceName;      // 省份名称
    private ProvinceEntity province;      // 省份
    //    private String cityId;            // 市id
//    private String cityName;          // 市名称
    private CityEntity city;                // 市
    private Boolean sgmwSystem;         // 是否SGMW体系
    private String star;                // 申请等级，星级
    private String qualification;       // 维修资质
    private String level;               // 服务站等级
    private DealerEntity parent;        // 父级服务站

    private String legalPerson;             // 法人
    private String legalPersonPhone;        // 法人联系方式
    private String stationMaster;           // 站长
    private String stationMasterPhone;      // 站长联系方式
    private String technicalDirector;       // 技术主管
    private String technicalDirectorPhone;  // 技术主管联系方式
    private String claimDirector;           // 索赔主管
    private String claimDirectorPhone;      // 索赔主管联系方式
    private String partDirector;            // 配件主管
    private String partDirectorPhone;       // 配件主管联系方式
    private String financeDirector;         // 财务主管
    private String financeDirectorPhone;    // 财务主管联系方式

    private String employeeCount;           // 员工总人数
    private String receptionistCount;       // 接待员数量
    private String partKeeyperCount;        // 配件员数量
    private String maintainerCount;         // 维修工数量
    private String qcInspectorCount;        // 质检员数量
    private String clerkCount;              // 结算员数量

    private String parkingArea;             // 停车区面积
    private String receptionArea;           // 接待区面积
    private String generalArea;             // 综合维修区面积
    private String assemblyArea;            // 总成维修区面积
    private String storageArea;             // 配件库总面积
    private String storageWulingArea;               // 五菱配件库面积
    private String storageUserdPartArea;            // 旧件库面积
    private String storageWulingUserdPartArea;      // 五菱旧件库面积

    private String fileBusinessLicense;             // 营业执照 照片文件
    private String fileTaxCertificate;              // 税务登记证 照片文件
    private String fileBankAccountOpeningPermit;    // 银行开户行许可证
    private String filePersonnelCertificate;        // 人员登记证书
    private String fileQualification;               // 维修资质表
    private String fileInvoiceInfo;                 // 服务站开票信息
    private String fileRoadTransportLicense;        // 道路运输营业许可证
    private String fileOrgChart;                    // 企业组织架构及设置书
    private String fileDevice;                      // 设备清单
    private String fileReceptionOffice;             // 接待室图片
    private String fileGlobal;                      // 服务商全貌图片
    private String fileOffice;                      // 办公室图片
    private String fileWorkshop;                    // 维修车间
    private String filePartStoreage;                // 配件库房图片
    private String fileMap;                         // 地理位置

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "Dealer")
    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    @Column(length = 200)
    public String getFileAlteration() {
        return fileAlteration;
    }

    public void setFileAlteration(String fileAlteration) {
        this.fileAlteration = fileAlteration;
    }

    //    ==========================================
    @Column(length = 20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(length = 20)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "Province")
    public ProvinceEntity getProvince() {
        return province;
    }

    public void setProvince(ProvinceEntity province) {
        this.province = province;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "City")
    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public Boolean getSgmwSystem() {
        return sgmwSystem;
    }

    public void setSgmwSystem(Boolean sgmwSystem) {
        this.sgmwSystem = sgmwSystem;
    }

    @Column(length = 10)
    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    @Column(length = 50)
    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Column(length = 10)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "Parent")
    public DealerEntity getParent() {
        return parent;
    }

    public void setParent(DealerEntity parent) {
        this.parent = parent;
    }

    @Column(length = 50)
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    @Column(length = 20)
    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }

    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }

    @Column(length = 50)
    public String getStationMaster() {
        return stationMaster;
    }

    public void setStationMaster(String stationMaster) {
        this.stationMaster = stationMaster;
    }

    @Column(length = 20)
    public String getStationMasterPhone() {
        return stationMasterPhone;
    }

    public void setStationMasterPhone(String stationMasterPhone) {
        this.stationMasterPhone = stationMasterPhone;
    }

    @Column(length = 50)
    public String getTechnicalDirector() {
        return technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    @Column(length = 20)
    public String getTechnicalDirectorPhone() {
        return technicalDirectorPhone;
    }

    public void setTechnicalDirectorPhone(String technicalDirectorPhone) {
        this.technicalDirectorPhone = technicalDirectorPhone;
    }

    @Column(length = 50)
    public String getClaimDirector() {
        return claimDirector;
    }

    public void setClaimDirector(String claimDirector) {
        this.claimDirector = claimDirector;
    }

    @Column(length = 20)
    public String getClaimDirectorPhone() {
        return claimDirectorPhone;
    }

    public void setClaimDirectorPhone(String claimDirectorPhone) {
        this.claimDirectorPhone = claimDirectorPhone;
    }

    @Column(length = 50)
    public String getPartDirector() {
        return partDirector;
    }

    public void setPartDirector(String partDirector) {
        this.partDirector = partDirector;
    }

    @Column(length = 20)
    public String getPartDirectorPhone() {
        return partDirectorPhone;
    }

    public void setPartDirectorPhone(String partDirectorPhone) {
        this.partDirectorPhone = partDirectorPhone;
    }

    @Column(length = 50)
    public String getFinanceDirector() {
        return financeDirector;
    }

    public void setFinanceDirector(String financeDirector) {
        this.financeDirector = financeDirector;
    }

    @Column(length = 20)
    public String getFinanceDirectorPhone() {
        return financeDirectorPhone;
    }

    public void setFinanceDirectorPhone(String financeDirectorPhone) {
        this.financeDirectorPhone = financeDirectorPhone;
    }

    @Column(length = 10)
    public String getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    @Column(length = 10)
    public String getReceptionistCount() {
        return receptionistCount;
    }

    public void setReceptionistCount(String receptionistCount) {
        this.receptionistCount = receptionistCount;
    }

    @Column(length = 10)
    public String getPartKeeyperCount() {
        return partKeeyperCount;
    }

    public void setPartKeeyperCount(String partKeeyperCount) {
        this.partKeeyperCount = partKeeyperCount;
    }

    @Column(length = 10)
    public String getMaintainerCount() {
        return maintainerCount;
    }

    public void setMaintainerCount(String maintainerCount) {
        this.maintainerCount = maintainerCount;
    }

    @Column(length = 10)
    public String getQcInspectorCount() {
        return qcInspectorCount;
    }

    public void setQcInspectorCount(String qcInspectorCount) {
        this.qcInspectorCount = qcInspectorCount;
    }

    @Column(length = 10)
    public String getClerkCount() {
        return clerkCount;
    }

    public void setClerkCount(String clerkCount) {
        this.clerkCount = clerkCount;
    }

    @Column(length = 20)
    public String getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(String parkingArea) {
        this.parkingArea = parkingArea;
    }

    @Column(length = 20)
    public String getReceptionArea() {
        return receptionArea;
    }

    public void setReceptionArea(String receptionArea) {
        this.receptionArea = receptionArea;
    }

    @Column(length = 20)
    public String getGeneralArea() {
        return generalArea;
    }

    public void setGeneralArea(String generalArea) {
        this.generalArea = generalArea;
    }

    @Column(length = 20)
    public String getAssemblyArea() {
        return assemblyArea;
    }

    public void setAssemblyArea(String assemblyArea) {
        this.assemblyArea = assemblyArea;
    }

    @Column(length = 20)
    public String getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(String storageArea) {
        this.storageArea = storageArea;
    }

    @Column(length = 20)
    public String getStorageWulingArea() {
        return storageWulingArea;
    }

    public void setStorageWulingArea(String storageWulingArea) {
        this.storageWulingArea = storageWulingArea;
    }

    @Column(length = 20)
    public String getStorageUserdPartArea() {
        return storageUserdPartArea;
    }

    public void setStorageUserdPartArea(String storageUserdPartArea) {
        this.storageUserdPartArea = storageUserdPartArea;
    }

    @Column(length = 20)
    public String getStorageWulingUserdPartArea() {
        return storageWulingUserdPartArea;
    }

    public void setStorageWulingUserdPartArea(String storageWulingUserdPartArea) {
        this.storageWulingUserdPartArea = storageWulingUserdPartArea;
    }

    @Column(length = 200)
    public String getFileBusinessLicense() {
        return fileBusinessLicense;
    }

    public void setFileBusinessLicense(String fileBusinessLicense) {
        this.fileBusinessLicense = fileBusinessLicense;
    }

    @Column(length = 200)
    public String getFileTaxCertificate() {
        return fileTaxCertificate;
    }

    public void setFileTaxCertificate(String fileTaxCertificate) {
        this.fileTaxCertificate = fileTaxCertificate;
    }

    @Column(length = 200)
    public String getFileBankAccountOpeningPermit() {
        return fileBankAccountOpeningPermit;
    }

    public void setFileBankAccountOpeningPermit(String fileBankAccountOpeningPermit) {
        this.fileBankAccountOpeningPermit = fileBankAccountOpeningPermit;
    }

    @Column(length = 200)
    public String getFilePersonnelCertificate() {
        return filePersonnelCertificate;
    }

    public void setFilePersonnelCertificate(String filePersonnelCertificate) {
        this.filePersonnelCertificate = filePersonnelCertificate;
    }

    @Column(length = 200)
    public String getFileQualification() {
        return fileQualification;
    }

    public void setFileQualification(String fileQualification) {
        this.fileQualification = fileQualification;
    }

    @Column(length = 200)
    public String getFileInvoiceInfo() {
        return fileInvoiceInfo;
    }

    public void setFileInvoiceInfo(String fileInvoiceInfo) {
        this.fileInvoiceInfo = fileInvoiceInfo;
    }

    @Column(length = 200)
    public String getFileRoadTransportLicense() {
        return fileRoadTransportLicense;
    }

    public void setFileRoadTransportLicense(String fileRoadTransportLicense) {
        this.fileRoadTransportLicense = fileRoadTransportLicense;
    }

    @Column(length = 200)
    public String getFileOrgChart() {
        return fileOrgChart;
    }

    public void setFileOrgChart(String fileOrgChart) {
        this.fileOrgChart = fileOrgChart;
    }

    @Column(length = 200)
    public String getFileDevice() {
        return fileDevice;
    }

    public void setFileDevice(String fileDevice) {
        this.fileDevice = fileDevice;
    }

    @Column(length = 200)
    public String getFileReceptionOffice() {
        return fileReceptionOffice;
    }

    public void setFileReceptionOffice(String fileReceptionOffice) {
        this.fileReceptionOffice = fileReceptionOffice;
    }

    @Column(length = 200)
    public String getFileGlobal() {
        return fileGlobal;
    }

    public void setFileGlobal(String fileGlobal) {
        this.fileGlobal = fileGlobal;
    }

    @Column(length = 200)
    public String getFileOffice() {
        return fileOffice;
    }

    public void setFileOffice(String fileOffice) {
        this.fileOffice = fileOffice;
    }

    @Column(length = 200)
    public String getFileWorkshop() {
        return fileWorkshop;
    }

    public void setFileWorkshop(String fileWorkshop) {
        this.fileWorkshop = fileWorkshop;
    }

    @Column(length = 200)
    public String getFilePartStoreage() {
        return filePartStoreage;
    }

    public void setFilePartStoreage(String filePartStoreage) {
        this.filePartStoreage = filePartStoreage;
    }

    @Column(length = 200)
    public String getFileMap() {
        return fileMap;
    }

    public void setFileMap(String fileMap) {
        this.fileMap = fileMap;
    }

    @Column(length = 200)
    public String getOtherCollaboration() {
        return otherCollaboration;
    }

    public void setOtherCollaboration(String otherCollaboration) {
        this.otherCollaboration = otherCollaboration;
    }

    @Column(length = 200)
    public String getProductsOfMaintain() {
        return productsOfMaintain;
    }

    public void setProductsOfMaintain(String productsOfMaintain) {
        this.productsOfMaintain = productsOfMaintain;
    }

    @Column(length = 200)
    public String getOtherMaintainCondition() {
        return otherMaintainCondition;
    }

    public void setOtherMaintainCondition(String otherMaintainCondition) {
        this.otherMaintainCondition = otherMaintainCondition;
    }

    @Column(length = 200)
    public String getOtherBrand() {
        return otherBrand;
    }

    public void setOtherBrand(String otherBrand) {
        this.otherBrand = otherBrand;
    }
}

