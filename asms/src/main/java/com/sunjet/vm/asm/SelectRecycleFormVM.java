package com.sunjet.vm.asm;

import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.service.asm.RecycleNoticeItemService;
import com.sunjet.utils.zk.ZkUtils;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 */
public class SelectRecycleFormVM extends FormBaseVM {

    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;
    private String data = "";
    private String dealerCode = "";
    private List selectedRecycleList = new ArrayList<>();
    private List<RecycleNoticeItemEntity> partList = new ArrayList<RecycleNoticeItemEntity>();

    @Init(superclass = true)
    public void init() {
        logger.info(Executions.getCurrent().getArg().get("id").toString());
        dealerCode = Executions.getCurrent().getArg().get("dealerCode").toString();
    }

    @Command
    @NotifyChange("*")
    public void searchRecycle() {
        this.partList.clear();
        List<RecycleNoticeItemEntity> recycleNoticeItems = recycleNoticeItemService.findCanReturnPart("%" + this.data.trim() + "%", dealerCode.trim());

        if (recycleNoticeItems.size() < 1) {
            ZkUtils.showInformation("未查询到相关物料！", "提示");
            return;
        }
        this.partList.addAll(recycleNoticeItems);
    }

    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List getSelectedRecycleList() {
        return selectedRecycleList;
    }

    public void setSelectedRecycleList(List selectedRecycleList) {
        this.selectedRecycleList = selectedRecycleList;
    }

    public List<RecycleNoticeItemEntity> getPartList() {
        return partList;
    }

    public void setPartList(List<RecycleNoticeItemEntity> partList) {
        this.partList = partList;
    }


}
