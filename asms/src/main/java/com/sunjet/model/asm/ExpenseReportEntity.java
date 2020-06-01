package com.sunjet.model.asm;

import com.sunjet.model.base.FlowDocEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lhj on 16/9/7.
 * <p>
 * 费用速报实体
 */
@Entity
@Table(name = "AsmExpenseReports")
public class ExpenseReportEntity extends FlowDocEntity {
    private static final long serialVersionUID = 3720803619098277432L;
    private String title;       // 速报标题
    private String dealerCode;  // 服务站编号
    private String dealerName;  // 服务站名称
    private String costType;    // 费用类别  必输项，单选选项，选项内容：非质保；特殊维修；
    private String linkman;     // 联系人
    private String linkmanPhone;     // 联系人电话
    private String serviceManager;  // 服务经理
    private String serviceManagerPhone;  // 服务经理电话
    private String vehicleType;  // 车辆分类

    private String faultDesc;       // 故障描述
    private String faultStatus;     // 故障时行驶状态
    private String faultRoad;       // 故障时路面情况
    private String faultAddress;    // 故障发生地点
    private String initialReason;   // 初步原因分析
    private String decisions;       // 处置意见
    private String file;            // 上传附件
    private Double estimatedCost;     // 预计费用
    private String originFile;      // 原始文件名
    private String comment;   //备注

    private Boolean canEditType = false;     // 是否可以编辑分类

    private Set<ReportVehicleEntity> reportVehicles = new HashSet<>();     // 车辆列表
    private Set<ReportPartEntity> reportParts = new HashSet<>();           // 检查结果，零件信息

    @Column(length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 32)
    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    @Column(length = 50)
    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Column(length = 32)
    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    @Column(length = 50)
    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    @Column(length = 20)
    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone;
    }

    @Column(length = 50)
    public String getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(String serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Column(length = 20)
    public String getServiceManagerPhone() {
        return serviceManagerPhone;
    }

    public void setServiceManagerPhone(String serviceManagerPhone) {
        this.serviceManagerPhone = serviceManagerPhone;
    }

    @Column(length = 50)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Column(length = 500)
    public String getFaultDesc() {
        return faultDesc;
    }

    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }

    @Column(length = 500)
    public String getFaultStatus() {
        return faultStatus;
    }

    public void setFaultStatus(String faultStatus) {
        this.faultStatus = faultStatus;
    }

    @Column(length = 500)
    public String getFaultRoad() {
        return faultRoad;
    }

    public void setFaultRoad(String faultRoad) {
        this.faultRoad = faultRoad;
    }

    @Column(length = 500)
    public String getFaultAddress() {
        return faultAddress;
    }

    public void setFaultAddress(String faultAddress) {
        this.faultAddress = faultAddress;
    }

    @Column(length = 500)
    public String getInitialReason() {
        return initialReason;
    }

    public void setInitialReason(String initialReason) {
        this.initialReason = initialReason;
    }

    @Column(length = 500)
    public String getDecisions() {
        return decisions;
    }

    public void setDecisions(String decisions) {
        this.decisions = decisions;
    }

    @Column(length = 200)
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Column()
    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Boolean getCanEditType() {
        return canEditType;
    }

    public void setCanEditType(Boolean canEditType) {
        this.canEditType = canEditType;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CrId")
    public Set<ReportVehicleEntity> getReportVehicles() {
        return reportVehicles;
    }

    public void setReportVehicles(Set<ReportVehicleEntity> reportVehicles) {
        this.reportVehicles = reportVehicles;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CrId")
    public Set<ReportPartEntity> getReportParts() {
        return reportParts;
    }

    public void setReportParts(Set<ReportPartEntity> reportParts) {
        this.reportParts = reportParts;
    }

    @Column(length = 50)
    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
