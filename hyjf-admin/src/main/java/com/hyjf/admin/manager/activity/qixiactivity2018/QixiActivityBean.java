package com.hyjf.admin.manager.activity.qixiactivity2018;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.QixiActivityCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * @author by xiehuili on 2018/7/23.
 */
public class QixiActivityBean implements Serializable {

    /**
     * 账户户名 检索条件
     */
    private String username;
    /**
     * 姓名 检索条件
     */
    private String truename;

    /**
     * 时间排序 检索条件
     */
    private String sort;
    /**
     * 发放时间排序 检索条件
     */
    private String sortTwo;
    /**
     * 排序列
     */
    private String col;
    /**
     * 排序列
     */
    private String colTwo;
    /**
     * 账户名 检索条件
     */
    private String userid;
    /**
     * 手机号 检索条件
     */
    private String mobile;
    /**
     * 产品类型 检索条件
     */
    private String type;

    /**
     * 奖励类型 检索条件
     */
    private String awardType;

    /**
     * 奖励批号 检索条件
     */
    private String awardNum;
    /**
     * 状态 检索条件
     */
    private Integer status;

    /**
     * 发放方式 检索条件
     */
    private Integer grandWay;

    public String getColTwo() {
        return colTwo;
    }

    public void setColTwo(String colTwo) {
        this.colTwo = colTwo;
    }

    private List<QixiActivityCustomize> recordList;

    public List<QixiActivityCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<QixiActivityCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAwardType() {
        return awardType;
    }

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }

    public String getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(String awardNum) {
        this.awardNum = awardNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGrandWay() {
        return grandWay;
    }

    public void setGrandWay(Integer grandWay) {
        this.grandWay = grandWay;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getSortTwo() {
        return sortTwo;
    }

    public void setSortTwo(String sortTwo) {
        this.sortTwo = sortTwo;
    }

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public int getPaginatorPage() {
        if (paginatorPage == 0) {
            paginatorPage = 1;
        }
        return paginatorPage;
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
}
