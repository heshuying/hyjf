/**
 * Description:用户列表前端查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.manager.user.manageruser;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminUserListCustomize;

/**
 * @author 王坤
 */

public class UserListCustomizeBean extends AdminUserListCustomize implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7851616338153262897L;

	private List<AdminUserListCustomize> recordList;
	private String ids;
	//注册时间 开始
	private String regTimeStart;
	//注册时间 结束
	private String regTimeEnd;
	//电子账号
	private String accountId;
	//用户id
	private String userId;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

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

	public List<AdminUserListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminUserListCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getRegTimeStart() {
		return regTimeStart;
	}

	public void setRegTimeStart(String regTimeStart) {
		this.regTimeStart = regTimeStart;
	}

	public String getRegTimeEnd() {
		return regTimeEnd;
	}

	public void setRegTimeEnd(String regTimeEnd) {
		this.regTimeEnd = regTimeEnd;
	}

    /**
     * ids
     * @return the ids
     */
    
    public String getIds() {
        return ids;
    }

    /**
     * @param ids the ids to set
     */
    
    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * serialversionuid
     * @return the serialversionuid
     */
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
	

}
