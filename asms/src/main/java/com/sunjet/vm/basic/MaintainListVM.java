package com.sunjet.vm.basic;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.MaintainEntity;
import com.sunjet.service.basic.MaintainService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.ListBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * 维修项目 列表
 * Created by Administrator on 2016/9/12.
 */
public class MaintainListVM extends ListBaseVM {
    @WireVariable
    private MaintainService maintainService;
    private String maintainCode;
    private String maintainName;

    public String getMaintainCode() {
        return maintainCode;
    }

    public void setMaintainCode(String maintainCode) {
        this.maintainCode = maintainCode;
    }

    public String getMaintainName() {
        return maintainName;
    }

    public void setMaintainName(String maintainName) {
        this.maintainName = maintainName;
    }

    @Init
    public void init() {
        this.setBaseService(maintainService);
        this.setFormUrl("/views/basic/maintain_form.zul");
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

//    @Command
//    @GlobalCommand(GlobalCommandValues.REFRESH_MAINTAINS)
//    @NotifyChange("*")
//    public void refreshData() {
//        System.out.println("调用全部方法");
//        if (dialog != null) {
//            dialog.detach();
//        }
//        initList();
//    }


    @Command
    @NotifyChange("*")
    public void deleteEntity(@BindingParam("entity") MaintainEntity entity) {
        maintainService.deleteMaintain(entity);
        initList();
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求
     */
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
        Specification<MaintainEntity> specification = (root, query, cb) -> {
            Predicate p = null;
            // 服务站编号
            if (StringUtils.isNotBlank(this.maintainCode)) {
                p = CustomRestrictions.like("code", this.maintainCode, true).toPredicate(root, query, cb);
//                p = cb.and(p,p01);
            }
            // 服务站名称
            if (StringUtils.isNotBlank(this.maintainName)) {
                if (p == null) {
                    p = CustomRestrictions.like("name", this.maintainName, true).toPredicate(root, query, cb);
                } else {
                    p = cb.and(p, CustomRestrictions.like("name", this.maintainName, true).toPredicate(root, query, cb));
                }
            }

//        if(!this.selectedProvince.getName().equalsIgnoreCase("全部")){
//
//        }

            return p;
        };
        searchDTO.setSpecification(specification);
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_MAINTAIN_LIST)
    @NotifyChange("resultDTO")
    public void refreshResourceList() {
        this.refreshList();
    }

    @GlobalCommand(GlobalCommandValues.REFRESH_MAINTAIN_ENTITY)
    @NotifyChange("*")
    public void refreshResourceEntity(@BindingParam("entity") DocEntity entity) {
        this.refreshEntity(entity);
    }

    @Command
    @NotifyChange({"maintainCode", "maintainName"})
    public void reset() {
        this.maintainCode = "";
        this.maintainName = "";
    }

}
