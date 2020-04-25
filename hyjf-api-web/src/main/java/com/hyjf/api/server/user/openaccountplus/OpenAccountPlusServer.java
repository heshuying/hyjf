package com.hyjf.api.server.user.openaccountplus;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.openaccount.UserOpenAccountService;
import com.hyjf.api.server.register.UserRegisterService;
import com.hyjf.api.server.util.ErrorCode;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.server.util.ServiceException;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BindUsers;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

/**
 * 外部服务接口：注册开户二合一
 *
 * @author yaoyong
 */
@RestController
@RequestMapping(OpenAccountPlusDefine.REQUEST_MAPPING)
public class OpenAccountPlusServer extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(OpenAccountPlusServer.class);

	@Autowired
	private UserOpenAccountService userOpenAccountService;
	@Autowired
	private UserRegisterService userRegisterService;
	@Autowired
	private BindUserService bindUserService;

	/**
	 * @param openAccountRequestBean
	 * @param request
	 * @return
	 */
	@RequestMapping(OpenAccountPlusDefine.SEND_SMS_ACTION)
	public OpenAccountPlusResult sendCode(@RequestBody OpenAccountPlusRequest openAccountRequestBean,
			HttpServletRequest request) {
		LogUtil.startLog(OpenAccountPlusServer.class.getName(), "sendCode");

		OpenAccountPlusResult resultBean = new OpenAccountPlusResult();
		// 手机号
		String mobile = openAccountRequestBean.getMobile();
		// 真实姓名
		String trueName = openAccountRequestBean.getTrueName();
		// 身份证号
		String idNo = openAccountRequestBean.getIdNo();
		// 推荐人
		String referee = openAccountRequestBean.getReferee();
		// 推广平台
		String utmId = openAccountRequestBean.getUtmId();
		// 注册渠道
		String channel = openAccountRequestBean.getChannel();
		// 注册平台
		String platform = openAccountRequestBean.getPlatform();
		// 机构编号
		String instCode = openAccountRequestBean.getInstCode();
		// 第三方绑定用户id
		Long bindUniqueId = openAccountRequestBean.getBindUniqueId();

		logger.info("入参 resultBean:" + JSONObject.toJSONString(resultBean));

		try {
			checkParameters(mobile, trueName, idNo, utmId, channel, platform, instCode);
		} catch (ServiceException e) {
			resultBean.setStatusForResponse(e.getErrorCode().getErrCode());
			resultBean.setStatusDesc(e.getErrorCode().getMsg());
			return resultBean;
		}

		UtmPlat utmPlat = null;
		try {
			// 根据渠道号检索推广渠道
			utmPlat = this.userRegisterService.selectUtmPlatByUtmId(utmId);
		} catch (Exception e) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
			resultBean.setStatusDesc("第三方操作平台非法");
			return resultBean;
		}
		if (utmPlat == null) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
			resultBean.setStatusDesc("第三方操作平台非法");
			return resultBean;
		}

		// 根据机构编号检索机构信息
		HjhInstConfig instConfig = this.userRegisterService.selectInstConfigByInstCode(instCode);
		if (instConfig == null) {
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000001);
			resultBean.setStatusDesc("机构编号非法");
			return resultBean;
		}

		// 如果有推荐人，校验推荐人正确性
		Users refereeUser = null;
		if(!StringUtils.isBlank(referee)){
			List<Users> users = userRegisterService.selectUserByRecommendName(referee);
			if (CollectionUtils.isEmpty(users)) {
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
				resultBean.setStatusDesc("推荐人无效");
				return resultBean;
			} else {
				refereeUser = users.get(0);
			}
		}

		// 验签
		if (!this.verifyRequestSign(openAccountRequestBean, OpenAccountPlusDefine.METHOD_SERVER_REGISTER)) {
			logger.info("----验签失败----");
			resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
			resultBean.setStatusDesc("验签失败！");
			return resultBean;
		}

		// 查询绑定关系
		BindUsers bindUser = bindUserService.getUsersByUniqueId(bindUniqueId, Integer.parseInt(utmId));

		// 根据手机号查询用户
		Users user = userOpenAccountService.selectUserByMobile(mobile);
		if (user == null) {
			// 检查用户身份证号是否开户（身份证存在代表开户成功）
			boolean checkIdNo = userOpenAccountService.checkIdNo(idNo);
			if (checkIdNo) {
				// 身份证不存在，在汇盈金服平台注册
				// 在平台进行注册
				Integer userId = this.userRegisterService.insertUserAction(mobile, instCode, request,
						instConfig.getInstType(), utmPlat, platform);
				if (userId == null || userId == 0) {
					logger.info("用户注册失败");
					resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
					resultBean.setStatusDesc("用户注册失败，请重试");
					return resultBean;
				}
				Users checkUser = this.userRegisterService.checkUserByUserId(userId);
				if (checkUser == null) {
					logger.info("根据用户ID获取用户信息表失败,用户ID:[" + userId + "]");
					resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
					resultBean.setStatusDesc("注册失败");
					return resultBean;
				}

				// 如果有推荐人，将推荐人保存
				if (refereeUser != null) {
					logger.info("save spread user ...");
					userRegisterService.inserSpreadUser(userId, refereeUser);
				}

				// 用户名
				String userName = checkUser.getUsername();

				try {
					// 调用江西银行短信验证码接口
					this.callBankSendSms(resultBean, mobile, userId, userName, channel);
				} catch (ServiceException e) {
					resultBean.setStatusForResponse(e.getErrorCode().getErrCode());
					resultBean.setStatusDesc(e.getErrorCode().getMsg());
					return resultBean;
				}


				if (bindUser == null) {
					// 绑定用户
					Boolean result = bindUserService.bindUser(userId, bindUniqueId+"", utmId);
					if (!result) {
						resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZCOOOO22);
						resultBean.setStatusDesc("绑定用户失败");
						return resultBean;
					}
				}

			} else {
				// 身份证号已存在
				resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000005);
				resultBean.setStatusDesc("身份证已开户，请保持手机号一致");
				return resultBean;
			}

		} else {
			Integer userId = user.getUserId();
			// 手机号已注册，判断用户身份证号是否存在
			boolean idNo1 = this.userOpenAccountService.checkIdNo(idNo);
			if (idNo1) {
				String userName = user.getUsername();
				try {
					// 调用江西银行短信验证码接口
					this.callBankSendSms(resultBean, mobile, userId, userName, channel);
				} catch (ServiceException e) {
					resultBean.setStatusForResponse(e.getErrorCode().getErrCode());
					resultBean.setStatusDesc(e.getErrorCode().getMsg());
					return resultBean;
				}
			} else {
				// 判断注册手机号与已存在的手机号是否相同
				Users users = this.userOpenAccountService.selectUserByIdNo(idNo);
				if (!mobile.equals(users.getMobile())) {
					resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000005);
					resultBean.setStatusDesc("手机号已被他人注册,请更换手机号");
					return resultBean;
				} else {
					resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000023);
					resultBean.setStatusDesc("已开户，无需再次开户");

					// 已开户的用户，需要返回电子账号和银联行号
					resultBean.setAccountId(bindUserService.getAccountId(users.getUserId()));
					resultBean.setPayAllianceCode(bindUserService.getPayAllianceCode(users.getUserId()));
					if (users.getIsSetPassword() != null){
						resultBean.setIsSetPassword(String.valueOf(users.getIsSetPassword()));
					}
					resultBean.setAutoInvesStatus(bindUserService.getAutoInvesStatus(users.getUserId()));
					resultBean.setTrueName(bindUserService.getTrueName(idNo));
					return resultBean;
				}
			}

			if (bindUser == null) {
				// 绑定关系不存在，则绑定
				Boolean result = bindUserService.bindUser(userId, bindUniqueId+"", utmId);
				if (!result) {
					resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZCOOOO22);
					resultBean.setStatusDesc("绑定用户失败");
					return resultBean;
				}
			}
		}
		return resultBean;
	}

	/**
	 * 参数合法性检查
	 * 
	 * @param mobile
	 * @param trueName
	 * @param idNo
	 * @param utmId
	 * @param channel
	 * @param platform
	 * @param instCode
	 */
	private void checkParameters(String mobile, String trueName, String idNo, String utmId, String channel,
			String platform, String instCode) {
		// 机构编号
		if (Validator.isNull(instCode)) {
			throw new ServiceException(ErrorCode.INST_CODE_ERROR);
		}

		// 开户平台
		if (Validator.isNull(platform)) {
			throw new ServiceException(ErrorCode.PLAT_FORM_ERROR);
		}
		// 开户平台只支持 0：PC 1：微官网 2：Android 3：iOS 4：其他
		if (!Arrays.asList("0", "1", "2", "3", "4").contains(platform)) {
			throw new ServiceException(ErrorCode.PLAT_FORM_ERROR);
		}

		// 手机号非空校验
		if (Validator.isNull(mobile)) {
			throw new ServiceException(ErrorCode.MOBILE_NULL_ERROR);
		}
		// 手机号合法校验
		if (!Validator.isMobile(mobile)) {
			throw new ServiceException(ErrorCode.MOBILE_VALID_ERROR);
		}

		// 真实姓名验证
		if (Validator.isNull(trueName)) {
			throw new ServiceException(ErrorCode.TRUENAME_NULL_ERROR);
		}
		// 身份证号验证
		if (Validator.isNull(idNo)) {
			throw new ServiceException(ErrorCode.IDNO_NULL_ERROR);
		}

		// 身份证号合法性验证
		boolean isPass = false;
		try {
			isPass = IdCard15To18.verify(idNo);
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.IDNO_VALID_ERROR);
		}
		if (!isPass) {
			throw new ServiceException(ErrorCode.IDNO_VALID_ERROR);
		}

		// 第三方操作平台
		if (Validator.isNull(utmId)) {
			throw new ServiceException(ErrorCode.UTM_NULL_ERROR);
		}

		// 渠道
		if (Validator.isNull(channel)) {
			throw new ServiceException(ErrorCode.CHANNEL_ERROR);
		}

		// 渠道只支持 APP:000001, 渠道PC:000002 渠道Wechat:000003
		if (!Arrays.asList("000001", "000002", "000003").contains(channel)) {
			throw new ServiceException(ErrorCode.CHANNEL_ERROR);
		}
	}

	/**
	 * 调用银行接口发送验证码
	 * 
	 * @param resultBean
	 * @param mobile
	 * @param userId
	 * @param userName
	 * @param utmId
	 */
	private void callBankSendSms(OpenAccountPlusResult resultBean, String mobile, Integer userId, String userName,
			String utmId) {
		// 发送短信订单
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		// 插入开户记录表
		boolean isUpdateFlag = userOpenAccountService.updateUserAccountLog(userId, userName, mobile, orderId);
		if (!isUpdateFlag) {
			logger.info("插入开户记录表失败,手机号:[" + mobile + "].");
			throw new ServiceException(ErrorCode.SEND_SMS_CODE_ERROR);
		}
		try {
			// 调用江西银行发送短信接口
			BankCallBean bankCallBean = this.userOpenAccountService.sendOpenAccountSms(userId, orderId,
					BankCallConstant.TXCODE_ACCOUNT_OPEN_PLUS, mobile, utmId);
			if (Validator.isNull(bankCallBean)) {
				logger.info("调用银行发送短信接口失败");
				throw new ServiceException(ErrorCode.SEND_SMS_CODE_ERROR);
			}
			// 短信发送返回结果码
			String retCode = bankCallBean.getRetCode();
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)
					&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
				logger.info("开户发送短信验证码,手机号:[" + mobile + "],银行返回结果:retCode:[" + retCode + "]");
				throw new ServiceException(ErrorCode.SEND_SMS_CODE_ERROR);
			}
			if (Validator.isNull(bankCallBean.getSrvAuthCode())
					&& !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
				logger.info("开户发送短信验证码,手机号:[" + mobile + "],银行返回结果:retCode:[" + retCode + "],前导业务授权码:"
						+ bankCallBean.getSrvAuthCode());
				throw new ServiceException(ErrorCode.SEND_SMS_CODE_ERROR);
			}
			// 业务授权码
			String srvAuthCode = bankCallBean.getSrvAuthCode();
			if (Validator.isNotNull(srvAuthCode)) {
				// 更新用户开户日志,更新前导业务授权码
				boolean openAccountLogFlag = this.userOpenAccountService.updateUserAccountLog(userId, orderId,
						srvAuthCode);
				if (!openAccountLogFlag) {
					logger.info("保存开户日志失败,更新前导业务授权码,手机号:[" + mobile + "],前导业务授权码:[" + srvAuthCode + "],订单号:[" + orderId
							+ "]");
					throw new ServiceException(ErrorCode.SEND_SMS_CODE_ERROR);
				}
			}
			logger.info("发送短信验证码成功,手机号:[" + mobile + "],前导业务授权码:[" + srvAuthCode + "],订单号:[" + orderId + "]");
			resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
			resultBean.setStatusDesc("发送短信验证码成功");
			resultBean.setOrderId(orderId);// 平台返回的唯一订单号
		} catch (Exception e) {
			logger.info("发送短信验证码异常,手机号:[" + mobile + "],异常信息:[" + e.getMessage() + "]");
			throw new ServiceException(ErrorCode.SEND_SMS_CODE_ERROR);
		}

	}

}
