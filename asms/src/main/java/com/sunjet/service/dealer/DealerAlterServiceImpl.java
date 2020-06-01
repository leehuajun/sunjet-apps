package com.sunjet.service.dealer;

import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerAlterRequestEntity;
import com.sunjet.repository.admin.DictionaryRepository;
import com.sunjet.repository.dealer.DealerAlterRepostitory;
import com.sunjet.utils.common.CommonHelper;
import com.sunjet.vm.base.DocStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
@Transactional
@Service("dealerAlterService")
public class DealerAlterServiceImpl implements DealerAlterService {
    @Autowired
    private DealerAlterRepostitory dealerAlterRepostitory;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerAlterRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerAlterRepostitory;
    }

    @Override
    public void savedealerAlter(DealerAlterRequestEntity dealerAlterRequest) {
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        dealerAlterRequest.setStatus(DocStatus.DRAFT.getIndex());
//        System.out.println(CommonHelper.getActiveUser().toString());
//        dealerAlterRequest.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        dealerAlterRequest.setCreaterName(CommonHelper.getActiveUser().getUsername());
        dealerAlterRepostitory.save(dealerAlterRequest);
    }

    @Override
    public List<DealerAlterRequestEntity> fineAll() {
        return dealerAlterRepostitory.findAll();
    }

    @Override
    public List<DealerEntity> findAllByKeyword(String keyword) {
        return dealerAlterRepostitory.findAllByKeyword(keyword);
    }

    @Override
    public DealerAlterRequestEntity findobjId(String objId) {
        DealerAlterRequestEntity dealerAlterRequestEntity = dealerAlterRepostitory.findOne(objId);
        return dealerAlterRepostitory.findOne(objId);
    }

    @Override
    public DealerAlterRequestEntity findOneById(String businessId) {
        return dealerAlterRepostitory.findOne(businessId);
    }

    @Override
    public void deleteEntity(DealerAlterRequestEntity entity) {
        dealerAlterRepostitory.delete(entity.getObjId());
    }
}
