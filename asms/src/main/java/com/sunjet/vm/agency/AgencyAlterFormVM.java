package com.sunjet.vm.agency;

import com.sunjet.model.agency.AgencyAlterRequestEntity;
import com.sunjet.service.agency.AgencyAlterService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.utils.common.BeanHelper;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

/**
 * Created by Administrator on 2016/9/5.
 */
public class AgencyAlterFormVM extends FlowFormBaseVM {


    @WireVariable
    private AgencyAlterService agencyAlterService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private AgencyService agencyService;

    private AgencyAlterRequestEntity agencyAlterRequest; //接收列表model的服务站变更对象

    public AgencyAlterRequestEntity getAgencyAlterRequest() {
        return agencyAlterRequest;
    }

    public void setAgencyAlterRequest(AgencyAlterRequestEntity agencyAlterRequest) {
        this.agencyAlterRequest = agencyAlterRequest;
    }

    @Init(superclass = true)
    public void init() {
        this.setBaseService(agencyAlterService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.agencyAlterRequest = agencyAlterService.findOneById(this.getBusinessId());
            this.setAgency(this.agencyAlterRequest.getAgency());
        } else {
            this.agencyAlterRequest = new AgencyAlterRequestEntity();
            this.agencyAlterRequest.setAgency(this.getAgency());
        }
        this.setEntity(this.agencyAlterRequest);
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
        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());

//        this.agencyAlterRequest.setAgency(this.getAgency());
//        BeanUtils.copyProperties(this.agencyAlterRequest, this.getAgency(), BeanHelper.getNullPropertyNames(this.agencyAlterRequest));
//        this.setEntity(this.agencyAlterRequest);
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
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
//        AgencyEntity agency = agencyAlterRequest.getAgency();
        // 赋值
//        agencyAlterRequest.setAgency(this.getAgency());
//        agencyService.saveAgency(agency);
//        agencyAlterService.save(agencyAlterRequest);
        if (this.getAgency() == null) {
            ZkUtils.showInformation("请选合作商再保存", "提示");
            return;
        }
        this.agencyAlterRequest.setAgency(this.getAgency());
        BeanUtils.copyProperties(this.agencyAlterRequest, this.getAgency(), BeanHelper.getNullPropertyNames(this.agencyAlterRequest));
        this.agencyAlterRequest = (AgencyAlterRequestEntity) this.saveEntity(this.agencyAlterRequest);
        this.setEntity(this.agencyAlterRequest);
        showDialog();
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

//    @Command
//    @NotifyChange("*")
//    public void updateAgency() {
//        AgencyEntity agency = agencyAlterRequest.getAgency();
//        // 赋值
//        BeanUtils.copyProperties(this.agencyAlterRequest, agency, BeanHelper.getNullPropertyNames(this.agencyAlterRequest));
//        agencyService.saveAgency(agency);
//    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
//        logger.info(CommonHelper.UPLOAD_PATH_AGENCY);

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_AGENCY);
//        this.agencyAlterRequest.setFileQualification(fileName);

        if (type.equalsIgnoreCase("t00")) {  // 维修资质
            this.agencyAlterRequest.setFileAlteration(fileName);  // 变更函
        } else if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAlterRequest.setFileQualification(fileName);
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAlterRequest.setFileOrgChart(fileName);
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAlterRequest.setFileBusinessLicense(fileName);
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAlterRequest.setFileGlobal(fileName);
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAlterRequest.setFileTaxCertificate(fileName);
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAlterRequest.setFileOffice(fileName);
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAlterRequest.setFileBankAccountOpeningPermit(fileName);
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAlterRequest.setFileReceptionOffice(fileName);
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAlterRequest.setFileInvoiceInfo(fileName);
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAlterRequest.setFilePartStoreage(fileName);
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAlterRequest.setFilePersonnelCertificate(fileName);
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAlterRequest.setFileMap(fileName);
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAlterRequest.setFileShelf(fileName);
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAlterRequest.setFileComputer(fileName);
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAlterRequest.setFileContainer(fileName);
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAlterRequest.setFileTelephone(fileName);
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAlterRequest.setFileLadderTruck(fileName);
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAlterRequest.setFileFax(fileName);
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAlterRequest.setFileForkTruck(fileName);
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAlterRequest.setFileCamera(fileName);
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAlterRequest.setFileLittleContainer(fileName);
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAlterRequest.setFilePacker(fileName);
        }
    }

    @Command
    @NotifyChange("agencyAlterRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("t00")) {  // 维修资质
            this.agencyAlterRequest.setFileAlteration("");  // 变更函
        } else if (type.equalsIgnoreCase("t01")) {  // 维修资质
            this.agencyAlterRequest.setFileQualification("");
        } else if (type.equalsIgnoreCase("t02")) {   //企业组织架构及设置书
            this.agencyAlterRequest.setFileOrgChart("");
        } else if (type.equalsIgnoreCase("t03")) {   //营业执照
            this.agencyAlterRequest.setFileBusinessLicense("");
        } else if (type.equalsIgnoreCase("t04")) {   //合作商全貌图片
            this.agencyAlterRequest.setFileGlobal("");
        } else if (type.equalsIgnoreCase("t05")) {   //税务登记证
            this.agencyAlterRequest.setFileTaxCertificate("");
        } else if (type.equalsIgnoreCase("t06")) {    //办公区图片
            this.agencyAlterRequest.setFileOffice("");
        } else if (type.equalsIgnoreCase("t07")) {    //银行开户行许可证
            this.agencyAlterRequest.setFileBankAccountOpeningPermit("");
        } else if (type.equalsIgnoreCase("t08")) {    //接待室图片
            this.agencyAlterRequest.setFileReceptionOffice("");
        } else if (type.equalsIgnoreCase("t09")) {    //合作库开票信息
            this.agencyAlterRequest.setFileInvoiceInfo("");
        } else if (type.equalsIgnoreCase("t10")) {    //配件库房图片
            this.agencyAlterRequest.setFilePartStoreage("");
        } else if (type.equalsIgnoreCase("t11")) {    //人员登记证书
            this.agencyAlterRequest.setFilePersonnelCertificate("");
        } else if (type.equalsIgnoreCase("t12")) {    //地图位置
            this.agencyAlterRequest.setFileMap("");
        } else if (type.equalsIgnoreCase("t13")) {      //标准货架
            this.agencyAlterRequest.setFileShelf("");
        } else if (type.equalsIgnoreCase("t14")) {     //电脑(有网络)
            this.agencyAlterRequest.setFileComputer("");
        } else if (type.equalsIgnoreCase("t15")) {     //定制货柜
            this.agencyAlterRequest.setFileContainer("");
        } else if (type.equalsIgnoreCase("t16")) {     //电话
            this.agencyAlterRequest.setFileTelephone("");
        } else if (type.equalsIgnoreCase("t17")) {     //登高车
            this.agencyAlterRequest.setFileLadderTruck("");
        } else if (type.equalsIgnoreCase("t18")) {     //传真
            this.agencyAlterRequest.setFileFax("");
        } else if (type.equalsIgnoreCase("t19")) {     //推高车
            this.agencyAlterRequest.setFileForkTruck("");
        } else if (type.equalsIgnoreCase("t20")) {     //数码相机
            this.agencyAlterRequest.setFileCamera("");
        } else if (type.equalsIgnoreCase("t21")) {     //小件容器
            this.agencyAlterRequest.setFileLittleContainer("");
        } else if (type.equalsIgnoreCase("t22")) {     //手动打包机
            this.agencyAlterRequest.setFilePacker("");
        }
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_AGENCY + filename;
    }
}
