package com.sunjet.vm.asm;

import com.sunjet.model.asm.ActivityNoticeEntity;
import com.sunjet.model.asm.ActivityVehicleEntity;
import com.sunjet.service.asm.ActivityNoticeService;
import com.sunjet.service.asm.ActivityVehicleService;
import com.sunjet.vm.base.ListBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class SelectActiveVehicleVM extends ListBaseVM {
    @WireVariable
    private ActivityVehicleService activityVehicleService;
    @WireVariable
    private ActivityNoticeService activityNoticeService;
    private ActivityNoticeEntity activityNoticeEntity;
    private List<ActivityNoticeEntity> activityNoticeEntities = new ArrayList<>();
    private String docNo = "";
    // private ActivityNoticeService activityNoticeService;
    List<ActivityVehicleEntity> activityNoticeVehicleEntities = new ArrayList<>();
    private ActivityVehicleEntity activityVehicleEntity = new ActivityVehicleEntity();
    private String keyword = "";

    public ActivityNoticeEntity getActivityNoticeEntity() {
        return activityNoticeEntity;
    }

    public void setActivityNoticeEntity(ActivityNoticeEntity activityNoticeEntity) {
        this.activityNoticeEntity = activityNoticeEntity;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public ActivityVehicleEntity getActivityVehicleEntity() {
        return activityVehicleEntity;
    }

    public void setActivityVehicleEntity(ActivityVehicleEntity activityVehicleEntity) {
        this.activityVehicleEntity = activityVehicleEntity;
    }

    public List<ActivityNoticeEntity> getActivityNoticeEntities() {
        return activityNoticeEntities;
    }

    public List<ActivityVehicleEntity> getActivityNoticeVehicleEntities() {
        return activityNoticeVehicleEntities;
    }

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Init(superclass = true)
    public void init() {

        logger.info(Executions.getCurrent().getArg().get("id").toString());
        logger.info(Executions.getCurrent().getArg().get("name").toString());
        //mc=Executions.getCurrent().getArg().get("mc").toString();
        docNo = Executions.getCurrent().getArg().get("docNo").toString();

    }


    @Command
    @NotifyChange("*")
    public void searchVehicle() {
        // this.activityNoticeEntities = activityVehicleService.filter("%" + this.keyword + "%");
        keyword = "";
//        activityNoticeEntity= activityVehicleService.findallDocNo("%" + this.keyword + "%",docNo);
        activityNoticeEntity = activityNoticeService.findallDocNo("%" + this.keyword.trim() + "%", docNo.trim());
        for (ActivityVehicleEntity i : activityNoticeEntity.getActivityVehicles()) {
            activityNoticeVehicleEntities.add(i);
        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

}
