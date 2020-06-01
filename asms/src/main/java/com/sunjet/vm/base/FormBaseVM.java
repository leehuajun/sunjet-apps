package com.sunjet.vm.base;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.basic.DocumentNoEntity;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.common.StringHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.NoEmptyConstraint;
import com.sunjet.utils.zk.ZkUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础表单型 ViewEntity
 * <p>
 * Created by lhj on 15/11/6.
 */
public class FormBaseVM extends BaseVM {

    /**
     * 新增或编辑（create 或 save ）
     */
    private DocEntity entity;   // 实体对象
    protected String objId;     // 实体对象的主键Id
    //    private Boolean savebutton = true;
//    private Boolean auditbutton = true;
//    private Boolean giveupbutton = true;
//    private Boolean deletebutton = true;
//    private Boolean closebutton = true;
    private Boolean readonly = true;

    public DocEntity getEntity() {
        return entity;
    }

//    public Boolean getSavebutton() {
//        return savebutton;
//    }
//
//    public void setSavebutton(Boolean savebutton) {
//        this.savebutton = savebutton;
//    }
//
//    public Boolean getAuditbutton() {
//        return auditbutton;
//    }
//
//    public void setAuditbutton(Boolean auditbutton) {
//        this.auditbutton = auditbutton;
//    }
//
//    public Boolean getGiveupbutton() {
//        return giveupbutton;
//    }
//
//    public void setGiveupbutton(Boolean giveupbutton) {
//        this.giveupbutton = giveupbutton;
//    }
//
//    public Boolean getDeletebutton() {
//        return deletebutton;
//    }
//
//    public void setDeletebutton(Boolean deletebutton) {
//        this.deletebutton = deletebutton;
//    }
//
//    public Boolean getClosebutton() {
//        return closebutton;
//    }
//
//    public void setClosebutton(Boolean closebutton) {
//        this.closebutton = closebutton;
//    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }


    @Init
    public void formBaseInit() {
        objId = (String) Executions.getCurrent().getArg().get("objId");
        if (StringUtils.isNotBlank(objId)) {
            readonly = true;
        } else {
            readonly = false;
        }
    }

    @Command
    public void checkClientInfo(@BindingParam("evt") Event event) {
        if (win != null) {
            win.setHeight(CommonHelper.windowHeight + "px");
//            win.setWidth((CommonHelper.windowWidth - 100) + "px");
            if ((CommonHelper.windowWidth - 100) > 900) {
                win.setWidth((CommonHelper.windowWidth - 100) + "px");
            }
        } else {
            ZkUtils.showError("Win is Null", "error");
        }
    }
}
