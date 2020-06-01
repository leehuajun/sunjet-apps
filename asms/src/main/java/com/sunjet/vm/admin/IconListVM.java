package com.sunjet.vm.admin;

import com.sunjet.model.admin.IconEntity;
import com.sunjet.service.admin.IconService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Grid;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class IconListVM extends ListBaseVM {

    @WireVariable
    private IconService iconService;

    @Init
    public void init() {
        this.setBaseService(iconService);
        this.setKeyword("");
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
                new SearchOrder("name", SearchOrder.OrderType.ASC, 1)
        ));
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
     */
    protected void configSpecification(SearchDTO searchDTO) {
        if (!this.getKeyword().equals("")) {
            Specification<IconEntity> specification = new Specification<IconEntity>() {
                @Override
                public Predicate toPredicate(Root<IconEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate p01 = CustomRestrictions.like("name", getKeyword(), true).toPredicate(root, query, cb);
//                    Predicate p02 = CustomRestrictions.like("logId",getKeyword(),true).toPredicate(root,query,cb);
//                    Predicate p = cb.or(p01,p02);
                    return p01;
                }
            };
            searchDTO.setSpecification(specification);
        }
    }
}
