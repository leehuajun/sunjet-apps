package com.sunjet.model.base;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 树节点实体
 */
@MappedSuperclass
public class TreeNodeEntity<T> extends DocEntity {

    private T parent;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "parent_id")
    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }
}
