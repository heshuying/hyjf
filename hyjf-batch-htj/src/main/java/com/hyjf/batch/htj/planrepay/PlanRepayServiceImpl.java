package com.hyjf.batch.htj.planrepay;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.client.autoinvestsys.InvestSysUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtApicronExample;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanLog;
import com.hyjf.mybatis.model.auto.DebtLoanLogExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample;
import com.hyjf.mybatis.model.auto.DebtRepayExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 自动扣款(放款服务)
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class PlanRepayServiceImpl extends BaseServiceImpl implements PlanRepayService {

	private static final String THIS_CLASS = PlanRepayServiceImpl.class.getName();

	/** 用户ID */
	private static final String VAL_USERID = "userId";

	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 出借订单号 */
	private static final String VAL_ORDER_ID = "order_id";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";

	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";
	/** 放款时间 */
	private static final String VAL_LOAN_TIME = "loan_time";

	/** 还款明细ID */
	private static final String PARAM_BORROWRECOVERID = "param_borrowrecoverid";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 自动还款
	 *
	 * @throws Exception
	 */
	public boolean updateDebtRepay(DebtPlan debtPlan, DebtPlanAccede debtPlanAccede) throws Exception {

		System.out.println("-----------汇添金计划还款开始---" + debtPlan.getDebtPlanNid() + "---------"
				+ debtPlanAccede.getAccedeOrderId());
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		/** 基本变量 */
		// 计划编号
		String planNid = debtPlan.getDebtPlanNid();
		// 订单号
		String planOrderId = debtPlanAccede.getAccedeOrderId();
		// 取出余额冻结订单信息1 2 取出还款冻结信息 3取出债权承接冻结
		// 余额冻结金额
		BigDecimal accedeBalance = debtPlanAccede.getAccedeBalance();
		// 取出还款冻结信息
		BigDecimal liquidatesRepayFrost = debtPlanAccede.getLiquidatesRepayFrost();
		// 取出债权承接冻结
		BigDecimal liquidatesCreditFrost = debtPlanAccede.getLiquidatesCreditFrost();
		// 总本息
		BigDecimal account = accedeBalance.add(liquidatesRepayFrost).add(liquidatesCreditFrost);
		// 利息
		BigDecimal interest = BigDecimal.ZERO;
		// 本金
		BigDecimal capital = BigDecimal.ZERO;
		if (account.compareTo(debtPlanAccede.getRepayCapital()) >= 0) {
			interest = account.subtract(debtPlanAccede.getRepayCapital());
			capital = debtPlanAccede.getRepayCapital();
		} else {
			capital = account;
		}

		// 初始化有用的数据防止后面出错
		debtPlanAccede.setRepayAccountYes(account);
		debtPlanAccede.setRepayCapitalYes(capital);
		debtPlanAccede.setRepayInterestYes(interest);
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		/** 自动还款处理开始 */
		// 插入还款debtaccountlist相应的资金
		boolean debtAccountListFlag = this.accedeDebtAccountList(debtPlanAccede);
		if (!debtAccountListFlag) {
			throw new Exception("后续accede更新失败!" + "[计划订单号：" + debtPlanAccede.getAccedeOrderId() + "]");
		}

		System.out.println("计划:" + planNid + " 还款总额:" + account + "还款利息：" + interest + " 还款本金:" + capital);
		// 更新计划还款相关字段
		if (debtPlan.getRepayAccountYes() != null) {
			debtPlan.setRepayAccountYes(debtPlan.getRepayAccountYes().add(account));
		}
		if (debtPlan.getRepayAccountCapitalYes() != null) {
			debtPlan.setRepayAccountCapitalYes(debtPlan.getRepayAccountCapitalYes().add(capital));
		}
		if (debtPlan.getRepayAccountInterestYes() != null) {
			debtPlan.setRepayAccountInterestYes(debtPlan.getRepayAccountInterestYes().add(interest));
		}
		// 还款总明细
		if (account.compareTo(BigDecimal.ZERO) > 0) {
			// 更新用户账户表
			Account investAccount = new Account();
			// 用户id
			investAccount.setUserId(debtPlanAccede.getUserId());
			// 待收
			investAccount.setAwait(debtPlanAccede.getRepayAccountWait());
			// 总计划待收
			investAccount.setPlanAccountWait(debtPlanAccede.getRepayAccountWait());
			// 总计划本金
			investAccount.setPlanCapitalWait(debtPlanAccede.getRepayCapitalWait());
			// 总计划利息
			investAccount.setPlanInterestWait(debtPlanAccede.getRepayInterestWait());
			// 更新用户计划账户
			boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanRepayAll(investAccount) > 0 ? true
					: false;
			if (accountFlag) {

				// 更新用户账户余额表
				Account account1 = this.selectUserAccount(debtPlanAccede.getUserId());
				// 插入相应的资金明细表
				AccountList AccountList = new AccountList();
				AccountList.setNid(planOrderId);
				AccountList.setUserId(debtPlanAccede.getUserId());
				AccountList.setTotal(account1.getTotal());
				AccountList.setBalance(account1.getBalance());
				AccountList.setFrost(account1.getFrost());
				AccountList.setAwait(account1.getAwait());
				AccountList.setPlanBalance(account1.getPlanBalance());
				AccountList.setPlanFrost(account1.getPlanFrost());
				AccountList.setAmount(account);
				AccountList.setType(1);
				AccountList.setTrade("plan_repay");
				AccountList.setTradeCode("balance");
				AccountList.setRemark("计划还款");
				AccountList.setCreateTime(GetDate.getNowTime10());
				AccountList.setWeb(0);
				// 插入交易明细
				boolean accountListFlag = this.accountListMapper.insertSelective(AccountList) > 0 ? true : false;
				if (accountListFlag) {
					// 插入相应的汇添金资金明细表
					DebtAccountList debtAccountList = new DebtAccountList();
					debtAccountList.setNid(debtPlanAccede.getAccedeOrderId());
					debtAccountList.setUserId(debtPlanAccede.getUserId());
					debtAccountList.setPlanNid(debtPlanAccede.getPlanNid());
					debtAccountList.setPlanOrderId(debtPlanAccede.getAccedeOrderId());
					debtAccountList.setUserName(debtPlanAccede.getUserName());
					debtAccountList.setTotal(account1.getTotal());
					debtAccountList.setBalance(account1.getBalance());
					debtAccountList.setFrost(account1.getFrost());
					debtAccountList.setAccountWait(account1.getAwait());
					debtAccountList.setRepayWait(account1.getRepay());
					debtAccountList.setCapitalWait(BigDecimal.ZERO);
					debtAccountList.setInterestWait(BigDecimal.ZERO);
					debtAccountList.setPlanBalance(account1.getPlanBalance());
					debtAccountList.setPlanFrost(account1.getPlanFrost());
					debtAccountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance());
					debtAccountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost());
					debtAccountList.setAmount(account);
					debtAccountList.setType(1);
					debtAccountList.setTrade("plan_repay");
					debtAccountList.setRemark(debtPlanAccede.getAccedeOrderId());
					debtAccountList.setTradeCode("balance");
					debtAccountList.setCreateTime(GetDate.getNowTime10());
					debtAccountList.setCreateUserId(debtPlanAccede.getUserId());
					debtAccountList.setCreateUserName(debtPlanAccede.getUserName());
					debtAccountList.setWeb(0);
					// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
					Integer userId = debtPlanAccede.getUserId();
					UsersInfo userInfo = getUsersInfoByUserId(userId);
					Integer attribute = null;
					if (Validator.isNotNull(userInfo)) {
						// 获取出借用户的用户属性
						attribute = userInfo.getAttribute();
						if (Validator.isNotNull(attribute)) {
							if (attribute == 1) {
								SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
								SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
										.createCriteria();
								spreadsUsersExampleCriteria.andUserIdEqualTo(debtPlanAccede.getUserId());
								List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
								if (sList != null && sList.size() == 1) {
									int refUserId = sList.get(0).getSpreadsUserid();
									// 查找用户推荐人
									Users refererUser = getUsersByUserId(refUserId);
									if (Validator.isNotNull(refererUser)) {

									}
								}
							} else if (attribute == 0) {
								SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
								SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
										.createCriteria();
								spreadsUsersExampleCriteria.andUserIdEqualTo(debtPlanAccede.getUserId());
								List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
								if (sList != null && sList.size() == 1) {
									int refUserId = sList.get(0).getSpreadsUserid();
									// 查找推荐人
									Users refererUser = getUsersByUserId(refUserId);
									if (Validator.isNotNull(refererUser)) {
										debtAccountList.setRefererUserId(refererUser.getUserId());
										debtAccountList.setRefererUserName(refererUser.getUsername());
									}
								}
							}
						}
					}
					// 插入交易明细
					boolean debtAccountListFlag1 = this.debtAccountListMapper.insertSelective(debtAccountList) > 0 ? true
							: false;
					if (debtAccountListFlag1) {
						// 给用户发送短信
						Map<String, String> replaceMap = new HashMap<String, String>();
						UsersInfo usersInfo = this.getUsersInfoByUserId(debtPlanAccede.getUserId());
						replaceMap.put("val_name", usersInfo.getTruename().substring(0, 1));
						if (usersInfo.getSex() == 1) {
							replaceMap.put("val_sex", "先生");
						} else {
							replaceMap.put("val_sex", "女士");
						}
						replaceMap.put("val_htj_title", planNid);
						replaceMap.put("val_amount", debtPlanAccede.getRepayCapitalYes().toString());
						replaceMap.put("val_profit", debtPlanAccede.getRepayInterestYes().toString());
						replaceMap.put("val_balance", account.toString());
						// 发送短信验证码
						SmsMessage smsMessage = new SmsMessage(debtPlanAccede.getUserId(), replaceMap, null, null,
								MessageDefine.SMSSENDFORUSER, null, CustomConstants.HTJ_PARAM_TPL_SDHK,
								CustomConstants.CHANNEL_TYPE_NORMAL);
						smsProcesser.gather(smsMessage);
						System.out.println("汇添金专属标 发送用户计划还款给用户短信，用户：" + debtPlanAccede.getUserId() + "  订单号:"
								+ planOrderId);
						return true;
					} else {
						throw new Exception("还款后debtAccountList表插入失败，计划订单号：" + debtPlanAccede.getAccedeOrderId());
					}
				} else {
					throw new Exception("资金明细表(AccountList)更新失败!" + "[计划订单号：" + planOrderId + "]");
				}

			} else {
				throw new Exception("资金表(Account)更新失败!" + "[计划订单号：" + planOrderId + "]");
			}
		} else {
			throw new Exception("还款金额为0!" + "[计划订单号：" + planOrderId + "]");
		}
	}

	/**
	 * 
	 * @method: accedeDebtAccountList
	 * @description: 更新accede表 还有debtaccountlist表
	 * @param debtFreeze
	 * @param debtPlanAccede
	 * @return
	 * @throws Exception
	 * @return: boolean
	 * @mender: zhouxiaoshuai
	 * @date: 2016年11月11日 上午11:57:04
	 */
	private boolean accedeDebtAccountList(DebtPlanAccede debtPlanAccedeTmp) throws Exception {
		DebtPlanAccede debtPlanAccede1 = new DebtPlanAccede();
		DebtAccountList debtAccountList = new DebtAccountList();
		debtPlanAccede1.setId(debtPlanAccedeTmp.getId());
		debtPlanAccede1.setRepayAccountYes(debtPlanAccedeTmp.getRepayAccountYes());
		debtPlanAccede1.setRepayCapitalYes(debtPlanAccedeTmp.getRepayCapitalYes());
		debtPlanAccede1.setRepayInterestYes(debtPlanAccedeTmp.getRepayInterestYes());
		debtPlanAccede1.setAccedeBalance(BigDecimal.ZERO);
		debtPlanAccede1.setAccedeFrost(BigDecimal.ZERO);
		debtPlanAccede1.setUpdateTime(GetDate.getNowTime10());
		// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
		Integer userId = debtPlanAccedeTmp.getUserId();
		UsersInfo userInfo = getUsersInfoByUserId(userId);
		Integer attribute = null;
		if (Validator.isNotNull(userInfo)) {
			// 获取出借用户的用户属性
			attribute = userInfo.getAttribute();
			if (Validator.isNotNull(attribute)) {
				// 出借人用户属性
				debtPlanAccede1.setUserAttribute(attribute);
				// 如果是线上员工或线下员工，推荐人的userId和username不插
				if (attribute == 2 || attribute == 3) {
					EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
					if (Validator.isNotNull(employeeCustomize)) {
						debtPlanAccede1.setInviteRepayRegionId(employeeCustomize.getRegionId());
						debtPlanAccede1.setInviteRepayRegionName(employeeCustomize.getRegionName());
						debtPlanAccede1.setInviteRepayBranchId(employeeCustomize.getBranchId());
						debtPlanAccede1.setInviteRepayBranchName(employeeCustomize.getBranchName());
						debtPlanAccede1.setInviteRepayDepartmentId(employeeCustomize.getDepartmentId());
						debtPlanAccede1.setInviteRepayDepartmentName(employeeCustomize.getDepartmentName());

					}
				} else if (attribute == 1) {
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && sList.size() == 1) {
						int refUserId = sList.get(0).getSpreadsUserid();
						// 查找用户推荐人
						Users refererUser = getUsersByUserId(refUserId);
						if (Validator.isNotNull(refererUser)) {
							debtPlanAccede1.setInviteRepayUserId(refererUser.getUserId());
							debtPlanAccede1.setInviteRepayUserName(refererUser.getUsername());
							debtAccountList.setRefererUserId(refererUser.getUserId());
							debtAccountList.setRefererUserName(refererUser.getUsername());
						}
						// 推荐人信息
						UsersInfo refererUserInfo = getUsersInfoByUserId(refUserId);
						// 推荐人用户属性
						if (Validator.isNotNull(refererUserInfo)) {
							debtPlanAccede1.setInviteRepayUserAttribute(refererUserInfo.getAttribute());
						}
						// 查找用户推荐人部门
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (Validator.isNotNull(employeeCustomize)) {
							debtPlanAccede1.setInviteRepayRegionId(employeeCustomize.getRegionId());
							debtPlanAccede1.setInviteRepayRegionName(employeeCustomize.getRegionName());
							debtPlanAccede1.setInviteRepayBranchId(employeeCustomize.getBranchId());
							debtPlanAccede1.setInviteRepayBranchName(employeeCustomize.getBranchName());
							debtPlanAccede1.setInviteRepayDepartmentId(employeeCustomize.getDepartmentId());
							debtPlanAccede1.setInviteRepayDepartmentName(employeeCustomize.getDepartmentName());
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
						Users refererUser = getUsersByUserId(refUserId);
						if (Validator.isNotNull(refererUser)) {
							debtPlanAccede1.setInviteRepayUserId(refererUser.getUserId());
							debtPlanAccede1.setInviteRepayUserName(refererUser.getUsername());
							debtAccountList.setRefererUserId(refererUser.getUserId());
							debtAccountList.setRefererUserName(refererUser.getUsername());
						}
						// 推荐人信息
						UsersInfo refererUserInfo = getUsersInfoByUserId(refUserId);
						// 推荐人用户属性
						if (Validator.isNotNull(refererUserInfo)) {
							debtPlanAccede1.setInviteRepayUserAttribute(refererUserInfo.getAttribute());
						}
					}
				}
			}
		}
		boolean accedeFlag = debtPlanAccedeMapper.updateByPrimaryKeySelective(debtPlanAccede1) > 0 ? true : false;
		if (accedeFlag) {
			// 更新用户账户表
			Account investAccount = new Account();
			// 承接用户id
			investAccount.setUserId(debtPlanAccedeTmp.getUserId());
			// 计划可用余额
			investAccount.setPlanBalance(debtPlanAccedeTmp.getRepayAccountYes());
			// 计划可用余额
			investAccount.setBalance(debtPlanAccedeTmp.getRepayAccountYes());
			// 总计划已还利息
			investAccount.setPlanRepayInterest(debtPlanAccedeTmp.getRepayInterestYes());
			// 更新用户计划账户
			boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanRepay(investAccount) > 0 ? true : false;
			if (!accountFlag) {
				throw new Exception("还款后account表更新失败，计划订单号：" + debtPlanAccedeTmp.getAccedeOrderId());
			}
		} else {
			throw new Exception("还款后更新debtplanaccede失败，加入订单号：" + debtPlanAccedeTmp.getAccedeOrderId());
		}
		return true;

	}

	@Override
	public boolean unFreezeOrder(int investUserId, String orderId, String trxId, String ordDate,
			String unfreezeOrderId, String unfreezeOrderDate) throws Exception {

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
			System.out.println("解冻接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", "调用交易查询接口(解冻)失败。" + message + ",[出借订单号："
						+ orderId + "]", null);
				throw new Exception("调用交易查询接口(解冻)失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orderId + "]");
			} else {
				// 汇付交易状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
				if (!"U".equals(transStat) && !"N".equals(transStat)) {
					/** 解冻订单 */
					ChinapnrBean unFreezeBean = usrUnFreeze(trxId, unfreezeOrderId, unfreezeOrderDate);
					String respCode = unFreezeBean == null ? "" : unFreezeBean.getRespCode();
					System.out.println("自动解冻接口返回码：" + respCode);
					// 调用接口失败时(000 或 107 以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
							&& !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
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
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用解冻接口失败![参数：" + bean.getAllParams()
					+ "]"));
			throw new Exception("调用交易查询接口(解冻)失败,[冻结标识：" + trxId + "]");
		} else if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinapnrBean.getRespCode())) {
			return chinapnrBean;
		} else {
			throw new Exception("调用交易查询接口(解冻)返回错误,[冻结标识：" + trxId + "]");
		}
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

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param periodNow
	 * @return
	 */
	public boolean updateAccedeStatus(DebtPlanAccede debtPlanAccede, Integer status) {
		// 当前时间
		int nowTime = GetDate.getNowTime10();

		DebtPlanAccede DebtPlanAccede = new DebtPlanAccede();
		DebtPlanAccede.setId(debtPlanAccede.getId());
		DebtPlanAccede.setStatus(status);// 0出借中 1出借完成 2还款中 3还款完成
		DebtPlanAccede.setUpdateTime(nowTime);
		boolean accedeFlag = debtPlanAccedeMapper.updateByPrimaryKeySelective(DebtPlanAccede) > 0 ? true : false;
		if (accedeFlag && status == 4) {
			DebtPlanAccede sysDebtPlanAccede = selectDebtPlanAccede(debtPlanAccede.getId());
			try {
				// 调用crm同步加入接口
				String debtinfo = JSONObject.toJSONString(sysDebtPlanAccede);
				InvestSysUtils.updateInvestSys(debtinfo);
			} catch (Exception e) {
				System.out.println("专属标还款 crm同步accoced表失败");
			}
		}
		return accedeFlag;
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
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}

		return chinapnrBean;
	}

	/**
	 * 取得还款任务
	 *
	 * @return
	 */
	public List<DebtPlan> getDebtPlanList(Integer status) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria criteria = example.createCriteria();
		criteria.andDebtPlanStatusEqualTo(status);
		example.setOrderByClause(" id asc ");
		List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getDebtApicronListLate(Integer status, Integer apiType) {
		DebtApicronExample example = new DebtApicronExample();
		DebtApicronExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		criteria.andUpdateTimeLessThan(GetDate.getNowTime10() - 3600);
		example.setOrderByClause(" id asc ");
		List<DebtApicron> list = this.debtApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getDebtApicronListWithRepayStatus(Integer status, Integer apiType) {
		DebtApicronExample example = new DebtApicronExample();
		DebtApicronExample.Criteria criteria = example.createCriteria();
		criteria.andRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<DebtApicron> list = this.debtApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateDebtPlan(Integer id, Integer status) {
		return updateDebtPlan(id, status, null);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateDebtApicronOfRepayStatus(Integer id, Integer status) {
		DebtApicron record = new DebtApicron();
		record.setId(id);
		record.setRepayStatus(status);
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.debtApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新DebtPlan任务表
	 *
	 * @param id
	 * @param status
	 * @param data
	 * @return
	 */
	public int updateDebtPlan(Integer id, Integer status, String data) {
		DebtPlanWithBLOBs record = new DebtPlanWithBLOBs();
		record.setId(id);
		record.setDebtPlanStatus(status);
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.debtPlanMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	public DebtBorrowWithBLOBs getBorrow(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrowWithBLOBs> list = this.debtBorrowMapper.selectByExampleWithBLOBs(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款信息
	 *
	 * @return
	 */
	public DebtRepay getDebtRepay(String borrowNid) {
		DebtRepayExample example = new DebtRepayExample();
		DebtRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtRepay> list = this.debtRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款计划信息
	 *
	 * @return
	 */
	public DebtRepayDetail getDebtRepayDetail(String borrowNid, Integer period) {
		DebtRepayDetailExample example = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayPeriodEqualTo(period);
		List<DebtRepayDetail> list = this.debtRepayDetailMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得满标日志
	 *
	 * @return
	 */
	public DebtLoanLog getAccountBorrow(String borrowNid) {
		DebtLoanLogExample example = new DebtLoanLogExample();
		DebtLoanLogExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtLoanLog> list = this.debtLoanLogMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 更新放款状态
	 *
	 * @param accountList
	 * @return
	 */
	public int updateBorrowTender(BorrowTender borrowTender) {
		return borrowTenderMapper.updateByPrimaryKeySelective(borrowTender);
	}

	/**
	 * 更新放款状态(优惠券)
	 *
	 * @param accountList
	 * @return
	 */
	public int updateBorrowTenderCpn(BorrowTenderCpn borrowTenderCpn) {
		return borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTenderCpn);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize borrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
	}

	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, String userId, String nid,
			int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserDebtInvestList(params);
		return list;
	}

	public int countProjectRepayPlanRecordTotal(String borrowNid, String userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		int total = webUserInvestListCustomizeMapper.countDebtLoanDetailRecordTotal(params);
		return total;
	}

	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, String userId, String nid,
			int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper
				.selectDebtLoanDetailList(params);
		return projectRepayList;
	}

	/**
	 * 发送短信(出借成功)
	 *
	 * @param userId
	 */
	public void sendSms(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile())
							|| (users.getInvestSms() != null && users.getInvestSms() == 1)) {
						return;
					}
					System.err.println("userid=" + msg.get(VAL_USERID) + ";专属标开始发送短信,发送金额" + msg.get(VAL_AMOUNT));
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null,
							MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TOUZI_SUCCESS,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 发送短信(优惠券出借成功)
	 *
	 * @param userId
	 */
	@Override
	public void sendSmsCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile())
							|| (users.getInvestSms() != null && users.getInvestSms() == 1)) {
						return;
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null,
							MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_COUPON_TENDER,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 发送邮件(出借成功)
	 *
	 * @param userId
	 */
	public void sendMail(List<Map<String, String>> msgList, String borrowNid) {
		if (msgList != null && msgList.size() > 0 && Validator.isNotNull(borrowNid)) {
			for (Map<String, String> msg : msgList) {
				try {
					// 向每个出借人发送邮件
					if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
						String userId = msg.get(VAL_USERID);
						String orderId = msg.get(VAL_ORDER_ID);
						Users users = getUsersByUserId(Integer.valueOf(userId));
						if (users == null || Validator.isNull(users.getEmail())) {
							return;
						}
						String email = users.getEmail();
						msg.put(VAL_NAME, users.getUsername());
						UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
						if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
							if (usersInfo.getSex() % 2 == 0) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						String fileName = borrowNid + "_" + orderId + ".pdf";
						String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_"
								+ GetDate.getMillis() + StringPool.FORWARD_SLASH;
						// 查询借款人用户名
						DebtBorrowCommonCustomize borrowCommonCustomize = new DebtBorrowCommonCustomize();
						// 借款编码
						borrowCommonCustomize.setBorrowNidSrch(borrowNid);
						List<DebtBorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
						if (recordList != null && recordList.size() == 1) {
						  
							Map<String, Object> contents = new HashMap<String, Object>();
							contents.put("record", recordList.get(0));
							contents.put("borrowNid", borrowNid);
							contents.put("nid", orderId);
							// 借款人用户名
							contents.put("borrowUsername", recordList.get(0).getUsername().substring(0,1)+"**");
							// 本笔的放款完成时间 (协议签订日期)
							contents.put("recoverTime", msg.get(VAL_LOAN_TIME));
							// 用户出借列表
							List<WebUserInvestListCustomize> userInvestList = this.selectUserInvestList(borrowNid,
									userId, orderId, -1, -1);
							if (userInvestList != null && userInvestList.size() > 0) {
		                        WebUserInvestListCustomize userInvest = userInvestList.get(0);
		                            /*userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
		                            userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
		                            userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");*/
		                        contents.put("userInvest", userInvest);
		                    }  else {
								System.out
										.println("专属标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。出借订单号:" + orderId);
								return;
							}
							// 如果是分期还款，查询分期信息
							String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
							if (borrowStyle != null) {
							    //计算预期收益
		                        BigDecimal earnings = new BigDecimal("0");
		                        // 收益率
		                        
		                        String borrowAprString = StringUtils.isEmpty(recordList.get(0).getBorrowApr())?"0.00":recordList.get(0).getBorrowApr().replace("%", "");
		                        BigDecimal borrowApr = new BigDecimal(borrowAprString);
		                        //出借金额
		                        String accountString = StringUtils.isEmpty(recordList.get(0).getAccount())?"0.00":recordList.get(0).getAccount().replace(",", "");
		                        BigDecimal account = new BigDecimal(accountString);
		                       // 周期
		                        String borrowPeriodString = StringUtils.isEmpty(recordList.get(0).getBorrowPeriod())?"0":recordList.get(0).getBorrowPeriod();
		                        String regEx="[^0-9]";   
		                        Pattern p = Pattern.compile(regEx);   
		                        Matcher m = p.matcher(borrowPeriodString); 
		                        borrowPeriodString = m.replaceAll("").trim();
		                        Integer borrowPeriod = Integer.valueOf(borrowPeriodString);
		                        if (StringUtils.equals("endday", borrowStyle)){
		                            // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷365*锁定期；
		                            earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
		                        } else {
		                            // 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
		                            earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

		                        }
		                        contents.put("earnings", earnings);
								if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
										|| "endmonth".equals(borrowStyle)) {
									int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, orderId);
									if (recordTotal > 0) {
										Paginator paginator = new Paginator(1, recordTotal);
										List<WebProjectRepayListCustomize> repayList = this
												.selectProjectRepayPlanList(borrowNid, userId, orderId,
														paginator.getOffset(), paginator.getLimit());
										contents.put("paginator", paginator);
										contents.put("repayList", repayList);
									} else {
										Paginator paginator = new Paginator(1, recordTotal);
										contents.put("paginator", paginator);
										contents.put("repayList", "");
									}
								}
							}
							String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT,
									contents);
							if (StringUtils.isNotEmpty(pdfUrl)) {
								File path = new File(filePath);
								if (!path.exists()) {
									path.mkdirs();
								}
								FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
							}
							String[] emails = { email };
							MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇盈金服互联网金融服务平台居间服务协议",
									null, new String[] { filePath + fileName }, emails,
									CustomConstants.EMAILPARAM_TPL_LOANS, MessageDefine.MAILSENDFORMAILINGADDRESS);
							mailMessageProcesser.gather(message);
							// 更新DebtLoan邮件发送状态
							String borrowRecoverId = msg.get(PARAM_BORROWRECOVERID);
							if (Validator.isNotNull(borrowRecoverId) && NumberUtils.isNumber(borrowRecoverId)) {
								DebtLoan debtLoan = new DebtLoan();
								debtLoan.setId(Integer.valueOf(borrowRecoverId));
								debtLoan.setSendmail(1);
								this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan);
							}
						} else {
							System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
							return;
						}
					}
				} catch (Exception e) {
					LogUtil.errorLog(THIS_CLASS, "sendMail", e);
				}
			}
		}
	}

	/**
	 * 
	 * App消息推送（优惠券出借成功）
	 * 
	 * @author hsy
	 * @param msgList
	 */
	@Override
	public void sendAppMSCoupon(List<Map<String, String>> msgList) {
		System.out.println("msgList size: " + msgList.size());
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_PROFIT))
						&& new BigDecimal(msg.get(VAL_PROFIT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						System.out.println("不满足发送push消息条件，推送失败");
						return;
					}
					System.out.println("开始调用推送消息接口");
					AppMsMessage appMsMessage = new AppMsMessage(users.getUserId(), msg, null,
							MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_COUPON_TENDER);
					appMsProcesser.gather(appMsMessage);
				}
			}
		}
	}

	/**
	 * 推送消息
	 * 
	 * @param msgList
	 * @author Administrator
	 */

	@Override
	public void sendMessage(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						return;
					} else {
						UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
						if (StringUtils.isEmpty(userInfo.getTruename())) {
							msg.put(VAL_NAME, users.getUsername());
						} else if (userInfo.getTruename().length() > 1) {
							msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
						} else {
							msg.put(VAL_NAME, userInfo.getTruename());
						}
						Integer sex = userInfo.getSex();
						if (Validator.isNotNull(sex)) {
							if (sex.intValue() == 2) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null,
								MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TOUZI_SUCCESS);
						appMsProcesser.gather(smsMessage);
					}
				}
			}
		}
	}

	// 新做++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	@Override
	public List<DebtPlanAccede> getdebtPlanAccedeList(String planNid) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria criteria = example.createCriteria();
		criteria.andPlanNidEqualTo(planNid);
		criteria.andStatusEqualTo(3);
		example.setOrderByClause(" id asc ");
		List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新放款状态
	 *
	 * @param accountList
	 * @return
	 */
	public int updateDebtInvest(DebtInvest debtInvest) {
		return debtInvestMapper.updateByPrimaryKeySelective(debtInvest);
	}

	/**
	 * 根据相应的用户userId,加入订单对应的承接债权的待还金额
	 * 
	 * @param userId
	 * @param accedeOrderId
	 * @return
	 */

	/**
	 * 根据相应的计划加入订单记录id获取计划加入记录
	 * 
	 * @param id
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlanAccede selectDebtPlanAccede(Integer id) {
		DebtPlanAccede debtPlanAccede = this.debtPlanAccedeMapper.selectByPrimaryKey(id);
		return debtPlanAccede;

	}

	@Override
	public boolean updateDebtPlanInfoById(DebtPlanWithBLOBs debtPlanInsert) {
		return debtPlanMapper.updateByPrimaryKeySelective(debtPlanInsert) > 0 ? true : false;
	}

}
