<%@ page contentType="application/pdf;charset=UTF-8" %>
<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<%@ page import="net.sf.jasperreports.engine.JasperFillManager" %>--%>
<%@ page import="org.zkoss.zk.ui.util.Clients" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Properties" %>
<%@ page import="net.sf.jasperreports.engine.JasperRunManager" %>
<%@ page import="com.sunjet.utils.common.CommonHelper" %>
<%@ page import="org.zkoss.zsoup.helper.StringUtil" %>
<%--<%@ taglib uri="http://www.zkoss.org/jsp/zul" prefix="z"%>--%>
<%--<z:init use="org.zkoss.zkplus.databind.AnnotateDataBinderInit" />--%>
<%
    //    Clients.showBusy("正在生成报表，请稍候...");
    Properties properties = new Properties();
    String realpath = request.getRealPath("/WEB-INF/classes");
    FileInputStream in = new FileInputStream(realpath + "/db.properties");
    properties.load(in);


//  request.getParameter("data");
    System.out.println(request.getQueryString());
    //报表编译之后生成的.jasper文件的存放位置
    File reportFile = new File(request.getRealPath("report/" + request.getParameter("report")));

    String driverClass = properties.getProperty("jdbc.driverClassName");
    String url = properties.getProperty("jdbc.jdbcUrl");
    String username = properties.getProperty("jdbc.username");
    String password = properties.getProperty("jdbc.password");
    in.close();
    System.out.println(driverClass);
    System.out.println(url);
    System.out.println(username);
    System.out.println(password);
    Class.forName(driverClass);
    Map parameters = new HashMap();
    //"SQLSTR"是报表中定义的一个参数名称,其类型为String型
//    parameters.put("SQLSTR",
//            "select * from sys_icons");

    Connection conn = DriverManager.getConnection(url, username, password);
    parameters.put("objId", request.getParameter("objId"));


//
//    JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), null, conn);
//    JasperViewer.viewReport(jasperPrint);

    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conn);
//  String msg = JasperRunManager.runReportToHtmlFile(reportFile.getPath(),null,conn);
    response.setContentType("application/pdf");
//  response.setContentLength(bytes.length);
//  response.getWriter().print(msg);
    ServletOutputStream outStream = response.getOutputStream();
    outStream.write(bytes, 0, bytes.length);
    CommonHelper.isOk = true;
    System.out.println("CommonHelper.isOk:" + CommonHelper.isOk);
    outStream.flush();
    outStream.close();
%>

<%!

    public String format(String str) {
        if (StringUtil.isBlank(str) || str == null) {
            return "%%";
        } else {
            return str.trim();
        }
    }
%>