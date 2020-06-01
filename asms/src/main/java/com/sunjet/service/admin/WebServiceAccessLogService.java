package com.sunjet.service.admin;

import com.sunjet.model.admin.WebServiceAccessLogEntity;
import com.sunjet.service.base.BaseService;

/**
 * Created by lhj on 16/6/17.
 */
public interface WebServiceAccessLogService extends BaseService {
    WebServiceAccessLogEntity findOne(String objId);
//  List<WebServiceAccessLogEntity> findAll();
}
