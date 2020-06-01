package com.sunjet.service.basic;

import com.sunjet.model.basic.MaintainEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface MaintainService extends BaseService {
    MaintainEntity save(MaintainEntity maintain);

    void deleteMaintain(MaintainEntity entity);

    List<MaintainEntity> findAllByFilter(String keyword);
}
