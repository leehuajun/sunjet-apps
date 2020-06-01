package com.sunjet.vm.settlement;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.FreightExpenseEntity;
import com.sunjet.model.asm.FreightSettlementEntity;
import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.FreightSettlementService;
import com.sunjet.service.asm.PendingSettlementDetailsService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zyh
 * @create 2016-11-14 上午11:38
 * <p>
 * 返回件运费单 表单 VM
 */
public class FreightCostFormVM extends FlowFormBaseVM {

    @WireVariable
    private FreightSettlementService freightSettlementService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private DealerService dealerService;
    private FreightSettlementEntity freightSettlement = new FreightSettlementEntity();
    private DealerEntity dealer;
    private List<DealerEntity> dealers = new ArrayList<>();
    private String keyword = "";        // 搜索关键字
    private Window window;
    private Map<String, Object> variables = new HashMap<>();

    public FreightCostFormVM() {
    }

    @Init(superclass = true)
    public void init() {
        this.dealer = new DealerEntity();
        this.setBaseService(freightSettlementService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.freightSettlement = freightSettlementService.findOneById(this.getBusinessId());
            dealer.setName(freightSettlement.getDealerName());
            dealer.setCode(freightSettlement.getDealerCode());
            this.setReadonly(true);
        } else {
            List<PendingSettlementDetailsEntity> details = (List<PendingSettlementDetailsEntity>) Executions.getCurrent().getArg().get("pendingSettlement");
            freightSettlement = new FreightSettlementEntity();
            freightSettlement.setSettlementType("运费结算单");
            freightSettlement.setRequestDate(new Date());
            freightSettlement.setStartDate(DateHelper.getFirstOfMonth());
            freightSettlement.setEndDate(new Date());
            freightSettlement.setOperator(CommonHelper.getActiveUser().getUsername());
            if (details != null) {
                double expenseTotal = 0.0;
                double freightExpense = 0.0;
                double otherExpense = 0.0;
                for (PendingSettlementDetailsEntity detail : details) {
                    FreightExpenseEntity expenseEntity = new FreightExpenseEntity();
                    expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                    expenseEntity.setFreightExpense(detail.getFreightExpense());
                    expenseEntity.setOtherExpense(detail.getOtherExpense());
                    expenseEntity.setSrcDocID(detail.getSrcDocID());
                    expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                    expenseEntity.setSrcDocType(detail.getSrcDocType());
                    freightExpense = freightExpense + expenseEntity.getFreightExpense();
                    otherExpense = otherExpense + expenseEntity.getOtherExpense();
                    expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                    this.freightSettlement.getItems().add(expenseEntity);
                    if (detail.getDealerCode() != null && this.freightSettlement.getDealerCode() == null) {
                        DealerEntity dealerEntity = dealerService.findDealerByCode(detail.getDealerCode());
                        this.setDealer(dealerEntity);
                        this.freightSettlement.setDealerCode(dealerEntity.getCode());
                        this.freightSettlement.setDealerName(dealerEntity.getName());
                        this.freightSettlement.setProvinceName(dealerEntity.getProvince().getName());
                    }
                }
                freightSettlement.setFreightExpense(freightExpense);
                freightSettlement.setOtherExpense(otherExpense);
                freightSettlement.setExpenseTotal(expenseTotal);
                this.setReadonly(true);
            } else {
                // 业务对象和业务对象Id都为空
                DealerEntity dealer = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
                if (dealer != null) {
                    this.freightSettlement.setDealerCode(dealer.getCode());
                    this.freightSettlement.setDealerName(dealer.getName());
                    this.freightSettlement.setProvinceName(dealer.getProvince().getName());
                }
                this.setReadonly(false);
            }
        }
        this.setEntity(freightSettlement);
    }


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
        if (win != null) {
            win.setTitle(win.getTitle() + titleMsg);
        }
    }

    @Override
    protected Boolean checkValid() {
        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {

        List<UserEntity> list = userService.findAllByDealerCode(this.dealer.getCode());

        List<String> users = new ArrayList<>();
        for (UserEntity userEntity : list) {
            System.out.println(userEntity.getLogId());
            users.add(userEntity.getLogId());
        }

        variables.put("dealerUsers", users);
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

    /**
     * 保存对象
     */
    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (dealer != null) {
            if (freightSettlement.getItems().size() < 1) {
                ZkUtils.showInformation("请先获取故障件返回单！", "提示");
                return;
            }

            freightSettlement.setDealerName(dealer.getName());
            freightSettlement.setDealerCode(dealer.getCode());

            //当主表非首次保存时，子表需要手工关联主表
            if (freightSettlement.getObjId() != null) {
                for (FreightExpenseEntity Item : freightSettlement.getItems()) {
                    Item.setFreightSettlement(freightSettlement);

                }
            }
            freightSettlement = (FreightSettlementEntity) this.saveEntity(freightSettlement);
            freightSettlement = freightSettlementService.findOneById(freightSettlement.getObjId());
            this.setEntity(freightSettlement);
            //清空单据对应的结算信息
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(freightSettlement.getObjId());
            if (details != null) {
                for (PendingSettlementDetailsEntity detail : details) {
                    detail.setSettlementDocID(null);
                    detail.setSettlementDocNo(null);
                    detail.setSettlementDocType(null);
                    detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                    detail.setSettlement(false);
                    detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                    detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                    detail.setModifiedTime(new Date());
                    pendingSettlementDetailsService.getRepository().save(detail);
                }
            }


            for (FreightExpenseEntity expense : freightSettlement.getItems()) {
                PendingSettlementDetailsEntity detail = pendingSettlementDetailsService.getOneBySrcDocID(expense.getSrcDocID());
                detail.setSettlementDocID(freightSettlement.getObjId());
                detail.setSettlementDocNo(freightSettlement.getDocNo());
                detail.setDocNo(freightSettlement.getDocNo());
                detail.setSettlement(true);
                detail.setSettlementStatus(DocStatus.SETTLING.getIndex());
                detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.getRepository().save(detail);
            }

            showDialog();
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");

        }
    }

    @Command
    @NotifyChange("freightSettlement")
    public void changeExpense() {
        freightSettlement.setExpenseTotal(freightSettlement.getOtherExpense() + freightSettlement.getFreightExpense());
    }

    @Command
    @NotifyChange("freightSettlement")
    public void ReadItem() {
        double expenseTotal = 0.0;
        double freightExpense = 0.0;
        double otherExpense = 0.0;
        //如果单据已保存再次取数据，需先清空单据对应的结算信息,再添加原有单据子行
        this.freightSettlement.getItems().clear();
        if (StringUtils.isNotBlank(freightSettlement.getObjId())) {
            List<FreightExpenseEntity> EntityList;
            EntityList = freightSettlementService.findFreightExpenseById(freightSettlement.getObjId());
            for (FreightExpenseEntity expenseList : EntityList) {
                this.freightSettlement.getItems().add(expenseList);
                expenseTotal = expenseTotal + expenseList.getExpenseTotal();
                freightExpense = freightExpense + expenseList.getFreightExpense();
                otherExpense = otherExpense + expenseList.getOtherExpense();
            }
        }

        if (dealer != null && org.apache.commons.lang3.StringUtils.isNotBlank(dealer.getCode())) {
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getFreightSelttlements(dealer.getCode(), freightSettlement.getStartDate(), freightSettlement.getEndDate());
            for (PendingSettlementDetailsEntity detail : details) {
                FreightExpenseEntity expenseEntity = new FreightExpenseEntity();
                expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseEntity.setFreightExpense(detail.getFreightExpense());
                expenseEntity.setOtherExpense(detail.getOtherExpense());
                expenseEntity.setSrcDocID(detail.getSrcDocID());
                expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + expenseEntity.getFreightExpense();
                otherExpense = otherExpense + expenseEntity.getOtherExpense();
                expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                this.freightSettlement.getItems().add(expenseEntity);
            }

            freightSettlement.setFreightExpense(freightExpense);
            freightSettlement.setOtherExpense(otherExpense);
            freightSettlement.setExpenseTotal(expenseTotal);
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");
        }
    }

    @Command
    @NotifyChange("freightSettlement")
    public void deleteItem(@BindingParam("model") FreightExpenseEntity model) {
        //回写待结算列表
        PendingSettlementDetailsEntity detail = pendingSettlementDetailsService.getOneBySrcDocID(model.getSrcDocID());
        detail.setSettlementDocID(null);
        detail.setSettlementDocNo(null);
        detail.setSettlement(false);
        detail.setSettlementDocType("运费结算单");
        detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setModifierId(CommonHelper.getActiveUser().getLogId());
        detail.setModifierName(CommonHelper.getActiveUser().getUsername());
        detail.setModifiedTime(new Date());
        pendingSettlementDetailsService.getRepository().save(detail);
        for (FreightExpenseEntity item : this.freightSettlement.getItems()) {
            if (item == model) {
                this.freightSettlement.setExpenseTotal(this.freightSettlement.getExpenseTotal() - model.getExpenseTotal());
                this.freightSettlement.setOtherExpense(this.freightSettlement.getOtherExpense() - model.getOtherExpense());
                this.freightSettlement.setFreightExpense(this.freightSettlement.getFreightExpense() - model.getFreightExpense());
                this.freightSettlement.getItems().remove(model);
                freightSettlementService.getRepository().save(this.freightSettlement);
                return;
            }
        }
    }


    @Command
    @NotifyChange("dealers")
    public void searchDealers(@BindingParam("model") String keyword) {
//        this.dealers = dealerService.getDealersByUserIdAndKeyword(CommonHelper.getActiveUser().getUserId(), keyword);
        if (CommonHelper.getActiveUser().getDealer() != null) {   // 服务站用户
            if (CommonHelper.getActiveUser().getDealer().getParent() != null) {  // 是二级服务站
                this.dealers.clear();
                this.dealers.add(CommonHelper.getActiveUser().getDealer());
                this.dealers.add(CommonHelper.getActiveUser().getDealer().getParent());
            } else {   // 一级服务站
                this.dealers = dealerService.findChildrenByParentIdAndFilter(CommonHelper.getActiveUser().getDealer().getObjId(), "%" + keyword + "%");
                this.dealers.add(CommonHelper.getActiveUser().getDealer());
            }
        } else if (CommonHelper.getActiveUser().getAgency() != null) {   // 合作商
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.dealers = dealerService.findAllByKeyword(keyword);
        }
    }

    @Command
    @NotifyChange("dealer")
    public void clearSelectedDealer() {
//        List<DealerEntity> dealers = new ArrayList<>();
//        UserEntity user = userService.findOne(CommonHelper.getActiveUser().getUserId());
//        if (user.getDealer() != null) {
//            this.dealer = user.getDealer();
//        } else {
//            this.dealer = null;
//        }
        if (CommonHelper.getActiveUser().getDealer() != null) {
            this.dealer = CommonHelper.getActiveUser().getDealer();
        } else {
            this.dealer = null;
        }


        this.setKeyword("");
    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.freightSettlement.getObjId() == null ? "" : this.freightSettlement.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/settlement/freight_settlement_report.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }


    @Command
    public void printDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.freightSettlement.getObjId() == null ? "" : this.freightSettlement.getObjId());
        map.put("startDate", dateToString(this.freightSettlement.getStartDate()));
        map.put("endDate", dateToString(this.freightSettlement.getEndDate()));

        window = (Window) ZkUtils.createComponents("/views/report/settlement/freight_settlement_detail.zul", null, map);
        window.setTitle("打印结算明细");
        window.doModal();

    }

    public static String dateToString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ctime = formatter.format(time);

        return ctime;
    }

    @Override
    public Boolean checkCanPrintReport() {
        if (this.freightSettlement.getStatus().equals(DocStatus.CLOSED.getIndex())) {
            return true;
        }
        return false;
    }

    @Command
    @NotifyChange("*")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.setKeyword("");
        this.dealers.clear();
        this.dealer = dealer;
        if (dealer.getProvince() != null) {
            this.freightSettlement.setDealerCode(dealer.getCode());
            this.freightSettlement.setDealerName(dealer.getName());
            this.freightSettlement.setProvinceName(dealer.getProvince().getName());
        }
    }

    @Command
    @NotifyChange("*")
    @Override
    public void desert() {
        super.desert();
        List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(this.freightSettlement.getObjId());
        if (details != null) {
            for (PendingSettlementDetailsEntity detail : details) {
                detail.setSettlementDocID(null);
                detail.setSettlementDocNo(null);
                detail.setSettlement(false);
                detail.setSettlementDocType("运费结算单");
                detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.getRepository().save(detail);
            }
        }
    }

    public FreightSettlementEntity getFreightSettlement() {
        return freightSettlement;
    }

    public void setFreightSettlement(FreightSettlementEntity freightSettlement) {
        this.freightSettlement = freightSettlement;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
