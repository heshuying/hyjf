/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.app.user.invest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.bank.service.user.tender.IncreaseInterestInvestService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.*;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppTenderServiceImpl extends BaseServiceImpl implements AppTenderService {
	private Logger logger = LoggerFactory.getLogger(AppTenderServiceImpl.class);

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	public static JedisPool pool = RedisUtils.getPool();
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private TransactionDefinition transactionDefinition;

	@Autowired
    private IncreaseInterestInvestService increaseInterestInvestService;

	@Autowired
	private MqService mqService;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;
	/**
	 * 
	 * 出借预插入
	 * 
	 * @param borrowNid
	 * @param OrdId
	 * @param userId
	 * @param account
	 * @param ip
	 * @return
	 * @author Administrator
	 * @throws Exception 
	 */
	@Override
	public boolean updateTenderLog(String borrowNid, String orderId, Integer userId, String account, String ip) throws Exception {
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
		boolean tenderTmpFlag = borrowTenderTmpMapper.insertSelective(temp) > 0 ? true : false;
		if (!tenderTmpFlag) {
			throw new Exception("插入borrowTenderTmp表失败，出借订单号：" + orderId);
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
			throw new Exception("插入borrowTenderTmpInfo表失败，出借订单号：" + orderId);
		}
		return tenderTmpInfoFlag;

	}


	/**
	 * 新用户新手标验证，标如果是新用户标，查看此用户是否有过出借记录，如果有返回true，提示不能投标了
	 *
	 * @param userId
	 * @param projectType
	 * @return
	 */

	@Override
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

	private UsersInfo getUserInfo(Integer userId) {
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
	 * 获取用户信息
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
	 * 获取项目类型
	 * 
	 * @param projectType
	 * @return
	 * @author b
	 */

	@Override
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
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(userIdInt);
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
		Users user = this.getUsers(Integer.parseInt(userId));
		if (StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
			cuc = this.getCouponUser(couponGrantId, userIdInt);
		}
		// 判断用户信息是否存在
		if (user == null) {
			return jsonMessage("用户信息不存在", "1");
		}
		// 判断用户是否禁用
		if (user.getStatus() == 1) {// 0启用，1禁用
			return jsonMessage("该用户已被禁用", "1");
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

		// 项目出借客户端
		if (platform.equals("0") && borrow.getCanTransactionPc().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionWei().equals("1")) {
				tmpInfo += " 微信端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		} else if (platform.equals("1") && borrow.getCanTransactionWei().equals("0")) {
			String tmpInfo = "";
			if (borrow.getCanTransactionAndroid().equals("1")) {
				tmpInfo += " Android端  ";
			}
			if (borrow.getCanTransactionIos().equals("1")) {
				tmpInfo += " Ios端  ";
			}
			if (borrow.getCanTransactionPc().equals("1")) {
				tmpInfo += " Pc端  ";
			}
			return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
		} else if (platform.equals("2") && borrow.getCanTransactionAndroid().equals("0")) {
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
		if (!(StringUtils.isNotEmpty(account) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 3)
				|| (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1))) {
			return jsonMessage("请输入出借金额", "1");
		}
		// 还款金额是否数值
		try {
			// 出借金额必须是整数
			Long accountInt = Long.parseLong(account);
			if ((accountInt == 0 && cuc == null) || (accountInt == 0 && cuc != null && cuc.getCouponType() == 2)) {
				return jsonMessage("出借金额不能为0元", "1");
			}
			if (accountInt != 0 && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
				return jsonMessage("该优惠券只能单独使用", "1");
			}
			if (accountInt < 0) {
				return jsonMessage("出借金额不能为负数", "1");
			}
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
			Account tenderAccount = this.getAccount(Integer.parseInt(userId));
			if (tenderAccount.getBankBalance().compareTo(accountBigDecimal) < 0) {
				return jsonMessage("余额不足，请充值！", "1");
			}

			// redis剩余金额不足
			if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
				return jsonMessage("剩余可投金额为" + balance + "元", "1");
			}
			// 根据项目标号获取相应的项目信息
			WebProjectDetailCustomize borrowDetail = webProjectListCustomizeMapper.selectProjectDetail(borrowNid);
			if (borrowDetail.getIncreaseMoney() != null && (accountInt - min) % Integer.parseInt(borrowDetail.getIncreaseMoney()) != 0 && accountBigDecimal.compareTo(new BigDecimal(balance)) == -1) {
				return jsonMessage("出借递增金额须为" + borrowDetail.getIncreaseMoney() + " 元的整数倍", "1");
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
			return jsonMessage("出借金额必须为整数", "1");
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
	 */
	@Override
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
			}else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) || queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2)) {
				logger.info("===============冻结记录不存在,不予处理========");
				deleteBorrowTenderTmp(orgOrderId);
				return true;
			} else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)) {
				logger.info("===============只能撤销投标状态为投标中的标的============");
				return false;
			} else {
				deleteBorrowTenderTmp(orgOrderId);
				return true;
			}
		}
	}

	private void deleteBorrowTenderTmp(String orgOrderId) {
		BorrowTenderTmpExample example =  new BorrowTenderTmpExample();
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
		Users investUser = this.getUsers(investUserId);
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

	// add by zhangjp 优惠券相关 start
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

	// add by zhangjp 优惠券相关 end

	/**
	 * 
	 * 获取项目类型名称
	 * 
	 * @param borrowProjectType
	 * @return
	 * @author Administrator
	 */
	@Override
	public String getProjectTypeName(BorrowProjectType borrowProjectType) {
		List<BorrowProjectType> list = getProjectTypeList();
		for (BorrowProjectType projectType : list) {
			if (projectType.getBorrowCd().equals(borrowProjectType.getBorrowCd())) {
				if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
					return borrowProjectType.getBorrowName();
				} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
					return borrowProjectType.getBorrowName();
				} else if ("HXF".equals(borrowProjectType.getBorrowClass())) {
					return borrowProjectType.getBorrowName();
				} else {
					return "汇直投";
				}
			}
		}
		return "汇直投";
	}

	/**
	 * 获取项目类型列表
	 * 
	 * @return
	 */
	public List<BorrowProjectType> getProjectTypeList() {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		return this.borrowProjectTypeMapper.selectByExample(example);
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

	/**
	 * 
	 * 查询用户出借记录
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	public int countBorrowTenderNum(Integer userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nid", nid);
		params.put("userId", userId);
		return webUserInvestListCustomizeMapper.countUserInvestNumBynid(params);
	}

	@Override
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
					logger.info("PC用户:" + userId + "***冻结前可投金额：" + accountRedisWait);
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
									status = "1";
									info.put("message", "redis操作成功！");
									info.put("status", status);
									logger.info("PC用户:" + userId + "***冻结前减redis：" + accountDecimal);
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
	
	/**
	 * 
	 * 回滚redis
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param account
	 * @author Administrator
	 */
	private boolean redisRecover(String borrowNid, Integer userId, String account) {
		JedisPool pool = RedisUtils.getPool();
		Jedis jedis = pool.getResource();
		BigDecimal accountBigDecimal = new BigDecimal(account);
		while ("OK".equals(jedis.watch(borrowNid))) {
			String balanceLast = RedisUtils.get(borrowNid);
			if (StringUtils.isNotBlank(balanceLast)) {
				logger.info("PC用户:" + userId + "***redis剩余金额：" + balanceLast);
				BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
				Transaction transaction = jedis.multi();
				transaction.set(borrowNid, recoverAccount.toString());
				List<Object> result = transaction.exec();
				if (result == null || result.isEmpty()) {
					jedis.unwatch();
				} else {
					String ret = (String) result.get(0);
					if (ret != null && ret.equals("OK")) {
						logger.info("用户:" + userId + "*******from redis恢复redis：" + account);
						return true;
					} else {
						jedis.unwatch();
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean updateTender(int userId, String borrowNid, String orderId, BankCallBean bean) {
		String authCode = bean.getAuthCode();// 出借结果授权码
		BorrowTenderExample example  =new BorrowTenderExample();
		example.createCriteria().andUserIdEqualTo(userId).andBorrowNidEqualTo(borrowNid).andNidEqualTo(orderId);
		BorrowTender borrowTender = new BorrowTender();
		borrowTender.setAuthCode(authCode);
		boolean tenderFlag = this.borrowTenderMapper.updateByExampleSelective(borrowTender, example)>0?true:false;
		//add by cwyang 2017-5-18 增加crm出借同步接口
		//del by liuyang 2017-09-14 删除crm出借同步接口
//		if (tenderFlag) {
//			int borrowID = 0;
//			try {
//				List<BorrowTender> resultTender = this.borrowTenderMapper.selectByExample(example);
//				if (resultTender!=null&&resultTender.size()==1) {
//					borrowID = resultTender.get(0).getId();
//				}
//				borrowID = borrowTender.getId();
//				logger.info("===============crm同步borrowTender 开始! borrID is " + borrowID);
//				InvestSysUtils.insertBorrowTenderSys(borrowID+"");
//			} catch (Exception e) {
//				logger.info("===============crm同步borrowTender 异常! borrID is " + borrowID);
//			}
//		}
		return tenderFlag;
	}
	
	/**
	 * 根据借款Id检索标的信息
	 * 
	 * @param borrowId
	 * @return
	 */
	@Override
	public BorrowWithBLOBs selectBorrowById(int borrowId) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(borrowId);
		List<BorrowWithBLOBs> result = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
		BigDecimal earnings = new BigDecimal("0");

		earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
				.divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
				.setScale(2, BigDecimal.ROUND_DOWN);

		return earnings;

	}

	
	@Override
	public JSONObject getCouponIsUsed(String orderId,String couponGrantId,int userId) {
		logger.info("================cwyang 异步优先执行.开始获取优惠券信息===========");
		CouponRealTenderExample realExample = new CouponRealTenderExample();
		realExample.createCriteria().andRealTenderIdEqualTo(orderId);
		//判断coupon_real_tender 是否存在关联信息 没有则直接返回失败!
		List<CouponRealTender> realTender = this.couponRealTenderMapper.selectByExample(realExample);
		if (realTender==null||realTender.size()==0) {
			TenderCouponInfo info = new TenderCouponInfo();
			info.setIsSuccess(CustomConstants.RESULT_FAIL);
			info.setStatus(CustomConstants.RESULT_FAIL);
			String result = JSONObject.toJSONString(info);
			return JSONObject.parseObject(result);
		}
		CouponUser couponUser = this.couponUserMapper.selectByPrimaryKey(Integer.parseInt(couponGrantId));
		TenderCouponInfo info = new TenderCouponInfo();
		info.setIsSuccess(CustomConstants.RESULT_SUCCESS);
		info.setStatus(CustomConstants.RESULT_SUCCESS);
		if (couponUser != null) {
			if (CustomConstants.USER_COUPON_STATUS_USED == couponUser.getUsedFlag()) {// 优惠券状态为已使用
				CouponTenderExample example = new CouponTenderExample();
				example.createCriteria().andCouponGrantIdEqualTo(couponUser.getId());
				List<CouponTender> couponTender = this.couponTenderMapper.selectByExample(example);
				String orderID = "";
				if (couponTender != null && couponTender.size() == 1) {
					orderID = couponTender.get(0).getOrderId();
				} else {
					logger.info("==============获取 couponTender 信息异常!=====");
					info.setIsSuccess(CustomConstants.RESULT_FAIL);
					info.setStatus(CustomConstants.RESULT_FAIL);
				}
				// 优惠券出借额度
				BigDecimal couponAccount = null;
				// 优惠券类别
				int couponTypeInt = -1;
				// 优惠券面值
				BigDecimal couponQuota = null;
				// 优惠券收益
				String couponInterest = null;
				// 项目标号
				String borrowNid = null;

				if (StringUtils.isNotEmpty(orderID)) {
					BorrowTenderCpnExample example2 = new BorrowTenderCpnExample();
					example2.createCriteria().andNidEqualTo(orderID);
					// 根据orderid获取优惠券的出借额度信息
					List<BorrowTenderCpn> tenderCpn = this.borrowTenderCpnMapper.selectByExample(example2);
					if (tenderCpn != null && tenderCpn.size() == 1) {
						couponAccount = tenderCpn.get(0).getAccount();
						borrowNid = tenderCpn.get(0).getBorrowNid();
					} else {
						logger.info("==============获取 borrowTenderCpn 信息异常!=====");
						info.setIsSuccess(CustomConstants.RESULT_FAIL);
						info.setStatus(CustomConstants.RESULT_FAIL);
					}
				} else {
					logger.info("==============获取 orderID 信息异常!=====");
					info.setIsSuccess(CustomConstants.RESULT_FAIL);
					info.setStatus(CustomConstants.RESULT_FAIL);
				}
				CouponConfigCustomizeV2 cuc = null;
				BigDecimal borrowApr = new BigDecimal(0);
				cuc = getCouponUser(couponGrantId, userId);
				if (cuc != null) {
					couponTypeInt = cuc.getCouponType();
					couponQuota = cuc.getCouponQuota();
					borrowApr = cuc.getCouponQuota();
				} else {
					logger.info("==============获取 CouponUser 信息异常!=====");
					info.setIsSuccess(CustomConstants.RESULT_FAIL);
					info.setStatus(CustomConstants.RESULT_FAIL);
				}
				// 根据项目编号获取相应的项目
				Borrow borrow = null;
				if (StringUtils.isNotEmpty(borrowNid)) {
					borrow = getBorrowByNid(borrowNid);
				} else {
					logger.info("==============获取 borrowNid 信息异常!=====");
					info.setIsSuccess(CustomConstants.RESULT_FAIL);
					info.setStatus(CustomConstants.RESULT_FAIL);
				}
				BigDecimal earnings = new BigDecimal(0);

				BigDecimal djBorrowApr = new BigDecimal(0);
				if (borrow != null) {
					djBorrowApr = borrow.getBorrowApr();
				} else {
					logger.info("==============获取 borrow 信息异常!=====");
					info.setIsSuccess(CustomConstants.RESULT_FAIL);
					info.setStatus(CustomConstants.RESULT_FAIL);
				}
				Integer borrowPeriod = borrow.getBorrowPeriod();
				DecimalFormat df = CustomConstants.DF_FOR_VIEW;
				BigDecimal couponInterestAccount = new BigDecimal(0);// 用来计算历史回报的本金
				if (couponTypeInt == 3) {// 代金券时用优惠券面值计算历史回报
					couponInterestAccount = cuc.getCouponQuota();
					borrowApr = borrow.getBorrowApr();
				} else {
					couponInterestAccount = couponAccount;
				}
				logger.info("==============cweyang 优惠券出借利率是 " + borrowApr);
				if (couponTypeInt == 1) {// 体验金的历史回报
					earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), djBorrowApr);
					couponInterest = df.format(earnings);
				} else {// 加息券和代金券的历史回报
					logger.info("============cwyang borrow.getBorrowStyle() is " + borrow.getBorrowStyle());
					switch (borrow.getBorrowStyle()) {
					case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
						// 计算历史回报
						earnings = DuePrincipalAndInterestUtils.getMonthInterest(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
						couponInterest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
						earnings = DuePrincipalAndInterestUtils.getDayInterest(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
						couponInterest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
						earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
								BigDecimal.ROUND_DOWN);
						couponInterest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
						earnings = AverageCapitalPlusInterestUtils.getInterestCount(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
						couponInterest = df.format(earnings);
						break;
					case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
						earnings = AverageCapitalUtils.getInterestCount(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
						couponInterest = df.format(earnings);
						break;
					default:
						break;
					}
				}
				info.setAccountDecimal(couponAccount.toString());
				// 优惠券收益
				info.setCouponInterest(String.valueOf(earnings));
				logger.info("===================cwyang 优惠券历史回报为: " + couponInterest);
				// 优惠券类别
				String couponTypeString = "";
				if (couponTypeInt == 1) {
					couponTypeString = "体验金";
				} else if (couponTypeInt == 2) {
					couponTypeString = "加息券";
				} else if (couponTypeInt == 3) {
					couponTypeString = "代金券";
				}
				info.setCouponTypeInt(couponTypeInt + "");
				info.setCouponType(couponTypeString);
				// 优惠券额度
				info.setCouponQuota(couponQuota.toString());
				// 跳转到成功画面

			} else {
				logger.info("==============未使用优惠券!=====");
				info.setIsSuccess(CustomConstants.RESULT_FAIL);
				info.setStatus(CustomConstants.RESULT_FAIL);
			}
		} else {
			logger.info("==============获取用户优惠券信息异常!=====");
			info.setIsSuccess(CustomConstants.RESULT_FAIL);
			info.setStatus(CustomConstants.RESULT_FAIL);
		}
		String result = JSONObject.toJSONString(info);
		return JSONObject.parseObject(result);
	}

	@Override
	public JSONObject userTender(Borrow borrow, BankCallBean bean) throws Exception {

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
		JSONObject result = null;
		//add by cwyang 增加redis防重校验 2017-08-02
		boolean checkTender = RedisUtils.tranactionSet("tender_orderid" + orderId, 20);
		if(!checkTender){//同步/异步 优先执行完毕
			result = new JSONObject();
			result.put("message", "同步/异步 优先执行完毕!");
			result.put("status", -1);
			return result;
		}
		// redis扣减
		result = this.redisTender(userId, borrowNid, txAmount);
		// redis结果状态
		String redisStatus = result.getString("status");
		if (redisStatus.equals("1")) {
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
				logger.info("用户:" + userId + "***********************************删除borrowTenderTmp，订单号：" + orderId);
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
				logger.info("用户:" + userId + "***********************************插入FreezeList，订单号：" + orderId);
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
						logger.info("用户:" + userId + "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + orderId);
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
				// 单笔出借的融资服务费
				borrowTender.setLoanFee(perService);
				//出借授权码
				if(StringUtils.isNotBlank(authCode)){
					borrowTender.setAuthCode(authCode);
				}
				// add by zhangjp vip出借记录 start
				borrowTender.setRemark("现金出借");
				// add by zhangjp vip出借记录 end
				boolean trenderFlag = borrowTenderMapper.insertSelective(borrowTender) > 0 ? true : false;
				// add by cwyang 2017-5-18 增加crm出借同步接口
				// del by liuyang 2017-09-14 删除crm出借同步接口
//				int borrowID = 0;
//				try {
//					borrowID = borrowTender.getId();
//					logger.info("===============crm同步borrowTender 开始! borrID is " + borrowID);
//					InvestSysUtils.insertBorrowTenderSys(borrowID + "");
//				} catch (Exception e) {
//					logger.info("===============crm同步borrowTender 异常! borrID is " + borrowID);
//				}
				// del by liuyang 2017-09-14 删除crm出借同步接口
				if (!trenderFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("插入出借表失败！");
				}
				logger.info("用户:" + userId + "***********************************插入borrowTender，订单号：" + orderId);

				// 插入产品加息
                if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
                    boolean increaseFlag = increaseInterestInvestService.insertIncreaseInterest(borrow,bean,borrowTender) > 0 ? true : false;
                    if (!increaseFlag) {
                        result.put("message", "出借失败！");
                        result.put("status", 0);
                        throw new Exception("插入产品加息表失败！");
                    }
                    logger.info("用户:" + userId + "***********************************插入产品加息，订单号：" + orderId);
                }


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
				Boolean accountFlag = this.adminAccountCustomizeMapper.updateAccountOfTender(accountBean) > 0 ? true : false;
				if (!accountFlag) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new RuntimeException("用户账户信息表更新失败");
				}
				// 插入account_list表
				logger.info("用户:" + userId + "***********************************更新account，订单号：" + orderId);
				Account account = this.getAccount(userId);
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
				logger.info("用户:" + userId + "***********************************插入accountList，订单号：" + orderId);
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
				logger.info("用户:" + userId + "***********************************更新borrow表，订单号：" + orderId);
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
					logger.info("用户:" + userId + "***********************************项目满标，订单号：" + orderId);
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

					// add by liushouyi nifa2 20181204 start
					// 发送满标状态埋点
					// 发送发标成功的消息队列到互金上报数据
					Map<String, String> params = new HashMap<String, String>();
					params.put("borrowNid", borrowNid);
					this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTED_DELAY_KEY, JSONObject.toJSONString(params));
					// add by liushouyi nifa2 20181204 end
				} else if (accountWait.compareTo(BigDecimal.ZERO) < 0) {
					result.put("message", "出借失败！");
					result.put("status", 0);
					throw new Exception("用户:" + userId + "项目编号:" + borrowNid + "***********************************项目暴标");
				}
				CommonSoaUtils.listedTwoInvestment(userId, accountDecimal);
				CommonSoaUtils.listInvestment(userId, accountDecimal, borrow.getBorrowStyle(), borrow.getBorrowPeriod());
				// 事务提交
				this.transactionManager.commit(txStatus);
				result.put("message", "出借成功！");
				result.put("status", 1);

				// MQ 出借触发奖励
				sendMQActivity(userId,orderId,accountDecimal,projectType);
				sendRrturnCashActivity(userId,orderId,accountDecimal,projectType);
				//add by cwyang 增加redis防重校验 2017-08-02
				RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
				// add by liuyang 神策数据统计 20180823 start
				try {
					// 出借成功后,发送神策数据统计MQ
					SensorsDataBean sensorsDataBean = new SensorsDataBean();
					sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
					sensorsDataBean.setOrderId(bean.getLogOrderId());
					sensorsDataBean.setEventCode("submit_tender");
					this.sendSensorsDataMQ(sensorsDataBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// add by liuyang 神策数据统计 20180823 end

				// add by liuyang 20181011 crm绩效统计修改 start
				// 当前时间> 20181201时,采用新的绩效统计方式,12月1日之后,此处判断可以删除
				try {
					//crm投资推送
					rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_POSTINTERFACE_CRM, JSON.toJSONString(borrowTender));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// add by liuyang 20181011 crm绩效统计修改 end
				return result;
			} catch (Exception e) {
				logger.info("标的:"+borrowNid+"==============出借异常!=======" + e.getMessage());
				e.printStackTrace();
				// 回滚事务
				this.transactionManager.rollback(txStatus);
				this.redisRecover(borrowNid, userId, txAmount);
				result.put("message", "出借失败！");
				result.put("status", 0);
			}
		}
		return result;
	}

	/**
	 * 发放活动奖励
	 * @param userId
	 * @param order
	 * @param investMoney
	 * @param projectType 项目类型
	 */
	private void sendMQActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderId", order);
		params.put("investMoney", investMoney.toString());
		//来源,1=新手标，2=散标，3=汇计划
		Integer productType = 2;
		//4 新手标
		if(4 == projectType){
			productType = 1;
		}
		params.put("productType", productType);
		logger.debug("sanbiao send mq activity userId="+userId+" orderId="+order);
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.MDIAU_ACTIVITY, JSONObject.toJSONString(params));
	}

	/**
	 * 纳觅返现活动
	 * @param userId
	 * @param order
	 */
	private void sendRrturnCashActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderId", order);
		params.put("investMoney", investMoney.toString());
		//来源,1=新手标，2=散标，3=汇计划
		Integer productType = 2;
		//4 新手标
		if(4 == projectType){
			productType = 1;
		}
		params.put("productType", productType);
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.RETURN_CASH_ACTIVITY, JSONObject.toJSONString(params));
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
		params.put("orderId",sensorsDataBean.getOrderId());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.SENSORS_DATA_ROUTINGKEY_HZT_INVEST, JSONObject.toJSONString(params));
	}
}