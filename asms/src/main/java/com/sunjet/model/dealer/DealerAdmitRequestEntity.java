package com.sunjet.model.dealer;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 服务站准入申请单实体
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
 * 6.   自动生成服务站编码，服务站编号格式：(手动录入)
 */
@Entity
@Table(name = "DealerAdmitRequests")
public class DealerAdmitRequestEntity extends FlowDocEntity {
    private static final long serialVersionUID = -8885688978424904989L;

    private DealerEntity dealer;      // 服务站信息
    private Boolean canEditCode = false;            // 是否可以编辑服务站编码，默认不允许
    private Boolean canEditServiceManager = false;  // 是否可以编辑服务经理，默认不允许
    private Boolean canUpload = false;              // 是否可以上传文件，默认不允许


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

    public Boolean getCanEditServiceManager() {
        return canEditServiceManager;
    }

    public void setCanEditServiceManager(Boolean canEditServiceManager) {
        this.canEditServiceManager = canEditServiceManager;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "Dealer")
    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }
}
