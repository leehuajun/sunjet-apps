package com.sunjet.vm.dealer;

import com.sunjet.cache.CacheManager;
import com.sunjet.model.admin.DictionaryEntity;
import com.sunjet.model.basic.CityEntity;
import com.sunjet.model.basic.ProvinceEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.dealer.DealerAlterService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DealerAlterFormVM extends FlowFormBaseVM {

    @WireVariable
    private DealerAlterService dealerAlterService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;

    private List<ProvinceEntity> provinceEntities;//省份列表
    private List<CityEntity> cityEntities;//市区列表
    private DealerAlterRequestEntity dealerAlterRequest;//接收列表model的服务站变更对象

    private List<String> strOtherCollaborations = new ArrayList<>();
    private List<String> strProductsOfMaintains = new ArrayList<>();
    private List<DictionaryEntity> selectedOtherCollaborations = new ArrayList<>();
    private List<DictionaryEntity> selectedProductsOfMaintains = new ArrayList<>();

    private List<DictionaryEntity> productsOfMaintains = new ArrayList<>();//拟维修我公司产品系列
    private List<DictionaryEntity> otherCollaborations = new ArrayList<>();//其他合作内容

    private List<DictionaryEntity> stars = new ArrayList<>();
    private List<DictionaryEntity> qualifications = new ArrayList<>();


    @Init(superclass = true)
    public void init() {
        this.setBaseService(dealerAlterService);
        provinceEntities = regionService.findAllProvince();
        stars = CacheManager.getDictionariesByParentCode("10010");
        qualifications = CacheManager.getDictionariesByParentCode("10020");
        this.productsOfMaintains = CacheManager.getDictionariesByParentCode("10050");
        this.otherCollaborations = CacheManager.getDictionariesByParentCode("10060");
        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.dealerAlterRequest = dealerAlterService.findOneById(this.getBusinessId());

            if (dealerAlterRequest.getOtherCollaboration() != null) {
                this.strOtherCollaborations = Arrays.asList(dealerAlterRequest.getOtherCollaboration().split("/"));
                for (String str : this.strOtherCollaborations) {
                    for (DictionaryEntity de : otherCollaborations) {
                        if (de.getName().equals(str)) {
                            this.selectedOtherCollaborations.add(de);
                        }
                    }
                }
            }
            if (dealerAlterRequest.getProductsOfMaintain() != null) {
                this.strProductsOfMaintains = Arrays.asList(dealerAlterRequest.getProductsOfMaintain().split("/"));
                for (String str : this.strProductsOfMaintains) {
                    for (DictionaryEntity de : productsOfMaintains) {
                        if (de.getName().equals(str)) {
                            this.selectedProductsOfMaintains.add(de);
                        }
                    }
                }
            }
            this.setDealer(this.dealerAlterRequest.getDealer());
            // dealerAlterRequest.setOriginDealerName(selectedDealer.getName());
            if (this.dealerAlterRequest != null && this.dealerAlterRequest.getDealer() != null) {
                if (this.dealerAlterRequest.getDealer().getProvince() != null) {
                    cityEntities = regionService.findCitiesByProvinceId(this.dealerAlterRequest.getDealer().getProvince().getObjId());
                }
            }
        } else {
            this.dealerAlterRequest = new DealerAlterRequestEntity();
            this.dealerAlterRequest.setDealer(this.getDealer());
        }
        this.setEntity(this.dealerAlterRequest);


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
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
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
        return true;
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (this.getDealer() == null) {
            ZkUtils.showExclamation("服务站不能为空！", "系统提示");
            return;
        }
        this.dealerAlterRequest.setDealer(this.getDealer());
        this.setEntity(this.saveEntity(dealerAlterRequest));
        showDialog();
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

    @Override
    @Command
    @NotifyChange("cityEntities")
    public void selectProvince() {
        this.cityEntities = regionService.findCitiesByProvinceId(this.dealerAlterRequest.getDealer().getProvince().getObjId());
    }

    @Command
    @NotifyChange("dealerAlterRequest")
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
        this.dealerAlterRequest.setOtherCollaboration(sb.toString().trim());
    }


    @Command
    @NotifyChange("dealerAlterRequest")
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
        this.dealerAlterRequest.setProductsOfMaintain(sb.toString().trim());
    }


    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_DEALER);
//        this.agencyAdmitRequest.getAgency().setFileQualification(fileName);

        if (type.equalsIgnoreCase("file15")) {  // 维修资质
            this.dealerAlterRequest.setFileBusinessLicense(fileName);            // 营业执照 照片文件
        } else if (type.equalsIgnoreCase("file01")) {
            this.dealerAlterRequest.setFileTaxCertificate(fileName);     // 税务登记证 照片文件
        } else if (type.equalsIgnoreCase("file02")) {
            this.dealerAlterRequest.setFileBankAccountOpeningPermit(fileName);  // 银行开户行许可证
        } else if (type.equalsIgnoreCase("file03")) {
            this.dealerAlterRequest.setFilePersonnelCertificate(fileName);  // 人员登记证书
        } else if (type.equalsIgnoreCase("file04")) {
            this.dealerAlterRequest.setFileQualification(fileName);       // 维修资质表
        } else if (type.equalsIgnoreCase("file05")) {
            this.dealerAlterRequest.setFileInvoiceInfo(fileName);      // 服务站开票信息
        } else if (type.equalsIgnoreCase("file06")) {
            this.dealerAlterRequest.setFileRoadTransportLicense(fileName);   // 道路运输营业许可证
        } else if (type.equalsIgnoreCase("file07")) {
            this.dealerAlterRequest.setFileOrgChart(fileName);        // 企业组织架构及设置书
        } else if (type.equalsIgnoreCase("file08")) {
            this.dealerAlterRequest.setFileDevice(fileName);      // 设备清单
        } else if (type.equalsIgnoreCase("file09")) {
            this.dealerAlterRequest.setFileReceptionOffice(fileName);  // 接待室图片
        } else if (type.equalsIgnoreCase("file10")) {
            this.dealerAlterRequest.setFileMap(fileName);        // 地理位置
        } else if (type.equalsIgnoreCase("file11")) {
            this.dealerAlterRequest.setFileGlobal(fileName);         // 服务商全貌图片
        } else if (type.equalsIgnoreCase("file12")) {
            this.dealerAlterRequest.setFileOffice(fileName);          // 办公室图片
        } else if (type.equalsIgnoreCase("file13")) {
            this.dealerAlterRequest.setFilePartStoreage(fileName);// 配件库房图片
        } else if (type.equalsIgnoreCase("file14")) {
            this.dealerAlterRequest.setFileWorkshop(fileName);    // 维修车间
        } else if (type.equalsIgnoreCase("file16")) {
            this.dealerAlterRequest.setFileAlteration(fileName);    // 变更函
        }


//        Clients.showNotification(event.getMedia().getContentType());
        if (!event.getMedia().getContentType().startsWith("image/")) {
//            Clients.showNotification("Please upload an image");
            return;
        } else {
//            Clients.showNotification(photo.getName());
        }
//        this.photo = photo;
    }

    @Command
    @NotifyChange("dealerAlterRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("file15")) {  // 维修资质
            this.dealerAlterRequest.setFileBusinessLicense("");            // 营业执照 照片文件
        } else if (type.equalsIgnoreCase("file01")) {
            this.dealerAlterRequest.setFileTaxCertificate("");     // 税务登记证 照片文件
        } else if (type.equalsIgnoreCase("file02")) {
            this.dealerAlterRequest.setFileBankAccountOpeningPermit("");  // 银行开户行许可证
        } else if (type.equalsIgnoreCase("file03")) {
            this.dealerAlterRequest.setFilePersonnelCertificate("");  // 人员登记证书
        } else if (type.equalsIgnoreCase("file04")) {
            this.dealerAlterRequest.setFileQualification("");       // 维修资质表
        } else if (type.equalsIgnoreCase("file05")) {
            this.dealerAlterRequest.setFileInvoiceInfo("");      // 服务站开票信息
        } else if (type.equalsIgnoreCase("file06")) {
            this.dealerAlterRequest.setFileRoadTransportLicense("");   // 道路运输营业许可证
        } else if (type.equalsIgnoreCase("file07")) {
            this.dealerAlterRequest.setFileOrgChart("");        // 企业组织架构及设置书
        } else if (type.equalsIgnoreCase("file08")) {
            this.dealerAlterRequest.setFileDevice("");      // 设备清单
        } else if (type.equalsIgnoreCase("file09")) {
            this.dealerAlterRequest.setFileReceptionOffice("");  // 接待室图片
        } else if (type.equalsIgnoreCase("file10")) {
            this.dealerAlterRequest.setFileMap("");        // 地理位置
        } else if (type.equalsIgnoreCase("file11")) {
            this.dealerAlterRequest.setFileGlobal("");         // 服务商全貌图片
        } else if (type.equalsIgnoreCase("file12")) {
            this.dealerAlterRequest.setFileOffice("");          // 办公室图片
        } else if (type.equalsIgnoreCase("file13")) {
            this.dealerAlterRequest.setFilePartStoreage("");// 配件库房图片
        } else if (type.equalsIgnoreCase("file14")) {
            this.dealerAlterRequest.setFileWorkshop("");    // 维修车间
        } else if (type.equalsIgnoreCase("file16")) {
            this.dealerAlterRequest.setFileAlteration("");    // 变更函
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


    public List<DictionaryEntity> getOtherCollaborations() {
        return otherCollaborations;
    }

    public List<DictionaryEntity> getProductsOfMaintains() {

        return productsOfMaintains;
    }

    public List<String> getStrProductsOfMaintains() {

        return strProductsOfMaintains;
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

    public List<DictionaryEntity> getSelectedOtherCollaborations() {

        return selectedOtherCollaborations;
    }

    public void setSelectedOtherCollaborations(List<DictionaryEntity> selectedOtherCollaborations) {
        this.selectedOtherCollaborations = selectedOtherCollaborations;
    }


    public List<DictionaryEntity> getSelectedProductsOfMaintains() {

        return selectedProductsOfMaintains;
    }

    public void setSelectedProductsOfMaintains(List<DictionaryEntity> selectedProductsOfMaintains) {
        this.selectedProductsOfMaintains = selectedProductsOfMaintains;
    }

    public List<ProvinceEntity> getProvinceEntities() {
        return provinceEntities;
    }

    public void setProvinceEntities(List<ProvinceEntity> provinceEntities) {
        this.provinceEntities = provinceEntities;
    }

    public List<CityEntity> getCityEntities() {
        return cityEntities;
    }

    public void setCityEntities(List<CityEntity> cityEntities) {
        this.cityEntities = cityEntities;
    }

    public DealerAlterRequestEntity getDealerAlterRequest() {
        return dealerAlterRequest;
    }

    public void setDealerAlterRequest(DealerAlterRequestEntity dealerAlterRequest) {
        this.dealerAlterRequest = dealerAlterRequest;
    }

    public void setOtherCollaborations(List<DictionaryEntity> otherCollaborations) {
        this.otherCollaborations = otherCollaborations;
    }

    public void setProductsOfMaintains(List<DictionaryEntity> productsOfMaintains) {
        this.productsOfMaintains = productsOfMaintains;
    }
}
