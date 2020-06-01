package com.sunjet.vm.admin;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.service.admin.PermissionService;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.admin.UserService;
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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class RoleListVM extends ListBaseVM {
    @WireVariable
    private RoleService roleService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private PermissionService permissionService;

    @Wire("#btnRefreshRole")
    private Button btnRefreshRole;

    @Init
    public void init() {
        this.setBaseService(roleService);
        this.setKeyword("");
        this.setFormUrl("/views/admin/role_form.zul");
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
                new SearchOrder("roleId", SearchOrder.OrderType.ASC, 1)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<RoleEntity> specification = new Specification<RoleEntity>() {
                @Override
                public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p01 = CustomRestrictions.like("name", getKeyword(), true).toPredicate(root, query, cb);
//                    Predicate p02 = CustomRestrictions.like("logId", getKeyword(), true).toPredicate(root, query, cb);
//                    Predicate p = cb.or(p01, p02);
                    return p01;
                }
            };
            searchDTO.setSpecification(specification);
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_ROLE_LIST)
    @NotifyChange("resultDTO")
    public void refreshRoleList() {
        this.refreshList();
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_ROLE_ENTITY)
    @NotifyChange("*")
    public void refreshRoleEntity(@BindingParam("entity") DocEntity entity) {
        this.refreshEntity(entity);
    }

    @Command
    @NotifyChange("resultDTO")
    public void deleteEntity(@BindingParam("entity") RoleEntity roleEntity) {
        ZkUtils.showQuestion("您确定删除角色【" + roleEntity.getName() + "】？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                // 用户点击的是确定按钮

                roleService.delete(roleEntity.getObjId());
                List<UserEntity> users = userService.findAllWithRoles();
                for (UserEntity entity : users) {
                    userService.save(entity);
                }
//                initList();
//                Events.postEvent("onClick", btnRefreshRole, null);
                BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_ROLE_LIST, null);
            }
        });
    }

    private void updateUsers() {
        List<UserEntity> users = userService.findAllWithRoles();
        for (UserEntity entity : users) {
            userService.save(entity);
        }
    }
}