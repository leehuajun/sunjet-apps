package com.sunjet.service.admin;

import com.sunjet.model.admin.WebServiceAccessLogEntity;
import com.sunjet.repository.admin.WebServiceAccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("webServiceAccessLogService")
public class WebServiceAccessLogServiceImpl implements WebServiceAccessLogService {

    @Autowired
    private WebServiceAccessLogRepository webServiceAccessLogRepository;

    @Override
    public JpaRepository getRepository() {
        return webServiceAccessLogRepository;
    }

    @Override
    public WebServiceAccessLogEntity findOne(String objId) {
        return webServiceAccessLogRepository.findOne(objId);
    }

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return webServiceAccessLogRepository;
    }
}