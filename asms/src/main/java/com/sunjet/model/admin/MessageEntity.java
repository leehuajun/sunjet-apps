package com.sunjet.model.admin;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: lhj
 * @create: 2017-04-28 13:59
 * @description: 说明
 */
@Entity
@Table(name = "SysMessages")
public class MessageEntity extends DocEntity {
    private String title;
    private String content;
    private String dealerCode;
    private String url;

    @Column(name = "Title", length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "Content", length = 2000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "DealerCode", length = 100)
    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    @Column(name = "Url", length = 200)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MessageEntity(String title, String content, String dealerCode, String url) {
        this.title = title;
        this.content = content;
        this.dealerCode = dealerCode;
        this.url = url;
    }
}
