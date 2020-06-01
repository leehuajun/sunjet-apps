package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by lhj on 2015/9/6.
 * 系统日志实体
 */
@Entity
@Table(name = "SysLogs")
public class LogEntity extends DocEntity {
    private static final long serialVersionUID = 1266364102549720670L;
    private String message;

    @Column(name = "Message", length = 200)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
