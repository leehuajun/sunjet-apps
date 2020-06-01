package com.sunjet.vm.basic;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务站 表单
 * <p>
 * Created by Administrator on 2016/9/5.
 */
public class DealerFormVM extends FormBaseVM {
    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private RoleService roleService;

    private DealerEntity dealer;
    private UserEntity serviceManager;
    private UserEntity defaultServiceManager;
    private String selectEnabled;
    private List<String> enableds = new ArrayList<>();
    private List<UserEntity> serviceManagers = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        this.enableds.add("是");
        this.enableds.add("否");
        if (StringUtils.isNotBlank(objId)) {
            dealer = (DealerEntity) dealerService.getRepository().findOne(objId);


        } else {
            dealer = new DealerEntity();
        }

        RoleEntity role = roleService.findOneWithUsersByRoleId("role000");
        Stream<UserEntity> stream = role.getUsers().stream().filter(new Predicate<UserEntity>() {
            @Override
            public boolean test(UserEntity userEntity) {
                if (userEntity.getEnabled() == true)
                    return true;

                return false;
            }
        });

        this.serviceManagers.addAll(stream.collect(Collectors.toList()));
//        this.setDefaultServiceManager(dealer.getServiceManager());
//        this.setServiceManager(dealer.getServiceManager());

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
    }

    @Command
    public void saveServiceManager() {
        if ("是".equals(this.getSelectEnabled())) {
            dealer.setEnabled(true);
        } else if ("否".equals(this.getSelectEnabled())) {
            dealer.setEnabled(false);
        } else {
            //默认设置
            dealer.setEnabled(dealer.getEnabled());
        }

        dealer.setServiceManager(dealer.getServiceManager());
        dealerService.save(dealer);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_LIST, null);
        ZkUtils.showInformation("保存成功", "提示");

    }


    public String getFilePath(String filename) {
        return CommonHelper.UPLOAD_DIR_DEALER + filename;
    }

    public List<UserEntity> getServiceManagers() {
        return serviceManagers;
    }

    public void setServiceManagers(List<UserEntity> serviceManagers) {
        this.serviceManagers = serviceManagers;
    }

    public UserEntity getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(UserEntity serviceManager) {
        this.serviceManager = serviceManager;
    }

    public UserEntity getDefaultServiceManager() {
        return defaultServiceManager;
    }

    public void setDefaultServiceManager(UserEntity defaultServiceManager) {
        this.defaultServiceManager = defaultServiceManager;
    }

    public List<String> getEnableds() {
        return enableds;
    }

    public void setEnableds(List<String> enableds) {
        this.enableds = enableds;
    }

    public DealerEntity getDealer() {
        return dealer;
    }

    public String getSelectEnabled() {
        return selectEnabled;
    }

    public void setSelectEnabled(String selectEnabled) {
        this.selectEnabled = selectEnabled;
    }
}
