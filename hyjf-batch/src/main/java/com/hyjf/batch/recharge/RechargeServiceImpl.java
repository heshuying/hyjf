/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mybatis.mapper.auto.AccountBankMapper;
import com.hyjf.mybatis.mapper.auto.AccountListMapper;
import com.hyjf.mybatis.mapper.auto.AccountMapper;
import com.hyjf.mybatis.mapper.auto.AccountRechargeMapper;
import com.hyjf.mybatis.mapper.auto.AccountWebListMapper;
import com.hyjf.mybatis.mapper.auto.UsersInfoMapper;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.mapper.customize.RechargeCustomizeMapper;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
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
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 充值相关服务层接口实现类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年1月11日
 * @see 下午2:23:40
 */
@Service("BatchRechargeServiceImpl")
public class RechargeServiceImpl extends BaseServiceImpl implements RechargeService {

	@Autowired
	private AccountRechargeMapper accountRechargeMapper;

	@Autowired
	private AccountListMapper accountListMapper;

	@Autowired
	protected AccountWebListMapper accountWebListMapper;

	@Autowired
	protected UsersInfoMapper usersInfoMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	protected AccountBankMapper accountBankMapper;

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private RechargeCustomizeMapper rechargeCustomizeMapper;

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

	/**
	 * 此处为实现/覆载说明
	 * 
	 * @author renxingchen
	 * @return
	 * @see com.hyjf.batch.borrow.Recharge.RechargeService#queryNoResultRechargeList()
	 */
	@Override
	public List<RechargeCustomize> queryNoResultRechargeList() {
		return rechargeCustomizeMapper.selectNoResultRechargeList();
	}

	/**
	 * 
	 * 此处为实现/覆载说明
	 * 
	 * @author renxingchen
	 * @see com.hyjf.batch.borrow.recharge.RechargeService#updateAndCheckRechargeStatus()
	 */
	@Override
	public void handleRechargeStatus(ChinapnrBean bean, Integer userId, String feeFrom) {
		// 发送请求获取结果
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		String respCode = chinapnrBean == null ? "" : chinapnrBean.getRespCode();
		// 如果接口调用成功
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			String ordId = bean.getOrdId(); // 订单号
			int nowTime = GetDate.getNowTime10(); // 当前时间
			AccountRechargeExample example;
			List<AccountRecharge> accountRecharges;
			// 判断充值结果状态
			switch (chinapnrBean.getTransStat()) {
			case "I":// 如果是初始状态
				break;
			case "F":// 如果是失败状态
				// 更新订单信息
				String message = "未知异常";
				try {
					if (StringUtils.isNotEmpty(chinapnrBean.getRespDesc())) {
						message = URLDecoder.decode(chinapnrBean.getRespDesc(), "utf-8");
					}
					if (null != bean.getSecRespDesc()) {
						message = message + ";" + URLDecoder.decode(bean.getSecRespDesc(), "utf-8");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				example = new AccountRechargeExample();
				example.createCriteria().andNidEqualTo(ordId);
				accountRecharges = this.accountRechargeMapper.selectByExample(example);
				if (accountRecharges != null && accountRecharges.size() == 1) {
					AccountRecharge accountRecharge = accountRecharges.get(0);
					// 更新处理状态
					accountRecharge.setStatus(0);
					accountRecharge.setUpdateTime(nowTime);
					accountRecharge.setMessage(message);
					this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge);
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
					if (accountRecharge.getStatus() == 2) {// 如果订单是处理中的状态
						try {
							// 开启事务
							txStatus = this.transactionManager.getTransaction(transactionDefinition);
							// 将数据封装更新至充值记录
							AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
							accountRechargeExample.createCriteria().andNidEqualTo(ordId).andStatusEqualTo(accountRecharge.getStatus());
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
							boolean isUpdateFlag = this.accountRechargeMapper.updateByExampleSelective(accountRecharge, accountRechargeExample)> 0 ? true :false;
							if(!isUpdateFlag){
								throw new Exception("充值重复操作~~~!");
							}

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
									UsersInfo info = getUsersInfoByUserId(userId);
									replaceMap.put("val_name", info.getTruename().substring(0, 1));
									replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
									SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_CHONGZHI_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
									AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_CHONGZHI_SUCCESS);
									smsProcesser.gather(smsMessage);
									appMsProcesser.gather(appMsMessage);
								}else{
								 // 替换参数
                                    Map<String, String> replaceMap = new HashMap<String, String>();
                                    replaceMap.put("val_amount", balance.toString());
                                    replaceMap.put("val_fee", feeAmt.toString());
                                    UsersInfo info = getUsersInfoByUserId(userId);
                                    replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                    replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                    AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_CHONGZHI_SUCCESS);
                                    appMsProcesser.gather(appMsMessage);
								}
							} else {
								// 回滚事务
								transactionManager.rollback(txStatus);
							}
						} catch (Exception e) {
							// 回滚事务
							transactionManager.rollback(txStatus);
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

}
