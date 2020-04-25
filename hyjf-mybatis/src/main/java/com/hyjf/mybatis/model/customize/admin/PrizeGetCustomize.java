package com.hyjf.mybatis.model.customize.admin;

import java.math.BigDecimal;

/**
 * 
 * 奖品配置
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年9月28日
 * @see 下午4:32:14
 */
public class PrizeGetCustomize {
    private Integer id;
    
    private String prizeName;
    
    /** 奖品数量*/
    private Integer prizeQuantity;
    
    /** 所需推荐星数量*/
    private Integer recommendQuantity;
    
    /** 剩余奖品数量*/
    private Integer prizeReminderQuantity;
    
    /** 奖品分组code*/
    private String prizeGroupCode;
    
    /** 奖品类型*/
    private String prizeType;
    
    /** 优惠券编号*/
    private String couponCode;
    
    /** 中奖几率*/
    private BigDecimal prizeProbability;
    
    /** 奖品图片地址*/
    private String prizePicUrl;
    
    /** 奖品获取方式*/
    private String prizeKind;
    
    /** 奖品排序值*/
    private Integer prizeSort;
    
    /** 奖品状态*/
    private String prizeStatus;
    
    /** 成功提示消息*/
    private String successMessage;
    
    private String remark;

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
        this.prizeName = prizeName;
    }

    public Integer getPrizeQuantity() {
        return prizeQuantity;
    }

    public void setPrizeQuantity(Integer prizeQuantity) {
        this.prizeQuantity = prizeQuantity;
    }

    public Integer getRecommendQuantity() {
        return recommendQuantity;
    }

    public void setRecommendQuantity(Integer recommendQuantity) {
        this.recommendQuantity = recommendQuantity;
    }

    public Integer getPrizeReminderQuantity() {
        return prizeReminderQuantity;
    }

    public void setPrizeReminderQuantity(Integer prizeReminderQuantity) {
        this.prizeReminderQuantity = prizeReminderQuantity;
    }

    public String getPrizeGroupCode() {
        return prizeGroupCode;
    }

    public void setPrizeGroupCode(String prizeGroupCode) {
        this.prizeGroupCode = prizeGroupCode;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getPrizeProbability() {
        return prizeProbability;
    }

    public void setPrizeProbability(BigDecimal prizeProbability) {
        this.prizeProbability = prizeProbability;
    }

    public String getPrizePicUrl() {
        return prizePicUrl;
    }

    public void setPrizePicUrl(String prizePicUrl) {
        this.prizePicUrl = prizePicUrl;
    }

    public String getPrizeKind() {
        return prizeKind;
    }

    public void setPrizeKind(String prizeKind) {
        this.prizeKind = prizeKind;
    }

    public Integer getPrizeSort() {
        return prizeSort;
    }

    public void setPrizeSort(Integer prizeSort) {
        this.prizeSort = prizeSort;
    }

    public String getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(String prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    
}

	