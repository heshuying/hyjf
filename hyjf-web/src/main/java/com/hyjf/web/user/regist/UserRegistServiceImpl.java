package com.hyjf.web.user.regist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.util.mail.MailUtil;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.util.WebUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Service
public class UserRegistServiceImpl extends BaseServiceImpl implements UserRegistService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;


	Logger _log = LoggerFactory.getLogger(UserRegistServiceImpl.class);

	/**
	 * 根据推荐人手机号或userId判断推荐人是否存在
	 */
	@Override
	public int countUserByRecommendName(String param) {
		//推荐人是否有效  add by jijun 20180508
		boolean isRefferValid = false;
		UsersExample example = new UsersExample();
		if(Validator.isMobile(param)) {
			UsersExample.Criteria criteria = example.createCriteria();
			criteria.andMobileEqualTo(param);
			isRefferValid = true;
		}else {
			//推荐人不是手机号就是userId
			if(Validator.isNumber(param) && Long.valueOf(param)<=Integer.MAX_VALUE) {
				UsersExample.Criteria criteriaUserId = example.createCriteria();
				criteriaUserId.andUserIdEqualTo(Integer.valueOf(param));
				isRefferValid = true;
			}
		}
		
		int cnt = 0;
		if(isRefferValid) {
			cnt = usersMapper.countByExample(example);
		}
		return cnt;
	}

	public static void main(String[] args) {
		boolean aa = Validator.isMobile("13001230125");
		System.out.println(aa);
	}

	/**
	 * 保存用户信息
	 */
	@Override
	public Boolean insertUserAction(String mobile, String password, String verificationCode, String reffer,
			String loginIp, String platform, HttpServletRequest request, HttpServletResponse response, Integer userType) {
		int result = insertUserActionNew(mobile, password, verificationCode, reffer, loginIp, platform, null, null,
				null, null, request, response, userType);
		if (result != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检查短信验证码
	 */
	@Override
	public int checkMobileCode(String phone, String code) {
		int time = GetDate.getNowTime10();
		int timeAfter = time - 1800;
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
		cra.andPosttimeLessThanOrEqualTo(time);
		cra.andMobileEqualTo(phone);
		cra.andCheckcodeEqualTo(code);
		return smsCodeMapper.countByExample(example);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 *
	 * @param utmReg
	 * @return
	 * @author b
	 */

	@Override
	public int saveUtmReg(UtmReg utmReg) {
		return utmRegMapper.insertSelective(utmReg);
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @return
	 * @author b
	 */

	@Override
	public SmsConfig getSmsConfig() {
		SmsConfigExample example = new SmsConfigExample();
		List<SmsConfig> list = smsConfigMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param mobile
	 * @param reason
	 * @throws Exception
	 * @author b
	 */

	@Override
	public void sendEmail(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		MailUtil.sendMailToSelf(CustomConstants.EMAILPARAM_TPL_DUANXINCHAOXIAN, replaceStrs, null);

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param mobile
	 * @param reason
	 * @throws Exception
	 * @author b
	 */

	@Override
	public void sendSms(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
				CustomConstants.PARAM_TPL_DUANXINCHAOXIAN, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
	}

	/** 发送的短信验证码保存到数据库,使之前的验证码无效 */
	@Override
	public int saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status,
			String platform) {
		// 使之前的验证码无效
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andMobileEqualTo(mobile);
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(UserRegistDefine.CKCODE_NEW);
		statusList.add(UserRegistDefine.CKCODE_YIYAN);
		cra.andStatusIn(statusList);
		List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
		if (codeList != null && codeList.size() > 0) {
			for (SmsCode smsCode : codeList) {
				// if (smsCode.getCheckfor().equals(MD5.toMD5Code(mobile +
				// verificationCode + verificationType + platform))) {
				smsCode.setStatus(UserRegistDefine.CKCODE_FAILED);// 失效7
				smsCodeMapper.updateByPrimaryKey(smsCode);
				// }
			}
		}
		// 保存新验证码到数据库
		SmsCode smsCode = new SmsCode();
		smsCode.setCheckfor(MD5.toMD5Code(mobile + verificationCode + verificationType + platform));
		smsCode.setMobile(mobile);
		smsCode.setCheckcode(verificationCode);
		smsCode.setPosttime(GetDate.getMyTimeInMillis());
		smsCode.setStatus(status);
		smsCode.setUserId(0);
		return smsCodeMapper.insertSelective(smsCode);
	}

	/**
	 * 检查短信验证码
	 */
	@Override
	public int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, String platform,
			Integer searchStatus, Integer updateStatus) {
		int time = GetDate.getNowTime10();
		int timeAfter = time - 900;// 15分钟有效 900
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
		cra.andPosttimeLessThanOrEqualTo(time);
		cra.andMobileEqualTo(mobile);
		cra.andCheckcodeEqualTo(verificationCode);
		List<Integer> status = new ArrayList<Integer>();
		status.add(UserRegistDefine.CKCODE_NEW);
		status.add(searchStatus);
		cra.andStatusIn(status);
		List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
		if (codeList != null && codeList.size() > 0) {
			for (SmsCode smsCode : codeList) {
				if (smsCode.getCheckfor().equals(MD5.toMD5Code(mobile + verificationCode + verificationType + platform))) {
					smsCode.setStatus(updateStatus);// 已验8或已读9
					smsCodeMapper.updateByPrimaryKey(smsCode);
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}

	/**
	 * 保存用户信息
	 */
	@Override
	public int insertUserActionNew(String mobile, String password, String verificationCode, String reffer,
			String loginIp, String platform, String utm_id, String utm_source, String utm_medium, String utm_content,
			HttpServletRequest request, HttpServletResponse response, Integer userType) {
		int userId = 0;
		try {
			// 写入用户信息表
			Users user = new Users();
			String userName = getUniqueUsername(mobile);
			user.setInstCode("10000000");
			user.setIsInstFlag(0);
			user.setUsername(userName);
			user.setMobile(mobile);
			user.setLoginIp(loginIp);
			user.setRechargeSms(0);
			user.setWithdrawSms(0);
			user.setInvestflag(0);
			user.setInvestSms(0);
			user.setRecieveSms(0);
			user.setVersion(new BigDecimal("0"));
			user.setUserType(userType);		// 根据前台传值确认用户类型
			user.setIsSetPassword(0);// 是否设置了交易密码 0未设置
			// user.setEmail(ru.getEmail());
			List<Users> recommends = null;
			// 写入用户详情表
			UsersInfo userInfo = new UsersInfo();
			// 以下语句用来设置用户有无主单开始 2015年12月30日18:28:34 孙亮
			userInfo.setAttribute(0);// 默认为无主单
			// 1.注册成功时，推荐人关联
			// B1、用户在注册时，没有填写推荐人，也没有预注册，或预注册时没有关联推荐人，则推荐人为空；
			// B2、用户注册时，填写了推荐人，忽略关联推荐人，以填写的推荐人为准；
			// B3、用户注册时，没有填写推荐人，预注册表中，有关联推荐人，以关联的推荐人为准；
			PreRegistExample preRegistExample = new PreRegistExample();
			preRegistExample.createCriteria().andMobileEqualTo(mobile);
			List<PreRegist> preRegistList = preRegistMapper.selectByExample(preRegistExample);
			if (preRegistList != null && preRegistList.size() != 0) {
				for (int i = 0; i < preRegistList.size(); i++) {
					PreRegist model = preRegistList.get(i);
					model.setRegistFlag(1);
					model.setRegistTime(GetDate.getMyTimeInMillis());
					preRegistMapper.updateByPrimaryKey(model);
				}
			}
			if (StringUtils.isEmpty(reffer)) {
				if (preRegistList != null && preRegistList.size() != 0) {
					reffer = preRegistList.get(0).getReferrer();
				}
			}
			if (StringUtils.isNotEmpty(reffer)) {
				boolean isRefferValid = false;
				UsersExample exampleUser = new UsersExample();
				if (Validator.isMobile(reffer)) {
					UsersExample.Criteria criteria = exampleUser.createCriteria();
					criteria.andMobileEqualTo(reffer);
					isRefferValid = true;
				} else {
					//update by jijun 20180502 推荐人手机号不能转int
					if(Validator.isNumber(reffer) && Long.valueOf(reffer)<=Integer.MAX_VALUE) {
						UsersExample.Criteria criteria1 = exampleUser.createCriteria();
						Integer recommend = Integer.valueOf(reffer);
						criteria1.andUserIdEqualTo(recommend);
						isRefferValid = true;
					}
				}
				if(isRefferValid) {
					recommends = usersMapper.selectByExample(exampleUser);
				}
//				recommends = usersMapper.selectByExample(exampleUser);
				if (recommends != null && recommends.size() == 1) {
					UsersInfoExample puie = new UsersInfoExample();
					UsersInfoExample.Criteria puipec = puie.createCriteria();
					puipec.andUserIdEqualTo(recommends.get(0).getUserId());
					List<UsersInfo> pUsersInfoList = usersInfoMapper.selectByExample(puie);
					if (pUsersInfoList != null && pUsersInfoList.size() == 1) {
						// 如果该用户的上级不为空
						UsersInfo parentInfo = pUsersInfoList.get(0);
						if (Validator.isNotNull(parentInfo) && Validator.isNotNull(parentInfo.getAttribute())) {
							user.setReferrer(recommends.get(0).getUserId());
							user.setReferrerUserName(recommends.get(0).getUsername());
							if (Validator.equals(parentInfo.getAttribute(), new Integer(2))
									|| Validator.equals(parentInfo.getAttribute(), new Integer(3))) {
								// 有推荐人且推荐人为员工(Attribute=2或3)时才设置为有主单
								userInfo.setAttribute(1);
							}
						}
					}
				}
			} else {

			}
			// 以上语句用来设置用户有无主单结束 2015年12月30日18:28:34 孙亮
			if (StringUtils.isNotBlank(platform)) {
				user.setRegEsb(Integer.parseInt(platform)); // 账户开通平台 0pc 1微信
															// 2安卓 3IOS 4其他
			}
			
			int time = GetDate.getNowTime10();
			String codeSalt = GetCode.getRandomCode(6);
			user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
			user.setRegIp(loginIp);
			user.setRegTime(time);
			user.setLoginIp(loginIp);
			user.setLoginTime(time);
			user.setLastIp(loginIp);
			user.setLastTime(time);
			user.setLogintime(1);// 登录次数
			user.setStatus(0);

			user.setSalt(codeSalt);
			user.setBorrowSms(0);
			// user.setAccountEsb(0);
			user.setPid(0);
			user.setUsernamep("");
			user.setPtype(0);
			user.setOpenAccount(0);
			user.setBankOpenAccount(0);
			usersMapper.insertSelective(user);
			
			// 根据ip获取注册地址
//            if(StringUtils.isNotEmpty(loginIp)){
//                getAddress(loginIp, userInfo);
//            }
            
			userId = user.getUserId();
			// userInfo表
			userInfo.setUserId(userId);
			userInfo.setRoleId(1);
			userInfo.setMobileIsapprove(1);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setUpdateTime(time);
			userInfo.setPromoteway(0);
			userInfo.setIs51(0);
			userInfo.setIsStaff(0);
			userInfo.setDepartmentId(0);
			userInfo.setNickname("");
			userInfo.setBirthday("");
			userInfo.setSex(1);
			userInfo.setIdcard("");
			userInfo.setEducation("");
			userInfo.setAddress("");
			userInfo.setIntro("");
			userInfo.setInterest("");
			userInfo.setParentId(0);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setPromoteway(0);
			userInfo.setIsContact(false);
			System.out.println("saveRegistUser***********************************预插入userInfo："
					+ JSON.toJSONString(userInfo));
			usersInfoMapper.insertSelective(userInfo);
			// 写入用户账户表
			{
				Account account = new Account();
				account.setUserId(userId);
				// 银行存管相关
				account.setBankBalance(BigDecimal.ZERO);
				account.setBankBalanceCash(BigDecimal.ZERO);
				account.setBankFrost(BigDecimal.ZERO);
				account.setBankFrostCash(BigDecimal.ZERO);
				account.setBankInterestSum(BigDecimal.ZERO);
				account.setBankInvestSum(BigDecimal.ZERO);
				account.setBankWaitCapital(BigDecimal.ZERO);
				account.setBankWaitInterest(BigDecimal.ZERO);
				account.setBankWaitRepay(BigDecimal.ZERO);
				account.setBankTotal(BigDecimal.ZERO);
				account.setBankAwaitCapital(BigDecimal.ZERO);
				account.setBankAwaitInterest(BigDecimal.ZERO);
				account.setBankAwait(BigDecimal.ZERO);
				account.setBankWaitRepayOrg(BigDecimal.ZERO);//add by cwyang account表添加字段
				account.setBankAwaitOrg(BigDecimal.ZERO);//add by cwyang account表添加字段
				
				// 汇付相关
				account.setTotal(BigDecimal.ZERO);
				account.setIncome(BigDecimal.ZERO);
				account.setExpend(BigDecimal.ZERO);
				account.setBalance(BigDecimal.ZERO);
				account.setBalanceCash(BigDecimal.ZERO);
				account.setBalanceFrost(BigDecimal.ZERO);
				account.setFrost(BigDecimal.ZERO);
				account.setAwait(BigDecimal.ZERO);
				account.setRepay(BigDecimal.ZERO);
				account.setFrostCash(BigDecimal.ZERO);
				account.setRecMoney(BigDecimal.ZERO);
				account.setFee(BigDecimal.ZERO);
				account.setInMoney(BigDecimal.ZERO);
				account.setInMoneyFlag(0);
				// 注册时为account表增加
				// planTotal,planBalance,planFrost,planAccountWait,planCapitalWait,planInterestWait,planRepayInterest默认值
				account.setPlanAccedeTotal(BigDecimal.ZERO);
				account.setPlanBalance(BigDecimal.ZERO);
				account.setPlanFrost(BigDecimal.ZERO);
				account.setPlanAccountWait(BigDecimal.ZERO);
				account.setPlanCapitalWait(BigDecimal.ZERO);
				account.setPlanInterestWait(BigDecimal.ZERO);
				account.setPlanRepayInterest(BigDecimal.ZERO);
				account.setVersion(BigDecimal.ZERO);
				System.out.println("saveRegistUser***********************************预插入account："
						+ JSON.toJSONString(account));
				accountMapper.insertSelective(account);
			}
			if (recommends != null && recommends.size() == 1) {
				int referer = recommends.get(0).getUserId();
				SpreadsUsers spreadUser = new SpreadsUsers();
				spreadUser.setUserId(userId);
				spreadUser.setSpreadsUserid(referer);
				spreadUser.setAddip(loginIp);
				spreadUser.setAddtime(String.valueOf(time));
				spreadUser.setType("reg");
				spreadUser.setOpernote("reg");
				spreadUser.setOperation(userId + "");
				System.out.println("saveRegistUser***********************************预插入spreadUser："
						+ JSON.toJSONString(spreadUser));
				spreadsUsersMapper.insertSelective(spreadUser);
			}

			// String version = request.getParameter("version");
			// if (StringUtils.isNotBlank(version)) {
			// String[] shuzu = version.split("\\.");
			// if (shuzu.length >= 4) {
			// System.out.println(shuzu[3]);
			// try {
			// Integer utmplatSourceId = Integer.parseInt(shuzu[3]);// sourceid
			// // 查询推广渠道
			// UtmPlatExample example = new UtmPlatExample();
			// example.createCriteria().andSourceIdEqualTo(utmplatSourceId);
			// List<UtmPlat> utmPlatList =
			// utmPlatMapper.selectByExample(example);
			// if (utmPlatList != null && utmPlatList.size() > 0) {
			// UtmPlat plat = utmPlatList.get(0);
			// AppChannelStatisticsDetail detail = new
			// AppChannelStatisticsDetail();
			// detail.setSourceId(utmplatSourceId);
			// detail.setSourceName(plat.getSourceName() != null ?
			// plat.getSourceName() : "");
			// detail.setUserId(userId);
			// detail.setUserName(userName);
			// detail.setRegisterTime(new Date());
			// detail.setCumulativeInvest(BigDecimal.ZERO);
			// appChannelStatisticsDetailMapper.insert(detail);
			// }
			// } catch (Exception e) {
			// // e.printStackTrace();
			// }
			// //
			// }
			// }
			// 插入utmReg表

			String utmIdInCookie = WebUtils.getCookie(request, "utm_id");
			
			/*if (request.getSession().getAttribute("utm_id") != null && StringUtils.isNotEmpty(request.getSession().getAttribute("utm_id").toString())) {
				if (Validator.isNumber(request.getSession().getAttribute("utm_id").toString())) {
					// 从session中取
					UtmReg utmReg = new UtmReg();
					utmReg.setCreateTime(GetDate.getNowTime10());
					utmReg.setUtmId(Integer.parseInt(request.getSession().getAttribute("utm_id").toString()));
					utmReg.setUserId(userId);
					utmReg.setOpenAccount(0);
					utmReg.setBindCard(0);
					saveUtmReg(utmReg);
					System.out.println("updateRegistUser***********************************预插入utmReg："
							+ JSON.toJSONString(utmReg));
				}
			} else*/ if (StringUtils.isNotEmpty(request.getParameter("utm_id"))) {
				if (Validator.isNumber(request.getParameter("utm_id"))) {
					// 从request中取
					UtmReg utmReg = new UtmReg();
					utmReg.setCreateTime(GetDate.getNowTime10());
					utmReg.setUtmId(Integer.parseInt(request.getParameter("utm_id")));
					utmReg.setUserId(userId);
					utmReg.setOpenAccount(0);
					utmReg.setBindCard(0);
					saveUtmReg(utmReg);
					System.out.println("updateRegistUser***********************************预插入utmReg："
							+ JSON.toJSONString(utmReg));
				}
			} else if (org.apache.commons.lang3.StringUtils.isNotEmpty(utmIdInCookie)) {
				if (Validator.isNumber(utmIdInCookie)) {
					UtmReg utmReg = new UtmReg();
					utmReg.setCreateTime(GetDate.getNowTime10());
					utmReg.setUtmId(Integer.parseInt(utmIdInCookie));
					utmReg.setUserId(userId);
					utmReg.setOpenAccount(0);
					utmReg.setBindCard(0);
					saveUtmReg(utmReg);
					System.out.println("updateRegistUser***********************************预插入utmReg："
							+ JSON.toJSONString(utmReg));
				}
			}
			// 保存用户注册日志
			{
				UsersLog userLog = new UsersLog();
				userLog.setUserId(userId);
				userLog.setIp(loginIp);
				userLog.setEvent("register");
				userLog.setContent("注册成功");
				System.out.println("saveRegistUser***********************************预插入userLog："
						+ JSON.toJSONString(userLog));
				usersLogMapper.insertSelective(userLog);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtil.errorLog(UserRegistServiceImpl.class.getName(), "saveRegistUser", e);
			return 0;
		}
		return userId;
	}

	@Override
	public boolean checkIfSendCoupon() {
		// TODO 预留活动id
		String activityId = CustomConstants.REGIST_ACTIVITY_ID;
		ActivityList activityList = activityListMapper.selectByPrimaryKey(Integer.parseInt(activityId));
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
		Long time = new Date().getTime() / 1000;
		if (time < timeStart) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 取得推荐用户编号
	 */
	@Override
	public String getRefferUserId(String reffer) {
		String refferUserId = "";
		try {
			UsersExample exampleUser = new UsersExample();
			if (Validator.isMobile(reffer)) {

				UsersExample.Criteria criteria = exampleUser.createCriteria();
				criteria.andMobileEqualTo(reffer);
				List<Users> users = this.usersMapper.selectByExample(exampleUser);
				if (users != null && users.size() > 0) {
					refferUserId = users.get(0).getUserId().toString();
				}
			} else {
				UsersExample.Criteria criteria1 = exampleUser.createCriteria();
				criteria1.andUserIdEqualTo(Integer.valueOf(reffer));
				int count = this.usersMapper.countByExample(exampleUser);
				if (count == 1) {
					refferUserId = reffer;
				}
			}
		} catch (Exception e) {
			System.out.println("获取推荐人失败");
		}

		return refferUserId;
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
		params.put("presetProps",sensorsDataBean.getPresetProps());
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId",sensorsDataBean.getUserId());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_SENSORS_DATA_SIGN_UP, JSONObject.toJSONString(params));
	}

}
