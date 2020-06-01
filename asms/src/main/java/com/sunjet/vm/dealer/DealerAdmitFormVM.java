package com.sunjet.vm.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.CountyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.repository.admin.DictionaryRepository;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.dealer.DealerAdmitService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DealerAdmitFormVM extends FlowFormBaseVM {
    @WireVariable
    private DealerAdmitService dealerAdmitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DictionaryRepository dictionaryRepository;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private RoleService roleService;

    private List<UserEntity> serviceManagers = new ArrayList<>();
    private List<String> strOtherCollaborations = new ArrayList<>();
    private List<String> strProductsOfMaintains = new ArrayList<>();
    private List<DictionaryEntity> selectedOtherCollaborations = new ArrayList<>();
    private List<DictionaryEntity> selectedProductsOfMaintains = new ArrayList<>();

    private DealerAdmitRequestEntity dealerAdmitRequest;
    private List<ProvinceEntity> provinceEntities;
    private List<CityEntity> cityEntities = new ArrayList<>();
    private List<CountyEntity> countyEntities;

    private List<DealerEntity> parentDealers = new ArrayList<>();       // 列出所有父级服务站
    private Boolean canSelectParent = false;        // 是否可以选择父级服务站
    private List<DictionaryEntity> stars = new ArrayList<>();
    private List<DictionaryEntity> qualifications = new ArrayList<>();
    private List<DictionaryEntity> productsOfMaintains = new ArrayList<>();//拟维修我公司产品系列
    private List<DictionaryEntity> otherCollaborations = new ArrayList<>();//其他合作内容
    private Map<String, Object> variables = new HashMap<>();  //流程变量


    @Init(superclass = true)
    public void init() {
        this.setBaseService(dealerAdmitService);
        provinceEntities = regionService.findAllProvince();
        this.stars = CacheManager.getDictionariesByParentCode("10010");
        this.qualifications = CacheManager.getDictionariesByParentCode("10020");
        this.productsOfMaintains = CacheManager.getDictionariesByParentCode("10050");
        this.otherCollaborations = CacheManager.getDictionariesByParentCode("10060");

        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.dealerAdmitRequest = dealerAdmitService.findOneById(this.getBusinessId());
            this.setDealer(this.dealerAdmitRequest.getDealer());

            if (dealerAdmitRequest.getDealer().getOtherCollaboration() != null) {
                this.strOtherCollaborations = Arrays.asList(dealerAdmitRequest.getDealer().getOtherCollaboration().split("/"));
                for (String str : this.strOtherCollaborations) {
                    for (DictionaryEntity de : otherCollaborations) {
                        if (de.getName().equals(str)) {
                            this.selectedOtherCollaborations.add(de);
                        }
                    }
                }
            }
            if (dealerAdmitRequest.getDealer().getProductsOfMaintain() != null) {
                this.strProductsOfMaintains = Arrays.asList(dealerAdmitRequest.getDealer().getProductsOfMaintain().split("/"));
                for (String str : this.strProductsOfMaintains) {
                    for (DictionaryEntity de : productsOfMaintains) {
                        if (de.getName().equals(str)) {
                            this.selectedProductsOfMaintains.add(de);
                        }
                    }
                }
            }
            if (this.dealerAdmitRequest.getDealer().getProvince() != null) {
                this.cityEntities = regionService.findCitiesByProvinceId(this.dealerAdmitRequest.getDealer().getProvince().getObjId());
            }
            if (this.dealerAdmitRequest.getDealer().getCity() != null) {
                this.countyEntities = regionService.findCountiesByCityId(this.dealerAdmitRequest.getDealer().getCity().getObjId());
            }

            if (this.dealerAdmitRequest.getCanEditServiceManager()) {
                RoleEntity role = roleService.findOneWithUsersByRoleId("role000");
                Stream<UserEntity> stream = role.getUsers().stream().filter(new Predicate<UserEntity>() {
                    @Override
                    public boolean test(UserEntity userEntity) {
                        if (userEntity.getEnabled() == true)
                            return true;

                        return false;
                    }
                });
                this.serviceManagers.addAll(stream.collect(Collectors.toList()));
            }


        } else {        // 业务对象和业务对象Id都为空
//            provinceEntities = regionService.findAllProvince();
            dealerAdmitRequest = new DealerAdmitRequestEntity();
//            DealerEntity dealerEntity = new DealerEntity();
            this.setDealer(new DealerEntity());
            dealerAdmitRequest.setDealer(this.getDealer());

            if (CommonHelper.getActiveUser().getDealer() != null) {   // 服务站用户
                this.dealerAdmitRequest.getDealer().setParent(CommonHelper.getActiveUser().getDealer());
                this.dealerAdmitRequest.getDealer().setLevel("二级");
                this.canSelectParent = false;
            } else {
                this.canSelectParent = true;
            }
        }
        this.setEntity(this.dealerAdmitRequest);


//        initStatus();

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
        if (CommonHelper.getActiveUser().getDealer() != null) {
            if (CommonHelper.getActiveUser().getDealer().getServiceManager() == null) {
                ZkUtils.showError("当前服务站的服务经理为空！", "系统提示！");
                return;
            }
            variables.put("submitter", "dealer");
            variables.put("serviceManager", CommonHelper.getActiveUser().getDealer().getServiceManager().getLogId());
            ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        // 用户点击的是确定按钮
                        startProcessInstance(variables);
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                    } else {
                        return;
                    }
                }
            });
        } else {
            variables.put("submitter", "none");
            ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    int clickedButton = (Integer) event.getData();
                    if (clickedButton == Messagebox.OK) {
                        // 用户点击的是确定按钮
                        startProcessInstance(variables);
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                    } else {
                        return;
                    }
                }
            });
        }
    }

    @Override
    @Command
    @NotifyChange("parentDealers")
    public void searchDealers(@BindingParam("model") String keyword) {
        this.parentDealers = dealerService.findWithoutChild(this.getKeyword().trim());
    }

    @Override
    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.dealerAdmitRequest.getDealer().setParent(dealer);
        dealerAdmitRequest.getDealer().setLevel("二级");
    }

    @Override
    @Command
    @NotifyChange({"dealerAdmitRequest"})
    public void clearSelectedDealer() {
        this.dealerAdmitRequest.getDealer().setParent(null);
        dealerAdmitRequest.getDealer().setLevel("一级");
    }

    @Override
    protected Boolean checkValid() {
        List<String> ignoreFields = Arrays.asList("serialVersionUID", "objId", "createrId", "createrName", "modifierId", "modifierName", "code", "county", "parent", "serviceManager", "fileTrain");
        if (ZkUtils.checkFieldValid(this.dealerAdmitRequest.getDealer(), ignoreFields) == false) {
            return false;
        }

        //this.dealerAdmitRequest.setStatus(DocStatus.DRAFT.getIndex());

        if (StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getObjId()) && StringUtils.isNotBlank(this.dealerAdmitRequest.getDealer().getCode())) {
            if (dealerService.checkCodeExists(this.dealerAdmitRequest.getDealer().getCode())) {
                ZkUtils.showError("服务站编号【" + this.dealerAdmitRequest.getDealer().getCode() + "】已存在!", "系统提示");
                return false;
            }
        }
        //基本信息验证
        if (this.dealerAdmitRequest.getDealer().getName() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getName())) {
            ZkUtils.showInformation("请填写服务站名称", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getAddress() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getAddress())) {
            ZkUtils.showInformation("请填写服务站地址", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getPhone())) {
            ZkUtils.showInformation("请填写电话", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFax() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFax())) {
            ZkUtils.showInformation("请填写传真", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getProvince() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getProvince().getName())) {
            ZkUtils.showInformation("请选择省份", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getCity() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getCity().getName())) {
            ZkUtils.showInformation("请选择市", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getOrgCode() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getOrgCode())) {
            ZkUtils.showInformation("请填写组织机构代码", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getTaxpayerCode() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getOrgCode())) {
            ZkUtils.showInformation("请填写纳税人识别号", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getBank() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getBank())) {
            ZkUtils.showInformation("请填写开户银行", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getBankAccount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getBankAccount())) {
            ZkUtils.showInformation("请填写银行账号", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getBusinessLicenseCode() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getBusinessLicenseCode())) {
            ZkUtils.showInformation("请填写营业执照号", "提示");
            return false;
        }
        //人员信息验证
        if (this.dealerAdmitRequest.getDealer().getLegalPerson() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getLegalPerson())) {
            ZkUtils.showInformation("请填写法人代表", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getLegalPersonPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getLegalPersonPhone())) {
            ZkUtils.showInformation("请填写法人联系电话", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getStationMaster() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getStationMaster())) {
            ZkUtils.showInformation("请填写站长", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getStationMasterPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getStationMasterPhone())) {
            ZkUtils.showInformation("请填写站长电话", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getTechnicalDirector() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getTechnicalDirector())) {
            ZkUtils.showInformation("请填写技术主管", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getTechnicalDirectorPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getTechnicalDirectorPhone())) {
            ZkUtils.showInformation("请填写技术主管电话", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getClaimDirector() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getClaimDirector())) {
            ZkUtils.showInformation("请填写索赔主管", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getClaimDirectorPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getClaimDirectorPhone())) {
            ZkUtils.showInformation("请填写索赔主管电话", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getPartDirector() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getPartDirector())) {
            ZkUtils.showInformation("请填写配件主管", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getPartDirectorPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getPartDirectorPhone())) {
            ZkUtils.showInformation("请填写配件主管电话", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFinanceDirector() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFinanceDirector())) {
            ZkUtils.showInformation("请填写财务经理", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFinanceDirectorPhone() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFinanceDirectorPhone())) {
            ZkUtils.showInformation("请填写财务经理电话", "提示");
            return false;
        }

        if (this.dealerAdmitRequest.getDealer().getEmployeeCount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getEmployeeCount())) {
            ZkUtils.showInformation("请填写员工人数", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getReceptionistCount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getReceptionistCount())) {
            ZkUtils.showInformation("请填接待员数量", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getPartKeeyperCount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getPartKeeyperCount())) {
            ZkUtils.showInformation("请填写配件员数量", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getMaintainerCount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getMaintainerCount())) {
            ZkUtils.showInformation("请填写维修工数量", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getQcInspectorCount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getQcInspectorCount())) {
            ZkUtils.showInformation("请填写质检员数量", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getClerkCount() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getClerkCount())) {
            ZkUtils.showInformation("请填写结算员数量", "提示");
            return false;
        }
        //验证场地信息
        if (this.dealerAdmitRequest.getDealer().getParkingArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getParkingArea())) {
            ZkUtils.showInformation("请填写停车区面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getReceptionArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getReceptionArea())) {
            ZkUtils.showInformation("请填写接待室面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getGeneralArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getGeneralArea())) {
            ZkUtils.showInformation("请填写综合维修区面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getAssemblyArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getAssemblyArea())) {
            ZkUtils.showInformation("请填写总成维修区面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getStorageArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getStorageArea())) {
            ZkUtils.showInformation("请填写配件库总面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getStorageWulingArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getStorageWulingArea())) {
            ZkUtils.showInformation("请填写五菱配件库总面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getStorageUserdPartArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getStorageUserdPartArea())) {
            ZkUtils.showInformation("请填写旧件库总面积", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getStorageUserdPartArea() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getStorageWulingUserdPartArea())) {
            ZkUtils.showInformation("请填写五菱旧件库总面积", "提示");
            return false;
        }
        //资质信息
        if (this.dealerAdmitRequest.getDealer().getOtherMaintainCondition() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getOtherMaintainCondition())) {
            ZkUtils.showInformation("请填写其他车辆维修条件", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getOtherBrand() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getOtherBrand())) {
            ZkUtils.showInformation("请填写兼做的品牌服务", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getProductsOfMaintain() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getProductsOfMaintain())) {
            ZkUtils.showInformation("请选择拟维修我公司产品", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getPartReport() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getPartReport())) {
            ZkUtils.showInformation("请上传配件储配表", "提示");
            return false;
        }
        //图片信息(附件)检查
        if (this.dealerAdmitRequest.getDealer().getFileBusinessLicense() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileBusinessLicense())) {
            ZkUtils.showInformation("请上传营业执照", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileTaxCertificate() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileTaxCertificate())) {
            ZkUtils.showInformation("请上传税务登记证", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileBankAccountOpeningPermit() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileBankAccountOpeningPermit())) {
            ZkUtils.showInformation("请上传银行开户行许可证", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFilePersonnelCertificate() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFilePersonnelCertificate())) {
            ZkUtils.showInformation("请上传人员登记证书", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileQualification() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileQualification())) {
            ZkUtils.showInformation("请上传维修资质表", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileInvoiceInfo() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileInvoiceInfo())) {
            ZkUtils.showInformation("请上传服务站开票信息", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileRoadTransportLicense() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileRoadTransportLicense())) {
            ZkUtils.showInformation("请上传道路运输营业许可证", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileOrgChart() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileOrgChart())) {
            ZkUtils.showInformation("请上传企业组织架构及设置书", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileDevice() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileDevice())) {
            ZkUtils.showInformation("请上传设备清单", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileReceptionOffice() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileReceptionOffice())) {
            ZkUtils.showInformation("请上传接待室图片", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileGlobal() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileGlobal())) {
            ZkUtils.showInformation("请上传服务商全貌图片", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileOffice() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileOffice())) {
            ZkUtils.showInformation("请上传办公室图片", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileWorkshop() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileWorkshop())) {
            ZkUtils.showInformation("请上传维修车间图片", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFilePartStoreage() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFilePartStoreage())) {
            ZkUtils.showInformation("请上传配件库房图片", "提示");
            return false;
        }
        if (this.dealerAdmitRequest.getDealer().getFileMap() == null || StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileMap())) {
            ZkUtils.showInformation("请上传地理位置", "提示");
            return false;
        }
//        if (this.dealerAdmitRequest.getDealer().getFileTrain() == null ) {
//            ZkUtils.showInformation("请上传培训资料", "提示");
//            return false;
//        }


        return true;
    }

    /**
     * 保存对象
     */
    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (dealerAdmitRequest.getObjId() == null) {
            this.setEntity(this.saveEntity(dealerAdmitRequest));
            showDialog();
        } else {
            DealerAdmitRequestEntity dealerAdmitRequestEntity = dealerAdmitService.findOneById(dealerAdmitRequest.getObjId());
            if (dealerAdmitRequestEntity.getStatus().equals(DocStatus.DRAFT.getIndex()) || dealerAdmitRequestEntity.getStatus().equals(DocStatus.REJECT.getIndex())) {
                this.setEntity(this.saveEntity(dealerAdmitRequest));
                showDialog();
            } else {
                ZkUtils.showError("非可编辑状态", "提示");
            }
        }


    }

    @Command
    @NotifyChange("readonly")
    public void update() {
        this.setReadonly(false);
    }

    @Command
    @NotifyChange("*")
    public void updateDealer() {
        dealerAdmitService.save(dealerAdmitRequest);
    }

    @Override
    @Command
    @NotifyChange("cityEntities")
    public void selectProvince() {
//        this.dealerAdmitRequest.getDealer().setProvince(province);
//        System.out.println(province.getName());
        this.cityEntities = regionService.findCitiesByProvinceId(this.dealerAdmitRequest.getDealer().getProvince().getObjId());
    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_DEALER);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("file01")) {  // 维修资质
            this.dealerAdmitRequest.getDealer().setFileBusinessLicense(fileName);
        } else if (type.equalsIgnoreCase("file02")) {
            this.dealerAdmitRequest.getDealer().setFileTaxCertificate(fileName);
        } else if (type.equalsIgnoreCase("file03")) {
            this.dealerAdmitRequest.getDealer().setFileBankAccountOpeningPermit(fileName);
        } else if (type.equalsIgnoreCase("file04")) {
            this.dealerAdmitRequest.getDealer().setFilePersonnelCertificate(fileName);
        } else if (type.equalsIgnoreCase("file05")) {
            this.dealerAdmitRequest.getDealer().setFileQualification(fileName);
        } else if (type.equalsIgnoreCase("file06")) {
            this.dealerAdmitRequest.getDealer().setFileInvoiceInfo(fileName);
        } else if (type.equalsIgnoreCase("file07")) {
            this.dealerAdmitRequest.getDealer().setFileRoadTransportLicense(fileName);
        } else if (type.equalsIgnoreCase("file08")) {
            this.dealerAdmitRequest.getDealer().setFileOrgChart(fileName);
        } else if (type.equalsIgnoreCase("file09")) {
            this.dealerAdmitRequest.getDealer().setFileDevice(fileName);
        } else if (type.equalsIgnoreCase("file10")) {
            this.dealerAdmitRequest.getDealer().setFileReceptionOffice(fileName);
        } else if (type.equalsIgnoreCase("file11")) {
            this.dealerAdmitRequest.getDealer().setFileMap(fileName);
        } else if (type.equalsIgnoreCase("file12")) {
            this.dealerAdmitRequest.getDealer().setFileGlobal(fileName);
        } else if (type.equalsIgnoreCase("file13")) {
            this.dealerAdmitRequest.getDealer().setFileOffice(fileName);
        } else if (type.equalsIgnoreCase("file14")) {
            this.dealerAdmitRequest.getDealer().setFilePartStoreage(fileName);
        } else if (type.equalsIgnoreCase("file15")) {
            this.dealerAdmitRequest.getDealer().setFileWorkshop(fileName);
        } else if (type.equalsIgnoreCase("file16")) {
            this.dealerAdmitRequest.getDealer().setFileTrain(fileName);
        } else if (type.equalsIgnoreCase("file17")) {
            this.dealerAdmitRequest.getDealer().setPartReport(fileName);
        }
//        else if(type.equalsIgnoreCase("file16")) {
//            this.dealerAdmitRequest.getDealer().setFileMap(fileName);
//        }

//
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
    @NotifyChange("dealerAdmitRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (this.dealerAdmitRequest.getStatus().equals(DocStatus.DRAFT.getIndex())) {
            //草稿状态不能删除图片
            ZkUtils.showError("非草稿状态不能修改", "提示");
            return;
        } else {
            if (type.equalsIgnoreCase("file01")) {  // 维修资质
                this.dealerAdmitRequest.getDealer().setFileBusinessLicense("");
            } else if (type.equalsIgnoreCase("file02")) {
                this.dealerAdmitRequest.getDealer().setFileTaxCertificate("");
            } else if (type.equalsIgnoreCase("file03")) {
                this.dealerAdmitRequest.getDealer().setFileBankAccountOpeningPermit("");
            } else if (type.equalsIgnoreCase("file04")) {
                this.dealerAdmitRequest.getDealer().setFilePersonnelCertificate("");
            } else if (type.equalsIgnoreCase("file05")) {
                this.dealerAdmitRequest.getDealer().setFileQualification("");
            } else if (type.equalsIgnoreCase("file06")) {
                this.dealerAdmitRequest.getDealer().setFileInvoiceInfo("");
            } else if (type.equalsIgnoreCase("file07")) {
                this.dealerAdmitRequest.getDealer().setFileRoadTransportLicense("");
            } else if (type.equalsIgnoreCase("file08")) {
                this.dealerAdmitRequest.getDealer().setFileOrgChart("");
            } else if (type.equalsIgnoreCase("file09")) {
                this.dealerAdmitRequest.getDealer().setFileDevice("");
            } else if (type.equalsIgnoreCase("file10")) {
                this.dealerAdmitRequest.getDealer().setFileReceptionOffice("");
            } else if (type.equalsIgnoreCase("file11")) {
                this.dealerAdmitRequest.getDealer().setFileMap("");
            } else if (type.equalsIgnoreCase("file12")) {
                this.dealerAdmitRequest.getDealer().setFileGlobal("");
            } else if (type.equalsIgnoreCase("file13")) {
                this.dealerAdmitRequest.getDealer().setFileOffice("");
            } else if (type.equalsIgnoreCase("file14")) {
                this.dealerAdmitRequest.getDealer().setFilePartStoreage("");
            } else if (type.equalsIgnoreCase("file15")) {
                this.dealerAdmitRequest.getDealer().setFileWorkshop("");
            } else if (type.equalsIgnoreCase("file16")) {
                this.dealerAdmitRequest.getDealer().setFileTrain("");
            } else if (type.equalsIgnoreCase("file17")) {
                this.dealerAdmitRequest.getDealer().setPartReport("");
            }
        }

    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectCollaboration() {
        logger.info(this.selectedOtherCollaborations.size() + "");
        StringBuffer sb = new StringBuffer();
        int count = this.selectedOtherCollaborations.size();
        this.strOtherCollaborations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strOtherCollaborations.add(this.selectedOtherCollaborations.get(i).getName());
            if (i == 0) {
                sb.append(this.selectedOtherCollaborations.get(i).getName());
            } else {
                sb.append("/" + this.selectedOtherCollaborations.get(i).getName());
            }
        }
        this.dealerAdmitRequest.getDealer().setOtherCollaboration(sb.toString().trim());
    }

    public Boolean checkCollaborationStats(String value) {
        LoggerUtil.getLogger().info(value);
        return this.strOtherCollaborations.contains(value);
    }

    @Command
    @NotifyChange("dealerAdmitRequest")
    public void selectProductsOfMaintain() {
        logger.info(this.selectedProductsOfMaintains.size() + "");
        StringBuffer sb = new StringBuffer();
        int count = this.selectedProductsOfMaintains.size();
        this.strProductsOfMaintains = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strProductsOfMaintains.add(this.selectedProductsOfMaintains.get(i).getName());
            if (i == 0) {
                sb.append(this.selectedProductsOfMaintains.get(i).getName());
            } else {
                sb.append("/" + this.selectedProductsOfMaintains.get(i).getName());
            }
        }
        this.dealerAdmitRequest.getDealer().setProductsOfMaintain(sb.toString().trim());
    }

    public Boolean checkProductsOfMaintainStats(String value) {
        return this.selectedProductsOfMaintains.contains(value);
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_DEALER + filename;
    }


    @Override
    @Command
    public void showHandleForm() {
        if (this.dealerAdmitRequest.getCanEditServiceManager() == true && this.dealerAdmitRequest.getDealer().getServiceManager() == null) {
            ZkUtils.showExclamation("请选择服务经理！", "系统提示");
            return;
        } else {
            dealerAdmitService.save(this.dealerAdmitRequest);
        }

        if (this.dealerAdmitRequest.getCanEditCode() == true) {
            if (StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getCode())) {
                ZkUtils.showExclamation("请输入服务站编号！", "系统提示");
                return;
            } else if (this.dealerAdmitRequest.getDealer().getCode().length() != 7) {
                ZkUtils.showExclamation("服务站编号长度必须等于7位", "系统提示");
                return;
            } else if (!this.dealerAdmitRequest.getDealer().getCode().trim().substring(3, 4).equals("1") && !this.dealerAdmitRequest.getDealer().getCode().trim().substring(3, 4).equals("2")) {
//                ZkUtils.showInformation(this.dealerAdmitRequest.getDealer().getCode().trim().substring(3,4),"title");
//                ZkUtils.showInformation(String.valueOf(this.dealerAdmitRequest.getDealer().getCode().trim().charAt(3)),"title");
                ZkUtils.showExclamation("服务站编号第4位必须是1或者2", "系统提示");
                return;
            } else {
                dealerAdmitService.save(this.dealerAdmitRequest);
            }
        }

        if (this.dealerAdmitRequest.getCanUpload() == true && StringUtils.isBlank(this.dealerAdmitRequest.getDealer().getFileTrain())) {
            ZkUtils.showExclamation("请上传文件！", "系统提示");
            return;
        } else {
            dealerAdmitService.save(this.dealerAdmitRequest);
        }

        super.showHandleForm();
    }

    public List<String> getStrOtherCollaborations() {
        return strOtherCollaborations;
    }

    public Boolean getCanSelectParent() {
        return canSelectParent;
    }

    public List<DealerEntity> getParentDealers() {
        return parentDealers;
    }

    public List<DictionaryEntity> getSelectedProductsOfMaintains() {
        return selectedProductsOfMaintains;
    }

    public void setSelectedProductsOfMaintains(List<DictionaryEntity> selectedProductsOfMaintains) {
        this.selectedProductsOfMaintains = selectedProductsOfMaintains;
    }

    public List<DictionaryEntity> getSelectedOtherCollaborations() {
        return selectedOtherCollaborations;
    }

    public List<DictionaryEntity> getProductsOfMaintains() {
        return productsOfMaintains;
    }

    public void setSelectedOtherCollaborations(List<DictionaryEntity> selectedOtherCollaborations) {
        this.selectedOtherCollaborations = selectedOtherCollaborations;
    }

    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public void setCityEntities(List<CityEntity> cityEntities) {
        this.cityEntities = cityEntities;
    }

    public List<CountyEntity> getCountyEntities() {
        return countyEntities;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public DealerAdmitRequestEntity getDealerAdmitRequest() {
        return dealerAdmitRequest;
    }

    public void setDealerAdmitRequest(DealerAdmitRequestEntity dealerAdmitRequest) {
        this.dealerAdmitRequest = dealerAdmitRequest;
    }

    public List<DictionaryEntity> getStars() {
        return stars;
    }

    public List<DictionaryEntity> getQualifications() {
        return qualifications;
    }

    public List<DictionaryEntity> getOtherCollaborations() {
        return otherCollaborations;
    }

    public List<UserEntity> getServiceManagers() {
        return serviceManagers;
    }

    public void setServiceManagers(List<UserEntity> serviceManagers) {
        this.serviceManagers = serviceManagers;
    }

}
