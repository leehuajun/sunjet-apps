package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 配件合作商、经销商实体
 */
@Entity
@Table(name = "BasicAgencies")
public class AgencyEntity extends DocEntity {
    private static final long serialVersionUID = -6704173665780561623L;
    private String code;            // 编号
    private String name;            // 名称
    private String phone;           // 电话
    private String fax;             // 传真
    private String address;         // 地址
    private ProvinceEntity province;      // 省份
    private CityEntity city;                // 市
    private CountyEntity county;            // 区/县
    private String coverProvinces;           //覆盖省份

    private String businessLicenseCode;     // 营业执照号
    private String orgCode;                 // 组织机构代码号
    private String taxpayerCode;            // 纳税人识别号
    private String taxRate;                 //税率
    private String bank;                    // 开户行
    private String bankAccount;             // 银行帐号

    private String legalPerson;             // 法人
    private String legalPersonPhone;        // 法人联系方式
    private String shopManager;             // 店长
    private String shopManagerPhone;        // 店长联系方式
    private String technicalDirector;       // 技术主管
    private String technicalDirectorPhone;  // 技术主管联系方式
    private String planDirector;            // 计划主管
    private String planDirectorPhone;       // 计划主管联系方式
    private String partDirector;            // 配件主管
    private String partDirectorPhone;       // 配件主管联系方式
    private String financeDirector;         // 财务主管
    private String financeDirectorPhone;    // 财务主管联系方式

    private String employeeCount;           // 员工总人数
    private String receptionistCount;       // 接待员数量
    private String logisticsClerkCount;     // 物流员数量
    private String invoiceClerkCount;       // 开票制单员数量
    private String storeKeeperCount;        // 库管员数量
    private String clerkCount;              // 结算员数量
    private String forkliftCount;           // 液压叉车数量

    private String officeArea;              // 办公室面积
    private String storageArea;             // 配件库面积
    private String receptionArea;           // 接待区面积
    private String shelfArea;               // 料架数量
    private String buildingStructure;       // 房屋结构
    private String productsOfSupply;        // 拟供应我公司产品系列
    private String otherBrand;              // 还兼做哪些品牌的配件

    private String filePersonnelCertificate;        // 人员登记证书
    private String fileQualification;               // 维修资质表
    private String fileBusinessLicense;             // 营业执照 照片文件
    private String fileTaxCertificate;              // 税务登记证 照片文件
    private String fileBankAccountOpeningPermit;    // 银行开户行许可证
    private String fileOrgChart;                    // 企业组织架构及设置书
    private String fileInvoiceInfo;                 // 合作库开票信息
    private String fileGlobal;                      // 合作库全貌图片
    private String fileOffice;                      // 办公室图片
    private String fileReceptionOffice;             // 接待室图片
    private String filePartStoreage;                // 配件库房图片
    private String fileMap;                         // 地理位置
    private String fileTrain;                       // 培训资料
    //  private String bank;

    private String shelfCount;                      // 标准货架数量
    private String fileShelf;                       // 标准货架 照片
    private String containerCount;                  // 定制货柜数量
    private String fileContainer;                   // 定制货柜 照片
    private String ladderTruckCount;                // 登高车数量
    private String fileLadderTruck;                 // 登高车 照片
    private String forkTruckCount;                  // 堆高车数量
    private String fileForkTruck;                   // 堆高车 照片
    private String littleContainerCount;            // 小件容器数量
    private String fileLittleContainer;             // 小容器 照片
    private String partNameplateCount;              // 零件铭牌数量
    private String storeLampCount;                  // 库房灯光数量
    private String tagCardCount;                    // 货物标签卡数量
    private String computerCount;                   // 电脑数量
    private String fileComputer;                    // 电脑照片
    private String telephoneCount;                  // 电话数量
    private String fileTelephone;                   // 电话照片
    private String faxCount;                        // 传真数量
    private String fileFax;                         // 传真机照片
    private String cameraCount;                     // 相机数量
    private String fileCamera;                      // 相机 照片
    private String packerCount;                     // 手动打包机数量
    private String filePacker;                      // 打包机 照片

    @Column(length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "County")
    public CountyEntity getCounty() {
        return county;
    }

    public void setCounty(CountyEntity county) {
        this.county = county;
    }

    @Column(length = 50)
    public String getBusinessLicenseCode() {
        return businessLicenseCode;
    }

    public void setBusinessLicenseCode(String businessLicenseCode) {
        this.businessLicenseCode = businessLicenseCode;
    }

    @Column(length = 50)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(length = 50)
    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    @Column(length = 50)
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(length = 50)
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
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
    public String getShopManager() {
        return shopManager;
    }

    public void setShopManager(String shopManager) {
        this.shopManager = shopManager;
    }

    @Column(length = 20)
    public String getShopManagerPhone() {
        return shopManagerPhone;
    }

    public void setShopManagerPhone(String shopManagerPhone) {
        this.shopManagerPhone = shopManagerPhone;
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
    public String getPlanDirector() {
        return planDirector;
    }

    public void setPlanDirector(String planDirector) {
        this.planDirector = planDirector;
    }

    @Column(length = 20)
    public String getPlanDirectorPhone() {
        return planDirectorPhone;
    }

    public void setPlanDirectorPhone(String planDirectorPhone) {
        this.planDirectorPhone = planDirectorPhone;
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
    public String getLogisticsClerkCount() {
        return logisticsClerkCount;
    }

    public void setLogisticsClerkCount(String logisticsClerkCount) {
        this.logisticsClerkCount = logisticsClerkCount;
    }

    @Column(length = 10)
    public String getInvoiceClerkCount() {
        return invoiceClerkCount;
    }

    public void setInvoiceClerkCount(String invoiceClerkCount) {
        this.invoiceClerkCount = invoiceClerkCount;
    }

    @Column(length = 10)
    public String getStoreKeeperCount() {
        return storeKeeperCount;
    }

    public void setStoreKeeperCount(String storeKeeperCount) {
        this.storeKeeperCount = storeKeeperCount;
    }

    @Column(length = 10)
    public String getClerkCount() {
        return clerkCount;
    }

    public void setClerkCount(String clerkCount) {
        this.clerkCount = clerkCount;
    }

    @Column(length = 10)
    public String getForkliftCount() {
        return forkliftCount;
    }

    public void setForkliftCount(String forkliftCount) {
        this.forkliftCount = forkliftCount;
    }

    @Column(length = 20)
    public String getOfficeArea() {
        return officeArea;
    }

    public void setOfficeArea(String officeArea) {
        this.officeArea = officeArea;
    }

    @Column(length = 20)
    public String getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(String storageArea) {
        this.storageArea = storageArea;
    }

    @Column(length = 20)
    public String getReceptionArea() {
        return receptionArea;
    }

    public void setReceptionArea(String receptionArea) {
        this.receptionArea = receptionArea;
    }

    @Column(length = 20)
    public String getShelfArea() {
        return shelfArea;
    }

    public void setShelfArea(String shelfArea) {
        this.shelfArea = shelfArea;
    }

    @Column(length = 50)
    public String getBuildingStructure() {
        return buildingStructure;
    }

    public void setBuildingStructure(String buildingStructure) {
        this.buildingStructure = buildingStructure;
    }

    @Column(length = 200)
    public String getProductsOfSupply() {
        return productsOfSupply;
    }

    public void setProductsOfSupply(String productsOfSupply) {
        this.productsOfSupply = productsOfSupply;
    }

    @Column(length = 200)
    public String getOtherBrand() {
        return otherBrand;
    }

    public void setOtherBrand(String otherBrand) {
        this.otherBrand = otherBrand;
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
    public String getFileOrgChart() {
        return fileOrgChart;
    }

    public void setFileOrgChart(String fileOrgChart) {
        this.fileOrgChart = fileOrgChart;
    }

    @Column(length = 200)
    public String getFileInvoiceInfo() {
        return fileInvoiceInfo;
    }

    public void setFileInvoiceInfo(String fileInvoiceInfo) {
        this.fileInvoiceInfo = fileInvoiceInfo;
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
    public String getFileReceptionOffice() {
        return fileReceptionOffice;
    }

    public void setFileReceptionOffice(String fileReceptionOffice) {
        this.fileReceptionOffice = fileReceptionOffice;
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
    public String getFileTrain() {
        return fileTrain;
    }

    public void setFileTrain(String fileTrain) {
        this.fileTrain = fileTrain;
    }

    @Column(length = 10)
    public String getShelfCount() {
        return shelfCount;
    }

    public void setShelfCount(String shelfCount) {
        this.shelfCount = shelfCount;
    }

    @Column(length = 200)
    public String getFileShelf() {
        return fileShelf;
    }

    public void setFileShelf(String fileShelf) {
        this.fileShelf = fileShelf;
    }

    @Column(length = 10)
    public String getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(String containerCount) {
        this.containerCount = containerCount;
    }

    @Column(length = 200)
    public String getFileContainer() {
        return fileContainer;
    }

    public void setFileContainer(String fileContainer) {
        this.fileContainer = fileContainer;
    }

    @Column(length = 10)
    public String getLadderTruckCount() {
        return ladderTruckCount;
    }

    public void setLadderTruckCount(String ladderTruckCount) {
        this.ladderTruckCount = ladderTruckCount;
    }

    @Column(length = 200)
    public String getFileLadderTruck() {
        return fileLadderTruck;
    }

    public void setFileLadderTruck(String fileLadderTruck) {
        this.fileLadderTruck = fileLadderTruck;
    }

    @Column(length = 10)
    public String getForkTruckCount() {
        return forkTruckCount;
    }

    public void setForkTruckCount(String forkTruckCount) {
        this.forkTruckCount = forkTruckCount;
    }

    @Column(length = 200)
    public String getFileForkTruck() {
        return fileForkTruck;
    }

    public void setFileForkTruck(String fileForkTruck) {
        this.fileForkTruck = fileForkTruck;
    }

    @Column(length = 10)
    public String getLittleContainerCount() {
        return littleContainerCount;
    }

    public void setLittleContainerCount(String littleContainerCount) {
        this.littleContainerCount = littleContainerCount;
    }

    @Column(length = 200)
    public String getFileLittleContainer() {
        return fileLittleContainer;
    }

    public void setFileLittleContainer(String fileLittleContainer) {
        this.fileLittleContainer = fileLittleContainer;
    }

    @Column(length = 10)
    public String getPartNameplateCount() {
        return partNameplateCount;
    }

    public void setPartNameplateCount(String partNameplateCount) {
        this.partNameplateCount = partNameplateCount;
    }

    @Column(length = 10)
    public String getStoreLampCount() {
        return storeLampCount;
    }

    public void setStoreLampCount(String storeLampCount) {
        this.storeLampCount = storeLampCount;
    }

    @Column(length = 10)
    public String getTagCardCount() {
        return tagCardCount;
    }

    public void setTagCardCount(String tagCardCount) {
        this.tagCardCount = tagCardCount;
    }

    @Column(length = 10)
    public String getComputerCount() {
        return computerCount;
    }

    public void setComputerCount(String computerCount) {
        this.computerCount = computerCount;
    }

    @Column(length = 200)
    public String getFileComputer() {
        return fileComputer;
    }

    public void setFileComputer(String fileComputer) {
        this.fileComputer = fileComputer;
    }

    @Column(length = 10)
    public String getTelephoneCount() {
        return telephoneCount;
    }

    public void setTelephoneCount(String telephoneCount) {
        this.telephoneCount = telephoneCount;
    }

    @Column(length = 200)
    public String getFileTelephone() {
        return fileTelephone;
    }

    public void setFileTelephone(String fileTelephone) {
        this.fileTelephone = fileTelephone;
    }

    @Column(length = 10)
    public String getFaxCount() {
        return faxCount;
    }

    public void setFaxCount(String faxCount) {
        this.faxCount = faxCount;
    }

    @Column(length = 200)
    public String getFileFax() {
        return fileFax;
    }

    public void setFileFax(String fileFax) {
        this.fileFax = fileFax;
    }

    @Column(length = 10)
    public String getCameraCount() {
        return cameraCount;
    }

    public void setCameraCount(String cameraCount) {
        this.cameraCount = cameraCount;
    }

    @Column(length = 200)
    public String getFileCamera() {
        return fileCamera;
    }

    public void setFileCamera(String fileCamera) {
        this.fileCamera = fileCamera;
    }

    @Column(length = 10)
    public String getPackerCount() {
        return packerCount;
    }

    public void setPackerCount(String packerCount) {
        this.packerCount = packerCount;
    }

    @Column(length = 200)
    public String getFilePacker() {
        return filePacker;
    }

    public void setFilePacker(String filePacker) {
        this.filePacker = filePacker;
    }

    public String getCoverProvinces() {
        return coverProvinces;
    }

    public void setCoverProvinces(String coverProvinces) {
        this.coverProvinces = coverProvinces;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}
