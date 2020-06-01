package com.sunjet.service.admin;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface RoleService extends BaseService {
    List<RoleEntity> findAll();

    RoleEntity findOne(String roleId);

    RoleEntity findOneWithUsersAndPermissionsById(String roleId);

    RoleEntity findOneWithUsersById(String roleId);

    RoleEntity findOneWithPermissionsById(String roleId);

    RoleEntity save(RoleEntity roleEntity);

    void removeUsersFromRole(String roleId);

    void addUsersToRole(RoleEntity roleEntity);

    List<RoleEntity> findAllByUser(UserEntity userEntity);

    List<RoleEntity> findAllWithUsers();

    void delete(String objId);

    RoleEntity findOneWithUsersByRoleId(String roleId);

//    void updateRoles();

//  List<UserEntity> findUsersByRoleId(String roleId);
}
