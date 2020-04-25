package com.hyjf.admin.manager.activity.twoeleven2018;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiehuili on 2018/10/10.
 */
public class TwoelevenBean  implements Serializable {

    private Integer id;
    /**
     * 账户户名 检索条件
     */
    private String username;
    /**
     * 姓名 检索条件
     */
    private String truename;

    /**
     * 奖励类型 检索条件
     */
    private String rewardType;


    /**
     * 奖励批号 检索条件
     */
    private String rewardId;

    /**
     * 排序 检索条件
     */
    private String sort;
    /**
     * 排序列
     */
    private String col;
    /**
     * 状态 检索条件
     */
    private Integer status;

    /**
     * 手机号 检索条件
     */
    private String mobile;

    private List<TwoelevenCustomize> recordList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public List<TwoelevenCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<TwoelevenCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
