package com.sunjet.vm.settlement;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.asm.AgencySettlementEntity;
import com.sunjet.model.asm.PartExpenseListEntity;
import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.asm.AgencySettlementService;
import com.sunjet.service.asm.PendingSettlementDetailsService;
import com.sunjet.service.basic.AgencyService;
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
 * 配件结算单 表单 VM
 */
public class PartCostFormVM extends FlowFormBaseVM {

    @WireVariable
    private AgencySettlementService agencySettlementService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @WireVariable
    private UserService userService;
    private AgencySettlementEntity agencySettlement = new AgencySettlementEntity();
    private AgencyEntity agency;
    private List<AgencyEntity> agencies = new ArrayList<>();
    private String keyword = "";        // 搜索关键字
    private Window window;
    private Map<String, Object> variables = new HashMap<>();
//    public PartCostFormVM() {
//    }

    @Init(superclass = true)
    public void init() {
        this.setBaseService(agencySettlementService);
        this.agency = new AgencyEntity();
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.agencySettlement = agencySettlementService.findOneById(this.getBusinessId());
            this.agency.setCode(this.agencySettlement.getAgencyCode());
            this.agency.setName(this.agencySettlement.getAgencyName());
            this.setReadonly(true);
        } else {        // 业务对象和业务对象Id都为空
            agencySettlement = new AgencySettlementEntity();
            agencySettlement.setRequestDate(new Date());
            agencySettlement.setStartDate(DateHelper.getFirstOfMonth());
            agencySettlement.setEndDate(new Date());
            agencySettlement.setOperator(CommonHelper.getActiveUser().getUsername());
            List<PendingSettlementDetailsEntity> details = (List<PendingSettlementDetailsEntity>) Executions.getCurrent().getArg().get("pendingSettlement");
            if (details != null) {
                double expenseTotal = 0.0;
                double freightExpense = 0.0;
                double otherExpense = 0.0;
                double partExpense = 0.0;
                double workingExpense = 0.0;
                for (PendingSettlementDetailsEntity detail : details) {
                    PartExpenseListEntity expenseListEntity = new PartExpenseListEntity();
                    expenseListEntity.setExpenseTotal(detail.getExpenseTotal());
                    expenseListEntity.setFreightExpense(detail.getFreightExpense());
                    expenseListEntity.setOtherExpense(detail.getOtherExpense());
                    expenseListEntity.setPartExpense(detail.getPartExpense());
                    expenseListEntity.setWorkingExpense(detail.getWorkingExpense());
                    expenseListEntity.setSrcDocID(detail.getSrcDocID());
                    expenseListEntity.setSrcDocNo(detail.getSrcDocNo());
                    expenseListEntity.setSrcDocType(detail.getSrcDocType());
                    freightExpense = freightExpense + expenseListEntity.getFreightExpense();
                    otherExpense = otherExpense + expenseListEntity.getOtherExpense();
                    partExpense = partExpense + expenseListEntity.getPartExpense();
                    workingExpense = workingExpense + expenseListEntity.getWorkingExpense();
                    expenseTotal = expenseTotal + expenseListEntity.getExpenseTotal();
                    this.agencySettlement.addPartExpense(expenseListEntity);
                    if (detail.getAgencyCode() != null && this.agencySettlement.getAgencyCode() == null) {
                        AgencyEntity agencyEntity = agencyService.findOneByCode(detail.getAgencyCode());
                        this.setAgency(agencyEntity);
                        this.agencySettlement.setAgencyCode(agencyEntity.getCode());
                        this.agencySettlement.setAgencyName(agencyEntity.getName());
                        if (agencyEntity.getProvince() != null) {
                            this.agencySettlement.setProvinceName(agencyEntity.getProvince().getName());
                        }
                    }

                }
                agencySettlement.setPartExpense(partExpense);
                agencySettlement.setFreight(freightExpense);
                agencySettlement.setOtherExpense(otherExpense);
                agencySettlement.setPunishmentExpense(0.0);
                agencySettlement.setRewardExpense(0.0);
                agencySettlement.setExpenseTotal(expenseTotal);
                this.setReadonly(true);
            } else {
                AgencyEntity agency = userService.findOne(CommonHelper.getActiveUser().getUserId()).getAgency();
                if (agency != null) {
                    this.agencySettlement.setAgencyCode(agency.getCode());
                    this.agencySettlement.setAgencyName(agency.getName());
                    this.agencySettlement.setProvinceName(agency.getProvince().getName());
                }
                this.setReadonly(false);
            }
        }

        this.setEntity(agencySettlement);
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

        List<UserEntity> list = userService.findAllByAgencyCode(this.agency.getCode());

        List<String> users = new ArrayList<>();
        for (UserEntity userEntity : list) {
            System.out.println(userEntity.getLogId());
            users.add(userEntity.getLogId());
        }

        variables.put("agencyUsers", users);
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
        if (agency != null) {
            if (agencySettlement.getItems().size() < 1) {
                ZkUtils.showInformation("请先获取供货单！", "提示");
                return;
            }
            //当主表非首次保存时，子表需要手工关联主表
            if (agencySettlement.getObjId() != null) {
                for (PartExpenseListEntity Item : agencySettlement.getItems()) {
                    Item.setAgencySettlement(agencySettlement);

                }
            }

            agencySettlement.setAgencyName(agency.getName());
            agencySettlement.setAgencyCode(agency.getCode());
            agencySettlement = (AgencySettlementEntity) this.saveEntity(agencySettlement);
            agencySettlement = agencySettlementService.findOneById(agencySettlement.getObjId());
            this.setEntity(agencySettlement);
            //清空单据对应的结算信息
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(agencySettlement.getObjId());
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


            for (PartExpenseListEntity part : agencySettlement.getItems()) {
                PendingSettlementDetailsEntity detail = pendingSettlementDetailsService.getOneBySrcDocID(part.getSrcDocID());
                detail.setSettlementDocID(agencySettlement.getObjId());
                detail.setSettlementDocNo(agencySettlement.getDocNo());
                detail.setDocNo(agencySettlement.getDocNo());
                detail.setSettlementStatus(DocStatus.SETTLING.getIndex());
                detail.setSettlement(true);
                detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.getRepository().save(detail);

            }

            showDialog();
        } else {
            ZkUtils.showInformation("请先选择经销商！", "提示");

        }
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgencies(@BindingParam("model") String keyword) {
        if (CommonHelper.getActiveUser().getAgency() != null) {   // 合作库用户
            this.agencies.clear();
            this.agencies.add(CommonHelper.getActiveUser().getAgency());
        } else if (CommonHelper.getActiveUser().getDealer() != null) {  // 服务站用户
//            this.dealers = dealerService.findAllByStatusAndKeyword("%" + keyword + "%");
        } else {   // 五菱用户
            this.agencies = agencyService.findAllByKeyword(keyword);
        }
    }

    @Command
    @NotifyChange("agency")
    public void clearSelectedAgency() {
        if (CommonHelper.getActiveUser().getAgency() != null) {
            this.agency = CommonHelper.getActiveUser().getAgency();
        } else {
            this.agency = null;
        }
        this.setKeyword("");
        this.agencySettlement.setProvinceName("");
        this.agencySettlement.setOperatorPhone("");
    }

    @Command
    @NotifyChange({"agency", "agencySettlement"})
    public void selectAgency(@BindingParam("model") AgencyEntity agency) {
        this.setKeyword("");
        this.agencies.clear();
        this.agency = agency;
        if (this.agency.getProvince() != null)
            this.agencySettlement.setProvinceName(this.agency.getProvince().getName());
        this.agencySettlement.setOperatorPhone(this.agency.getPhone());
    }

    @Command
    @NotifyChange("agencySettlement")
    public void changeExpense() {
        agencySettlement.setExpenseTotal(agencySettlement.getOtherExpense() + agencySettlement.getPartExpense() + agencySettlement.getFreight() - agencySettlement.getPunishmentExpense() + agencySettlement.getRewardExpense());
    }

    @Command
    @NotifyChange("agencySettlement")
    public void ReadItem() {
        double expenseTotal = 0.0;
        double freightExpense = 0.0;
        double otherExpense = 0.0;
        double partExpense = 0.0;
        double workingExpense = 0.0;
        //如果单据已保存再次取数据，需先清空单据对应的结算信息,再添加原有单据子行
        this.agencySettlement.getItems().clear();
        if (StringUtils.isNotBlank(agencySettlement.getObjId())) {
            List<PartExpenseListEntity> expenseListEntityList;
            expenseListEntityList = agencySettlementService.findPartExpenseListById(agencySettlement.getObjId());
            for (PartExpenseListEntity expenseList : expenseListEntityList) {
                this.agencySettlement.getItems().add(expenseList);
                expenseTotal = expenseTotal + expenseList.getExpenseTotal();
                freightExpense = freightExpense + expenseList.getFreightExpense();
                otherExpense = otherExpense + expenseList.getOtherExpense();
                partExpense = partExpense + expenseList.getPartExpense();
                workingExpense = workingExpense + expenseList.getWorkingExpense();
            }
        }

        if (agency != null) {
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getAgencySelttlements(agency.getCode(), agencySettlement.getStartDate(), agencySettlement.getEndDate());
            for (PendingSettlementDetailsEntity detail : details) {
                PartExpenseListEntity expenseListEntity = new PartExpenseListEntity();
                expenseListEntity.setExpenseTotal(detail.getExpenseTotal());
                expenseListEntity.setFreightExpense(detail.getFreightExpense());
                expenseListEntity.setOtherExpense(detail.getOtherExpense());
                expenseListEntity.setPartExpense(detail.getPartExpense());
                expenseListEntity.setWorkingExpense(detail.getWorkingExpense());
                expenseListEntity.setSrcDocID(detail.getSrcDocID());
                expenseListEntity.setSrcDocNo(detail.getSrcDocNo());
                expenseListEntity.setSrcDocType(detail.getSrcDocType());
                freightExpense = freightExpense + (expenseListEntity.getFreightExpense() == null ? 0.0 : expenseListEntity.getFreightExpense());
                otherExpense = otherExpense + (expenseListEntity.getOtherExpense() == null ? 0.0 : expenseListEntity.getOtherExpense());
                partExpense = partExpense + expenseListEntity.getPartExpense();
                workingExpense = workingExpense + expenseListEntity.getWorkingExpense();
                expenseTotal = expenseTotal + expenseListEntity.getExpenseTotal();
                this.agencySettlement.addPartExpense(expenseListEntity);
            }
            agencySettlement.setPartExpense(partExpense);
            agencySettlement.setFreight(freightExpense);
            agencySettlement.setOtherExpense(otherExpense);
            agencySettlement.setPunishmentExpense(0.0);
            agencySettlement.setRewardExpense(0.0);
            agencySettlement.setExpenseTotal(expenseTotal);
        } else {
            ZkUtils.showInformation("请先选择经销商！", "提示");
        }
    }

    @Command
    @NotifyChange("agencySettlement")
    public void deleteItem(@BindingParam("model") PartExpenseListEntity model) {
        //回写待结算列表
        PendingSettlementDetailsEntity detail = pendingSettlementDetailsService.getOneBySrcDocID(model.getSrcDocID());
        detail.setSettlementDocID(null);
        detail.setSettlementDocNo(null);
        detail.setSettlement(false);
        detail.setSettlementDocType("配件结算单");
        detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
        detail.setModifierId(CommonHelper.getActiveUser().getLogId());
        detail.setModifierName(CommonHelper.getActiveUser().getUsername());
        detail.setModifiedTime(new Date());
        pendingSettlementDetailsService.getRepository().save(detail);
        for (PartExpenseListEntity item : this.agencySettlement.getItems()) {
            if (item == model) {
                this.agencySettlement.setExpenseTotal(this.agencySettlement.getExpenseTotal() - model.getExpenseTotal());
                this.agencySettlement.setOtherExpense(this.agencySettlement.getOtherExpense() - model.getOtherExpense());
                this.agencySettlement.setPartExpense(this.agencySettlement.getPartExpense() - model.getPartExpense());
                this.agencySettlement.setFreight(this.agencySettlement.getFreight() - model.getFreightExpense());
                this.agencySettlement.getItems().remove(model);
                this.agencySettlementService.getRepository().save(this.agencySettlement);
                return;
            }
        }
    }

    @Override
    @Command
    public void printReport() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.agencySettlement.getObjId() == null ? "" : this.agencySettlement.getObjId());
        window = (Window) ZkUtils.createComponents("/views/report/settlement/agency_settlement_report.zul", null, map);
        window.setTitle("打印报表");
        window.doModal();
    }

    @Command
    public void printDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("objId", this.agencySettlement.getObjId() == null ? "" : this.agencySettlement.getObjId());
        map.put("startDate", dateToString(this.agencySettlement.getStartDate()));
        map.put("endDate", dateToString(this.agencySettlement.getEndDate()));

        window = (Window) ZkUtils.createComponents("/views/report/settlement/agency_settlement_detail.zul", null, map);
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
        if (this.agencySettlement.getStatus().equals(DocStatus.CLOSED.getIndex())) {
            return true;
        }
        return false;
    }

    @Command
    @NotifyChange("*")
    @Override
    public void desert() {
        super.desert();
        List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(this.agencySettlement.getObjId());
        if (details != null) {
            for (PendingSettlementDetailsEntity detail : details) {
                detail.setSettlementDocID(null);
                detail.setSettlementDocNo(null);
                detail.setSettlement(false);
                detail.setSettlementDocType("配件结算单");
                detail.setSettlementStatus(DocStatus.WAITING_SETTLE.getIndex());
                detail.setModifierId(CommonHelper.getActiveUser().getLogId());
                detail.setModifierName(CommonHelper.getActiveUser().getUsername());
                detail.setModifiedTime(new Date());
                pendingSettlementDetailsService.getRepository().save(detail);
            }
        }
    }

    public AgencySettlementEntity getAgencySettlement() {
        return agencySettlement;
    }

    public void setAgencySettlement(AgencySettlementEntity agencySettlement) {
        this.agencySettlement = agencySettlement;
    }

    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    public List<AgencyEntity> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<AgencyEntity> agencies) {
        this.agencies = agencies;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


}
