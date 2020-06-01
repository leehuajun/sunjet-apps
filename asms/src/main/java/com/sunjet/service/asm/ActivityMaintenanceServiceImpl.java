package com.sunjet.service.asm;

import com.sunjet.model.asm.ActivityMaintenanceEntity;
import com.sunjet.repository.asm.ActivityMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务活动维修
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("activityMaintenanceService")
public class ActivityMaintenanceServiceImpl implements ActivityMaintenanceService {
    @Autowired
    private ActivityMaintenanceRepository activityMaintenanceRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return activityMaintenanceRepository;
    }

    @Override
    public List<ActivityMaintenanceEntity> findall() {
        return activityMaintenanceRepository.findAll();
    }

    @Override
    public void save(ActivityMaintenanceEntity activityMaintenanceRequest) {
        activityMaintenanceRepository.save(activityMaintenanceRequest);
    }

    @Override
    public JpaRepository getRepository() {
        return activityMaintenanceRepository;
    }

    @Override
    public ActivityMaintenanceEntity findOneWithVehicles(String objId) {
        return activityMaintenanceRepository.findOneById(objId);
    }

    @Override
    public ActivityMaintenanceEntity findOneWithOthers(String objId) {
        return activityMaintenanceRepository.findOneWithOthers(objId);
    }

    @Override
    public void deleteEntity(ActivityMaintenanceEntity entity) {
        activityMaintenanceRepository.delete(entity.getObjId());
    }

    @Override
    public ActivityMaintenanceEntity findOneWithVehicleById(String objId) {
        return activityMaintenanceRepository.findOneWithVehicleById(objId);
    }
}
