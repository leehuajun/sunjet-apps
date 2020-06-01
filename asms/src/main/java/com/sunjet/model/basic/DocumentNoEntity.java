package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by lhj on 16/6/30.
 * 单据基础信息实体
 */
@Entity
@Table(name = "BasicDocumentNo")
public class DocumentNoEntity extends IdEntity {
    private static final long serialVersionUID = 1674940020293536123L;
    private String docName;                             // 单据名称
    private String docKey;                              // 单据Key，实体的类名字
    private String docCode;                             // 单据代码 简写 ，长度不定，长度应小于10位, ZLSB 代表质量速报
    private String lastNoDate = LocalDate.now().toString().replace("-", "");     // 最后一个单据号的日期部分(8位)  20160801
    private String lastNoSerialNumber = "0000";            // 最后一个单据号的流水部分(4位)  0001

    @Column(length = 50)
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Column(length = 50)
    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    @Column(length = 10)
    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    @Column(length = 8)
    public String getLastNoDate() {
        return lastNoDate;
    }

    public void setLastNoDate(String lastNoDate) {
        this.lastNoDate = lastNoDate;
    }

    @Column(length = 4)
    public String getLastNoSerialNumber() {
        return lastNoSerialNumber;
    }

    public void setLastNoSerialNumber(String lastNoSerialNumber) {
        this.lastNoSerialNumber = lastNoSerialNumber;
    }

    public DocumentNoEntity() {
    }

    public DocumentNoEntity(String docName, String docCode) {
        this.docName = docName;
        this.docCode = docCode;
    }

    public DocumentNoEntity(String docName, String docCode, String lastNoDate) {
        this.docName = docName;
        this.docCode = docCode;
        this.lastNoDate = lastNoDate;
    }

    public DocumentNoEntity(String docName, String docCode, String lastNoDate, String lastNoSerialNumber) {
        this.docName = docName;
        this.docCode = docCode;
        this.lastNoDate = lastNoDate;
        this.lastNoSerialNumber = lastNoSerialNumber;
    }
}
