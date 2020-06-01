package com.sunjet.service.admin;

import com.sunjet.model.admin.LogEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by lhj on 16/6/18.
 */
public interface LogService extends BaseService {
    List<LogEntity> findAll();

    void deleteById(String logId);
}
