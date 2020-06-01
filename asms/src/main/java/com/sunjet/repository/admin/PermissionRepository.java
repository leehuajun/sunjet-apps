package com.sunjet.repository.admin;

import com.sunjet.model.admin.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface PermissionRepository extends JpaRepository<PermissionEntity, String>, JpaSpecificationExecutor<PermissionEntity> {

    @Query("select p from PermissionEntity p where p.permissionCode like concat(?1,':%')")
    public List<PermissionEntity> getPermissionsByResourceCode(String resourceCode);

//    @Modifying
//    @Query(value = "delete from sys_permissions where permission_code like ?1", nativeQuery = true)
//    public void deleteAllByResourceCode(String code);

    @Modifying
    @Query("delete from PermissionEntity pe where pe.permissionCode like ?1")
    public void deleteAllByResourceCode(String code);

}
