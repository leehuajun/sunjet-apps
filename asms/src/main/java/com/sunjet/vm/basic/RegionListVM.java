package com.sunjet.vm.basic;

import com.sunjet.service.admin.RoleService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.vm.base.ListBaseVM;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Arrays;

/**
 * 区域 列表
 * Created by Administrator on 2016/9/12.
 */
public class RegionListVM extends ListBaseVM {
    @WireVariable
    private RegionService provinceService;
    @WireVariable
    private RoleService roleService;

    @Init
    public void init() {
        this.setBaseService(provinceService);
        this.setFormUrl("/views/basic/province_form.zul");
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
}
