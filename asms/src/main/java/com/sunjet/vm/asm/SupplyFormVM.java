package com.sunjet.vm.asm;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.SupplyEntity;
import com.sunjet.model.asm.SupplyItemEntity;
import com.sunjet.model.asm.SupplyWaitingItemEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.repository.asm.SupplyItemRepository;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.SupplyService;
import com.sunjet.service.asm.SupplyWaitingItemService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.PartService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang.StringUtils;
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

import java.io.IOException;
import java.util.*;

import static com.sunjet.utils.common.CommonHelper.FILTER_PARTS_ERROR;
import static com.sunjet.utils.common.CommonHelper.FILTER_PARTS_LEN;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 配件供货单 表单 VM
 */
public class SupplyFormVM extends FlowFormBaseVM {
    @WireVariable
    private SupplyService supplyService;
    @WireVariable
    private SupplyItemRepository supplyItemRepository;
    @WireVariable
    private SupplyWaitingItemService supplyWaitingItemService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private AgencyService agencyService;
    private SupplyEntity supplyRequest = new SupplyEntity();
    private List<PartEntity> parts = new ArrayList<>();
    private Set<DealerEntity> dealerEntityList = new HashSet<DealerEntity>();
    private Set<AgencyEntity> agencyEntityList = new HashSet<AgencyEntity>();
    private String keyword = "";
    private SupplyItemEntity supplyItem;
    private Window window;
    private Map<String, Object> variables = new HashMap<>();

    @Init(superclass = true)
    public void init() {
        this.setBaseService(supplyService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.supplyRequest = supplyService.findSupplyWithPartsById(this.getBusinessId());
            if (this.supplyRequest.getStatus() != 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }

        } else {        // 业务对象和业务对象Id都为空
            supplyRequest.setSupplyDate(new Date());
            AgencyEntity agency = userService.findOne(CommonHelper.getActiveUser().getUserId()).getAgency();
            if (agency != null) {
                this.supplyRequest.setAgencyCode(agency.getCode());
                this.supplyRequest.setAgencyName(agency.getName());
                this.supplyRequest.setAgencyAddress(agency.getAddress());
            }
            this.setReadonly(false);
            //从待供货列表传数据
            List<SupplyWaitingItemEntity> supplyWaitingItems = (List<SupplyWaitingItemEntity>) Executions.getCurrent().getArg().get("supplyWaitingItems");
            if (supplyWaitingItems != null) {
                this.setReadonly(true);
                List<SupplyItemEntity> supplyItemEntities = new ArrayList<>();
                this.supplyRequest.getItems().clear();
                double expenseTotal = 0;
                for (SupplyWaitingItemEntity item : supplyWaitingItems) {
                    SupplyItemEntity supplyItem = new SupplyItemEntity();
                    supplyItem.setPartCode(item.getPartCode());
                    supplyItem.setPartName(item.getPartName());
                    supplyItem.setAmount(item.getSurplusAmount());
                    supplyItem.setSupplyWaitingItem(item);
                    supplyItem.setSupplyNoticeItem(item.getSupplyNoticeItem());

                    List<PartEntity> partEntities = partService.findAllByCode(item.getPartCode());
                    if (partEntities.size() > 0) {
                        supplyItem.setPart(partEntities.get(0));
                        supplyItem.setPrice(partEntities.get(0).getPrice());
                        supplyItem.setMoney(supplyItem.getAmount() * supplyItem.getPrice());
                    }
                    expenseTotal = expenseTotal + supplyItem.getMoney();
                    this.supplyRequest.setDealerCode(item.getSupplyNoticeItem().getSupplyNotice().getDealerCode());
                    this.supplyRequest.setDealerName(item.getSupplyNoticeItem().getSupplyNotice().getDealerName());
                    this.supplyRequest.setDealerAdderss(item.getSupplyNoticeItem().getSupplyNotice().getDealerAdderss());
                    this.supplyRequest.setReceive(item.getSupplyNoticeItem().getSupplyNotice().getReceive());
                    this.supplyRequest.setOperatorPhone(item.getSupplyNoticeItem().getSupplyNotice().getOperatorPhone());

                    this.supplyRequest.setAgencyName(item.getAgencyName());
                    this.supplyRequest.setAgencyCode(item.getAgencyCode());

                    supplyItemEntities.add(supplyItem);
                }
                supplyRequest.setExpenseTotal(expenseTotal);
                supplyRequest.setPartExpense(expenseTotal);
                supplyRequest.setItems(supplyItemEntities);

            }

        }
        //supplyRequest.setRcvDate(null);
        this.setEntity(supplyRequest);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
//        this.setEntity(supplyRequest);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (!StringUtils.isNotBlank(this.supplyRequest.getAgencyCode())) {
            ZkUtils.showInformation("请选择经销商！", "提示");
            return;
        }
        if (!StringUtils.isNotBlank(this.supplyRequest.getDealerCode())) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }
        changeCount();
        showDialog();
    }

    @Override
    protected Boolean checkValid() {
        //if (this.supplyRequest.getStatus() > 0) {
        //    ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
        //    return false;
        //}
        if (this.supplyRequest.getDealerCode() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (this.supplyRequest.getItems().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }
        if (this.supplyRequest.getLogistics() == null && StringUtils.isBlank(this.supplyRequest.getLogistics())) {
            ZkUtils.showInformation("请填写物流名称！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.supplyRequest.getSubmitterPhone())) {
            ZkUtils.showInformation("请填写联系电话！", "提示");
            return false;
        }

        if (StringUtils.isBlank(this.supplyRequest.getLogisticsNum())) {
            ZkUtils.showInformation("请填写物流单号！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.supplyRequest.getTransportmodel())) {
            ZkUtils.showInformation("请填写运输方式！", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.supplyRequest.getDealerAdderss())) {
            ZkUtils.showInformation("请填写收货地址！", "提示");
            return false;
        }
//        if(this.supplyRequest.getTransportExpense()==0){
//            ZkUtils.showInformation("请填写运输费用！", "提示");
//            return false;
//        }
        if (this.supplyRequest.getLogisticsfile() == null && StringUtils.isBlank(this.supplyRequest.getLogisticsfile())) {
            ZkUtils.showInformation("请上传物流附件！", "提示");
            return false;
        }
        for (SupplyItemEntity supplyItemEntity : this.supplyRequest.getItems()) {
            if (supplyItemEntity.getLogisticsNum() == null || StringUtils.isBlank(supplyItemEntity.getLogisticsNum())) {
                ZkUtils.showInformation("请填写物流单号！", "提示");
                return false;
            }
            if (StringUtils.isBlank(supplyItemEntity.getPartCode())) {
                ZkUtils.showInformation("请选择配件！", "提示");
                return false;
            }
            if (supplyItemEntity.getAmount() == 0) {
                ZkUtils.showInformation("发货数量为0不能提交！", "提示");
                return false;
            }
        }
        return true;
    }

    public void changeCount() {
        if (supplyRequest.getObjId() != null) {
            //当主表非首次保存时，子表需要手工关联主表
            for (SupplyItemEntity Item : supplyRequest.getItems()) {
                Item.setSupply(supplyRequest);
            }

            List<SupplyItemEntity> supplyItemEntities = supplyItemRepository.findSupplyItemsByDocID(supplyRequest.getObjId());
            for (SupplyItemEntity supplyItem : supplyItemEntities) {
                SupplyWaitingItemEntity supplyWaitingItem = supplyItem.getSupplyWaitingItem();
                if (supplyWaitingItem != null) {
                    supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + supplyItem.getAmount());
                    supplyWaitingItem.setSentAmount(supplyWaitingItem.getRequestAmount() - supplyWaitingItem.getSurplusAmount());
                    supplyWaitingItem.setModifiedTime(new Date());
                    supplyWaitingItem.setModifierName(CommonHelper.getActiveUser().getUsername());
                    supplyWaitingItem.setModifierId(CommonHelper.getActiveUser().getUserId());
                    supplyWaitingItemService.getRepository().save(supplyWaitingItem);
                }

                //}
            }
        }
        FlowDocEntity entity = this.saveEntity(this.supplyRequest);
        this.supplyRequest = supplyService.findSupplyWithPartsById(entity.getObjId());
        this.setEntity(this.supplyRequest);
//        this.setEntity(this.saveEntity(this.supplyRequest));
        for (SupplyItemEntity item : this.supplyRequest.getItems()) {
            if (item.getSupplyWaitingItem() != null) {
                SupplyWaitingItemEntity waitingItem = item.getSupplyWaitingItem();
                waitingItem.setSurplusAmount(waitingItem.getSurplusAmount() - item.getAmount());
                waitingItem.setSentAmount(waitingItem.getRequestAmount() - waitingItem.getSurplusAmount());
                waitingItem.setModifiedTime(new Date());
                waitingItem.setModifierName(CommonHelper.getActiveUser().getUsername());
                waitingItem.setModifierId(CommonHelper.getActiveUser().getUserId());
                supplyWaitingItemService.getRepository().save(waitingItem);
            }
        }
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
        if (!org.apache.commons.lang3.StringUtils.isNotEmpty(this.getEntity().getObjId())) {
            ZkUtils.showExclamation("请先保存数据再提交！", "系统提示");
            return;
        }
//        Map<String, Object> variables = new HashMap<>();

//        if(this.getDealer()==null){
//            this.setDealer(dealerService.findDealerByCode(this.supplyRequest.getDealerCode()));
//        }
//
//        variables.put("level",this.getDealer().getLevel());
//
//        if(this.getDealer().getParent()!=null){
//            List<UserEntity> list = userService.findAllByDealerCode(this.getDealer().getParent().getCode());
//            variables.put("firstLevelUsers",list.toString().replace("[","").replace("]",""));
//        }

        changeCount();

        List<UserEntity> list = userService.findAllByDealerCode(this.supplyRequest.getDealerCode());
        System.out.println(list.size());
        List<String> users = new ArrayList<>();
        for (UserEntity userEntity : list) {
            System.out.println(userEntity.getLogId());
            users.add(userEntity.getLogId());
        }

        variables.put("dealerUsers", users);
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

    //确认收货控制
    @Override
    protected void completeTask(String outcome, String comment) throws IOException {
        if (CommonHelper.getActiveUser().getDealer() != null && "收货".equals(outcome) && this.supplyRequest.getRcvDate() == null) {
            ZkUtils.showInformation("请选择收货日期", "提示");
            return;
        }

        super.completeTask(outcome, comment);
    }

    @Command
    @NotifyChange("supplyRequest")
    public void addItemModel() {
        SupplyItemEntity supplyItemEntity = new SupplyItemEntity();
        supplyItemEntity.setLogisticsNum(supplyRequest.getLogisticsNum());
        supplyRequest.getItems().add(supplyItemEntity);
    }

    @Command
    @NotifyChange("supplyRequest")
    public void changePartAmount() {
        double totle = 0;
        for (SupplyItemEntity part : supplyRequest.getItems()) {
            if (part.getPartCode() != null) {
                if (part.getPart().getPrice() != null && part.getAmount() != null)
                    part.setMoney(part.getAmount() * part.getPart().getPrice());
                totle = totle + part.getAmount() * part.getPart().getPrice();
            }
        }
        if (this.supplyRequest.getOtherExpense() == null) {
            this.supplyRequest.setOtherExpense(0.0);
        }
        if (this.supplyRequest.getTransportExpense() == null) {
            this.supplyRequest.setTransportExpense(0.0);
        }
        supplyRequest.setPartExpense(totle);
        totle = totle + supplyRequest.getOtherExpense() + supplyRequest.getTransportExpense();
        supplyRequest.setExpenseTotal(totle);
    }

    @Command
    @NotifyChange("supplyRequest")
    public void importData(@BindingParam("model") org.zkoss.util.media.AMedia model) throws Exception {
//        String ORRPart="";
//        if(!model.getFormat().contains("xls"))
//        {
//            Messagebox.show("请选择xls类型EXECL文件!");
//            return;
//        }    // 第一步，创建一个webbook，对应一个Excel文件
//        supplyRequest.getItems().clear();
//        HSSFWorkbook workbook = new HSSFWorkbook(model.getStreamData());
//        HSSFSheet sheet = workbook.getSheetAt(0);
//        for(int j=1;j<sheet.getLastRowNum()+1;j++) {
//            HSSFRow row = sheet.getRow(j);
//            List<PartEntity> partEntities= partService.findAllByNo( row.getCell(0).toString());
//            if(partEntities.size()>0)
//            {SupplyItemEntity supplyPartList=new SupplyItemEntity();
//                supplyPartList.setPart(partEntities.get(0));
//                supplyPartList.setPartCode(partEntities.get(0).getCode());
//                supplyPartList.setPartName(partEntities.get(0).getName());
//                supplyPartList.setAmount(Double.valueOf(row.getCell(1).toString()));
//                supplyPartList.setLogisticsNum(row.getCell(2).toString());
//                supplyRequest.getItems().add(supplyPartList);
//            }
//            else
//            { ORRPart=ORRPart+ row.getCell(0).toString()+"|";
//            }
//
//
//        }
//        if(!ORRPart.isEmpty())
//            Messagebox.show("料号"+ORRPart+"不存在，无法导入！");
//
//

    }

    @Command
    @NotifyChange("supplyRequest")
    public void deleteItem(@BindingParam("model") SupplyItemEntity model) {
        try {
            for (SupplyItemEntity item : supplyRequest.getItems()) {
                if (item == model) {
                    changePartAmount();
                    supplyRequest.getItems().remove(item);
                    return;
                }
            }
        } catch (Exception ex) {
            ZkUtils.showInformation(ex.getMessage(), "异常提示");
        }
    }

    @Command
    public void selectSupplyNoticePart(@BindingParam("model") SupplyItemEntity part) {
        this.supplyItem = part;
    }


    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("parts")
    public void searchParts() {

        if (this.keyword.length() >= FILTER_PARTS_LEN) {
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
    @NotifyChange("supplyRequest")
    public void selectPart(@BindingParam("model") PartEntity partsEntity) {
        supplyItem.setPartCode(partsEntity.getCode());
        supplyItem.setPartName(partsEntity.getName());
        supplyItem.setPrice(partsEntity.getPrice());
        supplyItem.setPart(partsEntity);
        this.keyword = "";
        this.parts.clear();
        changePartAmount();
    }

    @Command
    public void selectSupplyPart(@BindingParam("model") SupplyItemEntity part) {
        this.supplyItem = part;
    }

    @Command
    @NotifyChange("supplyRequest")
    public void clearSelectedDealer() {
        supplyRequest.setDealerCode("");
        supplyRequest.setDealerName("");

    }

    @Command
    @NotifyChange("dealerEntityList")
    public void searchDealers(@BindingParam("model") String keyword) {
        this.dealerEntityList.clear();
        this.dealerEntityList.addAll(dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword.trim()));
    }

    @Command
    @NotifyChange("supplyRequest")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.setDealer(dealer);
        supplyRequest.setDealerCode(dealer.getCode());
        supplyRequest.setDealerName(dealer.getName());
        supplyRequest.setDealerAdderss(dealer.getAddress());
        supplyRequest.setOperatorPhone(dealer.getPhone());
        supplyRequest.setReceive(dealer.getStationMaster());
        supplyRequest.setOperatorPhone(dealer.getStationMasterPhone());
    }

    @Command
    @NotifyChange("supplyRequest")
    public void clearSelectedAgency() {
        supplyRequest.setAgencyName("");
        supplyRequest.setAgencyCode("");

    }

    @Command
    @NotifyChange("agencyEntityList")
    public void searchAgencys(@BindingParam("model") String keyword) {
        this.agencyEntityList.clear();
        this.agencyEntityList.addAll(agencyService.findAllByKeyword(keyword.trim()));
    }

    @Command
    @NotifyChange("supplyRequest")
    public void selectAgency(@BindingParam("model") AgencyEntity agency) {
        supplyRequest.setAgencyCode(agency.getCode());
        supplyRequest.setAgencyName(agency.getName());
        supplyRequest.setAgencyAddress(agency.getAddress());
        supplyRequest.setAgencyPhone(agency.getPhone());

    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
        supplyRequest.setLogisticsfilename(event.getMedia().getName());
        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_ASM);
        supplyRequest.setLogisticsfile(fileName);

    }

    @Command
    @NotifyChange("*")
    public void delUploadFile() {
        supplyRequest.setLogisticsfile("");

    }

    @Command
    @NotifyChange("*")
    public void changeLogisticsNum() {
        for (SupplyItemEntity supplyItemEntity : supplyRequest.getItems()) {
            supplyItemEntity.setLogisticsNum(supplyRequest.getLogisticsNum());
        }
    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "report");
        map.put("objId", this.supplyRequest.getObjId() == null ? "" : this.supplyRequest.getObjId());
        map.put("dealerName", this.supplyRequest.getDealerName() == null ? "" : this.supplyRequest.getDealerName());
        map.put("dealerCode", this.supplyRequest.getDealerCode() == null ? "" : this.supplyRequest.getDealerCode());
        map.put("docNo", this.supplyRequest.getDocNo() == null ? "" : this.supplyRequest.getDocNo());
        map.put("dealerAdderss", this.supplyRequest.getDealerAdderss() == null ? "" : this.supplyRequest.getDealerAdderss());
        map.put("receive", this.supplyRequest.getReceive() == null ? "" : this.supplyRequest.getReceive());
        map.put("operatorPhone", this.supplyRequest.getOperatorPhone() == null ? "" : this.supplyRequest.getOperatorPhone());
        map.put("agencyName", this.supplyRequest.getAgencyName() == null ? "" : this.supplyRequest.getAgencyName());
        map.put("agencyCode", this.supplyRequest.getAgencyCode() == null ? "" : this.supplyRequest.getAgencyCode());
        map.put("submitterName", this.supplyRequest.getSubmitterName() == null ? "" : this.supplyRequest.getSubmitterName());
        window = (Window) ZkUtils.createComponents("/views/report/asm/supply_report.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }

    @Command
    public void expressPrintReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "express");
        map.put("objId", this.supplyRequest.getObjId() == null ? "" : this.supplyRequest.getObjId());
        map.put("dealerName", this.supplyRequest.getDealerName() == null ? "" : this.supplyRequest.getDealerName());
        map.put("dealerAdderss", this.supplyRequest.getDealerAdderss() == null ? "" : this.supplyRequest.getDealerAdderss());
        map.put("operatorPhone", this.supplyRequest.getOperatorPhone() == null ? "" : this.supplyRequest.getOperatorPhone());
        map.put("agencyName", this.supplyRequest.getAgencyName() == null ? "" : this.supplyRequest.getAgencyName());
        map.put("agencyAddress", this.supplyRequest.getAgencyAddress() == null ? "" : this.supplyRequest.getAgencyAddress());
        map.put("agencyPhone", this.supplyRequest.getSubmitterPhone() == null ? "" : this.supplyRequest.getSubmitterPhone());
        map.put("receive", this.supplyRequest.getReceive() == null ? "" : this.supplyRequest.getReceive());
        map.put("submitterName", this.supplyRequest.getSubmitterName() == null ? "" : this.supplyRequest.getSubmitterName());
        window = (Window) ZkUtils.createComponents("/views/report/asm/supply_report.zul", null, map);
        window.setTitle("快递单打印");
        window.doModal();
    }

    @Command
    @Override
    @NotifyChange("*")
    public void desert() {
        super.desert();

        SupplyEntity supplyEntity = supplyService.findSupplyWithPartsById(this.supplyRequest.getObjId());
        for (SupplyItemEntity item : supplyEntity.getItems()) {
            SupplyWaitingItemEntity supplyWaitingItem = item.getSupplyWaitingItem();
            if (supplyWaitingItem != null) {
                supplyWaitingItem.setSurplusAmount(supplyWaitingItem.getSurplusAmount() + item.getAmount());
                supplyWaitingItem.setSentAmount(supplyWaitingItem.getRequestAmount() - supplyWaitingItem.getSurplusAmount());
                supplyWaitingItem.setModifiedTime(new Date());
                supplyWaitingItem.setModifierName(CommonHelper.getActiveUser().getUsername());
                supplyWaitingItem.setModifierId(CommonHelper.getActiveUser().getUserId());
                supplyWaitingItemService.getRepository().save(supplyWaitingItem);
            }
        }
        //清空供货单与调拨通知单关联
        for (SupplyItemEntity supplyItemEntity : supplyEntity.getItems()) {
            supplyItemEntity.setSupplyNoticeItem(null);
        }
        supplyService.save(supplyEntity);
        //supplyItemRepository.delete(supplyEntity.getItems());
        //supplyService.deleteEntity(supplyEntity);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_WAITING, null);

    }

    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }

    @Override
    public boolean checkCanExpressPrintReport() {
        return true;
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_ASM + filename;
    }


    public SupplyEntity getSupplyRequest() {
        return supplyRequest;
    }

    public void setSupplyRequest(SupplyEntity supplyRequest) {
        this.supplyRequest = supplyRequest;
    }

    public List<PartEntity> getParts() {
        return parts;
    }

    public void setParts(List<PartEntity> parts) {
        this.parts = parts;
    }

    public Set<DealerEntity> getDealerEntityList() {
        return dealerEntityList;
    }

    public void setDealerEntityList(Set<DealerEntity> dealerEntityList) {
        this.dealerEntityList = dealerEntityList;
    }

    public Set<AgencyEntity> getAgencyEntityList() {
        return agencyEntityList;
    }

    public void setAgencyEntityList(Set<AgencyEntity> agencyEntityList) {
        this.agencyEntityList = agencyEntityList;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SupplyItemEntity getSupplyItem() {
        return supplyItem;
    }

    public void setSupplyItem(SupplyItemEntity supplyItem) {
        this.supplyItem = supplyItem;
    }
}
