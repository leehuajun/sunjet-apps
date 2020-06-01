package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyNoticeEntity;
import com.sunjet.repository.asm.SupplyNoticeItemRepository;
import com.sunjet.repository.asm.SupplyNoticeRepository;
import com.sunjet.service.basic.DocumentNoService;
import com.sunjet.utils.common.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("supplyNoticeService")
public class SupplyNoticeServiceImpl implements SupplyNoticeService {
    @Override
    public JpaRepository getRepository() {
        return supplyNoticeRepository;
    }

    @Autowired
    private DocumentNoService documentNoService;

    @Autowired
    private SupplyNoticeRepository supplyNoticeRepository;
    @Autowired
    private SupplyNoticeItemRepository supplyNoticeItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyNoticeRepository;
    }


//    @Override
//    public List<SupplyNoticeEntity> getSupplyNoticeList(SupplyNoticeEntity supplyNoticeRequest) {
//        return supplyNoticeRepository.getSupplyNoticeList(supplyNoticeRequest.getDocNo());
//    }
//
//
//    @Override
//    public List<SupplyNoticeItemEntity> findItems(SupplyNoticeEntity supplyNoticeRequest) {
//        return supplyNoticeItemRepository.findSupplyNoticeItemsByNoticeId(supplyNoticeRequest.getObjId());
//
//    }

    @Override
    public SupplyNoticeEntity getSupplyNoticeByID(String id) {
        return supplyNoticeRepository.getSupplyNotice(id);
    }

    @Override
    public SupplyNoticeEntity save(SupplyNoticeEntity supplyNotice) {
//        String doc = "";
//        if (StringUtils.isBlank(supplyNotice.getDocNo())) {
//            supplyNotice.setDocNo(documentNoService.getDocumentNo(supplyNotice.getClass().getSimpleName()));
//            supplyNotice.setCreatedTime(new Date());
//            supplyNotice.setCreaterName(CommonHelper.getActiveUser().getUsername());
//            supplyNotice.setCreaterId(CommonHelper.getActiveUser().getUserId());
//        }

//        supplyNotice.setModifiedTime(new Date());
//        supplyNotice.setModifierName(CommonHelper.getActiveUser().getUsername());
//        supplyNotice.setModifierId(CommonHelper.getActiveUser().getUserId());

        return supplyNoticeRepository.save(supplyNotice);
//        return doc;
    }

    @Override
    public boolean audit(SupplyNoticeEntity entity) {
        try {
            entity.setStatus(2);
            entity.setModifiedTime(new Date());
            entity.setModifierId(CommonHelper.getActiveUser().getUserId());
            entity.setModifierName(CommonHelper.getActiveUser().getUsername());
            supplyNoticeRepository.save(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean delete(SupplyNoticeEntity entity) {
        try {
            supplyNoticeRepository.delete(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean close(SupplyNoticeEntity entity) {
        try {
            entity.setStatus(3);
            entity.setModifiedTime(new Date());
            entity.setModifierId(CommonHelper.getActiveUser().getUserId());
            entity.setModifierName(CommonHelper.getActiveUser().getUsername());

            supplyNoticeRepository.save(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public SupplyNoticeEntity findOneById(String objId) {
        return supplyNoticeRepository.findOne(objId);
    }

}
