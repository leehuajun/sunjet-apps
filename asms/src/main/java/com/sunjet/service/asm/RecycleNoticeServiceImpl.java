package com.sunjet.service.asm;

import com.sunjet.model.asm.RecycleNoticeEntity;
import com.sunjet.repository.asm.RecycleNoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 故障件返回通知
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("recycleNoticeService")
public class RecycleNoticeServiceImpl implements RecycleNoticeService {
    @Autowired
    private RecycleNoticeRepository recycleNoticeRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return recycleNoticeRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return recycleNoticeRepository;
    }

    @Override
    public void save(RecycleNoticeEntity recycleNoticeRequest) {
        recycleNoticeRepository.save(recycleNoticeRequest);
    }

    @Override
    public RecycleNoticeEntity findOneWithOthersId(String objId) {
        return recycleNoticeRepository.findOneWithOthersId(objId);
    }

    @Override
    public void deleteEntity(RecycleNoticeEntity entity) {
        recycleNoticeRepository.delete(entity.getObjId());
    }

    @Override
    public void deleteEntityByObjId(String objId) {
        recycleNoticeRepository.delete(objId);
    }


}
