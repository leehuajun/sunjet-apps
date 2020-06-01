package com.sunjet.service.asm;

import com.sunjet.model.asm.RecycleNoticeItemEntity;
import com.sunjet.repository.asm.RecycleNoticeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/11/7.
 */
@Transactional
@Service("recycleNoticeItemService")
public class RecycleNoticeItemServiceImpl implements RecycleNoticeItemService {
    @Autowired
    private RecycleNoticeItemRepository recycleNoticeItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return recycleNoticeItemRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return recycleNoticeItemRepository;
    }


    @Override
    public List<RecycleNoticeItemEntity> findAllPart(String partCode) {
        return recycleNoticeItemRepository.findAllPart(partCode);
    }

    @Override
    public List<RecycleNoticeItemEntity> findCanReturnPart(String partCode, String dealerCode) {
        return recycleNoticeItemRepository.findCanReturnPart(partCode, dealerCode);
    }


}
