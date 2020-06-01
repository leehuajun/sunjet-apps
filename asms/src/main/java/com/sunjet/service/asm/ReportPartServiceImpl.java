package com.sunjet.service.asm;

import com.sunjet.model.asm.ReportPartEntity;
import com.sunjet.repository.asm.ReportPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("reportPartService")
public class ReportPartServiceImpl implements ReportPartService {
    @Autowired
    private ReportPartRepository reportPartRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return reportPartRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return reportPartRepository;
    }

    @Override
    public List<ReportPartEntity> filter(String keyword) {
        return reportPartRepository.filter(keyword);
    }
}
