package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class NewyearPrizeConfig implements Serializable {
    private Integer id;

    private String prizeName;

    private Integer prizeQuantity;

    private BigDecimal prizeProbability;

    private Integer prizeDrawedCount;

    private Integer prizeOnline;

    private String prizeCouponCode;

    private Integer activityFlg;

    private String remark;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public Integer getPrizeQuantity() {
        return prizeQuantity;
    }

    public void setPrizeQuantity(Integer prizeQuantity) {
        this.prizeQuantity = prizeQuantity;
    }

    public BigDecimal getPrizeProbability() {
        return prizeProbability;
    }

    public void setPrizeProbability(BigDecimal prizeProbability) {
        this.prizeProbability = prizeProbability;
    }

    public Integer getPrizeDrawedCount() {
        return prizeDrawedCount;
    }

    public void setPrizeDrawedCount(Integer prizeDrawedCount) {
        this.prizeDrawedCount = prizeDrawedCount;
    }

    public Integer getPrizeOnline() {
        return prizeOnline;
    }

    public void setPrizeOnline(Integer prizeOnline) {
        this.prizeOnline = prizeOnline;
    }

    public String getPrizeCouponCode() {
        return prizeCouponCode;
    }

    public void setPrizeCouponCode(String prizeCouponCode) {
        this.prizeCouponCode = prizeCouponCode == null ? null : prizeCouponCode.trim();
    }

    public Integer getActivityFlg() {
        return activityFlg;
    }

    public void setActivityFlg(Integer activityFlg) {
        this.activityFlg = activityFlg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}