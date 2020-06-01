package com.sunjet.repository.flow;

import com.sunjet.model.flow.LeaveBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by lhj on 16/9/18.
 */
public interface LeaveBillRepository extends JpaRepository<LeaveBill, String>, JpaSpecificationExecutor<LeaveBill> {
}
