package com.sunjet.vm.admin;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.base.DocEntity;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.admin.UserService;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class UserListVM extends ListBaseVM {
    @WireVariable
    private UserService userService;
    @WireVariable
    private RoleService roleService;

    private String name = "";
    private String logId = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    @Init
    public void init() {
        this.setBaseService(userService);
        this.setKeyword("");
        this.setFormUrl("/views/admin/user_form.zul");
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
//        new SearchOrder("name", SearchOrder.OrderType.ASC, 2)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<UserEntity> specification = (root, query, cb) -> {
                Predicate p01 = CustomRestrictions.like("name", getKeyword(), true).toPredicate(root, query, cb);
                Predicate p02 = CustomRestrictions.like("logId", getKeyword(), true).toPredicate(root, query, cb);
                Predicate p = cb.or(p01, p02);
                return p;
            };
            searchDTO.setSpecification(specification);
        }
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_USER_LIST)
    @NotifyChange("resultDTO")
    public void refreshUserList() {
        this.refreshList();
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_USER_ENTITY)
    @NotifyChange("*")
    public void refreshUserEntity(@BindingParam("entity") DocEntity entity) {
        this.refreshEntity(entity);
    }

    /**
     * 删除用户信息
     *
     * @param userEntity
     * @return
     */
    @Command
    @NotifyChange("resultDTO")
    public void deleteEntity(@BindingParam("entity") UserEntity userEntity) {
        ZkUtils.showQuestion("您确定删除用户【" + userEntity.getName() + "】？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                // 用户点击的是确定按钮
                userService.delete(userEntity.getObjId());
//                roleService.updateRoles();
//                initList();
//                Events.postEvent("onClick", btnRefreshUser, null);
                BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_USER_LIST, null);
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });
    }
}
