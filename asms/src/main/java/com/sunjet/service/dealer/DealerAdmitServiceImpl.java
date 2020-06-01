package com.sunjet.service.dealer;

import com.sunjet.model.dealer.DealerAdmitRequestEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.repository.admin.DictionaryRepository;
import com.sunjet.repository.dealer.DealerAdmitRepostitory;
import com.sunjet.utils.common.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by Administrator on 2016/9/2.
 */
@Transactional
@Service("dealerAdmitService")
public class DealerAdmitServiceImpl implements DealerAdmitService {
    @Autowired
    private DealerAdmitRepostitory dealerAdmitRepostitory;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerAdmitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerAdmitRepostitory;
    }

    @Override
    public void update(DealerAdmitRequestEntity dealerAdmitRequestEntity) {
        dealerAdmitRepostitory.save(dealerAdmitRequestEntity);
    }

    @Override
    public DealerAdmitRequestEntity findOneById(String businessId) {
        return dealerAdmitRepostitory.findOne(businessId);
    }

    @Override
    public void save(DealerAdmitRequestEntity entity) {
//        entity.setCreaterId("4028839455a1c0940155a1c0a0b60006");
//
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        entity.setStatus(statusObj);
//        entity.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        entity.setCreaterName(CommonHelper.getActiveUser().getUsername());
        dealerAdmitRepostitory.save(entity);

    }

    @Override
    public void deleteEntity(DealerAdmitRequestEntity entity) {
        dealerAdmitRepostitory.delete(entity.getObjId());
    }

    @Override
    public DealerAdmitRequestEntity findOneByProcessInstanceId(String processInstanceId) {
        return dealerAdmitRepostitory.findOneByProcessInstanceId(processInstanceId);
    }
}
