package com.sunjet.repository.agency;

import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AgencyAdmitRepostitory extends JpaRepository<AgencyAdmitRequestEntity, String>, JpaSpecificationExecutor<AgencyAdmitRequestEntity> {

    @Query("select aa from AgencyAdmitRequestEntity aa where aa.processInstanceId=?1")
    AgencyAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId);
//    List<AgencyAdmitRequestEntity> findAll();
}
