package com.hyjf.api.server.user.withdraw;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.thirdparty.UserWithdrawRecordCustomize;

public class UserWithdrawRecordResultBean extends BaseResultBean {
	
	/**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3709370958884607483L;
    private List<UserWithdrawRecordCustomize> recordList;
    public List<UserWithdrawRecordCustomize> getRecordList() {
        return recordList;
    }
    public void setRecordList(List<UserWithdrawRecordCustomize> recordList) {
        this.recordList = recordList;
    }
    
	
	
}
