package com.sunjet.service.admin;

import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface ResourceService extends BaseService {
    ResourceEntity save(ResourceEntity resourceEntity);

    List<ResourceEntity> findAll();

    void delete(ResourceEntity model);

    ResourceEntity findOneWithOperationsById(String objId);

    List<ResourceEntity> findAllWithOperations();
}
