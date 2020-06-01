package com.sunjet.service.asm;

import com.sunjet.model.asm.ReportPartEntity;
import com.sunjet.service.base.BaseService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface ReportPartService extends BaseService {
    List<ReportPartEntity> filter(String keyword);

}
