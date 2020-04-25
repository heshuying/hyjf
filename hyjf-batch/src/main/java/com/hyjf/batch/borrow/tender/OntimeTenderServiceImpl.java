package com.hyjf.batch.borrow.tender;

import java.io.UnsupportedEncodingException;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.FinancingServiceChargeUtils;
import com.hyjf.common.chinapnr.MerPriv;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountLog;
import com.hyjf.mybatis.model.auto.AppointmentRecodLog;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowAppoint;
import com.hyjf.mybatis.model.auto.BorrowAppointExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpInfo;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class OntimeTenderServiceImpl extends BaseServiceImpl implements OntimeTenderService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	/**
	 * 资金明细（列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<Borrow> queryOntimeTenderList() {
		int onTime = GetDate.getNowTime10();
		List<Borrow> list = this.ontimeTenderCustomizeMapper.queryOntimeTenderList(onTime);
		return list;
	}

	/**
	 * 修改投标信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(BorrowWithBLOBs record) {
		int result = this.borrowMapper.updateByPrimaryKeySelective(record);
		return result;
	}

	/**
	 * 修改投标信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(BorrowWithBLOBs record) {
		int result = this.borrowMapper.updateByPrimaryKeySelective(record);
		return result;
	}

	/**
	 * 
	 * @param tplname
	 * @return
	 */
	public String queryMobiles(String tplname) {
		String mobiles = this.ontimeTenderCustomizeMapper.queryMobiles(tplname);
		return mobiles;
	}

	/**
	 * 查询相应的定时预约开始标的
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<Borrow> selectBorrowAppointStart() {
		int onTime = GetDate.getNowTime10();
		List<Borrow> list = this.ontimeTenderCustomizeMapper.selectBorrowAppointStart(onTime);
		return list;
	}

	/**
	 * 更新标的预约状态
	 * 
	 * @param borrowId
	 * @param status
	 * @author Administrator
	 */

	@Override
	public void updateBorrowAppointStatus(Integer borrowId, int status) {

		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andIdEqualTo(borrowId);
		BorrowWithBLOBs borrow = new BorrowWithBLOBs();
		borrow.setBookingStatus(status);
		this.borrowMapper.updateByExampleSelective(borrow, example);

	}

	/**
	 * 查询相应的定时预约结束标的
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<Borrow> selectBorrowAppointEnd() {
		int onTime = GetDate.getNowTime10();
		List<Borrow> list = this.ontimeTenderCustomizeMapper.selectBorrowAppointEnd(onTime);
		return list;

	}

	/**
	 * 调用自动投标接口成功后进行后续处理
	 * 
	 * @param borrowAppoint
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean updateBorrowTender(BorrowAppoint borrowAppoint, String orderDate, String freezeId) throws Exception {

		// 出借金额
		BigDecimal account = borrowAppoint.getAccount();
		// 订单号
		String orderId = borrowAppoint.getTenderNid();
		// 出借用户
		int userId = borrowAppoint.getUserId();
		AccountChinapnr chinapnr = this.selectAccountChinapnr(userId);
		String tenderCustid = String.valueOf(chinapnr.getChinapnrUsrcustid());
		// 项目编号
		String borrowNid = borrowAppoint.getBorrowNid();
		// 项目信息
		Borrow borrow = this.getBorrowByNid(borrowNid);
		// 剩余可投金额
		BigDecimal borrowAccountWait = borrow.getBorrowAccountWait();
		// 项目还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 项目管理费率
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 服务费率
		BigDecimal serviceFeeRate = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getServiceFeeRate());
		int nowTime = GetDate.getNowTime10();
		// 删除临时表
		BorrowTenderTmpExample borrowTenderTmpExample = new BorrowTenderTmpExample();
		BorrowTenderTmpExample.Criteria criteria1 = borrowTenderTmpExample.createCriteria();
		criteria1.andNidEqualTo(orderId);
		criteria1.andUserIdEqualTo(userId);
		criteria1.andBorrowNidEqualTo(borrowNid);
		boolean tenderTempFlag = borrowTenderTmpMapper.deleteByExample(borrowTenderTmpExample) > 0 ? true
				: false;
		if (tenderTempFlag) {
			// 插入冻结表
			FreezeList record = new FreezeList();
			record.setAmount(account);
			record.setBorrowNid(borrowNid);
			record.setCreateTime(nowTime);
			record.setOrdid(orderId);
			record.setUserId(userId);
			record.setRespcode("");
			record.setTrxid(freezeId);
			record.setOrdid(orderId);
			record.setUsrcustid(tenderCustid);
			record.setXfrom(1);
			record.setStatus(0);
			record.setUnfreezeManual(0);
			System.out.println(
					"用户:" + userId + "***********************************预插入FreezeList：" + JSON.toJSONString(record));
			boolean freezeFlag = freezeListMapper.insertSelective(record) > 0 ? true : false;
			if (freezeFlag) {
				BigDecimal perService = new BigDecimal(0);
				if (StringUtils.isNotEmpty(borrowStyle)) {
					// 到期还本还息end/先息后本endmonth/等额本息month/等额本金principal
					if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)
							|| CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
							|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
							|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
						perService = FinancingServiceChargeUtils.getMonthsPrincipalServiceCharge(account, serviceFeeRate);
					}
					// 按天计息到期还本还息
					if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
						perService = FinancingServiceChargeUtils.getDaysPrincipalServiceCharge(account, serviceFeeRate,
								borrowPeriod);
					}
				}
				BorrowTender borrowTender = new BorrowTender();
				borrowTender.setAccount(account);
				borrowTender.setAccountTender(new BigDecimal(0));
				borrowTender.setActivityFlag(0);//
				borrowTender.setAddip("");
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
				borrowTender.setLoanAmount(account.subtract(perService));
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
				Users users = selectUser(userId);

				// add by zhangjp vip出借记录 start
				UsersInfo userInfo = null;
				// add by zhangjp vip出借记录 end

				if (users != null) {
					if (users.getInvestflag() == 0) {
						users.setInvestflag(1);
						this.usersMapper.updateByPrimaryKeySelective(users);
					}
					// 出借用户名
					borrowTender.setTenderUserName(users.getUsername());
					// 获取出借人属性
					// modify by zhangjp vip出借记录 start
					// UsersInfo userInfo = getUserInfo(userId);
					userInfo = selectUserInfo(userId);
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
								EmployeeCustomize employeeCustomize = employeeCustomizeMapper
										.selectEmployeeByUserId(userId);
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
								SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
										.createCriteria();
								spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
								List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
								if (sList != null && sList.size() == 1) {
									int refUserId = sList.get(0).getSpreadsUserid();
									// 查找用户推荐人
									Users userss = selectUser(refUserId);
									if (userss != null) {
										borrowTender.setInviteUserId(userss.getUserId());
										borrowTender.setInviteUserName(userss.getUsername());
									}
									// 推荐人信息
									UsersInfo refUsers = selectUserInfo(refUserId);
									// 推荐人用户属性
									if (refUsers != null) {
										borrowTender.setInviteUserAttribute(refUsers.getAttribute());
									}
									// 查找用户推荐人部门
									EmployeeCustomize employeeCustomize = employeeCustomizeMapper
											.selectEmployeeByUserId(refUserId);
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
								SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
										.createCriteria();
								spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
								List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
								if (sList != null && sList.size() == 1) {
									int refUserId = sList.get(0).getSpreadsUserid();
									// 查找推荐人
									Users userss = selectUser(refUserId);
									if (userss != null) {
										borrowTender.setInviteUserId(userss.getUserId());
										borrowTender.setInviteUserName(userss.getUsername());
									}
									// 推荐人信息
									UsersInfo refUsers = selectUserInfo(refUserId);
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
				borrowTender.setClient(0);
				borrowTender.setInvestType(1);
				// 单笔出借的融资服务费
				borrowTender.setLoanFee(perService);
				// add by zhangjp vip出借记录 start
				borrowTender.setRemark("现金出借");
				// add by zhangjp vip出借记录 end
				System.out.println("用户:" + userId + "***********************************预插入borrowTender："
						+ JSON.toJSONString(borrowTender));
				boolean trenderFlag = borrowTenderMapper.insertSelective(borrowTender) > 0 ? true : false;
				if (trenderFlag) {
					
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
							Account accountBean = new Account();
							accountBean.setUserId(userId);
							accountBean.setFrost(account);
							// 出借人可用余额
							accountBean.setBalance(account);
							// 出借人待收金额
							accountBean.setInMoney(account);
							Boolean accountFlag = this.adminAccountCustomizeMapper.updateOfTender(accountBean) > 0 ? true
									: false;
							// 插入account_list表
							if (accountFlag) {
								AccountList accountList = new AccountList();
								accountList.setAmount(account);
								accountList.setAwait(new BigDecimal(0));
								accountList.setBalance(balance.subtract(account));
								accountList.setBaseUpdate(0);
								accountList.setCreateTime(nowTime);
								accountList.setFrost(frost.add(account));
								accountList.setInterest(new BigDecimal(0));
								accountList.setIp("");
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
								System.out.println("用户:" + userId + "***********************************预插入accountList："
										+ JSON.toJSONString(accountList));
								boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
								if (accountListFlag) {
									// 更新borrow表
									Map<String, Object> borrowParam = new HashMap<String, Object>();
									borrowParam.put("borrowAccountYes", account);
									borrowParam.put("borrowService", perService);
									borrowParam.put("borrowId", borrow.getId());
									boolean updateBorrowAccountFlag = borrowCustomizeMapper.updateOfBorrow(borrowParam) > 0
											? true : false;
									if (updateBorrowAccountFlag) {
										// 更新预约出借表为成功
										borrowAppoint.setTenderStatus(1);
										borrowAppoint.setUpdateTime(new Date());
										boolean borrowAppointFlag = this.borrowAppointMapper
												.updateByPrimaryKeySelective(borrowAppoint) > 0 ? true : false;
										if (borrowAppointFlag) {
											List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper
													.selectByExample(new CalculateInvestInterestExample());
											if (calculates != null && calculates.size() > 0) {
												CalculateInvestInterest calculateNew = new CalculateInvestInterest();
												calculateNew.setTenderSum(account);
												calculateNew.setId(calculates.get(0).getId());
												calculateNew.setCreateTime(GetDate.getDate(nowTime));
												this.webCalculateInvestInterestCustomizeMapper
														.updateCalculateInvestByPrimaryKey(calculateNew);
											}
											// 计算此时的剩余可出借金额
											BigDecimal accountWait = borrowAccountWait.subtract(account);
											// 满标处理
											if (accountWait.compareTo(new BigDecimal(0)) == 0) {
												System.out.println(
														"用户:" + userId + "***********************************项目满标");
												Map<String, Object> borrowFull = new HashMap<String, Object>();
												borrowFull.put("borrowId", borrow.getId());
												boolean fullFlag = borrowCustomizeMapper.updateOfFullBorrow(borrowFull) > 0
														? true : false;
												if (fullFlag) {
													// 纯发短信接口
													Map<String, String> replaceMap = new HashMap<String, String>();
													replaceMap.put("val_title", borrowNid);
													replaceMap.put("val_date", DateUtils.getNowDate());
													BorrowSendTypeExample sendTypeExample = new BorrowSendTypeExample();
													BorrowSendTypeExample.Criteria sendTypeCriteria = sendTypeExample
															.createCriteria();
													sendTypeCriteria.andSendCdEqualTo("AUTO_FULL");
													List<BorrowSendType> sendTypeList = borrowSendTypeMapper
															.selectByExample(sendTypeExample);
													if (sendTypeList == null || sendTypeList.size() == 0) {
														System.out.println("用户:" + userId
																+ "***********************************冻结成功后处理afterChinaPnR："
																+ "数据库查不到 sendTypeList == null");
														throw new Exception("数据库查不到" + BorrowSendType.class);
													}
													BorrowSendType sendType = sendTypeList.get(0);
													if (sendType.getAfterTime() == null) {
														System.out.println("用户:" + userId
																+ "***********************************冻结成功后处理afterChinaPnR："
																+ "sendType.getAfterTime()==null");
														throw new Exception("sendType.getAfterTime()==null");
													}
													replaceMap.put("val_times", sendType.getAfterTime() + "");
													SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_XMMB, CustomConstants.CHANNEL_TYPE_NORMAL);
													smsProcesser.gather(smsMessage);
												} else {
													throw new Exception("满标更新borrow表失败");
												}
											} else if (accountWait.compareTo(new BigDecimal(0)) < 0) {
												System.out.println("用户:" + userId + "项目编号:" + borrowNid
														+ "***********************************项目暴标");
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
											accountLog.setMoney(account);// 操作金额
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
											accountLog.setBalanceFrost(account.multiply(new BigDecimal(-1)));// 不可提现金额
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
											accountLog.setAddip("");
											accountLog.setBalanceFrostNew(BigDecimal.ZERO);
											accountLog.setBalanceFrostOld(BigDecimal.ZERO);
											System.out.println(
													"用户:" + userId + "***********************************预插入accountLog："
															+ JSON.toJSONString(accountLog));
											boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0
													? true : false;
											if (accountLogFlag) {
												return true;
											} else {
												throw new RuntimeException("accountLog表更新失败");
											}
										} else {
											throw new RuntimeException("borrowAppoint表更新失败");
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
		
	}

	/**
	 * 更新预约标的状态
	 * 
	 * @param borrowAppoint
	 * @param status
	 * @author Administrator
	 */

	@Override
	public void updateAppointStatus(BorrowAppoint borrowAppoint, int status, String remark) {
		borrowAppoint.setTenderStatus(status);
		borrowAppoint.setTenderRemark(remark);
		borrowAppoint.setUpdateTime(new Date());
		borrowAppoint.setTenderTime(new Date());
		this.borrowAppointMapper.updateByPrimaryKeySelective(borrowAppoint);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean updateSendBorrow(int borrowId) {

		// 当前时间
		int nowTime = GetDate.getNowTime10();
		BorrowWithBLOBs borrow = this.borrowMapper.selectByPrimaryKey(borrowId);
		borrow.setBorrowEndTime(String.valueOf(nowTime + borrow.getBorrowValidTime() * 86400));
		// 是否可以进行借款
		borrow.setBorrowStatus(1);
		// 发标的状态
		borrow.setVerifyStatus(1);
		// 状态
		borrow.setStatus(1);
		// 初审时间
		borrow.setVerifyTime(nowTime + "");
		boolean flag = this.borrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
		if (flag) {
			// 写入redis
			RedisUtils.set(borrow.getBorrowNid(), borrow.getBorrowAccountWait().toString());
			// 发送发标短信
			Map<String, String> params = new HashMap<String, String>();
			params.put("val_title", borrow.getBorrowNid());
			SmsMessage smsMessage = new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_DSFB, CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 校验用户相应的预约出借信息
	 * 
	 * @param borrowAppoint
	 * @param info
	 * @author Administrator
	 */

	@Override
	public void checkAppoint(BorrowAppoint borrowAppoint, JSONObject info) {
		// 1.校验用户的开户信息
		int userId = borrowAppoint.getUserId();
		AccountChinapnr chinapnr = this.selectAccountChinapnr(userId);
		if (Validator.isNotNull(chinapnr)) {
			// 2.校验用户的授权状态
			Users user = this.usersMapper.selectByPrimaryKey(userId);
			int userAppointStaus = user.getAuthStatus();
			if (userAppointStaus == 1) {

				// 3.校验用户的余额
				// 获取用户的余额
				BigDecimal avlBalance = BigDecimal.ZERO;
				// 调用汇付接口(4.4.2. 余额查询（后台）)
				ChinapnrBean bean = new ChinapnrBean();
				bean.setVersion(ChinaPnrConstant.VERSION_10);
				bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG); // 消息类型(必须)
				bean.setUsrCustId(String.valueOf(chinapnr.getChinapnrUsrcustid())); // 用户客户号(必须)

				// 写log用参数
				bean.setLogUserId(chinapnr.getUserId()); // 操作者ID
				bean.setLogRemark("余额查询(后台)"); // 备注
				bean.setLogClient("0"); // PC
				// 调用汇付接口
				ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);
				if (chinaPnrBean == null) {
					info.put("message", "调用用户余额查询接口失败");
				} else {
					// 接口返回正常时,执行更新操作
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
						String availableBalance = chinaPnrBean.getAvlBal().replace(",", "");
						System.out.println("AvlBal汇付返回可用余额:" + availableBalance + "++++++++++++++++++++++++++++");
						if (StringUtils.isNotBlank(availableBalance)) {
							avlBalance = new BigDecimal(availableBalance);
							// 预约金额
							BigDecimal account = borrowAppoint.getAccount();
							if (account.compareTo(avlBalance) == 1) {
								info.put("checkFlag", "4");
								info.put("message", "余额不足");
							} else {
								info.put("checkFlag", "1");
							}
						} else {
							info.put("message", "汇付返回数据错误");
						}
					} else {
						info.put("message", "查询用户汇付余额失败");
					}
				}
			} else {
				info.put("checkFlag", "2");
				info.put("message", "取消授权");
			}
		} else {
			info.put("message", "用户未开户");
		}

	}

	/**
	 * 根据项目编号查询相应的用于预约成功记录
	 * 
	 * @param borrowNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowAppoint> selectBorrowAppoint(String borrowNid) {
		BorrowAppointExample example = new BorrowAppointExample();
		BorrowAppointExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andAppointStatusEqualTo(1);
		crt.andTenderStatusIsNull();
		return borrowAppointMapper.selectByExample(example);

	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */
	public AccountChinapnr selectAccountChinapnr(Integer userId) {

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
	 * @throws Exception
	 */
	@Override
	public boolean updateBeforeApoint(BorrowAppoint borrowAppoint) throws Exception {

		int userId = borrowAppoint.getUserId();
		String borrowNid = borrowAppoint.getBorrowNid();
		String orderId = borrowAppoint.getTenderNid();
		BigDecimal account = borrowAppoint.getAccount();
		BorrowTenderTmp tenderTmp = new BorrowTenderTmp();
		tenderTmp.setUserId(userId);
		tenderTmp.setBorrowNid(borrowNid);
		tenderTmp.setNid(orderId);
		tenderTmp.setAccount(account);
		tenderTmp.setAddip("");
		tenderTmp.setChangeStatus(0);
		tenderTmp.setChangeUserid(0);
		tenderTmp.setChangePeriod(0);
		tenderTmp.setTenderStatus(0);
		tenderTmp.setTenderNid(borrowNid);
		tenderTmp.setTenderAwardAccount(new BigDecimal(0));
		tenderTmp.setRecoverFullStatus(0);
		tenderTmp.setRecoverFee(new BigDecimal(0));
		tenderTmp.setRecoverType("");
		tenderTmp.setRecoverAdvanceFee(new BigDecimal(0));
		tenderTmp.setRecoverLateFee(new BigDecimal(0));
		tenderTmp.setTenderAwardFee(new BigDecimal(0));
		tenderTmp.setContents("");
		tenderTmp.setAutoStatus(0);
		tenderTmp.setWebStatus(0);
		tenderTmp.setPeriodStatus(0);
		tenderTmp.setWeb(0);
		Boolean tenderTmpFlag = borrowTenderTmpMapper.insertSelective(tenderTmp) > 0 ? true : false;
		if (tenderTmpFlag) {
			BorrowTenderTmpInfo info = new BorrowTenderTmpInfo();
			info.setOrdid(orderId);
			Map<String, String> map = new HashMap<String, String>();
			map.put("borrow_nid", borrowNid);
			map.put("user_id", userId + "");
			map.put("account", account + "");
			map.put("status", "0");
			map.put("nid", orderId);
			map.put("addtime", (new Date().getTime() / 1000) + "");
			map.put("addip", "");
			String array = JSON.toJSONString(map);
			info.setTmpArray(array);
			info.setAddtime((new Date().getTime() / 1000) + "");
			Boolean tenderTmpInfoFlag = borrowTenderTmpInfoMapper.insertSelective(info) > 0 ? true : false;
			if (tenderTmpInfoFlag) {
				Date date = new Date();
				borrowAppoint.setTenderStatus(0);
				borrowAppoint.setTenderTime(date);
				borrowAppoint.setUpdateTime(date);
				boolean appointFlag = this.borrowAppointMapper.updateByPrimaryKeySelective(borrowAppoint) > 0 ? true
						: false;
				if (appointFlag) {
					return appointFlag;
				} else {
					throw new Exception("预约出借表borrowAppoint更新失败");
				}
			} else {
				throw new Exception("出借临时表borrowTenderTmpInfo插入失败");
			}
		} else {
			throw new Exception("出借临时表BorrowTenderTmp插入失败");
		}

	}

	/**
	 * 自动投标接口调用
	 * 
	 * @param borrowAppoint
	 * @return
	 * @author Administrator
	 * @throws UnsupportedEncodingException
	 */

	@Override
	public JSONObject appointTender(BorrowAppoint borrowAppoint, String orderDate) throws UnsupportedEncodingException {

		// 自动投标的接口返回
		JSONObject result = new JSONObject();
		// 订单号
		String orderId = borrowAppoint.getTenderNid();
		// 项目编号
		String borrowNid = borrowAppoint.getBorrowNid();
		// 出借用户id
		int userId = borrowAppoint.getUserId();
		// 出借用户汇付信息
		AccountChinapnr chinapnrTender = selectAccountChinapnr(userId);
		String tenderUsrcustid = String.valueOf(chinapnrTender.getChinapnrUsrcustid());
		// 项目信息
		Borrow borrow = this.getBorrowByNid(borrowNid);
		// 银行存管标识
		int bankInputFlag = borrow.getBankInputFlag();
		// 借款人汇付信息
		AccountChinapnr chinapnrBorrower = this.selectAccountChinapnr(borrow.getUserId());
		String borrowerUsrcustid = String.valueOf(chinapnrBorrower.getChinapnrUsrcustid());
		// 出借金额
		String account = borrowAppoint.getAccount().toString();
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
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));
		// 用户客户号
		chinapnrBean.setUsrCustId(tenderUsrcustid);
		// 手续费率最高0.1
		chinapnrBean.setMaxTenderRate("0.10");
		// 借款人信息
		Map<String, String> map = new HashMap<String, String>();
		map.put("BorrowerCustId", borrowerUsrcustid);
		map.put("BorrowerAmt", CustomUtil.formatAmount(account));
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
		/*
		 * // 最高还款费率最高0.1 chinapnrBean.setBorrowerRate("0.30"); // 借款人
		 * chinapnrBean.setBorrowerCustId(borrowerUsrcustid); // 借款金额
		 * chinapnrBean.setBorrowerAmt(CustomUtil.formatAmount(account));
		 */
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
			// 调用接口异常
			result.put("status", "0");
			result.put("message", "调用汇付接口返回异常");
		} else {
			// 接口返回正常时,执行更新操作
			if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
				// 自动投标成功
				result.put("status", "1");
				result.put("message", "调用汇付接口成功");
				result.put("freezeId", chinaPnrBean.getFreezeTrxId());
			} else {
				// 调用汇付自动投标接口返回失败
				result.put("status", "2");
				String message = chinaPnrBean.getRespDesc();
				message = URLDecoder.decode(message, "UTF-8");
				result.put("message", "调用汇付接口异常");
			}
		}
		return result;
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
	 * 回滚用户的余额
	 * 
	 * @param borrowAppoint
	 * @author Administrator
	 * @return
	 */

	@Override
	public int updateUserAccount(BorrowAppoint borrowAppoint) {

		BigDecimal account = borrowAppoint.getAccount();
		int userId = borrowAppoint.getUserId();
		Account appointAccount = new Account();
		appointAccount.setUserId(userId);
		appointAccount.setBalance(account);
		appointAccount.setFrost(account);
		return this.adminAccountCustomizeMapper.updateAppointAccount(appointAccount);
	}

	@Override
	public boolean updateUserRecord(BorrowAppoint borrowAppoint) throws Exception {

		Date date = new Date();
		int userId = borrowAppoint.getUserId();
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		int recordTotal = Validator.isNotNull(user.getRecodTotal()) ? user.getRecodTotal() : 0;
		AppointmentRecodLog appointRecordLog = new AppointmentRecodLog();
		appointRecordLog.setAddTime(date);
		appointRecordLog.setRecod(2);
		appointRecordLog.setRecodMoney(borrowAppoint.getAccount());
		appointRecordLog.setRecodNid(borrowAppoint.getBorrowNid());
		appointRecordLog.setApointOrderId(borrowAppoint.getOrderId());
		appointRecordLog.setRecodRemark("取消授权");
		appointRecordLog.setRecodTotal(recordTotal + 2);
		appointRecordLog.setRecodType(0);
		appointRecordLog.setUserId(userId);
		appointRecordLog.setUserName(user.getUsername());
		boolean appointRecordLogFlag = this.appointmentRecodLogMapper.insertSelective(appointRecordLog) > 0 ? true
				: false;
		if (appointRecordLogFlag) {
			user.setRecodTotal(recordTotal + 2);
			user.setRecodTime(date);
			boolean userFlag = this.usersMapper.updateByPrimaryKey(user) > 0 ? true : false;
			if (userFlag) {
				return true;
			} else {
				throw new Exception("更新users表失败！");
			}
		} else {
			throw new Exception("插入appointRecordLogFlag表失败！");
		}
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
			LogUtil.errorLog(this.getClass().getName(), methodName,
					new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
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
	@Override
	public ChinapnrBean usrUnFreeze(String trxId) throws Exception {

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
		if (Validator.isNull(chinapnrBean)) {
			LogUtil.errorLog(this.getClass().getName(), methodName,
					new Exception("调用解冻接口失败![参数：" + bean.getAllParams() + "]"));
			throw new Exception("调用交易查询接口(解冻)失败,[冻结标识：" + trxId + "]");
		} else if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinapnrBean.getRespCode())) {
			return chinapnrBean;
		} else {
			throw new Exception("调用交易查询接口(解冻)返回错误,[冻结标识：" + trxId + "]");
		}
	}

	@Override
	public boolean unFreezeOrder(int investUserId, String orderId, String trxId, String ordDate) throws Exception {

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
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder","调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orderId + "]", null);
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
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)&& !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
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
	 * 获取用户的冻结记录
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param appointOrderId
	 * @return
	 * @author Administrator
	 */

	@Override
	public FreezeList getUserFreeze(int userId, String borrowNid, String appointOrderId) {
		FreezeListExample example = new FreezeListExample();
		FreezeListExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andOrdidEqualTo(appointOrderId);
		List<FreezeList> freezeLists = this.freezeListMapper.selectByExample(example);
		if (freezeLists != null && freezeLists.size() == 1) {
			return freezeLists.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 删除相应的资金冻结记录
	 * 
	 * @param userId
	 * @param appointOrderId
	 * @param trxId
	 * @return
	 * @author Administrator
	 */
	@Override
	public boolean deleteFreezeList(BorrowAppoint borrowAppoint) {

		int nowTime = GetDate.getNowTime10();
		int userId = borrowAppoint.getUserId();
		String borrowNid = borrowAppoint.getBorrowNid();
		String appointOrderId = borrowAppoint.getOrderId();
		BigDecimal account = borrowAppoint.getAccount();
		FreezeListExample freezeExample = new FreezeListExample();
		FreezeListExample.Criteria crt = freezeExample.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andOrdidEqualTo(appointOrderId);
		boolean freezeFlag = this.freezeListMapper.deleteByExample(freezeExample) > 0 ? true : false;
		if (freezeFlag) {
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
				// 待收金额
				BigDecimal await = list.get(0).getAwait();
				// 待还金额
				BigDecimal repay = list.get(0).getRepay();
				// 更新用户账户余额表
				Account accountBean = new Account();
				accountBean.setUserId(userId);
				accountBean.setFrost(account);
				// 出借人可用余额
				accountBean.setBalance(account);
				// 出借人待收金额
				Boolean accountFlag = this.adminAccountCustomizeMapper.updateAppointAccount(accountBean) > 0 ? true: false;
				// 插入account_list表
				if (accountFlag) {
					System.out.println("用户:" + userId + "***********************************更新account，预约订单号：" + appointOrderId);
					// 插入huiyingdai_account_list表
					AccountList accountList = new AccountList();
					// 生成规则BorrowNid_userid_期数
					accountList.setNid(borrowNid + "_" + appointOrderId);
					// 借款人id
					accountList.setUserId(userId);
					// 操作金额
					accountList.setAmount(account);
					// 收支类型1收入2支出3冻结
					accountList.setType(1);
					// 交易类型
					accountList.setTrade("appoint_success");
					// 操作识别码
					accountList.setTradeCode("balance");
					// 资金总额
					accountList.setTotal(total);
					// 可用金额
					accountList.setBalance(balance.add(account));
					// 冻结金额
					accountList.setFrost(frost.subtract(account));
					// 待收金额
					accountList.setAwait(await);
					// 待还金额
					accountList.setRepay(repay);
					// 创建时间
					accountList.setCreateTime(nowTime);
					// 操作员
					accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
					accountList.setRemark(borrowNid);
					accountList.setBaseUpdate(0);
					accountList.setWeb(0);
					boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
					if (accountListFlag) {
						// 扣除可用金额
						AccountLog accountLog = new AccountLog();
						// 操作用户id
						accountLog.setUserId(userId);
						accountLog.setNid("appoint_success_" + userId + "_" + borrowNid + "_" + appointOrderId);
						accountLog.setTotalOld(BigDecimal.ZERO);
						accountLog.setCode("borrow");
						accountLog.setCodeType("tender");
						accountLog.setCodeNid(appointOrderId);
						accountLog.setBorrowNid(borrowNid);// 收入
						accountLog.setIncomeOld(BigDecimal.ZERO);
						accountLog.setIncomeNew(BigDecimal.ZERO);
						accountLog.setAccountWebStatus(0);
						accountLog.setAccountUserStatus(0);
						accountLog.setAccountType("");
						accountLog.setMoney(account);// 操作金额
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
						accountLog.setBalanceFrost(account.multiply(new BigDecimal(-1)));// 不可提现金额
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
						accountLog.setToUserid(userId); // 付给谁
						accountLog.setRemark("预约[" + borrowNid + "]所冻结资金");// 备注
						accountLog.setAddtime(String.valueOf(GetDate.getNowTime10()));
						accountLog.setBalanceFrostNew(BigDecimal.ZERO);
						accountLog.setBalanceFrostOld(BigDecimal.ZERO);
						boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0 ? true : false;
						if (accountLogFlag) {
							System.out.println("用户:" + userId + "***********************************插入accountLog，预约订单号：" + appointOrderId);
							return true;
						} else {
							throw new RuntimeException("accountLog表更新失败");
						}
					} else {
						throw new RuntimeException("用户交易明细表accountList插入失败");
					}
				} else {
					throw new RuntimeException("用户账户信息表account更新失败");
				}
			} else {
				throw new RuntimeException("用户账户信息表account查询失败");
			}
		}else{
			throw new RuntimeException("删除用户预约冻结表freezeList失败");
		}
	}
}
