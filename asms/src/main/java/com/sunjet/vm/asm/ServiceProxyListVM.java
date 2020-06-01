package com.sunjet.vm.asm;

import com.sunjet.model.asm.ServiceProxyEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.service.asm.ServiceProxyService;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.DocStatus;
import com.sunjet.vm.base.FlowListBaseVM;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
 * 服务委托单 列表 VM
 */
public class ServiceProxyListVM extends FlowListBaseVM {
    @WireVariable
    private ServiceProxyService serviceProxyService;
    private ServiceProxyEntity serviceProxyRequest;

    private String dealerCode = "";
    private String dealerName = "";
    private String activitydocNo = "";
    private String creator = "";
    private Boolean creatorReadonly = true;

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getActivitydocNo() {
        return activitydocNo;
    }

    public void setActivitydocNo(String activitydocNo) {
        this.activitydocNo = activitydocNo;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getCreatorReadonly() {
        return creatorReadonly;
    }

    public void setCreatorReadonly(Boolean creatorReadonly) {
        this.creatorReadonly = creatorReadonly;
    }

    public ServiceProxyEntity getServiceProxyRequest() {
        return serviceProxyRequest;
    }

    public void setServiceProxyRequest(ServiceProxyEntity serviceProxyRequest) {
        this.serviceProxyRequest = serviceProxyRequest;
    }

    @Init(superclass = true)
    public void init() {
        this.setFormUrl("/views/asm/service_proxy_form.zul");

        this.setBaseService(serviceProxyService);
        this.dealerCode = "";
        this.dealerName = "";
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        // 如果是admin，则可以查看全部
        if (activeUser.getLogId().equalsIgnoreCase("model")) {
            creatorReadonly = false;
        }
        creator = activeUser.getUsername();
        initList();

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

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
        Specification<ServiceProxyEntity> specification = (root, query, cb) -> {
            Predicate p04 = CustomRestrictions.gte("createdTime", DateHelper.getStartDate(this.getStartDate()), true).toPredicate(root, query, cb);
            Predicate p05 = CustomRestrictions.lt("createdTime", DateHelper.getEndDate(this.getEndDate()), true).toPredicate(root, query, cb);

            logger.info("开始日期:" + DateHelper.getStartDate(this.getStartDate()));
            logger.info("结束日期:" + DateHelper.getEndDate(this.getEndDate()));

            Predicate p = cb.and(p04, p05);
            // 服务站编号
            if (!this.dealerCode.isEmpty()) {
                Predicate p01 = CustomRestrictions.like("dealerCode", this.dealerCode.trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p01);
            }
            // 服务站名称
            if (!this.dealerName.isEmpty()) {
                Predicate p02 = CustomRestrictions.like("dealerName", this.dealerName.trim(), true).toPredicate(root, query, cb);
                p = cb.and(p, p02);
            }
            //单据编号
            if (!this.activitydocNo.isEmpty()) {
                Predicate p06 = CustomRestrictions.like("docNo", this.activitydocNo.trim(), true).toPredicate(root, query, cb);
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
        dealerName = "";
        dealerCode = "";
        activitydocNo = "";
        this.setSelectedStatus(DocStatus.ALL);
        this.setEndDate(new Date());
        this.setStartDate(DateHelper.getFirstOfMonth());
    }
}
