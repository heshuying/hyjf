package com.hyjf.web.hjhdetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.common.util.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowManinfoExample;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.auto.BorrowUsersExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.plan.PlanServiceImpl;
import com.hyjf.web.user.invest.InvestController;
import com.hyjf.web.user.invest.InvestServiceImpl;

/**
 * Description:汇计划service接口实现
 * Company: HYJF Corporation
 *
 * @author: LIBIN
 * @version: 1.0s
 * Modification History:
 */
@Service
public class HjhDetailServiceImpl extends BaseServiceImpl implements HjhDetailService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AuthService authService ;


	@Autowired
	TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

	private static String HOST = PropUtils.getSystem("hyjf.web.host").trim();

	Logger _log = LoggerFactory.getLogger(HjhDetailServiceImpl.class);

	@Override
	public DebtPlanDetailCustomize selectDebtPlanDetail(String planNid) {
		DebtPlanDetailCustomize planDetail = this.hjhPlanCustomizeMapper.selectDebtPlanDetail(planNid);
		return planDetail;
	}

	@Override
	public int countUserAccede(String planNid, Integer userId) {
		HjhAccedeExample example = new HjhAccedeExample();
		HjhAccedeExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andUserIdEqualTo(userId);
		int count = this.hjhAccedeMapper.countByExample(example);
		return count;
	}

	@Override
	public HjhPlan getPlanByNid(String planNid) {
		HjhPlanExample example = new HjhPlanExample();
		HjhPlanExample.Criteria cri = example.createCriteria();
		cri.andPlanNidEqualTo(planNid);
		List<HjhPlan> debtBorrowList = hjhPlanMapper.selectByExample(example);
		if (debtBorrowList != null && debtBorrowList.size() > 0) {
			return debtBorrowList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public int countPlanAccedeRecordTotal(Map<String, Object> params) {
		int count = this.hjhPlanCustomizeMapper.countPlanAccedeRecordTotal(params);
		return count;
	}

	@Override
	public Long selectPlanAccedeSum(Map<String, Object> params) {
		return hjhPlanCustomizeMapper.selectPlanAccedeSum(params);
	}

	/**
	 * 查询计划的加入记录
	 * @param params
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<DebtPlanAccedeCustomize> selectPlanAccedeList(Map<String, Object> params) {
		List<DebtPlanAccedeCustomize> planAccedeList = this.hjhPlanCustomizeMapper.selectPlanAccedeList(params);
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
		int count = this.hjhPlanCustomizeMapper.countPlanBorrowRecordTotal(params);
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
		List<DebtPlanBorrowCustomize> planAccedeList = this.hjhPlanCustomizeMapper.selectPlanBorrowList(params);
		// add 给借款人加密 wangxiaohui 20180425 start
		for (DebtPlanBorrowCustomize planAccede : planAccedeList) {
			String borrowNid = planAccede.getBorrowNid();
			if ("1".equals(planAccede.getCompanyOrPersonal())) {//如果类型是公司 huiyingdai_borrow_users
				BorrowUsersExample caExample = new BorrowUsersExample();
				BorrowUsersExample.Criteria caCra = caExample.createCriteria();
				caCra.andBorrowNidEqualTo(borrowNid);
				List<BorrowUsers> selectByExample = this.borrowUsersMapper.selectByExample(caExample);
				String tureName= selectByExample.get(0).getUsername();
				String str = "******";
				if (tureName != null && tureName != "") {
					if (tureName.length() <= 2) {
						tureName = str + tureName;
					}else if (tureName.length() > 2) {
						String substring = tureName.substring(tureName.length()-2);
						tureName = str + substring;
					}
				}
				planAccede.setTrueName(tureName);
			}else if("2".equals(planAccede.getCompanyOrPersonal())){//类型是个人 huiyingdai_borrow_maninfo
				//根据borrowNid查询查询个人的真实姓名
				BorrowManinfoExample boExample = new BorrowManinfoExample();
				BorrowManinfoExample.Criteria caCra = boExample.createCriteria();
				caCra.andBorrowNidEqualTo(borrowNid);
				List<BorrowManinfo> selectByExample = this.borrowManinfoMapper.selectByExample(boExample);
				String trueName = selectByExample.get(0).getName();
				String str = "**";
				if (trueName != null && trueName != "") {
					if (trueName.length() == 1) {
						trueName =  trueName + str;
					}else if (trueName.length() >= 1) {
						String substring = trueName.substring(0,1);
						trueName =  substring + str;
					}
				}
				planAccede.setTrueName(trueName);
			}
		}
		// add 给借款人加密 wangxiaohui 20180425 end
		return planAccedeList;
	}


	/**
	 * 加入计划验证
	 */
	@Override
	public JSONObject checkParamPlan(String planNid, String money, Integer userId, String couponGrantId,String redisKey,String threshold) {
		if (userId == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			Users user = this.getUsers(userId);
			// (1)判断用户信息是否存在
			if (user == null) {
				return jsonMessage("用户信息不存在", "1");
			} else {
				UsersInfoExample usersInfoExample = new UsersInfoExample();
				usersInfoExample.createCriteria().andUserIdEqualTo(userId);
				List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
				// (1.1)判断 用户是不是担保机构 + 用户是不是被禁用
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
					return jsonMessage("抱歉，您的账户已被禁用，如有疑问请联系客服", "1");
				} else {
					// 用户存在且用户未被禁用
					/**
					 * autoiStatus 自动投标授权状态 0(false): 未授权    1(true):已授权',
					 * autocStatus 自动债转授权状态 0(false): 未授权    1(true):已授权',
					 * invesTime 自动投标授权时间
					 * iorderId 自动投标订单号
					 */
					//if((boolean)map.get("autoiStatus")&&StringUtils.isEmpty(autoBid))
					/*Map<String, Object> map = hjhPlanCustomizeMapper.selectUserAppointmentInfo(userId + "");
					// 为空则无授权
					if (map == null || map.isEmpty()) {
						return jsonMessage("该产品需开通自动投标功能，马上开通？ ", "1");
					}*/
					// 自动出借授权
					if (!authService.checkInvesAuthStatus(userId)) {
						return jsonMessage("该产品需开通自动投标功能，马上开通？ ", "7");
					}
					// 自动债转授权
					if (!authService.checkCreditAuthStatus(userId)) {
						return jsonMessage("该产品需开通自动债转功能，马上开通？ ", "7");
					}
					// 缴费授权
					if (!authService.checkPaymentAuthStatus(userId)) {
						return jsonMessage("该产品需开通服务费授权功能，马上开通？ ", "7");
					}

					HjhPlan plan = this.getPlanByNid(planNid);

					if (plan.getPlanInvestStatus() == 2) {
						return jsonMessage("此计划项目已经关闭", "1");
					}
					// 查看江西银行账户
					BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
					Users users = this.getUsers(userId);
					//AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(userId);
					// 用户未在平台开户 且 未开户
					if (bankOpenAccount == null || users.getBankOpenAccount() == 0) {
						return jsonMessage("用户开户信息不存在", "1");
					} else {
						// 判断借款人开户信息是否存在
						if (bankOpenAccount.getUserId() == null && bankOpenAccount.getAccount() == null ) {
							return jsonMessage("用户银行客户号不存在", "1");
						} else {
							// 判断借款编号是否存在
							if (StringUtils.isEmpty(planNid)) {
								return jsonMessage("计划项目不存在", "1");
							} else {
								// 判断借款信息是否存在
								if (plan == null || plan.getId() == null) {
									return jsonMessage("计划项目不存在", "1");
								} else {
									CouponConfigCustomizeV2 cuc = this.getCouponUser(couponGrantId, userId);
									// 判断用户出借金额是否为空
									if (!(StringUtils.isNotEmpty(money) || (StringUtils.isEmpty(money) && cuc != null && cuc.getCouponType() == 3) || (StringUtils.isEmpty(money) && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1))) {
										return jsonMessage("请输入加入金额", "1");
									} else {
										// 还款金额是否数值
										try {
											// 出借金额必须是整数
											if (StringUtils.isEmpty(money)) {
												money = "0";
											}
											// 用户出借额
											BigDecimal accountBigDecimal = new BigDecimal(money);
											/*Long accountInt = Long.parseLong(money);*/
											if (accountBigDecimal.compareTo(BigDecimal.ZERO) == -1) {
												return jsonMessage("加入金额不能为负数", "1");
											} else {
												if ((accountBigDecimal.compareTo(BigDecimal.ZERO) == 0 && cuc == null) || (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0 && cuc != null && cuc.getCouponType() == 2)) {
													return jsonMessage("出借金额不能为0元", "1");
												}
												if (accountBigDecimal.compareTo(BigDecimal.ZERO) == -1 && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
													return jsonMessage("该优惠券只能单独使用", "1");
												}
												if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 1 && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
													return jsonMessage("该优惠券只能单独使用", "1");
												}
												if(plan.getLockPeriod() == null){
													return jsonMessage("计划锁定期不存在", "1");
												}
												String lockPeriod = plan.getLockPeriod().toString();
												// 从redis取该计划的开放额度
												String balance = RedisUtils.get(redisKey);
												if (StringUtils.isEmpty(balance)) {
													return jsonMessage("您来晚了，下次再来抢吧", "1");
												} else {
													// DB 该计划可投金额
													BigDecimal available = plan.getAvailableInvestAccount();//该计划的可投金额
													BigDecimal minInvest = plan.getMinInvestment();// 该计划的最小出借金额
													// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
													if (minInvest != null && minInvest.compareTo(BigDecimal.ZERO) != 0 && new BigDecimal(balance).compareTo(minInvest) == -1) {
														if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
															return jsonMessage("剩余可加入金额为" + balance + "元", "1");//以redis为主
														}
														if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
															return jsonMessage("剩余可加入只剩" + balance + "元，须全部购买",
																	"1");
														}
													} else {
														// 项目的剩余金额大于最低起投金额
														if (accountBigDecimal.compareTo(plan.getMinInvestment()) == -1) {
															if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
																if (cuc != null && cuc.getCouponType() != 3
																		&& cuc.getCouponType() != 1) {
																	return jsonMessage(
																			plan.getMinInvestment() + "元起投",
																			"1");
																}
															} else {
																return jsonMessage(plan.getMinInvestment()
																		+ "元起投", "1");
															}
														} else {
															BigDecimal max = plan.getMaxInvestment();
															if (max != null
																	&& max.compareTo(BigDecimal.ZERO) != 0
																	&& accountBigDecimal.compareTo(max) == 1) {
																return jsonMessage("项目最大加入额为" + max + "元", "1");
															}
														}
													}
													if (accountBigDecimal.compareTo(plan.getAvailableInvestAccount()) > 0) {
														return jsonMessage("加入金额不能大于项目可投金额", "1");
													} else {
														// 查询用户账户表-出借账户
														Account tenderAccount = this.getAccount(userId);
														// 获取汇盈平台DB---用户银行账户余额
														if (tenderAccount.getBankBalance().compareTo(accountBigDecimal) < 0) {
															return jsonMessage("余额不足，请充值！", "1");
														}
														// 获取江西银行---用户银行账户余额
														BankOpenAccount accountChinapnr = getBankOpenAccount(userId);
														BigDecimal userBankBalance = getBankBalance(userId, accountChinapnr.getAccount());
														if (userBankBalance.compareTo(accountBigDecimal) < 0) {
															return jsonMessage("余额不足，请充值！", "1");
														}
														if (StringUtils.isEmpty(balance)) {
															return jsonMessage("您来晚了，下次再来抢吧", "1");
														} else {
															// balance 是 redis 中的该计划可投金额
															if (StringUtils.isNotEmpty(balance)) {
																// redis剩余金额不足
																if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
																	return jsonMessage("项目太抢手了！剩余可加入金额只有"+ balance + "元", "1");
																} else {
																	//-1表示小于，0是等于，1是大于
																	// 开放额度 < 1000(阀值)
																	// 先判断开放额度是否小于1000，小于1000走阀值的逻辑
																	if(new BigDecimal(balance).compareTo(new BigDecimal(threshold)) == -1){
																		// 出借金额 != 开放额度
																		if( (accountBigDecimal.compareTo(new BigDecimal(balance)) == -1) && (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1)   ){
																			// 继续使用递增的逻辑
																			if (plan.getInvestmentIncrement() != null &&  BigDecimal.ZERO.compareTo((accountBigDecimal.subtract(minInvest)).remainder(plan.getInvestmentIncrement())) != 0
																					&& accountBigDecimal.compareTo(new BigDecimal(balance)) == -1) {

																				return jsonMessage("加入递增金额须为" + plan.getInvestmentIncrement() + " 元的整数倍", "1");
																			}
																		}
																	}
																	// 大于等于走正常出借校验的逻辑
																	//应该是： (用户出借额度 - 起投额度)%增量 = 0
																	else if (plan.getInvestmentIncrement() != null &&  BigDecimal.ZERO.compareTo((accountBigDecimal.subtract(minInvest)).remainder(plan.getInvestmentIncrement())) != 0
																			&& accountBigDecimal.compareTo(new BigDecimal(balance)) == -1) {

																		return jsonMessage("加入递增金额须为" + plan.getInvestmentIncrement() + " 元的整数倍", "1");
																	}

																	// 如果验证没问题，则返回出借人借款人的银行账号
																	String account = bankOpenAccount.getAccount();
																	/*Long tenderUsrcustid = accountChinapnrTender.getChinapnrUsrcustid();*/
																	JSONObject jsonMessage = new JSONObject();
																	jsonMessage.put("error", "0");
																	jsonMessage.put("tenderUsrcustid",account);
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
											e.printStackTrace();
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
	 * 查询users表的isbankopen
	 *
	 * @param message
	 * @param status
	 * @return
	 */
	public Users getUsers(String userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(Integer.valueOf(userId));
		List<Users> usersList = this.usersMapper.selectByExample(example);
		if (usersList != null && usersList.size() > 0) {
			return usersList.get(0);
		}
		return null;
	}

	public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		return ccTemp;
	}



	@Override
	public boolean updateAfterPlanRedis(String planNid, String frzzeOrderId, Integer userId, String accountStr,
			String tenderUsrcustid, int client, String ipAddr, String freezeTrxId, String frzzeOrderDate,
			String planOrderId, String couponGrantId, ModelAndView modelAndView,String couponInterest) throws Exception {

		int nowTime = GetDate.getNowTime10();
		Users user = getUsers(userId);
		UsersInfo userInfo = getUsersInfoByUserId(userId);
		HjhPlan debtPlan = getPlanByNid(planNid);
		// 预期年利率
		BigDecimal planApr = debtPlan.getExpectApr();
		// 周期
		Integer planPeriod = debtPlan.getLockPeriod();
		String borrowStyle = debtPlan.getBorrowStyle();
		BigDecimal earnings = new BigDecimal(0);
		if (StringUtils.isNotEmpty(borrowStyle)) {
			// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷365*锁定期；
			if (StringUtils.equals("endday", borrowStyle)){
				earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(accountStr),planApr.divide(new BigDecimal("100")), planPeriod);
			}
			// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
			else {
				earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(accountStr), planApr.divide(new BigDecimal("100")), planPeriod);
			}
		}
		// 当大于等于100时 取百位 小于100 时 取十位
			BigDecimal accountDecimal = new BigDecimal(accountStr);//用户出借金额
			BigDecimal maxInvestNumber = debtPlan.getMaxInvestment();//最高加入金额
			BigDecimal minInvestNumber = debtPlan.getMinInvestment();//最低加入金额
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

										/*(1)汇计划加入明细表插表开始*/

			//处理汇计划加入明细表(以下涵盖所有字段)
			HjhAccede planAccede = new HjhAccede();
			//DebtPlanAccede debtPlanAccede = new DebtPlanAccede();//	原 计划加入表
			planAccede.setAccedeOrderId(planOrderId);
			planAccede.setPlanNid(planNid);
			planAccede.setUserId(userId);
			planAccede.setUserName(user.getUsername());
			planAccede.setUserAttribute(userInfo.getAttribute());//用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
			planAccede.setAccedeAccount(accountDecimal);// 加入金额
			planAccede.setAlreadyInvest(BigDecimal.ZERO);//已出借金额(出借时维护)
			planAccede.setClient(client);
			planAccede.setOrderStatus(0);//0自动投标中 2自动投标成功 3锁定中 5退出中 7已退出 99 自动出借异常(出借时维护)
			planAccede.setAddTime(nowTime);
			planAccede.setCountInterestTime(0);//计息时间(最后放款时维护)
			planAccede.setSendStatus(0);//协议发送状态0未发送 1已发送
			planAccede.setLockPeriod(debtPlan.getLockPeriod());
			planAccede.setCommissionStatus(0);//提成计算状态:0:未计算,1:已计算
			planAccede.setAvailableInvestAccount(accountDecimal);//可投金额初始与加入金额一致
			planAccede.setWaitTotal(BigDecimal.ZERO);//(出借时维护)
			planAccede.setWaitCaptical(BigDecimal.ZERO);//(出借时维护)
			planAccede.setWaitInterest(BigDecimal.ZERO);//(出借时维护)
			planAccede.setReceivedTotal(BigDecimal.ZERO);//(退出时维护)
			planAccede.setReceivedInterest(BigDecimal.ZERO);//(退出时维护)
			planAccede.setReceivedCapital(BigDecimal.ZERO);//(退出时维护)
			planAccede.setQuitTime(0);//(退出时维护)
			planAccede.setLastPaymentTime(0);//最后回款时间(复审时维护)
			planAccede.setAcctualPaymentTime(0);//实际回款时间(退出时维护)
			planAccede.setShouldPayTotal(accountDecimal.add(earnings));//应还总额 = 应还本金 +应还利息
			planAccede.setShouldPayCapital(accountDecimal);
			planAccede.setShouldPayInterest(earnings);
			planAccede.setCreateUser(userId);
			planAccede.setCreateTime(nowTime);
			//planAccede.setUpdateUser();(更表时维护)
			//planAccede.setUpdateTime();(更表时维护)
			planAccede.setDelFlg(0);//初始未未删除
			//汇计划三期要求加入计划存当时的预期年化收益率 LIBIN PC
			if(debtPlan.getExpectApr() != null){
				planAccede.setExpectApr(debtPlan.getExpectApr());
			}
			 _log.info("username:"+user.getUsername() + " userid:" + userId);

			if (Validator.isNotNull(userInfo)) {
				SpreadsUsers spreadsUsers = this.getRecommendUser(userId);
				 _log.info("推荐人信息：" + spreadsUsers);
				if (spreadsUsers != null) {
					int refUserId = spreadsUsers.getSpreadsUserid();
					 _log.info("推荐人用户id：" + spreadsUsers.getSpreadsUserid());
					// 查找用户推荐人详情信息
					UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(refUserId);
					if (Validator.isNotNull(userInfoCustomize)) {
						planAccede.setInviteUserId(userInfoCustomize.getUserId());
						planAccede.setInviteUserName(userInfoCustomize.getUserName());
						planAccede.setInviteUserAttribute(userInfoCustomize.getAttribute());
						planAccede.setInviteUserRegionname(userInfoCustomize.getRegionName());
						planAccede.setInviteUserBranchname(userInfoCustomize.getBranchName());
						planAccede.setInviteUserDepartmentname(userInfoCustomize.getDepartmentName());
					}
				}else if(userInfo.getAttribute() == 2 || userInfo.getAttribute() == 3){
					// 查找用户推荐人详情信息
					UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(userId);
					if (Validator.isNotNull(userInfoCustomize)) {
						planAccede.setInviteUserId(userInfoCustomize.getUserId());
						planAccede.setInviteUserName(userInfoCustomize.getUserName());
						planAccede.setInviteUserAttribute(userInfoCustomize.getAttribute());
						planAccede.setInviteUserRegionname(userInfoCustomize.getRegionName());
						planAccede.setInviteUserBranchname(userInfoCustomize.getBranchName());
						planAccede.setInviteUserDepartmentname(userInfoCustomize.getDepartmentName());
					}
				}
			}
			 _log.info("计划加入用户:" + userId + "***********************************插入planAccede："
					+ JSON.toJSONString(planAccede));
			//boolean trenderFlag = debtPlanAccedeMapper.insertSelective(planAccede) > 0 ? true : false;// 原 汇添金插表
			boolean trenderFlag =  hjhAccedeMapper.insertSelective(planAccede) > 0 ? true : false;

			                                      /*汇计划加入明细表插表结束*/
			                                      /*add by pcc 汇添金使用优惠券出借 start*/

			if (StringUtils.isNotEmpty(couponGrantId)) {
				// 优惠券出借校验
				try {
					Map<String, String> validateMap = this.validateCoupon(userId, accountStr, couponGrantId, planNid,
							CustomConstants.CLIENT_PC);
					LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借校验结果："
							+ validateMap.get("statusDesc"));
					 _log.info("updateCouponTender" + "优惠券出借校验结果：" + validateMap.get("statusDesc"));
					if (MapUtils.isEmpty(validateMap)) {
						// 校验通过 进行出借
						this.updateCouponTender(couponGrantId, planNid, userId, accountStr, ipAddr, new Integer(
								frzzeOrderDate), planOrderId, modelAndView,couponInterest);
					}
				} catch (Exception e) {
					LogUtil.infoLog(InvestController.class.getName(), "tenderRetUrl", "优惠券出借失败");
				}
			}
			                                       /*add by pcc 汇添金使用优惠券出借 end*/

			if (trenderFlag) {//加入明细表插表成功的前提下，继续
				// TO DO
/*				try {
					// 调用crm同步加入接口
					String debtinfo = JSONObject.toJSONString(planAccede);
					InvestSysUtils.insertInvestSys(debtinfo);
				} catch (Exception e) {
					 _log.info("专属标加入 crm同步accoced表失败");
					e.printStackTrace();
				}*/


                //crm出借推送
                rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,
                        RabbitMQConstants.ROUTINGKEY_POSTINTERFACE_CRM, JSON.toJSONString(planAccede));


				                                  /*(2)更新huiyingdai_account账户余额表开始*/
				// 查询开户表获取电子账号
				BankOpenAccount accountChinapnrTender = getBankOpenAccount(userId);

				AccountExample example = new AccountExample();
				AccountExample.Criteria criteria = example.createCriteria();
				criteria.andUserIdEqualTo(userId);
				List<Account> list = accountMapper.selectByExample(example);
				if (list != null && list.size() == 1) {
					// 该用户的江西银行可用余额(查询当前用户)
					BigDecimal bankBalance = list.get(0).getBankBalance();
					// 该用户的汇计划账户可用余额(查询当前用户)
					BigDecimal planBalance = list.get(0).getBankBalance();
					// 冻结金额(查询当前用户)
					BigDecimal frost = list.get(0).getBankFrost();
					// 总金额(查询当前用户)
					BigDecimal total = list.get(0).getBankTotal();
				    /*该用户的计划可用余额 = 原该用户的计划可用余额 + 该用户加入金额*/
                    /*该用户的江西银行可用余额 = 原该用户的江西银行可用余额 - 该用户加入金额*/
					Account account = new Account();
					// 用户id
					account.setUserId(userId);
					account.setPlanBalance(accountDecimal);//注意：先set值，加法运算放在SQL中防并发
					account.setBankBalance(accountDecimal);//注意：先set值，减法运算放在SQL中防并发

					//汇计划二期-- 更新用户的累积出借金额
					account.setBankInvestSum(accountDecimal);//注意：先set值，加法运算放在SQL中防并发

					// 更新用户计划账户  huiyingdai_account
					boolean accountFlag = this.hjhPlanCustomizeMapper.updateOfHjhPlanJoin(account) > 0 ? true : false;

					//boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanJoin(account) > 0 ? true : false;//原 汇添金更表
					                              /*更新huiyingdai_account用户账户余额表结束*/
					                              /*(3)插入account_list表 开始*/
					if (accountFlag) {//huiyingdai_account更表成功的前提下，继续
						//先查询一遍Account表
						Account accedeAccount = this.selectUserAccount(userId);
						AccountList accountList = new AccountList();
						accountList.setIsBank(1);
						accountList.setIsShow(0);
						accountList.setNid(planOrderId);//插入生成的计划订单号
						accountList.setAccedeOrderId(planOrderId);//也插入生成的计划订单号
						accountList.setUserId(userId);
						accountList.setAmount(accountDecimal);//插入用户加入金额
						accountList.setType(2);// 收支类型1收入2支出3冻结
						accountList.setTrade("hjh_invest");// 汇计划出借
						accountList.setTradeCode("balance");
						accountList.setIp(ipAddr);
						accountList.setOperator(userId + "");
						accountList.setRemark(planNid);
						accountList.setBankBalance(accedeAccount.getBankBalance());//江西银行账户余额
						//accountList.setBalance(accedeAccount.getBalance());//原 汇付账户
						accountList.setPlanBalance(accedeAccount.getPlanBalance());//汇计划账户可用余额
						accountList.setInterest(new BigDecimal(0));
						accountList.setAwait(accedeAccount.getAwait());
						accountList.setPlanFrost(accedeAccount.getPlanFrost());
						accountList.setBankFrost(frost);
						accountList.setIsUpdate(0);
						accountList.setRepay(new BigDecimal(0));
						accountList.setBankTotal(total);
						accountList.setWeb(0);
						accountList.setBaseUpdate(0);
						accountList.setCreateTime(nowTime);
						accountList.setAccountId(accountChinapnrTender.getAccount());
						// 汇计划三期插入 accountList 表新增三个字段 By libin start
						accountList.setBankAwait(accedeAccount.getBankAwait());
						accountList.setBankAwaitCapital(accedeAccount.getBankAwaitCapital());
						accountList.setBankAwaitInterest(accedeAccount.getBankAwaitInterest());
						// 汇计划三期插入 accountList 表新增三个字段 By libin end
						 _log.info("加入计划用户:" + userId + "***********************************预插入accountList："
								+ JSON.toJSONString(accountList));
						boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
													/*(3)插入account_list表 结束*/

                                                    /*(4.1) 更新用户表的用户状态 开始  add by libin*/

						if (accountListFlag) {
							if(user!= null) {
							      if (user.getInvestflag() == 0) {
							    	  user.setInvestflag(1);
							          UsersExample userExample = new UsersExample();
							          userExample.createCriteria().andUserIdEqualTo(userId).andInvestflagEqualTo(0);
							          this.usersMapper.updateByExampleSelective(user, userExample);
							        }
							}
						}

                                                    /*(4.1) 更新用户表的用户状态 结束*/

													/*(4.2)更新汇计划列表 开始*/
						if (accountListFlag) {//account_list表 插表成功的前提下
							// 更新plan表
							Map<String, Object> plan = new HashMap<String, Object>();
							plan.put("planId", debtPlan.getPlanNid());
							plan.put("accountDecimal", accountDecimal);//用户加入金额
							plan.put("earnings", earnings);
							boolean updateBorrowAccountFlag = hjhPlanCustomizeMapper.updateByDebtPlanId(plan) > 0 ? true : false;//hyjf_hjh_plan
							                        /*(4.2)更新汇计划列表 结束*/

							                        /*汇计划二期(5)更新平台累积出借 开始*/
						if (updateBorrowAccountFlag) {
							List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
							if (calculates != null && calculates.size() > 0) {
								CalculateInvestInterest calculateNew = new CalculateInvestInterest();
								calculateNew.setTenderSum(accountDecimal);//用户所投金额
								calculateNew.setId(calculates.get(0).getId());
								calculateNew.setCreateTime(GetDate.getDate(nowTime));
								this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
							}
						}
						                           /*(5)更新平台累积出借 结束*/

							//计入计划成功更新mongo运营数据
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("type", 3);// 加入计划
							jsonObject.put("money", accountDecimal);
							rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_OPERATION_DATA, JSONObject.toJSONString(jsonObject));

												   /*(6)更新  渠道统计用户累计出借  和  huiyingdai_utm_reg的首投信息 开始*/
							if (updateBorrowAccountFlag) {//更新汇计划列表成功的前提下
								// 更新渠道统计用户累计出借
								// 出借人信息
								Users users = getUsers(userId);
								if (users != null) {
									// 更新渠道统计用户累计出借
									AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
									AppChannelStatisticsDetailExample.Criteria crt = channelExample.createCriteria();
									crt.andUserIdEqualTo(userId);
									List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(channelExample);
									if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
										AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
										Map<String, Object> params = new HashMap<String, Object>();
										params.put("id", channelDetail.getId());
										// 认购本金
										params.put("accountDecimal", accountDecimal);
										// 出借时间
										params.put("investTime", nowTime);
										// 项目类型
										params.put("projectType", "汇计划");
										// 首次投标项目期限
										String investProjectPeriod = debtPlan.getLockPeriod() + "天";
										params.put("investProjectPeriod", investProjectPeriod);
										// 更新渠道统计用户累计出借
										if (users.getInvestflag() == 1) {
											this.appChannelStatisticsDetailCustomizeMapper.updateAppChannelStatisticsDetail(params);
										} else if (users.getInvestflag() == 0) {
											// 更新首投出借
											this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
										}
										 _log.info("用户:"+ userId+ "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号："+ planAccede.getAccedeOrderId());
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
											params.put("projectType", "汇计划");
											// 首次投标项目期限
											String investProjectPeriod = debtPlan.getLockPeriod() + "天";
											// 首次投标项目期限
											params.put("investProjectPeriod", investProjectPeriod);
											// 更新渠道统计用户累计出借
											if (users.getInvestflag() == 0) {
												// 更新huiyingdai_utm_reg的首投信息
												this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
												 _log.info("用户:"+ userId + "***********************************预更新渠道统计表huiyingdai_utm_reg的首投信息，订单号："+ planAccede.getAccedeOrderId());
											}
										}
									}
								}
																/*(6)更新  渠道统计用户累计出借  和  huiyingdai_utm_reg的首投信息 结束*/
								return true;

							} else {
								throw new RuntimeException("huiyingdai_account表，hyjf_hjh_plan表，account_list表，hyjf_hjh_accede表 操作失败");
							}
						} else {
							throw new RuntimeException("account_list表操作失败");
						}
					} else {
						throw new RuntimeException("huiyingdai_account表操作失败");
					}
				} else {
					throw new RuntimeException("用户账户信息(huiyingdai_account)操作失败！");
				}
			} else {
				throw new RuntimeException("加入明细表操作失败！");
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

	@Override
	public void recoverRedis(String planNid, Integer userId, String account, String lockPeriod,String redisKey) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
			while ("OK".equals(jedis.watch(redisKey))) {
				String balanceLast = RedisUtils.get(redisKey);
				if (StringUtils.isBlank(balanceLast)) {
					return;
				}
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction tx = jedis.multi();
				tx.set(redisKey, recoverAccount + "");
				List<Object> result = tx.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					 _log.info("加入计划用户:" + userId + "***********************************from redis恢复redis："
							+ account);
					break;
				}
			}
	}


	/**
	 * 根据电子账号查询用户在江西银行的可用余额
	 *
	 * @param accountId
	 * @return
	 */
	public BigDecimal getBankBalance(Integer userId, String accountId) {
		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogRemark("电子账户余额查询");
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return balance;
	}

	/**
	 * 根据参数查找债转记录
	 */
	@Override
	public List<HjhDebtCredit> selectHjhDebtCreditList(Map<String,Object> mapParam) {
		if(null!=mapParam&&mapParam.size()>0) {
			String borrowNid = mapParam.get("borrowNid").toString();
			HjhDebtCreditExample hjhDebtCreditExample = new HjhDebtCreditExample();
	        HjhDebtCreditExample.Criteria criteria = hjhDebtCreditExample.createCriteria();
	        if(mapParam.containsKey("inStatus")) {
	        	List<Integer> listIn = (List<Integer>) mapParam.get("inStatus");
	        	criteria.andCreditStatusIn(listIn);
	        }
	        criteria.andBorrowNidEqualTo(borrowNid);
			List<HjhDebtCredit> listHjhDebtCredit = this.hjhDebtCreditMapper.selectByExample(hjhDebtCreditExample);
			return listHjhDebtCredit;
		}
		return null;
	}

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	@Override
	public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("eventCode", sensorsDataBean.getEventCode());
		params.put("userId", sensorsDataBean.getUserId());
		params.put("presetProps", sensorsDataBean.getPresetProps());
		params.put("orderId", sensorsDataBean.getOrderId());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.SENSORS_DATA_ROUTINGKEY_HJH_INVEST, JSONObject.toJSONString(params));
	}


}
