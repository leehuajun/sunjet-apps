package com.sunjet.repository.admin;

import com.sunjet.model.admin.MenuEntity;
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
public interface MenuRepository extends JpaRepository<MenuEntity, String>, JpaSpecificationExecutor<MenuEntity> {

    @Query("select m from MenuEntity m where m.enabled=true order by m.seq asc")
    public List<MenuEntity> findAllByAsc();

    @Query("select m from MenuEntity m where m.url=?1")
    MenuEntity findOneByUrl(String url);

    @Query("select m from MenuEntity m order by m.seq asc")
    public List<MenuEntity> findAll02();
}
