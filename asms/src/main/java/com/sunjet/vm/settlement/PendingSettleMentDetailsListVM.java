package com.sunjet.vm.settlement;

import com.sunjet.model.asm.PendingSettlementDetailsEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.asm.AgencySettlementService;
import com.sunjet.service.asm.PendingSettlementDetailsService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 待结算单据列表
 * Created by zyh on 2016/11/14.
 */
public class PendingSettleMentDetailsListVM extends FlowListBaseVM {
    @WireVariable
    private AgencySettlementService agencySettlementService;
    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    private String doctype = "";
    private boolean settlement = false;
    private List<PendingSettlementDetailsEntity> pendingSettlementService = new ArrayList<PendingSettlementDetailsEntity>();
    private List<PendingSettlementDetailsEntity> pendingSettlementPart = new ArrayList<PendingSettlementDetailsEntity>();
    private List<PendingSettlementDetailsEntity> pendingSettlementFreight = new ArrayList<PendingSettlementDetailsEntity>();

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(2);
        doctype = Executions.getCurrent().getArg().get("doctype").toString();
        this.setEnableAdd(false);

        this.setDocumentStatuses(DocStatus.getListSettlementStatus());

        if (doctype != null) {
            if (doctype.contains("服务结算单") && hasPermission("FWPendingEntity:create")) {
                this.setEnableWarrantSettlement(true);
            }
            if (doctype.contains("运费结算单") && hasPermission("FreightPendingEntity:create")) {
                this.setEnableFreightSettlement(true);
            }
            if (doctype.contains("配件结算单") && hasPermission("PartPendingEntity:create")) {
                this.setEnablePartSettlement(true);
            }
        }

        this.setBaseService(pendingSettlementDetailsService);
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }

    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void search() {
        pendingSettlementService.clear();
        pendingSettlementFreight.clear();
        pendingSettlementPart.clear();
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
        } else {
            filterList();
        }
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     * @param searchDTO
     */
    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("createdTime", SearchOrder.OrderType.DESC, 1)
//        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<PendingSettlementDetailsEntity> specification = (root, query, cb) -> {
            Predicate p01 = CustomRestrictions.gte("businessDate", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p02 = CustomRestrictions.lt("businessDate", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
//            Predicate p03 = CustomRestrictions.eq("settlement", settlement, true).toPredicate(root, query, cb);

//            Predicate p = cb.and(p01, p02, p03);
            Predicate p = cb.and(p01, p02);


            // 结算单类型
            if (this.doctype != null) {
                if (this.doctype.length() > 0) {
                    Predicate p00 = CustomRestrictions.eq("settlementDocType", this.doctype.trim(), true).toPredicate(root, query, cb);
                    p = cb.and(p, p00);
                }
            }
            // 服务站
            if (this.getDealer() != null) {
                Predicate p04 = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p04);
            }

            //单据编号
            if (StringUtils.isNotBlank(this.getDocNo())) {
                Predicate p05 = CustomRestrictions.like("srcDocNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p05);
            }
            // 经销商
            if (this.getAgency() != null) {
                Predicate p06 = CustomRestrictions.eq("agencyCode", this.getAgency().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            System.out.println("单据状态 : " + this.getSelectedStatus().getName());
            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p07 = CustomRestrictions.eq("settlementStatus", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p07);
            }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange("resultDTO")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        if (flowEntity.getStatus().equals(0)) {
            List<PendingSettlementDetailsEntity> details = pendingSettlementDetailsService.getOneBySettlementID(flowEntity.getObjId());
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

            this.getBaseService().getRepository().delete(flowEntity.getObjId());
            initList();
        } else {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
        }
    }

    public boolean isSettlement() {
        return settlement;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }


    @Command
    public void selectPendingSettlementService(@BindingParam("model") PendingSettlementDetailsEntity Item, @BindingParam("check") boolean check) {
        if (this.getDealer() == null) {
            ZkUtils.showInformation("请先选择服务站查询数据", "提示");
            return;
        }
        if (check && pendingSettlementService != null) {
            if (this.pendingSettlementService.size() > 0) {
                for (PendingSettlementDetailsEntity detail : this.pendingSettlementService) {
                    if (!detail.getDealerCode().equals(Item.getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个服务站的单据", "提示");
                        return;
                    }
                }
                this.pendingSettlementService.add(Item);
            } else {
                this.pendingSettlementService.add(Item);
            }

        } else {
            this.pendingSettlementService.remove(Item);
        }
    }

    @Command
    public void createWarrantSettlement() {
        if (pendingSettlementService.isEmpty()) {
            ZkUtils.showInformation("请选择待结算清单！", "提示");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("pendingSettlement", pendingSettlementService);
            Window window = (Window) ZkUtils.createComponents("views/settlement/warrant_maintenance_form.zul", null, map);
            window.doModal();
        }

    }

    @Command
    public void selectPendingSettlementFreight(@BindingParam("model") PendingSettlementDetailsEntity Item, @BindingParam("check") boolean check) {
        if (this.getDealer() == null) {
            ZkUtils.showInformation("请先选择服务站查询数据", "提示");
            return;
        }
        if (check && pendingSettlementFreight != null) {
            if (this.pendingSettlementFreight.size() > 0) {
                for (PendingSettlementDetailsEntity detail : this.pendingSettlementFreight) {
                    if (!detail.getDealerCode().equals(Item.getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个服务站的单据", "提示");
                        return;
                    }
                }
                this.pendingSettlementFreight.add(Item);
            } else {
                this.pendingSettlementFreight.add(Item);
            }

        } else {
            this.pendingSettlementFreight.remove(Item);
        }
    }

    @Command
    public void createFreightSettlement() {

        if (pendingSettlementFreight.isEmpty()) {
            ZkUtils.showInformation("请选择待结算清单！", "提示");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("pendingSettlement", pendingSettlementFreight);
            Window window = (Window) ZkUtils.createComponents("views/settlement/freight_cost_form.zul", null, map);
            window.doModal();
        }

    }

    @Command
    public void selectPendingSettlementPart(@BindingParam("model") PendingSettlementDetailsEntity Item, @BindingParam("check") boolean check) {
        if (this.getAgency() == null) {
            ZkUtils.showInformation("请先选择合作经销商查询数据", "提示");
            return;
        }
        if (check && pendingSettlementPart != null) {
            if (this.pendingSettlementService.size() > 0) {
                for (PendingSettlementDetailsEntity detail : this.pendingSettlementPart) {
                    if (!detail.getDealerCode().equals(Item.getDealerCode())) {
                        ZkUtils.showInformation("请选择同一个合作经销商的单据", "提示");
                        return;
                    }
                }
                this.pendingSettlementPart.add(Item);
            } else {
                this.pendingSettlementPart.add(Item);
            }

        } else {
            this.pendingSettlementPart.remove(Item);
        }
    }

    @Command
    public void createPartSettlement() {

        if (pendingSettlementPart.isEmpty()) {
            ZkUtils.showInformation("请选择待结算清单！", "提示");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("pendingSettlement", pendingSettlementPart);
            Window window = (Window) ZkUtils.createComponents("views/settlement/part_cost_form.zul", null, map);
            window.doModal();
        }

    }


    public Boolean chkStatus(Integer statusIdx) {
        if (statusIdx == null) {
            return false;
        } else if (statusIdx == DocStatus.WAITING_SETTLE.getIndex()) {
            return false;
        } else if (statusIdx == DocStatus.SETTLING.getIndex()) {
            return true;
        } else if (statusIdx == DocStatus.SETTLED.getIndex()) {
            return true;
        } else {
            return false;
        }
    }

}
