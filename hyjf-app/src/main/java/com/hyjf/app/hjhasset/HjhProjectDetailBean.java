/**
 * Description:项目详情查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.hjhasset;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;

public class HjhProjectDetailBean extends AppProjectDetailCustomize implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = -2913028255458205989L;

	/* 1项目信息 tabOneName */
	private String tabOneName;
	/* 项目信息Url tabOneUrl */
	private String tabOneUrl;
	/* 2风控信息或处置预案 tabTwoName */
	private String tabTwoName;
	/* 风控信息或处置预案url tabTwoUrl */
	private String tabTwoUrl;
	/* 3相关文件 tabThreeName */
	private String tabThreeName;
	/* 相关文件url tabThreeUrl */
	private String tabThreeUrl;
	/* 4出借记录 tabFourName */
	private String tabFourName;
	/* 出借记录url tabFourUrl */
	private String tabFourUrl;
	/* 5常见问题 tabFourName */
	private String tabFiveName;
	/* 常见问题url tabFourUrl */
	private String tabFiveUrl;
	/**用户出借url*/
	private String investUrl;
	public HjhProjectDetailBean() {
		super();
	}

	public String getTabOneName() {
		return tabOneName;
	}

	public void setTabOneName(String tabOneName) {
		this.tabOneName = tabOneName;
	}

	public String getTabOneUrl() {
		return tabOneUrl;
	}

	public void setTabOneUrl(String tabOneUrl) {
		this.tabOneUrl = tabOneUrl;
	}

	public String getTabTwoName() {
		return tabTwoName;
	}

	public void setTabTwoName(String tabTwoName) {
		this.tabTwoName = tabTwoName;
	}

	public String getTabTwoUrl() {
		return tabTwoUrl;
	}

	public void setTabTwoUrl(String tabTwoUrl) {
		this.tabTwoUrl = tabTwoUrl;
	}

	public String getTabThreeName() {
		return tabThreeName;
	}

	public void setTabThreeName(String tabThreeName) {
		this.tabThreeName = tabThreeName;
	}

	public String getTabThreeUrl() {
		return tabThreeUrl;
	}

	public void setTabThreeUrl(String tabThreeUrl) {
		this.tabThreeUrl = tabThreeUrl;
	}

	public String getTabFourName() {
		return tabFourName;
	}

	public void setTabFourName(String tabFourName) {
		this.tabFourName = tabFourName;
	}

	public String getTabFourUrl() {
		return tabFourUrl;
	}

	public void setTabFourUrl(String tabFourUrl) {
		this.tabFourUrl = tabFourUrl;
	}

	public String getInvestUrl() {
		return investUrl;
	}

	public void setInvestUrl(String investUrl) {
		this.investUrl = investUrl;
	}

	public String getTabFiveName() {
		return tabFiveName;
	}

	public void setTabFiveName(String tabFiveName) {
		this.tabFiveName = tabFiveName;
	}

	public String getTabFiveUrl() {
		return tabFiveUrl;
	}

	public void setTabFiveUrl(String tabFiveUrl) {
		this.tabFiveUrl = tabFiveUrl;
	}


}
