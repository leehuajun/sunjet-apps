package com.sunjet.vm.admin;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.service.admin.RoleService;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.basic.AgencyService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Checkbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sunjet.utils.common.CommonHelper.genPassword;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class UserFormVM extends FormBaseVM {
    @WireVariable
    private UserService userService;
    @WireVariable
    private RoleService roleService;
    @WireVariable
    private AgencyService agencyService;
    @WireVariable
    private DealerService dealerService;

    private String keywordAgency = "";
    private String keywordDealer = "";

    private String passwordConfirm;

    private List<AgencyEntity> agencies = new ArrayList<>();
    private List<DealerEntity> dealers = new ArrayList<>();

    private List<EntityWrapper<RoleEntity>> entityWrappers = new ArrayList<>();
    private List<EntityWrapper<RoleEntity>> entityWrapperSelectedItems = new ArrayList<>();
    private UserEntity userEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public List<EntityWrapper<RoleEntity>> getEntityWrappers() {
        return entityWrappers;
    }

    public void setEntityWrappers(List<EntityWrapper<RoleEntity>> modelWrappers) {
        this.entityWrappers = modelWrappers;
    }

    public List<EntityWrapper<RoleEntity>> getEntityWrapperSelectedItems() {
        return entityWrapperSelectedItems;
    }

    public void setEntityWrapperSelectedItems(List<EntityWrapper<RoleEntity>> modelWrapperSelectedItems) {
        this.entityWrapperSelectedItems = modelWrapperSelectedItems;
    }

    public List<AgencyEntity> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<AgencyEntity> agencies) {
        this.agencies = agencies;
    }

    public List<DealerEntity> getDealers() {
        return dealers;
    }

    public void setDealers(List<DealerEntity> dealers) {
        this.dealers = dealers;
    }

    public String getKeywordAgency() {
        return keywordAgency;
    }

    public void setKeywordAgency(String keywordAgency) {
        this.keywordAgency = keywordAgency;
    }

    public String getKeywordDealer() {
        return keywordDealer;
    }

    public void setKeywordDealer(String keywordDealer) {
        this.keywordDealer = keywordDealer;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


    @Init(superclass = true)
    public void init() {
        entityWrappers.clear();
        List<RoleEntity> roles = roleService.findAll();
//        UserEntity user = (UserEntity)this.getEntity();
        // 检查实体对象，并初始化状态，确定当前是 新增 或 编辑
//        System.out.println(this.getEntity());
        if (StringUtils.isNotBlank(objId)) {
            userEntity = userService.findOneWithRoles(objId);
            this.passwordConfirm = userEntity.getPassword();
            for (RoleEntity roleEntity : roles) {
                Boolean found = false;
                for (RoleEntity model : userEntity.getRoles()) {
                    if (model.getObjId().equals(roleEntity.getObjId())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    EntityWrapper<RoleEntity> modelWrapper = new EntityWrapper<>(roleEntity, true);
                    entityWrappers.add(modelWrapper);
                    entityWrapperSelectedItems.add(modelWrapper);
                } else {
                    entityWrappers.add(new EntityWrapper(roleEntity, false));
                }
            }
        } else {
            userEntity = new UserEntity();
            for (RoleEntity roleEntity : roles) {
                entityWrappers.add(new EntityWrapper(roleEntity, false));
            }
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    public void submit() {
        this.userEntity.getRoles().clear();
        for (EntityWrapper<RoleEntity> entityWrapper : entityWrapperSelectedItems) {
            this.userEntity.getRoles().add(entityWrapper.getEntity());
        }

//        if (userEntity.getRoles().size() > 0) {
//            userEntity.setRolesDesc(userEntity.getRoles().toString().replace("[", "").replace("]", ""));
//        }
        if (userEntity.getPassword() == null) {
            ZkUtils.showError("密码不能为空", "提示");
            return;
        } else if (!userEntity.getPassword().equals(this.passwordConfirm)) {
            //String reg="^[0-9a-zA-Z!@$#%^&*]{8}$";
            //String reg1="^[0-9]{8}$";
            //String reg2="^[a-zA-Z]{8}$";
            //String reg3="^[!@$#%^&*]{8}$";
            ZkUtils.showError("两次输入的密码不一致", "提示");
            return;
        } else if (userEntity.getPassword().equals(this.passwordConfirm)) {
            if (!rexCheckPassword(userEntity.getPassword())) {

                ZkUtils.showError("密码不少于6位，必须包含字母大、小写、数字或特殊字符（_!@#%）", "提示");
                return;
            }

        }
        UserEntity user = genPassword(userEntity);
        user = userService.save(user);
//        roleService.updateRoles();

        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", user);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_USER_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_USER_LIST, null);
        }
    }

    @Command
    public void changeCheckboxStatus(@BindingParam("ctrl") Component comp) {
        Checkbox checkbox = (Checkbox) comp;
        checkbox.setChecked(checkbox.isChecked() ? false : true);
    }

    @Command
    @NotifyChange("userEntity")
    public void checkUserType() {
        if (this.userEntity == null) {
            logger.info("userEntity is null!");
            return;
        }
        switch (userEntity.getUserType()) {
            case "agency":
                this.userEntity.setDealer(null);
                break;
            case "dealer":
                this.userEntity.setAgency(null);
                break;
            case "wuling":
                this.userEntity.setAgency(null);
                this.userEntity.setDealer(null);
                break;
        }
    }

    @Command
    @NotifyChange("agencies")
    public void searchAgence() {
        agencies = agencyService.findAllByKeyword(this.keywordAgency);
    }

    @Command
    @NotifyChange("agencies")
    public void searchDealer() {
        dealers = dealerService.findAllByKeyword(this.keywordDealer);
    }

    @Command
    @NotifyChange("userEntity")
    public void selectAgency(@BindingParam("model") AgencyEntity agency) {
        this.userEntity.setAgency(agency);
        this.userEntity.setDealer(null);
    }

    @Command
    @NotifyChange("userEntity")
    public void selectDealer(@BindingParam("model") DealerEntity dealer) {
        this.userEntity.setDealer(dealer);
        this.userEntity.setAgency(null);
    }

    public static boolean rexCheckPassword(String password) {
        // 6-20 位，字母、数字、字符
        //String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        String regStr = "^((?=.*[0-9].*)(?=.*[A-Za-z].*))[_0-9A-Za-z@!#%]{6,16}$";
        return password.matches(regStr);
    }


}
