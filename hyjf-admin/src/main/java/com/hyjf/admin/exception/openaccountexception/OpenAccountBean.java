package com.hyjf.admin.exception.openaccountexception;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminOpenAccountExceptionCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class OpenAccountBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	// 用戶id
	public String userId;
	// 用戶名
	public String userName;
	// 用户手机号
	public String mobile;
	// 推荐人名称
	public String recommendName;
	// 开户状态
	public String accountStatus;
	// 注册时间
	public String regTimeStart;
	// 注册时间
	public String regTimeEnd;
	// 相应的实体的列表
	private List<AdminOpenAccountExceptionCustomize> recordList;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
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

	public List<AdminOpenAccountExceptionCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminOpenAccountExceptionCustomize> recordList) {
		this.recordList = recordList;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

}
