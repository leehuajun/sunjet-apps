package com.sunjet.vm.basic;


import com.sunjet.model.basic.PartEntity;
import com.sunjet.service.basic.PartService;
import com.sunjet.utils.zk.GlobalCommandValues;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyehai02 on 2016-07-13.
 * 配件目录编辑
 */
public class PartFormVM extends FormBaseVM {
    @WireVariable
    private PartService partService;

    private List<String> enableds = new ArrayList<>();
    private PartEntity partEntity;
    private String selectEnabled;

    public PartEntity getPartEntity() {
        return partEntity;
    }

    public void setPartEntity(PartEntity partEntity) {
        this.partEntity = partEntity;
    }

    @Init(superclass = true)
    public void init() {
        partEntity = new PartEntity();
        this.enableds.add("是");
        this.enableds.add("否");
        this.selectEnabled = "是";
        if (StringUtils.isNotBlank(this.objId)) {
            partEntity = (PartEntity) partService.getRepository().findOne(this.objId);

            this.selectEnabled = partEntity.getEnabled() == true ? "是" : "否";
        }
//        System.out.println(this.selectEnabled);
    }

//    @Command
//    public void selectEnabled(@BindingParam("model") String value){
//        System.out.println(value);
//        if(("是").equals(value)){
//            ZkUtils.showExclamation("true","提示");
//        }else {
//            ZkUtils.showExclamation("false","提示");
//        }
//
//    }

    @Command
    public void savePart() {
        if ("是".equals(this.getSelectEnabled())) {
            partEntity.setEnabled(true);
        } else if ("否".equals(this.getSelectEnabled())) {
            partEntity.setEnabled(false);
        } else {
            //默认设置
            partEntity.setEnabled(partEntity.getEnabled());
        }
//    this.partEntity.setName();
        partService.getRepository().save(partEntity);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_PART_LIST, null);
        ZkUtils.showInformation("保存成功", "提示");


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    public List<String> getEnableds() {
        return enableds;
    }

    public void setEnableds(List<String> enableds) {
        this.enableds = enableds;
    }

    public String getSelectEnabled() {
        return selectEnabled;
    }

    public void setSelectEnabled(String selectEnabled) {
        this.selectEnabled = selectEnabled;
    }
}
