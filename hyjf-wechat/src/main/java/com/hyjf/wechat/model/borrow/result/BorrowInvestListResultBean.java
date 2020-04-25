package com.hyjf.wechat.model.borrow.result;

import java.util.List;

import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.wechat.base.BaseResultBean;

public class BorrowInvestListResultBean extends BaseResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 8959137696050695655L;

    private List<AppProjectInvestListCustomize> list;
    private String userCount;
    private boolean isEnd;
    private String account;
    public List<AppProjectInvestListCustomize> getList() {
        return list;
    }
    public void setList(List<AppProjectInvestListCustomize> list) {
        this.list = list;
    }
    public String getUserCount() {
        return userCount;
    }
    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
    public boolean isEnd() {
        return isEnd;
    }
    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    
    
}
