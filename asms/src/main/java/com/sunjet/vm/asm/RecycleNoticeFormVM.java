package com.sunjet.vm.asm;

import com.sunjet.model.asm.RecycleNoticeEntity;
import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.basic.PartEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.RecycleNoticeService;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.PartService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.List;

import static com.sunjet.utils.common.CommonHelper.FILTER_PARTS_ERROR;
import static com.sunjet.utils.common.CommonHelper.FILTER_PARTS_LEN;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 故障件返回通知单 表单 VM
 */
public class RecycleNoticeFormVM extends FlowFormBaseVM {
    @WireVariable
    private RecycleNoticeService recycleNoticeService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PartService partService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;

    private RecycleNoticeEntity recycleNotice;
    private RecycleNoticeItemEntity recycleNoticePart;
    private String keyword = "";
    private List<PartEntity> parts = new ArrayList<>();
    private DealerEntity dealer;
    private List<DealerEntity> dealers = new ArrayList<>();


    @Init(superclass = true)
    public void init() {
        this.setBaseService(recycleNoticeService);

        if (StringUtils.isNotBlank(this.getBusinessId())) { // 有业务对象Id
            this.recycleNotice = recycleNoticeService.findOneWithOthersId(this.getBusinessId());
            if (recycleNotice.getStatus() > 0) {
                this.setReadonly(true);
            } else {
                this.setReadonly(false);
            }
        } else { // 业务对象和业务对象Id都为空
            this.recycleNotice = new RecycleNoticeEntity();
            DealerEntity dealerEntity = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
            this.setReadonly(false);
            if (dealerEntity != null) {
                this.recycleNotice.setDealerCode(dealerEntity.getCode());
                this.recycleNotice.setDealerName(dealerEntity.getName());
            }
        }

        this.setEntity(this.recycleNotice);
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
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        //当主表非首次保存时，子表需要手工关联主表
        if (recycleNotice.getObjId() != null) {
            for (RecycleNoticeItemEntity Item : recycleNotice.getItems()) {
                Item.setRecycleNotice(recycleNotice);

            }
        }

        FlowDocEntity entity = this.saveEntity(this.recycleNotice);
        this.recycleNotice = recycleNoticeService.findOneWithOthersId(entity.getObjId());
        this.setEntity(this.recycleNotice);
//        this.setEntity(this.saveEntity(this.recycleNotice));
        showDialog();

    }

    @Override
    protected Boolean checkValid() {
        //if (this.recycleNotice.getStatus() > 0) {
        //    ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
        //    return false;
        //}
        if (this.recycleNotice.getDealerCode() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (this.recycleNotice.getItems().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }

        return true;
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

//        startProcessInstance(null);
    }

    @Command
    public void selectRecycleNoticePart(@BindingParam("model") RecycleNoticeItemEntity part) {
        this.recycleNoticePart = part;
    }

    @Command
    @NotifyChange("recycleNotice")
    public void addPart() {
        RecycleNoticeItemEntity item = new RecycleNoticeItemEntity();
        item.setAmount(1);
        this.recycleNotice.addNoticeItem(item);
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

    /**
     * 选中配件
     *
     * @param partEntity
     */
    @Command
    @NotifyChange("*")
    public void selectPart(@BindingParam("model") PartEntity partEntity) {
        recycleNoticePart.setPartCode(partEntity.getCode());
        recycleNoticePart.setPartName(partEntity.getName());
        this.keyword = "";
        this.parts.clear();
    }

    @Command
    @NotifyChange("dealers")
    public void searchDealers(@BindingParam("model") String keyword) {
        this.dealers = dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword);
        this.keyword = "";
    }

    @Command
    @NotifyChange("recycleNotice")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.dealer = dealer;
        this.recycleNotice.setDealerCode(dealer.getCode());
        this.recycleNotice.setDealerName(dealer.getName());
    }

    @Command
    @NotifyChange("*")
    public void clearSelectedDealer() {
        this.dealer = null;
    }


    @Command
    @NotifyChange("*")
    public void deletePart(@BindingParam("model") RecycleNoticeItemEntity model) {
        for (RecycleNoticeItemEntity item : recycleNotice.getItems()) {
            if (item == model) {
                recycleNotice.getItems().remove(item);
                return;
            }
        }
    }

    @Command
    @NotifyChange("*")
    @Override
    public void desert() {
        super.desert();
        WarrantyMaintenanceEntity warrantyMaintenanceEntity = warrantyMaintenanceService.findOneWithOthersById(this.recycleNotice.getSrcDocID());
        if (warrantyMaintenanceEntity != null) {
            warrantyMaintenanceEntity.setRecycleNoticeId(null);
            warrantyMaintenanceService.save(warrantyMaintenanceEntity);

        }

    }

    public RecycleNoticeEntity getRecycleNotice() {
        return recycleNotice;
    }

    public void setRecycleNotice(RecycleNoticeEntity recycleNotice) {
        this.recycleNotice = recycleNotice;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public RecycleNoticeItemEntity getRecycleNoticePart() {
        return recycleNoticePart;
    }

    public void setRecycleNoticePart(RecycleNoticeItemEntity recycleNoticePart) {
        this.recycleNoticePart = recycleNoticePart;
    }

    public List<PartEntity> getParts() {
        return parts;
    }

    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    public List<DealerEntity> getDealers() {
        return dealers;
    }

    public void setDealers(List<DealerEntity> dealers) {
        this.dealers = dealers;
    }
}
