package com.sunjet.service.agency;

import com.sunjet.model.agency.AgencyAlterRequestEntity;
import com.sunjet.repository.agency.AgencyAlterRepostitory;
import com.sunjet.repository.admin.DictionaryRepository;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2016/9/5.
 */
@Transactional
@Service("agencyAlterService")
public class AgencyAlterServiceImpl implements AgencyAlterService {
    @Autowired
    private AgencyAlterRepostitory agencyAlterRepostitory;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyAlterRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyAlterRepostitory;
    }

    @Override
    public void save(AgencyAlterRequestEntity agencyAlterRequest) {
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        agencyAlterRequest.setStatus(DocStatus.DRAFT.getIndex());
//        agencyAlterRequest.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        agencyAlterRequest.setCreaterName(CommonHelper.getActiveUser().getUsername());
        agencyAlterRepostitory.save(agencyAlterRequest);
    }

    @Override
    public AgencyAlterRequestEntity findOneById(String businessId) {
        return agencyAlterRepostitory.findOne(businessId);
    }

    // @Override
    // public void save(AgencyAlterRequestEntity entity) {
    //   agencyAlterRepostitory.save(entity);
    // }


    @Override
    public void deleteEntity(AgencyAlterRequestEntity entity) {
        agencyAlterRepostitory.delete(entity.getObjId());
    }
}
