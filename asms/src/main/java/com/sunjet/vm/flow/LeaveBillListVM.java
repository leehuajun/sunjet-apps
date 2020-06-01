package com.sunjet.vm.flow;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.service.flow.LeaveBillService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.dto.SearchDTO;
import com.sunjet.utils.dto.custom.CustomRestrictions;
import com.sunjet.utils.dto.custom.SearchOrder;
import com.sunjet.vm.base.FlowListBaseVM;
import org.springframework.data.jpa.domain.Specification;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;

/**
 * Created by lhj on 16/10/17.
 */
public class LeaveBillListVM extends FlowListBaseVM {

    @WireVariable
    private LeaveBillService leaveBillService;

    private Window win;

    @Init
    public void init() {
        this.setBaseService(leaveBillService);
        this.setKeyword("");
        this.setFormUrl("/views/flow/leavebill_form.zul");
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Override
    public void listBaseInit() {
        super.listBaseInit();
    }

    /***
     * 继承类可以根据需要进行重写该方法,实现各继承类个性化查询排序要求

     * @param searchDTO
     */
    @Override
    protected void configSearchOrder(SearchDTO searchDTO) {
        super.configSearchOrder(searchDTO);
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
        Specification<UserEntity> specification = (root, query, cb) -> {
            Predicate p01 = CustomRestrictions.eq("submitter", CommonHelper.getActiveUser().getLogId(), true).toPredicate(root, query, cb);
            return p01;
        };
        searchDTO.setSpecification(specification);
    }

//    @Command
//    public void handleTask(@BindingParam("entity") LeaveBill leaveBill) throws TabDuplicateException {
//        Map<String, Object> map = new HashMap<>();
//        String tabId = UUIDHelper.newUuid();
//        String tabName = "请假申请单";
//        if (leaveBill == null) {   // 新增
//            map.put("status", 0);
//        } else {
//            // 0:草稿  1:审核中  2:已审核  3:正常关闭  -1:异常关闭
//            map.put("status", leaveBill.getStatus());
//            map.put("businessId", leaveBill.getObjId());
//            tabId = leaveBill.getObjId();
//        }
//
//        String url = "/views/flow/leavebill_form.zul";
//
//        win = (Window) ZkUtils.createComponents(url, null, map);
////        window.setId(UUIDHelper.newUuid());
//        win.doModal();
//
////        ZkTabboxUtil.newTab(tabId, tabName, null, true, ZkTabboxUtil.OverFlowType.AUTO, url, map);
//
//    }

//    @GlobalCommand(GlobalCommandValues.LIST_TASK)
//    @NotifyChange("*")
//    public void listTask() {
//        if (win != null) {
//            win.detach();
//        }
//        initList();
//    }
}
