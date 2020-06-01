package com.sunjet.vm.admin;

import com.sunjet.service.admin.WebServiceAccessLogService;
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
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class WebServiceLogListVM extends ListBaseVM {

    @WireVariable
    private WebServiceAccessLogService webServiceAccessLogService;

    @Init
    public void init() {
        this.setBaseService(webServiceAccessLogService);
        this.setKeyword("");
        this.setFormUrl("/views/admin/ws_access_log_form.zul");
//    this.modelName = ConfigEntity.class.getSimpleName();
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
                new SearchOrder("beginTime", SearchOrder.OrderType.DESC, 1)
        ));
    }
//
//  /***
//   * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询要求
//   */
//  protected void configSpecification(SearchDTO searchDTO) {
//    if (!this.getKeyword().equals("")) {
//      Specification<ConfigEntity> specification = new Specification<ConfigEntity>() {
//        @Override
//        public Predicate toPredicate(Root<ConfigEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//          Predicate p01 = CustomRestrictions.like("configKey", getKeyword(), true).toPredicate(root, query, cb);
////                    Predicate p02 = CustomRestrictions.like("logId",getKeyword(),true).toPredicate(root,query,cb);
////                    Predicate p = cb.or(p01,p02);
//          return p01;
//        }
//      };
//      searchDTO.setSpecification(specification);
//    }
//  }
//

}
