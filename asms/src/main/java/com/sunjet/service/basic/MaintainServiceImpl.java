package com.sunjet.service.basic;

import com.sunjet.model.basic.MaintainEntity;
import com.sunjet.repository.basic.MaintainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
@Transactional
@Service("maintainService")
public class MaintainServiceImpl implements MaintainService {
    @Autowired
    private MaintainRepository maintainRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return maintainRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return maintainRepository;
    }

    @Override
    public MaintainEntity save(MaintainEntity maintain) {
        return maintainRepository.save(maintain);
    }

    @Override
    public void deleteMaintain(MaintainEntity entity) {
        maintainRepository.delete(entity);
    }

    @Override
    public List<MaintainEntity> findAllByFilter(String keyword) {
        return maintainRepository.findAllByFilter("%" + keyword + "%");
    }
}
