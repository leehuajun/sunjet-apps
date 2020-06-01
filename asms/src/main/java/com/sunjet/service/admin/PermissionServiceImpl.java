package com.sunjet.service.admin;

import com.sunjet.model.admin.PermissionEntity;
import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.repository.admin.PermissionRepository;
import com.sunjet.repository.admin.RoleRepository;
import com.sunjet.repository.admin.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return permissionRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return permissionRepository;
    }

    @Override
    public List<PermissionEntity> findAll() {
        return this.permissionRepository.findAll();
    }

    @Override
    public List<PermissionEntity> findAllByResourceCode(String resourceCode) {
        return this.permissionRepository.getPermissionsByResourceCode(resourceCode);
    }

    @Override
    public List<String> findAllByUserId(String userId) {
//        List<PermissionEntity> pmList = new ArrayList<>();
        List<String> pmList = new ArrayList<>();
        Set<String> pmSet = new HashSet<>();
        UserEntity userEntity = userRepository.findOneWithRoles(userId);

        for (RoleEntity roleEntity : userEntity.getRoles()) {
            RoleEntity rm = roleRepository.findOneWithPermissionsById(roleEntity.getObjId());
            for (PermissionEntity permissionEntity : rm.getPermissions()) {
                pmSet.add(permissionEntity.getPermissionCode());
            }
        }

        for (String str : pmSet) {
            pmList.add(str);
        }
        return pmList;
    }

    @Override
    public void delete(PermissionEntity permissionEntity) {
        permissionRepository.delete(permissionEntity);
    }

    @Override
    public void save(PermissionEntity permissionEntity) {
        permissionRepository.save(permissionEntity);
    }

    @Override
    public void deleteAllByResourceCode(String code) {
        String sql = "delete from sys_role_permission where permission_code=?";
        List<PermissionEntity> pms = permissionRepository.getPermissionsByResourceCode(code + ":%");
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, pms.get(i).getObjId());
            }

            @Override
            public int getBatchSize() {
                return pms.size();
            }
        });
//    StringBuffer sb = new StringBuffer();
//    List<String> sqls = new ArrayList<>();
//    for(PermissionEntity model: pms){
//      sqls.add("delete from sys_role_permission where permission_code='"+ model.getObjId() +"'");
//    }
//    jdbcTemplate.batchUpdate()
        permissionRepository.deleteAllByResourceCode(code + ":%");
    }
}