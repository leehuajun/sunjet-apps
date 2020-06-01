package com.sunjet.vm.asm;

import com.sunjet.model.basic.PartEntity;
import com.sunjet.service.basic.PartService;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class checkResultVM extends FormBaseVM {

    private String keyword = "";
    @Wire
    private PartEntity selectedPart;
    private List<PartEntity> selectedPartList = new ArrayList<>();
    private List<PartEntity> partList = new ArrayList<>();
    @WireVariable
    private PartService partService;

    @Init(superclass = true)
    public void init() {

//        logger.info(Executions.getCurrent().getArg().get("id").toString());
//        logger.info(Executions.getCurrent().getArg().get("name").toString());


    }

    @Command
    @NotifyChange("*")
    public void searchReportPart() {

        if (this.keyword.length() >= CommonHelper.FILTER_PARTS_LEN) {
            this.partList = partService.findAllByKeyword(this.keyword.trim());
            if (this.partList.size() < 1) {
                ZkUtils.showInformation("未查询到相关物料！", "提示");
            }

        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_PARTS_ERROR, "提示");
        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    public PartEntity getSelectedPart() {
        return selectedPart;
    }

    public void setSelectedPart(PartEntity selectedPart) {
        this.selectedPart = selectedPart;
    }

    public List<PartEntity> getSelectedPartList() {
        return selectedPartList;
    }

    public void setSelectedPartList(List<PartEntity> selectedPartList) {
        this.selectedPartList = selectedPartList;
    }

    public List<PartEntity> getPartList() {
        return partList;
    }

    public void setPartList(List<PartEntity> partList) {
        this.partList = partList;
    }

    public String getKeyword() {

        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
