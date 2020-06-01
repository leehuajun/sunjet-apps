package com.sunjet.service.basic;

import com.sunjet.model.basic.NoticeEntity;
import com.sunjet.model.basic.VehicleEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface NoticeService extends BaseService {
    void delete(NoticeEntity entity);

    NoticeEntity save(NoticeEntity notice);

    List<NoticeEntity> findAll();
}
