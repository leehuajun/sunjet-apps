package com.sunjet.repository.admin;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.basic.DealerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @Query("select u from UserEntity u where u.logId=?1 ")
    public UserEntity findOneByLogId(String logId);

    @Query("select u from UserEntity u where ?1 member of u.roles")
    public List<UserEntity> findAllByRole(RoleEntity roleEntity);

    //  public List<UserEntity> findUserListWithRolesByRoleId(String roleId);
//  @Query("select u from UserEntity u where ")
//  public List<UserEntity> findUserListByRoleId(String roleId);
//  select o from Order o inner join fetch o.orderItems where o.ower.age=26 order by o.orderid
    @Query("select u from UserEntity u left join fetch u.roles where u.objId=?1 ")
    public UserEntity findOneWithRoles(String userId);

    @Query("select u from UserEntity u left join fetch u.roles")
    List<UserEntity> findAllWithRoles();

    @Query("select u from UserEntity u where u.dealer<>null and u.dealer.code=?1")
    List<UserEntity> findAllByDealerCode(String dealerCode);

    @Query("select u from UserEntity u where u.agency<>null and u.agency.code=?1")
    List<UserEntity> findAllByAgencyCode(String agencyCode);
}
