/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * @author Administrator
 */

public class KeyCountCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * 渠道
	 */
	private String sourceName;

	/**
	 * 访问数
	 */
	private String accessNumber;

	/**
	 * 注册数
	 */
	private String registNumber;

	/**
	 * 开户数
	 */
	private String accountNumber;

	/**
	 * 出借人数
	 */
	private String tenderNumber;

	/**
	 * 关键字
	 */
	private String keyWord;

	/**
	 * 检索条件 渠道
	 */
	private String sourceIdSrch;

	/**
	 * 检索条件 关键词
	 */
	private String keyWordSrch;

	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	/**
	 * sourceName
	 * 
	 * @return the sourceName
	 */

	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName
	 *            the sourceName to set
	 */

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * accessNumber
	 * 
	 * @return the accessNumber
	 */

	public String getAccessNumber() {
		return accessNumber;
	}

	/**
	 * @param accessNumber
	 *            the accessNumber to set
	 */

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	/**
	 * registNumber
	 * 
	 * @return the registNumber
	 */

	public String getRegistNumber() {
		return registNumber;
	}

	/**
	 * @param registNumber
	 *            the registNumber to set
	 */

	public void setRegistNumber(String registNumber) {
		this.registNumber = registNumber;
	}

	/**
	 * accountNumber
	 * 
	 * @return the accountNumber
	 */

	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * tenderNumber
	 * 
	 * @return the tenderNumber
	 */

	public String getTenderNumber() {
		return tenderNumber;
	}

	/**
	 * @param tenderNumber
	 *            the tenderNumber to set
	 */

	public void setTenderNumber(String tenderNumber) {
		this.tenderNumber = tenderNumber;
	}

	/**
	 * keyWord
	 * 
	 * @return the keyWord
	 */

	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * @param keyWord
	 *            the keyWord to set
	 */

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	/**
	 * sourceIdSrch
	 * 
	 * @return the sourceIdSrch
	 */

	public String getSourceIdSrch() {
		return sourceIdSrch;
	}

	/**
	 * @param sourceIdSrch
	 *            the sourceIdSrch to set
	 */

	public void setSourceIdSrch(String sourceIdSrch) {
		this.sourceIdSrch = sourceIdSrch;
	}

	/**
	 * keyWordSrch
	 * 
	 * @return the keyWordSrch
	 */

	public String getKeyWordSrch() {
		return keyWordSrch;
	}

	/**
	 * @param keyWordSrch
	 *            the keyWordSrch to set
	 */

	public void setKeyWordSrch(String keyWordSrch) {
		this.keyWordSrch = keyWordSrch;
	}

	/**
	 * limitStart
	 * 
	 * @return the limitStart
	 */

	public int getLimitStart() {
		return limitStart;
	}

	/**
	 * @param limitStart
	 *            the limitStart to set
	 */

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	/**
	 * limitEnd
	 * 
	 * @return the limitEnd
	 */

	public int getLimitEnd() {
		return limitEnd;
	}

	/**
	 * @param limitEnd
	 *            the limitEnd to set
	 */

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

}
