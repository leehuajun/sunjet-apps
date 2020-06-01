package com.sunjet.vm.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.dealer.DealerLevelChangeService;
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
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DealerLevelChangeFormVM extends FlowFormBaseVM {
    @WireVariable
    private DealerLevelChangeService dealerLevelChangeService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;


    private List<ProvinceEntity> provinceEntities;//省份列表
    private List<CityEntity> cityEntities;
    private DealerLevelChangeRequestEntity dealerLevelChangeRequest;
    private List<DictionaryEntity> stars = new ArrayList<>();
    private List<DictionaryEntity> qualifications = new ArrayList<>();
    private List<DictionaryEntity> selectedOtherCollaborations = new ArrayList<>();
    private List<DictionaryEntity> selectedProductsOfMaintains = new ArrayList<>();

    private List<DictionaryEntity> productsOfMaintains = new ArrayList<>();//拟维修我公司产品系列
    private List<DictionaryEntity> otherCollaborations = new ArrayList<>();//其他合作内容

    private List<String> productsOfMaintain = new ArrayList<>();
    private List<String> strOtherCollaborations = new ArrayList<>();
    private List<String> strProductsOfMaintains = new ArrayList<>();
    private Map<String, Object> variables = new HashMap<>();


    @Init(superclass = true)
    public void init() {
        this.setBaseService(dealerLevelChangeService);
        provinceEntities = regionService.findAllProvince();
        stars = CacheManager.getDictionariesByParentCode("10010");
        qualifications = CacheManager.getDictionariesByParentCode("10020");
        this.productsOfMaintains = CacheManager.getDictionariesByParentCode("10050");
        this.otherCollaborations = CacheManager.getDictionariesByParentCode("10060");
        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.dealerLevelChangeRequest = dealerLevelChangeService.findOneById(this.getBusinessId());

            if (dealerLevelChangeRequest.getOtherCollaboration() != null) {
                this.strOtherCollaborations = Arrays.asList(getArrayByString(dealerLevelChangeRequest.getOtherCollaboration()));
                for (String str : this.strOtherCollaborations) {
                    for (DictionaryEntity de : otherCollaborations) {
                        if (de.getName().equals(str)) {
                            this.selectedOtherCollaborations.add(de);
                        }
                    }
                }
            }
            if (dealerLevelChangeRequest.getDealer().getProductsOfMaintain() != null) {
                this.strProductsOfMaintains = Arrays.asList(getArrayByString(dealerLevelChangeRequest.getProductsOfMaintain()));
                for (String str : this.strProductsOfMaintains) {
                    for (DictionaryEntity de : productsOfMaintains) {
                        if (de.getName().equals(str)) {
                            this.selectedProductsOfMaintains.add(de);
                        }
                    }
                }
            }
            this.setDealer(this.dealerLevelChangeRequest.getDealer());

            if (this.dealerLevelChangeRequest.getDealer().getProvince() != null) {
                cityEntities = regionService.findCitiesByProvinceId(this.dealerLevelChangeRequest.getDealer().getProvince().getObjId());
            }
        } else {
            this.dealerLevelChangeRequest = new DealerLevelChangeRequestEntity();
            this.dealerLevelChangeRequest.setDealer(this.getDealer());
        }
        this.setEntity(this.dealerLevelChangeRequest);


    }

    private String[] getArrayByString(Object object) {
        if (object == null) {
            return new String[]{""};
        } else {
            return object.toString().split("/");
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
//        if (win != null) {
//            win.setTitle(win.getTitle() + titleMsg);
//        }
    }

    @Override
    protected Boolean checkValid() {
//        if(StringUtils.isBlank(this.dealerLevelChangeRequest.getReason())){
//            ZkUtils.showError("请输入退出原因！","系统提示");
//            return false;
//        }
        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
        this.dealerLevelChangeRequest.setDealer(this.getDealer());

        variables.put("serviceManager", this.dealerLevelChangeRequest.getDealer().getServiceManager().getLogId());
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

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (this.getDealer() == null) {
            ZkUtils.showExclamation("服务站不能为空！", "系统提示");
            return;
        }
        dealerLevelChangeRequest.setDealer(this.getDealer());
        this.setEntity(this.saveEntity(dealerLevelChangeRequest));
        showDialog();
    }

    @Override
    @Command
    @NotifyChange("cityEntities")
    public void selectProvince() {
        this.cityEntities = regionService.findCitiesByProvinceId(this.dealerLevelChangeRequest.getDealer().getProvince().getObjId());
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

    @Command
    @NotifyChange("dealerLevelChangeRequest")
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
        this.dealerLevelChangeRequest.setProductsOfMaintain(sb.toString().trim());
    }

    @Command
    @NotifyChange("*")
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
        this.dealerLevelChangeRequest.setOtherCollaboration(sb.toString().trim());
    }

    public Boolean checkCollaborationStats(String value) {
        return this.otherCollaborations.contains(value);
    }

    public Boolean checkProductsOfMaintainStats(String value) {
        return this.productsOfMaintain.contains(value);
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_DEALER);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("file15")) {  // 维修资质
            this.dealerLevelChangeRequest.setFileBusinessLicense(fileName);            // 营业执照 照片文件
        } else if (type.equalsIgnoreCase("file01")) {
            this.dealerLevelChangeRequest.setFileTaxCertificate(fileName);     // 税务登记证 照片文件
        } else if (type.equalsIgnoreCase("file02")) {
            this.dealerLevelChangeRequest.setFileBankAccountOpeningPermit(fileName);  // 银行开户行许可证
        } else if (type.equalsIgnoreCase("file03")) {
            this.dealerLevelChangeRequest.setFilePersonnelCertificate(fileName);  // 人员登记证书
        } else if (type.equalsIgnoreCase("file04")) {
            this.dealerLevelChangeRequest.setFileQualification(fileName);       // 维修资质表
        } else if (type.equalsIgnoreCase("file05")) {
            this.dealerLevelChangeRequest.setFileInvoiceInfo(fileName);      // 服务站开票信息
        } else if (type.equalsIgnoreCase("file06")) {
            this.dealerLevelChangeRequest.setFileRoadTransportLicense(fileName);   // 道路运输营业许可证
        } else if (type.equalsIgnoreCase("file07")) {
            this.dealerLevelChangeRequest.setFileOrgChart(fileName);        // 企业组织架构及设置书
        } else if (type.equalsIgnoreCase("file08")) {
            this.dealerLevelChangeRequest.setFileDevice(fileName);      // 设备清单
        } else if (type.equalsIgnoreCase("file09")) {
            this.dealerLevelChangeRequest.setFileReceptionOffice(fileName);  // 接待室图片
        } else if (type.equalsIgnoreCase("file10")) {
            this.dealerLevelChangeRequest.setFileMap(fileName);        // 地理位置
        } else if (type.equalsIgnoreCase("file11")) {
            this.dealerLevelChangeRequest.setFileGlobal(fileName);         // 服务商全貌图片
        } else if (type.equalsIgnoreCase("file12")) {
            this.dealerLevelChangeRequest.setFileOffice(fileName);          // 办公室图片
        } else if (type.equalsIgnoreCase("file13")) {
            this.dealerLevelChangeRequest.setFilePartStoreage(fileName);// 配件库房图片
        } else if (type.equalsIgnoreCase("file14")) {
            this.dealerLevelChangeRequest.setFileWorkshop(fileName);    // 维修车间
        } else if (type.equalsIgnoreCase("file16")) {
            this.dealerLevelChangeRequest.setFileLevelChange(fileName);    // 变更函
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
    @NotifyChange("dealerLevelChangeRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("file15")) {  // 维修资质
            this.dealerLevelChangeRequest.setFileBusinessLicense("");            // 营业执照 照片文件
        } else if (type.equalsIgnoreCase("file01")) {
            this.dealerLevelChangeRequest.setFileTaxCertificate("");     // 税务登记证 照片文件
        } else if (type.equalsIgnoreCase("file02")) {
            this.dealerLevelChangeRequest.setFileBankAccountOpeningPermit("");  // 银行开户行许可证
        } else if (type.equalsIgnoreCase("file03")) {
            this.dealerLevelChangeRequest.setFilePersonnelCertificate("");  // 人员登记证书
        } else if (type.equalsIgnoreCase("file04")) {
            this.dealerLevelChangeRequest.setFileQualification("");       // 维修资质表
        } else if (type.equalsIgnoreCase("file05")) {
            this.dealerLevelChangeRequest.setFileInvoiceInfo("");      // 服务站开票信息
        } else if (type.equalsIgnoreCase("file06")) {
            this.dealerLevelChangeRequest.setFileRoadTransportLicense("");   // 道路运输营业许可证
        } else if (type.equalsIgnoreCase("file07")) {
            this.dealerLevelChangeRequest.setFileOrgChart("");        // 企业组织架构及设置书
        } else if (type.equalsIgnoreCase("file08")) {
            this.dealerLevelChangeRequest.setFileDevice("");      // 设备清单
        } else if (type.equalsIgnoreCase("file09")) {
            this.dealerLevelChangeRequest.setFileReceptionOffice("");  // 接待室图片
        } else if (type.equalsIgnoreCase("file10")) {
            this.dealerLevelChangeRequest.setFileMap("");        // 地理位置
        } else if (type.equalsIgnoreCase("file11")) {
            this.dealerLevelChangeRequest.setFileGlobal("");         // 服务商全貌图片
        } else if (type.equalsIgnoreCase("file12")) {
            this.dealerLevelChangeRequest.setFileOffice("");          // 办公室图片
        } else if (type.equalsIgnoreCase("file13")) {
            this.dealerLevelChangeRequest.setFilePartStoreage("");// 配件库房图片
        } else if (type.equalsIgnoreCase("file14")) {
            this.dealerLevelChangeRequest.setFileWorkshop("");    // 维修车间
        } else if (type.equalsIgnoreCase("file16")) {
            this.dealerLevelChangeRequest.setFileLevelChange("");    // 变更函
        }
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_DEALER + filename;
    }


    public List<DictionaryEntity> getStars() {
        return stars;
    }

    public List<DictionaryEntity> getQualifications() {
        return qualifications;
    }


    public List<String> getProductsOfMaintain() {
        return productsOfMaintain;
    }

    public void setProductsOfMaintain(List<String> productsOfMaintain) {
        this.productsOfMaintain = productsOfMaintain;
    }


    public DealerLevelChangeRequestEntity getDealerLevelChangeRequest() {
        return dealerLevelChangeRequest;
    }

    public void setDealerLevelChangeRequest(DealerLevelChangeRequestEntity dealerLevelChangeRequest) {
        this.dealerLevelChangeRequest = dealerLevelChangeRequest;
    }

    public List<DictionaryEntity> getSelectedProductsOfMaintains() {
        return selectedProductsOfMaintains;
    }

    public List<String> getStrProductsOfMaintains() {
        return strProductsOfMaintains;
    }

    public List<DictionaryEntity> getProductsOfMaintains() {
        return productsOfMaintains;
    }

    public List<DictionaryEntity> getOtherCollaborations() {
        return otherCollaborations;
    }

    public void setOtherCollaborations(List<DictionaryEntity> otherCollaborations) {
        this.otherCollaborations = otherCollaborations;
    }

    public void setStrProductsOfMaintains(List<String> strProductsOfMaintains) {
        this.strProductsOfMaintains = strProductsOfMaintains;
    }

    public List<String> getStrOtherCollaborations() {
        return strOtherCollaborations;
    }

    public void setStrOtherCollaborations(List<String> strOtherCollaborations) {
        this.strOtherCollaborations = strOtherCollaborations;
    }

    public void setSelectedProductsOfMaintains(List<DictionaryEntity> selectedProductsOfMaintains) {
        this.selectedProductsOfMaintains = selectedProductsOfMaintains;
    }

    public List<DictionaryEntity> getSelectedOtherCollaborations() {
        return selectedOtherCollaborations;
    }

    public void setSelectedOtherCollaborations(List<DictionaryEntity> selectedOtherCollaborations) {
        this.selectedOtherCollaborations = selectedOtherCollaborations;
    }

    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    @Override
    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }
}
