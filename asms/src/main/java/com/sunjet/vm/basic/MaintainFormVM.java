package com.sunjet.vm.basic;

import com.sunjet.model.basic.MaintainEntity;
import com.sunjet.service.basic.MaintainService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * 维修项目 表单
 * <p>
 * Created by Administrator on 2016/9/12.
 */
public class MaintainFormVM extends FormBaseVM {
    @WireVariable
    private MaintainService maintainService;

    private MaintainEntity maintain;

    public MaintainEntity getMaintain() {
        return maintain;
    }

    public void setMaintain(MaintainEntity maintain) {
        this.maintain = maintain;
    }

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            maintain = (MaintainEntity) maintainService.getRepository().findOne(objId);
        } else {
            maintain = new MaintainEntity();
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        win = (Window) view;
    }

    @Command
    public void submit() {
        this.maintain = maintainService.save(this.maintain);
        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", maintain);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MAINTAIN_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MAINTAIN_LIST, null);
        }
    }

}
