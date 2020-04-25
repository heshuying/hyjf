package com.hyjf.app.user.credit;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;

/**
 * 债转出借记录Bean
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月1日
 * @see 上午10:54:25
 */
public class AppTenderCreditInvestBean extends AppTenderCreditInvestListCustomize implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3517856125381324045L;

    // 债转编号
    private String creditNid;

    /**
     * 翻页机能用的隐藏变量
     */
    private int page = 1;

    /**
     * 翻页功能所用分页大小
     */
    private int pageSize = 10;

    public AppTenderCreditInvestBean() {
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

    public String getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(String creditNid) {
        this.creditNid = creditNid;
    }

}
