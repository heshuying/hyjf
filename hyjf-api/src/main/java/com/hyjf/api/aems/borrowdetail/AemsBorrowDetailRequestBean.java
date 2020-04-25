package com.hyjf.api.aems.borrowdetail;

import com.hyjf.base.bean.BaseBean;

/**
 * @author  jijun 20180911
 */

public class AemsBorrowDetailRequestBean extends BaseBean{

    //项目标号
    private String borrowNid;

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }
}
