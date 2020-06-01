package com.sunjet.service.dealer;

import com.sunjet.model.dealer.DealerQuitRequestEntity;
import com.sunjet.repository.admin.DictionaryRepository;
import com.sunjet.repository.dealer.DealerQuitRepostitory;
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
@Service("dealerQuitService")
public class DealerQuitServiceImpl implements DealerQuitService {
    @Autowired
    private DealerQuitRepostitory dealerQuitRepostitory;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerQuitRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerQuitRepostitory;
    }

    @Override
    public void saveQuit(DealerQuitRequestEntity dealerQuitRequestEntity) {
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        dealerQuitRequestEntity.setStatus(DocStatus.DRAFT.getIndex());
//        dealerQuitRequestEntity.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        dealerQuitRequestEntity.setCreaterName(CommonHelper.getActiveUser().getUsername());
        dealerQuitRepostitory.save(dealerQuitRequestEntity);
    }

    @Override
    public DealerQuitRequestEntity findOneById(String businessId) {
        return dealerQuitRepostitory.findOne(businessId);
    }

    @Override
    public void deleteEntity(DealerQuitRequestEntity entity) {
        dealerQuitRepostitory.delete(entity.getObjId());
    }
}
