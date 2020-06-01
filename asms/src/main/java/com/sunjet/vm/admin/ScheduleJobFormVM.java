package com.sunjet.vm.admin;

import com.sunjet.model.admin.ScheduleJobEntity;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ScheduleJobFormVM extends FormBaseVM {

/*    @Wire
    private Paging pagingUser;*/

    //  @Wire
//  private Window configFormDialog;
    //  @WireVariable
//  private SystemService systemService;
    private ScheduleJobEntity scheduleJob;


    @Init(superclass = true)
    public void init() {
//        scheduleJobEntity = (ScheduleJobEntity) (Executions.getCurrent().getArg().get("model"));
//
//        this.checkModel(scheduleJobEntity);
        scheduleJob = (ScheduleJobEntity) this.getEntity();
    }

    private void checkModel(ScheduleJobEntity scheduleJobEntity) {

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 初始化用户信息
     */
    @Command
    @NotifyChange({"scheduleJob"})
    public void InitData() {
//        this.userEntity = systemService.findUserEntityById()
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("scheduleJob")
    public void submit() {
        System.out.println("保存定时任务信息");
        //systemService.saveUserEntity(this.userEntity);
    }

//    @Command
//    public void closeDialog() {
//        configFormDialog.detach();
////        BindUtils.postGlobalCommand(null, null, "myGlobalCommand", null);
//    }


    public ScheduleJobEntity getScheduleJob() {
        return scheduleJob;
    }

    public void setScheduleJob(ScheduleJobEntity scheduleJob) {
        this.scheduleJob = scheduleJob;
    }
}
