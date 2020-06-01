package com.sunjet.repository.agency;

import com.sunjet.model.agency.AgencyQuitRequestEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyQuitRepostitory extends JpaRepository<AgencyQuitRequestEntity, String>, JpaSpecificationExecutor<AgencyQuitRequestEntity> {
}
