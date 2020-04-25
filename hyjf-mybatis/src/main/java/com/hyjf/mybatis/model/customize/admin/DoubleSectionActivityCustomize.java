package com.hyjf.mybatis.model.customize.admin;

/**
 * @Auther: walter.limeng
 * @Date: 2018/9/11 10:39
 * @Description: DoubleSectionActivityCustomize
 */
public class DoubleSectionActivityCustomize {
    /**
     * 序列化id
     */

    private static final long serialVersionUID = 7627555508742735666L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 姓名
     */
    private String trueName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 出借订单号
     */
    private String orderId;
    /**
     * 单笔出借金额
     */
    private String investMoney;
    /**
     * 产品类型
     */
    private String productType;
    /**
     * 产品编号
     */
    private String borrowNid;
    /**
     * 产品期限
     */
    private String productStyle;
    /**
     * 奖励名称
     */
    private String rewardName;
    /**
     * 奖励类型
     */
    private String rewardType;
    /**
     * 出借时间
     */
    private String investTime;
    /**
     * 奖励批号
     */
    private String rewardId;
    /**
     * 发放方式
     */
    private String distributionStatus;
    /**
     * 状态
     */
    private String rewardStatus;
    /**
     * 发放时间
     */
    private String updateTime;
    /**
     * 获得时间
     */
    private String createTime;
    /**
     * 获得时间
     */
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(String productStyle) {
        this.productStyle = productStyle;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }
}
