package com.sunjet.repository.admin;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by lhj on 2014-07-27 10:05
 * Description:
 */
public interface RoleRepository extends JpaRepository<RoleEntity, String>, JpaSpecificationExecutor<RoleEntity> {
    //  select o from Order o inner join fetch o.orderItems where o.ower.age=26 order by o.orderid
    @Query("select r from RoleEntity r left join fetch r.users left join fetch r.permissions where r.objId=?1")
    public RoleEntity findOneWithUsersAndPermissionsById(String roleId);

    @Query("select r from RoleEntity r left join fetch r.users where r.objId=?1")
    public RoleEntity findOneWithUsersById(String roleId);

    @Query("select r from RoleEntity r left join fetch r.permissions where r.objId=?1")
    public RoleEntity findOneWithPermissionsById(String roleId);

    @Query("select r from RoleEntity r left join fetch r.permissions")
    List<RoleEntity> findAllWithPermissions();

    @Query("select r from RoleEntity r left join fetch r.users")
    List<RoleEntity> findAllWithUsers();

    @Query("select r from RoleEntity r left join fetch r.users left join fetch r.permissions")
    List<RoleEntity> findAllWithUsersAndPermissions();

    @Query("select r from RoleEntity r left join fetch r.users where ?1 member of r.users")
    List<RoleEntity> findAllByUser(UserEntity userEntity);

    @Query("select r from RoleEntity r left join fetch r.users where r.roleId=?1")
    RoleEntity findOneWithUsersByRoleId(String roleId);
}
