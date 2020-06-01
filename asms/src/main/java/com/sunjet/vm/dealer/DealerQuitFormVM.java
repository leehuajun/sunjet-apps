package com.sunjet.vm.dealer;

import com.sunjet.model.dealer.DealerQuitRequestEntity;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.basic.RegionService;
import com.sunjet.service.dealer.DealerQuitService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DealerQuitFormVM extends FlowFormBaseVM {
    @WireVariable
    private DealerQuitService dealerQuitService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private DealerService dealerService;

    private DealerQuitRequestEntity dealerQuitRequestEntity;
    private Map<String, Object> variables = new HashMap<>();


    public DealerQuitRequestEntity getDealerQuitRequestEntity() {
        return dealerQuitRequestEntity;
    }

    public void setDealerQuitRequestEntity(DealerQuitRequestEntity dealerQuitRequestEntity) {

        this.dealerQuitRequestEntity = dealerQuitRequestEntity;
    }

    @Init(superclass = true)
    public void init() {
        this.setBaseService(dealerQuitService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.dealerQuitRequestEntity = dealerQuitService.findOneById(this.getBusinessId());
            this.setDealer(dealerQuitRequestEntity.getDealer());
        } else {
            this.dealerQuitRequestEntity = new DealerQuitRequestEntity();
            this.dealerQuitRequestEntity.setDealer(this.getDealer());
        }
        this.setEntity(this.dealerQuitRequestEntity);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
//        if (win != null) {
//            win.setTitle(win.getTitle() + titleMsg);
//        }
    }

    @Override
    protected Boolean checkValid() {
        if (StringUtils.isBlank(this.dealerQuitRequestEntity.getReason())) {
            ZkUtils.showError("请输入退出原因！", "系统提示");
            return false;
        }
        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
        this.dealerQuitRequestEntity.setDealer(this.getDealer());

        variables.put("serviceManager", this.dealerQuitRequestEntity.getDealer().getServiceManager().getLogId());
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    startProcessInstance(variables);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                } else {
                    return;
                }
            }
        });
    }

    /**
     * 保存对象
     */
    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
        if (this.getDealer() == null) {
            ZkUtils.showExclamation("服务站不能为空！", "系统提示");
            return;
        }
        if (StringUtils.isBlank(this.dealerQuitRequestEntity.getReason())) {
            ZkUtils.showExclamation("请填写退出原因！", "系统提示");
            return;
        }
        this.dealerQuitRequestEntity.setDealer(this.getDealer());
        this.setEntity(this.saveEntity(this.dealerQuitRequestEntity));
        showDialog();
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

}