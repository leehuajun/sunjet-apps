package com.sunjet.model.agency;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.AgencyEntity;
import com.sunjet.model.admin.DictionaryEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 合作商退出申请单实体
 */
@Entity
@Table(name = "AgencyQuitRequests")
public class AgencyQuitRequestEntity extends FlowDocEntity {
    private static final long serialVersionUID = 317066571588851599L;

    private AgencyEntity agency;     // 合作库信息
    private String reason;                  // 退出原因

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "Agency")
    public AgencyEntity getAgency() {
        return agency;
    }

    public void setAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    @Column(name = "Reason", length = 1000)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
