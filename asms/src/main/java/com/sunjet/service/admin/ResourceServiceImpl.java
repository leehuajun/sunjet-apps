package com.sunjet.service.admin;

import com.sunjet.model.admin.ResourceEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.repository.admin.PermissionRepository;
import com.sunjet.repository.admin.ResourceRepository;
import com.sunjet.repository.admin.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return resourceRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return resourceRepository;
    }

    @Override
    public ResourceEntity save(ResourceEntity resourceEntity) {
        return resourceRepository.save(resourceEntity);
    }

    @Override
    public List<ResourceEntity> findAll() {
        return this.resourceRepository.findAll();
    }

    @Override
    public void delete(ResourceEntity model) {
        String sql = "delete from sys_role_permission where permission_id in(select obj_id from sys_permissions where permission_code like '" + model.getCode() + ":%')";
        jdbcTemplate.execute(sql);
        sql = "delete from sys_permissions where permission_code like '" + model.getCode() + ":%'";
        jdbcTemplate.execute(sql);
        resourceRepository.delete(model);
//        List<RoleEntity> roleEntities = roleRepository.findAllWithPermissions();
//        for (RoleEntity roleEntity : roleEntities) {
//            roleEntity.setPermissionsDesc(roleEntity.getPermissions().toString().replace("[", "").replace("]", ""));
//            roleRepository.save(roleEntity);
//        }
    }

    @Override
    public ResourceEntity findOneWithOperationsById(String objId) {
        return resourceRepository.findOneWithOperationsById(objId);
    }

    @Override
    public List<ResourceEntity> findAllWithOperations() {
        return resourceRepository.findAllWithOperations();
    }
}