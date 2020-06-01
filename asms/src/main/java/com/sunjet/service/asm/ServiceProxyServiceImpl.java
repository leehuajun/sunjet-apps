package com.sunjet.service.asm;

import com.sunjet.model.asm.ServiceProxyEntity;
import com.sunjet.repository.asm.ServiceProxyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务委托
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("serviceProxyService")
public class ServiceProxyServiceImpl implements ServiceProxyService {
    @Autowired
    private ServiceProxyRepository serviceProxyRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return serviceProxyRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return serviceProxyRepository;
    }

    @Override
    public void save(ServiceProxyEntity serviceProxyRequest) {
        serviceProxyRepository.save(serviceProxyRequest);
    }
}
