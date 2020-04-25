package com.hyjf.app.user.credit;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.app.AppRepayPlanListCustomize;

/**
 * 
 * 还款计划ListBean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月5日
 * @see 下午1:44:53
 */
public class RepayPlanListBean extends AppRepayPlanListCustomize implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 3278149257478770256L;

    /** 用户id */
    private String userId;

    /** 认购单号 */
    private String assignNid;

    /** 债转编号 */
    private String creditNid;

    /**
     * 翻页机能用的隐藏变量
     */
    private int page = 1;

    /**
     * 翻页机能用的隐藏变量
     */
    private int pageSize = 10;

    public RepayPlanListBean() {
        super();
    }

    public int getPage() {
        if (page == 0) {
            page = 1;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        if (pageSize == 0) {
            pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssignNid() {
        return assignNid;
    }

    public void setAssignNid(String assignNid) {
        this.assignNid = assignNid;
    }

    public String getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }

}
