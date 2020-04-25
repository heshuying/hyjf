package com.hyjf.admin.exception.rechargeexception;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.finance.recharge.RechargeDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class RechargeExceptionServiceImpl extends BaseServiceImpl implements RechargeExceptionService {

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 查询符合条件的充值记录数量
	 * 
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryRechargeCount(RechargeCustomize rechargeCustomize) {
		Integer accountCount = this.adminRechargeExceptionCustomizeMapper.queryRechargeCount(rechargeCustomize);
		return accountCount;

	}

	/**
	 * 充值管理列表查询
	 * 
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize) {
		List<RechargeCustomize> accountInfos = this.adminRechargeExceptionCustomizeMapper.queryRechargeList(rechargeCustomize);
		return accountInfos;

	}

	/**
	 * 根据订单号nid获取充值信息
	 * 
	 * @param nid
	 * @return
	 */
	@Override
	public AccountRecharge queryRechargeByNid(String nid) {

		AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
		AccountRechargeExample.Criteria aCriteria = accountRechargeExample.createCriteria();
		aCriteria.andNidEqualTo(nid);

		List<AccountRecharge> accountRecharges = this.accountRechargeMapper.selectByExample(accountRechargeExample);
		if (accountRecharges != null && accountRecharges.size() == 1) {
			return accountRecharges.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param accountRecharge
	 * @return
	 */
	@Override
	public int updateRecharge(AccountRecharge accountRecharge) {
		int result = this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge);

		return result;
	}

	/**
	 * 手动充值处理
	 * 
	 * @param form
	 * @param chinapnrBean
	 * @return
	 */
	@Override
	public int updateHandRechargeRecord(RechargeExceptionBean form, ChinapnrBean chinapnrBean, UserInfoCustomize userInfo) {
		int ret = 0;

		// 增加时间
		Integer time = GetDate.getMyTimeInMillis();
		// 商户userID
		// Integer merUserId = Integer.valueOf(ShiroUtil.getLoginUserId());
		// 客户userID
		Integer cusUserId = form.getUserId();
		// 操作者用户名
		String operator = ShiroUtil.getLoginUsername();

		// 更新账户信息
		AccountExample accountExample = new AccountExample();
		AccountExample.Criteria accountCriteria = accountExample.createCriteria();
		accountCriteria.andUserIdEqualTo(cusUserId);
		Account account = accountMapper.selectByExample(accountExample).get(0);
		BigDecimal money = form.getMoney();// 充值金额
		account.setTotal(account.getTotal().add(money)); // 累加到账户总资产
		account.setBalance(account.getBalance().add(money)); // 累加可用余额
		account.setIncome(account.getIncome().add(money));// 累加到总收入
		ret += this.accountMapper.updateByExampleSelective(account, accountExample);

		// 写入收支明细
		AccountList accountList = new AccountList();
		accountList.setNid(form.getNid());
		accountList.setUserId(cusUserId);
		accountList.setAmount(money);
		accountList.setType(1);// 1收入2支出3冻结
		accountList.setTrade("recharge");
		accountList.setTradeCode("balance");
		// accountList.setTotal(account.getTotal().add(money));
		accountList.setTotal(account.getTotal());// 在上一步操作’更新账户信息’时已经加过了，所以不能再加了！
		// accountList.setBalance(account.getBalance().add(money));
		accountList.setBalance(account.getBalance());// 在上一步操作’更新账户信息’时已经加过了，所以不能再加了！
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setRemark("平台转账");// chinapnrBean.getLogRemark()
		accountList.setCreateTime(time);
		accountList.setOperator(operator);
		accountList.setIp(chinapnrBean.getLogIp());
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setWeb(0);
		ret += this.accountListMapper.insertSelective(accountList);

		// 写入充值表
		AccountRecharge accountRecharge = new AccountRecharge();
		accountRecharge.setNid(form.getNid());
		accountRecharge.setUserId(cusUserId);
		accountRecharge.setStatus(1);
		accountRecharge.setMoney(money);
		accountRecharge.setBalance(money);
		accountRecharge.setFee(new BigDecimal(0));
		accountRecharge.setPayment(chinapnrBean.getOpenBankId());// 充值银行 暂缺
		accountRecharge.setGateType("ADMIN");
		accountRecharge.setType(0);// 线下充值
		accountRecharge.setRemark("平台转账");
		accountRecharge.setCreateTime(time);
		accountRecharge.setOperator(operator);
		accountRecharge.setAddtime(time.toString());
		accountRecharge.setAddip(chinapnrBean.getLogIp());
		accountRecharge.setIsok(0);
		accountRecharge.setClient(0);// 0pc 1app
		accountRecharge.setIsok11(0);
		accountRecharge.setFlag(0);
		accountRecharge.setActivityFlag(0);
		ret += this.accountRechargeMapper.insertSelective(accountRecharge);

		// 写入网站收支
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(form.getNid());
		accountWebList.setAmount(money);
		accountWebList.setType(2);// 1收入2支出
		accountWebList.setTrade("recharge");
		accountWebList.setTradeType("平台转账");
		accountWebList.setUserId(cusUserId);
		accountWebList.setUsrcustid(chinapnrBean.getUsrCustId());
		accountWebList.setTruename(userInfo.getTrueName());
		accountWebList.setRegionName(userInfo.getRegionName());
		accountWebList.setBranchName(userInfo.getBranchName());
		accountWebList.setDepartmentName(userInfo.getDepartmentName());
		accountWebList.setRemark(chinapnrBean.getLogRemark());
		accountWebList.setCreateTime(time);
		accountWebList.setOperator(operator);
		accountWebList.setFlag(1);
		ret += this.accountWebListMapper.insertSelective(accountWebList);

		return ret;
	}

	/**
	 * 根据用户名查询用户ID
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public Users queryUserInfoByUserName(String username) {
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria uCriteria = usersExample.createCriteria();
		uCriteria.andUsernameEqualTo(username);
		List<Users> cususers = this.usersMapper.selectByExample(usersExample);

		if (cususers != null && cususers.size() == 1) {
			return cususers.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public UserInfoCustomize queryUserInfoByName(String username) {
		// 如果该用户是员工
		UserInfoCustomize userInfoCustomize = this.userInfoCustomizeMapper.queryUserInfoByEmployeeName(username);
		if (userInfoCustomize == null) {
			// 该用户不是员工
			userInfoCustomize = this.userInfoCustomizeMapper.queryUserInfoByName(username);
		}

		return userInfoCustomize;
	}

	@Override
	public HashMap<String, String> handleRechargeStatus(ChinapnrBean bean, Integer userId, String feeFrom) {
		// 发送请求获取结果
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		String respCode = chinapnrBean == null ? "" : chinapnrBean.getRespCode();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		// 如果接口调用成功
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			String ordId = bean.getOrdId(); // 订单号
			int nowTime = GetDate.getNowTime10(); // 当前时间
			AccountRechargeExample example;
			List<AccountRecharge> accountRecharges;
			// 判断充值结果状态
			switch (chinapnrBean.getTransStat()) {
			case "I":// 如果是初始状态
				resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_NG);
				resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "该笔充值在汇付的状态为初始状态");
				break;
			case "F":// 如果是失败状态
				example = new AccountRechargeExample();
				example.createCriteria().andNidEqualTo(ordId);
				accountRecharges = this.accountRechargeMapper.selectByExample(example);
				if (accountRecharges != null && accountRecharges.size() == 1) {
					AccountRecharge accountRecharge = accountRecharges.get(0);
					// 更新处理状态
					accountRecharge.setStatus(0);
					accountRecharge.setUpdateTime(nowTime);
					// accountRecharge.setMessage(message);
					this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge);
					resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_OK);
					resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "该笔充值在汇付的状态为失败状态，已经同步为失败状态");
				} else {
					resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_NG);
					resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "数据异常请稍后再试");
				}
				break;
			case "S":// 如果是成功状态
				TransactionStatus txStatus = null;
				// 查询用户账户,为了版本控制,必须把查询用户账户放在最前面
				AccountExample accountExample = new AccountExample();
				AccountExample.Criteria accountCriteria = accountExample.createCriteria();
				accountCriteria.andUserIdEqualTo(userId);
				Account account = this.accountMapper.selectByExample(accountExample).get(0);
				// 查询充值记录
				example = new AccountRechargeExample();
				example.createCriteria().andNidEqualTo(bean.getOrdId());
				accountRecharges = accountRechargeMapper.selectByExample(example);
				AccountRecharge accountRecharge = accountRecharges.get(0);
				if (null != accountRecharge) {
					BigDecimal transAmt = new BigDecimal(chinapnrBean.getTransAmt());// 充值金额
					BigDecimal feeAmt = new BigDecimal(chinapnrBean.getFeeAmt()); // 实收手续费
					BigDecimal balance = BigDecimal.ZERO;// 到账金额
					// 判断充值记录状态
					if (RechargeDefine.STATUS_SUCCESS.equals(accountRecharge.getStatus())) {// 如果已经是成功状态
						resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_OK);
						resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "数据修复成功");
					} else {// 如果不是成功状态
						try {
							// 开启事务
							txStatus = this.transactionManager.getTransaction(transactionDefinition);
							// 将数据封装更新至充值记录
							AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
							accountRechargeExample.createCriteria().andNidEqualTo(ordId == null ? "" : ordId);
							if ("U".equals(feeFrom)) {
								// 用户出手续费
								accountRecharge.setFee(feeAmt); // 费用
								accountRecharge.setDianfuFee(BigDecimal.ZERO);
								balance = transAmt.subtract(feeAmt);
								accountRecharge.setBalance(balance); // 实际到账余额
							} else {
								// 商户垫付手续费
								accountRecharge.setFee(BigDecimal.ZERO); // 费用
								accountRecharge.setDianfuFee(feeAmt);
								balance = transAmt; // 到账金额
								accountRecharge.setBalance(balance);// 实际到账余额
							}
							accountRecharge.setGateType(chinapnrBean.get(ChinaPnrConstant.PARAM_GATEBUSIID));
							// accountRecharge.setPayment(bean.getGateBankId());
							// accountRecharge.setCardid(bean.getCardId());
							accountRecharge.setUpdateTime(nowTime);
							accountRecharge.setStatus(1);
							accountRecharge.setMessage("");
							// 更新订单状态
							this.accountRechargeMapper.updateByExampleSelective(accountRecharge, accountRechargeExample);

							account.setTotal(account.getTotal().add(balance)); // 累加到账户总资产
							account.setBalance(account.getBalance().add(balance)); // 累加可用余额
							account.setIncome(account.getIncome().add(balance)); // 累加到总收入
							if (null == account.getRecMoney()) {
								account.setRecMoney(transAmt);
							} else {
								account.setRecMoney(account.getRecMoney().add(transAmt)); // 新充值资金更新
							}
							BigDecimal version = account.getVersion();
							accountCriteria.andVersionEqualTo(version);
							account.setVersion(version.add(BigDecimal.ONE));
							if (accountRecharge.getFeeFrom() == null || "U".equals(accountRecharge.getFeeFrom())) {
								account.setFee(account.getFee().add(feeAmt)); // 待返手续费更新
							} else {
								// 增加网站收支记录(在这里将手续费的流入流出修正)
								AccountWebList accountWebList = new AccountWebList();
								accountWebList.setOrdid(accountRecharge.getNid());// 订单号
								accountWebList.setUserId(userId); // 出借者
								UsersInfoExample usersInfoExample = new UsersInfoExample();
								usersInfoExample.createCriteria().andUserIdEqualTo(userId);
								List<UsersInfo> usersInfos = usersInfoMapper.selectByExample(usersInfoExample);
								if (usersInfos != null && usersInfos.size() > 0) {
									accountWebList.setTruename(usersInfos.get(0).getTruename());
								}
								accountWebList.setAmount(feeAmt); // 管理费
								accountWebList.setType(CustomConstants.TYPE_OUT); // 支出
								accountWebList.setTrade(CustomConstants.TRADE_RECHAFEE); // 充值
								accountWebList.setTradeType(CustomConstants.TRADE_RECHAFEE_DF); // 充值垫付手续费
								accountWebList.setRemark(accountRecharge.getNid());
								accountWebList.setFlag(1);
								accountWebList.setCreateTime(GetterUtil.getInteger(nowTime));
								this.accountWebListMapper.insertSelective(accountWebList);
							}
							// 写入交易明细
							AccountList accountList = new AccountList();
							accountList.setNid(ordId);
							accountList.setUserId(userId);
							accountList.setAmount(balance);
							accountList.setType(1);
							accountList.setTrade("recharge");
							accountList.setTradeCode("balance");
							accountList.setTotal(account.getTotal());
							accountList.setBalance(account.getBalance());
							accountList.setFrost(account.getFrost());
							accountList.setAwait(account.getAwait());
							accountList.setRepay(account.getRepay());
							if (chinapnrBean.getGateBusiId().equals("B2C")) {
								accountList.setRemark("个人网银充值");
							} else if (chinapnrBean.getGateBusiId().equals("B2B")) {
								accountList.setRemark("企业网银充值");
							} else if (chinapnrBean.getGateBusiId().equals("QP")) {
								accountList.setRemark("快捷充值");
							}
							accountList.setCreateTime(nowTime);
							accountList.setBaseUpdate(nowTime);
							accountList.setOperator("rechargeTask");
							// accountList.setIp(params.get("ip"));
							accountList.setIsUpdate(0);
							accountList.setBaseUpdate(0);
							accountList.setInterest(null);
							accountList.setWeb(0);
							this.accountListMapper.insertSelective(accountList);
							// 更新用户账户信息
							if (this.accountMapper.updateByExampleSelective(account, accountExample) > 0) {
								// 提交事务
								this.transactionManager.commit(txStatus);
								// 如果需要短信
								Users users = usersMapper.selectByPrimaryKey(userId);
								// 可以发送充值短信时
								if (users != null && users.getRechargeSms() != null && users.getRechargeSms() == 0) {
									// 替换参数
									Map<String, String> replaceMap = new HashMap<String, String>();
									replaceMap.put("val_amount", balance.toString());
									replaceMap.put("val_fee", feeAmt.toString());
									SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_CHONGZHI_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
									smsProcesser.gather(smsMessage);
								}
								resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_OK);
								resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "数据修复成功");
							} else {
								// 回滚事务
								transactionManager.rollback(txStatus);
								// 查询充值交易状态
								accountRecharges = accountRechargeMapper.selectByExample(example);// 查询充值记录
								accountRecharge = accountRecharges.get(0);
								if (RechargeDefine.STATUS_SUCCESS.equals(accountRecharge.getStatus())) {
									resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_OK);
									resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "数据修复成功");
								} else {
									// 账户数据过期，请查看交易明细 跳转中间页
									resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "数据修复失败，请重试");
									resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_NG);
								}
							}
						} catch (Exception e) {
							// 回滚事务
							transactionManager.rollback(txStatus);
							e.printStackTrace();
							String respDesc = "返回信息异常！";
							resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, respDesc);
							resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_NG);
						}
					}
				} else {
					resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_OK);
					resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, "数据状态异常，请查询后再试");
				}
				break;
			default:
				break;
			}
		} else {
			resultMap.put(RechargeExceptionDefine.JSON_STATUS_KEY, RechargeExceptionDefine.JSON_STATUS_NG);
			String respDesc = "返回信息异常！";
			try {
				respDesc = URLDecoder.decode(chinapnrBean.getRespDesc(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			resultMap.put(RechargeExceptionDefine.JSON_RESULT_KEY, respDesc);
		}
		return resultMap;
	}

	@Override
	public RechargeCustomize queryRechargeById(Integer id) {
		return this.rechargeCustomizeMapper.queryRechargeById(id);
	}

}
