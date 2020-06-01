package com.sunjet.service.admin;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.repository.admin.UserRepository;
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
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return userRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return userRepository;
    }

    /**
     * 根据用户登录名获取用户对象
     *
     * @param logId
     * @return
     */
    @Override
    public UserEntity findOneByLogId(String logId) {
        return userRepository.findOneByLogId(logId);
    }

    /**
     * 获取所有UserEntity对象
     *
     * @return
     */
    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据user的objId值获取UserEntity对象, 不加载roles集合
     *
     * @param userId 用户objId
     * @return userEntity 用户对象
     */
    @Override
    public UserEntity findOne(String userId) {
        return userRepository.findOne(userId);
    }

    /**
     * 根据user的objId值获取UserEntity对象, 加载roles集合
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity findOneWithRoles(String userId) {
        return userRepository.findOneWithRoles(userId);
    }

    /**
     * 保存用户对象
     *
     * @param userEntity
     * @return
     */
    @Override
    public UserEntity save(UserEntity userEntity) {
//        UserEntity entity = null;
//        if(StringUtils.isNotBlank(userEntity.getObjId())){
//            entity = userRepository.findOneWithRoles(userEntity.getObjId());
//            BeanUtils.copyProperties(userEntity, entity);
//        }else{
//            entity = userEntity;
//        }
//        entity.setRolesDesc(entity.getRoles().toString().replace("[", "").replace("]", ""));
        return userRepository.save(userEntity);
    }

    /**
     * 设置密码
     *
     * @param userEntity
     */

    @Override
    public void changePassword(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    /**
     * 删除用户
     *
     * @param userId
     */
    @Override
    public void delete(String userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<UserEntity> findAllWithRoles() {
        return userRepository.findAllWithRoles();
    }

    @Override
    public List<UserEntity> findAllByDealerCode(String dealerCode) {
        return userRepository.findAllByDealerCode(dealerCode);
    }

    @Override
    public List<UserEntity> findAllByAgencyCode(String agencyCode) {
        return userRepository.findAllByAgencyCode(agencyCode);
    }

    @Override
    public String findOneWithUserId(String userId) {
        UserEntity userEntity = userRepository.findOneByLogId(userId);
        return userEntity.getName();
    }

//  public ResultDTO<UserEntity> findUserEntityPagingAndSort(SearchDTO searchDTO) {
//
//    CustomQuery<UserEntity> customQuery = new CustomQuery<>(this.userRepository);
//
//    // customQuery.setJpaSpecificationExecutor(this.userRepository);
//
//    Page<UserEntity> page = customQuery.getPageContent(searchDTO);
//
//    ResultDTO<UserEntity> resultDTO = new ResultDTO<UserEntity>();
//    resultDTO.setTotal(page.getTotalElements());
//    resultDTO.setPageNumber(page.getNumber());
//    resultDTO.setPageSize(page.getSize());
//    resultDTO.setNumberOfCurrentPage(page.getNumberOfElements());
//
//    List<UserEntity> userEntityList = page.getContent();
//    resultDTO.setPageContent(new ArrayList<>());
//    for (UserEntity userEntity : userEntityList) {
//      resultDTO.getPageContent().add(userEntity);
//    }
//    return resultDTO;
//  }


}
