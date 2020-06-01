package com.sunjet.service.admin;

import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface PermissionService extends BaseService {
    List<PermissionEntity> findAll();

    List<PermissionEntity> findAllByResourceCode(String resourceCode);

    List<String> findAllByUserId(String userId);

    void delete(PermissionEntity permissionEntity);

    void save(PermissionEntity permissionEntity);

    void deleteAllByResourceCode(String code);

}
