package com.hyjf.admin.exception.withdrawexception;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.finance.returncash.ReturncashDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.WithdrawCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class WithdrawExceptionServiceImpl extends BaseServiceImpl implements WithdrawExceptionService {
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private TransactionDefinition transactionDefinition;
	
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	private static String ip = null;
	/**
	 * 获取提现列表数量
	 *
	 * @param form
	 * @return
	 */
	@Override
	public int getWithdrawRecordCount(WithdrawExceptionBean form) {
		WithdrawCustomize withdrawCustomize = new WithdrawCustomize();
		BeanUtils.copyProperties(form, withdrawCustomize);
		withdrawCustomize.setStatusExceptionSrch("1");
		return withdrawCustomizeMapper.selectWithdrawCount(withdrawCustomize);
	}

	/**
	 * 获取提现列表
	 *
	 * @return
	 */
	@Override
	public List<WithdrawCustomize> getWithdrawRecordList(WithdrawExceptionBean form) {
		WithdrawCustomize withdrawCustomize = new WithdrawCustomize();
		BeanUtils.copyProperties(form, withdrawCustomize);
		withdrawCustomize.setStatusExceptionSrch("1");
		return withdrawCustomizeMapper.selectWithdrawList(withdrawCustomize);
	}

	/**
	 * 提现确认
	 *
	 * @param form
	 * @return
	 */
	@Override
	public String updateWithdrawConfirm(WithdrawExceptionBean form, boolean success) {
		JSONObject ret = new JSONObject();

		// 提现记录表ID
		int id = form.getId();
		// 增加时间
		Integer nowTime = GetDate.getMyTimeInMillis();
		// 操作者用户名
		String operator = ShiroUtil.getLoginUsername();

		// 取得提现信息
		Accountwithdraw withdraw = accountwithdrawMapper.selectByPrimaryKey(id);
		// 取不到提现信息或者提现状态不等于处理中时,返回
		if (withdraw == null) {
			ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
			ret.put(ReturncashDefine.JSON_RESULT_KEY, "提现订单信息取得失败,请重新操作!");
			return ret.toString();
		}

		// 用户ID
		int userId = withdraw.getUserId();
		// 订单号
		String ordId = withdraw.getNid();
		// 提现状态
		int withdrawStatus = withdraw.getStatus();

		ChinapnrBean searchBean = checkCash(ordId);
		if (searchBean == null) {
			ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
			ret.put(ReturncashDefine.JSON_RESULT_KEY, "没有查到汇付数据!");
			return ret.toString();
		}
		if (!searchBean.getRespCode().equals(ChinaPnrConstant.RESPCODE_SUCCESS)
				|| Validator.isNull(searchBean.getFeeAmt()) || Validator.isNull(searchBean.getRealTransAmt())) {
			ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
			ret.put(ReturncashDefine.JSON_RESULT_KEY, "汇付返回的数据有误,状态码:" + searchBean.getRespCode() + ",返回手续费或提现金额为空!");
			return ret.toString();
		}
		// 提现金额
		BigDecimal transAmt = new BigDecimal(searchBean.getTransAmt());
		// 到账金额
		BigDecimal realTansAmt = new BigDecimal(searchBean.getRealTransAmt());
		// 提现手续费
		BigDecimal feeAmt = transAmt.subtract(realTansAmt);
		if (transAmt.compareTo(BigDecimal.ZERO) <= 0) {
			ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
			ret.put(ReturncashDefine.JSON_RESULT_KEY, "汇付返回的数据有误:提现金额小于等于0.00!");
			return ret.toString();
		}

		switch (searchBean.getTransStat()) {
		case "S": {
			// 已经成功
			if (withdrawStatus == CustomConstants.WITHDRAW_STATUS_SUCCESS) {
				ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
				ret.put(ReturncashDefine.JSON_RESULT_KEY, "该订单已经是成功状态!");
				return ret.toString();
			}

			// 开启事务
			TransactionStatus txStatus = null;
			try {
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				// // 1.如果提现状态为初始
				if (withdrawStatus == CustomConstants.WITHDRAW_STATUS_DEFAULT) {
					withdraw.setTotal(transAmt);
					withdraw.setCredited(realTansAmt);
					withdraw.setFee(feeAmt.toString());
					withdraw.setStatus(CustomConstants.WITHDRAW_STATUS_SUCCESS);
					withdraw.setAccount(searchBean.getOpenAcctId());
					withdraw.setBank(searchBean.getOpenBankId());
					withdraw.setVerifyTime(nowTime);
					withdraw.setVerifyUserid(0);
					withdraw.setVerifyRemark("提现确认");
					withdraw.setUpdateTime(0);
					withdraw.setReason("");
					AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
					AccountwithdrawExample.Criteria crt = accountWithdrawExample.createCriteria();
					crt.andIdEqualTo(withdraw.getId());
					crt.andStatusEqualTo(withdrawStatus);
					// 更新订单信息
					boolean withdrawFlag = this.accountwithdrawMapper.updateByExampleSelective(withdraw, accountWithdrawExample) > 0 ? true : false;
					if (!withdrawFlag) {
						throw new RuntimeException("提现更新withdraw失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
					// 2.更新账户信息
					Account account = new Account();
					account.setUserId(userId);
					account.setTotal(transAmt); // 总资产total减少
					account.setBalance(transAmt); // 可用余额balance减少
					account.setExpend(realTansAmt); // 总收入total增加
					/// 版本控制(防止事物重复提交)
					boolean accountFlag = this.adminAccountCustomizeMapper.updateUserWithdrawSuccess(account) > 0 ? true : false;
					// 更新用户账户信息
					if (!accountFlag) {
						throw new RuntimeException("提现更新account失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
					account = this.getAccount(userId);
					// 3.写入收支明细
					AccountList accountList = new AccountList();
					accountList.setBankAwait(account.getBankAwait());
					accountList.setBankAwaitCapital(account.getBankAwaitCapital());
					accountList.setBankAwaitInterest(account.getBankAwaitInterest());
					accountList.setBankBalance(account.getBankBalance());
					accountList.setBankFrost(account.getBankFrost());
					accountList.setBankInterestSum(account.getBankInterestSum());
					accountList.setBankTotal(account.getBankTotal());
					accountList.setBankWaitCapital(account.getBankWaitCapital());
					accountList.setBankWaitInterest(account.getBankWaitInterest());
					accountList.setBankWaitRepay(account.getBankWaitRepay());
					accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
					accountList.setPlanFrost(account.getPlanFrost());
					accountList.setCheckStatus(1);
					accountList.setTradeStatus(1);
					accountList.setIsBank(0);
					accountList.setNid(ordId);
					accountList.setUserId(userId);
					accountList.setAmount(transAmt);
					accountList.setType(2);
					accountList.setTrade("cash_success");
					accountList.setTradeCode("balance");
					accountList.setTotal(account.getTotal());
					accountList.setBalance(account.getBalance());
					accountList.setTotal(account.getTotal());
					accountList.setBalance(account.getBalance());
					accountList.setFrost(account.getFrost());
					accountList.setAwait(account.getAwait());
					accountList.setRepay(account.getRepay());
					accountList.setRemark("网站提现");
					accountList.setCreateTime(nowTime);
					accountList.setOperator(operator);
					accountList.setIp(getIp());
					accountList.setIsUpdate(0);
					accountList.setBaseUpdate(0);
					accountList.setInterest(null);
					accountList.setWeb(0);
					boolean accountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
					if (!accountListFlag) {
						throw new RuntimeException("提现插入交易明细accountList失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
					Users users = getUsers(userId);
					// 可以发送提现短信时
					if (users != null && users.getWithdrawSms() != null && users.getWithdrawSms() == 0) {
						// 替换参数
						Map<String, String> replaceMap = new HashMap<String, String>();
						replaceMap.put("val_amount", transAmt.toString());
						replaceMap.put("val_fee", feeAmt.toString());
						UsersInfo info = getUsersInfoByUserId(userId);
						replaceMap.put("val_name", info.getTruename().substring(0, 1));
						replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
						SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TIXIAN_SUCCESS,
								CustomConstants.CHANNEL_TYPE_NORMAL);
						AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
						smsProcesser.gather(smsMessage);
						appMsProcesser.gather(appMsMessage);
					}
				}else{
					// 更新状态为成功
					withdraw.setStatus(CustomConstants.WITHDRAW_STATUS_SUCCESS);// 4:成功
					withdraw.setUpdateTime(nowTime);
					withdraw.setVerifyTime(nowTime);
					withdraw.setVerifyUserid(0);
					withdraw.setVerifyRemark("提现确认");
					withdraw.setUpdateTime(0);
					withdraw.setReason("");
					AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
					AccountwithdrawExample.Criteria crt = accountWithdrawExample.createCriteria();
					crt.andIdEqualTo(withdraw.getId());
					crt.andStatusEqualTo(withdrawStatus);
					// 更新订单信息
					boolean withdrawFlag = this.accountwithdrawMapper.updateByExampleSelective(withdraw, accountWithdrawExample) > 0 ? true : false;
					if (!withdrawFlag) {
						throw new RuntimeException("提现更新withdraw失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
					// 更新账户信息
					Account account = this.getAccount(userId);
					// 写入收支明细
					AccountList accountList = new AccountList();
					accountList.setBankAwait(account.getBankAwait());
					accountList.setBankAwaitCapital(account.getBankAwaitCapital());
					accountList.setBankAwaitInterest(account.getBankAwaitInterest());
					accountList.setBankBalance(account.getBankBalance());
					accountList.setBankFrost(account.getBankFrost());
					accountList.setBankInterestSum(account.getBankInterestSum());
					accountList.setBankTotal(account.getBankTotal());
					accountList.setBankWaitCapital(account.getBankWaitCapital());
					accountList.setBankWaitInterest(account.getBankWaitInterest());
					accountList.setBankWaitRepay(account.getBankWaitRepay());
					accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
					accountList.setPlanFrost(account.getPlanFrost());
					accountList.setCheckStatus(1);
					accountList.setTradeStatus(1);
					accountList.setIsBank(0);
					accountList.setNid(ordId);
					accountList.setUserId(userId);
					accountList.setAmount(transAmt);
					accountList.setType(2);
					accountList.setTrade("cash_success");
					accountList.setTradeCode("balance");
					accountList.setTotal(account.getTotal());
					accountList.setBalance(account.getBalance());
					accountList.setFrost(account.getFrost());
					accountList.setAwait(account.getAwait());
					accountList.setRepay(account.getRepay());
					accountList.setRemark("网站提现");
					accountList.setCreateTime(nowTime);
					accountList.setBaseUpdate(nowTime);
					accountList.setOperator(operator);
					accountList.setIp(getIp());
					accountList.setIsUpdate(0);
					accountList.setBaseUpdate(0);
					accountList.setInterest(null);
					accountList.setWeb(0);
					boolean accountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
					if (!accountListFlag) {
						throw new RuntimeException("提现插入交易明细accountList失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
				
				}
				// 提交事务
				this.transactionManager.commit(txStatus);
			} catch (Exception e) {
				this.transactionManager.rollback(txStatus);
				e.printStackTrace();
			}
		}
			break;
		case "F": {
			if (withdrawStatus == CustomConstants.WITHDRAW_STATUS_FAIL) {
				ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
				ret.put(ReturncashDefine.JSON_RESULT_KEY, "该订单已经是失败状态!");
				return ret.toString();
			}

			// 开启事务
			TransactionStatus txStatus = null;
			try {
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				// 失敗原因
				String reason = searchBean.getRespDesc();
				if (withdrawStatus == CustomConstants.WITHDRAW_STATUS_DEFAULT) {
					withdraw.setStatus(CustomConstants.WITHDRAW_STATUS_FAIL);// 失败
					withdraw.setFee(feeAmt.toString()); // 更新手续费
					withdraw.setCredited(realTansAmt); // 更新到账金额
					withdraw.setTotal(transAmt); // 更新到总额
					withdraw.setAccount(searchBean.getOpenAcctId()); // 提现银行卡号
					withdraw.setBank(searchBean.getOpenBankId()); // 提现银行
					withdraw.setIsok(1);
					withdraw.setUpdateTime(nowTime);
					if (StringUtils.isNotEmpty(reason)) {
						if (reason.contains("%")) {
							reason = URLCodec.decodeURL(reason);
						}
					}
					withdraw.setReason(reason);
					AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
					AccountwithdrawExample.Criteria crt = accountWithdrawExample.createCriteria();
					crt.andIdEqualTo(withdraw.getId());
					crt.andStatusEqualTo(withdrawStatus);
					boolean accountWithdrawFlag = this.accountwithdrawMapper.updateByExampleSelective(withdraw, accountWithdrawExample) > 0 ? true : false;
					if (!accountWithdrawFlag) {
						throw new RuntimeException("提现更新accountwithdraw失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
				} else {
					// 更新为失败状态
					withdraw.setStatus(CustomConstants.WITHDRAW_STATUS_FAIL);// 失败
					withdraw.setUpdateTime(nowTime);
					if (StringUtils.isNotEmpty(reason)) {
						if (reason.contains("%")) {
							reason = URLCodec.decodeURL(reason);
						}
					}
					withdraw.setReason(reason);
					AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
					AccountwithdrawExample.Criteria crt = accountWithdrawExample.createCriteria();
					crt.andIdEqualTo(withdraw.getId());
					crt.andStatusEqualTo(withdrawStatus);
					boolean accountWithDrawFlag = this.accountwithdrawMapper.updateByExampleSelective(withdraw, accountWithdrawExample) > 0 ? true : false;
					if (!accountWithDrawFlag) {
						throw new RuntimeException("提现更新accountwithdraw失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
					Account account = new Account();
					account.setUserId(userId);
					account.setTotal(transAmt); // 总资产total增加
					account.setBalance(transAmt); // 可用余额balance增加
					account.setExpend(realTansAmt); // 总收入total增减少
					// 版本控制
					boolean accountFlag = this.adminAccountCustomizeMapper.updateUserWithdrawFail(account) > 0 ? true : false;
					// 更新用户账户信息
					if (!accountFlag) {
						throw new RuntimeException("提现更新account失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
				}
				// 提交事务
				this.transactionManager.commit(txStatus);
			} catch (Exception e) {
				this.transactionManager.rollback(txStatus);
				e.printStackTrace();
				ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
				ret.put(ReturncashDefine.JSON_RESULT_KEY, "提现确认操作失败,事务异常");
				return ret.toString();
			}
			break;
		}
		case "I":
		case "H":
		case "R": {
			if (withdrawStatus == CustomConstants.WITHDRAW_STATUS_FAIL) {
				ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
				ret.put(ReturncashDefine.JSON_RESULT_KEY, "该订单已经是失败状态!");
				return ret.toString();
			}

			// 开启事务
			TransactionStatus txStatus = null;
			try {
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				// 失敗原因
				String reason = searchBean.getRespDesc();
				if (withdrawStatus == CustomConstants.WITHDRAW_STATUS_DEFAULT) {
					withdraw.setStatus(CustomConstants.WITHDRAW_STATUS_FAIL);// 失败
					withdraw.setFee(feeAmt.toString()); // 更新手续费
					withdraw.setCredited(realTansAmt); // 更新到账金额
					withdraw.setTotal(transAmt); // 更新到总额
					withdraw.setAccount(searchBean.getOpenAcctId()); // 提现银行卡号
					withdraw.setBank(searchBean.getOpenBankId()); // 提现银行
					withdraw.setIsok(1);
					withdraw.setUpdateTime(nowTime);
					if (StringUtils.isNotEmpty(reason)) {
						if (reason.contains("%")) {
							reason = URLCodec.decodeURL(reason);
						}
					}
					withdraw.setReason(reason);
					AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
					AccountwithdrawExample.Criteria crt = accountWithdrawExample.createCriteria();
					crt.andIdEqualTo(withdraw.getId());
					crt.andStatusEqualTo(withdrawStatus);
					boolean accountWithdrawFlag = this.accountwithdrawMapper.updateByExampleSelective(withdraw, accountWithdrawExample) > 0 ? true : false;
					if (!accountWithdrawFlag) {
						throw new RuntimeException("提现更新accountwithdraw失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
				} else {
					// 更新为失败状态
					withdraw.setStatus(CustomConstants.WITHDRAW_STATUS_FAIL);// 失败
					withdraw.setUpdateTime(nowTime);
					withdraw.setReason("");
					AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
					AccountwithdrawExample.Criteria crt = accountWithdrawExample.createCriteria();
					crt.andIdEqualTo(withdraw.getId());
					crt.andStatusEqualTo(withdrawStatus);
					boolean accountWithDrawFlag = this.accountwithdrawMapper.updateByExampleSelective(withdraw, accountWithdrawExample) > 0 ? true : false;
					if (!accountWithDrawFlag) {
						throw new RuntimeException("提现更新accountwithdraw失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
					Account account = new Account();
					account.setUserId(userId);
					account.setTotal(transAmt); // 总资产total增加
					account.setBalance(transAmt); // 可用余额balance增加
					account.setExpend(realTansAmt); // 总收入total增减少
					// 版本控制
					boolean accountFlag = this.adminAccountCustomizeMapper.updateUserWithdrawFail(account) > 0 ? true : false;
					// 更新用户账户信息
					if (!accountFlag) {
						throw new RuntimeException("提现更新account失败,用户userId：" + userId + ",提现金额：" + transAmt);
					}
				}
				// 提交事务
				this.transactionManager.commit(txStatus);
			} catch (Exception e) {
				this.transactionManager.rollback(txStatus);
				e.printStackTrace();
				ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_NG);
				ret.put(ReturncashDefine.JSON_RESULT_KEY, "提现确认操作失败,事务异常");
				return ret.toString();
			}
			break;
		}
		default:
			break;
		}
		ret.put(ReturncashDefine.JSON_STATUS_KEY, ReturncashDefine.JSON_STATUS_OK);
		ret.put(ReturncashDefine.JSON_RESULT_KEY, "提现确认操作成功!");
		return ret.toString();

	}

	/**
	 * 查询用户的账户信息
	 * 
	 * @param userId
	 * @return
	 */
	private Account getAccount(int userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<Account> listAccount = accountMapper.selectByExample(example);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	private static String getIp() {
		if (ip == null) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				ip = "127.0.0.1";
			}
		}
		return ip;
	}

	public Users getUsers(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId);
	}

	// /**
	// * 判断是否提现成功
	// *
	// * @param ordId
	// * @return S:成功 F:失败
	// */
	// public String checkCashResult(String ordId) {
	// if (Validator.isNull(ordId)) {
	// return null;
	// }
	// // 调用汇付接口(交易状态查询)
	// ChinapnrBean bean = new ChinapnrBean();
	// bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
	// bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
	// bean.setOrdId(ordId); // 订单号(必须)
	// bean.setOrdDate(GetOrderIdUtils.getOrderDate()); // 订单日期(必须)
	// bean.setQueryTransType("CASH"); // 交易查询类型
	//
	// // 写log用参数
	// bean.setLogUserId(0);
	// bean.setLogRemark("交易状态查询(取现)"); // 备注
	// bean.setLogClient("0"); // PC
	//
	// // 调用汇付接口
	// ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
	// if (chinapnrBean != null) {
	// return chinapnrBean.getTransStat();
	// }
	// return null;
	// }

	public ChinapnrBean checkCash(String ordId) {
		if (Validator.isNull(ordId)) {
			return null;
		}
		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_TRANS_DETAIL); // 消息类型(必须)
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setQueryTransType("CASH"); // 交易查询类型

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询(取现)"); // 备注
		bean.setLogClient("0"); // PC

		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean != null) {
			if (chinapnrBean.getTransStat().equals("I") || chinapnrBean.getTransStat().equals("H")
					|| chinapnrBean.getTransStat().equals("R")) {
				return null;
			}
			return chinapnrBean;
		}
		return null;
	}

	// /**
	// * 判断是否提现成功
	// *
	// * @param ordId
	// * @return S:成功 F:失败
	// */
	// public JSONObject cashReconciliation(String startDate, String endDate,
	// String ordId) {
	// // 调用汇付接口(交易状态查询)
	// ChinapnrBean bean = new ChinapnrBean();
	// bean.setVersion(ChinaPnrConstant.VERSION_20); // 版本号(必须)
	// bean.setCmdId(ChinaPnrConstant.CMDID_CASH_RECONCILIATION); // 消息类型(必须)
	// bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
	// // 商户客户号
	// bean.setBeginDate(startDate); // 开始时间(必须)
	// bean.setEndDate(endDate); // 结束时间(必须)
	// bean.setPageNum("1");; // 订单日期(必须)
	// bean.setPageSize("1000");
	//
	// // 写log用参数
	// bean.setLogUserId(0);
	// bean.setLogRemark("取现对账"); // 备注
	// bean.setLogClient("0"); // PC
	//
	// // 调用汇付接口
	// ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
	// if (chinapnrBean != null) {
	// String cashReconciliationDtoList =
	// chinapnrBean.getCashReconciliationDtoList();
	// if (Validator.isNotNull(cashReconciliationDtoList)) {
	// try {
	// JSONArray ja = JSONArray.parseArray(cashReconciliationDtoList);
	// for (int i = 0;i<ja.size();i++) {
	// JSONObject jo = ja.getJSONObject(i);
	// if (ordId.equals(jo.get("OrdId"))) {
	// return jo;
	// }
	// }
	// } catch(Exception e ) {
	// e.printStackTrace();
	// }
	// }
	// }
	// return null;
	// }
	//
	// /**
	// * 取得成功信息
	// *
	// * @param ordId
	// * @return
	// */
	// public JSONObject getMsgData(String ordId) {
	// if (Validator.isNull(ordId)) {
	// return null;
	// }
	//
	// List<String> respCode = new ArrayList<String>();
	// respCode.add(ChinaPnrConstant.RESPCODE_SUCCESS);
	// respCode.add(ChinaPnrConstant.RESPCODE_CHECK);
	// ChinapnrLogExample example = new ChinapnrLogExample();
	// example.createCriteria().andOrdidEqualTo(ordId).andMsgTypeEqualTo("Cash").andRespCodeIn(respCode);
	// example.setOrderByClause(" resp_code ");
	// List<ChinapnrLog> list =
	// chinapnrLogMapper.selectByExampleWithBLOBs(example);
	// if (list != null && list.size() > 0) {
	// String msgData = list.get(0).getMsgdata();
	// if (Validator.isNotNull(msgData)) {
	// try {
	// JSONObject jo = JSONObject.parseObject(msgData);
	// return jo;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// return null;
	// }
}
