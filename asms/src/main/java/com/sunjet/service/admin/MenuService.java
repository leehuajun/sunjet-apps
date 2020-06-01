package com.sunjet.service.admin;

import com.sunjet.model.admin.MenuEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface MenuService extends BaseService {
    MenuEntity save(MenuEntity menuEntity);

    List<MenuEntity> findAllByUserId(String userId);

    void delete(MenuEntity model);

    MenuEntity findMenuByUrl(String url);

    MenuEntity findOneById(String objId);

    List<MenuEntity> findAll02();
}
