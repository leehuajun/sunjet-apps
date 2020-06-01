package com.sunjet.vm.report.asm;

import com.sunjet.model.admin.IconEntity;
import com.sunjet.service.admin.IconService;
import com.sunjet.vm.base.FormBaseVM;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

//import net.sf.json.JSONArray;

/**
 * Created by lhj on 16/4/27.
 */
public class QualtyReportVM extends FormBaseVM {

    @WireVariable
    private IconService iconService;
//  @Wire
//  private Component iframe;


    private String reportUrl;
    private List<IconEntity> iconList;

    public List<IconEntity> getIconList() {
        return iconList;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    @Init
    public void init() {
        String objId = Executions.getCurrent().getArg().get("objId").toString();


        this.reportUrl = "/views/report/asm/qualtyReport.jsp?objId=" + objId
                + "&report=qualityReport.jasper";

        iconList = iconService.findAll();


    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
    }
}
