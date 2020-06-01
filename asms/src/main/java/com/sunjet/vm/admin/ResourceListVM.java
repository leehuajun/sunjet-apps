package com.sunjet.vm.admin;

import com.sunjet.model.admin.OperationEntity;
import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.service.admin.PermissionService;
import com.sunjet.service.admin.ResourceService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ResourceListVM extends ListBaseVM {

    @WireVariable
    private ResourceService resourceService;
    @WireVariable
    private PermissionService permissionService;

    @Init
    public void init() {
        this.setBaseService(resourceService);
        this.setFormUrl("/views/admin/resource_form.zul");
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     */
    protected void configSearchOrder(SearchDTO searchDTO) {
        // 如果查询排序条件不为空,则把该 查询排序列表 赋给 searchDTO 查询对象.
        searchDTO.setSearchOrderList(Arrays.asList(
                new SearchOrder("createdTime", SearchOrder.OrderType.ASC, 1)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<ResourceEntity> specification = new Specification<ResourceEntity>() {
                @Override
                public Predicate toPredicate(Root<ResourceEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p01 = CustomRestrictions.like("code", getKeyword(), true).toPredicate(root, query, cb);
                    Predicate p02 = CustomRestrictions.like("name", getKeyword(), true).toPredicate(root, query, cb);
                    Predicate p = cb.or(p01, p02);
                    return p;
                }
            };
            searchDTO.setSpecification(specification);
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_RESOURCE_LIST)
    @NotifyChange("resultDTO")
    public void refreshResourceList() {
        this.refreshList();
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_RESOURCE_ENTITY)
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
    public void deleteEntity(@BindingParam("entity") ResourceEntity entity) {
        ZkUtils.showQuestion("【谨慎!】您正准备删除资源资源【" + entity.getName() + "】!" +
                "\r\n\r\n删除资源将会同时删除该资源对应的访问列表," +
                "\r\n同时也清除所有角色拥有对该资源的访问权限。" +
                "\r\n\r\n是否确定删除?", "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    resourceService.delete(entity);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RESOURCE_LIST, null);
                }
            }
        });
    }
}
