package com.sunjet.service.asm;

import com.sunjet.model.asm.FirstMaintenanceEntity;
import com.sunjet.repository.asm.FirstMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首保
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("firstMaintenanceService")
public class FirstMaintenanceServiceImpl implements FirstMaintenanceService {

    @Autowired
    private FirstMaintenanceRepository firstMaintenanceRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return firstMaintenanceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return firstMaintenanceRepository;
    }

    @Override
    public void save(FirstMaintenanceEntity firstMaintenanceRequest) {
        firstMaintenanceRepository.save(firstMaintenanceRequest);
    }

    @Override
    public List<FirstMaintenanceEntity> firsAll() {
        return firstMaintenanceRepository.findAll();
    }

    @Override
    public FirstMaintenanceEntity findOneById(String businessId) {
        return firstMaintenanceRepository.findOne(businessId);
    }

    @Override
    public FirstMaintenanceEntity findOneWithGoOutsById(String objId) {
        return firstMaintenanceRepository.findOneWithGoOutsById(objId);
    }

    @Override
    public void deleteEntity(FirstMaintenanceEntity entity) {
        firstMaintenanceRepository.delete(entity.getObjId());
    }

}
