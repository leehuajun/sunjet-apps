package com.sunjet.service.basic;

import com.sunjet.model.basic.NoticeEntity;
import com.sunjet.repository.basic.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
@javax.transaction.Transactional
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return noticeRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return noticeRepository;
    }

    @Override
    public void delete(NoticeEntity entity) {
        noticeRepository.delete(entity);
    }

    @Override
    public NoticeEntity save(NoticeEntity notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public List<NoticeEntity> findAll() {
//        return noticeRepository.findAll();
        return noticeRepository.findAll(new Sort(Sort.Direction.DESC, "publishDate"));
    }
}

