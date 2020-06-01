package com.sunjet.service.asm;

import com.sunjet.model.asm.SupplyEntity;
import com.sunjet.repository.asm.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供货单/发货单
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("supplyService")
public class SupplyServiceImpl implements SupplyService {
    @Override
    public JpaRepository getRepository() {
        return supplyRepository;
    }

    @Autowired
    private SupplyRepository supplyRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return supplyRepository;
    }

    @Override
    public void save(SupplyEntity supplyRequest) {
        supplyRepository.save(supplyRequest);
    }

    @Override
    public SupplyEntity getSupplyByID(String id) {
        return supplyRepository.getOne(id);
    }

    @Override
    public SupplyEntity findSupplyWithPartsById(String objId) {
        return this.supplyRepository.findOneWithPartsById(objId);
    }

    @Override
    public boolean deleteEntity(SupplyEntity supplyRequest) {
        try {
            supplyRepository.delete(supplyRequest);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
