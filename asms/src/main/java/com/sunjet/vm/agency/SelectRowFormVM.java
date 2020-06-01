package com.sunjet.vm.agency;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class SelectRowFormVM extends FormBaseVM {

    private List<EntityWrapper<RowWrapper>> entityWrappers = new ArrayList<>();
    private List<EntityWrapper<RowWrapper>> selectedEntityWrappers = new ArrayList<>();

    public List<EntityWrapper<RowWrapper>> getEntityWrappers() {
        return entityWrappers;
    }

    public void setEntityWrappers(List<EntityWrapper<RowWrapper>> entityWrappers) {
        this.entityWrappers = entityWrappers;
    }

    public List<EntityWrapper<RowWrapper>> getSelectedEntityWrappers() {
        return selectedEntityWrappers;
    }

    public void setSelectedEntityWrappers(List<EntityWrapper<RowWrapper>> selectedEntityWrappers) {
        this.selectedEntityWrappers = selectedEntityWrappers;
    }

    @Init(superclass = true)
    public void init() {
        entityWrappers = (List<EntityWrapper<RowWrapper>>) Executions.getCurrent().getArg().get("models");
        for (EntityWrapper<RowWrapper> ew : entityWrappers) {
            if (ew.getSelected() == true) {
                selectedEntityWrappers.add(ew);
            }
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }
}
