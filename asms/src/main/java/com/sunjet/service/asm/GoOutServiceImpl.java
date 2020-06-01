package com.sunjet.service.asm;

import com.sunjet.repository.asm.GoOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("goOutService")
public class GoOutServiceImpl implements GoOutService {
    @Autowired
    private GoOutRepository goOutRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return goOutRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return goOutRepository;
    }
}
