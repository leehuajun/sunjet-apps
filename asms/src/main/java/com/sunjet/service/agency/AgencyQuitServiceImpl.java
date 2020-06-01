package com.sunjet.service.agency;

import com.sunjet.model.agency.AgencyQuitRequestEntity;
import com.sunjet.repository.agency.AgencyQuitRepostitory;
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
@Service("agencyQuitService")
public class AgencyQuitServiceImpl implements AgencyQuitService {
    @Autowired
    private AgencyQuitRepostitory agencyQuitRepostitory;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return agencyQuitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return agencyQuitRepostitory;
    }

    @Override
    public void save(AgencyQuitRequestEntity agencyQuitRequestEntity) {
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        agencyQuitRequestEntity.setStatus(DocStatus.DRAFT.getIndex());
//        agencyQuitRequestEntity.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        agencyQuitRequestEntity.setCreaterName(CommonHelper.getActiveUser().getUsername());
        agencyQuitRepostitory.save(agencyQuitRequestEntity);
    }

    @Override
    public AgencyQuitRequestEntity findOneById(String businessId) {
        return agencyQuitRepostitory.findOne(businessId);
    }

    @Override
    public void deleteEntity(AgencyQuitRequestEntity entity) {
        agencyQuitRepostitory.delete(entity.getObjId());
    }

//  @Override
    // public void save(AgencyQuitRequestEntity entity) {
//        agencyQuitRepostitory.save(entity);
    // }

}
