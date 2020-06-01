package com.sunjet.service.asm;

import com.sunjet.model.asm.RecycleEntity;
import com.sunjet.repository.asm.RecycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 故障件返回
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("recycleService")
public class RecycleServiceImpl implements RecycleService {
    @Autowired
    private RecycleRepository recycleRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return recycleRepository;
    }

    @Override
    public void save(RecycleEntity recycleRequest) {
        recycleRepository.save(recycleRequest);
    }

    @Override
    public RecycleEntity findRecycleListById(String objId) {
        return this.recycleRepository.findOneWithItems(objId);
    }

    @Override
    public boolean deleteEntity(RecycleEntity entity) {
        try {
            recycleRepository.delete(entity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public RecycleEntity findOneWithItems(String objId) {
        return recycleRepository.findOneWithItems(objId);
    }


    @Override
    public JpaRepository getRepository() {
        return this.recycleRepository;
    }
}
