package com.sunjet.model.dealer;

import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.admin.DictionaryEntity;

import javax.persistence.*;

/**
 * Created by lhj on 16/6/30.
 * <p>
 * 服务站退出申请单实体
 */
@Entity
@Table(name = "DealerQuitRequests")
public class DealerQuitRequestEntity extends FlowDocEntity {
    private static final long serialVersionUID = 317066571588851599L;

    private DealerEntity dealer;      // 服务站信息
    private String reason;                  // 退出原因

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "Dealer")
    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    @Column(name = "Reason", length = 1000)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
