package com.sunjet.vm.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.service.asm.ActivityNoticeService;
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
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 服务活动通知单 列表 VM
 */
public class ActivityNoticeListVM extends FlowListBaseVM {
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Init(superclass = true)
    public void init() {
        this.setHeaderRows(1);   // 设置搜索栏的行数，默认是1行
        this.setFormUrl("/views/asm/activity_notice_form.zul");
        this.setBaseService(activityNoticeService);
        this.setEnableDelete(hasPermission(ActivityNoticeEntity.class.getSimpleName() + ":delete"));
        this.setEnableAdd(hasPermission(ActivityNoticeEntity.class.getSimpleName() + ":create"));
        this.setEnableUpdate(hasPermission(ActivityNoticeEntity.class.getSimpleName() + ":modify"));

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
        Specification<ActivityNoticeEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("publishDate", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("publishDate", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);
            Predicate p07 = CustomRestrictions.ne("status", DocStatus.OBSOLETE.getIndex(), true).toPredicate(root, query, cb);
            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p04, p05, p07);
            // 标题
            if (StringUtils.isNotBlank(this.title)) {
                Predicate p01 = CustomRestrictions.like("title", this.title.trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p01);
            }
//            // 活动类型
//            if (StringUtils.isNotBlank(this.activityNoticeRequest.getActivityType())) {
//                Predicate p02 = CustomRestrictions.like("activityType", this.activityNoticeRequest.getActivityType(), true).toPredicate(root, query, cb);
//                p = cb.and(p, p02);
//            }
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

    @Command
    @NotifyChange("*")
    public void reset() {
        this.setDocNo("");
        this.title = "";
        this.setSelectedStatus(DocStatus.ALL);
        this.setEndDate(new Date());
        this.setStartDate(DateHelper.getFirstOfMonth());
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_ACTIVITY_NOTION_LIST)
    @NotifyChange("*")
    public void activityNoticeRefresh() {
        initList();
    }

}