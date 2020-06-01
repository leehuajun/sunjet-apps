package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by lhj on 2015/9/6.
 * 图标实体
 */
@Entity
@Table(name = "SysIcons")
public class IconEntity extends DocEntity {

    private static final long serialVersionUID = -6056297580918896377L;
    private String name;

    @Column(name = "Name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IconEntity() {
    }

    public IconEntity(String name) {
        this.name = name;
    }
}
