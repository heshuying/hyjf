/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.invest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.app.user.credit.AppTenderCreditService;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.FinancingServiceChargeUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.*;

@Service("userInvestService")
public class InvestServiceImpl extends BaseServiceImpl implements InvestService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	@Autowired
	private PlatformTransactionManager transactionManager;
	public static JedisPool pool = RedisUtils.getConnection();
	@Autowired
	private TransactionDefinition transactionDefinition;
	@Autowired
	private AppTenderCreditService appTenderCreditService;
	@Autowired
    private AuthService authService;
	
	Logger _log = LoggerFactory.getLogger(InvestServiceImpl.class);

	/**
	 * 调用汇付天下接口前操作,
	 * 插入huiyingdai_borrow_tender_tmp和huiyingdai_borrow_tender_tmpinfo表
	 *
	 * @param borrowId
	 *            借款id
	 * @param userId
	 *            用户id
	 * @param account
	 *            出借金额
	 * @param ip
	 *            出借人ip
	 * @return 出借是否成功
	 */
	@Override
	public Boolean updateBeforeChinaPnR(String borrowNid, String orderId, Integer userId, String account, String ip, String couponGrantId,String userName) {

		BorrowTenderTmp temp = new BorrowTenderTmp();
		temp.setUserId(userId);
		temp.setBorrowNid(borrowNid);
		temp.setNid(orderId);
		temp.setAccount(new BigDecimal(account));
		temp.setAddip(ip);
		temp.setChangeStatus(0);
		temp.setChangeUserid(0);
		temp.setChangePeriod(0);
		temp.setTenderStatus(0);
		temp.setTenderNid(borrowNid);
		temp.setTenderAwardAccount(new BigDecimal(0));
		temp.setRecoverFullStatus(0);
		temp.setRecoverFee(new BigDecimal(0));
		temp.setRecoverType("");
		temp.setRecoverAdvanceFee(new BigDecimal(0));
		temp.setRecoverLateFee(new BigDecimal(0));
		temp.setTenderAwardFee(new BigDecimal(0));
		temp.setContents("");
		temp.setAutoStatus(0);
		temp.setWebStatus(0);
		temp.setPeriodStatus(0);
		temp.setWeb(0);
		temp.setIsBankTender(1);
		temp.setAddtime(String.valueOf(GetDate.getNowTime10()));
		temp.setTenderUserName(userName);
		if (StringUtils.isBlank(couponGrantId)) {
			couponGrantId = "0";
		}
		temp.setCouponGrantId(Integer.parseInt(couponGrantId));// add by cwyang 为出借完全掉单优惠券出借时修复做记录
		boolean tenderTmpFlag = borrowTenderTmpMapper.insertSelective(temp) > 0 ? true : false;
		if (!tenderTmpFlag) {
			throw new RuntimeException("插入borrowTenderTmp表失败，出借订单号：" + orderId);
		}
		BorrowTenderTmpInfo info = new BorrowTenderTmpInfo();
		info.setOrdid(orderId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("borrow_nid", borrowNid);
		map.put("user_id", userId + "");
		map.put("account", account + "");
		map.put("status", "0");
		map.put("nid", orderId);
		map.put("addtime", (new Date().getTime() / 1000) + "");
		map.put("addip", ip);
		String array = JSON.toJSONString(map);
		info.setTmpArray(array);
		info.setAddtime((new Date().getTime() / 1000) + "");
		Boolean tenderTmpInfoFlag = borrowTenderTmpInfoMapper.insertSelective(info) > 0 ? true : false;
		if (!tenderTmpInfoFlag) {
			throw new RuntimeException("插入borrowTenderTmpInfo表失败，出借订单号：" + orderId);
		}
		return tenderTmpInfoFlag;

	}

	/**
	 * 用户出借
	 * 
	 * @param borrowId
	 * @param userId
	 * @param account
	 * @param ip
	 * @return
	 * @throws Exception
	 */

	@Override
	public synchronized JSONObject userTender(Borrow borrow, ChinapnrBean bean) {

		JSONObject info = new JSONObject();
		// 操作ip
		String ip = bean.getLogIp();
		// 操作平台
		int client = StringUtils.isNotBlank(bean.getLogClient()) ? Integer.parseInt(bean.getLogClient()) : 0;
		// 获取出借用户的出借客户号
		String tenderUsrcustid = bean.getUsrCustId();
		// 出借人id
		Integer userId = bean.getLogUserId();
		// 冻结标示
		String freezeTrxId = bean.getFreezeTrxId();
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String orderId = bean.getOrdId();
		// 订单日期
		String orderDate = bean.getOrdDate();
		// 出借结果返回码
		String respCode = bean.getRespCode();
		// 项目编号
		String borrowNid = borrow.getBorrowNid();
		// 项目的还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 项目类型
		int projectType = borrow.getProjectType();
		// 服务费率
		BigDecimal serviceFeeRate = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getServiceFeeRate());
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 借款项目主键
		Integer borrowId = borrow.getId();
		// 手动控制事务
		TransactionStatus txStatus = null;
		// 发送状态
		String status = ChinaPnrConstant.STATUS_FAIL;
		// 冻结编号
		BigDecimal accountBigDecimal = new BigDecimal(account);
		Jedis jedis = pool.getResource();
		String accountRedisWait = RedisUtils.get(borrowNid);
		if (StringUtils.isNotBlank(accountRedisWait)) {
			// 操作redis
			while ("OK".equals(jedis.watch(borrowNid))) {
				accountRedisWait = RedisUtils.get(borrowNid);
				if (StringUtils.isNotBlank(accountRedisWait)) {
					_log.info("APP用户同步:" + userId + "***********************************冻结前可投金额：" + accountRedisWait);
					if (new BigDecimal(accountRedisWait).compareTo(BigDecimal.ZERO) == 0) {
						info.put("message", "您来晚了，下次再来抢吧！");
						info.put("status", status);
						break;
					} else {
						if (new BigDecimal(accountRedisWait).compareTo(accountBigDecimal) < 0) {
							info.put("message", "可投剩余金额为" + accountRedisWait + "元！");
							info.put("status", status);
							break;
						} else {
							Transaction tx = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(accountRedisWait).subtract(accountBigDecimal);
							tx.set(borrowNid, lastAccount + "");
							List<Object> result = tx.exec();
							if (result == null || result.isEmpty()) {
								jedis.unwatch();
							} else {
								String ret = (String) result.get(0);
								if (ret != null && ret.equals("OK")) {
									status = BankCallConstant.STATUS_VERTIFY_OK;
									info.put("message", "redis操作成功！");
									info.put("status", status);
									_log.info("PC用户:" + userId + "***冻结前减redis：" + accountBigDecimal);
									break;
								} else {
									jedis.unwatch();
								}
							}
						}
					}
				} else {
					info.put("message", "您来晚了，下次再来抢吧！");
					info.put("status", status);
					break;
				}
			}
		} else {
			info.put("message", "您来晚了，下次再来抢吧！");
			info.put("status", status);
		}
		if (ChinaPnrConstant.STATUS_VERTIFY_OK.equals(status)) {
			try {
				// 开启事务
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				int nowTime = GetDate.getNowTime10();
				// 删除临时表
				BorrowTenderTmpExample borrowTenderTmpExample = new BorrowTenderTmpExample();
				BorrowTenderTmpExample.Criteria criteria1 = borrowTenderTmpExample.createCriteria();
				criteria1.andNidEqualTo(orderId);
				criteria1.andUserIdEqualTo(userId);
				criteria1.andBorrowNidEqualTo(borrowNid);
				boolean tenderTempFlag = borrowTenderTmpMapper.deleteByExample(borrowTenderTmpExample) > 0 ? true : false;
				if (tenderTempFlag) {
					_log.info("用户:" + userId + "***********************************删除borrowTenderTmp，订单号：" + orderId);
					// 插入冻结表
					FreezeList record = new FreezeList();
					record.setAmount(new BigDecimal(account));
					record.setBorrowNid(borrowNid);
					record.setCreateTime(nowTime);
					record.setOrdid(orderId);
					record.setUserId(userId);
					record.setRespcode(respCode);
					record.setTrxid(freezeTrxId);
					record.setOrdid(orderId);
					record.setUsrcustid(tenderUsrcustid);
					record.setXfrom(1);
					record.setStatus(0);
					record.setUnfreezeManual(0);
					_log.info("用户:" + userId + "***********************************预插入FreezeList：" + JSON.toJSONString(record));
					boolean freezeFlag = freezeListMapper.insertSelective(record) > 0 ? true : false;
					if (freezeFlag) {
						BigDecimal accountDecimal = new BigDecimal(account);
						BigDecimal perService = new BigDecimal(0);
						if (StringUtils.isNotEmpty(borrowStyle)) {
							BigDecimal serviceScale = serviceFeeRate;
							// 到期还本还息end/先息后本endmonth/等额本息month/等额本金principal
							if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
								perService = FinancingServiceChargeUtils.getMonthsPrincipalServiceCharge(accountDecimal, serviceScale);
							}
							// 按天计息到期还本还息
							if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
								perService = FinancingServiceChargeUtils.getDaysPrincipalServiceCharge(accountDecimal, serviceScale, borrowPeriod);
							}
						}
						BorrowTender borrowTender = new BorrowTender();
						borrowTender.setAccount(accountDecimal);
						borrowTender.setAccountTender(new BigDecimal(0));
						borrowTender.setActivityFlag(0);//
						borrowTender.setAddip(ip);
						borrowTender.setAddtime(nowTime);
						borrowTender.setApiStatus(0);//
						borrowTender.setAutoStatus(0);//
						borrowTender.setBorrowNid(borrowNid);
						borrowTender.setChangePeriod(0);//
						borrowTender.setChangeUserid(0);
						borrowTender.setClient(0);
						borrowTender.setContents("");//
						borrowTender.setFlag(0);//
						borrowTender.setIsok(0);
						borrowTender.setIsReport(0);
						borrowTender.setChangeStatus(0);
						borrowTender.setLoanAmount(accountDecimal.subtract(perService));
						borrowTender.setNid(orderId);
						borrowTender.setOrderDate(orderDate);
						borrowTender.setPeriodStatus(0);//
						borrowTender.setRecoverAccountAll(new BigDecimal(0));//
						borrowTender.setRecoverAccountCapitalWait(new BigDecimal(0));//
						borrowTender.setRecoverAccountCapitalYes(new BigDecimal(0));
						borrowTender.setRecoverAccountInterest(new BigDecimal(0));
						borrowTender.setRecoverAccountInterestWait(new BigDecimal(0));
						borrowTender.setRecoverAccountInterestYes(new BigDecimal(0));
						borrowTender.setRecoverAccountWait(new BigDecimal(0));
						borrowTender.setRecoverAccountYes(new BigDecimal(0));
						borrowTender.setRecoverAdvanceFee(new BigDecimal(0));
						borrowTender.setRecoverFee(new BigDecimal(0));
						borrowTender.setRecoverFullStatus(0);
						borrowTender.setRecoverLateFee(new BigDecimal(0));
						borrowTender.setRecoverTimes(0);
						borrowTender.setRecoverType("");
						borrowTender.setStatus(0);
						borrowTender.setTenderAwardAccount(new BigDecimal(0));
						borrowTender.setTenderAwardFee(new BigDecimal(0));
						borrowTender.setTenderNid(borrowNid);
						borrowTender.setTenderStatus(0);
						borrowTender.setUserId(userId);
						// 出借人信息
						Users users = getUsers(userId);
						// add by zhangjp vip出借记录 start
						UsersInfo userInfo = null;
						// add by zhangjp vip出借记录 end

						if (users != null && projectType != 8) {
							// 更新渠道统计用户累计出借
							AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
							AppChannelStatisticsDetailExample.Criteria crt = channelExample.createCriteria();
							crt.andUserIdEqualTo(userId);
							List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(channelExample);
							if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
								AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("id", channelDetail.getId());
								params.put("accountDecimal", accountDecimal);
								// 出借时间
								params.put("investTime", nowTime);
								// 项目类型
								if (borrow.getProjectType() == 13) {
									params.put("projectType", "汇金理财");
								} else {
									params.put("projectType", "汇直投");
								}
								// 首次投标项目期限
								String investProjectPeriod = "";
								if (borrow.getBorrowStyle().equals("endday")) {
									investProjectPeriod = borrow.getBorrowPeriod() + "天";
								} else {
									investProjectPeriod = borrow.getBorrowPeriod() + "个月";
								}
								params.put("investProjectPeriod", investProjectPeriod);
								// 更新渠道统计用户累计出借
								if (users.getInvestflag() == 1) {
									this.appChannelStatisticsDetailCustomizeMapper.updateAppChannelStatisticsDetail(params);
								} else if (users.getInvestflag() == 0) {
									// 更新首投出借
									this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
								}
								_log.info("用户:" + userId + "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + orderId);
							} else {
								// 更新huiyingdai_utm_reg的首投信息
								UtmRegExample utmRegExample = new UtmRegExample();
								UtmRegExample.Criteria utmRegCra = utmRegExample.createCriteria();
								utmRegCra.andUserIdEqualTo(userId);
								List<UtmReg> utmRegList = this.utmRegMapper.selectByExample(utmRegExample);
								if (utmRegList != null && utmRegList.size() > 0) {
									UtmReg utmReg = utmRegList.get(0);
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("id", utmReg.getId());
									params.put("accountDecimal", accountDecimal);
									// 出借时间
									params.put("investTime", nowTime);
									// 项目类型
									if (borrow.getProjectType() == 13) {
										params.put("projectType", "汇金理财");
									} else {
										params.put("projectType", "汇直投");
									}
									// 首次投标项目期限
									String investProjectPeriod = "";
									if (borrow.getBorrowStyle().equals("endday")) {
										investProjectPeriod = borrow.getBorrowPeriod() + "天";
									} else {
										investProjectPeriod = borrow.getBorrowPeriod() + "个月";
									}
									params.put("investProjectPeriod", investProjectPeriod);

									// 更新渠道统计用户累计出借
									if (users.getInvestflag() == 0) {
										// 更新huiyingdai_utm_reg的首投信息
										this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
									}
								}
							}
						}
						if (users != null) {
							if (users.getInvestflag() == 0) {
								users.setInvestflag(1);
								this.usersMapper.updateByPrimaryKeySelective(users);
							} else {
								if (projectType == 4) {
									status = ChinaPnrConstant.STATUS_FAIL;
									info.put("message", "该项目限投一笔！");
									info.put("status", status);
									this.recoverRedis(borrowNid, userId, account);
									// 回滚事务
									this.transactionManager.rollback(txStatus);
									return info;
								}
							}
							// 出借用户名
							borrowTender.setTenderUserName(users.getUsername());
							// 获取出借人属性
							// modify by zhangjp vip出借记录 start
							// UsersInfo userInfo = getUserInfo(userId);
							userInfo = getUserInfo(userId);
							// modify by zhangjp vip出借记录 end
							// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
							Integer attribute = null;
							if (userInfo != null) {
								// 获取出借用户的用户属性
								attribute = userInfo.getAttribute();
								if (attribute != null) {
									// 出借人用户属性
									borrowTender.setTenderUserAttribute(attribute);
									// 如果是线上员工或线下员工，推荐人的userId和username不插
									if (attribute == 2 || attribute == 3) {
										EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
										if (employeeCustomize != null) {
											borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
											borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
											borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
											borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
											borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
											borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
										}
									} else if (attribute == 1) {
										SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
										SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
										spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
										List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
										if (sList != null && sList.size() == 1) {
											int refUserId = sList.get(0).getSpreadsUserid();
											// 查找用户推荐人
											Users userss = getUsers(refUserId);
											if (userss != null) {
												borrowTender.setInviteUserId(userss.getUserId());
												borrowTender.setInviteUserName(userss.getUsername());
											}
											// 推荐人信息
											UsersInfo refUsers = getUserInfo(refUserId);
											// 推荐人用户属性
											if (refUsers != null) {
												borrowTender.setInviteUserAttribute(refUsers.getAttribute());
											}
											// 查找用户推荐人部门
											EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
											if (employeeCustomize != null) {
												borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
												borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
												borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
												borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
												borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
												borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
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
											Users userss = getUsers(refUserId);
											if (userss != null) {
												borrowTender.setInviteUserId(userss.getUserId());
												borrowTender.setInviteUserName(userss.getUsername());
											}
											// 推荐人信息
											UsersInfo refUsers = getUserInfo(refUserId);
											// 推荐人用户属性
											if (refUsers != null) {
												borrowTender.setInviteUserAttribute(refUsers.getAttribute());
											}
										}
									}
								}
							}
						}
						borrowTender.setWeb(0);
						borrowTender.setWebStatus(0);
						borrowTender.setClient(client);
						borrowTender.setInvestType(0);
						// add by zhangjp vip出借记录 start
						borrowTender.setRemark("现金出借");
						// add by zhangjp vip出借记录 end
						// 单笔出借的融资服务费
						borrowTender.setLoanFee(perService);
						boolean trenderFlag = borrowTenderMapper.insertSelective(borrowTender) > 0 ? true : false;
						if (trenderFlag) {
							_log.info("用户:" + userId + "***********************************插入borrowTender，订单号：" + orderId);
							// add by zhangjp vip出借记录 start
							if (userInfo != null && userInfo.getVipId() != null) {
								VipUserTender vt = new VipUserTender();
								// 出借用户编号
								vt.setUserId(userId);
								// 出借用户vip编号
								vt.setVipId(userInfo.getVipId());
								// 出借编号
								vt.setTenderNid(orderId);
								// 账户V值
								vt.setSumVipValue(userInfo.getVipValue());
								vt.setAddTime(nowTime);
								vt.setAddUser(String.valueOf(userId));
								vt.setUpdateTime(nowTime);
								vt.setUpdateUser(String.valueOf(userId));
								vt.setDelFlg(0);
								this.vipUserTenderMapper.insertSelective(vt);
							}
							// add by zhangjp vip出借记录 end

							// 新出借金额汇总
							AccountExample example = new AccountExample();
							AccountExample.Criteria criteria = example.createCriteria();
							criteria.andUserIdEqualTo(userId);
							List<Account> list = accountMapper.selectByExample(example);
							if (list != null && list.size() == 1) {
								// 总金额
								BigDecimal total = list.get(0).getTotal();
								// 冻结金额
								BigDecimal frost = list.get(0).getFrost();
								// 可用金额
								BigDecimal balance = list.get(0).getBalance();
								// 更新用户账户余额表
								// 更新用户账户余额表
								Account accountBean = new Account();
								accountBean.setUserId(userId);
								// 出借人冻结金额增加
								accountBean.setBankFrost(accountDecimal);
								// 出借人可用余额扣减
								accountBean.setBankBalance(accountDecimal);
								// 江西银行账户余额
								accountBean.setBankBalanceCash(accountDecimal);
								// 江西银行账户冻结金额
								accountBean.setBankFrostCash(accountDecimal);
								Boolean accountFlag = this.adminAccountCustomizeMapper.updateAccountOfTender(accountBean) > 0 ? true : false;
								// 插入account_list表
								if (accountFlag) {
									_log.info("用户:" + userId + "***********************************更新account，订单号：" + orderId);
									AccountList accountList = new AccountList();
									accountList.setAmount(accountDecimal);
									accountList.setAwait(new BigDecimal(0));
									accountList.setBalance(balance.subtract(accountDecimal));
									accountList.setBaseUpdate(0);
									accountList.setCreateTime(nowTime);
									accountList.setFrost(frost.add(accountDecimal));
									accountList.setInterest(new BigDecimal(0));
									accountList.setIp(ip);
									accountList.setIsUpdate(0);
									accountList.setNid(orderId);
									accountList.setOperator(userId + "");
									accountList.setRemark(borrowNid);
									accountList.setRepay(new BigDecimal(0));
									accountList.setTotal(total);
									accountList.setTrade("tender");// 出借
									accountList.setTradeCode("frost");// 投标冻结后为frost
									accountList.setType(3);// 收支类型1收入2支出3冻结
									accountList.setUserId(userId);
									accountList.setWeb(0);
									boolean accountListFlag = accountListMapper.insert(accountList) > 0 ? true : false;
									if (accountListFlag) {
										_log.info("用户:" + userId + "***********************************插入accountList，订单号：" + orderId);
										// 更新borrow表
										Map<String, Object> borrowParam = new HashMap<String, Object>();
										borrowParam.put("borrowAccountYes", accountDecimal);
										borrowParam.put("borrowService", perService);
										borrowParam.put("borrowId", borrowId);
										boolean updateBorrowAccountFlag = borrowCustomizeMapper.updateOfBorrow(borrowParam) > 0 ? true : false;
										if (updateBorrowAccountFlag) {
											_log.info("用户:" + userId + "***********************************更新borrow表，订单号：" + orderId);
											List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
											if (calculates != null && calculates.size() > 0) {
												CalculateInvestInterest calculateNew = new CalculateInvestInterest();
												calculateNew.setTenderSum(accountDecimal);
												calculateNew.setId(calculates.get(0).getId());
												calculateNew.setCreateTime(GetDate.getDate(nowTime));
												this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
											}

											// 计算此时的剩余可出借金额
											BigDecimal accountWait =this.getBorrowByNid(borrowNid).getBorrowAccountWait();
											// 满标处理
											if (accountWait.compareTo(new BigDecimal(0)) == 0) {
												_log.info("用户:" + userId + "***********************************项目满标，订单号：" + orderId);
												Map<String, Object> borrowFull = new HashMap<String, Object>();
												borrowFull.put("borrowId", borrowId);
												boolean fullFlag = borrowCustomizeMapper.updateOfFullBorrow(borrowFull) > 0 ? true : false;
												if (fullFlag) {
													// 清除标总额的缓存
													RedisUtils.del(borrowNid);
													// 纯发短信接口
													Map<String, String> replaceMap = new HashMap<String, String>();
													replaceMap.put("val_title", borrowNid);
													replaceMap.put("val_date", DateUtils.getNowDate());
													BorrowSendTypeExample sendTypeExample = new BorrowSendTypeExample();
													BorrowSendTypeExample.Criteria sendTypeCriteria = sendTypeExample.createCriteria();
													sendTypeCriteria.andSendCdEqualTo("AUTO_FULL");
													List<BorrowSendType> sendTypeList = borrowSendTypeMapper.selectByExample(sendTypeExample);
													if (sendTypeList == null || sendTypeList.size() == 0) {
														_log.info("用户:" + userId + "***********************************冻结成功后处理afterChinaPnR：" + "数据库查不到 sendTypeList == null");
														throw new Exception("数据库查不到" + BorrowSendType.class);
													}
													BorrowSendType sendType = sendTypeList.get(0);
													if (sendType.getAfterTime() == null) {
														_log.info("用户:" + userId + "***********************************冻结成功后处理afterChinaPnR：" + "sendType.getAfterTime()==null");
														throw new Exception("sendType.getAfterTime()==null");
													}
													replaceMap.put("val_times", sendType.getAfterTime() + "");
													SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_XMMB,
															CustomConstants.CHANNEL_TYPE_NORMAL);
													smsProcesser.gather(smsMessage);
												} else {
													throw new Exception("满标更新borrow表失败");
												}
											} else if (accountWait.compareTo(new BigDecimal(0)) < 0) {
												_log.info("用户:" + userId + "项目编号:" + borrowNid + "***********************************项目暴标");
												throw new RuntimeException("没有可投金额");
											}
											String tenderId = orderId.substring(10);
											// 扣除可用金额
											AccountLog accountLog = new AccountLog();
											accountLog.setUserId(userId);// 操作用户id
											accountLog.setNid("tender_frost_" + userId + "_" + borrowNid + "_" + tenderId);
											accountLog.setTotalOld(BigDecimal.ZERO);
											accountLog.setCode("borrow");
											accountLog.setCodeType("tender");
											accountLog.setCodeNid(tenderId);
											accountLog.setBorrowNid(borrowNid);// 收入
											accountLog.setIncomeOld(BigDecimal.ZERO);
											accountLog.setIncomeNew(BigDecimal.ZERO);
											accountLog.setAccountWebStatus(0);
											accountLog.setAccountUserStatus(0);
											accountLog.setAccountType("");
											accountLog.setMoney(accountDecimal);// 操作金额
											accountLog.setIncome(BigDecimal.ZERO);// 收入
											accountLog.setExpend(BigDecimal.ZERO);// 支出
											accountLog.setExpendNew(BigDecimal.ZERO);
											accountLog.setBalanceOld(BigDecimal.ZERO);
											accountLog.setBalanceNew(BigDecimal.ZERO);
											accountLog.setBalanceCash(BigDecimal.ZERO);
											accountLog.setBalanceCashNew(BigDecimal.ZERO);
											accountLog.setBalanceCashOld(BigDecimal.ZERO);
											accountLog.setExpendOld(BigDecimal.ZERO);
											accountLog.setBalanceCash(BigDecimal.ZERO);// 可提现金额
											accountLog.setBalanceFrost(accountDecimal.multiply(new BigDecimal(-1)));// 不可提现金额
											accountLog.setFrost(frost);// 冻结金额
											accountLog.setFrostOld(BigDecimal.ZERO);
											accountLog.setFrostNew(BigDecimal.ZERO);
											accountLog.setAwait(BigDecimal.ZERO);// 待收金额
											accountLog.setRepay(BigDecimal.ZERO);// 待还金额
											accountLog.setRepayOld(BigDecimal.ZERO);
											accountLog.setRepayNew(BigDecimal.ZERO);
											accountLog.setAwait(BigDecimal.ZERO);
											accountLog.setAwaitNew(BigDecimal.ZERO);
											accountLog.setAwaitOld(BigDecimal.ZERO);
											accountLog.setType("tender");// 类型
											accountLog.setToUserid(borrowPeriod); // 付给谁
											accountLog.setRemark("投标[" + borrowNid + "]所冻结资金");// 备注
											accountLog.setAddtime(String.valueOf(nowTime));
											accountLog.setAddip(ip);
											accountLog.setBalanceFrostNew(BigDecimal.ZERO);
											accountLog.setBalanceFrostOld(BigDecimal.ZERO);
											boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0 ? true : false;
											if (accountLogFlag) {
												_log.info("用户:" + userId + "***********************************插入accountLog，订单号：" + orderId);
												// 提交事务
												this.transactionManager.commit(txStatus);
												info.put("isExcute", "1");
												return info;
											} else {
												throw new RuntimeException("accountLog表更新失败");
											}
										} else {
											throw new RuntimeException("borrow表更新失败");
										}
									} else {
										throw new RuntimeException("用户账户交易明细表更新失败");
									}
								} else {
									throw new RuntimeException("用户账户信息表更新失败");
								}
							} else {
								throw new RuntimeException("用户账户信息查询失败！");
							}
						} else {
							throw new RuntimeException("插入出借表失败！");
						}
					} else {
						throw new RuntimeException("插入冻结表失败！");
					}
				} else {
					throw new RuntimeException("删除borrowTenderTmp表失败");
				}

			} catch (Exception e) {
				// 回滚事务
				this.transactionManager.rollback(txStatus);
				this.recoverRedis(borrowNid, userId, account);
				status = ChinaPnrConstant.STATUS_FAIL;
				info.put("message", "出借失败！");
				info.put("status", status);
			}
		}
		return info;
	}

	/**
	 * 优惠券出借
	 */
	@Override
	public synchronized Boolean updateCouponTender(String couponGrantId, String borrowNid, String ordDate, Integer userId, String account, String ip, Integer client, int couponOldTime,
			String mainTenderNid, Map<String, Object> retMap) {
		LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借开始。。。。。。");

		LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借开始。。。。。。");

		JSONObject jsonObject = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, mainTenderNid, ip, couponOldTime + "");

		if (jsonObject.getIntValue("status") == 0) {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			return true;
		} else {
			LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借结束。。。。。。");
			return false;
		}

		/*
		 * 
		 * 
		 * 
		 * _log.info("优惠券出借开始。。。。。。券编号：" + couponGrantId); Borrow
		 * borrow = this.getBorrowByNid(borrowNid); String methodName =
		 * "updateCouponTender"; int nowTime = GetDate.getNowTime10();
		 * Map<String, Object> paramMap = new HashMap<String, Object>();
		 * paramMap.put("couponGrantId", couponGrantId); paramMap.put("userId",
		 * userId); CouponConfigCustomizeV2 ccTemp =
		 * this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		 * if (Validator.isNotNull(ccTemp)) { // 优惠券类别 int couponType =
		 * ccTemp.getCouponType(); // 面值 BigDecimal couponQuota =
		 * ccTemp.getCouponQuota(); // 更新时间 int updateTime =
		 * ccTemp.getUserUpdateTime(); // 排他校验 if (updateTime != couponOldTime
		 * || ccTemp.getUsedFlag() != 0) { _log.info(
		 * "此优惠券已被使用。。。。。。券编号："+couponGrantId); // 优惠券已被使用 return false; } //
		 * 生成订单id String tenderNid =
		 * GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)); // 出借金额
		 * BigDecimal accountDecimal = null; BigDecimal borrowApr = null; if
		 * (couponType == 1) { // 体验金 出借资金=体验金面值 accountDecimal = couponQuota;
		 * borrowApr = borrow.getBorrowApr(); } else if (couponType == 2) { //
		 * 加息券 出借资金=真实出借资金 accountDecimal = new BigDecimal(account); borrowApr =
		 * couponQuota; } else if (couponType == 3) { // 代金券 出借资金=体验金面值
		 * accountDecimal = couponQuota; borrowApr = borrow.getBorrowApr(); } //
		 * 优惠券出借额度 retMap.put("couponAccount", accountDecimal); // 优惠券面值
		 * retMap.put("couponQuota", couponQuota); // 优惠券类别
		 * retMap.put("couponType", couponType); // 出借表数据插入 BorrowTenderCpn
		 * borrowTenderCpn = new BorrowTenderCpn();
		 * borrowTenderCpn.setAccount(accountDecimal);
		 * borrowTenderCpn.setAccountTender(new BigDecimal(0));
		 * borrowTenderCpn.setActivityFlag(0);// borrowTenderCpn.setAddip(ip);
		 * borrowTenderCpn.setAddtime(Integer.valueOf((new Date().getTime() /
		 * 1000) + "")); borrowTenderCpn.setApiStatus(0);//
		 * borrowTenderCpn.setAutoStatus(0);//
		 * borrowTenderCpn.setBorrowNid(borrowNid);
		 * borrowTenderCpn.setChangePeriod(0);//
		 * borrowTenderCpn.setChangeUserid(0); borrowTenderCpn.setClient(0);
		 * borrowTenderCpn.setContents("");// borrowTenderCpn.setFlag(0);//
		 * borrowTenderCpn.setIsok(0); borrowTenderCpn.setIsReport(0);
		 * borrowTenderCpn.setChangeStatus(0); borrowTenderCpn.setLoanAmount(new
		 * BigDecimal("0.00")); borrowTenderCpn.setNid(tenderNid);
		 * borrowTenderCpn.setOrderDate(ordDate);
		 * borrowTenderCpn.setPeriodStatus(0);// // 预期本息收益
		 * borrowTenderCpn.setRecoverAccountAll(new BigDecimal(0)); // 预期利息
		 * borrowTenderCpn.setRecoverAccountInterest(new BigDecimal(0)); // 已收本息
		 * borrowTenderCpn.setRecoverAccountYes(new BigDecimal(0)); // 已收本金
		 * borrowTenderCpn.setRecoverAccountCapitalYes(new BigDecimal(0)); //
		 * 已收利息 borrowTenderCpn.setRecoverAccountInterestYes(new BigDecimal(0));
		 * BigDecimal recoverAccountWait = BigDecimal.ZERO; BigDecimal
		 * recoverAccountCapitalWait = BigDecimal.ZERO; BigDecimal
		 * recoverAccountInterestWait = BigDecimal.ZERO; BigDecimal
		 * couponInterest =
		 * this.calculateCouponInterest(borrow.getBorrowStyle(), accountDecimal,
		 * borrowApr, borrow.getBorrowPeriod()); if (couponType == 1) { // 体验金
		 * 出借 // 待收本息 recoverAccountWait = couponInterest; // 待收利息
		 * recoverAccountInterestWait = couponInterest; } else if (couponType ==
		 * 2) { // 加息券 出借 // 待收本息 recoverAccountWait = couponInterest; // 待收利息
		 * recoverAccountInterestWait = couponInterest; } else if (couponType ==
		 * 3) { // 代金券 出借资金=体验金面值 // 待收本息 recoverAccountWait =
		 * couponQuota.add(couponInterest); // 待收本金 recoverAccountCapitalWait =
		 * couponQuota; // 待收利息 recoverAccountInterestWait = couponInterest; }
		 * // 待收本息 borrowTenderCpn.setRecoverAccountWait(recoverAccountWait); //
		 * 待收本金 borrowTenderCpn.setRecoverAccountCapitalWait(
		 * recoverAccountCapitalWait); // 待收利息
		 * borrowTenderCpn.setRecoverAccountInterestWait(
		 * recoverAccountInterestWait); borrowTenderCpn.setRecoverAdvanceFee(new
		 * BigDecimal(0)); borrowTenderCpn.setRecoverFee(new BigDecimal(0));
		 * borrowTenderCpn.setRecoverFullStatus(0);
		 * borrowTenderCpn.setRecoverLateFee(new BigDecimal(0));
		 * borrowTenderCpn.setRecoverTimes(0);
		 * borrowTenderCpn.setRecoverType(""); borrowTenderCpn.setStatus(0);
		 * borrowTenderCpn.setTenderAwardAccount(new BigDecimal(0));
		 * borrowTenderCpn.setTenderAwardFee(new BigDecimal(0));
		 * borrowTenderCpn.setTenderNid(borrowNid);
		 * borrowTenderCpn.setTenderStatus(0);
		 * borrowTenderCpn.setUserId(userId); borrowTenderCpn.setRemark("");
		 * borrowTenderCpn.setWeb(0); borrowTenderCpn.setWebStatus(0);
		 * borrowTenderCpn.setClient(client); // 单笔出借的融资服务费
		 * borrowTenderCpn.setLoanFee(new BigDecimal("0.00")); String remark =
		 * StringUtils.EMPTY; if (couponType == 1) { remark = "体验金<br />编号：" +
		 * ccTemp.getCouponUserCode(); } else if (couponType == 2) { remark =
		 * "加息券<br />编号：" + ccTemp.getCouponUserCode(); } else if (couponType ==
		 * 3) { remark = "代金券<br />编号：" + ccTemp.getCouponUserCode(); }
		 * borrowTenderCpn.setRemark(remark);
		 * LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName,
		 * "*******borrowTenderCpn表，插入数据！"); boolean tenderCpnFlag =
		 * borrowTenderCpnMapper.insertSelective(borrowTenderCpn) > 0 ? true :
		 * false; if (tenderCpnFlag) { _log.info(
		 * "*******borrowTenderCpn表，插入数据！券编号："+couponGrantId); // 优惠券出借表
		 * CouponTender ct = new CouponTender(); // 优惠券用户编号
		 * ct.setCouponGrantId(Integer.valueOf(couponGrantId)); // 优惠券出借编号
		 * ct.setOrderId(tenderNid); ct.setAddTime(nowTime);
		 * ct.setAddUser(userId.toString()); ct.setUpdateTime(nowTime);
		 * ct.setUpdateUser(userId.toString()); ct.setDelFlg(0);
		 * LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName,
		 * "*******CouponTender表，插入数据！"); boolean couponTenderFlag =
		 * this.couponTenderMapper.insertSelective(ct) > 0 ? true : false; if
		 * (couponTenderFlag) { _log.info(
		 * "*******CouponTender表，插入数据！券编号："+couponGrantId); // 优惠券出借与真实出借关联表
		 * CouponRealTender crt = new CouponRealTender(); // 优惠券出借编号
		 * crt.setCouponTenderId(tenderNid); // 主单出借编号
		 * crt.setRealTenderId(mainTenderNid); crt.setAddTime(nowTime);
		 * crt.setAddUser(userId.toString()); crt.setUpdateTime(nowTime);
		 * crt.setUpdateUser(userId.toString()); crt.setDelFlg(0);
		 * LogUtil.infoLog(InvestServiceImpl.class.toString(),
		 * methodName,"*******CouponRealTender表，插入数据！"); boolean
		 * couponRealTenderFlag =
		 * this.couponRealTenderMapper.insertSelective(crt) > 0 ? true : false;
		 * if (couponRealTenderFlag) { _log.info(
		 * "*******CouponRealTender表，插入数据！券编号："+couponGrantId); // 优惠券用户表状态
		 * CouponUser cu = new CouponUser();
		 * cu.setId(Integer.valueOf(couponGrantId)); cu.setUpdateTime(nowTime);
		 * cu.setUpdateUser(userId.toString()); // 状态更新为 1:已使用
		 * cu.setUsedFlag(1);
		 * LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName,
		 * "*******CouponUser表，更新使用状态！"); boolean couponUserFlag =
		 * this.couponUserMapper.updateByPrimaryKeySelective(cu) > 0 ? true:
		 * false; if (couponUserFlag) { _log.info(
		 * "*******CouponUser表，更新使用状态！券编号："+couponGrantId); return true; } else
		 * { throw new RuntimeException("couponUser表更新失败"); } } else { throw new
		 * RuntimeException("couponRealTender表插入失败"); } } else { throw new
		 * RuntimeException("couponTender表插入失败"); } } else { throw new
		 * RuntimeException("borrowTenderCpn表插入失败"); } } else {
		 * _log.info( "此优惠券不存在，优惠券编号："+couponGrantId); throw new
		 * RuntimeException("此优惠券不存在，优惠券编号："+couponGrantId); }
		 */
	}

	/**
	 * 取得用户优惠券信息
	 * 
	 * @param couponGrantId
	 * @return
	 */
	public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		return ccTemp;
	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */

	@Override
	public AccountChinapnr getAccountChinapnr(Integer userId) {
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
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param account
	 * @param tenderUsrcustid
	 * @param borrowerUsrcustid
	 * @param OrdId
	 * @return
	 * @author b
	 */

	@Override
	public FreezeDefine freeze(Integer userId, String account, String tenderUsrcustid, String borrowerUsrcustid, String OrdId) {
		FreezeDefine freezeDefine = new FreezeDefine();
		Properties properties = PropUtils.getSystemResourcesProperties(); // 商户后台应答地址(必须)
		String bgRetUrl = properties.getProperty("hyjf.chinapnr.callbackurl").trim();
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setVersion("10");// 接口版本号
		chinapnrBean.setCmdId("UsrFreezeBg"); // 消息类型(冻结)
		chinapnrBean.setUsrCustId(tenderUsrcustid);// 出借用户客户号
		chinapnrBean.setOrdId(OrdId); // 订单号(必须)
		chinapnrBean.setOrdDate(GetDate.getServerDateTime(1, new Date()));// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));// 交易金额(必须)
		chinapnrBean.setRetUrl(""); // 页面返回
		chinapnrBean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		chinapnrBean.setType("user_freeze"); // 日志类型
		chinapnrBean.setLogUserId(userId);
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (bean != null) {
			String respCode = bean.getRespCode();
			freezeDefine.setFreezeTrxId(bean.getTrxId());
			_log.info("用户:" + userId + "***********************************冻结订单号：" + bean.getTrxId());
			if (StringUtils.isNotEmpty(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
				freezeDefine.setFreezeFlag(true);
			} else {
				_log.info("用户:" + userId + "***********************************冻结失败错误码：" + respCode);
				freezeDefine.setFreezeFlag(false);
			}
			return freezeDefine;
		} else {
			return null;
		}

	}

	/**
	 * 更新标
	 *
	 * @param record
	 * @return
	 * @author b
	 */

	@Override
	public boolean updateBorrow(Borrow record) {
		borrowMapper.updateByPrimaryKey(record);
		return true;

	}

	/**
	 * 新用户新手标验证，标如果是新用户标，查看此用户是否有过出借记录，如果有返回true，提示不能投标了
	 *
	 * @param userId
	 * @param projectType
	 * @return
	 */

	@Override
	public boolean checkIsNewUserCanInvest(Integer userId, Integer projectType) {

		// 新的判断是否为新用户方法
		int total = webUserInvestListCustomizeMapper.countNewUserTotal(userId + "");
		if (total == 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断是否51老用户,如果是则返回true，否则返回false
	 *
	 * @param userId
	 * @param projectType
	 * @return
	 * @author b
	 */

	@Override
	public boolean checkIs51UserCanInvest(Integer userId) {
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		UsersInfoExample.Criteria borrowCriteria = usersInfoExample.createCriteria();
		borrowCriteria.andUserIdEqualTo(userId);
		List<UsersInfo> list = usersInfoMapper.selectByExample(usersInfoExample);
		if (list != null && !list.isEmpty()) {
			UsersInfo usersInfo = list.get(0);
			if (usersInfo != null) {
				Integer is51 = usersInfo.getIs51();// 1是51，0不是
				if (is51 != null && is51 == 1) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param userId
	 * @return
	 * @author b
	 */

	@Override
	public UsersInfo getUserInfo(Integer userId) {
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
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param userId
	 * @return
	 * @author b
	 */

	@Override
	public Users getUsers(Integer userId) {
		if (userId == null) {
			return null;
		}
		// 查找用户
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria criteria2 = usersExample.createCriteria();
		criteria2.andUserIdEqualTo(userId);
		List<Users> userList = usersMapper.selectByExample(usersExample);
		Users users = null;
		if (userList != null && !userList.isEmpty()) {
			users = userList.get(0);

		}
		return users;

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param projectType
	 * @return
	 * @author b
	 */

	@Override
	public BorrowProjectType getBorrowProjectType(String projectType) {
		if (StringUtils.isEmpty(projectType)) {
			return null;
		}
		// 查找用户
		BorrowProjectTypeExample borrowProjectTypeExample = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria criteria2 = borrowProjectTypeExample.createCriteria();
		criteria2.andBorrowCdEqualTo(projectType);
		List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(borrowProjectTypeExample);
		BorrowProjectType borrowProjectType = null;
		if (list != null && !list.isEmpty()) {
			borrowProjectType = list.get(0);

		}
		return borrowProjectType;
	}

	// 异常出现恢复redis值
	public boolean recoverRedis(String borrowNid, Integer userId, String account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
		String balanceLast = RedisUtils.get(borrowNid);
		if (StringUtils.isNotEmpty(balanceLast)) {
			while ("OK".equals(jedis.watch(borrowNid))) {
				balanceLast = RedisUtils.get(borrowNid);
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction tx = jedis.multi();
				tx.set(borrowNid, recoverAccount + "");
				List<Object> result = tx.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					String ret = (String) result.get(0);
					if (ret != null && ret.equals("OK")) {
						_log.info("用户:" + userId + "***********************************from redis恢复redis：" + account);
						break;
					} else {
						jedis.unwatch();
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param usrCustId
	 * @return
	 */

	@Override
	public Integer getUserIdByUsrcustId(String usrCustId) {
		if (StringUtils.isEmpty(usrCustId)) {
			return null;
		}
		// 查找用户userId
		AccountChinapnrExample borrowProjectTypeExample = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria2 = borrowProjectTypeExample.createCriteria();
		criteria2.andChinapnrUsrcustidEqualTo(Long.valueOf(usrCustId));
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(borrowProjectTypeExample);
		Integer userId = null;
		if (list != null && !list.isEmpty()) {
			userId = list.get(0).getUserId();

		}
		return userId;

	}

	@Override
	public boolean unFreezeOrder(int borrowUserId, int investUserId, String orderId, String trxId, String ordDate) throws Exception {

		// 借款人在汇付的账户信息
		AccountChinapnr inCust = this.getAccountChinapnr(borrowUserId);
		if (inCust == null) {
			throw new Exception("借款人未开户。[借款人ID：" + borrowUserId + "]，" + "[出借订单号：" + orderId + "]");
		}
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
					ChinapnrBean unFreezeBean = usrUnFreeze(trxId);
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
	 */
	private ChinapnrBean usrUnFreeze(String trxId) {
		String methodName = "usrUnFreeze";

		// 调用汇付接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_USR_UN_FREEZE); // 消息类型(必须)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(0)); // 订单号(必须)
		bean.setOrdDate(GetOrderIdUtils.getOrderDate()); // 订单日期(必须)
		bean.setTrxId(trxId); // 本平台交易唯一标识(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("资金（货款）解冻"); // 备注
		bean.setLogClient("0"); // PC

		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);

		if (chinapnrBean == null) {
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用解冻接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}

		return chinapnrBean;
	}

	/**
	 * 检查参数的正确性
	 * 
	 * @param userId
	 * @param transAmt
	 *            交易金额
	 * @param flag
	 *            交易类型，1购买 2赎回
	 * @return
	 */
	public JSONObject checkParam(String borrowNid, String account, String userId, String platform, CouponConfigCustomizeV2 cuc) {
		if (account == null || "".equals(account)) {
			account = "0";
		}
		_log.info("=============cwyang APP开始检查参数==================");
		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId) || !DigitalUtils.isInteger(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		}
		Users user = this.getUsers(Integer.parseInt(userId));
		// 判断用户信息是否存在
		if (user == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
		List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
		if (null != usersInfos && usersInfos.size() == 1) {
			String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
			if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
				if (usersInfos.get(0).getRoleId() !=1) {// 担保机构用户
					return jsonMessage("仅限出借人进行出借", "1");
				}
			}

		} else {
			return jsonMessage("账户信息异常", "1");
		}
		// 判断用户是否禁用
		if (user.getStatus() == 1) {// 0启用，1禁用
			return jsonMessage("该用户已被禁用", "1");
		}
		// 判断用户是否设置了交易密码
		if (user.getIsSetPassword() == 0) {
			return jsonMessage("该用户未设置交易密码", "1");
		}
		// 缴费授权状态 
		if (!authService.checkPaymentAuthStatus(Integer.parseInt(userId))) {
			return jsonMessage("该用户未服务费授权", "1");
		}
		// 判断借款编号是否存在
		if (StringUtils.isEmpty(borrowNid)) {
			return jsonMessage("借款项目不存在", "1");
		}
		Borrow borrow = this.getBorrowByNid(borrowNid);
		// 判断借款信息是否存在
		if (borrow == null || borrow.getId() == null) {
			return jsonMessage("借款项目不存在", "1");
		}
		if (borrow.getUserId() == null) {
			return jsonMessage("借款人不存在", "1");
		}
		Integer projectType = borrow.getProjectType();// 0，51老用户；1，新用户；2，全部用户
		if (projectType == null) {
			return jsonMessage("未设置该出借项目的项目类型", "1");
		}
		BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(projectType));
		if (borrowProjectType == null) {
			return jsonMessage("未查询到该出借项目的设置信息", "1");
		}
		// 51老用户标
		if (borrowProjectType.getInvestUserType().equals("0")) {
			boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
			if (!is51User) {
				return jsonMessage("该项目只能51老用户出借", "1");
			}
		}
		if (borrowProjectType.getInvestUserType().equals("1")) {
			boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId), projectType);
			if (!newUser) {
				return jsonMessage("该项目只能新手出借", "1");
			}
		}
		BankOpenAccount accountChinapnrCrediter = appTenderCreditService.getBankOpenAccount(Integer.parseInt(userId));
		// 用户未在平台开户
		if (accountChinapnrCrediter == null) {
			_log.info("=============cwyang 用户开户信息不存在===userid is====" + userId);
			return jsonMessage("用户开户信息不存在", "1");
		}
		// 判断借款人开户信息是否存在
		if (accountChinapnrCrediter.getAccount() == null) {
			return jsonMessage("用户银行账户号不存在", "1");
		}
		
		
		// 出借客户端
		if (platform.equals("2") && borrow.getCanTransactionAndroid().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " PC端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		} else if (platform.equals("3") && borrow.getCanTransactionIos().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " PC端  ";
			}
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		}
		// 借款人开户信息
		BankOpenAccount accountChinapnrBorrower = appTenderCreditService.getBankOpenAccount(Integer.parseInt(userId));
		if (accountChinapnrBorrower == null) {
			return jsonMessage("借款人未开户", "1");
		}
		if (accountChinapnrBorrower.getAccount() == null) {
			return jsonMessage("借款人银行账户号不存在", "1");
		}
		if (userId.equals(String.valueOf(borrow.getUserId()))) {
			return jsonMessage("借款人不可以自己出借项目", "1");
		}
		// 判断借款是否流标
		if (borrow.getStatus() == 6) { // 流标
			return jsonMessage("此项目已经流标", "1");
		}
		// 已满标
		if (borrow.getBorrowFullStatus() == 1) {
			return jsonMessage("此项目已经满标", "1");
		}
		// 判断用户出借金额是否为空
		if (!(StringUtils.isNotEmpty(account) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 3))) {
			return jsonMessage("请输入出借金额", "1");
		}
		// 还款金额是否数值
		if (!DigitalUtils.isNumber(account)) {
			return jsonMessage("出借金额格式错误", "1");
		}
		if (/*!(!"0".equals(account) || ("0".equals(account) && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)))*/
                ("0".equals(account) && cuc == null)
                        || ("0".equals(account) && cuc != null && cuc.getCouponType() == 2)) {
			return jsonMessage("出借金额不能为0元", "1");
		}
		try {
			// 出借金额必须是整数
			int accountInt = Integer.parseInt(account);
			if (accountInt != 0 && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
				return jsonMessage("该优惠券只能单独使用", "1");
			}
			if (accountInt < 0) {
				return jsonMessage("出借金额不能为负数", "1");
			}
			// 将出借金额转化为BigDecimal
			BigDecimal accountBigDecimal = new BigDecimal(account);
			String balance = RedisUtils.get(borrowNid);
			if (StringUtils.isEmpty(balance)) {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			}
			// 起投金额
			Integer min = borrow.getTenderAccountMin();
			// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
			if (min != null && min != 0 && new BigDecimal(balance).compareTo(new BigDecimal(min)) == -1) {
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
					//return jsonMessage("项目太抢手了！剩余可投金额只有" + balance + "元", "1");
					return jsonMessage("出借金额不能大于项目剩余", "1");
				}
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
					return jsonMessage("剩余可投只剩" + balance + "元，须全部购买", "1");
				}
			} else {// 项目的剩余金额大于最低起投金额
				if (accountBigDecimal.compareTo(new BigDecimal(min)) == -1) {
					if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
						if (cuc != null && cuc.getCouponType() != 3 && cuc.getCouponType() != 1) {
							return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
						}
					} else {
						return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
					}
				} else {
					Integer max = borrow.getTenderAccountMax();
					if (max != null && max != 0 && accountBigDecimal.compareTo(new BigDecimal(max)) == 1) {
						return jsonMessage("项目最大出借额为" + max + "元", "1");
					}
				}
			}
			if (accountBigDecimal.compareTo(borrow.getAccount()) > 0) {
				return jsonMessage("出借金额不能大于项目总额", "1");
			}
			// 用户账户信息
			Account tenderAccount = this.getAccount(Integer.parseInt(userId));
			if (tenderAccount.getBankBalance().compareTo(accountBigDecimal) < 0) {
				//return jsonMessage("余额不足，请充值！", "1");
				return jsonMessage("可用金额不足", "1");
			}
			// redis剩余金额不足
			if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
				//return jsonMessage("剩余可投金额为" + balance + "元", "1");
				//return jsonMessage("出借金额不能超过" + balance + "元", "1");
				return jsonMessage("出借金额不能大于项目剩余", "1");
			} else {
				AppProjectDetailCustomize borrowDetail = appProjectListCustomizeMapper.selectProjectDetail(borrowNid);
				// add by cwyang 在只使用代金券和体验金,并且没有本金的情况下,不进行出借递增金额的判断,在出借金额等于最大可投金额时也不做递增金额的判断
				if (!(cuc != null && (cuc.getCouponType() == 3||cuc.getCouponType() == 1) && accountInt == 0)) {
					if (borrowDetail.getIncreaseMoney() != null && (accountInt - min) % Integer.parseInt(borrowDetail.getIncreaseMoney()) != 0
							&& accountBigDecimal.compareTo(new BigDecimal(balance)) == -1 && accountInt < borrow.getTenderAccountMax()) {
						return jsonMessage("出借递增金额须为" + borrowDetail.getIncreaseMoney() + " 元的整数倍", "1");
					}
				}
				
				// 如果验证没问题，则返回出借人借款人的银行账号
				Long borrowerUsrcustid = Long.parseLong(accountChinapnrBorrower.getAccount());
				Long tenderUsrcustid = Long.parseLong(accountChinapnrCrediter.getAccount());
				JSONObject jsonMessage = new JSONObject();
				jsonMessage.put(CustomConstants.APP_STATUS, "0");
				jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
				jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
				jsonMessage.put("borrowId", borrow.getId());
				jsonMessage.put("bankInputFlag", borrow.getBankInputFlag() + "");
				jsonMessage.put("tenderUserName", user.getUsername());
				_log.info("返回信息为:" + jsonMessage);
				return jsonMessage;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return jsonMessage("出借金额必须为整数", "1");
		}
	}

	/**
	 * 检查参数的正确性(优惠券出借，无本金出借)
	 * 
	 * @param userId
	 * @param transAmt
	 *            交易金额
	 * @param flag
	 *            交易类型，1购买 2赎回
	 * @return
	 */
	@Override
	public JSONObject checkParamForCoupon(Borrow borrow, AppTenderVo vo, String userId, CouponConfigCustomizeV2 cuc, String couponGrantId) {

		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId) || !DigitalUtils.isInteger(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		}
		Users user = this.getUsers(Integer.parseInt(userId));
		// 判断用户信息是否存在
		if (user == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		// 判断用户是否禁用
		if (user.getStatus() == 1) {// 0启用，1禁用
			return jsonMessage("该用户已被禁用", "1");
		}
		Integer projectType = borrow.getProjectType();// 0，51老用户；1，新用户；2，全部用户
		if (projectType == null) {
			return jsonMessage("未设置该出借项目的项目类型", "1");
		}
		BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(projectType));
		if (borrowProjectType == null) {
			return jsonMessage("未查询到该出借项目的设置信息", "1");
		}
		// 51老用户标
		if (borrowProjectType.getInvestUserType().equals("0")) {
			boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
			if (!is51User) {
				return jsonMessage("该项目只能51老用户出借", "1");
			}
		}
		if (borrowProjectType.getInvestUserType().equals("1")) {
			boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId), projectType);
			if (!newUser) {
				return jsonMessage("该项目只能新手出借", "1");
			}
		}

		if (/*"0".equals(vo.getAccount()) && cuc.getCouponType() != 3 && cuc.getCouponType() != 1*/
                ("0".equals(vo.getAccount()) && cuc == null)
                        || ("0".equals(vo.getAccount()) && cuc != null && cuc.getCouponType() == 2)) {
			return jsonMessage("出借金额不能为0元", "1");
		}

		if (!"0".equals(vo.getAccount()) && StringUtils.isNotEmpty(vo.getAccount()) && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
			return jsonMessage("该优惠券只能单独使用", "1");
		}
		// 用户开户信息
		_log.info("==============cwyang 校验用户开户信息=======userID " + userId);
		BankOpenAccount accountChinapnrTender = appTenderCreditService.getBankOpenAccount(Integer.parseInt(userId));
//		AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.parseInt(userId));
		_log.info("================cwyang 判断开户信息 userID is " + userId);
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			_log.info("===============cwyang 开户信息不存在!");
			return jsonMessage("用户开户信息不存在", "1");
		}
		// 判断借款人开户信息是否存在
		if (accountChinapnrTender.getAccount() == null) {
			return jsonMessage("用户银行账户号不存在", "1");
		}
		// 判断借款信息是否存在
		if (borrow == null || borrow.getId() == null) {
			return jsonMessage("借款项目不存在", "1");
		}
		if (borrow.getUserId() == null) {
			return jsonMessage("借款人不存在", "1");
		}
		// 借款人开户信息
		BankOpenAccount accountChinapnrBorrower  = appTenderCreditService.getBankOpenAccount(borrow.getUserId());
		if (accountChinapnrBorrower == null) {
			return jsonMessage("借款人未开户", "1");
		}
		if (accountChinapnrBorrower.getAccount() == null) {
			return jsonMessage("借款人银行帐户号不存在", "1");
		}
		if (userId.equals(String.valueOf(borrow.getUserId()))) {
			return jsonMessage("借款人不可以自己出借项目", "1");
		}
		// 判断借款是否流标
		if (borrow.getStatus() == 6) { // 流标
			return jsonMessage("此项目已经流标", "1");
		}
		// 已满标
		if (borrow.getBorrowFullStatus() == 1) {
			return jsonMessage("此项目已经满标", "1");
		}
		// 有优惠校验
		JSONObject jsonError = this.validateCoupon(vo.getAccount(), borrow, vo.getPlatform(), couponGrantId, userId);
		if (jsonError != null) {
			return jsonError;
		} else {
			// 如果验证没问题，则返回出借人借款人的汇付账号
			Long borrowerUsrcustid = Long.parseLong(accountChinapnrBorrower.getAccount());
			Long tenderUsrcustid = Long.parseLong(accountChinapnrTender.getAccount());
			JSONObject jsonMessage = new JSONObject();
			jsonMessage.put(CustomConstants.APP_STATUS, "0");
			jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
			jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
			jsonMessage.put("borrowId", borrow.getId());
			_log.info("=================cwyang 返回信息为:　" + jsonMessage);
			return jsonMessage;
		}
	}

	/**
	 * 
	 * @param account
	 * @param couponConfig
	 * @param borrow
	 * @param platform
	 * @param cuc
	 * @return
	 */
	private JSONObject validateCoupon(String account, Borrow borrow, String platform, String couponGrantId, String userId) {

		JSONObject jsonObject = CommonSoaUtils.CheckCoupon(userId, borrow.getBorrowNid(), account, platform, couponGrantId);
		int status = jsonObject.getIntValue("status");
		String statusDesc = jsonObject.getString("statusDesc");
		if (status == 1) {
			return jsonMessage(statusDesc, status + "");
		}
		return null;
	}

	/**
	 * 组成返回信息
	 * 
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String data, String error) {
		JSONObject jo = null;
		if (Validator.isNotNull(data)) {
			jo = new JSONObject();
			jo.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			jo.put(CustomConstants.APP_STATUS_DESC, data);
		}
		return jo;
	}

	/**
	 * 更改 新手同时出借状态位
	 */
	@Override
	public boolean updateUserInvestFlagById(Integer userId) {

		Integer newFlag = webUserInvestListCustomizeMapper.selectUserInvestFlag(userId);
		if (newFlag != null && newFlag == 0) {
			Integer investFlag = webUserInvestListCustomizeMapper.updateUserInvestFlag(userId);
			if (investFlag > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		// 查询项目信息
		Borrow borrow = this.getBorrowByNid(borrowNid);
		BorrowProjectType borrowProjectType = getProjectTypeByBorrowNid(borrowNid);
		String style = borrow.getBorrowStyle();
		// 加息券是否启用 0禁用 1启用
		Integer couponFlg = borrow.getBorrowInterestCoupon();
		// 体验金是否启用 0禁用 1启用
		Integer moneyFlg = borrow.getBorrowTasteMoney();
		/*
		 * int couponType = 0; if(couponFlg==1&&moneyFlg!=1){ couponType = 2;
		 * }else if(couponFlg!=1&&moneyFlg==1){ couponType = 1; }
		 * map.put("couponType", couponType);
		 */
		List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
		Collections.sort(couponConfigs, new ComparatorCouponBean());
		// List<UserCouponConfigCustomize> availableCouponConfigs=new
		// ArrayList<UserCouponConfigCustomize>();
		for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {

			// 验证项目加息券或体验金是否可用
			if (couponFlg != null && couponFlg == 0) {
				if (userCouponConfigCustomize.getCouponType() == 2) {
					continue;
				}
			}
			if (moneyFlg != null && moneyFlg == 0) {
				if (userCouponConfigCustomize.getCouponType() == 1) {
					continue;
				}
			}
			// 验证项目期限、
			Integer type = userCouponConfigCustomize.getProjectExpirationType();
			if ("endday".equals(style)) {
				if (type == 1) {
					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 3) {
					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 4) {
					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 2) {
					if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod()
							|| (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow.getBorrowPeriod()) {
						continue;
					}
				}
			} else {
				if (type == 1) {
					if (userCouponConfigCustomize.getProjectExpirationLength() != borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 3) {
					if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 4) {
					if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
						continue;
					}
				} else if (type == 2) {
					if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod() || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow.getBorrowPeriod()) {
						continue;
					}
				}
			}

			// 验证项目金额
			Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();
			if (tenderQuota == 1) {
				if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money) || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
					continue;
				}
			} else if (tenderQuota == 2) {
				if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
					continue;
				}
			}
			// 验证优惠券适用的项目
			String projectType = userCouponConfigCustomize.getProjectType();
			boolean ifprojectType = true;
			if (projectType.indexOf("-1") != -1) {
				if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
					ifprojectType = false;
				}
			} else {
				if ("HXF".equals(borrowProjectType.getBorrowClass())) {
					if (projectType.indexOf("2") != -1) {
						ifprojectType = false;
					}
				} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
					if (projectType.indexOf("3") != -1) {
						ifprojectType = false;
					}
				} else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
					if (projectType.indexOf("4") != -1) {
						ifprojectType = false;
					}
				} else {
					if (projectType.indexOf("1") != -1) {
						if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
							ifprojectType = false;
						}
					}
				}
			}
			if (ifprojectType) {
				continue;
			}

			// 验证使用平台
			String couponSystem = userCouponConfigCustomize.getCouponSystem();
			String[] couponSystemArr = couponSystem.split(",");
			for (String couponSystemString : couponSystemArr) {
				if ("-1".equals(couponSystemString)) {
					return userCouponConfigCustomize;
				}
				if (platform.equals(couponSystemString)) {
					return userCouponConfigCustomize;
				}
			}
		}
		return null;
	}

	@Override
	public UserCouponConfigCustomize getCouponById(String couponId) {
		UserCouponConfigCustomize couponConfig = couponConfigCustomizeMapper.getBestCouponById(couponId);
		return couponConfig;
	}

	/**
	 * 获取项目类型
	 * 
	 * @param borrowNid
	 * @return
	 */
	public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL).andBorrowClassEqualTo(borrowNid.substring(0, 3));
		List<BorrowProjectType> borrowProjectTypes = this.borrowProjectTypeMapper.selectByExample(example);
		BorrowProjectType borrowProjectType = new BorrowProjectType();
		if (borrowProjectTypes != null && borrowProjectTypes.size() > 0) {
			borrowProjectType = borrowProjectTypes.get(0);
		}
		return borrowProjectType;
	}

	/**
	 * 获取项目类型
	 * 
	 * @return
	 */
	public List<BorrowProjectType> getProjectTypeList() {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		return this.borrowProjectTypeMapper.selectByExample(example);
	}

	// pcc20160715
	@Override
	public String validateCouponProjectType(String projectTypeList, String projectTypeCd) {
		boolean ifprojectType = true;
		BorrowProjectType borrowProjectType = new BorrowProjectType();
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		example.createCriteria().andBorrowCdEqualTo(projectTypeCd);
		List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(example);
		borrowProjectType = list.get(0);
		if (projectTypeList.indexOf("-1") != -1) {
			if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
				ifprojectType = false;
			}
		} else {

			if ("HXF".equals(borrowProjectType.getBorrowClass())) {
				if (projectTypeList.indexOf("2") != -1) {
					ifprojectType = false;
				}
			} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
				if (projectTypeList.indexOf("3") != -1) {
					ifprojectType = false;
				}
			} else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
				if (projectTypeList.indexOf("4") != -1) {
					ifprojectType = false;
				}
			} else {
				if (projectTypeList.indexOf("1") != -1) {
					if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
						ifprojectType = false;
					}
				}
			}
		}
		if (!ifprojectType) {
			return null;
		}
		return "对不起，您选择的优惠券不能用于当前类别标的";
	}

	/**
	 * 
	 * 获取出借记录
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public int countBorrowTenderNum(Integer userId, String borrowNid, String nid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andNidEqualTo(nid);
		crt.andBorrowNidEqualTo(borrowNid);
		return borrowTenderMapper.countByExample(example);
	}

	/**
	 * 
	 * 更新出借临时表
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param ordId
	 * @return
	 * @author Administrator
	 */
	@Override
	public int updateBorrowTenderTmp(Integer userId, String borrowNid, String ordId) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		BorrowTenderTmpExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andNidEqualTo(ordId);
		crt.andBorrowNidEqualTo(borrowNid);
		BorrowTenderTmp tenderTmp = new BorrowTenderTmp();
		tenderTmp.setStatus(1);
		return this.borrowTenderTmpMapper.updateByExampleSelective(tenderTmp, example);
	}

	/**
	 * 
	 * 查询优惠券出借数据
	 * 
	 * @param couponGrantId
	 * @param borrow
	 * @param userId
	 * @param account
	 * @param couponOldTime
	 * @return
	 * @author Administrator
	 */
	@Override
	public Map<String, Object> queryCouponData(String couponGrantId, Borrow borrow, Integer userId, String account, int couponOldTime) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		// 出借金额
		BigDecimal accountDecimal = null;
		if (Validator.isNotNull(ccTemp)) {
			// 优惠券类别
			int couponType = ccTemp.getCouponType();
			// 面值
			BigDecimal couponQuota = ccTemp.getCouponQuota();
			// 年华收益率
			BigDecimal couponRate = null;
			if (couponType == 1) {
				// 体验金 出借资金=体验金面值
				accountDecimal = couponQuota;
				couponRate = borrow.getBorrowApr();
			} else if (couponType == 2) {
				// 加息券 出借资金=真实出借资金
				accountDecimal = new BigDecimal(account);
				couponRate = borrow.getBorrowApr();
			} else if (couponType == 3) {
				// 代金券 出借资金=体验金面值
				accountDecimal = couponQuota;
				couponRate = borrow.getBorrowApr();
			}
			// 优惠券出借额度
			retMap.put("couponAccount", accountDecimal);
			// 优惠券面值
			retMap.put("couponQuota", couponQuota);
			// 优惠券类别
			retMap.put("couponType", couponType);
			// 年华收益率
			retMap.put("couponRate", couponRate);
			return retMap;
		} else {
			return null;
		}
	}

	/**
	 * 更新优惠券出借状态
	 * 
	 * @param orderId
	 * @param couponGrantId
	 * @param userId
	 * @return
	 * @author Administrator
	 */
	@Override
	public boolean updateCouponTenderStatus(String orderId, String couponGrantId, Integer userId) {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int nowTime = GetDate.getNowTime10();
		// 优惠券用户表状态
		CouponUser cu = new CouponUser();
		cu.setId(Integer.valueOf(couponGrantId));
		cu.setUpdateTime(nowTime);
		boolean flag = this.couponUserMapper.updateByPrimaryKeySelective(cu) > 0 ? true : false;
		if (flag) {
			CouponRealTenderExample realExample = new CouponRealTenderExample();
			CouponRealTenderExample.Criteria realCrt = realExample.createCriteria();
			realCrt.andRealTenderIdEqualTo(orderId);
			int count = this.couponRealTenderMapper.countByExample(realExample);
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public boolean checkIfSendCoupon(Users user, String account) {
		// 预留活动id
		int activityId = Integer.parseInt(CustomConstants.INVEST_ACTIVITY_ID);
		ActivityList activityList = activityListMapper.selectByPrimaryKey(activityId);
		if (activityList == null) {
			return false;
		}
		Integer timeStart = activityList.getTimeStart();
		Integer timeEnd = activityList.getTimeEnd();
		if (timeStart > GetDate.getNowTime10()) {
			return false;
		}
		if (timeEnd != null && timeEnd < GetDate.getNowTime10()) {
			return false;
		}
		BigDecimal accountBigDecimal = BigDecimal.ZERO;
		if (account == null) {
			return false;
		} else {
			accountBigDecimal = new BigDecimal(account);
		}
		if (accountBigDecimal.compareTo(new BigDecimal("1000")) != -1) {
			// 判断推荐人
			SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
			SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
			spreadsUsersExampleCriteria.andUserIdEqualTo(user.getUserId());
			List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
			if (sList != null && sList.size() == 1) {
				int refUserId = sList.get(0).getSpreadsUserid();
				// 判断是否是投之家来的
				if (refUserId != 111734) {
					return false;
				} else {
					CouponUserExample example = new CouponUserExample();
					CouponUserExample.Criteria cra = example.createCriteria();
					cra.andUserIdEqualTo(user.getUserId());
					cra.andActivityIdEqualTo(activityId);
					cra.andDelFlagEqualTo(CustomConstants.FALG_NOR);
					int couponCount = couponUserMapper.countByExample(example);
					if (couponCount > 0) {
						return false;
					} else {
						return true;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void extraUeldInvest(Borrow borrow, ChinapnrBean bean) {
		// 操作ip
		String ip = bean.getLogIp();
		// 操作平台
		int client = StringUtils.isNotBlank(bean.getLogClient()) ? Integer.parseInt(bean.getLogClient()) : 0;
		// 出借人id
		Integer userId = bean.getLogUserId();
		// 借款金额
		String account = bean.getTransAmt();
		// 订单id
		String tenderOrderId = bean.getOrdId();
		// 项目编号
		String borrowNid = borrow.getBorrowNid();
		// 项目的还款方式
		String borrowStyle = borrow.getBorrowStyle();
		BorrowStyle borrowStyleMain = this.getborrowStyleByNid(borrowStyle);
		String borrowStyleName = borrowStyleMain.getName();
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		Users users = this.getUsers(userId);
		// 生成额外利息订单
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
		// 根据orderid查询出借信息tender
		BorrowTender tender = this.getBorrowTenderByNidUserIdBorrowNid(tenderOrderId, userId, borrowNid);
		if (tender != null) {

			IncreaseInterestInvest increaseInterestInvest = new IncreaseInterestInvest();
			increaseInterestInvest.setUserId(userId);
			increaseInterestInvest.setInvestUserName(users.getUsername());
			increaseInterestInvest.setTenderId(tender.getId());
			increaseInterestInvest.setTenderNid(tenderOrderId);
			increaseInterestInvest.setBorrowNid(borrowNid);
			increaseInterestInvest.setBorrowApr(borrow.getBorrowApr());
			increaseInterestInvest.setBorrowExtraYield(borrow.getBorrowExtraYield());
			increaseInterestInvest.setBorrowPeriod(borrowPeriod);
			increaseInterestInvest.setBorrowStyle(borrowStyle);
			increaseInterestInvest.setBorrowStyleName(borrowStyleName);
			increaseInterestInvest.setOrderId(orderId);
			increaseInterestInvest.setOrderDate(GetDate.getServerDateTime(10, new Date()));
			increaseInterestInvest.setAccount(new BigDecimal(account));
			increaseInterestInvest.setStatus(0);
			increaseInterestInvest.setWeb(0);
			increaseInterestInvest.setClient(client);
			increaseInterestInvest.setAddip(ip);
			increaseInterestInvest.setRemark("融通宝加息出借");
			increaseInterestInvest.setInvestType(0);
			increaseInterestInvest.setCreateTime(GetDate.getNowTime10());
			increaseInterestInvest.setCreateUserId(userId);
			increaseInterestInvest.setCreateUserName(users.getUsername());
			boolean incinvflag = increaseInterestInvestMapper.insertSelective(increaseInterestInvest) > 0 ? true : false;
			if (!incinvflag) {
				System.err.println("融通宝出借额外利息出借失败，插入额外出借信息失败,出借订单号:" + tenderOrderId);
			}
		} else {
			System.err.println("融通宝出借额外利息出借失败，获取出借信息失败,出借订单号:" + tenderOrderId);
		}

	}

	private BorrowStyle getborrowStyleByNid(String borrowStyle) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cri = example.createCriteria();
		cri.andNidEqualTo(borrowStyle);
		List<BorrowStyle> style = borrowStyleMapper.selectByExample(example);
		return style.get(0);
	}

	private BorrowTender getBorrowTenderByNidUserIdBorrowNid(String orderId, Integer userId, String borrowNid) { // 删除临时表
		BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria1 = borrowTenderExample.createCriteria();
		criteria1.andNidEqualTo(orderId);
		criteria1.andUserIdEqualTo(userId);
		criteria1.andBorrowNidEqualTo(borrowNid);
		List<BorrowTender> tenderList = borrowTenderMapper.selectByExample(borrowTenderExample);
		if (tenderList != null && tenderList.size() > 0) {
			return tenderList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public BorrowProjectType getProjectType(Integer projectType) {
		BorrowProjectType borrowProjectType = new BorrowProjectType();
		if(null != projectType && !"".equals(projectType)){
			
			BorrowProjectTypeExample borrowProjectTypeExample = new BorrowProjectTypeExample();
			BorrowProjectTypeExample.Criteria criteria1 = borrowProjectTypeExample.createCriteria();
			criteria1.andBorrowCdEqualTo(projectType+"");
			List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(borrowProjectTypeExample);
			if(null != list && list.size() > 0){
				borrowProjectType = list.get(0);
			}
		}
		return borrowProjectType;
	}

	@Override
	public int getCountByBorrowId(String borrowId) {
		return borrowCreditCustomizeMapper.getCountByBorrowId(borrowId);
	}

	@Override
	public BorrowTender getTenderByNid(String logOrderId) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andNidEqualTo(logOrderId);
		List<BorrowTender> list = borrowTenderMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
}

class ComparatorCouponBean implements Comparator<UserCouponConfigCustomize> {

	@Override
	public int compare(UserCouponConfigCustomize couponBean1, UserCouponConfigCustomize couponBean2) {
		if (1 == couponBean1.getCouponType()) {
			couponBean1.setCouponType(4);
		}
		if (1 == couponBean2.getCouponType()) {
			couponBean2.setCouponType(4);
		}
		int flag = couponBean1.getCouponType() - couponBean2.getCouponType();
		if (4 == couponBean1.getCouponType()) {
			couponBean1.setCouponType(1);
		}
		if (4 == couponBean2.getCouponType()) {
			couponBean2.setCouponType(1);
		}
		return flag;
	}
}