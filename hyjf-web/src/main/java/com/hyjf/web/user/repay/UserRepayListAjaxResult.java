/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web.user.repay;

import java.util.Date;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

/**
 * 用户借款列表ajax返回值对象
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月29日
 * @see 上午9:42:19
 */
public class UserRepayListAjaxResult extends WebBaseAjaxResultBean {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3091714256051407464L;

    public List<WebUserRepayProjectListCustomize> getReplayList() {
        return replayList;
    }
    //用户角色
    public String roleId;
    //当前时间（用于校验担保机构最多只能提前三天还款）
    public Date nowdate;

    public void setReplayList(List<WebUserRepayProjectListCustomize> replayList) {
        this.replayList = replayList;
    }

    private List<WebUserRepayProjectListCustomize> replayList;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Date getNowdate() {
		return nowdate;
	}

	public void setNowdate(Date nowdate) {
		this.nowdate = nowdate;
	}

}
