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
public class SupplyReportVM extends FormBaseVM {

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
//        JSONArray jsonArray = JSONArray.fromObject(iconList);
        String type = Executions.getCurrent().getArg().get("type").toString();
        if ("report".equals(type)) {
            String objId = Executions.getCurrent().getArg().get("objId").toString();
            String dealerName = Executions.getCurrent().getArg().get("dealerName").toString();
            String dealerCode = Executions.getCurrent().getArg().get("dealerCode").toString();
            String docNo = Executions.getCurrent().getArg().get("docNo").toString();
            String dealerAdderss = Executions.getCurrent().getArg().get("dealerAdderss").toString();
            String receive = Executions.getCurrent().getArg().get("receive").toString();
            String operatorPhone = Executions.getCurrent().getArg().get("operatorPhone").toString();
            String agencyName = Executions.getCurrent().getArg().get("agencyName").toString();
            String agencyCode = Executions.getCurrent().getArg().get("agencyCode").toString();
            String submitterName = Executions.getCurrent().getArg().get("submitterName").toString();
            this.reportUrl = "/views/report/asm/supply.jsp?objId=" + objId
                    + "&dealerName=" + dealerName
                    + "&dealerCode=" + dealerCode
                    + "&dealerAdderss=" + dealerAdderss
                    + "&docNo=" + docNo
                    + "&receive=" + receive
                    + "&operatorPhone=" + operatorPhone
                    + "&agencyName=" + agencyName
                    + "&agencyCode=" + agencyCode
                    + "&submitterName=" + submitterName
                    + "&report=supplyReport.jasper";
//        Clients.showBusy("正在生成报表，请稍候...");

            iconList = iconService.findAll();
        } else {
            String objId = Executions.getCurrent().getArg().get("objId").toString();
            String dealerAdderss = Executions.getCurrent().getArg().get("dealerAdderss").toString();
            String dealerName = Executions.getCurrent().getArg().get("dealerName").toString();
            String operatorPhone = Executions.getCurrent().getArg().get("operatorPhone").toString();
            String agencyName = Executions.getCurrent().getArg().get("agencyName").toString();
            String agencyAddress = Executions.getCurrent().getArg().get("agencyAddress").toString();
            String agencyPhone = Executions.getCurrent().getArg().get("agencyPhone").toString();
            String receive = Executions.getCurrent().getArg().get("receive").toString();
            String submitterName = Executions.getCurrent().getArg().get("submitterName").toString();

            this.reportUrl = "/views/report/asm/expressageReport.jsp?objId=" + objId
                    + "&dealerName=" + dealerName
                    + "&agencyAddress=" + agencyAddress
                    + "&dealerAdderss=" + dealerAdderss
                    + "&receive=" + receive
                    + "&operatorPhone=" + operatorPhone
                    + "&agencyName=" + agencyName
                    + "&agencyPhone=" + agencyPhone
                    + "&submitterName=" + submitterName
                    + "&report=expressageReport.jasper";
            iconList = iconService.findAll();

        }


//        JasperFillManager

//    HttpServletRequest request = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
//    HttpServletResponse response = (HttpServletResponse)Executions.getCurrent().getNativeResponse();
//    try {
//      request.setAttribute("data",iconList);
//      response.sendRedirect(this.reportUrl);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
//        CommonHelper.isOk = false;
//        Clients.showBusy("正在生成报表，请稍候...");
//        while(!CommonHelper.isOk){
//            Thread.sleep(500L);
//            System.out.println("CommonHelper.isOk:" + CommonHelper.isOk);
//        }
//        System.out.println("CommonHelper.isOk:" + CommonHelper.isOk);
//        Clients.clearBusy();

    }
}
