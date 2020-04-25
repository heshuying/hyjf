package com.hyjf.batch.exception.investall;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.client.autoinvestsys.InvestSysUtils;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.FinancingServiceChargeUtils;
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
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.auto.VipUserTender;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;

@Service
public class BankInvestAllExceptionServiceImpl extends BaseServiceImpl implements BankInvesetExceptionAllService {

	public static JedisPool pool = RedisUtils.getPool();

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

	Logger _log = LoggerFactory.getLogger(BankInvestAllExceptionServiceImpl.class);

	@Override
	public void updateTender() {

		// 判断huiyingdai_borrow_tender_tmp 表是否存在未删除的数据,如果有则存在全部掉单的出借
		BorrowTenderTmpExample bexample = new BorrowTenderTmpExample();
		bexample.createCriteria().andStatusEqualTo(0).andIsBankTenderEqualTo(1);
		List<BorrowTenderTmp> borrowTenderTmpList = this.borrowTenderTmpMapper.selectByExample(bexample);
		// 遍历循环掉单记录开始进行出借掉单修复
		if (borrowTenderTmpList != null && borrowTenderTmpList.size() > 0) {
			for (int i = 0; i < borrowTenderTmpList.size(); i++) {
				String orderid = borrowTenderTmpList.get(i).getNid();
				try {
					_log.info("开始处理出借订单号为:[" + orderid + "]");
					Integer userId = borrowTenderTmpList.get(i).getUserId();
					BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
					// 用户电子账户号
					String accountId = bankOpenAccount == null ? "" : bankOpenAccount.getAccount();
					// 根据相应信息接口查询订单
					BankCallBean bean = queryBorrowTenderList(accountId, orderid, String.valueOf(userId));
					// 获得银行信息开始进行掉单处理
					updateTenderStart(bean, orderid, userId, borrowTenderTmpList.get(i));
				} catch (Exception e) {
					_log.info("=============出借全部掉单异常处理失败! 失败订单: " + orderid + "失败原因: " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 开始进行掉单修复
	 * 
	 * @param bean
	 * @param borrowTenderTmp
	 * @throws Exception
	 */
	private void updateTenderStart(BankCallBean bean, String orderId, Integer userId, BorrowTenderTmp borrowTenderTmp) throws Exception {
		if (bean != null) {
			bean.convert();
			if ("1".equals(bean.getState())) {
				// 投标中的出借数据
				String borrowId = bean.getProductId();// 借款Id
				String account = bean.getTxAmount();// 借款金额
				try {
					// 根据借款Id检索标的信息
					// modify by cwyang borrowid 换成borrowNid
					BorrowWithBLOBs borrow = this.getBorrowByNid(borrowId);
					String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();// 项目编号
					_log.info("===============开始修复出借全部掉单,标的号:" + borrowNid);
					if (StringUtils.isBlank(borrowNid)) {
						throw new Exception("=======================出借掉单修复,borrowNid 为空!");
					} else {
						String couponGrantId = null;
						couponGrantId = borrowTenderTmp.getCouponGrantId() + "";
						JSONObject checkParamResult = checkParam(borrowNid, account, userId, null, couponGrantId);
						if (checkParamResult == null) {
							throw new Exception("出借掉单修复校验失败!");
						} else if (checkParamResult.get("error") != null && checkParamResult.get("error").equals("1")) {
							throw new Exception(checkParamResult.get("data") + "");
						}
						int couponOldTime = Integer.MIN_VALUE;
						// 如果redis操作成功
						try {
							if (StringUtils.isBlank(bean.getOrderId())) {
								bean.setOrderId(orderId);
							}
							// 进行出借, tendertmp锁住
							JSONObject tenderResult = updateTender(borrow, bean);
							// 出借成功
							if (tenderResult.getString("status").equals("1")) {
								_log.info("出借全部掉单修复:" + userId + "***掉单修复出借成功：" + account);
								if (StringUtils.isNotBlank(couponGrantId)) {
									// 优惠券出借校验
									try {
										CouponConfigCustomizeV2 cuc = null;
										cuc = getCouponUser(couponGrantId, userId);
										// 排他check用
										couponOldTime = cuc.getUserUpdateTime();
										// 优惠券出借校验
										JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId);
										int couponStatus = couponCheckResult.getIntValue("status");
										String statusDesc = couponCheckResult.getString("statusDesc");
										_log.info("updateCouponTender" + "优惠券出借校验结果：" + statusDesc);
										if (couponStatus == 0) {
											// 优惠券出借
											CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, orderId, null, couponOldTime + "");
										}
									} catch (Exception e) {
										_log.info("优惠券出借失败");
									}
								}
								// 如果是融通宝项目且存在加息
								if (borrow.getProjectType() == 13) {
									if (borrow.getBorrowExtraYield() != null && borrow.getBorrowExtraYield().compareTo(new BigDecimal("0")) > 0) {
										extraUeldInvest(borrow, bean);
									}
								}
							}
							// 出借失败 回滚redis
							else {
								// 更新tendertmp
								boolean updateFlag = updateBorrowTenderTmp(userId, borrowNid, orderId);
								// 更新失败，出借失败
								if (updateFlag) {
									// 出借失败,出借撤销
									try {
										boolean flag = this.bidCancel(userId, borrowId, orderId, account);
										if (!flag) {
											_log.info("投标失败,请联系客服人员!");
										}
									} catch (Exception e) {
										_log.info("投标申请撤销失败,请联系客服人员!");
									}
								} else {
									_log.info("恭喜您出借成功!");
									boolean updateTenderFlag = this.updateTender(userId, borrowNid, orderId, bean);
									if (!updateTenderFlag) {
										_log.info("投标出現错误,请联系客服人员!");
									}
								}
							}
						} catch (Exception e) {
							// 更新tendertmp
							boolean updateFlag = updateBorrowTenderTmp(userId, borrowNid, orderId);
							// 更新失败，出借失败
							if (updateFlag) {
								// 出借失败,出借撤销
								try {
									boolean flag = bidCancel(userId, borrowId, orderId, account);
									if (!flag) {
										_log.info("投标失败,请联系客服人员!");
									}
								} catch (Exception e1) {
									_log.info("投标申请撤销失败,请联系客服人员!");
								}
							} else {
								_log.info("恭喜您出借成功!");
								boolean updateTenderFlag = this.updateTender(userId, borrowNid, orderId, bean);
								if (!updateTenderFlag) {
									_log.info("投标出現错误,请联系客服人员!");
								}
							}
						}
					}
				} catch (Exception e) {
					boolean flag = this.bidCancel(userId, borrowId, orderId, account);
					if (flag) {
						System.out.println("撤销成功!");
					} else {
						System.out.println("投标失败,请联系客服人员!");
					}
				}
			} else {
				borrowTenderTmp.setStatus(1);
				int result = this.borrowTenderTmpMapper.updateByPrimaryKey(borrowTenderTmp);
				if (result > 0) {
					_log.info("==============变更borrowTenderTmp表状态成功!==============");
				} else {
					_log.info("==============变更borrowTenderTmp表状态失败!==============");
				}
				throw new Exception("====================出借信息不是投标中的状态,不予处理!===========================订单号: " + orderId);
			}
		} else {
			throw new Exception("=====================出借全部掉单处理没有找到匹配的标的信息,订单号: " + orderId + ", 请手动处理!==============");
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
	 * 出借校验
	 * 
	 * @param borrowNid
	 * @param account
	 * @param userIdInt
	 * @param platform
	 * @param couponGrantId
	 * @return
	 * @author Administrator
	 */
	public JSONObject checkParam(String borrowNid, String account, Integer userIdInt, String platform, String couponGrantId) {
		CouponConfigCustomizeV2 cuc = null;
		// -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
		/*
		 * if(StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1",
		 * couponGrantId)){ cuc = this.getCouponUser(couponGrantId); }
		 */
		UsersInfo usersInfo = this.getUsersInfoByUserId(userIdInt);
		if (null != usersInfo) {
			String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
			if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
				if (usersInfo.getRoleId() != 1) {// 担保机构用户
					return jsonMessage("仅限出借人进行出借", "1");
				}
			}

		} else {
			return jsonMessage("账户信息异常", "1");
		}
		String userId = "";
		if (userIdInt == null) {
			return jsonMessage("您未登陆，请先登录", "1");
		} else {
			userId = String.valueOf(userIdInt.intValue());
		}
		// 判断用户userId是否存在
		if (StringUtils.isEmpty(userId)) {
			return jsonMessage("您未登陆，请先登录", "1");
		}
		Users user = this.getUsersByUserId(Integer.parseInt(userId));
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = this.getCouponUser(couponGrantId, userIdInt);
		}
		// 判断用户信息是否存在
		if (user == null) {
			return jsonMessage("用户信息不存在", "1");
		}

		// 判断用户是否设置了交易密码
		if (user.getIsSetPassword() == 0) {
			return jsonMessage("该用户未设置交易密码", "1");
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
		// 出借项目的配置信息
		BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(projectType));
		if (borrowProjectType == null) {
			return jsonMessage("未查询到该出借项目的配置信息", "1");
		}
		// 51老用户标
		if (borrowProjectType.getInvestUserType().equals("0")) {
			boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
			if (!is51User) {
				return jsonMessage("该项目只能51老用户出借", "1");
			}
		}
		if (borrowProjectType.getInvestUserType().equals("1")) {
			boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId));
			if (!newUser) {
				return jsonMessage("该项目只能新手出借", "1");
			}
		}
		BankOpenAccount accountChinapnrTender = this.getBankOpenAccount(Integer.parseInt(userId));
		// 用户未在平台开户
		if (accountChinapnrTender == null) {
			return jsonMessage("用户开户信息不存在", "1");
		}
		// 判断借款人开户信息是否存在
		if (StringUtils.isEmpty(accountChinapnrTender.getAccount())) {
			return jsonMessage("用户银行客户号不存在", "1");
		}

		BankOpenAccount accountChinapnrBorrower = this.getBankOpenAccount(borrow.getUserId());
		if (accountChinapnrBorrower == null) {
			return jsonMessage("借款人未开户", "1");
		}
		if (StringUtils.isEmpty(accountChinapnrBorrower.getAccount())) {
			return jsonMessage("借款人汇付客户号不存在", "1");
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
		if (!(StringUtils.isNotEmpty(account) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 3) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 1 && cuc
				.getAddFlg() == 1))) {
			return jsonMessage("请输入出借金额", "1");
		}
		// 还款金额是否数值
		try {
			// 新需求判断顺序变化
			// 将出借金额转化为BigDecimal
			BigDecimal accountBigDecimal = new BigDecimal(account);
			String balance = RedisUtils.get(borrowNid);
			if (StringUtils.isEmpty(balance)) {
				return jsonMessage("您来晚了，下次再来抢吧", "1");
			}
			// 剩余可投金额
			Integer min = borrow.getTenderAccountMin();
			// 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
			if (min != null && min != 0 && new BigDecimal(balance).compareTo(new BigDecimal(min)) == -1) {
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
					return jsonMessage("剩余可投金额为" + balance + "元", "1");
				}
				if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
					return jsonMessage("剩余可投只剩" + balance + "元，须全部购买", "1");
				}
			} else {
				// 项目的剩余金额大于最低起投金额
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
			// 出借人记录
			Account tenderAccount = this.getAccountByUserId(Integer.parseInt(userId));
			if (tenderAccount.getBankBalance().compareTo(accountBigDecimal) < 0) {
				return jsonMessage("余额不足，请充值！", "1");
			}
			// 判断用户是否禁用
			if (user.getStatus() == 1) {// 0启用，1禁用
				return jsonMessage("该用户已被禁用", "1");
			}
			// redis剩余金额不足
			if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
				return jsonMessage("剩余可投金额为" + balance + "元", "1");
			}
			// 如果验证没问题，则返回出借人借款人的汇付账号
			String borrowerUsrcustid = accountChinapnrBorrower.getAccount();
			String tenderUsrcustid = accountChinapnrTender.getAccount();
			JSONObject jsonMessage = new JSONObject();
			jsonMessage.put("error", "0");
			jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
			jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
			jsonMessage.put("borrowId", borrow.getId());
			jsonMessage.put("tenderUserName", user.getUsername());
			return jsonMessage;
		} catch (Exception e) {
			System.out.println("=========出借校验异常===========");
			return jsonMessage("出借金额必须为整数", "1");
		}

	}

	/**
	 * 新用户新手标验证，标如果是新用户标，查看此用户是否有过出借记录，如果有返回true，提示不能投标了
	 *
	 * @param userId
	 * @param projectType
	 * @return
	 */
	public boolean checkIsNewUserCanInvest(Integer userId) {
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
	 * 获取项目类型
	 * 
	 * @param projectType
	 * @return
	 * @author b
	 */
	public BorrowProjectType getBorrowProjectType(String projectType) {
		if (StringUtils.isBlank(projectType)) {
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

	public boolean updateTender(int userId, String borrowNid, String orderId, BankCallBean bean) {
		String authCode = bean.getAuthCode();// 出借结果授权码
		BorrowTenderExample example = new BorrowTenderExample();
		example.createCriteria().andUserIdEqualTo(userId).andBorrowNidEqualTo(borrowNid).andNidEqualTo(orderId);
		BorrowTender borrowTender = new BorrowTender();
		borrowTender.setAuthCode(authCode);
		boolean tenderFlag = this.borrowTenderMapper.updateByExampleSelective(borrowTender, example) > 0 ? true : false;

		// 刪除CRM 接口调用 del by liuyang 20180112 start
		// add by cwyang 2017-5-18 增加crm出借同步接口
//		if (tenderFlag) {
//			int borrowID = 0;
//			try {
//				List<BorrowTender> resultTender = this.borrowTenderMapper.selectByExample(example);
//				if (resultTender != null && resultTender.size() == 1) {
//					borrowID = resultTender.get(0).getId();
//				}
//				System.out.println("===============crm同步borrowTender 开始! borrID is " + borrowID);
//				InvestSysUtils.insertBorrowTenderSys(borrowID + "");
//			} catch (Exception e) {
//				System.out.println("===============crm同步borrowTender 异常! borrID is " + borrowID);
//			}
//		}
		// 刪除CRM 接口调用 del by liuyang 20180112 end
		return tenderFlag;
	}

	public boolean bidCancel(int investUserId, String productId, String orgOrderId, String txAmount) throws Exception {
		// 出借人的账户信息
		BankOpenAccount outCust = this.getBankOpenAccount(investUserId);
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + investUserId + "]，" + "[出借订单号：" + orgOrderId + "]");
		}
		String tenderAccountId = outCust.getAccount();
		// 调用交易查询接口(出借撤销)
		BankCallBean queryTransStatBean = bidCancel(investUserId, tenderAccountId, productId, orgOrderId, txAmount);
		if (queryTransStatBean == null) {
			throw new Exception("调用投标申请撤销失败。" + ",[出借订单号：" + orgOrderId + "]");
		} else {
			String queryRespCode = queryTransStatBean.getRetCode();
			System.out.print("出借失败交易接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRetMsg();
				LogUtil.errorLog(this.getClass().getName(), "bidCancel", "调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orgOrderId + "]", null);
				throw new Exception("调用投标申请撤销失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orgOrderId + "]");
			} else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) || queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2)) {
				System.out.println("===============冻结记录不存在,不予处理========");
				deleteBorrowTenderTmp(orgOrderId);
				return true;
			} else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)) {
				System.out.println("===============只能撤销投标状态为投标中的标的============");
				return false;
			} else {
				deleteBorrowTenderTmp(orgOrderId);
				return true;
			}
		}
	}

	private void deleteBorrowTenderTmp(String orgOrderId) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		example.createCriteria().andNidEqualTo(orgOrderId);
		this.borrowTenderTmpMapper.deleteByExample(example);
	}

	/**
	 * 投标失败后,调用出借撤销接口
	 * 
	 * @param ordId
	 * @param ordDate
	 * @param queryTransType
	 * @return
	 */
	private BankCallBean bidCancel(Integer investUserId, String investUserAccountId, String productId, String orgOrderId, String txAmount) {
		String methodName = "bidCancel";
		// 调用汇付接口(交易状态查询)
		BankCallBean bean = new BankCallBean();
		String orderId = GetOrderIdUtils.getOrderId2(investUserId);
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		Users investUser = this.getUsersByUserId(investUserId);
		bean.setVersion(BankCallConstant.VERSION_10); // 版本号(必须)
		bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL); // 交易代码
		bean.setInstCode(instCode);
		bean.setBankCode(bankCode);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6)); // 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(investUserAccountId);// 电子账号
		bean.setOrderId(orderId); // 订单号(必须)
		bean.setTxAmount(CustomUtil.formatAmount(txAmount));// 交易金额
		bean.setProductId(productId);// 标的号
		bean.setOrgOrderId(orgOrderId);// 原标的订单号
		bean.setLogOrderId(orderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
		bean.setLogUserId(String.valueOf(investUserId));// 用户Id
		bean.setLogUserName(investUser == null ? "" : investUser.getUsername()); // 用户名
		bean.setLogRemark("投标申请撤销"); // 备注
		// 调用汇付接口
		BankCallBean chinapnrBean = BankCallUtils.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 更新出借记录临时表
	 * 
	 * @param userId
	 * @param ordId
	 * @return
	 * @author Administrator
	 */
	public boolean updateBorrowTenderTmp(int userId, String borrowNid, String ordId) {
		BorrowTenderTmpExample example = new BorrowTenderTmpExample();
		BorrowTenderTmpExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andNidEqualTo(ordId);
		crt.andBorrowNidEqualTo(borrowNid);
		BorrowTenderTmp tenderTmp = new BorrowTenderTmp();
		tenderTmp.setStatus(1);
		boolean flag = this.borrowTenderTmpMapper.updateByExampleSelective(tenderTmp, example) > 0 ? true : false;
		return flag;

	}

	public boolean redisRecover(int userId, String borrowNid, String account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
		while ("OK".equals(jedis.watch(borrowNid))) {
			String balanceLast = RedisUtils.get(borrowNid);
			if (StringUtils.isNotBlank(balanceLast)) {
				System.out.println("PC用户:" + userId + "***redis剩余金额：" + balanceLast);
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction transaction = jedis.multi();
				transaction.set(borrowNid, recoverAccount.toString());
				List<Object> result = transaction.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					String ret = (String) result.get(0);
					if (ret != null && ret.equals("OK")) {
						System.out.println("用户:" + userId + "*******from redis恢复redis：" + account);
						return true;
					} else {
						jedis.unwatch();
					}
				}
			}
		}
		return false;
	}

	/**
	 * 根据相应信息接口查询投标申请
	 * 
	 * @param accountId
	 * @param orgOrderId
	 * @return
	 */
	private BankCallBean queryBorrowTenderList(String accountId, String orgOrderId, String userId) {
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setOrgOrderId(orgOrderId);
		bean.setLogUserId(userId);
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
		bean.setLogRemark("出借人投标申请查询");
		// 调用接口
		return BankCallUtils.callApiBg(bean);
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

	public void extraUeldInvest(Borrow borrow, BankCallBean bean) {
		// 操作ip
		String ip = bean.getLogIp();
		// 操作平台
		int client = bean.getLogClient() != 0 ? bean.getLogClient() : 0;
		// 出借人id
		Integer userId = Integer.parseInt(bean.getLogUserId());
		// 借款金额
		String account = bean.getTxAmount();
		// 订单id
		String tenderOrderId = bean.getOrderId();
		// 项目编号
		String borrowNid = borrow.getBorrowNid();
		// 项目的还款方式
		String borrowStyle = borrow.getBorrowStyle();
		BorrowStyle borrowStyleMain = this.getborrowStyleByNid(borrowStyle);
		String borrowStyleName = borrowStyleMain.getName();
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		Users users = this.getUsersByUserId(userId);
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
			increaseInterestInvest.setRemark("产品加息");
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

	/**
	 * 取得用户优惠券信息
	 * 
	 * @param couponGrantId
	 * @return
	 */
	public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
		CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
		return ccTemp;
	}

	public JSONObject redisTender(int userId, String borrowNid, String txAmount) {

		Jedis jedis = pool.getResource();
		String status = BankCallConstant.STATUS_FAIL; // 发送状态
		JSONObject info = new JSONObject();
		BigDecimal accountDecimal = new BigDecimal(txAmount);// 冻结前验证
		String accountRedisWait = RedisUtils.get(borrowNid);
		if (StringUtils.isNotBlank(accountRedisWait)) {
			// 操作redis
			while ("OK".equals(jedis.watch(borrowNid))) {
				accountRedisWait = RedisUtils.get(borrowNid);
				if (StringUtils.isNotBlank(accountRedisWait)) {
					System.out.println("PC用户:" + userId + "***冻结前可投金额：" + accountRedisWait);
					if (new BigDecimal(accountRedisWait).compareTo(BigDecimal.ZERO) == 0) {
						info.put("message", "您来晚了，下次再来抢吧！");
						info.put("status", status);
						break;
					} else {
						if (new BigDecimal(accountRedisWait).compareTo(accountDecimal) < 0) {
							info.put("message", "可投剩余金额为" + accountRedisWait + "元！");
							info.put("status", status);
							break;
						} else {
							Transaction transaction = jedis.multi();
							BigDecimal lastAccount = new BigDecimal(accountRedisWait).subtract(accountDecimal);
							transaction.set(borrowNid, lastAccount.toString());
							List<Object> result = transaction.exec();
							if (result == null || result.isEmpty()) {
								jedis.unwatch();
							} else {
								String ret = (String) result.get(0);
								if (ret != null && ret.equals("OK")) {
									status = BankCallConstant.STATUS_SUCCESS;
									info.put("message", "redis操作成功！");
									info.put("status", status);
									System.out.println("PC用户:" + userId + "***冻结前减redis：" + accountDecimal);
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
		return info;
	}

	public JSONObject updateTender(Borrow borrow, BankCallBean bean) throws Exception {

		int nowTime = GetDate.getNowTime10();
		String ip = bean.getLogIp();// 操作ip
		int client = bean.getLogClient() != 0 ? bean.getLogClient() : 0;// 操作平台
		String accountId = bean.getAccountId();// 获取出借用户的出借客户号
		Integer userId = Integer.parseInt(bean.getLogUserId());// 出借人id
		String txAmount = bean.getTxAmount();// 借款金额
		String orderId = bean.getOrderId();// 订单id
		String orderDate = bean.getLogOrderDate(); // 订单日期
		String retCode = bean.getRetCode();// 出借结果返回码
		String authCode = bean.getAuthCode();// 出借结果授权码
		String borrowNid = borrow.getBorrowNid();// 项目编号
		String borrowStyle = borrow.getBorrowStyle();// 项目的还款方式
		int projectType = borrow.getProjectType();// 项目类型
		BigDecimal serviceFeeRate = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getServiceFeeRate()); // 服务费率
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 借款期数
		Integer borrowId = borrow.getId();// 借款项目主键
		BigDecimal accountDecimal = new BigDecimal(txAmount);// 冻结前验证
		// redis扣减
		JSONObject result = this.redisTender(userId, borrowNid, txAmount);
		// redis结果状态
		String redisStatus = result.getString("status");
		if (redisStatus.equals("0")) {
			// 手动控制事务
			TransactionStatus txStatus = null;
			try {
				// 手动控制事务
				txStatus = this.transactionManager.getTransaction(transactionDefinition);
				// 删除临时表
				BorrowTenderTmpExample borrowTenderTmpExample = new BorrowTenderTmpExample();
				BorrowTenderTmpExample.Criteria criteria1 = borrowTenderTmpExample.createCriteria();
				criteria1.andNidEqualTo(orderId);
				criteria1.andUserIdEqualTo(userId);
				criteria1.andBorrowNidEqualTo(borrowNid);
				boolean tenderTempFlag = borrowTenderTmpMapper.deleteByExample(borrowTenderTmpExample) > 0 ? true : false;
				if (!tenderTempFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("删除borrowTenderTmp表失败");
				}
				System.out.println("用户:" + userId + "***********************************删除borrowTenderTmp，订单号：" + orderId);
				// 插入冻结表
				FreezeList record = new FreezeList();
				record.setAmount(accountDecimal);
				record.setBorrowNid(borrowNid);
				record.setCreateTime(nowTime);
				record.setOrdid(orderId);
				record.setUserId(userId);
				record.setRespcode(retCode);
				record.setTrxid("");
				record.setOrdid(orderId);
				record.setUsrcustid(accountId);
				record.setXfrom(1);
				record.setStatus(0);
				record.setUnfreezeManual(0);
				boolean freezeFlag = freezeListMapper.insertSelective(record) > 0 ? true : false;
				if (!freezeFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("插入冻结表失败！");
				}
				System.out.println("用户:" + userId + "***********************************插入FreezeList，订单号：" + orderId);
				BigDecimal perService = new BigDecimal(0);
				if (StringUtils.isNotEmpty(borrowStyle)) {
					BigDecimal serviceScale = serviceFeeRate;
					// 到期还本还息end/先息后本endmonth/等额本息month/等额本金principal
					if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
							|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
						perService = FinancingServiceChargeUtils.getMonthsPrincipalServiceCharge(accountDecimal, serviceScale);
					}
					// 按天计息到期还本还息
					else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
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
				borrowTender.setClient(1);// modify by cwyang 2017-5-3 出借平台为微信端
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
				Users users = getUsersByUserId(userId);
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
						System.out.println("用户:" + userId + "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + orderId);
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
						UsersExample userExample = new UsersExample();
						userExample.createCriteria().andUserIdEqualTo(userId).andInvestflagEqualTo(0);
						boolean userFlag = this.usersMapper.updateByExampleSelective(users, userExample) > 0 ? true : false;
						if (!userFlag) {
							result.put("message", "出借新手标失败！");
							result.put("status", 0);
							throw new Exception("更新新手标识失败，用户userId：" + userId);
						}
					} else {
						if (projectType == 4) {
							// 回滚事务
							result.put("message", "您已不是新手，不能出借新手标！");
							result.put("status", 0);
							throw new Exception("用户已不是新手出借，用户userId：" + userId);
						}
					}
					// 出借用户名
					borrowTender.setTenderUserName(users.getUsername());
					// 获取出借人属性
					// modify by zhangjp vip出借记录 start
					// UsersInfo userInfo = getUserInfo(userId);
					userInfo = getUsersInfoByUserId(userId);
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
									Users userss = getUsersByUserId(refUserId);
									if (userss != null) {
										borrowTender.setInviteUserId(userss.getUserId());
										borrowTender.setInviteUserName(userss.getUsername());
									}
									// 推荐人信息
									UsersInfo refUsers = getUsersInfoByUserId(refUserId);
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
									Users userss = getUsersByUserId(refUserId);
									if (userss != null) {
										borrowTender.setInviteUserId(userss.getUserId());
										borrowTender.setInviteUserName(userss.getUsername());
									}
									// 推荐人信息
									UsersInfo refUsers = getUsersInfoByUserId(refUserId);
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
				// 单笔出借的融资服务费
				borrowTender.setLoanFee(perService);
				if (StringUtils.isNotBlank(authCode)) {
					borrowTender.setAuthCode(authCode);
				}
				// add by zhangjp vip出借记录 start
				borrowTender.setRemark("现金出借");
				// add by zhangjp vip出借记录 end
				boolean trenderFlag = borrowTenderMapper.insertSelective(borrowTender) > 0 ? true : false;
				// CRM 接口调用删除 del by liuyang 20180112 start
//				// add by cwyang 2017-5-18 增加crm出借同步接口
//				int borrowID = 0;
//				try {
//					borrowID = borrowTender.getId();
//					System.out.println("===============crm同步borrowTender 开始! borrID is " + borrowID);
//					InvestSysUtils.insertBorrowTenderSys(borrowID + "");
//				} catch (Exception e) {
//					System.out.println("===============crm同步borrowTender 异常! borrID is " + borrowID);
//				}
				// CRM 接口调用删除 del by liuyang 20180112 end
				if (!trenderFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("插入出借表失败！");
				}
				System.out.println("用户:" + userId + "***********************************插入borrowTender，订单号：" + orderId);
				// add by zhangjp vip出借记录 start
				if (userInfo != null && userInfo.getVipId() != null) {
					VipUserTender vt = new VipUserTender();
					// 出借用户编号
					vt.setUserId(userId);
					// 出借用户vip编号
					vt.setVipId(userInfo.getVipId());
					// 出借编号
					vt.setTenderNid(orderId);
					vt.setSumVipValue(userInfo.getVipValue());
					vt.setAddTime(nowTime);
					vt.setAddUser(String.valueOf(userId));
					vt.setUpdateTime(nowTime);
					vt.setUpdateUser(String.valueOf(userId));
					vt.setDelFlg(0);
					this.vipUserTenderMapper.insertSelective(vt);
				}
				// add by zhangjp vip出借记录 end

				// 更新用户账户余额表
				Account accountBean = new Account();
				accountBean.setUserId(userId);
				// 出借人冻结金额增加
				accountBean.setBankFrost(accountDecimal);
				// 出借人可用余额扣减
				accountBean.setBankBalance(accountDecimal);
				// 江西银行账户余额 update by yangchangwei 2017-4-28
				// 此账户余额出借后应该扣减掉相应出借金额,sql已改
				accountBean.setBankBalanceCash(accountDecimal);
				// 江西银行账户冻结金额
				accountBean.setBankFrostCash(accountDecimal);
				Boolean accountFlag = this.adminAccountCustomizeMapper.updateOfTender(accountBean) > 0 ? true : false;
				if (!accountFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("用户账户信息表更新失败");
				}
				// 插入account_list表
				System.out.println("用户:" + userId + "***********************************更新account，订单号：" + orderId);
				Account account = this.getAccountByUserId(userId);
				AccountList accountList = new AccountList();
				accountList.setAmount(accountDecimal);
				/** 银行存管相关字段设置 */
				accountList.setAccountId(accountId);
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
				accountList.setCheckStatus(0);
				accountList.setTradeStatus(1);// 交易状态 update by cwyang 2017-4-26
												// 0:失败 1:成功
				accountList.setIsBank(1);
				accountList.setTxDate(Integer.parseInt(bean.getTxDate()));
				accountList.setTxTime(Integer.parseInt(bean.getTxTime()));
				accountList.setSeqNo(bean.getSeqNo());
				accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
				/** 非银行存管相关字段 */
				accountList.setAwait(new BigDecimal(0));
				accountList.setBalance(account.getBalance());
				accountList.setBaseUpdate(0);
				accountList.setCreateTime(nowTime);
				accountList.setFrost(account.getFrost());
				accountList.setInterest(new BigDecimal(0));
				accountList.setIp(ip);
				accountList.setIsUpdate(0);
				accountList.setNid(orderId);
				accountList.setOperator(userId + "");
				accountList.setRemark(borrowNid);
				accountList.setRepay(new BigDecimal(0));
				accountList.setTotal(account.getTotal());
				accountList.setTrade("tender");// 出借
				accountList.setTradeCode("frost");// 投标冻结后为frost
				accountList.setType(3);// 收支类型1收入2支出3冻结
				accountList.setUserId(userId);
				accountList.setWeb(0);
				accountList.setIsBank(1);// 是否是银行的交易记录(0:否,1:是)
				boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
				if (!accountListFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("用户账户交易明细表更新失败");
				}
				System.out.println("用户:" + userId + "***********************************插入accountList，订单号：" + orderId);
				// 更新borrow表
				Map<String, Object> borrowParam = new HashMap<String, Object>();
				borrowParam.put("borrowAccountYes", accountDecimal);
				borrowParam.put("borrowService", perService);
				borrowParam.put("borrowId", borrowId);
				boolean updateBorrowAccountFlag = borrowCustomizeMapper.updateOfBorrow(borrowParam) > 0 ? true : false;
				if (!updateBorrowAccountFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("borrow表更新失败");
				}
				System.out.println("用户:" + userId + "***********************************更新borrow表，订单号：" + orderId);
				List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
				if (calculates != null && calculates.size() > 0) {
					CalculateInvestInterest calculateNew = new CalculateInvestInterest();
					calculateNew.setTenderSum(accountDecimal);
					calculateNew.setId(calculates.get(0).getId());
					calculateNew.setCreateTime(GetDate.getDate(nowTime));
					this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
				}

				// 计算此时的剩余可出借金额
				BigDecimal accountWait = this.getBorrowByNid(borrowNid).getBorrowAccountWait();
				// 满标处理
				if (accountWait.compareTo(new BigDecimal(0)) == 0) {
					System.out.println("用户:" + userId + "***********************************项目满标，订单号：" + orderId);
					Map<String, Object> borrowFull = new HashMap<String, Object>();
					borrowFull.put("borrowId", borrowId);
					boolean fullFlag = borrowCustomizeMapper.updateOfFullBorrow(borrowFull) > 0 ? true : false;
					if (!fullFlag) {
						result.put("message", "出借失败！");
						result.put("status", 0);
						throw new Exception("满标更新borrow表失败");
					}
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
						result.put("message", "出借失败！");
						result.put("status", 0);
						throw new Exception("用户:" + userId + "***********************************冻结成功后处理afterChinaPnR：" + "数据库查不到 sendTypeList == null");
					}
					BorrowSendType sendType = sendTypeList.get(0);
					if (sendType.getAfterTime() == null) {
						result.put("message", "出借失败！");
						result.put("status", 0);
						throw new Exception("用户:" + userId + "***********************************冻结成功后处理afterChinaPnR：" + "sendType.getAfterTime()==null");
					}
					replaceMap.put("val_times", sendType.getAfterTime() + "");
					// 发送短信验证码
					SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_XMMB, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				} else if (accountWait.compareTo(BigDecimal.ZERO) < 0) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("用户:" + userId + "项目编号:" + borrowNid + "***********************************项目暴标");
				}
				// 事务提交
				this.transactionManager.commit(txStatus);
				result.put("message", "出借成功！");
				result.put("status", 1);
				return result;
			} catch (Exception e) {
				// 回滚事务
				System.out.println("================出借异常!============");
				this.transactionManager.rollback(txStatus);
				this.redisRecover(userId, borrowNid, txAmount);
				result.put("message", "出借失败！");
				result.put("status", 0);
			}
		}
		return result;
	}

	/**
	 * 根据userID获取account表信息
	 * 
	 * @param userId
	 * @return
	 */
	private Account getAccountByUserId(Integer userId) {
		Account account = null;
		AccountExample example = new AccountExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Account> exampleAccountList = this.accountMapper.selectByExample(example);
		if (exampleAccountList != null && exampleAccountList.size() == 1) {
			account = exampleAccountList.get(0);
		}
		return account;
	}

}
