package com.sunjet.model.admin;


import com.sunjet.model.base.TreeNodeEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.rmi.server.ObjID;

/**
 * Created by zhaoyehai02 on 2016-06-08.
 * 字典
 */
@Entity
@Table(name = "SysDictionaries")
public class DictionaryEntity extends TreeNodeEntity<DictionaryEntity> {
    private static final long serialVersionUID = 226806555109100262L;
    private String name;//名称
    private String code;//编码
    private Integer seq; // 排序
    private String value;   // 值

//    private String value;//值
//    private DictionaryEntity parent;//父节点

    @Column(name = "item_name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "item_code", length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "item_value", length = 50)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }


    public DictionaryEntity() {
    }

    public DictionaryEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public DictionaryEntity(String name, String code, Integer seq) {
        this.name = name;
        this.code = code;
        this.seq = seq;
    }

    public DictionaryEntity(String name, DictionaryEntity parent) {
        this.name = name;
        this.setParent(parent);
    }

    public DictionaryEntity(String name, String code, DictionaryEntity parent) {
        this.name = name;
        this.code = code;
        this.setParent(parent);
    }

    public DictionaryEntity(String name, String code, Integer seq, DictionaryEntity parent) {
        this.name = name;
        this.code = code;
        this.seq = seq;
        this.setParent(parent);
    }

    public DictionaryEntity(String name, String code, Integer seq, String value, DictionaryEntity parent) {
        this.name = name;
        this.code = code;
        this.seq = seq;
        this.value = value;
        this.setParent(parent);
    }


    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
//        if(this.getObjId()==null || ((DictionaryEntity) o).getObjId()==null)
//            return false;

        DictionaryEntity that = (DictionaryEntity) o;

        return new org.apache.commons.lang.builder.EqualsBuilder()
                .append(this.getObjId(), that.getObjId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37)
                .append(this.getObjId())
                .toHashCode();
    }
}
