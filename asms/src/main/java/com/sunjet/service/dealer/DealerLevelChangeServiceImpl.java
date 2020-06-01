package com.sunjet.service.dealer;

import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.repository.admin.DictionaryRepository;
import com.sunjet.repository.dealer.DealerLevelChangeRepostitory;
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
@Service("dealerLevelChangeService")
public class DealerLevelChangeServiceImpl implements DealerLevelChangeService {
    @Autowired
    private DealerLevelChangeRepostitory dealerLevelChangeRepostitory;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return dealerLevelChangeRepostitory;
    }

    @Override
    public JpaRepository getRepository() {
        return dealerLevelChangeRepostitory;
    }

    @Override
    public void saveAlteration(DealerLevelChangeRequestEntity dealerLevelChangeRequestEntity) {
//        DictionaryEntity statusObj = dictionaryRepository.findOne("402883a8572e5d3801572e5d45d60001");
//        dealerLevelChangeRequestEntity.setStatus(DocStatus.DRAFT.getIndex());
//        dealerLevelChangeRequestEntity.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        dealerLevelChangeRequestEntity.setCreaterName(CommonHelper.getActiveUser().getUsername());
        dealerLevelChangeRepostitory.save(dealerLevelChangeRequestEntity);
    }

    @Override
    public List<DealerEntity> findAllByKeyword(String keyword) {
        return dealerLevelChangeRepostitory.findAllByKeyword(keyword);
    }

    @Override
    public DealerLevelChangeRequestEntity findOneById(String businessId) {
        return dealerLevelChangeRepostitory.findOne(businessId);
    }

    @Override
    public void deleteEntity(DealerLevelChangeRequestEntity entity) {
        dealerLevelChangeRepostitory.delete(entity.getObjId());
    }
}
