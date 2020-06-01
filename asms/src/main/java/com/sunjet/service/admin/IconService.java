package com.sunjet.service.admin;

import com.sunjet.model.admin.IconEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
public interface IconService extends BaseService {
    List<IconEntity> findAll();

    void deleteById(String iconId);
}
