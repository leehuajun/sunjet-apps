package com.sunjet.vm.asm;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.ActivityDistributionEntity;
import com.sunjet.model.asm.SupplyNoticeEntity;
import com.sunjet.model.asm.SupplyNoticeItemEntity;
import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.repository.asm.ActivityDistributionRepository;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.SupplyNoticeService;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.PartService;
import com.sunjet.service.flow.ProcessService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zsoup.helper.StringUtil;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

import static com.sunjet.utils.common.CommonHelper.FILTER_PARTS_ERROR;
import static com.sunjet.utils.common.CommonHelper.FILTER_PARTS_LEN;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 调拨通知  VM
 */
public class SupplyNoticeFormVM extends FlowFormBaseVM {
    @WireVariable
    private SupplyNoticeService supplyNoticeService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;

    @WireVariable
    private ActivityDistributionRepository activityDistributionRepository;

    @WireVariable
    private ProcessService processService;

    private SupplyNoticeEntity supplyNoticeRequest;
    private WarrantyMaintenanceEntity warrantyMaintenanceEntity;
    private VehicleEntity warrantyMaintenanceVehicle;
    private List<PartEntity> parts = new ArrayList<PartEntity>();
    private List<DealerEntity> dealerEntityList = new ArrayList<DealerEntity>();
    private String keyword = "";
    private SupplyNoticeItemEntity supplyNoticeItem;
    private Window window;
    private Map<String, Object> variables = new HashMap<>();


    @Init(superclass = true)
    public void init() {
        this.setBaseService(supplyNoticeService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {    // 有业务对象
            supplyNoticeRequest = supplyNoticeService.getSupplyNoticeByID(this.getBusinessId());
            if (supplyNoticeRequest.getSrcDocNo() != null & supplyNoticeRequest.getSrcDocNo() != "") {
                warrantyMaintenanceEntity = warrantyMaintenanceService.findOneWithOthersBySrcDocNo(supplyNoticeRequest.getSrcDocNo());
            }
            if (supplyNoticeRequest.getStatus() > 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }

            if (this.supplyNoticeRequest.getDealerCode() != null) {
                DealerEntity dealerEntity = dealerService.findDealerByCode(this.supplyNoticeRequest.getDealerCode());
                if (dealerEntity != null) {
                    this.supplyNoticeRequest.setDealerCode(dealerEntity.getCode());
                    this.supplyNoticeRequest.setDealerName(dealerEntity.getName());
                    this.supplyNoticeRequest.setProvinceName(dealerEntity.getProvince().getName());


                }
                //默认带入收货人联系电和收货地址为站长电话
                if (StringUtils.isBlank(this.supplyNoticeRequest.getReceive()) || StringUtils.isBlank(this.supplyNoticeRequest.getOperatorPhone())) {
                    this.supplyNoticeRequest.setReceive(dealerEntity.getStationMaster());
                    this.supplyNoticeRequest.setOperatorPhone(dealerEntity.getStationMasterPhone());
                    this.supplyNoticeRequest.setDealerAdderss(dealerEntity.getAddress());
                }


            }
        } else {        // 业务对象和业务对象Id都为空
            supplyNoticeRequest = new SupplyNoticeEntity();
            DealerEntity dealerEntity = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
            if (dealerEntity != null) {
                this.supplyNoticeRequest.setDealerCode(dealerEntity.getCode());
                this.supplyNoticeRequest.setDealerName(dealerEntity.getName());
                this.supplyNoticeRequest.setProvinceName(dealerEntity.getProvince().getName());
                this.supplyNoticeRequest.setDealerAdderss(dealerEntity.getAddress());
                //默认带入收货人联系电和收货地址为站长电话
                this.supplyNoticeRequest.setReceive(dealerEntity.getStationMaster());
                this.supplyNoticeRequest.setOperatorPhone(dealerEntity.getStationMasterPhone());
            }


        }
        if (!StringUtil.isBlank(supplyNoticeRequest.getSrcDocNo())) {
            warrantyMaintenanceVehicle = warrantyMaintenanceService.findOneVehicleByDocNo(supplyNoticeRequest.getSrcDocNo());
        }
        this.setReadonly(false);
        this.setEntity(supplyNoticeRequest);


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
//        this.setEntity(supplyNoticeRequest);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (this.supplyNoticeRequest.getStatus() > 0) {
            ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
            return;
        }
        if (this.supplyNoticeRequest.getDealerCode() == null || this.supplyNoticeRequest.getDealerCode() == "") {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        if (this.supplyNoticeRequest.getItems().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return;
        }
        //当主表非首次保存时，子表需要手工关联主表
        if (supplyNoticeRequest.getObjId() != null) {
            for (SupplyNoticeItemEntity Item : supplyNoticeRequest.getItems()) {
                Item.setSupplyNotice(supplyNoticeRequest);

            }
        }
        FlowDocEntity entity = this.saveEntity(this.supplyNoticeRequest);
        this.supplyNoticeRequest = supplyNoticeService.getSupplyNoticeByID(entity.getObjId());
        this.setEntity(this.supplyNoticeRequest);
//        this.setEntity(this.saveEntity(this.supplyNoticeRequest));
        showDialog();
    }

    @Override
    protected Boolean checkValid() {
        if (StringUtil.isBlank(this.supplyNoticeRequest.getDealerCode())) {
            ZkUtils.showInformation("请填写服务站", "提示");
            return false;
        }
        if (StringUtil.isBlank(this.supplyNoticeRequest.getReceive())) {
            ZkUtils.showInformation("请填写收货人", "提示");
            return false;
        }
        if (StringUtil.isBlank(this.supplyNoticeRequest.getDealerAdderss())) {
            ZkUtils.showInformation("请填写收货地址", "提示");
            return false;
        }
        if (StringUtil.isBlank(this.supplyNoticeRequest.getOperatorPhone())) {
            ZkUtils.showInformation("请填写联系电话", "提示");
            return false;
        }
        for (SupplyNoticeItemEntity supplyNoticeItem : this.supplyNoticeRequest.getItems()) {
            if (supplyNoticeItem.getRequestAmount().equals(0.0)) {

                ZkUtils.showInformation("配件数量小于0,不能提交", "提示");
                return false;
            } else if (supplyNoticeItem.getArrivalTime() == null) {
                ZkUtils.showError("请填写到货时间", "提示");
                return false;
            }

        }
        if (this.supplyNoticeRequest.getItems().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }


        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {

        if (this.getDealer() != null) {
            variables.put("level", this.getDealer().getLevel());
        } else {
            variables.put("level", null);
        }
//        variables.put("serviceManager",this.getDealer().getServiceManager().getObjId());
        if (this.getDealer() != null && this.getDealer().getParent() != null) {
            List<UserEntity> list = userService.findAllByDealerCode(this.getDealer().getParent().getCode());
            List<String> users = new ArrayList<>();
            for (UserEntity userEntity : list) {
                System.out.println(userEntity.getLogId());
                users.add(userEntity.getLogId());
            }
            variables.put("firstLevelUsers", users);
        }
//        variables.put("days", this.leaveBill.getDays());
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

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void addItemModel() {
        SupplyNoticeItemEntity supplyNoticeItemEntity = new SupplyNoticeItemEntity();
        supplyNoticeItemEntity.setArrivalTime(new Date());
//        supplyNoticeRequest.getItems().add(supplyNoticeItemEntity);
        supplyNoticeRequest.addNoticeItem(supplyNoticeItemEntity);

    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void importData(@BindingParam("model") org.zkoss.util.media.AMedia model) throws Exception {
//        String ORRPart="";
//        if(!model.getFormat().contains("xls"))
//        {
//            Messagebox.show("请选择xls类型EXECL文件!");
//            return;
//        }    // 第一步，创建一个webbook，对应一个Excel文件
//        if( supplyNoticeRequest.getItems()!=null)
//        supplyNoticeRequest.getItems().clear();
//        HSSFWorkbook workbook = new HSSFWorkbook(model.getStreamData());
//        HSSFSheet sheet = workbook.getSheetAt(0);
//        for(int j=1;j<sheet.getLastRowNum()+1;j++) {
//            HSSFRow row = sheet.getRow(j);
//            List<PartEntity> partEntities= partService.findAllByNo( row.getCell(0).toString());
//            if(partEntities.size()>0)
//            {
//                SupplyNoticeItemEntity partsEntity=new SupplyNoticeItemEntity();
//                partsEntity.setPart(partEntities.get(0));
//                partsEntity.setPartCode(partEntities.get(0).getCode());
//                partsEntity.setPartName(partEntities.get(0).getName());
//                partsEntity.setSupplyamount(Double.valueOf(row.getCell(1).toString()));
//                partsEntity.setTransportmodel(row.getCell(2).toString());
//                if(supplyNoticeRequest.getItems()!=null) {
//                    supplyNoticeRequest.getItems().add(partsEntity);
//                }
//                else
//                {List<SupplyNoticeItemEntity> entityList=new ArrayList<>();
//                    entityList.add(partsEntity);
//                    supplyNoticeRequest.setItems(entityList);}
//            }
//            else
//            { ORRPart=ORRPart+ row.getCell(0).toString()+"|";
//            }
//
//
//        }
//        if(!ORRPart.isEmpty())
//        Messagebox.show("料号"+ORRPart+"不存在，无法导入！");
    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void deleteItem(@BindingParam("model") SupplyNoticeItemEntity partsEntity) {
        for (SupplyNoticeItemEntity item : supplyNoticeRequest.getItems()) {
            if (item == partsEntity) {
                supplyNoticeRequest.getItems().remove(item);
                return;
            }
        }
    }

    @Command
    public void selectSupplyNoticePart(@BindingParam("model") SupplyNoticeItemEntity part) {
        this.supplyNoticeItem = part;
    }


    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("parts")
    public void searchParts() {
        if (this.keyword.trim().length() >= FILTER_PARTS_LEN) {
            this.parts = partService.findAllByKeyword(this.keyword.trim());
            if (this.parts.size() < 1) {
                ZkUtils.showInformation("未查询到相关物料！", "提示");
            }
        } else {
            ZkUtils.showInformation(FILTER_PARTS_ERROR, "提示");
            return;
        }


    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void selectPart(@BindingParam("model") PartEntity partsEntity) {
        supplyNoticeItem.setPartCode(partsEntity.getCode());
        supplyNoticeItem.setPartName(partsEntity.getName());
        supplyNoticeItem.setPart(partsEntity);
        this.parts.clear();
        this.keyword = "";

    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void clearSelectedDealer() {
        supplyNoticeRequest.setDealerCode("");
        supplyNoticeRequest.setDealerName("");
        supplyNoticeRequest.setProvinceName("");
    }

    @Command
    @NotifyChange("dealerEntityList")
    public void searchDealers(@BindingParam("model") String keyword) {
        this.dealerEntityList = dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword.trim());
    }

    @Command
    @NotifyChange("supplyNoticeRequest")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        supplyNoticeRequest.setDealerCode(dealer.getCode());
        supplyNoticeRequest.setDealerName(dealer.getName());
        supplyNoticeRequest.setProvinceName(dealer.getProvince().getName());
        //supplyNoticeRequest.setOperatorPhone(dealer.getPhone());
        supplyNoticeRequest.setDealerAdderss(dealer.getAddress());
        //supplyNoticeRequest.setReceive(dealer.getName());
        if (dealer.getServiceManager() != null)
            supplyNoticeRequest.setServiceManager(dealer.getServiceManager().getName());
        //默认带入收货人联系电和收货地址为站长电话
        this.supplyNoticeRequest.setReceive(dealer.getStationMaster());
        this.supplyNoticeRequest.setOperatorPhone(dealer.getStationMasterPhone());
    }


    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();

        map.put("docNo", this.supplyNoticeRequest.getDocNo() == null ? "" : this.supplyNoticeRequest.getDocNo());
        window = (Window) ZkUtils.createComponents("/views/report/asm/supplyNotice_report.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();

    }


    /**
     * 单据作废
     */
    @Override
    @Command
    @NotifyChange("*")
    public void desert() {
        if (this.supplyNoticeRequest.getSrcDocID() != null) {
            if ("三包服务单".equals(this.supplyNoticeRequest.getSrcDocType())) {
                WarrantyMaintenanceEntity warrantyMaintenanceEntity = warrantyMaintenanceService.findOneWithOthersBySrcDocNo(this.supplyNoticeRequest.getSrcDocNo());
                warrantyMaintenanceEntity.setSupplyNoticeId(null);
                warrantyMaintenanceEntity.setCanEditSupply(true);
                warrantyMaintenanceService.save(warrantyMaintenanceEntity);
            } else {
                ActivityDistributionEntity activityDistributionEntity = activityDistributionRepository.findOne(this.supplyNoticeRequest.getSrcDocID());
                activityDistributionEntity.setSupplyNoticeId(null);
                activityDistributionEntity.setCanEditSupply(true);
                activityDistributionRepository.save(activityDistributionEntity);
            }
        }
        this.supplyNoticeRequest.setStatus(DocStatus.OBSOLETE.getIndex());
        supplyNoticeService.save(this.supplyNoticeRequest);
        processService.deleteProcessInstance(this.supplyNoticeRequest.getProcessInstanceId());
        processService.deleteHistoricProcessInstance(this.supplyNoticeRequest.getProcessInstanceId());
        ZkUtils.showInformation("单据已作废", "提示");
        this.supplyNoticeRequest.setProcessInstanceId(null);
        this.supplyNoticeService.save(this.supplyNoticeRequest);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

    ///**
    // * 作废按钮
    // *
    // * @return
    // */
    //@Override
    //public boolean checkCanDesert() {
    //    if (this.supplyNoticeRequest.getStatus() == DocStatus.REJECT.getIndex()
    //            && CommonHelper.getActiveUser().getLogId().equals(supplyNoticeRequest.getSubmitter())) {
    //        return true;
    //    }
    //    return false;
    //}

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }

    public SupplyNoticeEntity getSupplyNoticeRequest() {
        return supplyNoticeRequest;
    }

    public void setSupplyNoticeRequest(SupplyNoticeEntity supplyNoticeRequest) {
        this.supplyNoticeRequest = supplyNoticeRequest;
    }

    public PartService getPartService() {
        return partService;
    }

    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    public List<PartEntity> getParts() {
        return parts;
    }

    public void setParts(List<PartEntity> parts) {
        this.parts = parts;
    }

    public List<DealerEntity> getDealerEntityList() {
        return dealerEntityList;
    }

    public void setDealerEntityList(List<DealerEntity> dealerEntityList) {
        this.dealerEntityList = dealerEntityList;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SupplyNoticeItemEntity getSupplyNoticeItem() {
        return supplyNoticeItem;
    }

    public void setSupplyNoticeItem(SupplyNoticeItemEntity supplyNoticeItem) {
        this.supplyNoticeItem = supplyNoticeItem;
    }

    public VehicleEntity getWarrantyMaintenanceVehicle() {
        return warrantyMaintenanceVehicle;
    }

    public void setWarrantyMaintenanceVehicle(VehicleEntity warrantyMaintenanceVehicle) {
        this.warrantyMaintenanceVehicle = warrantyMaintenanceVehicle;
    }

    public WarrantyMaintenanceEntity getWarrantyMaintenanceEntity() {
        return warrantyMaintenanceEntity;
    }

    public void setWarrantyMaintenanceEntity(WarrantyMaintenanceEntity warrantyMaintenanceEntity) {
        this.warrantyMaintenanceEntity = warrantyMaintenanceEntity;
    }


}
