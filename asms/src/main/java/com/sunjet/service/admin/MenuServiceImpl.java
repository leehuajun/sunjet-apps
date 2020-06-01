package com.sunjet.service.admin;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.repository.admin.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return menuRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return menuRepository;
    }

    @Override
    public MenuEntity save(MenuEntity menuEntity) {
        return menuRepository.save(menuEntity);
    }

    /**
     * 根据用户ID,获取用户Menu清单
     *
     * @param userId
     * @return
     */
    @Override
    public List<MenuEntity> findAllByUserId(String userId) {
        List<MenuEntity> menuEntityList = null;

        menuEntityList = menuRepository.findAllByAsc();
        return menuEntityList;
    }

    @Override
    public void delete(MenuEntity model) {
        menuRepository.delete(model);
    }

    @Override
    public MenuEntity findMenuByUrl(String url) {
        return menuRepository.findOneByUrl(url);
    }

    @Override
    public MenuEntity findOneById(String objId) {
        return menuRepository.findOne(objId);
    }

    @Override
    public List<MenuEntity> findAll02() {
        return menuRepository.findAll02();
    }
}