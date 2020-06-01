package com.sunjet.service.asm;

import com.sunjet.repository.asm.RecycleItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("recycleListService")
public class RecycleListServiceImpl implements RecycleListService {
    @Autowired
    private RecycleItemRepository recycleItemRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return recycleItemRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return recycleItemRepository;
    }
}
