package com.sunjet.model.agency;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.admin.DictionaryEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 合作商准入申请单实体
 * <p>
 * <p>
 * 1．	选择项
 * 类型：准入、变更、退出，单选项
 * 是否SGMW体系：是、否，单选项
 * 维修资质：一类；二类；三类（汽车综合小修）；三类（发动机维修），单选
 * 其他车辆维修条件：柴油车、天然气车、电动车、特种车辆、其它（手工输入），多选项
 * 拟申请等级：五星级、四星级、三星级，单选项
 * 2．	校验必填项，所有字段都必填
 * 3．	类型默认选择准入，不能更改
 * 4．	图片信息均需要上传文件，可以上传多份，支持JPG、PDF、WORD等格式文档
 * 5．	申请单可以打印
 * 6.   自动生成合作商编码，合作商编号格式：(手动录入)
 */
@Entity
@Table(name = "AgencyAdmitRequests")
public class AgencyAdmitRequestEntity extends FlowDocEntity {


    private Boolean canUpload = false;      // 是否允许上传文件
    private Boolean canEditCode = false;    // 是否允许录入经销商编号
    private AgencyEntity agency;       // 合作库信息

    public Boolean getCanUpload() {
        return canUpload;
    }

    public void setCanUpload(Boolean canUpload) {
        this.canUpload = canUpload;
    }

    public Boolean getCanEditCode() {
        return canEditCode;
    }

    public void setCanEditCode(Boolean canEditCode) {
        this.canEditCode = canEditCode;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "Agency")
    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }
}
