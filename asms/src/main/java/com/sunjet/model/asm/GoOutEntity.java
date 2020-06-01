package com.sunjet.model.asm;

import com.sunjet.model.base.DocEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务委托单出外子行
 * Created by lhj on 16/9/15.
 */
@Entity
@Table(name = "AsmGoOuts")
public class GoOutEntity extends DocEntity {
    private static final long serialVersionUID = 8634488924832022123L;
    private Integer rowNum;     // 行号
    private String place;    // 外出地点
    private Double mileage = 0.0;     // 单向里程
    private Double tranCosts = 0.0;     // 交通费用
    private Double trailerMileage = 0.0;     // 拖车里程
    private Double trailerCost = 0.0;     // 拖车费用
    private int outGoNum = 0;    // 外出人数
    private Double outGoDay = 0.0;    // 外出天数
    private Double personnelSubsidy = 0.0;    // 人员补贴
    private Double nightSubsidy = 0.0;    // 住宿补贴
    private Double timeSubsidy = 0.0;    // 外出工时费用
    private Double goOutSubsidy = 0.0;    // 外出补贴费用
    private Double amountCost = 0.0;      // 行汇总金额
    private String outGoPicture;    //外出凭证照片
    private String comment;     // 备注

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Column(length = 200)
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getTranCosts() {
        return tranCosts;
    }

    public void setTranCosts(Double tranCosts) {
        this.tranCosts = tranCosts;
    }

    public Double getTrailerMileage() {
        return trailerMileage;
    }

    public void setTrailerMileage(Double trailerMileage) {
        this.trailerMileage = trailerMileage;
    }

    public Double getTrailerCost() {
        return trailerCost;
    }

    public void setTrailerCost(Double trailerCost) {
        this.trailerCost = trailerCost;
    }

    public int getOutGoNum() {
        return outGoNum;
    }

    public void setOutGoNum(int outGoNum) {
        this.outGoNum = outGoNum;
    }

    public Double getOutGoDay() {
        return outGoDay;
    }

    public void setOutGoDay(Double outGoDay) {
        this.outGoDay = outGoDay;
    }

    public Double getPersonnelSubsidy() {
        return personnelSubsidy;
    }

    public void setPersonnelSubsidy(Double personnelSubsidy) {
        this.personnelSubsidy = personnelSubsidy;
    }

    public Double getNightSubsidy() {
        return nightSubsidy;
    }

    public void setNightSubsidy(Double nightSubsidy) {
        this.nightSubsidy = nightSubsidy;
    }

    public Double getTimeSubsidy() {
        return timeSubsidy;
    }

    public void setTimeSubsidy(Double timeSubsidy) {
        this.timeSubsidy = timeSubsidy;
    }

    public Double getGoOutSubsidy() {
        return goOutSubsidy;
    }

    public void setGoOutSubsidy(Double goOutSubsidy) {
        this.goOutSubsidy = goOutSubsidy;
    }

    public Double getAmountCost() {
        return amountCost;
    }

    public void setAmountCost(Double amountCost) {
        this.amountCost = amountCost;
    }

    public String getOutGoPicture() {
        return outGoPicture;
    }

    public void setOutGoPicture(String outGoPicture) {
        this.outGoPicture = outGoPicture;
    }

    @Column(length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
