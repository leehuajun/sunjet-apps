package com.sunjet.service.flow;

import com.sunjet.model.flow.LeaveBill;
import com.sunjet.service.base.BaseService;
import com.sunjet.service.base.FlowBaseService;

/**
 * Created by lhj on 16/10/17.
 */
public interface LeaveBillService extends FlowBaseService, BaseService {
    LeaveBill saveLeaveBill(LeaveBill leaveBill);

    LeaveBill findOne(String objId);
}
