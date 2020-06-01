package com.sunjet.service.admin;


import com.sunjet.model.admin.OperationEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/18.
 */
public interface OperationService extends BaseService {
    List<OperationEntity> findAll();

    void deleteById(String operationId);

    OperationEntity findOneById(String objId);

    OperationEntity save(OperationEntity operationEntity);

    void delete(OperationEntity model);
}
