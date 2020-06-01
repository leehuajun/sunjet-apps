package com.sunjet.service.admin;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface UserService extends BaseService {

    UserEntity findOneByLogId(String logId);

    List<UserEntity> findAll();

    UserEntity findOne(String userId);

    UserEntity findOneWithRoles(String userId);

    UserEntity save(UserEntity userEntity);

//    void resetPassword(UserEntity userEntity);

    void changePassword(UserEntity userEntity);

    void delete(String userId);

    List<UserEntity> findAllWithRoles();

    List<UserEntity> findAllByDealerCode(String dealerCode);

    List<UserEntity> findAllByAgencyCode(String agencyCode);

    //获得用户姓名
    String findOneWithUserId(String userId);
}
