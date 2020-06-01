package com.sunjet.service.asm;

import com.sunjet.model.asm.WarrantyMaintenanceEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.repository.asm.WarrantyMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 三包维修
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("warrantyMaintenanceService")
public class WarrantyMaintenanceServiceImpl implements WarrantyMaintenanceService {
    @Autowired
    private WarrantyMaintenanceRepository warrantyMaintenanceRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return warrantyMaintenanceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return warrantyMaintenanceRepository;
    }

    @Override
    public void save(WarrantyMaintenanceEntity warrantyMaintenanceRequest) {
        warrantyMaintenanceRepository.save(warrantyMaintenanceRequest);
    }

    @Override
    public WarrantyMaintenanceEntity findOneWithOthersById(String objId) {
        return warrantyMaintenanceRepository.findOneWithOthersById(objId);
    }

    @Override
    public void deleteEntity(WarrantyMaintenanceEntity entity) {
        warrantyMaintenanceRepository.delete(entity);
    }

    @Override
    public WarrantyMaintenanceEntity findOneWithVehicleById(String objId) {
        return warrantyMaintenanceRepository.findOneWithVehicleById(objId);
    }

    @Override
    public WarrantyMaintenanceEntity findOneByProcessInstanceId(String processInstanceId) {
        return warrantyMaintenanceRepository.findOneByProcessInstanceId(processInstanceId);
    }

    @Override
    public VehicleEntity findOneVehicleByDocNo(String docNo) {
        return warrantyMaintenanceRepository.findOneVehicleByDocNo(docNo);
    }

    @Override
    public WarrantyMaintenanceEntity findOneWithOthersBySrcDocNo(String srcDocNo) {
        return warrantyMaintenanceRepository.findOneWithOthersBySrcDocNo(srcDocNo);
    }
}
