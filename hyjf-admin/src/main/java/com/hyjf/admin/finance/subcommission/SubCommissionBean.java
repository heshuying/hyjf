package com.hyjf.admin.finance.subcommission;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.SubCommission;

/**
 * 商户分佣FormBean
 *
 * @author liuyang
 */
public class SubCommissionBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1251849043086260227L;

	/**
	 * 转入用户名检索
	 */
	private String receiveUserNameSrch;
	
	/**
	 * 转入姓名
	 */
	private String truename;

	/**
	 * 订单号检索用
	 */
	private String orderIdSrch;

	/**
	 * 转账状态检索用
	 */
	private String tradeStatusSrch;
	
	/**
	 * 添加时间开始(检索用)
	 */
	private String timeStartSrch;
	/**
	 * 添加时间结束(检索用)
	 */
	private String timeEndSrch;

	/**
	 * 转出方用户电子账户号
	 */
	private String accountId;

	/**
	 * 收款方用户ID
	 */
	private Integer receiveUserId;

	/**
	 * 收款方用户名
	 */
	private String receiveUserName;

	/**
	 * 收款方用户电子账户号
	 */
	private String receiveAccountId;

	/**
	 * 分佣金额
	 */
	private String txAmount;
	/**
	 * 交易密码
	 */
	private String password;

	/**
	 * 账户余额
	 */
	private String balance;

	/**
	 * 备注说明
	 */
	private String remark;
	/**
	 * 检索结果list
	 */
	private List<SubCommission> recordList;

	/**
	 * 转账对象list
	 */
	private List<SubCommissionListBean> recoverListBean;
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

	public String getOrderIdSrch() {
		return orderIdSrch;
	}

	public void setOrderIdSrch(String orderIdSrch) {
		this.orderIdSrch = orderIdSrch;
	}

	public String getTradeStatusSrch() {
		return tradeStatusSrch;
	}

	public void setTradeStatusSrch(String tradeStatusSrch) {
		this.tradeStatusSrch = tradeStatusSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public List<SubCommission> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<SubCommission> recordList) {
		this.recordList = recordList;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getReceiveAccountId() {
		return receiveAccountId;
	}

	public void setReceiveAccountId(String receiveAccountId) {
		this.receiveAccountId = receiveAccountId;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceiveUserNameSrch() {
		return receiveUserNameSrch;
	}

	public void setReceiveUserNameSrch(String receiveUserNameSrch) {
		this.receiveUserNameSrch = receiveUserNameSrch;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public List<SubCommissionListBean> getRecoverListBean() {
		return recoverListBean;
	}

	public void setRecoverListBean(List<SubCommissionListBean> recoverListBean) {
		this.recoverListBean = recoverListBean;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	
	
}
