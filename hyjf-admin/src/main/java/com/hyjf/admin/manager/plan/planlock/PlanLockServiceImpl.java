package com.hyjf.admin.manager.plan.planlock;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.FinancingServiceChargeUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestLog;
import com.hyjf.mybatis.model.auto.DebtInvestLogExample;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanBorrow;
import com.hyjf.mybatis.model.auto.DebtPlanBorrowExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtAdminCreditCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class PlanLockServiceImpl extends BaseServiceImpl implements PlanLockService {
	// 自动投标开始++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static JedisPool pool = RedisUtils.getPool();
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private TransactionDefinition transactionDefinition;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 主动出借
	 * 
	 * @param debtPlanAccede
	 * @param borrowNid
	 * @param investNum
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean tender(DebtPlanAccede debtPlanAccede, String borrowNid, BigDecimal investNum, BigDecimal minSurplusInvestAccount) {
		System.out.println("开始调用手动出借,计划加入订单号：" + debtPlanAccede.getAccedeOrderId() + ",项目编号：" + borrowNid + ",授权服务金额：" + investNum.toString());
		// 出借用户id
		int userId = debtPlanAccede.getUserId();
		// 计划加入订单号
		String planOrderId = debtPlanAccede.getAccedeOrderId();
		try {
			// 出借订单号
			String investOrderId = GetOrderIdUtils.getOrderId0(userId);
			// 订单日期
			String investOrderDate = GetDate.getServerDateTime(1, new Date());
			// 生成出借日志
			boolean debtInvestLogFlag = this.saveDebtInvestLog(debtPlanAccede, borrowNid, investOrderId, investOrderDate, investNum);
			if (debtInvestLogFlag) {
				try {
					// 调用自动投标接口进行出借
					ChinapnrBean bean = this.autoTender(borrowNid, userId, investOrderId, investOrderDate, investNum);
					if (Validator.isNotNull(bean)) {
						// 用户主动出借接口返回码
						String respCode = bean.getRespCode();
						if (StringUtils.isNotBlank(respCode)) {
							// 汇付返回成功
							if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
								try {
									// 计划未出借前剩余余额
									BigDecimal balance = debtPlanAccede.getAccedeBalance();
									// 用户出借方法
									boolean debtInvestFlag = this.debtInvest(debtPlanAccede, bean, borrowNid);
									if (debtInvestFlag) {
										debtPlanAccede = this.selectDebtPlanAccede(debtPlanAccede.getId());
										// 计划剩余余额
										balance = debtPlanAccede.getAccedeBalance();
										if (balance.compareTo(BigDecimal.ZERO) == 1) {
											if (balance.compareTo(minSurplusInvestAccount) == -1) {
												try {
													boolean debtPlanAccedeFlag = this.updateDebtPlanAccedeFinish(debtPlanAccede);
													if (debtPlanAccedeFlag) {
														System.out.println("结束调用出借,计划加入订单号：" + debtPlanAccede.getAccedeOrderId() + ",项目编号：" + borrowNid + ",授权服务金额：" + investNum.toString());
														return true;
													} else {
														throw new Exception("更新用户此笔加入订单为完成失败,加入订单号：" + planOrderId);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
											} else {
												return true;
											}
										} else {
											boolean debtPlanAccedeFlag = this.updateDebtPlanAccedeFinish(debtPlanAccede);
											if (debtPlanAccedeFlag) {
												return true;
											} else {
												throw new Exception("更新用户此笔加入订单为完成失败,加入订单号：" + planOrderId);
											}
										}
									} else {
										System.out.println("调用汇付主动投标接口成功后后续处理失败,冻结用户余额,计划加入订单号：" + debtPlanAccede.getAccedeOrderId() + ",项目编号：" + borrowNid + ",授权服务金额：" + investNum.toString());
										// 出借失败解冻
										try {
											String freezeOrderId = bean.getOrdId();// 冻结订单号
											String freezeOrderDate = bean.getOrdDate();// 冻结订单日期
											String trxId = bean.getTrxId();// 冻结标识
											String unfreezeOrderId = GetOrderIdUtils.getOrderId0(userId);// 解冻订单号
											String unfreezeOrderDate = GetDate.getServerDateTime(1, new Date());// 解冻订单日期
											boolean flag = this.unFreezeOrder(userId, freezeOrderId, trxId, freezeOrderDate, unfreezeOrderId, unfreezeOrderDate);
											if (flag) {
												try {
													debtPlanAccede = this.selectDebtPlanAccede(debtPlanAccede.getId());
													boolean updateDebtInvestLogFlag = this.updateDebtInvestLog(debtPlanAccede, userId, investOrderId);
													if (!updateDebtInvestLogFlag) {
														throw new Exception("主动投标调用汇付接口返回成功后续处理失败后，更新出借日志为失败失败，用户id：" + userId + ",计划加入订单号：" + planOrderId);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
											} else {
												throw new Exception("调用汇付主动投标接口成功后后续处理失败！解冻用户订单失败，用户id：" + userId + ",冻结订单号：" + freezeOrderId);
											}
										} catch (Exception e1) {
											e1.printStackTrace();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								boolean updateDebtInvestLogFlag = this.updateDebtInvestLog(debtPlanAccede, userId, investOrderId);
								if (!updateDebtInvestLogFlag) {
									throw new Exception("主动投标调用汇付接口返回错误后，更新出借日志为失败失败，出借订单号：" + investOrderId + ",返回码为：" + respCode);
								}
							}
						} else {
							throw new Exception("主动投标调用汇付接口返回信息错误，出借订单号：" + investOrderId + ",返回码为：" + respCode);
						}
					} else {
						throw new Exception("调用汇付主动投标接口失败！用户id：" + userId + ",计划加入订单号：" + planOrderId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				throw new Exception("保存debtInvestLog表失败,计划加入订单号：" + planOrderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 调用汇付天下接口前操作,
	 * 
	 * @param debtPlanAccede
	 * @param orderDate
	 * @param orderId
	 * @param orderDate2
	 * @param accedeBalance
	 * @return 出借是否成功
	 * @throws Exception
	 */
	private boolean saveDebtInvestLog(DebtPlanAccede debtPlanAccede, String borrowNid, String orderId, String orderDate, BigDecimal account) throws Exception {

		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 加入用户id
		int userId = debtPlanAccede.getUserId();
		// 计划nid
		String planNid = debtPlanAccede.getPlanNid();
		// 计划加入订单号
		String planOrderId = debtPlanAccede.getAccedeOrderId();
		// 用户的加入平台
		int client = debtPlanAccede.getClient();
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		if (Validator.isNotNull(user)) {
			UsersInfoExample userInfoExample = new UsersInfoExample();
			UsersInfoExample.Criteria userInfoCrt = userInfoExample.createCriteria();
			userInfoCrt.andUserIdEqualTo(userId);
			List<UsersInfo> userInfos = this.usersInfoMapper.selectByExample(userInfoExample);
			if (userInfos != null && userInfos.size() == 1) {
				UsersInfo userInfo = userInfos.get(0);
				DebtInvestLog debtInvestLog = new DebtInvestLog();
				debtInvestLog.setUserId(userId);
				debtInvestLog.setUserName(user.getUsername());
				debtInvestLog.setUserAttribute(userInfo.getAttribute());
				debtInvestLog.setPlanNid(planNid);
				debtInvestLog.setPlanOrderId(planOrderId);
				debtInvestLog.setBorrowNid(borrowNid);
				debtInvestLog.setOrderId(orderId);
				debtInvestLog.setAccount(account);
				debtInvestLog.setInvestType(0);
				debtInvestLog.setClient(client);
				debtInvestLog.setAddip("");
				debtInvestLog.setCreateUserId(userId);
				debtInvestLog.setCreateTime(nowTime);
				debtInvestLog.setCreateUserId(userId);
				debtInvestLog.setCreateUserName(user.getUsername());
				boolean debtInvestLogFlag = debtInvestLogMapper.insertSelective(debtInvestLog) > 0 ? true : false;
				if (debtInvestLogFlag) {
					return true;
				} else {
					throw new Exception("出借临时表BorrowTenderTmp插入失败，计划加入订单号：" + planOrderId);
				}
			} else {
				throw new Exception("未查询到相应的usersInfo用户信息，用户id：" + userId);
			}
		} else {
			throw new Exception("未查询到相应的users用户信息，用户id：" + userId);
		}
	}

	/**
	 * 自动投标接口调用
	 * 
	 * @param borrowAppoint
	 * @return
	 * @author Administrator
	 * @param userId
	 * @throws Exception
	 */

	private ChinapnrBean autoTender(String borrowNid, int userId, String orderId, String orderDate, BigDecimal account) throws Exception {

		// 出借用户汇付信息
		AccountChinapnr chinapnrTender = selectAccountChinapnr(userId);
		String tenderUsrcustid = String.valueOf(chinapnrTender.getChinapnrUsrcustid());
		// 项目信息
		DebtBorrow borrow = this.selectDebtBorrowByNid(borrowNid);
		// 银行存管标识
		int bankInputFlag = borrow.getBankInputFlag();
		// 借款人汇付信息
		AccountChinapnr chinapnrBorrower = this.selectAccountChinapnr(borrow.getUserId());
		String borrowerUsrcustid = String.valueOf(chinapnrBorrower.getChinapnrUsrcustid());
		// 商户后台应答地址(必须)
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		// 接口版本号
		chinapnrBean.setVersion(ChinaPnrConstant.VERSION_20);// 2.0
		// 消息类型(主动投标)
		chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_AUTO_TENDER);
		// 订单号(必须)
		chinapnrBean.setOrdId(orderId);
		// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setOrdDate(orderDate);
		// 交易金额(必须)
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account.toString()));
		// 用户客户号
		chinapnrBean.setUsrCustId(tenderUsrcustid);
		// 手续费率最高0.1
		chinapnrBean.setMaxTenderRate("0.10");
		// 借款人信息
		Map<String, String> map = new HashMap<String, String>();
		map.put("BorrowerCustId", borrowerUsrcustid);
		map.put("BorrowerAmt", CustomUtil.formatAmount(account.toString()));
		map.put("BorrowerRate", "0.30");// 云龙提示
		if (bankInputFlag == 1) {
			map.put("ProId", borrowNid);// 2.0新增字段
			chinapnrBean.setProId(borrowNid);// 2.0新增字段
		}
		JSONArray array = new JSONArray();
		array.add(map);
		String BorrowerDetails = JSON.toJSONString(array);
		chinapnrBean.setBorrowerDetails(BorrowerDetails);
		// 2.0冻结类型
		chinapnrBean.setIsFreeze("Y");
		// 2.0冻结订单号
		chinapnrBean.setFreezeOrdId(orderId);
		// 私有域
		MerPriv merPriv = new MerPriv();
		merPriv.setBorrowNid(borrowNid);
		chinapnrBean.setMerPrivPo(merPriv);
		// log参数
		// 操作用户
		chinapnrBean.setLogUserId(Integer.valueOf(userId));
		// 日志类型
		chinapnrBean.setType("user_tender");
		// 调用汇付接口
		ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(chinapnrBean);
		if (chinaPnrBean == null) {
			return null;
		} else {
			// 接口返回正常时,执行更新操作
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
				// 自动投标成功
				return chinaPnrBean;
			} else {
				// 调用汇付自动投标接口返回失败
				String message = chinaPnrBean.getRespDesc();
				message = URLDecoder.decode(message, "UTF-8");
				throw new Exception(message);
			}
		}
	}

	/**
	 * 调用自动投标接口成功后进行后续处理
	 * 
	 * @param borrowAppoint
	 * @return
	 * @author Administrator
	 */

	private boolean debtInvest(DebtPlanAccede debtPlanAccede, ChinapnrBean bean, String borrowNid) throws Exception {

		// 计划nid
		String planNid = debtPlanAccede.getPlanNid();
		// 计划加入订单号
		String planOrderId = debtPlanAccede.getAccedeOrderId();
		// 加入平台
		int client = debtPlanAccede.getClient();
		// 出借用户
		int userId = debtPlanAccede.getUserId();
		// 出借用户名
		String userName = debtPlanAccede.getUserName();
		// 冻结标示
		String freezeId = bean.getFreezeTrxId();
		// 借款金额
		BigDecimal account = new BigDecimal(bean.getTransAmt());
		// 订单id
		String orderId = bean.getOrdId();
		// 订单日期
		String orderDate = bean.getOrdDate();
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		Jedis jedis = pool.getResource();
		String accountRedisWait = RedisUtils.get(CustomConstants.DEBT_REDITS + borrowNid);
		if (StringUtils.isNotBlank(accountRedisWait)) {
			// 操作redis
			while ("OK".equals(jedis.watch(CustomConstants.DEBT_REDITS + borrowNid))) {
				accountRedisWait = RedisUtils.get(CustomConstants.DEBT_REDITS + borrowNid);
				if (StringUtils.isNotBlank(accountRedisWait)) {
					System.out.println("用户userId:" + userId + "***冻结前可投金额：" + accountRedisWait);
					if (new BigDecimal(accountRedisWait).compareTo(BigDecimal.ZERO) == 0) {
						break;
					} else {
						if (new BigDecimal(accountRedisWait).compareTo(account) < 0) {
							break;
						} else {
							Transaction tx = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(accountRedisWait).subtract(account);
							tx.set(CustomConstants.DEBT_REDITS + borrowNid, lastAccount + "");
							List<Object> result = tx.exec();
							if (result == null || result.isEmpty()) {
								jedis.unwatch();
							} else {
								status = ChinaPnrConstant.STATUS_VERTIFY_OK;
								System.out.println("用户userId:" + userId + "***冻结前减redis：" + account);
								break;
							}
						}
					}
				} else {
					break;
				}
			}
		}
		if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(status)) {
			// 手动控制事务
			TransactionStatus txStatus = null;
			try {
				// 开启事务
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				// 新出借金额汇总
				AccountExample example = new AccountExample();
				AccountExample.Criteria criteria = example.createCriteria();
				criteria.andUserIdEqualTo(userId);
				List<Account> list = accountMapper.selectByExample(example);
				if (list != null && list.size() == 1) {
					BigDecimal version = list.get(0).getVersion();
					// 查询相应的出借客户信息
					AccountChinapnr chinapnr = this.selectAccountChinapnr(userId);
					// 出借用户汇付客户号
					String userCustId = String.valueOf(chinapnr.getChinapnrUsrcustid());
					// 项目信息
					DebtBorrow borrow = this.selectDebtBorrowByNid(borrowNid);
					if (Validator.isNotNull(borrow)) {
						// 剩余可投金额
						BigDecimal borrowAccountWait = borrow.getBorrowAccountWait();
						// 项目还款方式
						String borrowStyle = borrow.getBorrowStyle();
						// 借款期数
						Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
						// 服务费率
						BigDecimal serviceFeeRate = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getServiceFeeRate());
						// 更新临时表为成功
						DebtInvestLogExample debtInvestLogExample = new DebtInvestLogExample();
						DebtInvestLogExample.Criteria debtInvestLogCrt = debtInvestLogExample.createCriteria();
						debtInvestLogCrt.andUserIdEqualTo(userId);
						debtInvestLogCrt.andPlanNidEqualTo(planNid);
						debtInvestLogCrt.andPlanOrderIdEqualTo(planOrderId);
						debtInvestLogCrt.andBorrowNidEqualTo(borrowNid);
						debtInvestLogCrt.andOrderIdEqualTo(orderId);
						DebtInvestLog debtInvestLog = new DebtInvestLog();
						debtInvestLog.setStatus(1);
						boolean debtInvestTmpFLag = this.debtInvestLogMapper.updateByExampleSelective(debtInvestLog, debtInvestLogExample) > 0 ? true : false;
						if (debtInvestTmpFLag) {
							// 插入冻结表
							DebtFreeze debtFreeze = new DebtFreeze();
							debtFreeze.setUserId(userId);
							debtFreeze.setUserName(userName);
							debtFreeze.setUserCustId(userCustId);
							debtFreeze.setPlanNid(planNid);
							debtFreeze.setPlanOrderId(planOrderId);
							debtFreeze.setBorrowNid(borrowNid);
							debtFreeze.setOrderId(orderId);
							debtFreeze.setOrderDate(orderDate);
							debtFreeze.setTrxId(freezeId);
							debtFreeze.setAmount(account);
							debtFreeze.setStatus(0);
							debtFreeze.setFreezeType(0);// 投标冻结
							debtFreeze.setDelFlag(0);// 删除标识
							debtFreeze.setCreateTime(nowTime);
							debtFreeze.setCreateUserId(userId);
							debtFreeze.setCreateUserName(userName);
							boolean debtFreezeFlag = this.debtFreezeMapper.insertSelective(debtFreeze) > 0 ? true : false;
							if (debtFreezeFlag) {
								BigDecimal perService = new BigDecimal(0);
								// 计算服务费
								if (StringUtils.isNotBlank(borrowStyle)) {
									// 到期还本还息end/先息后本endmonth/等额本息month/等额本金principal
									if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
										perService = FinancingServiceChargeUtils.getMonthsPrincipalServiceCharge(account, serviceFeeRate);
									}
									// 按天计息到期还本还息
									else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
										perService = FinancingServiceChargeUtils.getDaysPrincipalServiceCharge(account, serviceFeeRate, borrowPeriod);
									}
									DebtInvest debtInvest = new DebtInvest();
									debtInvest.setUserId(userId);
									debtInvest.setUserName(userName);
									debtInvest.setPlanNid(planNid);
									debtInvest.setPlanOrderId(planOrderId);
									debtInvest.setBorrowNid(borrowNid);
									debtInvest.setOrderId(orderId);
									debtInvest.setOrderDate(orderDate);
									debtInvest.setTrxId(freezeId);
									debtInvest.setAccount(account);
									debtInvest.setLoanAmount(BigDecimal.ZERO);
									debtInvest.setLoanFee(perService);// 修改出借逻辑服务费插入方式，放款时处理
									// debtInvest.setLoanOrderId();
									// debtInvest.setLoanOrderDate();
									debtInvest.setRepayAccount(BigDecimal.ZERO);
									debtInvest.setRepayCapital(BigDecimal.ZERO);
									debtInvest.setRepayInterest(BigDecimal.ZERO);
									debtInvest.setRepayAccountYes(BigDecimal.ZERO);
									debtInvest.setRepayCapitalYes(BigDecimal.ZERO);
									debtInvest.setRepayInterestYes(BigDecimal.ZERO);
									debtInvest.setRepayAccountWait(BigDecimal.ZERO);
									debtInvest.setRepayCapitalWait(BigDecimal.ZERO);
									debtInvest.setRepayInterestWait(BigDecimal.ZERO);
									debtInvest.setInvestType(0);
									debtInvest.setRepayTimes(0);
									debtInvest.setStatus(0);
									debtInvest.setClient(client);
									debtInvest.setWeb(0);
									debtInvest.setRemark("汇添金计划自动投标");
									debtInvest.setAddip("");
									debtInvest.setCreateTime(nowTime);
									debtInvest.setCreateUserId(userId);
									debtInvest.setCreateUserName(userName);
									Users users = this.getUsersByUserId(userId);
									if (Validator.isNotNull(users)) {
										if (users.getInvestflag() == 0) {
											users.setInvestflag(1);
											this.usersMapper.updateByPrimaryKeySelective(users);
										}
										UsersInfo userInfo = selectUserInfo(userId);
										// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
										Integer attribute = null;
										if (Validator.isNotNull(userInfo)) {
											// 获取出借用户的用户属性
											attribute = userInfo.getAttribute();
											if (Validator.isNotNull(attribute)) {
												// 出借人用户属性
												debtInvest.setUserAttribute(attribute);
												// 如果是线上员工或线下员工，推荐人的userId和username不插
												if (attribute == 2 || attribute == 3) {
													EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
													if (Validator.isNotNull(employeeCustomize)) {
														debtInvest.setInviteRegionId(employeeCustomize.getRegionId());
														debtInvest.setInviteRegionName(employeeCustomize.getRegionName());
														debtInvest.setInviteBranchId(employeeCustomize.getBranchId());
														debtInvest.setInviteBranchName(employeeCustomize.getBranchName());
														debtInvest.setInviteDepartmentId(employeeCustomize.getDepartmentId());
														debtInvest.setInviteDepartmentName(employeeCustomize.getDepartmentName());
													}
												} else if (attribute == 1) {
													SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
													SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
													spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
													List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
													if (sList != null && sList.size() == 1) {
														int refUserId = sList.get(0).getSpreadsUserid();
														// 查找用户推荐人
														Users refererUser = selectUser(refUserId);
														if (Validator.isNotNull(refererUser)) {
															debtInvest.setInviteUserId(refererUser.getUserId());
															debtInvest.setInviteUserName(refererUser.getUsername());
														}
														// 推荐人信息
														UsersInfo refererUserInfo = selectUserInfo(refUserId);
														// 推荐人用户属性
														if (Validator.isNotNull(refererUserInfo)) {
															debtInvest.setInviteUserAttribute(refererUserInfo.getAttribute());
														}
														// 查找用户推荐人部门
														EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
														if (Validator.isNotNull(employeeCustomize)) {
															debtInvest.setInviteRegionId(employeeCustomize.getRegionId());
															debtInvest.setInviteRegionName(employeeCustomize.getRegionName());
															debtInvest.setInviteBranchId(employeeCustomize.getBranchId());
															debtInvest.setInviteBranchName(employeeCustomize.getBranchName());
															debtInvest.setInviteDepartmentId(employeeCustomize.getDepartmentId());
															debtInvest.setInviteDepartmentName(employeeCustomize.getDepartmentName());
														}
													}
												} else if (attribute == 0) {
													SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
													SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
													spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
													List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
													if (sList != null && sList.size() == 1) {
														int refUserId = sList.get(0).getSpreadsUserid();
														// 查找推荐人
														Users refererUser = selectUser(refUserId);
														if (Validator.isNotNull(refererUser)) {
															debtInvest.setInviteUserId(refererUser.getUserId());
															debtInvest.setInviteUserName(refererUser.getUsername());
														}
														// 推荐人信息
														UsersInfo refererUserInfo = selectUserInfo(refUserId);
														// 推荐人用户属性
														if (Validator.isNotNull(refererUserInfo)) {
															debtInvest.setInviteUserAttribute(refererUserInfo.getAttribute());
														}
													}
												}
											}
										}
										boolean debtInvestFlag = debtInvestMapper.insertSelective(debtInvest) > 0 ? true : false;
										if (debtInvestFlag) {
											// 出借人计划订单信息更新，余额扣减
											DebtPlanAccede debtPlanAccedeNew = new DebtPlanAccede();
											debtPlanAccedeNew.setAccedeBalance(account);
											debtPlanAccedeNew.setAccedeFrost(account);
											debtPlanAccedeNew.setId(debtPlanAccede.getId());
											boolean debtPlanAccedeFlag = batchDebtPlanAccedeCustomizeMapper.updatePlanAccedeInvest(debtPlanAccedeNew) > 0 ? true : false;
											if (debtPlanAccedeFlag) {
												// 更新用户账户余额表
												Account investAccount = new Account();
												// 承接用户id
												investAccount.setUserId(userId);
												// 计划真实可用余额
												investAccount.setPlanBalance(account);
												// 计划真实冻结余额
												investAccount.setPlanFrost(account);
												// 计划真实加入余额
												investAccount.setPlanAccedeBalance(account);
												// 计划真实可用余额
												investAccount.setPlanAccedeFrost(account);
												// 操作版本
												investAccount.setVersion(version);
												// 更新用户计划账户
												boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanTender(investAccount) > 0 ? true : false;
												if (accountFlag) {
													debtPlanAccede = selectDebtPlanAccede(debtPlanAccede.getId());
													Account investAccountNew = this.selectUserAccount(userId);
													// 插入相应的汇添金资金明细表
													DebtAccountList debtAccountList = new DebtAccountList();
													debtAccountList.setNid(orderId);
													debtAccountList.setUserId(userId);
													debtAccountList.setUserName(userName);
													debtAccountList.setRefererUserId(debtInvest.getInviteUserId());
													debtAccountList.setRefererUserName(debtInvest.getInviteUserName());
													debtAccountList.setPlanNid(planNid);
													debtAccountList.setPlanOrderId(planOrderId);
													debtAccountList.setTotal(investAccountNew.getTotal());
													debtAccountList.setBalance(investAccountNew.getBalance());
													debtAccountList.setFrost(investAccountNew.getFrost());
													debtAccountList.setAccountWait(investAccountNew.getAwait());
													debtAccountList.setRepayWait(investAccountNew.getRepay());
													debtAccountList.setInterestWait(BigDecimal.ZERO);
													debtAccountList.setCapitalWait(BigDecimal.ZERO);
													debtAccountList.setPlanBalance(investAccountNew.getPlanBalance());
													debtAccountList.setPlanFrost(investAccountNew.getPlanFrost());
													debtAccountList.setPlanBalance(investAccountNew.getPlanBalance());
													debtAccountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance());
													debtAccountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost());
													debtAccountList.setAmount(account);
													debtAccountList.setType(3);
													debtAccountList.setTrade("accede_invest_freeze");
													debtAccountList.setTradeCode("balance");
													debtAccountList.setRemark(debtPlanAccede.getAccedeOrderId());
													debtAccountList.setCreateTime(GetDate.getNowTime10());
													debtAccountList.setCreateUserId(userId);
													debtAccountList.setCreateUserName(userName);
													debtAccountList.setWeb(0);
													if (Validator.isNotNull(userInfo)) {
														// 获取出借用户的用户属性
														if (Validator.isNotNull(attribute)) {
															if (attribute == 1 || attribute == 0) {
																debtAccountList.setRefererUserId(debtInvest.getInviteUserId());
																debtAccountList.setRefererUserName(debtInvest.getInviteUserName());
															}
														}
													}
													// 插入交易明细
													boolean debtAccountListFlag = this.debtAccountListMapper.insertSelective(debtAccountList) > 0 ? true : false;
													if (debtAccountListFlag) {
														// 更新borrow表
														Map<String, Object> borrowParam = new HashMap<String, Object>();
														borrowParam.put("borrowAccountYes", account);
														borrowParam.put("borrowService", perService);
														borrowParam.put("borrowId", borrow.getId());
														boolean borrowFlag = debtBorrowCustomizeMapper.updateOfBorrow(borrowParam) > 0 ? true : false;
														if (borrowFlag) {
															DebtPlan debtPlan = new DebtPlan();
															debtPlan.setDebtPlanNid(planNid);
															debtPlan.setDebtPlanBalance(account);
															debtPlan.setDebtPlanFrost(account);
															debtPlan.setUpdateTime(nowTime);
															debtPlan.setUpdateUserId(userId);
															debtPlan.setUpdateUserName(userName);
															boolean debtPlanFlag = batchDebtPlanCustomizeMapper.updateDebtPlanInvest(debtPlan) > 0 ? true : false;
															if (debtPlanFlag) {
																DebtPlanBorrowExample planBorrowExample = new DebtPlanBorrowExample();
																DebtPlanBorrowExample.Criteria planBorrowCrt = planBorrowExample.createCriteria();
																planBorrowCrt.andDebtPlanNidEqualTo(planNid);
																planBorrowCrt.andBorrowNidEqualTo(borrowNid);
																List<DebtPlanBorrow> debtPlanBorrows = this.debtPlanBorrowMapper.selectByExample(planBorrowExample);
																if (debtPlanBorrows != null && debtPlanBorrows.size() > 0) {
																	planBorrowCrt.andAddTypeNotEqualTo(0);
																	DebtPlanBorrow debtPlanBorrow = new DebtPlanBorrow();
																	debtPlanBorrow.setDelFlag(0);
																	debtPlanBorrow.setType(0);
																	debtPlanBorrow.setAddType(1);
																	debtPlanBorrow.setUpdateTime(nowTime);
																	debtPlanBorrow.setUpdateUserId(userId);
																	debtPlanBorrow.setUpdateUserName(userName);
																	this.debtPlanBorrowMapper.updateByExampleSelective(debtPlanBorrow, planBorrowExample);
																} else {
																	DebtPlanBorrow debtPlanBorrow = new DebtPlanBorrow();
																	debtPlanBorrow.setDebtPlanNid(planNid);
																	debtPlanBorrow.setBorrowNid(borrowNid);
																	debtPlanBorrow.setDelFlag(0);
																	debtPlanBorrow.setType(0);
																	debtPlanBorrow.setAddType(1);
																	debtPlanBorrow.setCreateTime(nowTime);
																	debtPlanBorrow.setCreateUserId(userId);
																	debtPlanBorrow.setCreateUserName(userName);
																	boolean debtPlanBorrowFlag = this.debtPlanBorrowMapper.insertSelective(debtPlanBorrow) > 0 ? true : false;
																	if (!debtPlanBorrowFlag) {
																		throw new Exception("债权同计划关联表插入失败，计划编号：" + planNid + ",债转编号：" + borrowNid);
																	}
																}
																List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
																if (calculates != null && calculates.size() > 0) {
																	CalculateInvestInterest calculateNew = new CalculateInvestInterest();
																	calculateNew.setTenderSum(account);
																	calculateNew.setId(calculates.get(0).getId());
																	this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
																}
																// 计算此时的剩余可出借金额
																BigDecimal accountWait = borrowAccountWait.subtract(account);
																// 满标处理
																if (accountWait.compareTo(new BigDecimal(0)) == 0) {
																	System.out.println("用户:" + userId + "项目" + borrowNid + "满标");
																	Map<String, Object> borrowFull = new HashMap<String, Object>();
																	borrowFull.put("borrowId", borrow.getId());
																	boolean fullFlag = debtBorrowCustomizeMapper.updateOfFullBorrow(borrowFull) > 0 ? true : false;
																	if (fullFlag) {
																		// 满标删除redis
																		RedisUtils.del(CustomConstants.DEBT_REDITS + borrowNid);
																		// 纯发短信接口
																		Map<String, String> replaceMap = new HashMap<String, String>();
																		replaceMap.put("val_title", borrowNid);
																		replaceMap.put("val_date", DateUtils.getNowDate());
																		BorrowSendTypeExample sendTypeExample = new BorrowSendTypeExample();
																		BorrowSendTypeExample.Criteria sendTypeCriteria = sendTypeExample.createCriteria();
																		sendTypeCriteria.andSendCdEqualTo("AUTO_FULL");
																		List<BorrowSendType> sendTypeList = borrowSendTypeMapper.selectByExample(sendTypeExample);
																		if (sendTypeList != null && sendTypeList.size() > 0) {
																			BorrowSendType sendType = sendTypeList.get(0);
																			if (sendType.getAfterTime() == null) {
																				replaceMap.put("val_times", sendType.getAfterTime() + "");
																				SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_XMMB, CustomConstants.CHANNEL_TYPE_NORMAL);
																				smsProcesser.gather(smsMessage);
																			}
																		}
																	} else {
																		throw new Exception("满标更新borrow表满标状态失败，标号：" + borrowNid);
																	}
																} else if (accountWait.compareTo(new BigDecimal(0)) < 0) {
																	System.out.println("用户:" + userId + "项目编号:" + borrowNid + "****项目暴标");
																	throw new Exception("用户:" + userId + "项目编号:" + borrowNid + "****项目暴标");
																}
																// 提交事务
																this.transactionManager.commit(txStatus);
																return true;
															} else {
																throw new Exception("更新计划信息失败，计划编号：" + planNid);
															}
														} else {
															throw new Exception("更新标的信息失败，项目编号：" + borrowNid);
														}
													} else {
														throw new Exception("用户账户信息debtaccountlist表插入失败，用户userId：" + userId + ",计划加入订单号：" + planOrderId + "，出借订单号：" + orderId);
													}
												} else {
													throw new Exception("更新用户账户信息account表失败，用户userId：" + userId + ",计划加入订单号：" + planOrderId + "，出借订单号：" + orderId);
												}
											} else {
												throw new Exception("更新debtPlanAccede表失败，用户userId：" + userId + ",计划加入订单号：" + planOrderId);
											}
										} else {
											throw new Exception("插入debtInvest表失败，用户userId：" + userId + ",计划加入订单号：" + planOrderId + "，出借订单号：" + orderId);
										}
									} else {
										throw new Exception("未查询到相应的用户信息，用户userId：" + userId);
									}
								} else {
									throw new Exception("标的的还款方式为空，项目编号：" + borrowNid);
								}
							} else {
								throw new Exception("删除相应的debtFreeze插入失败，订单号：" + orderId + ",计划加入订单号：" + planOrderId);
							}
						} else {
							throw new Exception("删除相应的debtInvestTmp表失败，订单号：" + orderId + ",计划加入订单号：" + planOrderId);
						}
					} else {
						throw new Exception("未查询到相应的标的信息，项目编号：" + borrowNid);
					}
				} else {
					throw new Exception("用户账户信息查询失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				// 回滚事务
				this.transactionManager.rollback(txStatus);
				this.recoverRedis(borrowNid, userId, account.toString());
				return false;
			}
		} else {
			throw new Exception("redis操作失败，项目编号：" + borrowNid);
		}
	}

	/**
	 * 更新计划订单为完成
	 * 
	 * @param debtPlanAccede
	 * @return
	 * @author Administrator
	 */
	private boolean updateDebtPlanAccedeFinish(DebtPlanAccede debtPlanAccede) {

		DebtPlanAccede accede = this.debtPlanAccedeMapper.selectByPrimaryKey(debtPlanAccede.getId());
		accede.setStatus(1);
		boolean flag = this.debtPlanAccedeMapper.updateByPrimaryKey(accede) > 0 ? true : false;
		return flag;
	}

	/**
	 * 更新相应的出借记录为失败
	 * 
	 * @param debtPlanAccede
	 * @param userId
	 * @param investOrderId
	 * @return
	 */

	private boolean updateDebtInvestLog(DebtPlanAccede debtPlanAccede, int userId, String investOrderId) {

		// 计划nid
		String planNid = debtPlanAccede.getPlanNid();
		// 计划加入订单号
		String planOrderId = debtPlanAccede.getAccedeOrderId();
		DebtInvestLogExample example = new DebtInvestLogExample();
		DebtInvestLogExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andPlanOrderIdEqualTo(planOrderId);
		crt.andOrderIdEqualTo(investOrderId);
		crt.andUserIdEqualTo(userId);
		DebtInvestLog debtInvestLog = new DebtInvestLog();
		debtInvestLog.setStatus(2);
		boolean debtCredtTenderLogFlag = this.debtInvestLogMapper.updateByExampleSelective(debtInvestLog, example) > 0 ? true : false;
		return debtCredtTenderLogFlag;

	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */

	private AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnr accountChinapnr = null;
		if (userId == null) {
			return null;
		}
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			accountChinapnr = list.get(0);
		}
		return accountChinapnr;
	}

	/**
	 * 冻结用户的相应的订单金额
	 *
	 * @param tenderUsrcustid
	 * @param account
	 * @param borrowerUsrcustid
	 * @param OrdId
	 * @return
	 * @author b
	 */

	public String freezeOrder(int userId, String tenderUsrcustid, BigDecimal account, String orderId, String orderDate) {

		ChinapnrBean chinapnrBean = new ChinapnrBean();
		// 接口版本号
		chinapnrBean.setVersion("10");
		// 消息类型(冻结)
		chinapnrBean.setCmdId("UsrFreezeBg");
		// 出借用户客户号
		chinapnrBean.setUsrCustId(tenderUsrcustid);
		// 订单号(必须)
		chinapnrBean.setOrdId(orderId);
		// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setOrdDate(orderDate);
		// 交易金额(必须)
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account.toString()));
		// 页面返回
		chinapnrBean.setRetUrl("");
		// 商户后台应答地址(必须)
		chinapnrBean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 日志类型
		chinapnrBean.setType("user_freeze");
		chinapnrBean.setLogUserId(userId);
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (bean != null) {
			String respCode = bean.getRespCode();
			if (StringUtils.isNotEmpty(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
				System.out.println("用户:" + userId + "***********************************冻结订单号：" + bean.getTrxId());
				return bean.getTrxId();
			} else {
				System.out.println("用户:" + userId + "***********************************冻结失败错误码：" + respCode);
				return null;
			}
		} else {
			return null;
		}
	}

	private boolean unFreezeOrder(int investUserId, String orderId, String trxId, String ordDate, String unfreezeOrderId, String unfreezeOrderDate) throws Exception {

		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(investUserId);
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + investUserId + "]，" + "[出借订单号：" + orderId + "]");
		}
		// 调用交易查询接口(解冻)
		ChinapnrBean queryTransStatBean = queryTransStat(orderId, ordDate, "FREEZE");
		if (queryTransStatBean == null) {
			throw new Exception("调用交易查询接口(解冻)失败。" + ",[出借订单号：" + orderId + "]");
		} else {
			String queryRespCode = queryTransStatBean.getRespCode();
			System.out.print("出借失败交易接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", "调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orderId + "]", null);
				throw new Exception("调用交易查询接口(解冻)失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orderId + "]");
			} else {
				// 汇付交易状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
				if (!"U".equals(transStat) && !"N".equals(transStat)) {
					/** 解冻订单 */
					ChinapnrBean unFreezeBean = usrUnFreeze(trxId, unfreezeOrderId, unfreezeOrderDate);
					String respCode = unFreezeBean == null ? "" : unFreezeBean.getRespCode();
					System.out.print("出借失败自动解冻接口返回码：" + respCode);
					// 调用接口失败时(000 或 107 以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
						String message = unFreezeBean == null ? "" : unFreezeBean.getRespDesc();
						message = "调用解冻接口失败。" + respCode + "：" + message + "，出借订单号[" + orderId + "]";
						LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", message, null);
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		}
	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */
	private AccountChinapnr selectAccountChinapnr(Integer userId) {

		if (Validator.isNotNull(userId)) {
			AccountChinapnrExample example = new AccountChinapnrExample();
			AccountChinapnrExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				AccountChinapnr accountChinapnr = list.get(0);
				return accountChinapnr;
			}
		}
		return null;
	}

	/**
	 * 获取借款信息
	 *
	 * @param borrowId
	 * @return 借款信息
	 */
	private DebtBorrow selectDebtBorrowByNid(String borrowNid) {
		DebtBorrow borrow = null;
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> list = debtBorrowMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			borrow = list.get(0);
		}
		return borrow;
	}

	/**
	 * 查询用户信息
	 *
	 * @param userId
	 * @return
	 * @author b
	 */

	private Users selectUser(Integer userId) {

		if (Validator.isNotNull(userId)) {
			Users user = this.usersMapper.selectByPrimaryKey(userId);
			return user;
		} else {
			return null;
		}

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param userId
	 * @return
	 * @author b
	 */
	private UsersInfo selectUserInfo(Integer userId) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria usersInfoExampleCriteria = usersInfoExample.createCriteria();
		usersInfoExampleCriteria.andUserIdEqualTo(userId);
		List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(usersInfoExample);
		UsersInfo usersInfo = null;
		if (userInfoList != null && !userInfoList.isEmpty()) {
			usersInfo = userInfoList.get(0);
		}
		return usersInfo;
	}

	/**
	 * 
	 * 回滚redis
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param account
	 * @author Administrator
	 */
	private void recoverRedis(String borrowNid, Integer userId, String account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
		String balanceLast = RedisUtils.get(borrowNid);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(balanceLast)) {
			while ("OK".equals(jedis.watch(borrowNid))) {
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction tx = jedis.multi();
				tx.set(borrowNid, recoverAccount + "");
				List<Object> result = tx.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					System.out.println("用户:" + userId + "***********************************from redis恢复redis：" + account);
					break;
				}
			}
		}
	}

	/**
	 * 交易状态查询(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean queryTransStat(String ordId, String ordDate, String queryTransType) {
		String methodName = "queryTransStat";

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setQueryTransType(queryTransType); // 交易查询类型
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询"); // 备注
		bean.setLogClient("0"); // PC
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 资金（货款）解冻(调用汇付天下接口)
	 *
	 * @param trxId
	 * @return
	 * @throws Exception
	 */
	private ChinapnrBean usrUnFreeze(String trxId, String unfreezeOrderId, String unfreezeOrderDate) throws Exception {

		String methodName = "usrUnFreeze";
		// 调用汇付接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_USR_UN_FREEZE); // 消息类型(必须)
		bean.setOrdId(unfreezeOrderId); // 订单号(必须)
		bean.setOrdDate(unfreezeOrderDate); // 订单日期(必须)
		bean.setTrxId(trxId); // 本平台交易唯一标识(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("资金（货款）解冻"); // 备注
		bean.setLogClient("0"); // PC
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (Validator.isNull(chinapnrBean)) {
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用解冻接口失败![参数：" + bean.getAllParams() + "]"));
			throw new Exception("调用交易查询接口(解冻)失败,[冻结标识：" + trxId + "]");
		} else if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinapnrBean.getRespCode())) {
			return chinapnrBean;
		} else {
			throw new Exception("调用交易查询接口(解冻)返回错误,[冻结标识：" + trxId + "]");
		}
	}

	/**
	 * 
	 * 校验用户出借
	 * 
	 * @param borrowNid
	 * @param account
	 * @param userIdInt
	 * @param platform
	 * @param appointCheck
	 * @return
	 * @author Administrator
	 */
	public JSONObject checkParamAppointment(String borrowNid, String account, Integer userIdInt) {

		String userId = "";
		if (userIdInt == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			userId = String.valueOf(userIdInt.intValue());
		}
		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			Users user = this.getUsersByUserId(Integer.parseInt(userId));
			// 判断用户信息是否存在
			if (user == null) {
				return jsonMessage("用户信息不存在", "1");
			} else {
				// 判断用户是否禁用
				if (user.getStatus() == 1) {// 0启用，1禁用
					return jsonMessage("该用户已被禁用", "1");
				} else {// 用户存在且用户未被禁用
					/**
					 * authType 授权方式：0完全授权，1部分授权 authStatus 预约授权状态：0：未授权1：已授权
					 * authTime 授权操作时间 recodTotal 违约累计积分 recodTime 违约更新时间
					 * recodTruncateTime 积分清空时间
					 */
					Map<String, Object> map = webUserInvestListCustomizeMapper.selectUserAppointmentInfo(userId);
					if (map == null || map.isEmpty()) {
						return jsonMessage("用户未进行授权,没有用户授权信息", "1");
					} else {
						if (map.get("authStatus") == null) {
							return jsonMessage("用户未进行授权,没有用户授权状态", "1");
						} else {
							// 授权状态
							Integer authStatus = checkAppointmentStatus(userIdInt);
							if (authStatus == 0) {
								return jsonMessage("用户汇付授权状态未开启", "1");
							}
							if ((map.get("authStatus") + "").equals("0")) {
								return jsonMessage("用户未进行授权,授权状态为关闭  ", "1");
							}
						}
					}
					DebtBorrow borrow = this.getDebtBorrowByNid(borrowNid);
					AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.parseInt(userId));
					// 用户未在平台开户
					if (accountChinapnrTender == null) {
						return jsonMessage("用户开户信息不存在", "1");
					} else {
						// 判断借款人开户信息是否存在
						if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
							return jsonMessage("用户汇付客户号不存在", "1");
						} else {
							// 判断项目编号是否存在
							if (StringUtils.isEmpty(borrowNid)) {
								return jsonMessage("借款项目不存在", "1");
							} else {

								// 判断借款信息是否存在
								if (borrow == null || borrow.getId() == null) {
									return jsonMessage("借款项目不存在", "1");
								} else if (borrow.getUserId() == null) {
									return jsonMessage("借款人不存在", "1");
								} else {
									AccountChinapnr accountChinapnrBorrower = this.getAccountChinapnr(borrow.getUserId());
									if (accountChinapnrBorrower == null) {
										return jsonMessage("借款人未开户", "1");
									} else {
										if (accountChinapnrBorrower.getChinapnrUsrcustid() == null) {
											return jsonMessage("借款人汇付客户号不存在", "1");
										} else {
											if (userId.equals(String.valueOf(borrow.getUserId()))) {
												return jsonMessage("借款人不可以自己投自己项目", "1");
											} else {
												// 判断借款是否流标
												if (borrow.getStatus() == 2) { // 流标
													return jsonMessage("此项目已经流标", "1");
												} else {
													// 已满标
													if (borrow.getBorrowFullStatus() == 1) {
														return jsonMessage("此项目已经满标", "1");
													} else {
														// 判断用户出借金额是否为空
														if (StringUtils.isEmpty(account)) {
															return jsonMessage("请输入出借金额", "1");
														} else {
															// 还款金额是否数值
															try {
																// 出借金额必须是整数
																Long accountInt = Long.parseLong(account);
																if (accountInt == 0) {
																	return jsonMessage("出借金额不能为0元", "1");
																} else {
																	if (accountInt < 0) {
																		return jsonMessage("出借金额不能为负数", "1");
																	} else {
																		// 新需求判断顺序变化
																		// 将出借金额转化为BigDecimal
																		BigDecimal accountBigDecimal = new BigDecimal(account);
																		String balance = RedisUtils.get(CustomConstants.DEBT_REDITS + borrowNid);
																		if (StringUtils.isEmpty(balance)) {
																			return jsonMessage("您来晚了，下次再来抢吧", "1");
																		} else {
																			// 剩余可投金额
																			Integer min = borrow.getTenderAccountMin();
																			// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
																			if (min != null && min != 0 && new BigDecimal(balance).compareTo(new BigDecimal(min)) == -1) {
																				if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
																					return jsonMessage("剩余可出借金额为" + balance + "元", "1");
																				}
																			} else {// 项目的剩余金额大于最低起投金额
																				if (accountBigDecimal.compareTo(new BigDecimal(min)) == -1) {
																					return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
																				} else {
																					Integer max = borrow.getTenderAccountMax();
																					if (max != null && max != 0 && accountBigDecimal.compareTo(new BigDecimal(max)) == 1) {
																						return jsonMessage("项目最大出借额为" + max + "元", "1");
																					}
																				}
																			}
																			if (accountBigDecimal.compareTo(borrow.getAccount()) > 0) {
																				return jsonMessage("出借金额不能大于项目总额", "1");
																			} else {
																				Account tenderAccount = this.getAccount(Integer.parseInt(userId));
																				if (tenderAccount.getPlanAccedeBalance().compareTo(accountBigDecimal) < 0) {
																					return jsonMessage("余额不足，请充值！", "1");
																				}
																				if (StringUtils.isEmpty(balance)) {
																					return jsonMessage("您来晚了，下次再来抢吧", "1");
																				} else {
																					if (StringUtils.isNotEmpty(balance)) {
																						// redis剩余金额不足
																						if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
																							return jsonMessage("项目太抢手了！剩余可出借金额只有" + balance + "元", "1");
																						} else {
																							// 如果验证没问题，则返回出借人借款人的汇付账号
																							Long borrowerUsrcustid = accountChinapnrBorrower.getChinapnrUsrcustid();
																							Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();
																							JSONObject jsonMessage = new JSONObject();
																							jsonMessage.put("error", "0");
																							jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
																							jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
																							jsonMessage.put("borrowId", borrow.getId());
																							jsonMessage.put("bankInputFlag", borrow.getBankInputFlag() + "");
																							return jsonMessage;

																						}
																					} else {
																						return jsonMessage("您来晚了，下次再来抢吧", "1");
																					}
																				}
																			}
																		}
																	}
																}
															} catch (Exception e) {
																return jsonMessage("出借金额必须为整数", "1");
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 拼装返回信息
	 * 
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String data, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(data)) {
			jo = new JSONObject();
			jo.put("error", error);
			jo.put("data", data);
		}
		return jo;
	}

	/**
	 * 
	 * @method: checkAppointmentStatus
	 * @description: 查看预约授权状态
	 * @return
	 * @param tenderUsrcustid
	 * @return
	 * @return: Map<String, Object>
	 * @throws Exception
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:53:08
	 */
	private Integer checkAppointmentStatus(Integer usrId) {
		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(usrId);
		if (outCust == null) {
			return 0;
		}
		// 调用预约授权查询接口
		ChinapnrBean queryTransStatBean = queryAppointmentStatus(outCust.getChinapnrUsrcustid());
		if (queryTransStatBean == null) {
			return 0;
		} else {
			String queryRespCode = queryTransStatBean.getRespCode();
			System.out.print("调用预约授权查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode) && !ChinaPnrConstant.RESPCODE_NOTEXIST.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", "调用预约授权查询接口失败。" + message + ",[返回码：" + queryRespCode + "]", null);
				return 0;
			} else {
				// 汇付预约状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				if ("N".equals(transStat)) {
					return 1;
				} else if ("C".equals(transStat)) {
					return 0;
				} else if (ChinaPnrConstant.RESPCODE_NOTEXIST.equals(queryRespCode)) {
					return 0;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 调用预约授权查询接口
	 *
	 * @return
	 */
	private ChinapnrBean queryAppointmentStatus(Long Usrcustid) {

		// 调用预约授权查询接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_TENDER_PLAN); // 消息类型(必须)
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));// 商户号(必须)
		bean.setUsrCustId(Usrcustid + ""); // 订单日期(必须)
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			System.out.println("调用预约授权查询接口失败![参数：" + bean.getAllParams() + "]");
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 获取借款信息
	 *
	 * @param borrowId
	 * @return 借款信息
	 */

	private DebtBorrow getDebtBorrowByNid(String borrowNid) {
		DebtBorrow borrow = null;
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> list = debtBorrowMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			borrow = list.get(0);
		}
		return borrow;
	}



	/**
	 * 根据相应的计划加入订单记录id获取计划加入记录
	 * 
	 * @param id
	 * @return
	 * @author Administrator
	 */

	private DebtPlanAccede selectDebtPlanAccede(Integer id) {
		DebtPlanAccede debtPlanAccede = this.debtPlanAccedeMapper.selectByPrimaryKey(id);
		return debtPlanAccede;

	}

	/**
	 * 根据userId获取用户账户信息
	 * 
	 * @param userId
	 * @return
	 */

	private Account selectUserAccount(Integer userId) {

		AccountExample example = new AccountExample();
		AccountExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<Account> accountList = this.accountMapper.selectByExample(example);
		if (accountList != null && accountList.size() == 1) {
			return accountList.get(0);
		}
		return null;

	}

	// TODO
	// 自动投标结束++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * 
	 * @method: countPlan
	 * @description: 计划数量查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public int countPlan(PlanCommonCustomize planCommonCustomize) {
		int ret = 0;
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.parseInt(planCommonCustomize.getPlanStatusSrch()));
		}
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 满标/到期时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getFullExpireTime())) {
			cra.andFullExpireTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getFullExpireTime())));
		}
		// 满标/到期时间end
		if (StringUtils.isNotEmpty(planCommonCustomize.getFullExpireTimeEnd())) {
			planCommonCustomize.setFullExpireTimeEnd(planCommonCustomize.getFullExpireTimeEnd() + " 23:59:59");
			cra.andFullExpireTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getFullExpireTimeEnd())));
		}
		// 应清算时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTime())) {
			cra.andLiquidateShouldTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getLiquidateShouldTime())));
		}

		// 应清算时间end
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTimeEnd())) {
			planCommonCustomize.setLiquidateShouldTimeEnd(planCommonCustomize.getLiquidateShouldTimeEnd() + " 23:59:59");
			cra.andLiquidateShouldTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getLiquidateShouldTimeEnd())));
		}

		ret = this.debtPlanMapper.countByExample(example);

		return ret;
	}

	/**
	 * 
	 * @method: selectPlanList
	 * @description: 计划列表查询
	 * @return
	 * @return: List<DebtPlanType>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public List<DebtPlan> selectPlanList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.parseInt(planCommonCustomize.getPlanStatusSrch()));
		}
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 满标/到期时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getFullExpireTime())) {
			cra.andFullExpireTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getFullExpireTime())));
		}
		// 满标/到期时间end
		if (StringUtils.isNotEmpty(planCommonCustomize.getFullExpireTimeEnd())) {
			planCommonCustomize.setFullExpireTimeEnd(planCommonCustomize.getFullExpireTimeEnd() + " 23:59:59");
			cra.andFullExpireTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getFullExpireTimeEnd())));
		}
		// 应清算时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTime())) {
			cra.andLiquidateShouldTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getLiquidateShouldTime())));
		}
		// 应清算时间end
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTimeEnd())) {
			planCommonCustomize.setLiquidateShouldTimeEnd(planCommonCustomize.getLiquidateShouldTimeEnd() + " 23:59:59");
			cra.andLiquidateShouldTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getLiquidateShouldTimeEnd())));
		}
		// 排序
		example.setOrderByClause("liquidate_should_time ASC");
		example.setLimitStart(planCommonCustomize.getLimitStart());
		example.setLimitEnd(planCommonCustomize.getLimitEnd());
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);

		return result;
	}

	/**
	 * 
	 * @method: exportPlanList
	 * @description: 计划列表查询
	 * @return: List<DebtPlan>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public List<DebtPlan> exportPlanList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划状态 0 发起中；1 待审核；2审核不通过；3待开放；4募集中；5锁定中；6清算中；7清算完成，8未还款，9还款中，10还款完成
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			cra.andDebtPlanStatusEqualTo(Integer.parseInt(planCommonCustomize.getPlanStatusSrch()));
		}
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 满标/到期时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getFullExpireTime())) {
			cra.andFullExpireTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getFullExpireTime())));
		}
		// 满标/到期时间end
		if (StringUtils.isNotEmpty(planCommonCustomize.getFullExpireTimeEnd())) {
			planCommonCustomize.setFullExpireTimeEnd(planCommonCustomize.getFullExpireTimeEnd() + " 23:59:59");
			cra.andFullExpireTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getFullExpireTimeEnd())));
		}
		// 应清算时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTime())) {
			cra.andLiquidateShouldTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getLiquidateShouldTime())));
		}
		// 应清算时间end
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTimeEnd())) {
			planCommonCustomize.setLiquidateShouldTimeEnd(planCommonCustomize.getLiquidateShouldTimeEnd() + " 23:59:59");
			cra.andLiquidateShouldTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(planCommonCustomize.getLiquidateShouldTimeEnd())));
		}
		// 排序
		example.setOrderByClause("liquidate_should_time Desc");
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);
		return result;
	}

	@Override
	public Long countPlanAccede(PlanCommonCustomize planCommonCustomize) {
		Long ret = planLockCustomizeMapper.countPlanAccedeForAdmin(planCommonCustomize);
		return ret;
	}

	@Override
	public List<PlanLockCustomize> selectPlanAccedeList(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectPlanAccedeListForAdmin(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> selectPlanCountMap(String planNidSrch) {
		return planLockCustomizeMapper.selectPlanCountMap(planNidSrch);
	}

	@Override
	public HashMap<String, Object> planLockSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.planLockSumMap(planCommonCustomize);
	}

	@Override
	public int updateCycleTimesZero(String accedeorderId) {
		return planLockCustomizeMapper.updateCycleTimesZero(accedeorderId);
	}

	@Override
	public List<PlanInvestCustomize> selectPlanInvestList(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectPlanInvestList(planCommonCustomize);
	}

	@Override
	public Long countPlanInvest(PlanCommonCustomize planCommonCustomize) {

		return planLockCustomizeMapper.countPlanInvest(planCommonCustomize);

	}

	@Override
	public HashMap<String, Object> planInvestSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.planInvestSumMap(planCommonCustomize);
	}

	@Override
	public int countDebtLoan(PlanCommonCustomize planCommonCustomize) {

		int ret = 0;
		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria cra = example.createCriteria();
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(planCommonCustomize.getPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andPlanOrderIdLike("%" + planCommonCustomize.getPlanOrderId() + "%");
		}
		// 用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameLike("%" + planCommonCustomize.getUserName() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getBorrowNid())) {
			cra.andBorrowNidLike("%" + planCommonCustomize.getBorrowNid() + "%");
		}
		// 回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(planCommonCustomize.getRepayStatus()));
		}
		// 应还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThan(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart()));
		}
		// 应还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.getDayEnd(planCommonCustomize.getRepayTimeEnd()));
		}
		ret = this.debtLoanMapper.countByExample(example);
		return ret;
	}

	@Override
	public List<DebtLoan> selectDebtLoanList(PlanCommonCustomize planCommonCustomize) {

		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria cra = example.createCriteria();
		// 计划订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(planCommonCustomize.getPlanNidSrch());
		}
		// 计划订单号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanOrderId())) {
			cra.andPlanOrderIdLike("%" + planCommonCustomize.getPlanOrderId() + "%");
		}
		// 用户名
		if (StringUtils.isNotEmpty(planCommonCustomize.getUserName())) {
			cra.andUserNameLike("%" + planCommonCustomize.getUserName() + "%");
		}
		// 项目编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getBorrowNid())) {
			cra.andBorrowNidLike("%" + planCommonCustomize.getBorrowNid() + "%");
		}
		// 回款状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayStatus())) {
			cra.andRepayStatusEqualTo(Integer.parseInt(planCommonCustomize.getRepayStatus()));
		}
		// 应还款时间开始
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeStart())) {
			cra.andRepayTimeGreaterThan(GetDate.getDayStart(planCommonCustomize.getRepayTimeStart()));
		}
		// 应还款时间结束
		if (StringUtils.isNotEmpty(planCommonCustomize.getRepayTimeEnd())) {
			cra.andRepayTimeGreaterThanOrEqualTo(GetDate.getDayEnd(planCommonCustomize.getRepayTimeEnd()));
		}
		// 排序
		example.setOrderByClause("repay_time Desc");
		example.setLimitStart(planCommonCustomize.getLimitStart());
		example.setLimitEnd(planCommonCustomize.getLimitEnd());
		return debtLoanMapper.selectByExample(example);
	}

	@Override
	public HashMap<String, Object> DebtLoanSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.DebtLoanSumMap(planCommonCustomize);
	}

	@Override
	public List<DebtPlanAccede> getDebtPlanAccedes(String accedeOrderId) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cra = example.createCriteria();
		cra.andAccedeOrderIdEqualTo(accedeOrderId);
		return debtPlanAccedeMapper.selectByExample(example);

	}

	@Override
	public Long countLoanDetail(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.countLoanDetail(planCommonCustomize);
	}

	@Override
	public List<Map<String, Object>> selectLoanDetailList(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectLoanDetailList(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> LoanDeailSumMap(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.LoanDeailSumMap(planCommonCustomize);
	}

	@Override
	public Long countLoanDetailNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.countLoanDetailNew(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> LoanDeailSumMapNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.LoanDeailSumMapNew(planCommonCustomize);
	}

	@Override
	public List<Map<String, Object>> selectLoanDetailListNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectLoanDetailListNew(planCommonCustomize);
	}

	/**
	 * 检索汇添金转让类产品件数
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public int countCreditProject(Map<String, Object> param) {

		return this.debtAdminCreditCustomizeMapper.countCreditProject(param);
	}

	/**
	 * 检索汇添金转让类产品列表
	 * 
	 * @Title selectDebtCreditProject
	 * @param param
	 * @return
	 */
	@Override
	public List<DebtAdminCreditCustomize> selectDebtCreditProject(Map<String, Object> param) {

		return this.debtAdminCreditCustomizeMapper.selectDebtCreditProject(param);
	}

	/**
	 * 根据加入订单号查询加入详情列表
	 * 
	 * @Title selectDebtPlanAccedeByAccedeOrderId
	 * @param accedeOrderId
	 * @return
	 */
	@Override
	public List<DebtPlanAccede> selectDebtPlanAccedeByAccedeOrderId(String accedeOrderId) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cra = example.createCriteria();
		cra.andAccedeOrderIdEqualTo(accedeOrderId);
		List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(example);
		return list;
	}

	@Override
	public Long countPlanInvestNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.countPlanInvestNew(planCommonCustomize);
	}

	@Override
	public HashMap<String, Object> planInvestSumMapNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.planInvestSumMapNew(planCommonCustomize);
	}

	@Override
	public List<PlanInvestCustomize> selectPlanInvestListNew(PlanCommonCustomize planCommonCustomize) {
		return planLockCustomizeMapper.selectPlanInvestListNew(planCommonCustomize);
	}

	/**
	 * 计算计划到期出借利率率
	 * 
	 * @Title calculationPlanExpectApr
	 * @param plan
	 * @return
	 */
	@Override
	public String calculationPlanExpectApr(DebtPlan plan) {
		String debtPlanNid = plan.getDebtPlanNid();
		BigDecimal planExpireFairValue = BigDecimal.ZERO;
		BigDecimal expireApr = BigDecimal.ZERO;
		HashMap<String, Object> result = this.planLockCustomizeMapper.sumPlanExpireFairValue(debtPlanNid);
		if (result != null && !"0.00".equals(result.get("expireFairValue"))) {
			planExpireFairValue = new BigDecimal(result.get("expireFairValue").toString());
			BigDecimal expireExpireFairValuer = planExpireFairValue.add(plan.getDebtPlanBalance()).add(plan.getDebtPlanFrost()).add(new BigDecimal(result.get("liquidatesCreditFrost").toString())).add(new BigDecimal(result.get("liquidatesRepayFrost").toString()))
					.add(new BigDecimal(result.get("serviceFee").toString())).subtract(plan.getDebtPlanMoneyYes());
			if (BigDecimal.ZERO.compareTo(expireExpireFairValuer) >= 0 || plan.getDebtPlanMoneyYes().compareTo(BigDecimal.ZERO) == 0) {
				return "0.00";
			}
			expireApr = expireExpireFairValuer.divide(plan.getDebtPlanMoneyYes(), 8, BigDecimal.ROUND_DOWN).divide(new BigDecimal(plan.getDebtLockPeriod()), 8, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(12)).multiply(new BigDecimal(100));
			expireApr = expireApr.setScale(2, BigDecimal.ROUND_DOWN);
		}
		// 预期到期出借利率小于0
		if (expireApr.compareTo(BigDecimal.ZERO) < 0) {
			return "0.00";
		} else {
			return expireApr.toString();
		}
	}

	/**
	 * 根据计划编号检索计划详情
	 * 
	 * @Title selectDebtPlanInfoByPlanNid
	 * @param planNid
	 * @return
	 */
	@Override
	public DebtPlan selectDebtPlanInfoByPlanNid(String planNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPlanNidEqualTo(planNid);
		List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	public Account getAccount(Integer userId) {
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Account> listAccount = accountMapper.selectByExample(example);
        if (listAccount != null && listAccount.size() > 0) {
            return listAccount.get(0);
        }
        return null;
    }
}
