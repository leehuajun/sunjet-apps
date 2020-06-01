package com.sunjet.vm.asm;

import com.sunjet.model.asm.RecycleEntity;
import com.sunjet.model.asm.RecycleItemEntity;
import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.service.asm.RecycleNoticeItemService;
import com.sunjet.service.asm.RecycleService;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p/>
 * 故障件返回单 列表 VM
 */
public class RecycleListVM extends FlowListBaseVM {
    @WireVariable
    private RecycleService recycleService;

    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;

    @Init(superclass = true)
    public void init() {

        this.setHeaderRows(2);   // 设置搜索栏的行数，默认是1行
        this.setFormUrl("/views/asm/recycle_form.zul");
        this.setBaseService(recycleService);
        this.setEnableDelete(hasPermission(RecycleEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(RecycleEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(RecycleEntity.class.getSimpleName() + ":modify"));

        this.setDocumentStatuses(DocStatus.getListWithAllNotSettlement());
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        initList();
    }


    @Command
    @NotifyChange({"resultDTO", "currentPageNo"})
    public void search() {
        if (this.getEndDate().getTime() <= this.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
        } else {
            filterList();
        }
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     *
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
     *
     * @param searchDTO
     */
    @Override
    protected void configSpecification(SearchDTO searchDTO) {
        Specification<RecycleEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p07 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p04, p05, p07);
            // 服务站编号
            if (this.getDealer() != null) {
                Predicate p01 = CustomRestrictions.eq("dealerCode", this.getDealer().getCode().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p01);
            }
            //单据编号
            if (StringUtils.isNotBlank(this.getDocNo())) {
                Predicate p06 = CustomRestrictions.like("docNo", this.getDocNo().trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p06);
            }
            // 状态
            if (this.getSelectedStatus() != DocStatus.ALL) {
                Predicate p03 = CustomRestrictions.eq("status", this.getSelectedStatus().getIndex(), true).toPredicate(root, query, cb);
                p = cb.and(p, p03);
            }
            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void deleteFlowEntity(@BindingParam("entity") FlowDocEntity flowEntity) {
        if (!flowEntity.getStatus().equals(0)) {
            ZkUtils.showInformation("非草稿单据不能删除！", "提示");
            return;
        }
        RecycleEntity recycleEntity = recycleService.findOneWithItems(flowEntity.getObjId());
        for (RecycleItemEntity item : recycleEntity.getItems()) {
            RecycleNoticeItemEntity noticeItem = item.getRecycleNoticeItem();
            noticeItem.setBackAmount(noticeItem.getBackAmount() - item.getBackAmount());
            noticeItem.setCurrentAmount(noticeItem.getAmount() - noticeItem.getBackAmount());
            recycleNoticeItemService.getRepository().save(noticeItem);

        }

        super.deleteFlowEntity(flowEntity);

        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_SUPPLY_WAITING, null);

    }
}
