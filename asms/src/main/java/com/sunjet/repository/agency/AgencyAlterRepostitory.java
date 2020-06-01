package com.sunjet.repository.agency;

import com.sunjet.model.agency.AgencyAlterRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyAlterRepostitory extends JpaRepository<AgencyAlterRequestEntity, String>, JpaSpecificationExecutor<AgencyAlterRequestEntity> {
}
