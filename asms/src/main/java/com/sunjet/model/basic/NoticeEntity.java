package com.sunjet.model.basic;

import com.sunjet.model.base.DocEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 公告
 * <p>
 * Created by lhj on 16/10/23.
 */
@Entity
@Table(name = "BasicNotices")
public class NoticeEntity extends DocEntity {
    private static final long serialVersionUID = -7942079054161894365L;
    private String title;       // 公告标题
    private Date publishDate = new Date(); // 发布时间
    private String publisher;   // 发布人
    private String content;     // 公告内容
    private String fileOriginName;    // 附件原始名称
    private String fileRealName;    // 附件真实路径

    @Column(length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Column(length = 50)
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Column(length = 10000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(length = 200)
    public String getFileOriginName() {
        return fileOriginName;
    }

    public void setFileOriginName(String fileOriginName) {
        this.fileOriginName = fileOriginName;
    }

    @Column(length = 200)
    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }
}
