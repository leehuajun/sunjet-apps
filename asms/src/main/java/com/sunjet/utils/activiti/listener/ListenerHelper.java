package com.sunjet.utils.activiti.listener;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.service.base.BaseService;

import java.lang.reflect.Field;

/**
 * Created by lhj on 16/11/24.
 */
public class ListenerHelper {
    public static UserEntity getServiceManager(BaseService service, String objId) throws NoSuchFieldException, IllegalAccessException {
        Object obj = service.getRepository().findOne(objId);
        Field field = obj.getClass().getField("serviceManager");
        if (field != null) {
            return (UserEntity) field.get(obj);
        } else {
            return null;
        }
    }
}
