package com.sunjet.vm.admin;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.helper.ActiveUser;
import com.sunjet.service.admin.UserService;
import com.sunjet.utils.permission.EncryptTool;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.shiro.SecurityUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import static com.sunjet.utils.common.CommonHelper.genPassword;
import static com.sunjet.vm.admin.UserFormVM.rexCheckPassword;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class ChangePasswordFormVM extends FormBaseVM {
    @WireVariable
    private UserService userService;

    private UserEntity user;

    private String originPassword;
    private String newPassword;
    private String newPasswordConfirm;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    @Init(superclass = true)
    public void init() {
        ActiveUser currentUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        user = userService.findOne(currentUser.getUserId());
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void changePassword() {
        Integer hashInterations = 1;
        if (this.originPassword == null) {
            ZkUtils.showError("请输入原密码", "系统提示");
            return;
        }
        if (this.newPassword == null) {
            ZkUtils.showError("请输入新密码", "系统提示");
            return;
        } else if (this.newPasswordConfirm == null) {
            ZkUtils.showError("请确认密码", "系统提示");
            return;
        }
        if (this.newPassword.trim().equals(this.newPasswordConfirm.trim()) == false) {
            ZkUtils.showExclamation("两次输入的密码不一致，请重新输入!", "系统提示");
            this.newPassword = "";
            this.newPasswordConfirm = "";
            return;
        }
        String originCredentials = EncryptTool.generatePasswordMd5(this.originPassword, user.getSalt(), hashInterations);

        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        if (!originCredentials.equals(user.getPassword())) {
            ZkUtils.showExclamation("原密码不正确，请重新输入!", "系统提示");
            this.originPassword = "";
            return;
        } else {
            if (!rexCheckPassword(this.newPasswordConfirm)) {

                ZkUtils.showError("密码不少于6位，必须包含字母大、小写、数字或特殊字符（_!@#%）", "提示");
                return;
            }
        }

        user.setPassword(this.newPassword);
        UserEntity userEntity1 = genPassword(user);
        userService.changePassword(userEntity1);

        ZkUtils.showQuestion("密码修改成功！\r是否退出重新登录?", "系统提示", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    Executions.sendRedirect("/logout.zul");
                } else {
                    // 用户点击的是取消按钮
                }
            }
        });
    }
}
