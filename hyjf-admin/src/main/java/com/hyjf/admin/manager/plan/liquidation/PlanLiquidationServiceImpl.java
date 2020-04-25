package com.hyjf.admin.manager.plan.liquidation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtCreditExample;
import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.DebtDetailExample;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtFreezeExample;
import com.hyjf.mybatis.model.auto.DebtFreezeLog;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestExample;
import com.hyjf.mybatis.model.auto.DebtInvestExample.Criteria;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.auto.DebtLoanDetailExample;
import com.hyjf.mybatis.model.auto.DebtLoanExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanBorrow;
import com.hyjf.mybatis.model.auto.DebtPlanBorrowExample;
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
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtPlanCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class PlanLiquidationServiceImpl extends BaseServiceImpl implements PlanLiquidationService {

	/**
	 * 
	 * @method: exportPlanList
	 * @description: 计划列表查询
	 * @return: List<DebtPlan>
	 * @mender: zhouxiaoshuai
	 * @date: 2016年9月12日 下午5:57:15
	 */
	@Override
	public List<DebtPlan> exportPlanLiquidationList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		parmEncapsulation(planCommonCustomize, cra);
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);
		return result;
	}

	/**
	 * 
	 * 参数封装
	 * 
	 * @author renxingchen
	 * @param planCommonCustomize
	 * @param cra
	 */
	private void parmEncapsulation(PlanCommonCustomize planCommonCustomize, DebtPlanExample.Criteria cra) {
		// 计划编号
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanNidSrch())) {
			cra.andDebtPlanNidLike("%" + planCommonCustomize.getPlanNidSrch() + "%");
		}
		// 计划状态
		if (StringUtils.isNotEmpty(planCommonCustomize.getPlanStatusSrch())) {
			if ("-1".equals(planCommonCustomize.getPlanStatusSrch())) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(5);
				list.add(6);
				list.add(7);
				cra.andDebtPlanStatusIn(list);
			} else {
				if ("6".equals(planCommonCustomize.getPlanStatusSrch())) {
					List<Integer> list = new ArrayList<Integer>();
					list.add(6);
					list.add(7);
					cra.andDebtPlanStatusIn(list);
					cra.andDebtPlanStatusEqualTo(Integer.valueOf(planCommonCustomize.getPlanStatusSrch()));
				}

			}
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(5);
			list.add(6);
			list.add(7);
			cra.andDebtPlanStatusIn(list);
		}
		// 应清算时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTime())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate
					.getDayStart(planCommonCustomize.getLiquidateShouldTime())));
		}
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateShouldTimeEnd())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate
					.getDayEnd(planCommonCustomize.getLiquidateShouldTimeEnd())));
		}
		// 实际清算时间
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateFactTimeStart())) {
			cra.andCreateTimeGreaterThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate
					.getDayStart(planCommonCustomize.getLiquidateFactTimeStart())));
		}
		if (StringUtils.isNotEmpty(planCommonCustomize.getLiquidateFactTimeEnd())) {
			cra.andCreateTimeLessThanOrEqualTo(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate
					.getDayEnd(planCommonCustomize.getLiquidateFactTimeEnd())));
		}
	}

	@Override
	public List<DebtPlan> selectPlanLiquidationList(PlanCommonCustomize planCommonCustomize) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		parmEncapsulation(planCommonCustomize, cra);
		cra.andLiquidateShouldTimeLessThanOrEqualTo(GetDate.getNowTime10() + 259200);
		// 排序
		example.setOrderByClause("liquidate_fact_time Asc,liquidate_should_time Desc");
		// 分页
		if (planCommonCustomize.getLimitStart() >= 0) {
			example.setLimitStart(planCommonCustomize.getLimitStart());
			example.setLimitEnd(planCommonCustomize.getLimitEnd());
		}
		List<DebtPlan> result = this.debtPlanMapper.selectByExample(example);
		return result;
	}

	@Override
	public int countPlanLiquidation(PlanCommonCustomize planCommonCustomize) {
		int ret = 0;
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		parmEncapsulation(planCommonCustomize, cra);
		cra.andLiquidateShouldTimeLessThanOrEqualTo(GetDate.getNowTime10() + 259200);
		ret = this.debtPlanMapper.countByExample(example);
		return ret;
	}

	@Override
	public DebtPlanCustomize selectPlanLanLiquidationDetail(String planNid) {
		return this.adminDebtPlanCustomizeMapper.selectByPlanNid(planNid);
	}

	@Override
	public List<DebtInvest> selectPlanInvestList(String planNid, Integer status) {
		DebtInvestExample example = new DebtInvestExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andPlanNidEqualTo(planNid);
		createCriteria.andStatusEqualTo(status);
		return this.debtInvestMapper.selectByExample(example);
	}

	@Override
	public DebtFreeze selectDebtFreeze(String trxId, String accedeOrderId, Integer type) {
		DebtFreezeExample example = new DebtFreezeExample();
		DebtFreezeExample.Criteria crt = example.createCriteria();
		if (StringUtils.isNoneBlank(trxId)) {
			crt.andTrxIdEqualTo(trxId);
		}
		if (StringUtils.isNoneBlank(accedeOrderId)) {
			crt.andPlanOrderIdEqualTo(accedeOrderId);
		}
		if (null != type) {
			crt.andFreezeTypeEqualTo(type);
		}
		List<DebtFreeze> debtFreezes = this.debtFreezeMapper.selectByExample(example);
		if (debtFreezes != null && debtFreezes.size() > 0) {
			return debtFreezes.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean unFreezeOrder(Integer investUserId, String orderId, String trxId, String orderDate,
			String unfreezeOrderId, String unfreezeOrderDate) throws Exception {
		// 调用交易查询接口(解冻)
		ChinapnrBean queryTransStatBean = queryTransStat(orderId, orderDate, "FREEZE");
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
	 * 
	 * 解冻
	 * 
	 * @author renxingchen
	 * @param trxId
	 * @param unfreezeOrderId
	 * @param unfreezeOrderDate
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

	@Override
	public boolean updateDebtAccountList(DebtFreeze debtFreeze) throws Exception {
		// 更新冻结订单状态
		int nowTime = GetDate.getNowTime10();// 当前时间
		debtFreeze.setDelFlag(1);
		debtFreeze.setStatus(1);
		debtFreeze.setUpdateTime(nowTime);
		debtFreeze.setUpdateUserId(debtFreeze.getUserId());
		debtFreeze.setUpdateUserName(debtFreeze.getCreateUserName());
		boolean freezeFlag = this.debtFreezeMapper.updateByPrimaryKeySelective(debtFreeze) > 0 ? true : false;
		if (freezeFlag) {
			DebtInvestExample investExample = new DebtInvestExample();
			DebtInvestExample.Criteria cri = investExample.createCriteria();
			cri.andOrderIdEqualTo(debtFreeze.getOrderId());
			DebtInvest record = new DebtInvest();
			record.setStatus(4);// 设置出借状态为新状态 清算出借解冻
			debtInvestMapper.updateByExampleSelective(record, investExample);
			// 更新用户账户表
			Account investAccount = new Account();
			investAccount.setUserId(debtFreeze.getUserId());// 承接用户id
			investAccount.setPlanFrost(debtFreeze.getAmount());// 计划冻结金额
			investAccount.setPlanBalance(debtFreeze.getAmount()); // 计划可用余额
			// 更新用户计划账户
			boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanUnFreeze(investAccount) > 0 ? true
					: false;
			if (accountFlag) {
				Account account = this.selectUserAccount(debtFreeze.getUserId());
				if (null != account) {
					DebtPlanAccede debtPlanAccede = this.selectDebtPlanAccede(debtFreeze.getPlanOrderId());
					if (null != debtPlanAccede) {
						// 插入相应的汇添金资金明细表
						DebtAccountList debtAccountList = new DebtAccountList();
						debtAccountList.setNid(debtFreeze.getUnfreezeOrderId());
						debtAccountList.setUserId(debtFreeze.getUserId());
						debtAccountList.setPlanNid(debtFreeze.getPlanNid());
						debtAccountList.setPlanOrderId(debtFreeze.getPlanOrderId());
						debtAccountList.setUserName(debtFreeze.getUserName());
						debtAccountList.setTotal(account.getTotal());
						debtAccountList.setBalance(account.getBalance());
						debtAccountList.setFrost(account.getFrost());
						debtAccountList.setAccountWait(account.getAwait());
						debtAccountList.setRepayWait(account.getRepay());
						debtAccountList.setCapitalWait(BigDecimal.ZERO);
						debtAccountList.setInterestWait(BigDecimal.ZERO);
						debtAccountList.setPlanBalance(account.getPlanBalance());
						debtAccountList.setPlanFrost(account.getPlanFrost());
						debtAccountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance().add(
								debtFreeze.getAmount()));
						debtAccountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost().subtract(
								debtFreeze.getAmount()));
						debtAccountList.setAmount(debtFreeze.getAmount());
						debtAccountList.setType(4);
						debtAccountList.setTrade("accede_unfreeze");
						debtAccountList.setTradeCode("balance");
						debtAccountList.setRemark(debtFreeze.getPlanOrderId());
						debtAccountList.setCreateTime(GetDate.getNowTime10());
						debtAccountList.setCreateUserId(debtFreeze.getUserId());
						debtAccountList.setCreateUserName(debtFreeze.getUserName());
						debtAccountList.setWeb(0);
						UsersInfo userInfo = getUsersInfoByUserId(debtFreeze.getUserId());
						// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
						Integer attribute = null;
						if (Validator.isNotNull(userInfo)) {
							// 获取出借用户的用户属性
							attribute = userInfo.getAttribute();
							if (Validator.isNotNull(attribute)) {
								if (attribute == 1) {
									SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
									SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
											.createCriteria();
									spreadsUsersExampleCriteria.andUserIdEqualTo(debtFreeze.getUserId());
									List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
									if (sList != null && sList.size() == 1) {
										int refUserId = sList.get(0).getSpreadsUserid();
										// 查找用户推荐人
										Users refererUser = getUsersByUserId(refUserId);
										if (Validator.isNotNull(refererUser)) {
											debtAccountList.setRefererUserId(refererUser.getUserId());
											debtAccountList.setRefererUserName(refererUser.getUsername());
										}
									}
								} else if (attribute == 0) {
									SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
									SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
											.createCriteria();
									spreadsUsersExampleCriteria.andUserIdEqualTo(debtFreeze.getUserId());
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
						boolean debtAccountListFlag = this.debtAccountListMapper.insertSelective(debtAccountList) > 0 ? true
								: false;
						if (debtAccountListFlag) {
							return true;
						} else {
							throw new Exception("解冻后debtAccountList表插入失败，解冻订单号：" + debtFreeze.getOrderId());
						}
					} else {
						// TODO
					}
				} else {
					// TODO
				}
			}
		} else {
			// TODO
		}
		return false;
	}

	/**
	 * 
	 * 查询计划加入表信息
	 * 
	 * @author renxingchen
	 * @param planNid
	 * @return
	 */
	private DebtPlanAccede selectDebtPlanAccede(String planOrderId) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		example.createCriteria().andAccedeOrderIdEqualTo(planOrderId);
		List<DebtPlanAccede> debtPlanAccedes = this.debtPlanAccedeMapper.selectByExample(example);
		if (debtPlanAccedes != null && debtPlanAccedes.size() == 1) {
			return debtPlanAccedes.get(0);
		}
		return null;
	}

	/**
	 * 
	 * 查询用户账户信息
	 * 
	 * @author renxingchen
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

	@Override
	public DebtPlanAccede selectPlanAccede(String planOrderId) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria crt = example.createCriteria();
		crt.andAccedeOrderIdEqualTo(planOrderId);
		List<DebtPlanAccede> planAccedes = this.debtPlanAccedeMapper.selectByExample(example);
		if (planAccedes != null && planAccedes.size() == 1) {
			return planAccedes.get(0);
		}
		return null;
	}

	@Override
	public AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnrExample example = new AccountChinapnrExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<AccountChinapnr> accountChinapnrs = this.accountChinapnrMapper.selectByExample(example);
		if (accountChinapnrs != null && accountChinapnrs.size() == 1) {
			return accountChinapnrs.get(0);
		}
		return null;
	}

	@Override
	public Boolean updateBeforeChinaPnR(String planNid, String frzzeOrderId, String frzzeOrderDate, Integer userId,
			BigDecimal accedeBalance, String tenderUsrcustid) {
		Users user = getUsersByUserId(userId);
		DebtFreezeLog debtFreezeLog = new DebtFreezeLog();
		debtFreezeLog.setAmount(accedeBalance);
		debtFreezeLog.setUserId(userId);
		debtFreezeLog.setUserName(user.getUsername());
		debtFreezeLog.setUserCustId(tenderUsrcustid);
		debtFreezeLog.setPlanNid(planNid);
		debtFreezeLog.setPlanOrderId(frzzeOrderId);// 因为是临时冻结记录表暂时插入冻结的订单号，后续冻结表会更新为加入的订单号
		debtFreezeLog.setOrderId(frzzeOrderId);
		debtFreezeLog.setOrderDate(frzzeOrderDate);
		debtFreezeLog.setTrxId(frzzeOrderId);// 因为是临时冻结记录表暂时插入冻结的订单号，后续冻结表会更新为冻结返回的标识
		debtFreezeLog.setStatus(0);// 冻结订单状态 0冻结 1解冻
		debtFreezeLog.setFreezeType(1);// 冻结类型 0出借冻结 1汇添金冻结
		debtFreezeLog.setDelFlag(0);// 是否有效 0有效 1无效记录
		debtFreezeLog.setCreateTime(GetDate.getNowTime10());
		debtFreezeLog.setCreateUserId(userId);
		debtFreezeLog.setCreateUserName(user.getUsername());
		return debtFreezeLogMapper.insertSelective(debtFreezeLog) > 0 ? true : false;
	}

	@Override
	public String freeze(Integer userId, BigDecimal accedeBalance, String tenderUsrcustid, String frzzeOrderId,
			String frzzeOrderDate) {
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setVersion("10");// 接口版本号
		chinapnrBean.setCmdId("UsrFreezeBg"); // 消息类型(冻结)
		chinapnrBean.setUsrCustId(tenderUsrcustid);// 出借用户客户号
		chinapnrBean.setOrdId(frzzeOrderId); // 订单号(必须)
		chinapnrBean.setOrdDate(frzzeOrderDate);// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setTransAmt(CustomConstants.DF_FOR_VIEW_V1.format(accedeBalance.doubleValue()));// 交易金额(必须)
		chinapnrBean.setRetUrl(""); // 页面返回
		chinapnrBean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		chinapnrBean.setType("user_freeze"); // 日志类型
		chinapnrBean.setLogUserId(userId);
		// chinapnrBean.setIsFreeze("N");
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (bean != null) {
			String respCode = bean.getRespCode();
			if (StringUtils.isNotEmpty(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
				return bean.getTrxId();
			} else {
				System.out.println("用户:" + userId + "***********************************加入计划冻结失败错误码：" + respCode);
				return null;
			}
		} else {
			return null;
		}

	}

	@Override
	public Boolean updatefreezeLog(String planNid, String frzzeOrderId, String frzzeOrderDate, Integer userId,
			BigDecimal accedeBalance, String tenderUsrcustid, Integer client, String trxIdFreeze, String planOrderId,
			DebtPlanAccede planAccede, String borrowNid) throws Exception {
		Users user = getUsersByUserId(userId);
		DebtFreeze debtFreeze = new DebtFreeze();
		debtFreeze.setAmount(accedeBalance);
		debtFreeze.setUserId(userId);
		debtFreeze.setUserName(user.getUsername());
		debtFreeze.setUserCustId(tenderUsrcustid);
		debtFreeze.setPlanNid(planNid);
		debtFreeze.setBorrowNid(borrowNid);
		debtFreeze.setPlanOrderId(planOrderId);
		debtFreeze.setOrderId(frzzeOrderId);
		debtFreeze.setOrderDate(frzzeOrderDate);
		debtFreeze.setTrxId(trxIdFreeze);
		debtFreeze.setStatus(0);// 冻结订单状态 0冻结 1解冻
		debtFreeze.setFreezeType(1);// 冻结类型 0出借冻结 1汇添金冻结
		debtFreeze.setDelFlag(0);// 是否有效 0有效 1无效记录
		debtFreeze.setCreateTime(GetDate.getNowTime10());
		debtFreeze.setCreateUserId(userId);
		debtFreeze.setCreateUserName(user.getUsername());
		boolean freeze = this.debtFreezeMapper.insertSelective(debtFreeze) > 0 ? true : false;
		if (freeze) {
			// 更新清算回复后的金额
			// 更新accede
			DebtPlanAccede planAccedeUpdate = new DebtPlanAccede();
			planAccedeUpdate.setId(planAccede.getId());
			planAccedeUpdate.setAccedeBalance(accedeBalance);
			Boolean updateAccedeFlag = this.updateDebtPlanAccede(planAccedeUpdate);
			if (updateAccedeFlag) {
				// 更新plan
				DebtPlan planUpdate = new DebtPlan();
				planUpdate.setDebtPlanNid(planAccede.getPlanNid());
				planUpdate.setDebtPlanBalance(accedeBalance);
				Boolean updatePlanFlag = this.updateDebtPlanBalance(planUpdate);
				if (updatePlanFlag) {
					// 插入冻结明细
					Account account = this.selectUserAccount(debtFreeze.getUserId());
					if (null != account) {
						// 更新用户账户表
						Account investAccount = new Account();
						investAccount.setUserId(debtFreeze.getUserId());// 承接用户id
						investAccount.setPlanFrost(debtFreeze.getAmount());// 计划冻结金额
						investAccount.setPlanBalance(debtFreeze.getAmount()); // 计划可用余额
						// 更新用户计划账户
						boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanFreeze(investAccount) > 0 ? true
								: false;
						if (accountFlag) {
							DebtPlanAccede debtPlanAccede = this.selectDebtPlanAccede(debtFreeze.getPlanOrderId());
							if (null != debtPlanAccede) {
								// 插入相应的汇添金资金明细表
								DebtAccountList debtAccountList = new DebtAccountList();
								debtAccountList.setNid(debtFreeze.getOrderId());
								debtAccountList.setUserId(debtFreeze.getUserId());
								debtAccountList.setPlanNid(debtFreeze.getPlanNid());
								debtAccountList.setPlanOrderId(debtFreeze.getPlanOrderId());
								debtAccountList.setUserName(debtFreeze.getUserName());
								debtAccountList.setTotal(account.getTotal());
								debtAccountList.setBalance(account.getBalance());
								debtAccountList.setFrost(account.getFrost());
								debtAccountList.setAccountWait(account.getAwait());
								debtAccountList.setRepayWait(account.getRepay());
								debtAccountList.setCapitalWait(BigDecimal.ZERO);
								debtAccountList.setInterestWait(BigDecimal.ZERO);
								debtAccountList.setPlanBalance(account.getPlanBalance()
										.subtract(debtFreeze.getAmount()));
								debtAccountList.setPlanFrost(account.getPlanFrost().add(debtFreeze.getAmount()));
								debtAccountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance());
								debtAccountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost());
								debtAccountList.setAmount(accedeBalance);
								debtAccountList.setType(3);
								debtAccountList.setTrade("accede_freeze");
								debtAccountList.setTradeCode("frost");
								debtAccountList.setRemark(debtFreeze.getPlanOrderId());
								debtAccountList.setCreateTime(GetDate.getNowTime10());
								debtAccountList.setCreateUserId(debtFreeze.getUserId());
								debtAccountList.setCreateUserName(debtFreeze.getUserName());
								debtAccountList.setWeb(0);
								UsersInfo userInfo = getUsersInfoByUserId(debtFreeze.getUserId());
								// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
								Integer attribute = null;
								if (Validator.isNotNull(userInfo)) {
									// 获取出借用户的用户属性
									attribute = userInfo.getAttribute();
									if (Validator.isNotNull(attribute)) {
										if (attribute == 1) {
											SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
											SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
													.createCriteria();
											spreadsUsersExampleCriteria.andUserIdEqualTo(debtFreeze.getUserId());
											List<SpreadsUsers> sList = spreadsUsersMapper
													.selectByExample(spreadsUsersExample);
											if (sList != null && sList.size() == 1) {
												int refUserId = sList.get(0).getSpreadsUserid();
												// 查找用户推荐人
												Users refererUser = getUsersByUserId(refUserId);
												if (Validator.isNotNull(refererUser)) {
													debtAccountList.setRefererUserId(refererUser.getUserId());
													debtAccountList.setRefererUserName(refererUser.getUsername());
												}
											}
										} else if (attribute == 0) {
											SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
											SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
													.createCriteria();
											spreadsUsersExampleCriteria.andUserIdEqualTo(debtFreeze.getUserId());
											List<SpreadsUsers> sList = spreadsUsersMapper
													.selectByExample(spreadsUsersExample);
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
								boolean debtAccountListFlag = this.debtAccountListMapper
										.insertSelective(debtAccountList) > 0 ? true : false;
								if (!debtAccountListFlag) {
									throw new Exception("冻结后debtAccountList表插入失败，冻结订单号：" + debtFreeze.getOrderId());
								}
								// 更新plan
								DebtPlanExample example = new DebtPlanExample();
								DebtPlanExample.Criteria cri = example.createCriteria();
								cri.andDebtPlanNidEqualTo(planAccede.getPlanNid());
								cri.andDebtPlanStatusEqualTo(6);
								DebtPlanWithBLOBs plan = new DebtPlanWithBLOBs();
								plan.setDebtPlanStatus(7);
								boolean planFlag = debtPlanMapper.updateByExampleSelective(plan, example) > 0 ? true
										: false;
								if (planFlag) {
									return true;
								} else {
									throw new Exception("清算回复金额，更新计划DebtPla表失败计划订单号：" + planAccede.getAccedeOrderId());

								}
							} else {
								throw new Exception("清算回复金额，更新计划DebtPlanAccede表失败计划订单号："
										+ planAccede.getAccedeOrderId());
							}
						} else {
							throw new Exception("清算回复金额，查询account表失败计划订单号：" + planAccede.getAccedeOrderId());
						}
					} else {
						throw new Exception("清算回复金额，更新计划DebtPlan表失败计划订单号：" + planAccede.getAccedeOrderId());
					}
				} else {
					throw new Exception("清算回复金额，更新account表失败计划订单号：" + planAccede.getAccedeOrderId());
				}
			} else {
				throw new Exception("清算回复金额，查询计划DebtPlanAccede表失败计划订单号：" + planAccede.getAccedeOrderId());
			}
		} else {
			throw new Exception("清算回复金额，插入冻结frzze表失败冻结订单号：" + frzzeOrderId);
		}

	}

	private Boolean updateDebtPlanBalance(DebtPlan planUpdate) {
		Integer ret = this.debtPlanAccedeCustomizeMapper.updateDebtPlanBalance(planUpdate);
		if (ret > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateDebtPlanAccede(DebtPlanAccede planAccedeUpdate) {
		Integer ret = this.debtPlanAccedeCustomizeMapper.updateDebtPlanAccede(planAccedeUpdate);
		if (ret > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateDebtPlan(DebtPlan debtPlan) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cri = example.createCriteria();
		cri.andIdEqualTo(debtPlan.getId());
		cri.andDebtPlanStatusEqualTo(5);
		DebtPlanWithBLOBs debtPlanWithBLOBs = new DebtPlanWithBLOBs();
		debtPlanWithBLOBs.setDebtPlanStatus(6);
		int ret = this.debtPlanMapper.updateByExampleSelective(debtPlanWithBLOBs, example);
		if (ret > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateLiquidation(String planNid, BigDecimal actualApr, Integer liquidationShouldTime) {
		// 查询债权信息
		List<DebtDetail> debtDetails = this.debtDetailCustomizeMapper.selectDebtDetailForLiquidation(planNid);
		if (debtDetails != null && debtDetails.size() > 0) {

			DebtCredit debtCredit;
			int nowTime;
			BigDecimal defaultAccount = new BigDecimal(0);
			Integer holdDays = 0;
			Integer remainDays = 0;
			BigDecimal interestAdvance = new BigDecimal(0);
			BigDecimal total = new BigDecimal(0);// 本金加利息
			BigDecimal capital = new BigDecimal(0);// 本金
			BigDecimal interest = new BigDecimal(0);// 利息
			boolean liquidation = true;
			DebtLoanExample debtLoanExample;
			DebtCreditExample debtCreditExample;
			DebtCredit credit;// 债权上次清算出来的债权编号
			DebtLoan debtLoan = null;// 放款主数据
			DebtDetail debtDetailCur;// 当前计息周期的债权详情
			List<DebtDetail> debtDetailsNoRepay;
			for (DebtDetail debtDetail : debtDetails) {// 转换债权对象和credit对象
				System.out.println("计划:" + planNid + "债转编号:" + debtDetail.getCreditNid() + "开始清算" + "总共有"
						+ debtDetails.size());
				nowTime = GetDate.getNowTime10();
				if (StringUtils.isNotBlank(debtDetail.getCreditNid())) {// 如果上次清算债权编号不为空
					debtCreditExample = new DebtCreditExample();
					debtCreditExample.createCriteria().andCreditNidEqualTo(debtDetail.getCreditNid());
					credit = this.debtCreditMapper.selectByExample(debtCreditExample).get(0);
					if (null != credit && credit.getDelFlag() == 0 && credit.getCreditStatus() == 2) {// 将debt_credit表中的数据置为无效
						credit.setDelFlag(1);
						this.debtCreditMapper.updateByPrimaryKey(credit);
					}
				}
				// 更新放款信息-写入清算时间
				debtLoanExample = new DebtLoanExample();
				debtLoanExample.createCriteria().andInvestOrderIdEqualTo(debtDetail.getInvestOrderId());
				debtLoan = this.debtLoanMapper.selectByExample(debtLoanExample).get(0);
				if (null != debtLoan) {
					debtLoan.setCreditStatus(1);
					this.debtLoanMapper.updateByPrimaryKey(debtLoan);
				}
				debtCredit = new DebtCredit();
				debtCredit.setUserId(debtDetail.getUserId());
				debtCredit.setUserName(debtDetail.getUserName());
				debtCredit.setPlanNid(debtDetail.getPlanNid());
				debtCredit.setPlanOrderId(debtDetail.getPlanOrderId());
				debtCredit.setBorrowNid(debtDetail.getBorrowNid());
				debtCredit.setBorrowName(debtDetail.getBorrowName());
				debtCredit.setBorrowApr(debtDetail.getBorrowApr());
				debtCredit.setSellOrderId(debtDetail.getOrderId());
				debtCredit.setInvestOrderId(debtDetail.getInvestOrderId());
				debtCredit.setCreditNid(GetOrderIdUtils.getOrderId0(debtDetail.getUserId()));
				debtCredit.setCreditStatus(0);
				debtCredit.setRepayStatus(0);
				debtCredit.setIsLiquidates(0);
				debtCredit.setCreditOrder(0);
				debtCredit.setSourceType(debtDetail.getSourceType());
				int lastdays = 0;
				// 区分还款方式
				if (CustomConstants.BORROW_STYLE_ENDDAY.equals(debtDetail.getBorrowStyle())
						|| CustomConstants.BORROW_STYLE_END.equals(debtDetail.getBorrowStyle())) {
					// 查询当前所处的计息期数的债权信息
					debtDetailCur = this.debtDetailCustomizeMapper.selectDebtDetailCurRepayPeriod(debtDetail
							.getOrderId());
					if (null != debtDetailCur) {
						// 到期还本还息
						capital = debtDetail.getLoanCapital();
						interest = debtDetail.getLoanInterest();
						total = capital.add(interest);
						try {
							holdDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(debtDetail.getLoanTime()),
									GetDate.timestamptoStrYYYYMMDD(liquidationShouldTime));
							if (CustomConstants.BORROW_STYLE_ENDDAY.equals(debtDetail.getBorrowStyle())) {
								remainDays = debtDetail.getBorrowPeriod() - holdDays;
							} else {
								remainDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(liquidationShouldTime),
										GetDate.timestamptoStrYYYYMMDD(debtDetail.getRepayTime()));
							}
							String nowDateStr = GetDate.getDateTimeMyTimeInMillis(nowTime);
							String recoverDate = GetDate.getDateTimeMyTimeInMillis(Integer.valueOf(debtLoan
									.getRepayTime()));
							lastdays = GetDate.daysBetween(nowDateStr, recoverDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						debtCredit.setAssignPeriod(1);
						debtCredit.setLiquidatesPeriod(1);
						debtCredit.setCreditPeriod(1);
						debtCredit.setRepayPeriod(0);
						debtCredit.setCreditAccount(total);// 债转总额
						debtCredit.setCreditCapital(capital);// 债转总本金
						debtCredit.setLiquidatesCapital(capital);// 清算总本金
						debtCredit.setCreditInterest(interest);// 债转总利息
						debtCredit.setCreditAccountWait(total);// 待承接总金额
						debtCredit.setCreditCapitalWait(capital);// 待承接本金
						debtCredit.setCreditInterestWait(interest);// 待承接利息
						// 计算待垫付的利息
						interestAdvance = (debtDetail
								.getLoanInterest()
								.multiply(
										debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8,
												BigDecimal.ROUND_DOWN))
								.multiply(
										new BigDecimal(holdDays).divide(new BigDecimal((holdDays + remainDays)), 8,
												BigDecimal.ROUND_DOWN)).setScale(2, BigDecimal.ROUND_DOWN));
						if (remainDays == 0) {
							debtCredit.setActualApr(debtDetail.getBorrowApr());// 债权的实际出借利率
						} else {
							debtCredit.setActualApr((debtDetail.getLoanInterest().subtract(interestAdvance))
									.divide(debtDetail.getLoanCapital().add(interestAdvance), 8, BigDecimal.ROUND_DOWN)
									.divide(new BigDecimal(remainDays), 8, BigDecimal.ROUND_DOWN)
									.multiply(new BigDecimal(360)).multiply(new BigDecimal(100))
									.setScale(2, BigDecimal.ROUND_DOWN));// 债权的实际出借利率
						}
						debtCredit.setCreditInterestAdvance(interestAdvance);
						debtCredit.setCreditInterestAdvanceWait(interestAdvance);
						// 计算此时的公允价值
						debtCredit.setLiquidationFairValue(capital.add((interest.multiply(new BigDecimal(holdDays)))
								.divide(new BigDecimal((holdDays + remainDays)), 2, BigDecimal.ROUND_DOWN)));
						debtCredit.setRepayAccount(BigDecimal.ZERO);
						debtCredit.setRepayCapital(BigDecimal.ZERO);
						debtCredit.setRepayInterest(BigDecimal.ZERO);
						debtCredit.setRepayAccountWait(total);
						debtCredit.setRepayCapitalWait(capital);
						debtCredit.setRepayInterestWait(interest);
						// 计算此时的公允价值
						debtCredit.setHoldDays(holdDays); // 债权持有时间
						debtCredit.setRemainDays(remainDays);// 还款剩余时间
						debtCredit.setCreditTerm(lastdays);
						// 设置还款时间
						debtCredit.setCreditRepayEndTime(debtDetail.getRepayTime());
						debtCredit.setCreditRepayLastTime(0);
					} else {
						// TODO 有未还款 最后一期或者往前逾期
						try {
							this.yuqi(debtCredit, debtDetail, holdDays, remainDays, interestAdvance, total, capital,
									interest, nowTime, lastdays, liquidationShouldTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {// 分期还款
					// 查询当前所处的计息期数的债权信息
					debtDetailCur = this.debtDetailCustomizeMapper.selectDebtDetailCurRepayPeriod(debtDetail
							.getOrderId());
					if (null != debtDetailCur) {
						if (debtDetailCur.getRepayPeriod() == debtDetail.getRepayPeriod()) {// 当前所处的计息期数==尚未还款的期数
							// 说明之前的分期还款正常完成
							debtCredit.setActualApr(debtDetail.getBorrowApr());
							try {
								// 剩余时间是当前时间至本期应还款时间
								remainDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(nowTime),
										GetDate.timestamptoStrYYYYMMDD(debtDetail.getRepayTime()));
								if (debtDetail.getRepayPeriod() == 1) {// 如果第一期尚未还款
									debtCredit.setCreditRepayLastTime(0);
									// 持有时间是放款时间至当前时间
									holdDays = GetDate.daysBetween(
											GetDate.timestamptoStrYYYYMMDD(debtDetail.getLoanTime()),
											GetDate.timestamptoStrYYYYMMDD(nowTime));
								} else {// 如果不是第一期
									// 查询上一期已经还款的债权详情
									debtDetailCur = this.debtDetailCustomizeMapper
											.selectLastDebtDetailRepayed(debtDetail.getOrderId());
									debtCredit.setCreditRepayLastTime(debtDetailCur.getRepayTime());
									// 持有时间是上一期应还时间至当前时间
									holdDays = GetDate.daysBetween(
											GetDate.timestamptoStrYYYYMMDD(debtDetailCur.getRepayTime()),
											GetDate.timestamptoStrYYYYMMDD(nowTime));
								}
								String bidNid = debtDetail.getBorrowNid();
								DebtRepayDetailExample example = new DebtRepayDetailExample();
								DebtRepayDetailExample.Criteria cra = example.createCriteria();
								cra.andBorrowNidEqualTo(bidNid).andRepayPeriodEqualTo(debtDetail.getBorrowPeriod());
								List<DebtRepayDetail> borrowRepayPlans = this.debtRepayDetailMapper
										.selectByExample(example);

								if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
									lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate
											.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRepayPlans.get(0)
													.getRepayTime())));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							// 设置分期相关信息
							debtCredit.setAssignPeriod(debtDetail.getRepayPeriod());
							debtCredit.setLiquidatesPeriod(debtDetail.getRepayPeriod());
							debtCredit.setCreditPeriod(debtDetail.getBorrowPeriod() - debtDetail.getRepayPeriod() + 1);
							debtCredit.setRepayPeriod(debtDetail.getRepayPeriod() - 1);
							// 计算待垫付的利息
							interestAdvance = ((debtDetail.getLoanInterest().multiply(new BigDecimal(holdDays)))
									.divide(new BigDecimal(holdDays + remainDays), 2, BigDecimal.ROUND_DOWN));
							debtCredit.setCreditInterestAdvance(interestAdvance);
							debtCredit.setCreditInterestAdvanceWait(interestAdvance);
							capital = debtDetail.getLoanCapital();
							interest = debtDetail.getLoanInterest();
							total = capital.add(interest);
							// 计算真实的债权总额、本金、利息
							if (debtDetail.getRepayPeriod() == debtDetail.getBorrowPeriod()) {// 如果当前期是最后一期
								debtCredit.setCreditAccount(total);// 债转总额
								debtCredit.setCreditCapital(capital);// 债转总本金
								debtCredit.setLiquidatesCapital(capital);// 清算总本金
								debtCredit.setCreditInterest(interest);// 债转总利息
								debtCredit.setCreditAccountWait(total);// 待承接总金额
								debtCredit.setCreditCapitalWait(capital);// 待承接本金
								debtCredit.setCreditInterestWait(interest);// 待承接利息
								debtCredit.setRepayAccount(BigDecimal.ZERO);
								debtCredit.setRepayCapital(BigDecimal.ZERO);
								debtCredit.setRepayInterest(BigDecimal.ZERO);
								debtCredit.setRepayAccountWait(total);
								debtCredit.setRepayCapitalWait(capital);
								debtCredit.setRepayInterestWait(interest);
								debtCredit.setCreditRepayEndTime(debtDetail.getRepayTime());
							} else {// 如果当前期不是最后一期 则需要查询当前期到最后一期的债权信息
								debtDetailsNoRepay = this.debtDetailCustomizeMapper.selectDebtDetailNoRepay(debtDetail
										.getOrderId());
								capital = new BigDecimal(0);
								interest = new BigDecimal(0);
								total = new BigDecimal(0);
								for (DebtDetail debtDetailNoRepay : debtDetailsNoRepay) {
									capital = capital.add(debtDetailNoRepay.getLoanCapital());
									interest = interest.add(debtDetailNoRepay.getLoanInterest());
									total = capital.add(interest);
									if (debtDetailNoRepay.getRepayPeriod() == debtDetailNoRepay.getBorrowPeriod()) {
										debtCredit.setCreditRepayEndTime(debtDetailNoRepay.getRepayTime());
									}
								}
								debtCredit.setCreditAccount(total);// 债转总额
								debtCredit.setCreditCapital(capital);// 债转总本金
								debtCredit.setLiquidatesCapital(capital);// 清算总本金
								debtCredit.setCreditInterest(interest);// 债转总利息
								debtCredit.setCreditAccountWait(total);// 待承接总金额
								debtCredit.setCreditCapitalWait(capital);// 待承接本金
								debtCredit.setCreditInterestWait(interest);// 待承接利息
								debtCredit.setRepayAccount(BigDecimal.ZERO);
								debtCredit.setRepayCapital(BigDecimal.ZERO);
								debtCredit.setRepayInterest(BigDecimal.ZERO);
								debtCredit.setRepayAccountWait(total);
								debtCredit.setRepayCapitalWait(capital);
								debtCredit.setRepayInterestWait(interest);
							}
							// 计算此时的公允价值
							debtCredit.setLiquidationFairValue(capital.add(interestAdvance));
							debtCredit.setHoldDays(holdDays); // 债权持有时间
							debtCredit.setRemainDays(remainDays);// 还款剩余时间
							debtCredit.setCreditTerm(lastdays);
						} else {
							// 当前所处的计息期数 != 尚未还款的期数
							// (说明可能出现逾期或者延期或者提前还款),有未还款 中间期数逾期
							try {
								this.yuqi(debtCredit, debtDetail, holdDays, remainDays, interestAdvance, total,
										capital, interest, nowTime, lastdays, liquidationShouldTime);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						// TODO 有未还款 最后一期或者往前逾期
						try {
							this.yuqi(debtCredit, debtDetail, holdDays, remainDays, interestAdvance, total, capital,
									interest, nowTime, lastdays, liquidationShouldTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				debtCredit.setCreditAccountAssigned(defaultAccount);
				debtCredit.setCreditCapitalAssigned(defaultAccount);
				debtCredit.setCreditInterestAssigned(defaultAccount);
				debtCredit.setCreditInterestAdvanceAssigned(defaultAccount);
				debtCredit.setCreditDiscount(defaultAccount);
				debtCredit.setCreditIncome(defaultAccount);
				debtCredit.setCreditServiceFee(defaultAccount);
				debtCredit.setCreditPrice(defaultAccount);
				debtCredit.setCreditRepayNextTime(debtDetail.getRepayTime());
				debtCredit.setCreditRepayYesTime(0);
				debtCredit.setEndTime(0);
				debtCredit.setAssignNum(0);
				debtCredit.setDelFlag(0);
				debtCredit.setClient(0);
				debtCredit.setCreateTime(nowTime);
				debtCredit.setCreateUserId(debtDetail.getUserId());
				debtCredit.setCreateUserName(debtDetail.getUserName());
				int ret = this.debtCreditMapper.insertSelective(debtCredit);
				if (ret > 0) {
					continue;
				} else {
					liquidation = false;
					break;
				}
			}
			if (liquidation) {
				if (this.debtDetailCustomizeMapper.updateDetailDelFlagToOne(planNid) > 0) {// 将detail
																							// delFlag置为1
					DebtPlanExample example = new DebtPlanExample();
					example.createCriteria().andDebtPlanNidEqualTo(planNid);
					example.createCriteria().andDebtPlanStatusEqualTo(6);
					DebtPlanWithBLOBs record = new DebtPlanWithBLOBs();
					record.setDebtPlanStatus(7);
					record.setLiquidateFactTime(GetDate.getNowTime10());
					if (this.debtPlanMapper.updateByExampleSelective(record, example) > 0) {// 更新计划表的清算时间
						return true;
					} else {
						throw new RuntimeException("更新计划表失败");
					}
				} else {
					throw new RuntimeException("更新债权信息表的债权有效状态失败");
				}
			} else {
				throw new RuntimeException("清算失败");
			}
		} else {

			DebtPlanExample example = new DebtPlanExample();
			example.createCriteria().andDebtPlanNidEqualTo(planNid);
			DebtPlanWithBLOBs record = new DebtPlanWithBLOBs();
			record.setDebtPlanStatus(8);
			record.setLiquidateFactTime(GetDate.getNowTime10());
			if (this.debtPlanMapper.updateByExampleSelective(record, example) > 0) {

				DebtPlanAccedeExample example1 = new DebtPlanAccedeExample();
				example1.createCriteria().andPlanNidEqualTo(planNid);
				example1.createCriteria().andStatusEqualTo(0);
				DebtPlanAccede record1 = new DebtPlanAccede();
				record1.setStatus(3);
				record1.setUpdateTime(GetDate.getNowTime10());
				if (this.debtPlanAccedeMapper.updateByExampleSelective(record1, example1) > 0) {
					return true;
				} else {
					throw new RuntimeException("更新计划加入表失败");
				}

			} else {
				throw new RuntimeException("更新计划表失败");
			}

		}
	}

	/**
	 * 
	 * @method: yuqi
	 * @description: 逾期计算方法，记得去改detail的逾期延期状态 天数和逾期利息延期利息 天数
	 * @param debtCredit
	 * @param debtDetail
	 * @param holdDays
	 * @param remainDays
	 * @param interestAdvance
	 * @param total
	 * @param capital
	 * @param interest
	 * @param nowTime
	 * @param lastdays
	 * @param liquidationShouldTime
	 * @return: void
	 * @throws Exception
	 * @mender: zhouxiaoshuai
	 * @date: 2017年2月23日 下午3:06:55
	 */
	private void yuqi(DebtCredit debtCredit, DebtDetail debtDetail, Integer holdDays, Integer remainDays,
			BigDecimal interestAdvance, BigDecimal total, BigDecimal capital, BigDecimal interest, int nowTime,
			int lastdays, Integer liquidationShouldTime) throws Exception {
		// 说明之前的分期还款正常完成
		debtCredit.setActualApr(debtDetail.getBorrowApr());
		// 查询上一期已经还款的债权详情
		DebtDetail debtDetailCur = this.debtDetailCustomizeMapper.selectLastDebtDetailRepayed(debtDetail.getOrderId());
		BigDecimal lateInterest = BigDecimal.ZERO;
		BigDecimal delayInterest = BigDecimal.ZERO;
		BigDecimal lateInterestSum = BigDecimal.ZERO;
		BigDecimal delayInterestSum = BigDecimal.ZERO;

		// 判断是逾期还是延期
		// 查询所有未还款的债权信息(为处理逾期或延期做准备)
		DebtDetailExample detailExample = new DebtDetailExample();
		DebtDetailExample.Criteria cri = detailExample.createCriteria();
		cri.andBorrowNidEqualTo(debtDetail.getBorrowNid());
		cri.andRepayStatusEqualTo(0);
		cri.andInvestOrderIdEqualTo(debtDetail.getInvestOrderId());
		cri.andStatusEqualTo(1);
		cri.andRepayTimeLessThan(nowTime);
		detailExample.setOrderByClause(" repay_period asc");
		List<DebtDetail> debtDetails = this.debtDetailMapper.selectByExample(detailExample);
		for (int i = 0; i < debtDetails.size(); i++) {
			DebtDetail debtDetailAll = debtDetails.get(i);
			List<DebtRepayDetail> repayDetails = searchRepayDetail(debtDetail.getBorrowNid(),
					debtDetailAll.getRepayPeriod());
			// TODO 看分期是否逾期
			if (repayDetails != null && repayDetails.size() > 0) {
				// 更新相应的债转承接金额
				DebtLoanDetailExample debtLoanDetailExample = new DebtLoanDetailExample();
				DebtLoanDetailExample.Criteria debtLoanDetailCrt = debtLoanDetailExample.createCriteria();
				debtLoanDetailCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
				debtLoanDetailCrt.andRepayPeriodEqualTo(debtDetailAll.getRepayPeriod());
				List<DebtLoanDetail> debtLoanDetailList = this.debtLoanDetailMapper
						.selectByExample(debtLoanDetailExample);
				if (debtLoanDetailList != null && debtLoanDetailList.size() == 1) {
					DebtLoanDetail debtLoanDetail = debtLoanDetailList.get(0);

					DebtRepayDetail repayDetail = repayDetails.get(0);
					int delayDays = repayDetail.getDelayDays().intValue();
					String repayTimeStr = repayDetail.getRepayTime();
					int time = GetDate.getNowTime10();
					// 用户计划还款时间
					String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
					// 用户实际还款时间
					String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
					// 应清算时间
					String liqTime = GetDate.getDateTimeMyTimeInMillis(liquidationShouldTime);
					// 项目天数
					Integer termDays = 30;
					// 如果是逾期的第一期则取上一期到这期的还款时间 天数 ，就是当月天数
					if ((debtDetailCur.getRepayPeriod() + 1) == debtDetailAll.getRepayPeriod() && i != 0) {
						termDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(debtDetailCur.getRepayTime()),
								GetDate.timestamptoStrYYYYMMDD(debtDetailAll.getRepayTime()));
					} else {
						// 中间期
						termDays = GetDate.daysBetween(
								GetDate.timestamptoStrYYYYMMDD(debtDetails.get(i - 1).getRepayTime()),
								GetDate.timestamptoStrYYYYMMDD(debtDetailAll.getRepayTime()));
					}
					// 待收收益
					interest = debtDetailAll.getRepayInterestWait();
					// 获取实际还款同计划还款时间的时间差
					int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
					if (distanceDays < 0) {// 用户延期或者逾期了
						int lateDays = delayDays + distanceDays;
						DebtDetail record = new DebtDetail();
						record.setId(debtDetailAll.getId());
						if (lateDays >= 0) {// 用户延期还款
							// 延期利息=待收收益*（应清算日-原项目还款日）/项目天数 (*（承接本金/出让人总出让本金）
							// 如果应清算日期和项目还款时间 取最小
							// 是1)
							if (GetDate.daysBetween(repayTime, liqTime) > delayDays) {
								delayInterest = interest.multiply(new BigDecimal(delayDays)).divide(
										new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
							} else {
								delayInterest = interest.multiply(
										new BigDecimal(GetDate.daysBetween(repayTime, liqTime))).divide(
										new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
							}
							if (delayInterest.compareTo(BigDecimal.ZERO) < 0) {
								delayInterest = BigDecimal.ZERO;
							}
							delayDays = -distanceDays;
							record.setAdvanceStatus(2);
							record.setDelayDays(delayDays);
							record.setDelayInterest(delayInterest);
							record.setDelayInterestAssigned(BigDecimal.ZERO);
							debtDetailMapper.updateByPrimaryKeySelective(record);
							if (debtDetailAll.getSourceType() == 1) {
								delayInterestSum = delayInterestSum.add(delayInterest);
								debtLoanDetail.setRepayDelayInterest(delayInterest);
								boolean debtLoanDetailFlag = this.debtLoanDetailMapper
										.updateByPrimaryKeySelective(debtLoanDetail) > 0 ? true : false;
								if (!debtLoanDetailFlag) {
									throw new Exception("更新相应的出借订单的分期放款信息失败，出借订单号：" + debtCredit.getInvestOrderId()
											+ ",期数：" + debtDetailAll.getRepayPeriod());
								}
							}

						} else {
							// 用户逾期还款

							if (delayDays > 0) {
								// 延期并逾期
								// 延期利息+逾期利息=待收收益*延期天数/项目当期天数*（承接本金/出让人总出让本金）+待收收益*（应清算日-应还款日-延期天数）/项目当期天数*（承接本金/出让人总出让本金）；
								if (GetDate.daysBetween(repayTime, liqTime) > delayDays) {
									delayInterest = interest.multiply(new BigDecimal(delayDays)).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								} else {
									delayInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, liqTime))).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								}
								record.setDelayDays(delayDays);
								record.setDelayInterest(delayInterest);
								delayInterestSum = delayInterestSum.add(delayInterest);
								// 逾期
								// 逾期利息=待收收益*（应清算日-原项目还款日）/项目天数*（承接本金/出让人总出让本金）
								if (liquidationShouldTime > time) {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, factRepayTime) - delayDays))
											.divide(new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								} else {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, liqTime) - delayDays))
											.divide(new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								}
							} else {
								// 逾期
								// 逾期利息=待收收益*（应清算日-原项目还款日）/项目天数*（承接本金/出让人总出让本金）
								if (liquidationShouldTime > time) {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, factRepayTime))).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								} else {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, liqTime))).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								}
							}

							lateInterestSum = lateInterestSum.add(lateInterest);
							lateDays = -lateDays;
							record.setAdvanceStatus(3);
							record.setLateDays(lateDays);
							record.setLateInterest(lateInterest);
							record.setLateInterestAssigned(BigDecimal.ZERO);
							debtDetailMapper.updateByPrimaryKeySelective(record);
							if (debtDetailAll.getSourceType() == 1) {
								debtLoanDetail.setRepayDelayInterest(delayInterest);
								debtLoanDetail.setRepayLateInterest(lateInterest);
								boolean debtLoanDetailFlag = this.debtLoanDetailMapper
										.updateByPrimaryKeySelective(debtLoanDetail) > 0 ? true : false;
								if (!debtLoanDetailFlag) {
									throw new Exception("更新相应的出借订单的分期放款信息失败，出借订单号：" + debtCredit.getInvestOrderId()
											+ ",期数：" + debtDetailAll.getRepayPeriod());
								}
							}
						}
					}
				} else {
					throw new Exception("未查询到相应的出借订单的分期放款信息，出借订单号：" + debtCredit.getInvestOrderId() + ",期数："
							+ debtDetailAll.getRepayPeriod());
				}
			} else {
				// 不分期预期或延期
				List<DebtRepay> debtRepays = searchRepay(debtDetail.getBorrowNid());
				if (debtRepays != null && debtRepays.size() > 0) {
					DebtRepay debtRepay = debtRepays.get(0);
					int delayDays = debtRepay.getDelayDays().intValue();
					String repayTimeStr = debtRepay.getRepayTime();
					int time = GetDate.getNowTime10();
					// 用户计划还款时间
					String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
					// 用户实际还款时间
					String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
					// 应清算时间
					String liqTime = GetDate.getDateTimeMyTimeInMillis(liquidationShouldTime);
					// 项目天数
					Integer termDays = 30;
					termDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(debtDetailAll.getLoanTime()),
							GetDate.timestamptoStrYYYYMMDD(debtDetailAll.getRepayTime()));
					// 待收收益
					interest = debtDetailAll.getRepayInterestWait();
					// 获取实际还款同计划还款时间的时间差
					int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
					if (distanceDays < 0) {// 用户延期或者逾期了
						int lateDays = delayDays + distanceDays;
						DebtDetail record = new DebtDetail();
						record.setId(debtDetailAll.getId());
						if (lateDays >= 0) {// 用户延期还款
							// 延期利息=待收收益*（应清算日-原项目还款日）/项目天数
							// (*（承接本金/出让人总出让本金）
							// 是1)
							if (GetDate.daysBetween(repayTime, liqTime) > delayDays) {
								delayInterest = interest.multiply(new BigDecimal(delayDays)).divide(
										new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
							} else {
								delayInterest = interest.multiply(
										new BigDecimal(GetDate.daysBetween(repayTime, liqTime))).divide(
										new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
							}
							if (delayInterest.compareTo(BigDecimal.ZERO) < 0) {
								delayInterest = delayInterest.multiply(new BigDecimal("-1"));
							}
							delayDays = -distanceDays;
							record.setAdvanceStatus(2);
							record.setDelayDays(delayDays);
							record.setDelayInterest(delayInterest);
							record.setDelayInterestAssigned(BigDecimal.ZERO);
							debtDetailMapper.updateByPrimaryKeySelective(record);
							if (debtDetailAll.getSourceType() == 1) {
								delayInterestSum = delayInterestSum.add(delayInterest);
							}

						} else {
							// 用户逾期还款

							if (delayDays > 0) {
								// 延期并逾期
								// 延期利息+逾期利息=待收收益*延期天数/项目当期天数*（承接本金/出让人总出让本金）+待收收益*（应清算日-应还款日-延期天数）/项目当期天数*（承接本金/出让人总出让本金）；
								if (GetDate.daysBetween(repayTime, liqTime) > delayDays) {
									delayInterest = interest.multiply(new BigDecimal(delayDays)).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								} else {
									delayInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, liqTime))).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								}
								if (delayInterest.compareTo(BigDecimal.ZERO) < 0) {
									delayInterest = delayInterest.multiply(new BigDecimal("-1"));
								}
								record.setDelayDays(delayDays);
								record.setDelayInterest(delayInterest);
								delayInterestSum = delayInterestSum.add(delayInterest);
								// 逾期
								// 逾期利息=待收收益*（应清算日-原项目还款日）/项目天数*（承接本金/出让人总出让本金）
								if (liquidationShouldTime > time) {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, factRepayTime) - delayDays))
											.divide(new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								} else {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, liqTime) - delayDays))
											.divide(new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								}
							} else {
								// 逾期
								// 逾期利息=待收收益*（应清算日-原项目还款日）/项目天数*（承接本金/出让人总出让本金）
								if (liquidationShouldTime > time) {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, factRepayTime))).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								} else {
									lateInterest = interest.multiply(
											new BigDecimal(GetDate.daysBetween(repayTime, liqTime))).divide(
											new BigDecimal(termDays), 2, BigDecimal.ROUND_DOWN);
								}
							}

							if (lateInterest.compareTo(BigDecimal.ZERO) < 0) {
								lateInterest = lateInterest.multiply(new BigDecimal("-1"));
							}

							lateInterestSum = lateInterestSum.add(lateInterest);
							lateDays = -lateDays;
							record.setAdvanceStatus(3);
							record.setLateDays(lateDays);
							record.setLateInterest(lateInterest);
							record.setLateInterestAssigned(BigDecimal.ZERO);
							debtDetailMapper.updateByPrimaryKeySelective(record);
						}
					}
				} else {
					throw new Exception("未查询到相应的出借订单的放款信息，出借订单号：" + debtCredit.getInvestOrderId() + ",期数："
							+ debtDetailAll.getRepayPeriod());
				}

			}

		}
		if (debtDetail.getSourceType() == 1) {
			// 修改loan分期或者不分期的逾期延期利息
			DebtLoanExample loanExample = new DebtLoanExample();
			DebtLoanExample.Criteria criLoan = loanExample.createCriteria();
			criLoan.andBorrowNidEqualTo(debtDetail.getBorrowNid());
			criLoan.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
			List<DebtLoan> listLoan = debtLoanMapper.selectByExample(loanExample);
			if (listLoan != null && listLoan.size() > 0) {
				DebtLoan debtLoan = new DebtLoan();
				debtLoan.setRepayDelayInterest(delayInterestSum);
				debtLoan.setRepayLateInterest(lateInterestSum);
				boolean debtLoanFlag = this.debtLoanMapper.updateByExampleSelective(debtLoan, loanExample) > 0 ? true
						: false;
				if (!debtLoanFlag) {
					throw new Exception("更新debtLoan表失败，出借订单号:" + debtCredit.getInvestOrderId() + "；标号："
							+ debtDetail.getBorrowNid());
				}
			} else {
				throw new Exception("未查询到相应的出借订单的放款信息，出借订单号：" + debtCredit.getInvestOrderId() + ",标号："
						+ debtDetail.getBorrowNid());
			}
		}
		try {
			// 剩余时间是当前时间至本期应还款时间
			remainDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(nowTime),
					GetDate.timestamptoStrYYYYMMDD(debtDetail.getRepayTime()));
			if (remainDays <= 0) {
				remainDays = 0;
			}
			// 持有期限 需求 如果是分期最后一期或者是不分期逾期 ， 那么是最后一期或者当期天数
			if (debtDetail.getRepayPeriod() == debtDetail.getBorrowPeriod()
					|| debtDetail.getBorrowStyle().equals("end") || debtDetail.getBorrowStyle().equals("endday")) {
				// 不分期 逾期
				if (debtDetail.getBorrowStyle().equals("end") || debtDetail.getBorrowStyle().equals("endday")) {
					holdDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(debtDetail.getLoanTime()),
							GetDate.timestamptoStrYYYYMMDD(debtDetail.getRepayTime()));

				} else {
					// 分期最后一期 逾期
					holdDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(debtDetailCur.getRepayTime()),
							GetDate.timestamptoStrYYYYMMDD(debtDetails.get(debtDetails.size() - 1).getRepayTime()));
				}
				debtCredit.setCreditRepayLastTime(0);
				lastdays = GetDate.daysBetween(nowTime, debtDetail.getRepayTime());
				if (lastdays < 0) {
					lastdays = 0;
				}
			} else {// 分期不是最后一期
				debtCredit.setCreditRepayLastTime(0);
				// 持有时间是上一期应还时间至当前时间 (当期上一期还款时间)
				holdDays = GetDate.daysBetween(
						GetDate.timestamptoStrYYYYMMDD(debtDetails.get(debtDetails.size() - 1).getRepayTime()),
						GetDate.timestamptoStrYYYYMMDD(liquidationShouldTime));
				lastdays = GetDate.daysBetween(nowTime, debtDetails.get(debtDetails.size() - 1).getRepayTime());
				if (lastdays < 0) {
					lastdays = 0;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 设置分期相关信息
		debtCredit.setAssignPeriod(debtDetail.getRepayPeriod());
		debtCredit.setHoldDays(holdDays);
		debtCredit.setRemainDays(remainDays);
		debtCredit.setCreditTerm(lastdays);
		debtCredit.setLiquidatesPeriod(debtDetail.getRepayPeriod());
		if (debtDetail.getBorrowStyle().equals("end") || debtDetail.getBorrowStyle().equals("endday")) {
			debtCredit.setCreditPeriod(1);
		} else {
			debtCredit.setCreditPeriod(debtDetail.getBorrowPeriod() - debtDetail.getRepayPeriod() + 1);
		}

		debtCredit.setRepayPeriod(debtDetail.getRepayPeriod() - 1);
		// 计算待垫付的利息
		interestAdvance = ((debtDetail.getLoanInterest().multiply(new BigDecimal(holdDays))).divide(new BigDecimal(
				holdDays + remainDays), 2, BigDecimal.ROUND_DOWN)).add(lateInterestSum).add(delayInterestSum);
		debtCredit.setCreditInterestAdvance(interestAdvance);
		debtCredit.setCreditInterestAdvanceWait(interestAdvance);
		capital = debtDetail.getLoanCapital();
		interest = debtDetail.getLoanInterest();
		total = capital.add(interest);
		// 计算真实的债权总额、本金、利息
		if (debtDetail.getRepayPeriod() == debtDetail.getBorrowPeriod()) {// 如果当前期是最后一期
			debtCredit.setCreditAccount(total);// 债转总额
			debtCredit.setCreditCapital(capital);// 债转总本金
			debtCredit.setLiquidatesCapital(capital);// 清算总本金
			debtCredit.setCreditInterest(interest);// 债转总利息
			debtCredit.setCreditAccountWait(total);// 待承接总金额
			debtCredit.setCreditCapitalWait(capital);// 待承接本金
			debtCredit.setCreditInterestWait(interest);// 待承接利息
			debtCredit.setRepayAccount(BigDecimal.ZERO);
			debtCredit.setRepayCapital(BigDecimal.ZERO);
			debtCredit.setRepayInterest(BigDecimal.ZERO);
			debtCredit.setRepayAccountWait(total);
			debtCredit.setRepayCapitalWait(capital);
			debtCredit.setRepayInterestWait(interest);
			debtCredit.setCreditRepayEndTime(debtDetail.getRepayTime());
		} else {// 如果当前期不是最后一期 则需要查询当前期到最后一期的债权信息
			List<DebtDetail> debtDetailsNoRepay = this.debtDetailCustomizeMapper.selectDebtDetailNoRepay(debtDetail
					.getOrderId());
			capital = new BigDecimal(0);
			interest = new BigDecimal(0);
			total = new BigDecimal(0);
			for (DebtDetail debtDetailNoRepay : debtDetailsNoRepay) {
				capital = capital.add(debtDetailNoRepay.getLoanCapital());
				// 是否是当前期之前 （有利息） ，还是之后 （没利息）
				if (debtDetailNoRepay.getRepayPeriod() < debtDetail.getRepayPeriod()) {
					interest = interest.add(debtDetailNoRepay.getLoanInterest());
				}
				if (debtDetailNoRepay.getRepayPeriod() == debtDetailNoRepay.getBorrowPeriod()
						|| debtDetailNoRepay.getRepayPeriod() == debtDetail.getRepayPeriod()) {
					// 最后一期或者不分期
					interest = debtDetail.getRepayInterestWait();
					debtCredit.setCreditRepayEndTime(debtDetailNoRepay.getRepayTime());
				}
				total = capital.add(interest);
			}
			debtCredit.setCreditAccount(total);// 债转总额
			debtCredit.setCreditCapital(capital);// 债转总本金
			debtCredit.setLiquidatesCapital(capital);// 清算总本金
			debtCredit.setCreditInterest(interest);// 债转总利息
			debtCredit.setCreditAccountWait(total);// 待承接总金额
			debtCredit.setCreditCapitalWait(capital);// 待承接本金
			debtCredit.setCreditInterestWait(interest);// 待承接利息
			debtCredit.setRepayAccount(BigDecimal.ZERO);
			debtCredit.setRepayCapital(BigDecimal.ZERO);
			debtCredit.setRepayInterest(BigDecimal.ZERO);
			debtCredit.setRepayAccountWait(total);
			debtCredit.setRepayCapitalWait(capital);
			debtCredit.setRepayInterestWait(interest);
		}
		debtCredit.setCreditDelayInterest(delayInterestSum);
		debtCredit.setCreditLateInterest(lateInterestSum);
		// 计算此时的公允价值
		debtCredit.setLiquidationFairValue(capital.add(interestAdvance));
	}

	private List<DebtRepay> searchRepay(String borrowNid) {
		DebtRepayExample debtRepayDetailExample = new DebtRepayExample();
		DebtRepayExample.Criteria cri = debtRepayDetailExample.createCriteria();
		cri.andBorrowNidEqualTo(borrowNid);
		List<DebtRepay> debtRepayDetails = debtRepayMapper.selectByExample(debtRepayDetailExample);
		return debtRepayDetails;
	}

	private List<DebtRepayDetail> searchRepayDetail(String borrowNid, Integer repayPeriod) {
		DebtRepayDetailExample debtRepayDetailExample = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria cri = debtRepayDetailExample.createCriteria();
		cri.andBorrowNidEqualTo(borrowNid);
		cri.andRepayPeriodEqualTo(repayPeriod);
		List<DebtRepayDetail> debtRepayDetails = debtRepayDetailMapper.selectByExample(debtRepayDetailExample);
		return debtRepayDetails;
	}

	@Override
	public List<DebtDetail> selectDebtDetail(String debtPlanNid) {
		DebtDetailExample example = new DebtDetailExample();
		example.createCriteria().andPlanNidEqualTo(debtPlanNid);
		return this.debtDetailMapper.selectByExample(example);
	}

	@Override
	public Integer queryDebtCreditCount(String debtPlanNid) {
		return this.debtCreditCustomizeMapper.queryDebtCreditCount(debtPlanNid);
	}

	@Override
	public List<DebtCreditCustomize> selectDebtCreditForPages(DebtCreditCustomize debtCreditCustomize) {
		return this.debtCreditCustomizeMapper.selectDebtCreditForPages(debtCreditCustomize);
	}

	@Override
	public DebtCreditCustomize selectDebtCreditForPagesSum(DebtCreditCustomize debtCreditCustomize) {
		return this.debtCreditCustomizeMapper.selectDebtCreditForPagesSum(debtCreditCustomize);
	}

	@Override
	public int queryFullBorrow(String planNid) {
		return this.debtBorrowCustomizeMapper.selectFullBorrow(planNid).size();
	}

	@Override
	public int updateDebtBorrowFullStatus(String borrowNid, BigDecimal account, BigDecimal loanFee, String planNid,
			Integer userId, String userName) {
		// 回改累计出借
		List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper
				.selectByExample(new CalculateInvestInterestExample());
		if (calculates != null && calculates.size() > 0) {
			CalculateInvestInterest calculateNew = new CalculateInvestInterest();
			calculateNew.setTenderSum(account);
			calculateNew.setId(calculates.get(0).getId());
			this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestSubByPrimaryKey(calculateNew);
		}
		// 回改关联信息
		DebtPlanBorrowExample planBorrowExample = new DebtPlanBorrowExample();
		DebtPlanBorrowExample.Criteria planBorrowCrt = planBorrowExample.createCriteria();
		planBorrowCrt.andDebtPlanNidEqualTo(planNid);
		planBorrowCrt.andBorrowNidEqualTo(borrowNid);
		List<DebtPlanBorrow> debtPlanBorrows = this.debtPlanBorrowMapper.selectByExample(planBorrowExample);
		if (debtPlanBorrows != null && debtPlanBorrows.size() > 0) {
			DebtPlanBorrow debtPlanBorrow = new DebtPlanBorrow();
			debtPlanBorrow.setDelFlag(1);
			debtPlanBorrow.setUpdateTime(GetDate.getNowTime10());
			debtPlanBorrow.setUpdateUserId(userId);
			debtPlanBorrow.setUpdateUserName(userName);
			this.debtPlanBorrowMapper.updateByExampleSelective(debtPlanBorrow, planBorrowExample);
		}
		// 回改标的信息
		DebtBorrowExample example = new DebtBorrowExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> debtBorrows = this.debtBorrowMapper.selectByExample(example);
		DebtBorrow record = debtBorrows.get(0);
		BigDecimal accuntyes = record.getBorrowAccountYes();
		BigDecimal accuntwait = record.getBorrowAccountWait();
		record.setBorrowFullStatus(0);
		record.setBorrowAccountYes(accuntyes.subtract(account));
		record.setBorrowAccountScale((accuntyes.subtract(account)).divide(record.getAccount(), 2, BigDecimal.ROUND_DOWN));
		record.setBorrowAccountWait(accuntwait.add(account));
		if (record.getBorrowService() != null) {
			record.setBorrowService(new BigDecimal(record.getBorrowService()).subtract(loanFee).toString());
		}
		record.setTenderTimes(record.getTenderTimes() - 1);
		return this.debtBorrowMapper.updateByPrimaryKey(record);
	}

}
