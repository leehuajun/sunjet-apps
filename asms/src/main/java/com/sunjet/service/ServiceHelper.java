package com.sunjet.service;

import com.sunjet.model.admin.UserEntity;
import com.sunjet.model.agency.AgencyAdmitRequestEntity;
import com.sunjet.model.agency.AgencyAlterRequestEntity;
import com.sunjet.model.basic.DealerEntity;
import com.sunjet.model.flow.LeaveBill;
import com.sunjet.service.admin.UserService;
import com.sunjet.service.agency.AgencyAdmitService;
import com.sunjet.service.agency.AgencyAlterService;
import com.sunjet.service.base.FlowBaseService;
import com.sunjet.service.basic.DealerService;
import com.sunjet.service.flow.LeaveBillService;
import com.sunjet.utils.common.CommonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/10/20.
 */
@Component("serviceHelper")
public class ServiceHelper {
    @Autowired
    private DealerService dealerService;
    @Autowired
    private UserService userService;

    public List<DealerEntity> getDealers(String userId, String keyword) {
        List<DealerEntity> dealers = new ArrayList<>();
        UserEntity user = userService.findOne(userId);
        if (user.getUserType().equalsIgnoreCase("wuling")) {   // 五菱的用户
            dealers = dealerService.findAllByKeyword("%" + keyword + "%");
        } else if (user.getUserType().equalsIgnoreCase("dealer")) {   // 服务站用户
            if (user.getDealer().getParent() == null) {   // 是一级服务站
                dealers = dealerService.findChildrenByParentIdAndFilter(user.getDealer().getObjId(), keyword);
                if (dealers == null) {
                    dealers = new ArrayList<>();
                }
                dealers.add(user.getDealer());
            }
        }
        return dealers;
    }
}
