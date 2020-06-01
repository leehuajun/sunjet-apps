package com.sunjet.vm.flow;

import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.ListBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ColseTaskListVM extends ListBaseVM {

    private String processInstanceID = "";

    @Init
    public void init() {
        this.setBaseService(userService);
        initList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void colseTask() {
        try {
            if (processInstanceID != null) {
                processService.deleteHistoricProcessInstance(processInstanceID);
                ZkUtils.showInformation("流程已关闭", "提示");
            } else {
                ZkUtils.showError("请输入流程ID", "提示");
            }

        } catch (Exception e) {
            ZkUtils.showError("流程不存在", "提示");
        }


    }

    public String getProcessInstanceID() {
        return processInstanceID;
    }

    public void setProcessInstanceID(String processInstanceID) {
        this.processInstanceID = processInstanceID;
    }
}
