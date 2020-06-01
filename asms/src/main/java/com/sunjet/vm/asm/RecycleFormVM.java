package com.sunjet.vm.asm;

import com.sunjet.model.asm.RecycleEntity;
import com.sunjet.model.asm.RecycleItemEntity;
import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.repository.asm.RecycleItemRepository;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.RecycleNoticeItemService;
import com.sunjet.service.asm.RecycleService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 故障件返回单 表单 VM
 */
public class RecycleFormVM extends FlowFormBaseVM {
    @WireVariable
    private RecycleService recycleService;

    @WireVariable
    private PartService partService;
    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;
    @WireVariable
    private UserService userService;
    private RecycleEntity recycleRequest = new RecycleEntity();
    @WireVariable
    private RecycleItemRepository recycleItemRepository;
    private Window window;

    private Double expenseTotal;


    @Init(superclass = true)
    public void init() {
        this.setBaseService(recycleService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.recycleRequest = recycleService.findRecycleListById(this.getBusinessId());
            this.setReadonly(true);
            this.setDealer(dealerService.findDealerByCode(this.recycleRequest.getDealerCode()));
        } else {        // 业务对象和业务对象Id都为空
            recycleRequest = new RecycleEntity();
            updateSelectedRecycleNoticeList();
            DealerEntity dealerEntity = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
            this.setReadonly(false);
            if (dealerEntity != null) {
                this.recycleRequest.setDealerCode(dealerEntity.getCode());
                this.recycleRequest.setDealerName(dealerEntity.getName());
                this.recycleRequest.setProvinceName(dealerEntity.getProvince().getName());
                this.recycleRequest.setServiceManager(dealerEntity.getServiceManager().getName());
            }

        }
        this.setEntity(recycleRequest);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;

    }

    @Command
    @NotifyChange("*")
    public void changeLogisticsNum() {
        for (RecycleItemEntity item : this.recycleRequest.getItems()) {
            item.setLogisticsNum(this.recycleRequest.getLogisticsNum());
        }
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        for (RecycleItemEntity item : recycleRequest.getItems()) {
            if (item.getBackAmount() > item.getWaitAmount()) {
                ZkUtils.showInformation("返回数量不能大于应返回数量", "错误");
                return;
            } else if (item.getBackAmount() <= 0) {
                ZkUtils.showInformation("返回数量不能小于0", "错误");
                return;
            }
        }


        changeCount();
        showDialog();


    }

    private void changeCount() {

        // 如果objid为空，表示新增
        if (StringUtils.isBlank(this.getEntity().getObjId())) {
            for (RecycleItemEntity item : ((RecycleEntity) this.getEntity()).getItems()) {
                RecycleNoticeItemEntity noticeItem = item.getRecycleNoticeItem();
                noticeItem.setBackAmount(noticeItem.getBackAmount() + item.getBackAmount());
                noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());
                recycleNoticeItemService.getRepository().save(noticeItem);
            }
            this.saveEntity(this.recycleRequest);
            this.recycleRequest = recycleService.findOneWithItems(recycleRequest.getObjId());    // 旧单据
            this.setEntity(this.recycleRequest);
        } else {  // 已存在，修改
            RecycleEntity recycleEntity = recycleService.findOneWithItems(this.getEntity().getObjId());    // 旧单据
            List<RecycleItemEntity> addList = new ArrayList<>();    // 新增的
            List<RecycleItemEntity> removeList = new ArrayList<>(); // 删除的
            if (recycleEntity.getItems().size() > 0) {
                for (RecycleItemEntity oldItem : recycleEntity.getItems()) {    //  循环旧单据,删除旧单据子项，并复原通知单
                    Boolean exists = false; // 判断旧对象在新列表中是否存在
                    for (RecycleItemEntity item : this.recycleRequest.getItems()) {  // 循环新单据
                        if (StringUtils.isBlank(item.getObjId())) {
                            addList.add(item);  // 新增
                            RecycleNoticeItemEntity noticeItem = (RecycleNoticeItemEntity) recycleNoticeItemService.getRepository().findOne(item.getRecycleNoticeItem().getObjId());
                            if (noticeItem != null) {
                                noticeItem.setBackAmount(noticeItem.getBackAmount() + item.getBackAmount());       // 已返回数量
                                noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());   // 待返回数量
                                recycleNoticeItemService.getRepository().save(noticeItem);

                            }
                        } else if (item.getObjId().equals(oldItem.getObjId())) {
                            // 存在
                            exists = true;
                            RecycleNoticeItemEntity noticeItem = (RecycleNoticeItemEntity) recycleNoticeItemService.getRepository().findOne(item.getRecycleNoticeItem().getObjId());
                            if (noticeItem != null) {
                                noticeItem.setBackAmount(noticeItem.getBackAmount() - oldItem.getBackAmount() + item.getBackAmount());   // 已返回数量
                                noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());   // 待返回数量
                                recycleNoticeItemService.getRepository().save(noticeItem);
                            }
                            oldItem.setBackAmount(item.getBackAmount());
                            oldItem.setLogisticsNum(item.getLogisticsNum());
                        }
                    }
                    // 不存在
                    if (exists == false) {
                        removeList.add(oldItem);
                        RecycleNoticeItemEntity noticeItem = (RecycleNoticeItemEntity) recycleNoticeItemService.getRepository().findOne(oldItem.getRecycleNoticeItem().getObjId());
                        if (noticeItem != null) {
                            noticeItem.setBackAmount(noticeItem.getBackAmount() - oldItem.getBackAmount());   // 已返回数量
                            noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());   // 待返回数量
                            recycleNoticeItemService.getRepository().save(noticeItem);
                        }
                    }
                }
                for (RecycleItemEntity deleteItem : removeList) {
                    recycleEntity.getItems().remove(deleteItem);
                }
                for (RecycleItemEntity addItem : addList) {
                    recycleEntity.addItem(addItem);
                }
            } else {
                for (RecycleItemEntity addItem : recycleRequest.getItems()) {
                    recycleEntity.addItem(addItem);
                    RecycleNoticeItemEntity noticeItem = (RecycleNoticeItemEntity) recycleNoticeItemService.getRepository().findOne(addItem.getRecycleNoticeItem().getObjId());
                    if (noticeItem != null) {
                        noticeItem.setBackAmount(noticeItem.getBackAmount() + addItem.getBackAmount());       // 已返回数量
                        noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());   // 待返回数量
                        recycleNoticeItemService.getRepository().save(noticeItem);
                    }
                }
            }
            recycleEntity.setLogistics(this.recycleRequest.getLogistics());
            recycleEntity.setLogisticsNum(this.recycleRequest.getLogisticsNum());
            recycleEntity.setTransportExpense(this.recycleRequest.getTransportExpense());
            recycleEntity.setOtherExpense(this.recycleRequest.getOtherExpense());
            recycleEntity.setOperatorPhone(this.recycleRequest.getOperatorPhone());
            recycleEntity.setExpenseTotal(this.recycleRequest.getExpenseTotal());
            recycleEntity.setArriveDate(this.recycleRequest.getArriveDate());
            recycleEntity.setRecyclefile(this.recycleRequest.getRecyclefile());
            recycleEntity.setRecyclefileName(this.recycleRequest.getRecyclefileName());
            recycleEntity.setLogisticsfile(this.recycleRequest.getLogisticsfile());
            recycleEntity.setLogisticsfileName(this.recycleRequest.getLogisticsfileName());
            recycleEntity.setComment(this.recycleRequest.getComment());
            this.saveEntity(recycleEntity);
            this.recycleRequest = recycleService.findOneWithItems(recycleEntity.getObjId());    // 旧单据
            this.setEntity(this.recycleRequest);
        }
    }

    @Override
    protected Boolean checkValid() {

        if (this.getDealer() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (this.recycleRequest.getArriveDate() != null) {
            if (this.recycleRequest.getCreatedTime().getTime() >= this.recycleRequest.getArriveDate().getTime()) {
                ZkUtils.showInformation("送达时间不能小于创建时间", "提示");
                return false;
            }
        }
        if (StringUtils.isBlank(this.recycleRequest.getLogistics())) {
            ZkUtils.showInformation("请填写物流名称", "提示");
            return false;
        }
        if (StringUtils.isBlank(this.recycleRequest.getLogisticsNum())) {
            ZkUtils.showInformation("请填写物流单号", "提示");
            return false;
        }
        if (this.recycleRequest.getTransportExpense() <= 0) {
            ZkUtils.showInformation("运输费用不能小于0！", "提示");
            return false;
        }
        if (this.recycleRequest.getRecyclefile() == null) {
            ZkUtils.showInformation("请上传故障件图片！", "提示");
            return false;
        }
        if (this.recycleRequest.getLogisticsfile() == null) {
            ZkUtils.showInformation("请上传物流凭证！", "提示");
            return false;
        }

        if (this.recycleRequest.getItems().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }
        for (RecycleItemEntity item : recycleRequest.getItems()) {
            if (item.getBackAmount() > item.getWaitAmount()) {
                ZkUtils.showInformation("本次返回数量大于应返回数量", "错误");
                return false;
            }
        }


        return true;
    }

    @Command
    @NotifyChange("recycleRequest")
    public void changeExpense() {
        if (this.recycleRequest.getOtherExpense() == null) {
            this.recycleRequest.setOtherExpense(0.0);
        }
        if (this.recycleRequest.getTransportExpense() == null) {
            this.recycleRequest.setTransportExpense(0.0);
        }
        recycleRequest.setExpenseTotal(recycleRequest.getOtherExpense() + recycleRequest.getTransportExpense());
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
        if (!org.apache.commons.lang3.StringUtils.isNotEmpty(this.getEntity().getObjId())) {
            ZkUtils.showExclamation("请先保存数据再提交！", "系统提示");
            return;
        }
        changeCount();
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
    protected void completeTask(String outcome, String comment) throws IOException {
        if ("同意".equals(outcome) && this.recycleRequest.getArriveDate() == null) {
            ZkUtils.showInformation("请填写送达时间", "提示");
            return;
        }
        super.completeTask(outcome, comment);
    }

    @Command
    @NotifyChange("recycleRequest")
    public void addItemModel() {
        if (this.getDealer() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", 101010);
        map.put("dealerCode", this.getDealer().getCode());
        window = (Window) ZkUtils.createComponents("/views/asm/select_recycle_form.zul", null, map);
        window.setTitle("选择故障件");
        window.doModal();

    }

    @Command
    @NotifyChange("recycleRequest")
    public void deleteItem(@BindingParam("model") RecycleItemEntity model) {
        this.recycleRequest.getItems().remove(model);
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event, @BindingParam("t") String type) {
        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_ASM);
        if (type.equalsIgnoreCase("file01")) {
            this.recycleRequest.setRecyclefile(fileName);
            this.recycleRequest.setRecyclefileName(event.getMedia().getName());
        } else if (type.equalsIgnoreCase("file02")) {
            this.recycleRequest.setLogisticsfile(fileName);
            this.recycleRequest.setLogisticsfileName(event.getMedia().getName());
        }
    }

    @Command
    @NotifyChange("recycleRequest")
    public void delUploadFile(@BindingParam("t") String type) {
        if (type.equalsIgnoreCase("file01")) {
            this.recycleRequest.setRecyclefile("");
        } else if (type.equalsIgnoreCase("file02")) {
            this.recycleRequest.setLogisticsfile("");
        }
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_ASM + filename;
    }


    @GlobalCommand
    @NotifyChange("recycleRequest")
    public void updateSelectedRecycleList(@BindingParam("recycleNoticeItem") List<RecycleNoticeItemEntity> RecycleNoticeItems) {
        for (RecycleNoticeItemEntity recycle : RecycleNoticeItems) {
            Boolean exists = false;
            for (RecycleItemEntity v : this.recycleRequest.getItems()) {
                if (v.getRecycleNoticeItem().getObjId().equals(recycle.getObjId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                RecycleItemEntity recycleItem = new RecycleItemEntity();
                recycleItem.setPartCode(recycle.getPartCode());
                recycleItem.setPartName(recycle.getPartName());
                recycleItem.setWaitAmount(recycle.getAmount() - recycle.getBackAmount());  // 还剩应返回的数量
                recycleItem.setBackAmount(recycle.getAmount() - recycle.getBackAmount());
                recycleItem.setSrcDocNo(recycle.getRecycleNotice().getSrcDocNo());
                recycleItem.setSrcDocType(recycle.getRecycleNotice().getSrcDocType());
                recycleItem.setLogisticsNum(recycleRequest.getLogisticsNum());
                recycleItem.setWarrantyTime(recycle.getWarrantyTime());
                recycleItem.setWarrantyMileage(recycle.getWarrantyMileage());
                recycleItem.setPattern(recycle.getPattern());
                recycle.setReason(recycle.getReason());
                recycleItem.setRecycleNoticeItem(recycle);
                this.recycleRequest.addItem(recycleItem);
            }
        }

        if (window != null) {
            window.detach();
        }

    }

    //待返推送更新
    @NotifyChange("*")
    public void updateSelectedRecycleNoticeList() {
        //得到待返配件
        List<RecycleNoticeItemEntity> recycleNoticeItems = (List<RecycleNoticeItemEntity>) Executions.getCurrent().getArg().get("recycleNoticeItem");
        if (recycleNoticeItems == null) {
            return;
        } else {
            this.recycleRequest.getItems().clear();
            for (RecycleNoticeItemEntity item : recycleNoticeItems) {
                RecycleItemEntity recycleItem = new RecycleItemEntity();
                recycleItem.setPartCode(item.getPartCode());   // 配件号
                recycleItem.setPartName(item.getPartName());    // 配给名称
                recycleItem.setWaitAmount(item.getAmount() - item.getBackAmount());  // 还剩应返回的数量
                recycleItem.setBackAmount(item.getAmount() - item.getBackAmount());  // 还剩应返回的数量
                recycleItem.setSrcDocNo(item.getRecycleNotice().getSrcDocNo());   // 来源单据编号
                recycleItem.setSrcDocType(item.getRecycleNotice().getSrcDocType()); // 单据来源类型
                recycleItem.setLogisticsNum(recycleRequest.getLogisticsNum());  // 物流号码
                recycleItem.setWarrantyTime(item.getWarrantyTime());        // 三包时间
                recycleItem.setWarrantyMileage(item.getWarrantyMileage()); // 三包里程
                recycleItem.setPattern(item.getPattern());  // 故障模式
                recycleItem.setReason(item.getReason());  //   换件原因
                recycleItem.setRecycleNoticeItem(item);
                this.recycleRequest.setDealerCode(item.getRecycleNotice().getDealerCode());   // 服务站编号
                this.recycleRequest.setDealerName(item.getRecycleNotice().getDealerName());  //  服务站名称
                this.recycleRequest.addItem(recycleItem);
            }
        }

    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.recycleRequest.getObjId() == null ? "" : this.recycleRequest.getObjId());
        map.put("docNo", this.recycleRequest.getDocNo() == null ? "" : this.recycleRequest.getDocNo());
        map.put("dealerCode", recycleRequest.getDealerCode() == null ? "" : this.recycleRequest.getDealerCode());
        map.put("type", "recycle");
        window = (Window) ZkUtils.createComponents("/views/report/asm/recycle_report.zul", null, map);
        window.setTitle("打印返回故障件单据");
        window.doModal();

    }

    @Command
    public void printRecycleItemReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.recycleRequest.getObjId() == null ? "" : this.recycleRequest.getObjId());
        map.put("docNo", this.recycleRequest.getDocNo() == null ? "" : this.recycleRequest.getDocNo());
        map.put("type", "");
        map.put("dealerCode", recycleRequest.getDealerCode() == null ? "" : this.recycleRequest.getDealerCode());
        window = (Window) ZkUtils.createComponents("/views/report/asm/recycle_report.zul", null, map);
        window.setTitle("打印故障件标签");
        window.doModal();
    }

    @Command
    @NotifyChange("*")
    @Override
    public void desert() {
        super.desert();

        RecycleEntity recycleEntity = recycleService.findOneWithItems(this.recycleRequest.getObjId());
        for (RecycleItemEntity item : recycleEntity.getItems()) {
            RecycleNoticeItemEntity noticeItem = item.getRecycleNoticeItem();
            noticeItem.setBackAmount(noticeItem.getBackAmount() - item.getBackAmount());
            noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());
            recycleNoticeItemService.getRepository().save(noticeItem);
            //item.setRecycleEntity(null);
            item.setRecycleNoticeItem(null);
            recycleItemRepository.save(item);

        }
    }


    @Override
    public Boolean checkCanPrintReport() {
        return true;
    }

    public RecycleEntity getRecycleRequest() {
        return recycleRequest;
    }

    public void setRecycleRequest(RecycleEntity recycleRequest) {
        this.recycleRequest = recycleRequest;
    }

    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

}
