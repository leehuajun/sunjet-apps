package com.sunjet.service.base;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lhj on 16/6/18.
 */
public interface FlowBaseService {
    JpaRepository getRepository();
//    FlowDocEntity save(FlowDocEntity entity);
//    FlowDocEntity findOne(String objId);
}
