package com.sunjet.service.asm;

import com.sunjet.model.asm.ReportVehicleEntity;
import com.sunjet.service.base.BaseService;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface QrVehicleService extends BaseService {
    java.util.List<ReportVehicleEntity> filter(String keyword);

}
