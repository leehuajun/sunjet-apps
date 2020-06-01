package com.sunjet.service.flow;

import com.sunjet.model.flow.LeaveBill;
import com.sunjet.repository.flow.LeaveBillRepository;
import com.sunjet.service.base.FlowBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * Created by lhj on 16/10/17.
 */
@Service("leaveBillService")
public class LeaveBillServiceImpl implements LeaveBillService {
    @Autowired
    private LeaveBillRepository leaveBillRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return this.leaveBillRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return this.leaveBillRepository;
    }

    @Override
    public LeaveBill saveLeaveBill(LeaveBill leaveBill) {
        return leaveBillRepository.save(leaveBill);
    }

    @Override
    public LeaveBill findOne(String objId) {
        return leaveBillRepository.findOne(objId);
    }


}
