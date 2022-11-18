package com.shubhamwithcode.mymall.Models;

import java.util.Date;

public class RewardModel {
    private String type;
    private String lowerLimit;
    private String upperLimit;
    private String discORamt;
    private String coupenBody;
    private Date date;
    private boolean alreadyUsed;
    private String coupenId;

    public RewardModel(String coupenId,String type, String lowerLimit, String upperLimit, String discORamt, String coupenBody, Date date, boolean alreadyUsed) {
        this.coupenId = coupenId;
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.discORamt = discORamt;
        this.coupenBody = coupenBody;
        this.date = date;
        this.alreadyUsed = alreadyUsed;
    }

    public String getCoupenId() {
        return coupenId;
    }
    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }
    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }
    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLowerLimit() {
        return lowerLimit;
    }
    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
    public String getUpperLimit() {
        return upperLimit;
    }
    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }
    public String getdiscORamt() {
        return discORamt;
    }
    public void setdiscORamt(String discORamt) {
        this.discORamt = discORamt;
    }
    public String getCoupenBody() {
        return coupenBody;
    }
    public void setCoupenBody(String coupenBody) {
        this.coupenBody = coupenBody;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
