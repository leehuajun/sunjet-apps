package com.sunjet.vm.asm;

import com.sunjet.model.asm.ActivityPartEntity;
import com.sunjet.service.asm.ActivityPartService;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * Created by Administrator on 2016/10/31.
 */
public class CheckActiviResultVM extends FormBaseVM {
    @WireVariable
    private ActivityPartService activityPartService;
    private ActivityPartEntity activityPartList;
    private String keyword = "";


    public ActivityPartEntity getActivityPartList() {
        return activityPartList;
    }

    public void setActivityPartList(ActivityPartEntity activityPartList) {
        this.activityPartList = activityPartList;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Init(superclass = true)
    public void init() {

        logger.info(Executions.getCurrent().getArg().get("id").toString());
        logger.info(Executions.getCurrent().getArg().get("name").toString());


    }

    @Command
    @NotifyChange("*")
    public void searchReportPart() {
        this.activityPartList = activityPartService.filter("%" + this.keyword.trim() + "%");


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }
}
