package com.hyjf.admin.manager.activity.listed2;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.ActdecListedTwoCustomize;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2018/01/31 17:00
 * @version V1.0  
 */
public class ActdecListedTwoBean extends ActdecListedTwoCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = -4853969892766073101L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * 检索条件 用户名
	 * 
	 */
	private String userNameSrch;

	/**
	 * 检索条件 姓名
	 * 
	 */
	private String trueNameSrch;

	/**
	 * 检索条件 手机号
	 * 
	 */
	private String mobileSrch;
	
	/**
	 * 检索条件 操作(0领奖、1充值、2出借、3提现)
	 * 
	 */
	private String tradeSrch;
	
	/**
	 * 检索条件 领取奖励
	 * 
	 */
	private String acceptPrizeSrch;

	/**
	 * 检索条件 领取时间
	 * 
	 */
	private String acceptTimeStartSrch; 

	/**
	 * 检索条件 领取时间
	 * 
	 */
	private String acceptTimeEndSrch;

	/**
	 * paginatorPage
	 * @return the paginatorPage
	 */
	
	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	/**
	 * @param paginatorPage the paginatorPage to set
	 */
	
	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	/**
	 * paginator
	 * @return the paginator
	 */
	
	public Paginator getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	/**
	 * limitStart
	 * @return the limitStart
	 */
	
	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart the limitStart to set
	 */
	
	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * @return the limitEnd
	 */
	
	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd the limitEnd to set
	 */
	
	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	/**
	 * userNameSrch
	 * @return the userNameSrch
	 */
	
	public String getUserNameSrch() {
		return userNameSrch;
	}

	/**
	 * @param userNameSrch the userNameSrch to set
	 */
	
	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	/**
	 * trueNameSrch
	 * @return the trueNameSrch
	 */
	
	public String getTrueNameSrch() {
		return trueNameSrch;
	}

	/**
	 * @param trueNameSrch the trueNameSrch to set
	 */
	
	public void setTrueNameSrch(String trueNameSrch) {
		this.trueNameSrch = trueNameSrch;
	}

	/**
	 * mobileSrch
	 * @return the mobileSrch
	 */
	
	public String getMobileSrch() {
		return mobileSrch;
	}

	/**
	 * @param mobileSrch the mobileSrch to set
	 */
	
	public void setMobileSrch(String mobileSrch) {
		this.mobileSrch = mobileSrch;
	}

	/**
	 * tradeSrch
	 * @return the tradeSrch
	 */
	
	public String getTradeSrch() {
		return tradeSrch;
	}

	/**
	 * @param tradeSrch the tradeSrch to set
	 */
	
	public void setTradeSrch(String tradeSrch) {
		this.tradeSrch = tradeSrch;
	}

	/**
	 * acceptPrizeSrch
	 * @return the acceptPrizeSrch
	 */
	
	public String getAcceptPrizeSrch() {
		return acceptPrizeSrch;
	}

	/**
	 * @param acceptPrizeSrch the acceptPrizeSrch to set
	 */
	
	public void setAcceptPrizeSrch(String acceptPrizeSrch) {
		this.acceptPrizeSrch = acceptPrizeSrch;
	}

	/**
	 * acceptTimeStartSrch
	 * @return the acceptTimeStartSrch
	 */
	
	public String getAcceptTimeStartSrch() {
		return acceptTimeStartSrch;
	}

	/**
	 * @param acceptTimeStartSrch the acceptTimeStartSrch to set
	 */
	
	public void setAcceptTimeStartSrch(String acceptTimeStartSrch) {
		this.acceptTimeStartSrch = acceptTimeStartSrch;
	}

	/**
	 * acceptTimeEndSrch
	 * @return the acceptTimeEndSrch
	 */
	
	public String getAcceptTimeEndSrch() {
		return acceptTimeEndSrch;
	}

	/**
	 * @param acceptTimeEndSrch the acceptTimeEndSrch to set
	 */
	
	public void setAcceptTimeEndSrch(String acceptTimeEndSrch) {
		this.acceptTimeEndSrch = acceptTimeEndSrch;
	} 

}
