package com.hyjf.admin.manager.activity.listed1;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActdecListedOne;

/**
 * @package com.hyjf.admin.maintenance.AlllBorrowCustomize;
 * @author GOGTZ-Z
 * @date 2018/01/31 17:00
 * @version V1.0  
 */
public class ActdecListedOneBean extends ActdecListedOne implements Serializable {

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
	 */
	private String userNameSrch;
	
	/**
	 * 检索条件 姓名
	 */
	private String userTureNameSrch;
	
	/**
	 * 检索条件 手机号
	 */
	private String userMobileSrch;
	
	/**
	 * 检索条件 标的编号
	 */
	private String numberSrch;
	
	/**
	 * 检索条件 订单号
	 */
	private String orderNumberSrch;
	
	/**
	 * 检索条件 获得奖励
	 */
	private Integer rewardSrch;
	
	/**
	 * 检索条件 是否领取
	 */
	private String whetherSrch;
	
	/**
	 * 检索条件 领取时间
	 */
	private String createTimeStartSrch;
	
	/**
	 * 检索条件 领取时间
	 */
	private String createTimeEndSrch;
	
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
	 * userTureNameSrch
	 * @return the userTureNameSrch
	 */
	
	public String getUserTureNameSrch() {
		return userTureNameSrch;
	}

	/**
	 * @param userTureNameSrch the userTureNameSrch to set
	 */
	
	public void setUserTureNameSrch(String userTureNameSrch) {
		this.userTureNameSrch = userTureNameSrch;
	}

	/**
	 * userMobileSrch
	 * @return the userMobileSrch
	 */
	
	public String getUserMobileSrch() {
		return userMobileSrch;
	}

	/**
	 * @param userMobileSrch the userMobileSrch to set
	 */
	
	public void setUserMobileSrch(String userMobileSrch) {
		this.userMobileSrch = userMobileSrch;
	}

	/**
	 * numberSrch
	 * @return the numberSrch
	 */
	
	public String getNumberSrch() {
		return numberSrch;
	}

	/**
	 * @param numberSrch the numberSrch to set
	 */
	
	public void setNumberSrch(String numberSrch) {
		this.numberSrch = numberSrch;
	}

	/**
	 * orderNumberSrch
	 * @return the orderNumberSrch
	 */
	
	public String getOrderNumberSrch() {
		return orderNumberSrch;
	}

	/**
	 * @param orderNumberSrch the orderNumberSrch to set
	 */
	
	public void setOrderNumberSrch(String orderNumberSrch) {
		this.orderNumberSrch = orderNumberSrch;
	}

	/**
	 * rewardSrch
	 * @return the rewardSrch
	 */
	
	public Integer getRewardSrch() {
		return rewardSrch;
	}

	/**
	 * @param rewardSrch the rewardSrch to set
	 */
	
	public void setRewardSrch(Integer rewardSrch) {
		this.rewardSrch = rewardSrch;
	}

	/**
	 * whetherSrch
	 * @return the whetherSrch
	 */
	
	public String getWhetherSrch() {
		return whetherSrch;
	}

	/**
	 * @param whetherSrch the whetherSrch to set
	 */
	
	public void setWhetherSrch(String whetherSrch) {
		this.whetherSrch = whetherSrch;
	}

	/**
	 * createTimeStartSrch
	 * @return the createTimeStartSrch
	 */
	
	public String getCreateTimeStartSrch() {
		return createTimeStartSrch;
	}

	/**
	 * @param createTimeStartSrch the createTimeStartSrch to set
	 */
	
	public void setCreateTimeStartSrch(String createTimeStartSrch) {
		this.createTimeStartSrch = createTimeStartSrch;
	}

	/**
	 * createTimeEndSrch
	 * @return the createTimeEndSrch
	 */
	
	public String getCreateTimeEndSrch() {
		return createTimeEndSrch;
	}

	/**
	 * @param createTimeEndSrch the createTimeEndSrch to set
	 */
	
	public void setCreateTimeEndSrch(String createTimeEndSrch) {
		this.createTimeEndSrch = createTimeEndSrch;
	}

}
