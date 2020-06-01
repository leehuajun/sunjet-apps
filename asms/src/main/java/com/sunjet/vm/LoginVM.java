package com.sunjet.vm;

import com.google.code.kaptcha.Producer;
import com.sunjet.model.admin.ConfigEntity;
import com.sunjet.service.asm.RecycleNoticeService;
import com.sunjet.service.asm.SupplyNoticeService;
import com.sunjet.service.asm.WarrantyMaintenanceService;
import com.sunjet.utils.common.LoggerUtil;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.activiti.engine.TaskService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.awt.image.BufferedImage;

/**
 * Created by lhj on 15/11/6.
 */
public class LoginVM extends FormBaseVM {
    @WireVariable
    private Producer captchaProducer;
    private BufferedImage captchaImage;
    private Boolean captchaOk = false;
    private String sourceCaptcha = "";
    private String inputCaptcha = "";
    private String message;



    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;

    @WireVariable
    private SupplyNoticeService supplyNoticeService;

    @WireVariable
    private RecycleNoticeService recycleNoticeService;
    @WireVariable
    TaskService taskService;



    public String getMessage() {
        return message;
    }

    public BufferedImage getCaptchaImage() {
        return captchaImage;
    }

    public Boolean getCaptchaOk() {
        return captchaOk;
    }

    public String getInputCaptcha() {
        return inputCaptcha;
    }

    public void setInputCaptcha(String inputCaptcha) {
        this.inputCaptcha = inputCaptcha;
    }


    @Init
    public void init() {
//        Caches.ConfigCache.Init(configService.findAll());
//        Caches.DictionaryCache.init(dictionaryService.findAll());

//        for(String key : DictionaryHelper.getKeySet()){
//            System.out.println(DictionaryHelper.getDictionariesByParentCode(key).toString());
//        }

        Subject subject = SecurityUtils.getSubject();

//    if (subject == null)
//      return;
        if (subject != null && subject.isAuthenticated()) {
            Executions.sendRedirect("/logout.zul");
//      subject.logout();
//      Executions.sendRedirect("/index.zul");
        }
//    message = String.valueOf(subject.isAuthenticated());
//    System.out.println(message);

//        Executions.getCurrent().getDesktop()

//        Producer producer = new DefaultKaptcha();
        genCaptcha();


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
//        view.getPage().setStyle("background-color:rgb(78,116,149)");


        //更新版本号
        ConfigEntity configEntity = configService.findOne("app_version");
        try {
            if (configEntity.getConfigValue().equals("版本号:V1.0.0.019")) {

                configEntity.setConfigValue("版本号:V1.0.0.020");
                configService.save(configEntity);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Command
    @NotifyChange("captchaOk")
    public void checkCaptcha() {
        LoggerUtil.getLogger().info("字符变动了！");
        LoggerUtil.getLogger().info("字符长度:" + inputCaptcha.length());
        if (inputCaptcha.length() == 4) {
            if (inputCaptcha.toLowerCase().equals(sourceCaptcha.toLowerCase())) {
                this.captchaOk = true;
                return;
            }
            ZkUtils.showInformation("验证码错误！", "提示");
        }
        this.captchaOk = false;
        return;
    }

    /**
     * 获取客户端信息
     *
     * @param event
     */
//    @RequiresRoles("xxx")
    @Command
    public void showClientInfo(@BindingParam("evt") ClientInfoEvent event) {

        logger.info(String.format("Desktop: width:%s x height:%s", event.getDesktopWidth(), event.getDesktopHeight()));
        logger.info(String.format("Screen: width:%s x height:%s", event.getScreenWidth(), event.getScreenHeight()));
    }

    @Command
    @NotifyChange("*")
    public void genCaptcha() {
        sourceCaptcha = captchaProducer.createText();
        BufferedImage bufferedImage = captchaProducer.createImage(sourceCaptcha);
        System.out.println(bufferedImage.getWidth());
        System.out.println(bufferedImage.getHeight());
        this.captchaImage = bufferedImage;
    }
}
