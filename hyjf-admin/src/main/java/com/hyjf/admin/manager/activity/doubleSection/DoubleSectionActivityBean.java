package com.hyjf.admin.manager.activity.doubleSection;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.DoubleSectionActivityCustomize;

import java.util.List;

/**
 * @Auther: walter.limeng
 * @Date: 2018/9/11 10:01
 * @Description: DoubleSectionActivityBean
 */
public class DoubleSectionActivityBean {

    /**
     * 账户户名 检索条件
     */
    private String userName;
    /**
     * 姓名 检索条件
     */
    private String trueName;
    /**
     * 手机号码 检索条件
     */
    private String mobile;
    /**
     * 产品类型 检索条件
     */
    private String productType;
    /**
     * 状态类型 检索条件
     */
    private String rewardStatus;
    /**
     * 发放方式类型 检索条件
     */
    private String distributionStatus;
    /**
     * 奖励类型 检索条件
     */
    private String rewardType;
    /**
     * 奖励编号 检索条件
     */
    private String rewardId;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
    private Integer id;

    private List<DoubleSectionActivityCustomize> recordList;

    /**
     * 时间排序 检索条件
     */
    private String sort;
    /**
     * 排序列
     */
    private String col;
    /**
     * 时间排序 检索条件
     */
    private String sortTwo;
    /**
     * 排序列
     */
    private String colTwo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
    }

    public List<DoubleSectionActivityCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<DoubleSectionActivityCustomize> recordList) {
        this.recordList = recordList;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSortTwo() {
        return sortTwo;
    }

    public void setSortTwo(String sortTwo) {
        this.sortTwo = sortTwo;
    }

    public String getColTwo() {
        return colTwo;
    }

    public void setColTwo(String colTwo) {
        this.colTwo = colTwo;
    }

}
