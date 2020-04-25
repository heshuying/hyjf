/**
 * Description:汇添金计划service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.plan;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.pandect.PandectService;
import com.hyjf.client.autoinvestsys.InvestSysUtils;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtFreezeLog;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanIntroduceCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanInvestDataCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanQuestionCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanRiskControlCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.user.invest.FreezeDefine;
import com.hyjf.web.user.invest.InvestController;
import com.hyjf.web.user.invest.InvestServiceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class PlanServiceImpl extends BaseServiceImpl implements PlanService {

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();
	@Autowired
	PandectService pandectService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/**
	 * 查询指定项目类型的项目列表
	 */
	@Override
	public List<DebtPlanCustomize> searchDebtPlanList(Map<String, Object> params) {
		List<DebtPlanCustomize> debtPlanList = this.debtPlanCustomizeMapper.selectDebtPlanList(params);
		return debtPlanList;
	}

	/**
	 * 查询指定项目类型的项目数目
	 */
	@Override
	public int queryDebtPlanRecordTotal(Map<String, Object> params) {
		int total = debtPlanCustomizeMapper.queryDebtPlanRecordTotal(params);
		return total;
	}

	/**
	 * 根据计划nid查询相应的计划详情
	 * 
	 * @param planNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlanDetailCustomize selectDebtPlanDetail(String planNid) {
		DebtPlanDetailCustomize debtPlanDetail = this.debtPlanCustomizeMapper.selectDebtPlanDetail(planNid);
		return debtPlanDetail;

	}

	/**
	 * 查询相应的计划介绍
	 * 
	 * @param planNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlanIntroduceCustomize selectDebtPlanIntroduce(String planNid) {
		DebtPlanIntroduceCustomize debtPlanIntroduce = this.debtPlanCustomizeMapper.selectDebtPlanIntroduce(planNid);
		return debtPlanIntroduce;

	}

	/**
	 * 查询相应的计划风控信息
	 * 
	 * @param planNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlanRiskControlCustomize selectDebtPlanRiskControl(String planNid) {
		DebtPlanRiskControlCustomize debtPlanRiskControl = this.debtPlanCustomizeMapper
				.selectDebtPlanRiskControl(planNid);
		return debtPlanRiskControl;

	}

	/**
	 * 查询相应的计划相关问题
	 * 
	 * @param planNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlanQuestionCustomize selectDebtPlanQuestion(String planNid) {
		DebtPlanQuestionCustomize debtPlanQuestion = this.debtPlanCustomizeMapper.selectDebtPlanQuestion(planNid);
		return debtPlanQuestion;

	}

	/**
	 * 查询用户的加入记录
	 * 
	 * @param planNid
	 * @param userId
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countUserAccede(String planNid, Integer userId) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andUserIdEqualTo(userId);
		int count = this.debtPlanAccedeMapper.countByExample(example);
		return count;
	}

	/**
	 * 根据相应的计划nid查询相应的计划
	 * 
	 * @param planNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlan selectDebtPlanByNid(String planNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria crt = example.createCriteria();
		crt.andDebtPlanNidEqualTo(planNid);
		List<DebtPlan> debtPlans = this.debtPlanMapper.selectByExample(example);
		if (debtPlans != null && debtPlans.size() == 1) {
			return debtPlans.get(0);
		}
		return null;

	}

	/**
	 * 统计计划的加入记录总数
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countPlanAccedeRecordTotal(Map<String, Object> params) {
		int count = this.debtPlanCustomizeMapper.countPlanAccedeRecordTotal(params);
		return count;

	}

	/**
	 * 查询计划的加入记录
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params) {
		List<DebtPlanAccedeCustomize> planAccedeList = this.debtPlanCustomizeMapper.selectPlanAccedeList(params);
		return planAccedeList;
	}

	/**
	 * 查询相应的计划标的记录总数
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countPlanBorrowRecordTotal(Map<String, Object> params) {
		int count = this.debtPlanCustomizeMapper.countPlanBorrowRecordTotal(params);
		return count;

	}

	/**
	 * 查询相应的计划标的列表
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtPlanBorrowCustomize> selectPlanBorrowList(Map<String, Object> params) {
		List<DebtPlanBorrowCustomize> planAccedeList = this.debtPlanCustomizeMapper.selectPlanBorrowList(params);
		return planAccedeList;

	}

	/**
	 * 查询相应的计划标的记录总数
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countPlanBorrowRecordTotalLast(Map<String, Object> params) {
		int count = this.debtPlanCustomizeMapper.countPlanBorrowRecordTotalLast(params);
		return count;

	}

	/**
	 * 查询相应的计划标的列表
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtPlanBorrowCustomize> selectPlanBorrowListLast(Map<String, Object> params) {
		List<DebtPlanBorrowCustomize> planAccedeList = this.debtPlanCustomizeMapper.selectPlanBorrowListLast(params);
		return planAccedeList;

	}

	@Override
	public Long selectPlanAccedeSum(Map<String, Object> params) {
		return debtPlanCustomizeMapper.selectPlanAccedeSum(params);
	}

	@Override
	public DebtPlan getPlanByNid(String planNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cri = example.createCriteria();
		cri.andDebtPlanNidEqualTo(planNid);
		List<DebtPlan> debtBorrowList = debtPlanMapper.selectByExample(example);
		if (debtBorrowList != null && debtBorrowList.size() > 0) {
			return debtBorrowList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 加入计划验证
	 */
	@Override
	public JSONObject checkParamPlan(String planNid, String money, Integer userId, String couponGrantId) {
		if (userId == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			Users user = this.getUsers(userId);
			// 判断用户信息是否存在
			if (user == null) {
				return jsonMessage("用户信息不存在", "1");
			} else {
				UsersInfoExample usersInfoExample = new UsersInfoExample();
				usersInfoExample.createCriteria().andUserIdEqualTo(userId);
				List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
				if (null != usersInfos && usersInfos.size() == 1) {
					String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
					if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
						if (usersInfos.get(0).getRoleId() != 1) {// 担保机构用户
							return jsonMessage("仅限出借人进行出借", "1");
						}
					}

				} else {
					return jsonMessage("账户信息异常", "1");
				}
				// 判断用户是否禁用
				if (user.getStatus() == 1) {// 0启用，1禁用
					return jsonMessage("该用户已被禁用", "1");
				} else {// 用户存在且用户未被禁用
					/**
					 * authType 授权方式：0完全授权，1部分授权 authStatus 预约授权状态：0：未授权1：已授权
					 * authTime 授权操作时间 recodTotal 违约累计积分 recodTime 违约更新时间
					 * recodTruncateTime 积分清空时间
					 */
					Map<String, Object> map = webUserInvestListCustomizeMapper.selectUserAppointmentInfo(userId + "");
					
					if (map == null || map.isEmpty()) {
						return jsonMessage("您未进行授权  <a href='" + HOST
								+ "/user/pandect/appointment.do?appointment=1'>去授权 </a> ", "1");
					} else {
						if (map.get("authStatus") == null || map.get("authStatus").toString().equals("0")) {
							return jsonMessage("您未进行授权  <a href='" + HOST
									+ "/user/pandect/appointment.do?appointment=1'>去授权 </a> ", "1");
						} else {
							// 预约接口查询
							Map<String, Object> appointmentMap = pandectService.checkAppointmentStatus(userId, "1");
							boolean appointmentFlag = (boolean) appointmentMap.get("appointmentFlag");
							String isError = appointmentMap.get("isError").toString();
							if (appointmentFlag && isError.equals("0")) {
								return jsonMessage("您状态不同步请关闭授权 后重新授权, <a href='" + HOST
										+ "/user/pandect/appointment.do?appointment=0'>关闭授权授权 </a> ", "1");
							}

						}
					}
					DebtPlan plan = this.getPlanByNid(planNid);
					if (plan.getDebtPlanStatus() != 4) {
						return jsonMessage("此项目已经募集完成", "1");
					}
					if (plan.getBuyEndTime() < GetDate.getNowTime10()) {
						return jsonMessage("此项目已经募集已结束", "1");
					}
					AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(userId);
					// 用户未在平台开户
					if (accountChinapnrTender == null) {
						return jsonMessage("用户开户信息不存在", "1");
					} else {
						// 判断借款人开户信息是否存在
						if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
							return jsonMessage("用户汇付客户号不存在", "1");
						} else {
							// 判断借款编号是否存在
							if (StringUtils.isEmpty(planNid)) {
								return jsonMessage("计划项目不存在", "1");
							} else {
								// 判断借款信息是否存在
								if (plan == null || plan.getId() == null) {
									return jsonMessage("计划项目不存在", "1");
								} else {
									// 判断借款是否流标
									if (plan.getDebtPlanStatus() == 11) { // 流标
										return jsonMessage("此计划已经流标", "1");
									} else {
										// 已满标
										if (plan.getDebtPlanStatus() == 5) {
											return jsonMessage("此项目已经满标", "1");
										} else {
											CouponConfigCustomizeV2 cuc = this.getCouponUser(couponGrantId, userId);
											// 判断用户出借金额是否为空
											if (!(StringUtils.isNotEmpty(money)
													|| (StringUtils.isEmpty(money) && cuc != null && cuc
															.getCouponType() == 3) || (StringUtils.isEmpty(money)
													&& cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1))) {
												return jsonMessage("请输入加入金额", "1");
											} else {
												// 还款金额是否数值
												try {
													// 出借金额必须是整数
													if (StringUtils.isEmpty(money)) {
														money = "0";
													}
													Long accountInt = Long.parseLong(money);
													if (accountInt < 0) {
														return jsonMessage("加入金额不能为负数", "1");
													} else {
														if ((accountInt == 0 && cuc == null)
																|| (accountInt == 0 && cuc != null && cuc
																		.getCouponType() == 2)) {
															return jsonMessage("出借金额不能为0元", "1");
														}
														if (accountInt != 0 && cuc != null && cuc.getCouponType() == 1
																&& cuc.getAddFlg() == 1) {
															return jsonMessage("该优惠券只能单独使用", "1");
														}
														BigDecimal accountBigDecimal = new BigDecimal(money);
														String balance = RedisUtils.get(planNid);
														if (StringUtils.isEmpty(balance)) {
															return jsonMessage("您来晚了，下次再来抢吧", "1");
														} else {
															// 剩余可投金额
															BigDecimal min = plan.getDebtMinInvestment();
															// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
															if (min != null && min.compareTo(BigDecimal.ZERO) != 0
																	&& new BigDecimal(balance).compareTo(min) == -1) {
																if (accountBigDecimal
																		.compareTo(new BigDecimal(balance)) == 1) {
																	return jsonMessage("剩余可加入金额为" + balance + "元", "1");
																}
																if (accountBigDecimal
																		.compareTo(new BigDecimal(balance)) != 0) {
																	return jsonMessage("剩余可加入只剩" + balance + "元，须全部购买",
																			"1");
																}
															} else {// 项目的剩余金额大于最低起投金额
																if (accountBigDecimal.compareTo(min) == -1) {
																	if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
																		if (cuc != null && cuc.getCouponType() != 3
																				&& cuc.getCouponType() != 1) {
																			return jsonMessage(
																					plan.getDebtMinInvestment() + "元起投",
																					"1");
																		}
																	} else {
																		return jsonMessage(plan.getDebtMinInvestment()
																				+ "元起投", "1");
																	}
																} else {
																	BigDecimal max = plan.getDebtMaxInvestment();
																	if (max != null
																			&& max.compareTo(BigDecimal.ZERO) != 0
																			&& accountBigDecimal.compareTo(max) == 1) {
																		return jsonMessage("项目最大加入额为" + max + "元", "1");
																	}
																}
															}
															if (accountBigDecimal.compareTo(plan.getDebtPlanMoney()) > 0) {
																return jsonMessage("加入金额不能大于项目总额", "1");
															} else {
																Account tenderAccount = this.getAccount(userId);
																if (tenderAccount.getBalance().compareTo(
																		accountBigDecimal) < 0) {
																	return jsonMessage("余额不足，请充值！", "1");
																}
																if (StringUtils.isEmpty(balance)) {
																	return jsonMessage("您来晚了，下次再来抢吧", "1");
																} else {
																	if (StringUtils.isNotEmpty(balance)) {
																		// redis剩余金额不足
																		if (accountBigDecimal.compareTo(new BigDecimal(
																				balance)) == 1) {
																			return jsonMessage("项目太抢手了！剩余可加入金额只有"
																					+ balance + "元", "1");
																		} else {
																			if (plan.getDebtInvestmentIncrement() != null
																					&& (accountInt - min.longValue())
																							% plan.getDebtInvestmentIncrement()
																									.longValue() != 0
																					&& accountBigDecimal
																							.compareTo(new BigDecimal(
																									balance)) == -1) {
																				return jsonMessage(
																						"加入递增金额须为"
																								+ plan.getDebtInvestmentIncrement()
																								+ " 元的整数倍", "1");
																			}
																			// 如果验证没问题，则返回出借人借款人的汇付账号
																			Long tenderUsrcustid = accountChinapnrTender
																					.getChinapnrUsrcustid();
																			JSONObject jsonMessage = new JSONObject();
																			jsonMessage.put("error", "0");
																			jsonMessage.put("tenderUsrcustid",
																					tenderUsrcustid);
																			jsonMessage.put("planId", plan.getId());
																			return jsonMessage;

																		}
																	} else {
																		return jsonMessage("您来晚了，下次再来抢吧", "1");
																	}
																}
															}
														}
													}

												} catch (Exception e) {
													return jsonMessage("加入金额必须为整数", "1");
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

	public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		return ccTemp;
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

	@Override
	public Boolean updateBeforeChinaPnR(String planNid, String orderId, Integer userId, String account,
			String tenderUsrcustid, String orderDate) {
		Users user = getUsers(userId);
		DebtFreezeLog debtFreezeLog = new DebtFreezeLog();
		debtFreezeLog.setAmount(new BigDecimal(account));
		debtFreezeLog.setUserId(userId);
		debtFreezeLog.setUserName(user.getUsername());
		debtFreezeLog.setUserCustId(tenderUsrcustid);
		debtFreezeLog.setPlanNid(planNid);
		debtFreezeLog.setPlanOrderId(orderId);// 因为是临时冻结记录表暂时插入冻结的订单号，后续冻结表会更新为加入的订单号
		debtFreezeLog.setOrderId(orderId);
		debtFreezeLog.setOrderDate(orderDate);
		debtFreezeLog.setTrxId(orderId);// 因为是临时冻结记录表暂时插入冻结的订单号，后续冻结表会更新为冻结返回的标识
		debtFreezeLog.setStatus(0);// 冻结订单状态 0冻结 1解冻
		debtFreezeLog.setFreezeType(1);// 冻结类型 0出借冻结 1汇添金冻结
		debtFreezeLog.setDelFlag(0);// 是否有效 0有效 1无效记录
		debtFreezeLog.setCreateTime(GetDate.getNowTime10());
		debtFreezeLog.setCreateUserId(userId);
		debtFreezeLog.setCreateUserName(user.getUsername());
		Boolean flag1 = debtFreezeLogMapper.insertSelective(debtFreezeLog) > 0 ? true : false;
		return flag1;

	}

	/**
	 * 加入计划冻结
	 */
	@Override
	public FreezeDefine freeze(Integer userId, String account, String tenderUsrcustid, String frzzeOrderId,
			String frzzeOrderDate) {
		FreezeDefine freezeDefine = new FreezeDefine();
		Properties properties = PropUtils.getSystemResourcesProperties(); // 商户后台应答地址(必须)
		String bgRetUrl = properties.getProperty("hyjf.chinapnr.callbackurl").trim();
		ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setVersion("10");// 接口版本号
		chinapnrBean.setCmdId("UsrFreezeBg"); // 消息类型(冻结)
		chinapnrBean.setUsrCustId(tenderUsrcustid);// 出借用户客户号
		chinapnrBean.setOrdId(frzzeOrderId); // 订单号(必须)
		chinapnrBean.setOrdDate(frzzeOrderDate);// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account));// 交易金额(必须)
		chinapnrBean.setRetUrl(""); // 页面返回
		chinapnrBean.setBgRetUrl(bgRetUrl); // 商户后台应答地址(必须)
		chinapnrBean.setType("user_freeze"); // 日志类型
		chinapnrBean.setLogUserId(userId);
		// chinapnrBean.setIsFreeze("N");
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (bean != null) {
			String respCode = bean.getRespCode();
			freezeDefine.setFreezeTrxId(bean.getTrxId());
			System.out.println("用户:" + userId + "***********************************加入计划冻结订单号：" + bean.getTrxId());
			if (StringUtils.isNotEmpty(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
				freezeDefine.setFreezeFlag(true);
			} else {
				System.out.println("用户:" + userId + "***********************************加入计划冻结失败错误码：" + respCode);
				freezeDefine.setFreezeFlag(false);
			}
			return freezeDefine;
		} else {
			return null;
		}
	}

	@Override
	public void recoverRedis(String planNid, Integer userId, String account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
		String balanceLast = RedisUtils.get(planNid);
		if (StringUtils.isNotBlank(balanceLast)) {
			while ("OK".equals(jedis.watch(planNid))) {
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction tx = jedis.multi();
				tx.set(planNid, recoverAccount + "");
				List<Object> result = tx.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					System.out.println("加入计划用户:" + userId + "***********************************from redis恢复redis："
							+ account);
					break;
				}
			}
		}
	}

	@Override
	public boolean updateAfterPlanRedis(String planNid, String frzzeOrderId, Integer userId, String accountStr,
			String tenderUsrcustid, int client, String ipAddr, String freezeTrxId, String frzzeOrderDate,
			String planOrderId, String couponGrantId, ModelAndView modelAndView,String couponInterest) throws Exception {

		int nowTime = GetDate.getNowTime10();
		Users user = getUsers(userId);
		UsersInfo userInfo = getUsersInfoByUserId(userId);
		DebtPlan debtPlan = getPlanByNid(planNid);
		BigDecimal planApr = debtPlan.getExpectApr();
		// 周期
		Integer planPeriod = debtPlan.getDebtLockPeriod();
		// 计算历史回报
		BigDecimal earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(accountStr),
				planApr.divide(new BigDecimal("100")), planPeriod);
	
			// 当大于等于100时 取百位 小于100 时 取十位
			BigDecimal accountDecimal = new BigDecimal(accountStr);
			BigDecimal maxInvestNumber = new BigDecimal(debtPlan.getMaxInvestNumber());
			BigDecimal minInvestNumber = new BigDecimal(debtPlan.getMinInvestNumber());
			BigDecimal investMaxTmp = accountDecimal.divide(maxInvestNumber, 2, BigDecimal.ROUND_DOWN);
			BigDecimal investMinTmp = accountDecimal.divide(minInvestNumber, 2, BigDecimal.ROUND_UP);
			BigDecimal investMax = BigDecimal.ZERO;
			if (investMaxTmp.compareTo(new BigDecimal("100")) >= 0) {
				investMax = ((investMaxTmp).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_DOWN))
						.multiply(new BigDecimal("100"));
			} else {
				investMax = ((investMaxTmp).divide(new BigDecimal("10"), 0, BigDecimal.ROUND_DOWN))
						.multiply(new BigDecimal("10"));
			}
			BigDecimal investMin = ((investMinTmp).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_UP))
					.multiply(new BigDecimal("100"));

			DebtPlanAccede debtPlanAccede = new DebtPlanAccede();
			debtPlanAccede.setPlanNid(planNid);
			debtPlanAccede.setUserId(userId);
			debtPlanAccede.setUserName(user.getUsername());
			debtPlanAccede.setAccedeOrderId(planOrderId);
			debtPlanAccede.setFreezeOrderId(frzzeOrderId);
			debtPlanAccede.setAccedeAccount(accountDecimal);
			debtPlanAccede.setAccedeBalance(accountDecimal);// 加入金额 实际为冻结
			debtPlanAccede.setAccedeFrost(BigDecimal.ZERO);
			debtPlanAccede.setUnderTakeTimes(0);
			debtPlanAccede.setInvestMax(investMin);// 除最大笔数得到最小的出借金额
			debtPlanAccede.setInvestMin(investMax);// 除最小笔数得到最大的出借金额
			debtPlanAccede.setCycleTimes(0);
			debtPlanAccede.setRepayAccount(accountDecimal.add(earnings));
			debtPlanAccede.setRepayCapital(accountDecimal);
			debtPlanAccede.setRepayInterest(earnings);
			debtPlanAccede.setRepayAccountWait(accountDecimal.add(earnings));
			debtPlanAccede.setRepayCapitalWait(accountDecimal);
			debtPlanAccede.setRepayInterestWait(earnings);
			debtPlanAccede.setRepayAccountYes(BigDecimal.ZERO);
			debtPlanAccede.setRepayCapitalYes(BigDecimal.ZERO);
			debtPlanAccede.setRepayInterestYes(BigDecimal.ZERO);
			debtPlanAccede.setStatus(0);// 此笔加入是否已经完成 0出借中 1出借完成 2还款中 3还款完成
			debtPlanAccede.setDelFlag(0);
			debtPlanAccede.setSendStatus(0);// 协议是否已经发送 0未发送 1已发送
			debtPlanAccede.setClient(client);
			debtPlanAccede.setCreateTime(nowTime);
			debtPlanAccede.setCreateUserId(userId);
			debtPlanAccede.setCreateUserName(user.getUsername());
			// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工

			Integer refferId = null;
			String refferName = null;
			Integer attribute = null;
			if (Validator.isNotNull(userInfo)) {
				// 获取出借用户的用户属性
				attribute = userInfo.getAttribute();
				if (Validator.isNotNull(attribute)) {
					// 出借人用户属性
					debtPlanAccede.setUserAttribute(attribute);
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (attribute == 2 || attribute == 3) {
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (Validator.isNotNull(employeeCustomize)) {
							debtPlanAccede.setInviteRegionId(employeeCustomize.getRegionId());
							debtPlanAccede.setInviteRegionName(employeeCustomize.getRegionName());
							debtPlanAccede.setInviteBranchId(employeeCustomize.getBranchId());
							debtPlanAccede.setInviteBranchName(employeeCustomize.getBranchName());
							debtPlanAccede.setInviteDepartmentId(employeeCustomize.getDepartmentId());
							debtPlanAccede.setInviteDepartmentName(employeeCustomize.getDepartmentName());
						}
					} else if (attribute == 1) {
						SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
						SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
						spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
						List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
						if (sList != null && sList.size() == 1) {
							int refUserId = sList.get(0).getSpreadsUserid();
							// 查找用户推荐人
							Users refererUser = getUsers(refUserId);
							if (Validator.isNotNull(refererUser)) {
								refferId = refererUser.getUserId();
								refferName = refererUser.getUsername();
								debtPlanAccede.setInviteUserId(refererUser.getUserId());
								debtPlanAccede.setInviteUserName(refererUser.getUsername());
							}
							// 推荐人信息
							UsersInfo refererUserInfo = getUsersInfoByUserId(refUserId);
							// 推荐人用户属性
							if (Validator.isNotNull(refererUserInfo)) {
								debtPlanAccede.setInviteUserAttribute(refererUserInfo.getAttribute());
							}
							// 查找用户推荐人部门
							EmployeeCustomize employeeCustomize = employeeCustomizeMapper
									.selectEmployeeByUserId(refUserId);
							if (Validator.isNotNull(employeeCustomize)) {
								debtPlanAccede.setInviteRegionId(employeeCustomize.getRegionId());
								debtPlanAccede.setInviteRegionName(employeeCustomize.getRegionName());
								debtPlanAccede.setInviteBranchId(employeeCustomize.getBranchId());
								debtPlanAccede.setInviteBranchName(employeeCustomize.getBranchName());
								debtPlanAccede.setInviteDepartmentId(employeeCustomize.getDepartmentId());
								debtPlanAccede.setInviteDepartmentName(employeeCustomize.getDepartmentName());
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
							Users refererUser = getUsers(refUserId);
							if (Validator.isNotNull(refererUser)) {
								refferId = refererUser.getUserId();
								refferName = refererUser.getUsername();
								debtPlanAccede.setInviteUserId(refererUser.getUserId());
								debtPlanAccede.setInviteUserName(refererUser.getUsername());
							}
							// 推荐人信息
							UsersInfo refererUserInfo = getUsersInfoByUserId(refUserId);
							// 推荐人用户属性
							if (Validator.isNotNull(refererUserInfo)) {
								debtPlanAccede.setInviteUserAttribute(refererUserInfo.getAttribute());
							}
						}
					}
				}
			}
			System.out.println("计划加入用户:" + userId + "***********************************插入DebtPlanAccede："
					+ JSON.toJSONString(debtPlanAccede));
			boolean trenderFlag = debtPlanAccedeMapper.insertSelective(debtPlanAccede) > 0 ? true : false;
			// add by pcc 汇添金使用优惠券出借 start
			if (StringUtils.isNotEmpty(couponGrantId)) {

				// 优惠券出借校验
				try {
					Map<String, String> validateMap = this.validateCoupon(userId, accountStr, couponGrantId, planNid,
							CustomConstants.CLIENT_PC);
					LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借校验结果："
							+ validateMap.get("statusDesc"));
					System.out.println("updateCouponTender" + "优惠券出借校验结果：" + validateMap.get("statusDesc"));
					if (MapUtils.isEmpty(validateMap)) {
						// 校验通过 进行出借
						this.updateCouponTender(couponGrantId, planNid, userId, accountStr, ipAddr, new Integer(
								frzzeOrderDate), planOrderId, modelAndView,couponInterest);
					}
				} catch (Exception e) {
					LogUtil.infoLog(InvestController.class.getName(), "tenderRetUrl", "优惠券出借失败");
				}
			}
			// add by pcc 汇添金使用优惠券出借 end
			if (trenderFlag) {
				try {
					// 调用crm同步加入接口
					String debtinfo = JSONObject.toJSONString(debtPlanAccede);
					InvestSysUtils.insertInvestSys(debtinfo);
				} catch (Exception e) {
					System.out.println("专属标加入 crm同步accoced表失败");
					e.printStackTrace();
				}
				// 新出借金额汇总
				AccountExample example = new AccountExample();
				AccountExample.Criteria criteria = example.createCriteria();
				criteria.andUserIdEqualTo(userId);
				List<Account> list = accountMapper.selectByExample(example);
				if (list != null && list.size() == 1) {
					// 可用余额
					BigDecimal balance = list.get(0).getBalance();
					// 冻结金额
					BigDecimal frost = list.get(0).getFrost();
					// 总金额
					BigDecimal total = list.get(0).getTotal();
					// 更新用户账户余额表
					Account investAccount = new Account();
					// 承接用户id
					investAccount.setUserId(userId);
					// 计划加入总金额
					investAccount.setPlanAccedeTotal(accountDecimal);
					// 计划可用余额
					investAccount.setPlanBalance(accountDecimal);
				
					// 计划累计待收总额
					investAccount.setPlanInterestWait(earnings);
					// 计划累计待收本金
					investAccount.setPlanCapitalWait(accountDecimal);
					// 计划待收利息
					investAccount.setPlanAccountWait(accountDecimal.add(earnings));
					// 更新用户计划账户
					boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanJoin(investAccount) > 0 ? true
							: false;
					// 插入account_list表
					if (accountFlag) {
						Account accedeAccount = this.selectUserAccount(userId);
						AccountList accountList = new AccountList();
						accountList.setNid(planOrderId);
						accountList.setUserId(userId);
						accountList.setAmount(accountDecimal);
						accountList.setFrost(frost);
						accountList.setBalance(accedeAccount.getBalance());
						accountList.setInterest(new BigDecimal(0));
						accountList.setAwait(accedeAccount.getAwait());
						accountList.setPlanBalance(accedeAccount.getPlanBalance());
						accountList.setPlanFrost(accedeAccount.getPlanFrost());
						accountList.setIp(ipAddr);
						accountList.setIsUpdate(0);
						accountList.setOperator(userId + "");
						accountList.setRemark(planNid);
						accountList.setRepay(new BigDecimal(0));
						accountList.setTotal(total);
						accountList.setTrade("accede_plan");// 计划加入
						accountList.setTradeCode("balance");//
						accountList.setType(2);// 收支类型1收入2支出3冻结
						accountList.setWeb(0);
						accountList.setBaseUpdate(0);
						accountList.setCreateTime(nowTime);
						System.out.println("加入计划用户:" + userId + "***********************************预插入accountList："
								+ JSON.toJSONString(accountList));
						boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
						if (accountListFlag) {
							// 插入相应的出让人汇添金资金明细表
							DebtAccountList debtAccountList = new DebtAccountList();
							debtAccountList.setNid(frzzeOrderId);
							debtAccountList.setUserId(userId);
							debtAccountList.setUserName(user.getUsername());
							debtAccountList.setPlanNid(planNid);
							debtAccountList.setPlanOrderId(planOrderId);
							debtAccountList.setBalance(balance.subtract(accountDecimal));
							debtAccountList.setFrost(frost);
							debtAccountList.setPlanBalance(accedeAccount.getPlanBalance());
							debtAccountList.setPlanFrost(accedeAccount.getPlanFrost());
							debtAccountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance());
							debtAccountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost());
							debtAccountList.setAmount(accountDecimal);
							debtAccountList.setAccountWait(accedeAccount.getAwait());
							debtAccountList.setRepayWait(accedeAccount.getRepay());
							debtAccountList.setInterestWait(BigDecimal.ZERO);
							debtAccountList.setCapitalWait(BigDecimal.ZERO);
							debtAccountList.setType(2);
							debtAccountList.setTrade("accede_plan");
							debtAccountList.setTradeCode("balance");
							debtAccountList.setTotal(total);
							debtAccountList.setRemark(planOrderId);
							debtAccountList.setCreateTime(nowTime);
							debtAccountList.setWeb(0);
							debtAccountList.setCreateUserId(userId);
							debtAccountList.setCreateUserName(user.getUsername());
							debtAccountList.setRefererUserId(refferId);
							debtAccountList.setRefererUserName(refferName);
							System.out.println("计划加入用户:" + userId
									+ "***********************************预插入debtAccountList："
									+ JSON.toJSONString(debtAccountList));
							// 插入交易明细
							boolean debtAccountListFlag = this.debtAccountListMapper.insertSelective(debtAccountList) > 0 ? true
									: false;
							if (debtAccountListFlag) {
								// 更新plan表
								Map<String, Object> plan = new HashMap<String, Object>();
								plan.put("account", accountDecimal);
								plan.put("planId", debtPlan.getId());
								boolean updateBorrowAccountFlag = debtPlanCustomizeMapper.updateByDebtPlanId(plan) > 0 ? true
										: false;
								if (updateBorrowAccountFlag) {
									// 更新渠道统计用户累计出借
									// 出借人信息
									Users users = getUsers(userId);
									if (users != null) {
										// 更新渠道统计用户累计出借
										AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
										AppChannelStatisticsDetailExample.Criteria crt = channelExample
												.createCriteria();
										crt.andUserIdEqualTo(userId);
										List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper
												.selectByExample(channelExample);
										if (appChannelStatisticsDetails != null
												&& appChannelStatisticsDetails.size() == 1) {
											AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails
													.get(0);
											Map<String, Object> params = new HashMap<String, Object>();
											params.put("id", channelDetail.getId());
											// 认购本金
											params.put("accountDecimal", accountDecimal);
											// 出借时间
											params.put("investTime", nowTime);
											// 项目类型
											params.put("projectType", "汇添金");
											// 首次投标项目期限
											String investProjectPeriod = debtPlan.getDebtLockPeriod() + "个月";
											params.put("investProjectPeriod", investProjectPeriod);
											// 更新渠道统计用户累计出借
											if (users.getInvestflag() == 1) {
												this.appChannelStatisticsDetailCustomizeMapper
														.updateAppChannelStatisticsDetail(params);
											} else if (users.getInvestflag() == 0) {
												// 更新首投出借
												this.appChannelStatisticsDetailCustomizeMapper
														.updateFirstAppChannelStatisticsDetail(params);
											}
											System.out
													.println("用户:"
															+ userId
															+ "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号："
															+ debtPlanAccede.getAccedeOrderId());
										}else{
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
												params.put("projectType", "汇添金");
												// 首次投标项目期限
												String investProjectPeriod = debtPlan.getDebtLockPeriod() + "个月";
												// 首次投标项目期限
												params.put("investProjectPeriod", investProjectPeriod);
												// 更新渠道统计用户累计出借
												if (users.getInvestflag() == 0) {
													// 更新huiyingdai_utm_reg的首投信息
													this.appChannelStatisticsDetailCustomizeMapper
															.updateFirstUtmReg(params);
													System.out
															.println("用户:"
																	+ userId
																	+ "***********************************预更新渠道统计表huiyingdai_utm_reg的首投信息，订单号："
																	+ debtPlanAccede.getAccedeOrderId());
												}
											}
										}
									}
									
									// 出借人信息:更新新手标志位
									users = getUsers(userId);
									if (users != null) {
										if (users.getInvestflag() == 0) {
											users.setInvestflag(1);
											this.usersMapper.updateByPrimaryKeySelective(users);
										}
									}
									// 计算此时的剩余可出借金额
									BigDecimal accountWait = debtPlan.getDebtPlanMoneyWait().subtract(accountDecimal);
									// 满标处理
									if (accountWait.compareTo(new BigDecimal(0)) == 0) {
										System.out.println("计划加入用户:" + userId
												+ "***********************************项目满标");
										Map<String, Object> planFull = new HashMap<String, Object>();
										planFull.put("planId", debtPlan.getId());
										debtPlanCustomizeMapper.updateOfFullPlan(planFull);
										// 纯发短信接口
										Map<String, String> replaceMap = new HashMap<String, String>();
										replaceMap.put("val_htj_title", planNid);
										replaceMap.put("val_time", DateUtils.getNowDate());
										DebtPlanAccedeExample debtPlanAccedeExample = new DebtPlanAccedeExample();
										DebtPlanAccedeExample.Criteria cri = debtPlanAccedeExample.createCriteria();
										cri.andPlanNidEqualTo(planNid);
										cri.andDelFlagEqualTo(0);
										List<DebtPlanAccede> debtPlanAccedeList = debtPlanAccedeMapper
												.selectByExample(debtPlanAccedeExample);
										if (debtPlanAccedeList != null && debtPlanAccedeList.size() > 0) {
											for (int i = 0; i < debtPlanAccedeList.size(); i++) {
												DebtPlanAccede debtPlanAccedeTmp = debtPlanAccedeList.get(i);
												UsersInfo usersInfo = this.getUsersInfoByUserId(debtPlanAccedeTmp
														.getUserId());
												replaceMap.put("val_name", usersInfo.getTruename().substring(0, 1));
												if (usersInfo.getSex() == 1) {
													replaceMap.put("val_sex", "先生");
												} else {
													replaceMap.put("val_sex", "女士");
												}
												replaceMap.put("val_amount", debtPlanAccedeTmp.getAccedeAccount()
														.toString());
												DecimalFormat df = CustomConstants.DF_FOR_VIEW;
												df.setRoundingMode(RoundingMode.FLOOR);
												replaceMap.put("val_profit",
														df.format(debtPlanAccedeTmp.getRepayInterest()));
												DebtPlan debtPlan1 = getPlanByNid(planNid);
												replaceMap.put("val_date", GetDate
														.timestamptoStrYYYYMMDDHHMMSS(debtPlan1
																.getLiquidateShouldTime()
																+ ((debtPlan1.getDebtQuitPeriod() - 1) * 24 * 3600)));
												// 发送短信验证码
												SmsMessage smsMessage = new SmsMessage(debtPlanAccedeTmp.getUserId(),
														replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null,
														CustomConstants.HTJ_PARAM_TPL_XMMB,
														CustomConstants.CHANNEL_TYPE_NORMAL);
												smsProcesser.gather(smsMessage);
												System.out.println("汇添金专属标 发送用户锁定中短信，用户："
														+ debtPlanAccedeTmp.getUserId() + "  订单号:" + planOrderId);
												this.sendMail(debtPlanAccedeTmp.getAccedeOrderId(),
														debtPlanAccedeTmp.getUserId(), debtPlan1);
											}

										}
										replaceMap.put("val_date", debtPlan.getDebtLockPeriod() + "个月");
										// 发送短信验证码
										SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null,
												MessageDefine.SMSSENDFORMANAGER, null,
												CustomConstants.HTJ_PARAM_TPL_JHMB, CustomConstants.CHANNEL_TYPE_NORMAL);
										smsProcesser.gather(smsMessage);
										// 取得是否线上
										String online = "生产环境";
										String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
										if (payUrl == null || !payUrl.contains("online")) {
											online = "测试环境";
										}
										String[] toMail = new String[] {};
										if ("测试环境".equals(online)) {
											toMail = new String[] { "jiangying@hyjf.com",
													"liudandan@hyjf.com" };
										} else {
											toMail = new String[] { "wangkun@hyjf.com",
													"gaohonggang@hyjf.com", "sunjianhua@hyjf.com" };
										}
										MailMessage message = new MailMessage(null, replaceMap, "计划满标", null, null,
												toMail, CustomConstants.HTJ_PARAM_TPL_JHMB,
												MessageDefine.MAILSENDFORMAILINGADDRESS);
										mailMessageProcesser.gather(message);
                                        // add by pcc 计算汇添金优惠券的应还款时间 start
                                        CommonSoaUtils.planCouponRecover(planNid);
										// add by pcc 计算汇添金优惠券的应还款时间 end

									} else if (accountWait.compareTo(new BigDecimal(0)) < 0) {
										System.out.println("智投加入用户:" + userId + "智投编号:" + planNid
												+ "***********************************智投加入暴标");
										throw new RuntimeException("没有可加入金额");
									}
									return true;
								} else {
									throw new RuntimeException("borrow表更新失败");
								}
							} else {
								throw new RuntimeException("用户账户计划交易明细表更新失败");
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
				throw new RuntimeException("插入预约表失败！");
			}
	}

	/**
	 * 优惠券出借校验
	 * 
	 * @param userId
	 * 
	 * @param account
	 * @param couponType
	 * @param borrowNid
	 * @return
	 */
	private Map<String, String> validateCoupon(Integer userId, String account, String couponGrantId, String planNid,
			String platform) {

        JSONObject jsonObject = CommonSoaUtils.planCheckCoupon(userId + "", planNid, account, platform, couponGrantId);
        int status = jsonObject.getIntValue("status");
        String statusDesc = jsonObject.getString("statusDesc");
        Map<String, String> paramMap = new HashMap<String, String>();
        if (status == 1) {
            paramMap.put(CustomConstants.APP_STATUS_DESC, statusDesc);
        }

		return paramMap;
	}

	private void sendMail(String accedeOrderId, Integer userId, DebtPlan debtPlan1) {
		try {
			Map<String, String> msg = new HashMap<String, String>();
			// 向每个出借人发送邮件
			if (Validator.isNotNull(accedeOrderId) && Validator.isNotNull(userId) && Validator.isNotNull(debtPlan1)) {
				Map<String, Object> contents = new HashMap<String, Object>();
				if (debtPlan1.getFullExpireTime() != null) {
					contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan1.getFullExpireTime()));
				} else {
					contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan1.getBuyEndTime()));
				}
				contents.put("debtPlan", debtPlan1);
				// 1基本信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("accedeOrderId", accedeOrderId);
				params.put("userId", userId);
				UsersInfo userInfoNew = getUsersInfoByUserId(userId);
				contents.put("userInfo", userInfoNew);

				PlanCommonCustomize planCommonCustomize = new PlanCommonCustomize();
				if (params.get("userId") != null) {
					planCommonCustomize.setUserId(params.get("userId") + "");
				}
				if (params.get("accedeOrderId") != null) {
					planCommonCustomize.setAccedeOrderId(params.get("accedeOrderId") + "");
				}
				List<PlanLockCustomize> recordList = planLockCustomizeMapper.selectPlanAccedeList(planCommonCustomize);

				if (recordList != null && recordList.size() > 0) {
					PlanLockCustomize planinfo = recordList.get(0);
					contents.put("planinfo", planinfo);
				}
				Users users = this.getUsers(Integer.valueOf(userId));
				if (users == null || Validator.isNull(users.getEmail())) {
					return;
				}
				UsersInfo usersinfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (usersinfo == null || Validator.isNull(usersinfo.getTruename())) {
					return;
				}
				String email = users.getEmail();

				msg.put(VAL_NAME, usersinfo.getTruename());
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() % 2 == 0) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				String fileName = debtPlan1.getDebtPlanNid() + "_" + accedeOrderId + ".pdf";
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "Plan_"
						+ GetDate.getMillis() + StringPool.FORWARD_SLASH;
				String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.HTJ_TENDER_CONTRACT, contents);
				if (StringUtils.isNotEmpty(pdfUrl)) {
					File path = new File(filePath);
					if (!path.exists()) {
						path.mkdirs();
					}
					FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
				}
				String[] emails = { email };
				MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇添金出借计划服务协议", null,
						new String[] { filePath + fileName }, emails, CustomConstants.EMAILPARAM_JYTZ_HTJ_DEAL,
						MessageDefine.MAILSENDFORMAILINGADDRESS);
				mailMessageProcesser.gather(message);
				DebtPlanAccedeExample example = new DebtPlanAccedeExample();
				DebtPlanAccedeExample.Criteria cra = example.createCriteria();
				cra.andPlanNidEqualTo(debtPlan1.getDebtPlanNid());
				cra.andAccedeOrderIdEqualTo(accedeOrderId);
				List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(example);
				if (list != null && list.size() > 0) {
					DebtPlanAccede record = list.get(0);
					record.setSendStatus(1);
					boolean updateFlag = this.debtPlanAccedeMapper.updateByPrimaryKeySelective(record) > 0 ? true
							: false;
					if (!updateFlag) {
						throw new Exception("计划加入表(hyjf_debt_accede)更新失败!" + "[加入订单号:" + accedeOrderId + "]");
					}
				} else {
					throw new Exception("计划加入表(hyjf_debt_accede)查询失败!" + "[加入订单号:" + accedeOrderId + "]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 
	 * @method: countPlanBorrowRecordTotalCredit
	 * @description: 包括债转的债权列表总数
	 * @param params
	 * @return
	 * @return: int
	 * @mender: zhouxiaoshuai
	 * @date: 2016年11月25日 下午3:48:17
	 */
	@Override
	public int countPlanBorrowRecordTotalCredit(Map<String, Object> params) {
		int count = this.debtPlanCustomizeMapper.countPlanBorrowRecordTotalCredit(params);
		return count;

	}

	/**
	 * 查询相应的计划标的列表
	 * 
	 * @param params
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<DebtPlanBorrowCustomize> selectPlanBorrowListCredit(Map<String, Object> params) {
		List<DebtPlanBorrowCustomize> planAccedeList = this.debtPlanCustomizeMapper.selectPlanBorrowListCredit(params);
		return planAccedeList;

	}

	@Override
	public DebtPlan selectLastPlanByTime(Integer buyBeginTime) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cri = example.createCriteria();
		cri.andLiquidateShouldTimeIsNotNull().andLiquidateShouldTimeGreaterThan(buyBeginTime);
		example.setOrderByClause("liquidate_should_time asc");
		cri.andDebtPlanStatusEqualTo(5);
		List<DebtPlan> list = debtPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

    /**
     * 
     * 获取用户优惠券总张数
     * @author pcc
     * @param usedFlag
     * @param userId
     * @param modelAndView
     */
    @Override
    public int countCouponUsers(int usedFlag, Integer userId) {
        JSONObject jsonObject = CommonSoaUtils.countCouponUsers(usedFlag, userId);
        return jsonObject.get("recordTotal")==null?0:(int)jsonObject.get("recordTotal");
    }
    /**
     * 
     * 获取可用优惠券数量
     * @author pcc
     * @param planNid
     * @param userId
     * @param money
     * @param platform
     * @param modelAndView
     */
    @Override
    public int getUserCouponAvailableCount(String planNid, Integer userId, String money, String platform) {
        JSONObject jsonObject = CommonSoaUtils.getUserCouponAvailableCount(planNid, userId, money, platform);
        
        return jsonObject.get("availableCouponListCount")==null?0:(int)jsonObject.get("availableCouponListCount");
    }

    private UserCouponConfigCustomize getBestCoupon(String planNid, Integer userId, String money, String platform) {
        if (money == null || "".equals(money) || money.length() == 0) {
            money = "0";
        }
        UserCouponConfigCustomize couponConfig = null;
        JSONObject jsonObject = CommonSoaUtils.getBestCoupon(planNid, userId, money, platform);
        String couponConfigJson = (String) jsonObject.get("couponConfigJson");
        if (couponConfigJson != null && couponConfigJson.length() > 0) {
            couponConfig =JSONObject.toJavaObject(JSONObject.parseObject(couponConfigJson), UserCouponConfigCustomize.class);
        }
        return couponConfig;
    }
    /**
     * 
     * 获取最优优惠券信息
     * @author pcc
     * @param couponConfig
     * @param couponId
     * @param planNid
     * @param userId
     * @param money
     * @param platform
     * @return
     */
    @Override
    public UserCouponConfigCustomize getUserOptimalCoupon(String couponId, String planNid, Integer userId,
        String money, String platform) {
        UserCouponConfigCustomize couponConfig = new UserCouponConfigCustomize();
        if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
            // 获取用户最优优惠券
            couponConfig = getBestCoupon(planNid, userId, money, platform);
        } else {
            couponConfig = couponConfigCustomizeMapper.getBestCouponById(couponId);
        }
        return couponConfig;
    }
    /**
     * 
     * 计算优惠券历史回报
     * @author pcc
     * @param userCouponId
     * @param planNid
     * @param money
     * @return
     */
    @Override
    public BigDecimal getCouponInterest(String couponId, String planNid, String money) {
        JSONObject jsonObject = CommonSoaUtils.getCouponInterest(couponId, planNid, money);
        
        return new BigDecimal((String)jsonObject.get("couponInterest"));
    }


	@Override
	public void getProjectAvailableUserCoupon(String platform, String planNid, Integer userId, JSONObject ret,
			String money) {
	    if(userId!=null&&userId!=0){
	        JSONObject jsonObject = CommonSoaUtils.getProjectAvailableUserCoupon(platform, planNid, money, userId);
	        ret.put("availableCouponList", JSONArray.parseArray(
	                JSONObject.toJSONString(jsonObject.get("availableCouponList")), PlanCouponResultBean.class));
	        ret.put("notAvailableCouponList", JSONArray.parseArray(
	                JSONObject.toJSONString(jsonObject.get("notAvailableCouponList")), PlanCouponResultBean.class));
	        ret.put("availableCouponListCount", jsonObject.get("availableCouponListCount"));
	        ret.put("notAvailableCouponListCount", jsonObject.get("notAvailableCouponListCount"));
	    }else{
	        ret.put("availableCouponList", new ArrayList<PlanCouponResultBean>());
            ret.put("notAvailableCouponList", new ArrayList<PlanCouponResultBean>());
            ret.put("availableCouponListCount", 0);
            ret.put("notAvailableCouponListCount", 0);
	    }
		
	}

	@Override
	public void updateCouponTender(String couponGrantId, String planNid, Integer userId, String accountStr,
			String ipAddr, int couponOldTime, String planOrderId, ModelAndView modelAndView,String couponInterest) {
		LogUtil.infoLog(PlanServiceImpl.class.toString(), "updateCouponTender", "汇添金优惠券出借开始。。。。。。");
		JSONObject jsonObject = CommonSoaUtils.planCouponInvest(userId + "", planNid, accountStr,
				CustomConstants.CLIENT_PC, couponGrantId, planOrderId, ipAddr, couponOldTime + "");

		if (jsonObject.getIntValue("status") == 0) {
			// 优惠券收益
			modelAndView.addObject("couponInterest", jsonObject.getString("couponInterest"));
			couponInterest=jsonObject.getString("couponInterest");
			// 优惠券类别
			modelAndView.addObject("couponType", jsonObject.getString("couponTypeInt"));
			// 优惠券额度
			modelAndView.addObject("couponQuota", jsonObject.getString("couponQuota"));
			modelAndView.addObject("investDesc", "出借成功！");
		}

	}

	/**
	 * 查询汇添金出借数据
	 * 
	 * @return
	 */
	@Override
	public DebtPlanInvestDataCustomize searchInvestData() {
		DebtPlanInvestDataCustomize htjInvestData = this.debtPlanCustomizeMapper.searchInvestData();
		return htjInvestData;
	}

}
