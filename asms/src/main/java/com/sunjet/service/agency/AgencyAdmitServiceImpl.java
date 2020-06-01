package com.sunjet.service.agency;

import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.repository.agency.AgencyAdmitRepostitory;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by Administrator on 2016/9/2.
 */
@Transactional
@Service("agencyAdmitService")
public class AgencyAdmitServiceImpl implements AgencyAdmitService {
    @Autowired
    private AgencyAdmitRepostitory agencyAdmitRepostitory;
//    @Autowired
//    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyAdmitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyAdmitRepostitory;
    }

    @Override
    public void save(AgencyAdmitRequestEntity agencyAdmitRequest) {
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        agencyAdmitRequest.setStatus(DocStatus.DRAFT.getIndex());
//        agencyAdmitRequest.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        agencyAdmitRequest.setCreaterName(CommonHelper.getActiveUser().getUsername());
        agencyAdmitRepostitory.save(agencyAdmitRequest);
    }

    @Override
    public AgencyAdmitRequestEntity findOneById(String businessId) {
        return agencyAdmitRepostitory.findOne(businessId);
    }

    @Override
    public void deleteEntity(AgencyAdmitRequestEntity entity) {
        agencyAdmitRepostitory.delete(entity.getObjId());
    }

    @Override
    public AgencyAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId) {
        return agencyAdmitRepostitory.findOneByProcessInstanceId(processInstanceId);
    }
}
