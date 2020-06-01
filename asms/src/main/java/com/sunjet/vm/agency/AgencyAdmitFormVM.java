package com.sunjet.vm.agency;

import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.service.agency.AgencyAdmitService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class AgencyAdmitFormVM extends FlowFormBaseVM {
    @WireVariable
    private AgencyAdmitService agencyAdmitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private AgencyService agencyService;


    private List<ProvinceEntity> provinceEntities;  // 省份/直辖市 集合
    private List<CityEntity> cityEntities;          // 选中的省份/直辖市的下属城市集合
    private List<CountyEntity> countyEntities;      // 选中的城市的下属县/区集合

//    private ProvinceEntity selectedProvince;        // 选中的 省份/直辖市
//    private CityEntity selectedCity;                // 选中的 城市
//    private CountyEntity selectedCounty;            // 选中的 县/区

    private AgencyAdmitRequestEntity agencyAdmitRequest;        // 合作商对象


    @Init(superclass = true)
    public void init() {
        this.setBaseService(agencyAdmitService);
        provinceEntities = regionService.findAllProvince();

        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.agencyAdmitRequest = agencyAdmitService.findOneById(this.getBusinessId());
            this.setSelectedProvince(agencyAdmitRequest.getAgency().getProvince());
            if (this.agencyAdmitRequest.getAgency().getProvince() != null) {
                this.cityEntities = regionService.findCitiesByProvinceId(this.agencyAdmitRequest.getAgency().getProvince().getObjId());
            }
            if (this.agencyAdmitRequest.getAgency().getCity() != null) {
                this.countyEntities = regionService.findCountiesByCityId(this.agencyAdmitRequest.getAgency().getCity().getObjId());
            }
        } else {        // 业务对象和业务对象Id都为空
            agencyAdmitRequest = new AgencyAdmitRequestEntity();
            this.setAgency(new AgencyEntity());
            agencyAdmitRequest.setAgency(this.getAgency());

        }
        this.setEntity(this.agencyAdmitRequest);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
//        if (win != null) {
//            win.setTitle(win.getTitle() + titleMsg);
//        }
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    startProcessInstance(null);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                } else {
                    return;
                }
            }
        });
    }

    @Override
    protected Boolean checkValid() {
        List<String> ignoreFields = Arrays.asList("serialVersionUID", "objId", "createrId", "createrName", "modifierId", "modifierName", "code", "county", "fileTrain");
        if (ZkUtils.checkFieldValid(this.agencyAdmitRequest.getAgency(), ignoreFields) == false) {
            return false;
        }
//        agencyAdmitService.save(agencyAdmitRequest);

        if (StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getObjId()) && StringUtils.isNotBlank(this.agencyAdmitRequest.getAgency().getCode())) {
            if (agencyService.checkCodeExists(this.agencyAdmitRequest.getAgency().getCode())) {
                ZkUtils.showError("合作商编号【" + this.agencyAdmitRequest.getAgency().getCode() + "】已存在!", "系统提示");
                return false;
            }
        }
        if (this.agencyAdmitRequest.getAgency().getName() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getName())) {
            ZkUtils.showInformation("请填写服务站名称", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPhone())) {
            ZkUtils.showInformation("请填写电话", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFax() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFax())) {
            ZkUtils.showInformation("请填写传真", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getAddress() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getAddress())) {
            ZkUtils.showInformation("请填写地址", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getProvince() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getProvince().getName())) {
            ZkUtils.showInformation("请填写省份", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getCity() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getCity().getName())) {
            ZkUtils.showInformation("请填写市", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getCounty() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getCounty().getName())) {
            ZkUtils.showInformation("请填写区/县", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getBusinessLicenseCode() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getBusinessLicenseCode())) {
            ZkUtils.showInformation("请填写营业执照号", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getOrgCode() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getOrgCode())) {
            ZkUtils.showInformation("请填写组织机构代码号", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getTaxpayerCode() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getTaxpayerCode())) {
            ZkUtils.showInformation("请填写纳税人识别号", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getBank() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getBank())) {
            ZkUtils.showInformation("请填写开户行", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getBankAccount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getBankAccount())) {
            ZkUtils.showInformation("请填写银行帐号", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getLegalPerson() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getLegalPerson())) {
            ZkUtils.showInformation("请填写法人", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getLegalPersonPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getLegalPersonPhone())) {
            ZkUtils.showInformation("请填写法人联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getShopManager() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getShopManager())) {
            ZkUtils.showInformation("请填写店长", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getShopManagerPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getShopManagerPhone())) {
            ZkUtils.showInformation("请填写店长联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getTechnicalDirector() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getTechnicalDirector())) {
            ZkUtils.showInformation("请填写技术主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getTechnicalDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getTechnicalDirectorPhone())) {
            ZkUtils.showInformation("请填写技术主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPlanDirector() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPlanDirector())) {
            ZkUtils.showInformation("请填写计划主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPlanDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPlanDirectorPhone())) {
            ZkUtils.showInformation("请填写计划主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPartDirector() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPartDirector())) {
            ZkUtils.showInformation("请填写配件主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPartDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPartDirectorPhone())) {
            ZkUtils.showInformation("请填写配件主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFinanceDirector() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFinanceDirector())) {
            ZkUtils.showInformation("请填写财务主管", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFinanceDirectorPhone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFinanceDirectorPhone())) {
            ZkUtils.showInformation("请填写财务主管联系方式", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getEmployeeCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getEmployeeCount())) {
            ZkUtils.showInformation("请填写员工总人数", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getReceptionistCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getReceptionistCount())) {
            ZkUtils.showInformation("请填写接待员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getLogisticsClerkCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getLogisticsClerkCount())) {
            ZkUtils.showInformation("请填写物流员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getInvoiceClerkCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getInvoiceClerkCount())) {
            ZkUtils.showInformation("请填写开票制单员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getStoreKeeperCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getStoreKeeperCount())) {
            ZkUtils.showInformation("请填写库管员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getClerkCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getClerkCount())) {
            ZkUtils.showInformation("请填写结算员数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getForkliftCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getForkliftCount())) {
            ZkUtils.showInformation("请填写液压叉车数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getOfficeArea() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getOfficeArea())) {
            ZkUtils.showInformation("请填写办公室面积", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getStorageArea() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getStorageArea())) {
            ZkUtils.showInformation("请填写配件库面积", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getReceptionArea() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getReceptionArea())) {
            ZkUtils.showInformation("请填写接待区面积", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getShelfArea() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getShelfArea())) {
            ZkUtils.showInformation("请填写料架数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getBuildingStructure() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getBuildingStructure())) {
            ZkUtils.showInformation("请填写房屋结构", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getProductsOfSupply() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getProductsOfSupply())) {
            ZkUtils.showInformation("请填写拟供应我公司产品系列", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getOtherBrand() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getOtherBrand())) {
            ZkUtils.showInformation("请填写还兼做哪些品牌的配件", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFilePersonnelCertificate() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFilePersonnelCertificate())) {
            ZkUtils.showInformation("请填写人员登记证书", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileQualification() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileQualification())) {
            ZkUtils.showInformation("请填写维修资质表", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileBusinessLicense() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileBusinessLicense())) {
            ZkUtils.showInformation("请填写营业执照", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileTaxCertificate() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileTaxCertificate())) {
            ZkUtils.showInformation("请填写税务登记证", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileBankAccountOpeningPermit() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileBankAccountOpeningPermit())) {
            ZkUtils.showInformation("请填写银行开户行许可证", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileOrgChart() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileOrgChart())) {
            ZkUtils.showInformation("请填写企业组织架构及设置书", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileInvoiceInfo() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileInvoiceInfo())) {
            ZkUtils.showInformation("请填写合作库开票信息", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileGlobal() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileGlobal())) {
            ZkUtils.showInformation("请填写合作库全貌图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileOffice() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileOffice())) {
            ZkUtils.showInformation("请填写办公室图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileReceptionOffice() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPhone())) {
            ZkUtils.showInformation("请填写接待室图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFilePartStoreage() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFilePartStoreage())) {
            ZkUtils.showInformation("请填写配件库房图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileMap() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileMap())) {
            ZkUtils.showInformation("请填写地理位置", "提示");
            return false;
        }
//        if (this.agencyAdmitRequest.getAgency().getFileTrain() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileTrain())) {
//            ZkUtils.showInformation("请填写培训资料", "提示");
//            return false;
//        }
        if (this.agencyAdmitRequest.getAgency().getShelfCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getShelfCount())) {
            ZkUtils.showInformation("请填写标准货架数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileShelf() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileShelf())) {
            ZkUtils.showInformation("请填写标准货架图片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getContainerCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getContainerCount())) {
            ZkUtils.showInformation("请填写定制货柜数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileContainer() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileContainer())) {
            ZkUtils.showInformation("请填写定制货柜照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getLadderTruckCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getLadderTruckCount())) {
            ZkUtils.showInformation("请填写登高车数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileLadderTruck() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileLadderTruck())) {
            ZkUtils.showInformation("请填写登高车照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getForkTruckCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getForkTruckCount())) {
            ZkUtils.showInformation("请填写堆高车数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileForkTruck() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileForkTruck())) {
            ZkUtils.showInformation("请填写堆高车照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getLittleContainerCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getLittleContainerCount())) {
            ZkUtils.showInformation("请填写小件容器数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileLittleContainer() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileLittleContainer())) {
            ZkUtils.showInformation("请填写小容器照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPartNameplateCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPartNameplateCount())) {
            ZkUtils.showInformation("请填写零件铭牌数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getStoreLampCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getStoreLampCount())) {
            ZkUtils.showInformation("请填写库房灯光数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getTagCardCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getTagCardCount())) {
            ZkUtils.showInformation("请填写货物标签卡数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getComputerCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getComputerCount())) {
            ZkUtils.showInformation("请填写电脑数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileComputer() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileComputer())) {
            ZkUtils.showInformation("请填写电脑照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getTelephoneCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getTelephoneCount())) {
            ZkUtils.showInformation("请填写电话数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileTelephone() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileTelephone())) {
            ZkUtils.showInformation("请填写电话照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFaxCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFaxCount())) {
            ZkUtils.showInformation("请填写传真数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileFax() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileFax())) {
            ZkUtils.showInformation("请填写传真机照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getCameraCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getCameraCount())) {
            ZkUtils.showInformation("请填写相机数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFileCamera() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileCamera())) {
            ZkUtils.showInformation("请填写相机照片", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getPackerCount() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getPackerCount())) {
            ZkUtils.showInformation("请填写手动打包机数量", "提示");
            return false;
        }
        if (this.agencyAdmitRequest.getAgency().getFilePacker() == null || StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFilePacker())) {
            ZkUtils.showInformation("请填写打包机照片", "提示");
            return false;
        }


        return true;
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        this.setEntity(this.saveEntity(this.agencyAdmitRequest));
        showDialog();
    }

    @Override
    @Command
    public void showHandleForm() {

        if (this.agencyAdmitRequest.getCanEditCode() == true && StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getCode())) {
            ZkUtils.showExclamation("请输入服务站编号！", "系统提示");
            return;
        } else {
            agencyAdmitService.save(this.agencyAdmitRequest);
        }

        //if (StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getCoverProvinces())) {
        //    ZkUtils.showExclamation("请输入覆盖省份", "系统提示");
        //    return;
        //}

        if (this.agencyAdmitRequest.getCanUpload() == true && StringUtils.isBlank(this.agencyAdmitRequest.getAgency().getFileTrain())) {
            ZkUtils.showExclamation("请上传文件！", "系统提示");
            return;
        } else {
            agencyAdmitService.save(this.agencyAdmitRequest);
        }


        super.showHandleForm();
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void selectProvince() {
        this.cityEntities = regionService.findCitiesByProvinceId(this.agencyAdmitRequest.getAgency().getProvince().getObjId());
    }

    @Command
    @NotifyChange("*")
    public void selectCity() {
        this.countyEntities = regionService.findCountiesByCityId(this.agencyAdmitRequest.getAgency().getCity().getObjId());
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_AGENCY);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAdmitRequest.getAgency().setFileQualification(fileName);
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAdmitRequest.getAgency().setFileOrgChart(fileName);
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAdmitRequest.getAgency().setFileBusinessLicense(fileName);
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAdmitRequest.getAgency().setFileGlobal(fileName);
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAdmitRequest.getAgency().setFileTaxCertificate(fileName);
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAdmitRequest.getAgency().setFileOffice(fileName);
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAdmitRequest.getAgency().setFileBankAccountOpeningPermit(fileName);
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAdmitRequest.getAgency().setFileReceptionOffice(fileName);
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAdmitRequest.getAgency().setFileInvoiceInfo(fileName);
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAdmitRequest.getAgency().setFilePartStoreage(fileName);
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAdmitRequest.getAgency().setFilePersonnelCertificate(fileName);
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAdmitRequest.getAgency().setFileMap(fileName);
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAdmitRequest.getAgency().setFileShelf(fileName);
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAdmitRequest.getAgency().setFileComputer(fileName);
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAdmitRequest.getAgency().setFileContainer(fileName);
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAdmitRequest.getAgency().setFileTelephone(fileName);
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAdmitRequest.getAgency().setFileLadderTruck(fileName);
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAdmitRequest.getAgency().setFileFax(fileName);
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAdmitRequest.getAgency().setFileForkTruck(fileName);
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAdmitRequest.getAgency().setFileCamera(fileName);
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAdmitRequest.getAgency().setFileLittleContainer(fileName);
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAdmitRequest.getAgency().setFilePacker(fileName);
        }

//        else if (type.equalsIgnoreCase("t23")) {     //手动打包机
//            this.agencyAdmitRequest.getAgency().setFilePacker(fileName);
//        }
        else if (type.equalsIgnoreCase("file23")) {   //培训资料
            this.agencyAdmitRequest.getAgency().setFileTrain(fileName);
        }
//        Clients.showNotification(event.getMedia().getContentType());
//        if (!event.getMedia().getContentType().startsWith("image/")) {
////            Clients.showNotification("Please upload an image");
//            return;
//        } else {
////            Clients.showNotification(photo.getName());
//        }
//        this.photo = photo;
    }

    @Command
    @NotifyChange("agencyAdmitRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAdmitRequest.getAgency().setFileQualification("");
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAdmitRequest.getAgency().setFileOrgChart("");
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAdmitRequest.getAgency().setFileBusinessLicense("");
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAdmitRequest.getAgency().setFileGlobal("");
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAdmitRequest.getAgency().setFileTaxCertificate("");
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAdmitRequest.getAgency().setFileOffice("");
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAdmitRequest.getAgency().setFileBankAccountOpeningPermit("");
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAdmitRequest.getAgency().setFileReceptionOffice("");
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAdmitRequest.getAgency().setFileInvoiceInfo("");
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAdmitRequest.getAgency().setFilePartStoreage("");
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAdmitRequest.getAgency().setFilePersonnelCertificate("");
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAdmitRequest.getAgency().setFileMap("");
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAdmitRequest.getAgency().setFileShelf("");
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAdmitRequest.getAgency().setFileComputer("");
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAdmitRequest.getAgency().setFileContainer("");
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAdmitRequest.getAgency().setFileTelephone("");
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAdmitRequest.getAgency().setFileLadderTruck("");
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAdmitRequest.getAgency().setFileFax("");
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAdmitRequest.getAgency().setFileForkTruck("");
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAdmitRequest.getAgency().setFileCamera("");
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAdmitRequest.getAgency().setFileLittleContainer("");
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAdmitRequest.getAgency().setFilePacker("");
        }

//        else if (type.equalsIgnoreCase("t23")) {     //手动打包机
//            this.agencyAdmitRequest.getAgency().setFilePacker(fileName);
//        }
        else if (type.equalsIgnoreCase("file23")) {   //培训资料
            this.agencyAdmitRequest.getAgency().setFileTrain("");
        }
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_AGENCY + filename;
    }


    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public void setCityEntities(List<CityEntity> cityEntities) {
        this.cityEntities = cityEntities;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public List<CountyEntity> getCountyEntities() {
        return countyEntities;
    }

    public AgencyAdmitRequestEntity getAgencyAdmitRequest() {
        return agencyAdmitRequest;
    }

    public void setAgencyAdmitRequest(AgencyAdmitRequestEntity agencyAdmitRequest) {
        this.agencyAdmitRequest = agencyAdmitRequest;
    }

}
