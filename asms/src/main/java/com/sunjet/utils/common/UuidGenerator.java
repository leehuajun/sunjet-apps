package com.sunjet.utils.common;

import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * Created by lhj on 16/9/22.
 */
public class UuidGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return UUIDHelper.newUuid();
    }
}
