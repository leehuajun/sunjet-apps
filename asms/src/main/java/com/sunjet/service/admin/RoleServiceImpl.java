package com.sunjet.service.admin;

import com.sunjet.model.admin.RoleEntity;
import com.sunjet.model.admin.UserEntity;
import com.sunjet.repository.admin.RoleRepository;
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
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return roleRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return roleRepository;
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public RoleEntity findOne(String roleId) {
        return roleRepository.findOne(roleId);
    }

    @Override
    public RoleEntity findOneWithUsersAndPermissionsById(String roleId) {
        return roleRepository.findOneWithUsersAndPermissionsById(roleId);
    }

    @Override
    public RoleEntity findOneWithUsersById(String roleId) {
        return roleRepository.findOneWithUsersById(roleId);
    }

    @Override
    public RoleEntity findOneWithPermissionsById(String roleId) {
        return roleRepository.findOneWithPermissionsById(roleId);
    }

    @Override
    public RoleEntity save(RoleEntity roleEntity) {
        RoleEntity role = roleRepository.save(roleEntity);
        return roleRepository.findOneWithUsersAndPermissionsById(role.getObjId());
    }

    @Override
    public void removeUsersFromRole(String roleId) {
        String sql = "delete from sys_user_role where role_id='" + roleId + "'";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void addUsersToRole(RoleEntity roleEntity) {
        String sql = "INSERT into sys_user_role(user_id,role_id) values(?,?)";
//    jdbcTemplate.execute(sql);
//    List<UserEntity> ums = (List<UserEntity>) roleEntity.getUsers();
        if (roleEntity.getUsers().size() == 0)
            return;

        List<UserEntity> userEntityList = new ArrayList<>();
        for (UserEntity um : roleEntity.getUsers()) {
            userEntityList.add(um);
        }
//    List<PermissionEntity> pms = permissionRepository.getPermissionsByResourceCode(code + ":%");
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
//        UserEntity um = ((List<UserEntity>) roleEntity.getUsers()).get(i);
                preparedStatement.setString(1, userEntityList.get(i).getObjId());
                preparedStatement.setString(2, roleEntity.getObjId());
            }

            @Override
            public int getBatchSize() {
                return roleEntity.getUsers().size();
            }
        });
    }

    @Override
    public List<RoleEntity> findAllByUser(UserEntity userEntity) {
        return roleRepository.findAllByUser(userEntity);
    }

    @Override
    public List<RoleEntity> findAllWithUsers() {
        return roleRepository.findAllWithUsers();
    }

    @Override
    public void delete(String objId) {
        jdbcTemplate.execute("delete from sys_user_role where role_id='" + objId + "'");
        roleRepository.delete(objId);
    }

    @Override
    public RoleEntity findOneWithUsersByRoleId(String roleId) {
        return roleRepository.findOneWithUsersByRoleId(roleId);
    }

//    @Override
//    public void updateRoles() {
//        List<RoleEntity> roles = roleRepository.findAllWithUsers();
//        for (RoleEntity role : roles) {
//            role.setUsersDesc(role.getUsers().toString().replace("[", "").replace("]", ""));
//            roleRepository.save(role);
//        }
//    }
}
