package com.sunjet.model.flow;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-03-22 15:51
 * @description: 说明
 */
@Entity
@Table(name = "flowComment")
public class CommentEntity {

    private Long id;            // 自增长id
    private String flowInstanceId;  // 流程实例id
    private String userId;          // 审批人id
    private String username;        // 审批人姓名
    private Date doDate;            // 审批时间
    private String result;          // 审批结果
    private String message;         // 审批意见

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FlowinstId", length = 50)
    public String getFlowInstanceId() {
        return flowInstanceId;
    }

    public void setFlowInstanceId(String flowInstanceId) {
        this.flowInstanceId = flowInstanceId;
    }

    @Column(name = "UserId", length = 50)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "Username", length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "DoDate")
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDoDate() {
        return doDate;
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate;
    }

    @Column(name = "Result", length = 200)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Column(name = "Message", length = 2000)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
