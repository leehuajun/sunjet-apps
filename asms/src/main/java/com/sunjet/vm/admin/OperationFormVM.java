package com.sunjet.vm.admin;

import com.sunjet.model.admin.OperationEntity;
import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.helper.EntityWrapper;
import com.sunjet.service.admin.OperationService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class OperationFormVM extends FormBaseVM {
    @WireVariable
    private OperationService operationService;

    private OperationEntity operationEntity = new OperationEntity();


    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            operationEntity = operationService.findOneById(objId);
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
    @NotifyChange("operationEntity")
    public void submit() {
        operationEntity = operationService.save(operationEntity);

        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", operationEntity);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_OPERATION_ENTITY, map);
        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_OPERATION_LIST, null);
        }
    }

//    @Command
//    public void closeDialog() {
//        configFormDialog.detach();
////        BindUtils.postGlobalCommand(null, null, "myGlobalCommand", null);
//    }

    public OperationEntity getOperationEntity() {
        return operationEntity;
    }

    public void setOperationEntity(OperationEntity operationEntity) {
        this.operationEntity = operationEntity;
    }
}
