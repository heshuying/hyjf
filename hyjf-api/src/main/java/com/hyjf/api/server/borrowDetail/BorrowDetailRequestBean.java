package com.hyjf.api.server.borrowDetail;

import com.hyjf.base.bean.BaseBean;

/**
 * @author  fuqiang
 */

public class BorrowDetailRequestBean extends BaseBean{

    //项目标号
    private String borrowNid;

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
}
