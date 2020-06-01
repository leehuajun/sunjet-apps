package com.sunjet.vm.basic;

import com.sunjet.model.basic.NoticeEntity;
import com.sunjet.service.basic.NoticeService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class NoticeFormVM extends FormBaseVM {

    @WireVariable
    private NoticeService noticeService;

    private NoticeEntity notice = new NoticeEntity();

    public NoticeEntity getNotice() {
        return notice;
    }

    public void setNotice(NoticeEntity notice) {
        this.notice = notice;
    }

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            notice = (NoticeEntity) noticeService.getRepository().findOne(objId);
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.win = (Window) view;
    }
//    /**
//     * 表单提交,保存用户信息
//     */
//    @Command
//    @NotifyChange("*")
//    public void saveNotice() {
////        ZkUtils.showInformation(this.notice.getContent(),this.notice.getTitle());
////        this.notice.setContent("<![CDATA[" + this.getNotice() + "]]>");
//        this.notice.setPublishDate(new Date());
//        this.notice.setPublisher(CommonHelper.getActiveUser().getUsername());
//        this.notice = noticeService.save(this.notice);
//        ZkUtils.showInformation("保存成功！", "系统提示");
//        this.setReadonly(true);
//    }

    /**
     * 表单提交,保存信息
     */
    @Command
//    @NotifyChange("resourceEntity")
    public void submit() {
        this.notice.setPublishDate(new Date());
        this.notice.setPublisher(CommonHelper.getActiveUser().getUsername());
        notice = (NoticeEntity) noticeService.getRepository().save(this.notice);
        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", notice);
            //BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_NOTICE_ENTITY, map);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_NOTICE_LIST, map);

        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_NOTICE_LIST, null);
            //BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_NOTICE_LIST, null);
        }
    }

    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        String fileOriginName = event.getMedia().getName();
        String fileRealName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_NOTICE);
        this.notice.setFileOriginName(fileOriginName);
        this.notice.setFileRealName(fileRealName);
    }

    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_NOTICE + filename;
    }

    @Command
    public void deleteFile() {
        this.notice.setFileRealName("");
        this.notice.setFileOriginName("");
    }

    @Command
    @NotifyChange("*")
    public void changeStatus() {
        if (this.notice.getPublisher() != null && this.notice.getPublisher().equals(CommonHelper.getActiveUser().getUsername())) {
            this.setReadonly(false);
        } else {
            ZkUtils.showError("非发布人不能编辑", "提示");
        }
    }


}
