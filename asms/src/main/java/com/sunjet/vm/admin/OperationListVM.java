package com.sunjet.vm.admin;

import com.sunjet.model.admin.OperationEntity;
import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.service.admin.OperationService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class OperationListVM extends ListBaseVM {

    @WireVariable
    private OperationService operationService;

    @Init
    public void init() {
        this.setBaseService(operationService);
        this.setFormUrl("/views/admin/operation_form.zul");
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }

    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("seq", SearchOrder.OrderType.ASC, 1)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<OperationEntity> specification = new Specification<OperationEntity>() {
                @Override
                public Predicate toPredicate(Root<OperationEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p01 = CustomRestrictions.like("optCode", getKeyword(), true).toPredicate(root, query, cb);
                    Predicate p02 = CustomRestrictions.like("optName", getKeyword(), true).toPredicate(root, query, cb);
                    Predicate p = cb.or(p01, p02);
                    return p;
                }
            };
            searchDTO.setSpecification(specification);
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_OPERATION_LIST)
    @NotifyChange("resultDTO")
    public void refreshResourceList() {
        this.refreshList();
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_OPERATION_ENTITY)
    @NotifyChange("*")
    public void refreshResourceEntity(@BindingParam("entity") DocEntity entity) {
        this.refreshEntity(entity);
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    @Command
    @NotifyChange("resultDTO")
    public void deleteEntity(@BindingParam("entity") OperationEntity entity) {
        ZkUtils.showQuestion("【谨慎!】您正准备删除操作【" + entity.getOptName() + "】!" +
                "\r\n\r\n删除操作将会同时删除该操作对应的访问列表," +
                "\r\n\r\n是否确定删除?", "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    operationService.delete(entity);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_OPERATION_LIST, null);
                }
            }
        });
    }
}
