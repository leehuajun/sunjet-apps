package com.sunjet.vm.flow;

import com.sunjet.model.flow.LeaveBill;
import com.sunjet.service.flow.LeaveBillService;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FlowFormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class LeaveBillFormVM extends FlowFormBaseVM {

    @WireVariable
    private LeaveBillService leaveBillService;
    private LeaveBill leaveBill;

    public LeaveBill getLeaveBill() {
        return leaveBill;
    }

    public void setLeaveBill(LeaveBill leaveBill) {
        this.leaveBill = leaveBill;
    }


    @Init(superclass = true)
    public void init() {
        this.setBaseService(leaveBillService);
        if (StringUtils.isNotBlank(this.getBusinessId())) {   // 有业务对象Id
            this.leaveBill = leaveBillService.findOne(this.getBusinessId());
        } else {        // 业务对象和业务对象Id都为空
            this.leaveBill = new LeaveBill();
        }
        this.setEntity(this.leaveBill);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
        if (win != null) {
            win.setTitle(win.getTitle() + titleMsg);
        }
    }

    @Command
    @NotifyChange("*")
    public void startProcess() {
        this.leaveBill = (LeaveBill) this.getEntity();
        Map<String, Object> variables = new HashMap<>();
        variables.put("days", this.leaveBill.getDays());
        if (StringUtils.isNotBlank(this.leaveBill.getProcessInstanceId())) {
            ZkUtils.showExclamation("流程已启动，不能重复启动！", "系统提示");
            return;
        }
        startProcessInstance(variables);
    }

//    @GlobalCommand(GlobalCommandValues.START_PROCESS)
//    public void startProcess(@BindingParam("outcome") String outcome,@BindingParam("comment")String comment,@BindingParam("key")String key) throws IOException {
//        if(!key.equals(this.getProcessDefinitionKey())){
//            System.out.println("LeaveBillFormVM: 和俺没得关系，等别人处理！");
//            return;
//        }
//
//        this.leaveBill = (LeaveBill) this.getFlowDocEntity();
//
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("days", this.leaveBill.getDays());
//        completeTask(outcome,variables,comment);
//        showDialog();
//    }


//    private void updateWinTitleByTaskId(String taskId) {
//        Task task001 = processService.findTaskById(taskId);
//        if (StringUtils.isBlank(task001.getAssignee())) {
//            titleMsg = "[组任务]";
//        } else {
//            titleMsg = "[个人任务]";
//        }
//    }

}
