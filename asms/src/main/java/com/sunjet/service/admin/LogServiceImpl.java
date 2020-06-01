package com.sunjet.service.admin;

import com.sunjet.model.admin.LogEntity;
import com.sunjet.repository.admin.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return logRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return logRepository;
    }

    /**
     * 获取所有的 log 列表
     *
     * @return
     */
    @Override
    public List<LogEntity> findAll() {
        return logRepository.findAll();
    }

    /**
     * 根据 log 的 id,删除 log
     *
     * @param logId
     */
    @Override
    public void deleteById(String logId) {
        logRepository.delete(logId);
    }
}