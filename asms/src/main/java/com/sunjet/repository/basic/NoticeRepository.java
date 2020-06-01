package com.sunjet.repository.basic;

import com.sunjet.model.basic.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * IconRepository
 * 公告
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface NoticeRepository extends JpaRepository<NoticeEntity, String>, JpaSpecificationExecutor<NoticeEntity> {


}
