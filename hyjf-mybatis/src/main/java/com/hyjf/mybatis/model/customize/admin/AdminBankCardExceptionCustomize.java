
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * 银行卡异常
 * 
 * @author 孙亮
 * @since 2016年1月20日 下午4:28:29
 */
public class AdminBankCardExceptionCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;
	private String userId;// 用户ID
	private String username;// 用户名
	private String truename;// 姓名
	private String idcard;// 身份证号
	private String account;// 银行账号
	private String bankcode;// 所属银行代码
	private String bank;// 所属银行
	private String cardType;// cardType
	private String isdefault;// 默认卡,cardType=1,2显示是,否则显示否
	private String bankShuxing;// 银行卡属性,cardType=0普通提现卡,1默认卡,2快捷支付卡
	private String addtime;// 添加时间
	private String usernameSearch;// 用户名
	private String bankSearch;// 所属银行代码
	private String accountSearch;// 银行账号
	private String isdefaultSearch;// 默认提现卡
	private String bankShuxingSearch;// 银行卡属性
	private String startDateSearch;// 查询添加时间开始日期
	private String endDateSearch;// 查询添加时间结束日期
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getBankShuxing() {
		return bankShuxing;
	}

	public void setBankShuxing(String bankShuxing) {
		this.bankShuxing = bankShuxing;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUsernameSearch() {
		return usernameSearch;
	}

	public void setUsernameSearch(String usernameSearch) {
		this.usernameSearch = usernameSearch;
	}

	public String getBankSearch() {
		return bankSearch;
	}

	public void setBankSearch(String bankSearch) {
		this.bankSearch = bankSearch;
	}

	public String getAccountSearch() {
		return accountSearch;
	}

	public void setAccountSearch(String accountSearch) {
		this.accountSearch = accountSearch;
	}

	public String getIsdefaultSearch() {
		return isdefaultSearch;
	}

	public void setIsdefaultSearch(String isdefaultSearch) {
		this.isdefaultSearch = isdefaultSearch;
	}

	public String getBankShuxingSearch() {
		return bankShuxingSearch;
	}

	public void setBankShuxingSearch(String bankShuxingSearch) {
		this.bankShuxingSearch = bankShuxingSearch;
	}

	public String getStartDateSearch() {
		return startDateSearch;
	}

	public void setStartDateSearch(String startDateSearch) {
		this.startDateSearch = startDateSearch;
	}

	public String getEndDateSearch() {
		return endDateSearch;
	}

	public void setEndDateSearch(String endDateSearch) {
		this.endDateSearch = endDateSearch;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

}
