package com.sunjet.service.admin;

import com.sunjet.model.admin.OperationEntity;
import com.sunjet.repository.admin.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("operationService")
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return operationRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return operationRepository;
    }

    /**
     * 获取所有的 operation 列表
     *
     * @return
     */
    @Override
    public List<OperationEntity> findAll() {
        return operationRepository.findAll();
    }

    /**
     * 根据 operation 的 id,删除 operation
     *
     * @param operationId
     */
    @Override
    public void deleteById(String operationId) {
        operationRepository.delete(operationId);
    }

    @Override
    public OperationEntity findOneById(String objId) {
        return operationRepository.findOne(objId);
    }

    @Override
    public OperationEntity save(OperationEntity operationEntity) {
        return operationRepository.save(operationEntity);
    }

    @Override
    public void delete(OperationEntity model) {
        operationRepository.delete(model);
    }
}