package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;
import com.sunjet.model.base.FlowDocEntity;
import com.sunjet.model.basic.PartEntity;

import javax.persistence.*;

/*
速报配件子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmReportParts")
public class ReportPartEntity extends FlowDocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    private PartEntity part;    // 零件
    private Integer amount = 1;     // 数量
    private String fault;       // 故障模式
    private String comment;     // 备注

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "partId")
    public PartEntity getPart() {
        return part;
    }

    public void setPart(PartEntity part) {
        this.part = part;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Column(length = 200)
    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReportPartEntity() {

    }

    public ReportPartEntity(Integer rowNum) {
        this.rowNum = rowNum;
    }


}
