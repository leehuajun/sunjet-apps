package com.sunjet.vm.agency;

import com.sunjet.model.agency.AgencyQuitRequestEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.service.agency.AgencyQuitService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * Created by Administrator on 2016/9/8.
 */
public class AgencyQuitFormVM extends FlowFormBaseVM {
    private String keyword;

    @WireVariable
    private AgencyQuitService agencyQuitService;
    @WireVariable
    private AgencyService agencyService;

    private AgencyQuitRequestEntity agencyQuitRequest;

    public AgencyQuitRequestEntity getAgencyQuitRequest() {
        return agencyQuitRequest;
    }

    public void setAgencyQuitRequest(AgencyQuitRequestEntity agencyQuitRequest) {
        this.agencyQuitRequest = agencyQuitRequest;
    }

    @Init(superclass = true)
    public void init() {
        this.setBaseService(agencyQuitService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {
            this.agencyQuitRequest = agencyQuitService.findOneById(this.getBusinessId());
            this.setAgency(this.agencyQuitRequest.getAgency());
        } else {
            this.agencyQuitRequest = new AgencyQuitRequestEntity();
            this.agencyQuitRequest.setAgency(this.getAgency());

        }
        this.setEntity(this.agencyQuitRequest);
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
        if (this.agencyQuitRequest.getAgency() == null) {
            ZkUtils.showExclamation("合作商不能为空！", "系统提示");
            return false;
        }
        if (StringUtils.isBlank(this.agencyQuitRequest.getReason())) {
            ZkUtils.showExclamation("请填写退出原因！", "系统提示");
            return false;
        }
        return true;
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
//        this.agencyAdmitRequest = (AgencyAdmitRequestEntity) this.getFlowDocEntity();
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    startProcessInstance(null);
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);

                } else {
                    return;
                }
            }
        });
    }

    @Override
    @Command
    @NotifyChange("*")
    public void saveFlowDocEntity() {
//        AgencyEntity agencyEntity = agencyAlterRequest.getAgency();
//        agencyQuitService.save(this.agencyQuitRequest);
//        this.agencyQuitRequest.setAgency(this.getAgency());
        if (this.agencyQuitRequest.getAgency() == null) {
            ZkUtils.showInformation("请选合作商再保存", "提示");
            return;
        }
        this.setEntity(this.saveEntity(this.agencyQuitRequest));
        showDialog();
    }

    @Command
    @NotifyChange("*")
    public void update() {
        this.setReadonly(false);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void selectAgency(@BindingParam("model") AgencyEntity agency) {
        this.agencyQuitRequest.setAgency(agency);
    }

}
