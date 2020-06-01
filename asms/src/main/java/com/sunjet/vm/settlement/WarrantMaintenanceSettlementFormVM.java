package com.sunjet.vm.settlement;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.DealerSettlementEntity;
import com.sunjet.model.asm.ExpenseListEntity;
import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.DealerSettlementService;
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
 * 服务结算单 表单 VM
 */
public class WarrantMaintenanceSettlementFormVM extends FlowFormBaseVM {

    @WireVariable
    private DealerSettlementService dealerSettlementService;
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private UserService userService;
    private DealerSettlementEntity dealerSettlement = new DealerSettlementEntity();
    private DealerEntity dealer;
    private List<DealerEntity> dealers = new ArrayList<>();
    private String keyword = "";        // 搜索关键字
    private Window window;
    private Map<String, Object> variables = new HashMap<>();

    public WarrantMaintenanceSettlementFormVM() {
    }

    @Init(superclass = true)
    public void init() {
        this.dealer = new DealerEntity();
        this.setBaseService(dealerSettlementService);
        //initData();
        if (this.getEntity() != null) {    // 有业务对象
            this.dealerSettlement = (DealerSettlementEntity) this.getEntity();
            this.dealerSettlement = dealerSettlementService.findOneById(this.dealerSettlement.getObjId());
            dealer.setName(dealerSettlement.getDealerName());
            dealer.setCode(dealerSettlement.getDealerCode());
            this.setReadonly(true);
        } else {
            if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
                this.dealerSettlement = dealerSettlementService.findOneById(this.getBusinessId());
                dealer.setName(dealerSettlement.getDealerName());
                dealer.setCode(dealerSettlement.getDealerCode());
                this.setReadonly(true);
            } else {        // 业务对象和业务对象Id都为空
                List<PendingSettlementDetailsEntity> details = (List<PendingSettlementDetailsEntity>) Executions.getCurrent().getArg().get("pendingSettlement");
                dealerSettlement = new DealerSettlementEntity();
                dealerSettlement.setSettlementType("服务结算单");
                dealerSettlement.setRequestDate(new Date());
                dealerSettlement.setOperator(CommonHelper.getActiveUser().getUsername());
                dealerSettlement.setStartDate(DateHelper.getFirstOfMonth());
                dealerSettlement.setEndDate(new Date());

                if (details != null) {
                    double expenseTotal = 0.0;
                    double freightExpense = 0.0;
                    double otherExpense = 0.0;
                    double partExpense = 0.0;
                    double workingExpense = 0.0;
                    double outExpense = 0.0;
                    for (PendingSettlementDetailsEntity detail : details) {
                        ExpenseListEntity expenseEntity = new ExpenseListEntity();
                        expenseEntity.setBusinessDate(detail.getBusinessDate());
                        expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                        expenseEntity.setFreightExpense(detail.getFreightExpense());
                        expenseEntity.setOtherExpense(detail.getOtherExpense());
                        expenseEntity.setPartExpense(detail.getPartExpense());
                        expenseEntity.setWorkingExpense(detail.getWorkingExpense());
                        expenseEntity.setOutExpense(detail.getOutExpense());
                        expenseEntity.setSrcDocID(detail.getSrcDocID());
                        expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                        expenseEntity.setSrcDocType(detail.getSrcDocType());
                        freightExpense = freightExpense + expenseEntity.getFreightExpense();
                        otherExpense = otherExpense + expenseEntity.getOtherExpense();
                        partExpense = partExpense + expenseEntity.getPartExpense();
                        workingExpense = workingExpense + expenseEntity.getWorkingExpense();
                        outExpense = outExpense + expenseEntity.getOutExpense();
                        expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                        this.dealerSettlement.addExpense(expenseEntity);
                        if (detail.getDealerCode() != null && this.dealerSettlement.getDealerCode() == null) {
                            DealerEntity dealerEntity = dealerService.findDealerByCode(detail.getDealerCode());
                            this.setDealer(dealerEntity);
                            this.dealerSettlement.setDealerCode(dealerEntity.getCode());
                            this.dealerSettlement.setDealerName(dealerEntity.getName());
                            this.dealerSettlement.setProvinceName(dealerEntity.getProvince().getName());
                        }
                    }
                    dealerSettlement.setOtherExpense(otherExpense);
                    dealerSettlement.setPartExpense(partExpense);
                    dealerSettlement.setWorkingExpense(workingExpense);
                    dealerSettlement.setOutExpense(outExpense);
                    dealerSettlement.setPunishmentExpense(0.0);
                    dealerSettlement.setRewardExpense(0.0);
                    dealerSettlement.setExpenseTotal(expenseTotal);
                    this.setReadonly(true);
                } else {
                    DealerEntity dealer = userService.findOne(CommonHelper.getActiveUser().getUserId()).getDealer();
                    if (dealer != null) {
                        this.dealerSettlement.setDealerCode(dealer.getCode());
                        this.dealerSettlement.setDealerName(dealer.getName());
                        this.dealerSettlement.setProvinceName(dealer.getProvince().getName());
                    }
                    this.setReadonly(false);
                }
            }
        }

        this.setEntity(dealerSettlement);
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

            if (dealerSettlement.getItems().size() < 1) {
                ZkUtils.showInformation("请先获取服务单！", "提示");
                return;
            }

            //当主表非首次保存时，子表需要手工关联主表
            if (dealerSettlement.getObjId() != null) {
                for (ExpenseListEntity Item : dealerSettlement.getItems()) {
                    Item.setDealerSettlement(dealerSettlement);

                }
            }

            dealerSettlement.setDealerName(dealer.getName());
            dealerSettlement.setDealerCode(dealer.getCode());
            dealerSettlement = (DealerSettlementEntity) this.saveEntity(dealerSettlement);
            dealerSettlement = dealerSettlementService.findOneById(dealerSettlement.getObjId());
            this.setEntity(dealerSettlement);

//清空单据对应的结算信息
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(dealerSettlement.getObjId());
            if (details != null) {
                for (PendingSettlementDetailsEntity detail : details) {
                    detail.setSettlementDocID(null);
                    detail.setSettlementDocNo(null);
                    detail.setSettlementDocType(null);
                    detail.setSettlement(false);
                    detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                    detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                    detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                    detail.setModifiedTime(new Date());
                    pendingSettlementDetailsService.getRepository().save(detail);
                }
            }


            for (ExpenseListEntity expense : dealerSettlement.getItems()) {
                PendingSettlementDetailsEntity detail = pendingSettlementDetailsService.getOneBySrcDocID(expense.getSrcDocID());
                detail.setSettlementDocID(dealerSettlement.getObjId());
                detail.setSettlementDocNo(dealerSettlement.getDocNo());
                detail.setDocNo(dealerSettlement.getDocNo());
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
    @NotifyChange("dealerSettlement")
    public void changeExpense() {
        if (dealerSettlement.getRewardExpense() == null) {
            dealerSettlement.setRewardExpense(0.0);
        }
        if (dealerSettlement.getPunishmentExpense() == null) {
            dealerSettlement.setPunishmentExpense(0.0);
        }
        dealerSettlement.setExpenseTotal(
                dealerSettlement.getOtherExpense()
                        + dealerSettlement.getWorkingExpense()
                        + dealerSettlement.getOutExpense()
                        + dealerSettlement.getPartExpense()
                        - dealerSettlement.getPunishmentExpense()
                        + dealerSettlement.getRewardExpense());
    }

    @Command
    @NotifyChange("dealerSettlement")
    public void ReadItem() {
        double expenseTotal = 0.0;
        double freightExpense = 0.0;
        double otherExpense = 0.0;
        double partExpense = 0.0;
        double workingExpense = 0.0;
        double outExpense = 0.0;

        //如果单据已保存再次取数据，需先清空单据对应的结算信息,再添加原有单据子行
        this.dealerSettlement.getItems().clear();
        if (StringUtils.isNotBlank(dealerSettlement.getObjId())) {
            List<ExpenseListEntity> expenseListEntityList;
            expenseListEntityList = dealerSettlementService.findExpenseListById(dealerSettlement.getObjId());
            for (ExpenseListEntity expenseList : expenseListEntityList) {
                this.dealerSettlement.getItems().add(expenseList);
                expenseTotal = expenseTotal + expenseList.getExpenseTotal();
                freightExpense = freightExpense + expenseList.getFreightExpense();
                otherExpense = otherExpense + expenseList.getOtherExpense();
                partExpense = partExpense + expenseList.getPartExpense();
                workingExpense = workingExpense + expenseList.getWorkingExpense();
                outExpense = outExpense + expenseList.getOutExpense();
            }
        }


        if (dealer != null && StringUtils.isNotBlank(dealer.getCode())) {
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getDealerSelttlements(dealer.getCode(), dealerSettlement.getStartDate(), dealerSettlement.getEndDate());
            for (PendingSettlementDetailsEntity detail : details) {
                ExpenseListEntity expenseEntity = new ExpenseListEntity();
                expenseEntity.setBusinessDate(detail.getBusinessDate());
                expenseEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseEntity.setFreightExpense(detail.getFreightExpense());
                expenseEntity.setOtherExpense(detail.getOtherExpense());
                expenseEntity.setPartExpense(detail.getPartExpense());
                expenseEntity.setWorkingExpense(detail.getWorkingExpense());
                expenseEntity.setOutExpense(detail.getOutExpense());
                expenseEntity.setSrcDocID(detail.getSrcDocID());
                expenseEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + expenseEntity.getFreightExpense();
                otherExpense = otherExpense + expenseEntity.getOtherExpense();
                partExpense = partExpense + expenseEntity.getPartExpense();
                workingExpense = workingExpense + expenseEntity.getWorkingExpense();
                outExpense = outExpense + expenseEntity.getOutExpense();
                expenseTotal = expenseTotal + expenseEntity.getExpenseTotal();
                this.dealerSettlement.addExpense(expenseEntity);
            }
            dealerSettlement.setOtherExpense(otherExpense);
            dealerSettlement.setPartExpense(partExpense);
            dealerSettlement.setWorkingExpense(workingExpense);
            dealerSettlement.setOutExpense(outExpense);
            dealerSettlement.setPunishmentExpense(0.0);
            dealerSettlement.setRewardExpense(0.0);
            dealerSettlement.setExpenseTotal(expenseTotal);
        } else {
            ZkUtils.showInformation("请先选择服务站！", "提示");
        }
    }

    @Command
    @NotifyChange("dealerSettlement")
    public void deleteItem(@BindingParam("model") ExpenseListEntity model) {
        //回写待结算列表
        PendingSettlementDetailsEntity detail = pendingSettlementDetailsService.getOneBySrcDocID(model.getSrcDocID());
        detail.setSettlementDocID(null);
        detail.setSettlementDocNo(null);
        detail.setSettlement(false);
        detail.setSettlementDocType("服务结算单");
        detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setModifierId(CommonHelper.getActiveUser().getLogId());
        detail.setModifierName(CommonHelper.getActiveUser().getUsername());
        detail.setModifiedTime(new Date());
        pendingSettlementDetailsService.getRepository().save(detail);
        for (ExpenseListEntity item : this.dealerSettlement.getItems()) {
            if (item.equals(model)) {
                this.dealerSettlement.setExpenseTotal(this.dealerSettlement.getExpenseTotal() - model.getExpenseTotal());
                this.dealerSettlement.setOtherExpense(this.dealerSettlement.getOtherExpense() - model.getOtherExpense());
                this.dealerSettlement.setWorkingExpense(this.dealerSettlement.getWorkingExpense() - model.getWorkingExpense());
                this.dealerSettlement.setOutExpense(this.dealerSettlement.getOutExpense() - model.getOutExpense());
                this.dealerSettlement.setPartExpense(this.dealerSettlement.getPartExpense() - model.getPartExpense());


                this.dealerSettlement.getItems().remove(model);
                dealerSettlementService.getRepository().save(this.dealerSettlement);
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


    @Command
    @NotifyChange("*")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.setKeyword("");
        this.dealers.clear();
        this.dealer = dealer;
        dealerSettlement.setDealerName(dealer.getName());
        dealerSettlement.setDealerCode(dealer.getCode());
        if (dealer.getProvince() != null)
            dealerSettlement.setProvinceName(dealer.getProvince().getName());

    }


    public DealerSettlementEntity getDealerSettlement() {
        return dealerSettlement;
    }

    public void setDealerSettlement(DealerSettlementEntity dealerSettlement) {
        this.dealerSettlement = dealerSettlement;
    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.dealerSettlement.getObjId() == null ? "" : this.getDealerSettlement().getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/settlement/dealer_settlement_report.zul", null, map);
        window.setTitle("打印结算单");
        window.doModal();
    }

    @Command
    public void printDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.dealerSettlement.getObjId() == null ? "" : this.getDealerSettlement().getObjId());
        map.put("startDate", dateToString(this.getDealerSettlement().getStartDate()));
        map.put("endDate", dateToString(this.getDealerSettlement().getEndDate()));

        window = (Window) ZkUtils.createComponents("/views/report/settlement/dealer_settlement_detail.zul", null, map);
        window.setTitle("打印结算明细");
        window.doModal();

    }

    @Override
    public Boolean checkCanPrintReport() {
        if (this.dealerSettlement.getStatus().equals(DocStatus.CLOSED.getIndex())) {
            return true;
        }
        return false;
    }

    public static String dateToString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String ctime = formatter.format(time);

        return ctime;
    }

    @Command
    @NotifyChange("*")
    @Override
    public void desert() {
        super.desert();
        List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(this.dealerSettlement.getObjId());
        if (details != null) {
            for (PendingSettlementDetailsEntity detail : details) {
                detail.setSettlementDocID(null);
                detail.setSettlementDocNo(null);
                detail.setSettlement(false);
                detail.setSettlementDocType("服务结算单");
                detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.getRepository().save(detail);
            }
        }
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
