package com.sunjet.service.asm;

import com.sunjet.model.asm.ReportVehicleEntity;
import com.sunjet.repository.asm.RecycleItemRepository;
import com.sunjet.repository.asm.ReportVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("recycleItemService")
public class RecycleItemServiceImpl implements RecycleItemService {
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
