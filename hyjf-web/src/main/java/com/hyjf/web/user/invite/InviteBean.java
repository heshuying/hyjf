package com.hyjf.web.user.invite;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebInviteRecordCustomize;
import com.hyjf.mybatis.model.customize.web.WebRewardRecordCustomize;

public class InviteBean  implements Serializable {

	private static final long serialVersionUID = 193971424806808003L;
	
	public String userId;
	/**
	 * yqjl,邀请记录； jljl，奖励记录
	 */
	public String recordType;
    // 请求处理是否成功
    private boolean status = false;
    //邀请列表
    public List<WebInviteRecordCustomize> inviteList;
    //奖励列表
    public List<WebRewardRecordCustomize> rewardList;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public InviteBean() {
		super();
	}

    public void success() {
        this.status = true;
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
	


	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<WebInviteRecordCustomize> getInviteList() {
		return inviteList;
	}

	public void setInviteList(List<WebInviteRecordCustomize> inviteList) {
		this.inviteList = inviteList;
	}

	public List<WebRewardRecordCustomize> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<WebRewardRecordCustomize> rewardList) {
		this.rewardList = rewardList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
}
