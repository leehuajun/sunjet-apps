package com.sunjet.vm.flow;

import com.sunjet.service.flow.ProcessService;
import com.sunjet.vm.base.ListBaseVM;
import org.activiti.engine.repository.Deployment;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class DeploymentListVM extends ListBaseVM {

    @WireVariable
    private ProcessService processService;

    private List<Deployment> deployments;

    public List<Deployment> getDeployments() {
        return deployments;
    }

    public void setDeployments(List<Deployment> deployments) {
        this.deployments = deployments;
    }

    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(false);
        deployments = processService.findDeploymentList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Override
    @Command
    @NotifyChange("*")
    public void refreshData() {
        this.setKeyword("");
        deployments = processService.findDeploymentList();
    }

    @Override
    @Command
    @NotifyChange("*")
    public void filterList() {
//        this.processDefinitions = processService.findProcessDefinitionLastVersionListByFilter(this.getKeyword());
        deployments = processService.findDeploymentListByFilter(this.getKeyword());
    }

    @Command
    @NotifyChange("*")
    public void delete(@BindingParam("model") Deployment deployment) {
//        repositoryService.deleteDeployment(deployment.getId(),true);
        processService.deleteProcessDefinitionByDeploymentId(deployment.getId());
        deployments = processService.findDeploymentList();
    }
}
