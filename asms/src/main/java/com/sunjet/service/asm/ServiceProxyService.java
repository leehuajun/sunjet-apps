package com.sunjet.service.asm;

import com.sunjet.model.asm.ServiceProxyEntity;
import com.sunjet.service.base.BaseService;

/**
 * 服务委托
 * Created by lhj on 16/9/17.
 */
public interface ServiceProxyService extends BaseService {
    void save(ServiceProxyEntity serviceProxyRequest);
}
