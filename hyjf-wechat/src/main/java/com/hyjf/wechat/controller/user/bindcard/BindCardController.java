package com.hyjf.wechat.controller.user.bindcard;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 绑卡 add by jijun 2018/03/26
 */
@Controller
@RequestMapping(value = BindCardDefine.REQUEST_MAPPING)
public class BindCardController extends BaseController {
	// 声明log日志
	private Logger logger = LoggerFactory.getLogger(BindCardController.class);

	/** THIS_CLASS */
	private static final String THIS_CLASS = BindCardController.class.getName();

	@Autowired
	private BindCardService userBindCardService;

	/**
	 * 绑定银行卡发送短信验证码
	 * 
	 * @param request
	 * @return
	 */
	@SignValidate
	@ResponseBody
	@RequestMapping(BindCardDefine.SEND_PLUS_CODE_ACTION)
	public BaseResultBean sendSmsCode(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, BindCardDefine.SEND_PLUS_CODE_ACTION);
		BindCardResultVo result = new BindCardResultVo();

		String mobile = request.getParameter("mobile"); // 手机号
		String cardNo = request.getParameter("cardNo"); // 银行卡号

		// 检查参数正确性
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(cardNo)) {
			return result.setEnum(ResultEnum.PARAM);
		}

		Integer userId = requestUtil.getRequestUserId(request);

		// 请求发送短信验证码
		BankCallBean bean = this.userBindCardService.cardBindPlusSendSms(userId,
				BankCallMethodConstant.TXCODE_CARD_BIND_PLUS, mobile, BankCallConstant.CHANNEL_WEI, cardNo);
		if (bean == null) {
			return result.setEnum(ResultEnum.ERROR_034);
		}
		// 返回失败
		if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
			if ("JX900651".equals(bean.getRetCode())) {
				result.setEnum(ResultEnum.SUCCESS);
				result.setCode(bean.getSrvAuthCode());
				return result;
			}
			return result.setEnum(ResultEnum.ERROR_034);
		}
		result.setEnum(ResultEnum.SUCCESS);
		result.setCode(bean.getSrvAuthCode());
		return result;
	}

	/**
	 * 用户绑卡
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(BindCardDefine.REQUEST_BINDCARD)
	public BaseResultBean bindCard(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, BindCardDefine.REQUEST_BINDCARD);
		BindCardResultVo result = new BindCardResultVo();
		// 唯一标识
//		String sign = request.getParameter("sign");
		String cardNo = request.getParameter("cardNo");
		Integer userId = requestUtil.getRequestUserId(request);
		if (Validator.isNull(cardNo)) {
			result.setStatus(ResultEnum.PARAM.getStatus());
			result.setStatusDesc("请求参数非法");
			return result;
		}
		// 检查验证码是否正确
		String code = request.getParameter("code");
		logger.info("输入验证码code is: {}", code);
		if (Validator.isNull(code)) {
			result.setStatus(ResultEnum.ERROR_031.getStatus());
			result.setStatusDesc("短信验证码不能为空！");
			return result;
		}

		// 检查验证码是否正确
		String lastSrvAuthCode = request.getParameter("lastSrvAuthCode");
		logger.info("输入验证码lastSrvAuthCode is: {}", lastSrvAuthCode);
		if (Validator.isNull(lastSrvAuthCode)) {
			result.setStatus(ResultEnum.PARAM.getStatus());
			result.setStatusDesc("请求参数非法");
			return result;
		}

		String mobile = request.getParameter("mobile");
		if (Validator.isNull(mobile)) {
			result.setStatus(ResultEnum.PARAM.getStatus());
			result.setStatusDesc("请求参数非法");
			return result;
		}

		BankOpenAccount accountChinapnrTender = userBindCardService.getBankOpenAccount(userId);
		if (accountChinapnrTender == null || Validator.isNull(accountChinapnrTender.getAccount())) {
			result.setStatus(ResultEnum.USER_ERROR_200.getStatus());
			result.setStatusDesc("用户未开户");
			return result;
		}
		UsersInfo usersInfo = userBindCardService.getUsersInfoByUserIdTrue(userId);
		// 调用汇付接口(4.2.2 用户绑卡接口)
		BankCallBean bean = new BankCallBean();
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(userId + "");
		bean.setLogRemark("用户绑卡增强");
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_CARD_BIND_PLUS);
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
		bean.setAccountId(accountChinapnrTender.getAccount());// 存管平台分配的账号
		bean.setIdType(BankCallConstant.ID_TYPE_IDCARD);// 证件类型01身份证
		bean.setIdNo(usersInfo.getIdcard());// 证件号
		bean.setName(usersInfo.getTruename());// 姓名
		bean.setMobile(mobile);// 手机号
		bean.setCardNo(cardNo);// 银行卡号
		bean.setLastSrvAuthCode(lastSrvAuthCode);
		bean.setSmsCode(code);
		bean.setUserIP(GetCilentIP.getIpAddr(request));// 客户IP
		LogAcqResBean logAcq = new LogAcqResBean();
		logAcq.setCardNo(cardNo);
		bean.setLogAcqResBean(logAcq);
		BankCallBean retBean = null;
		// 跳转到江西银行画面
		try {
			retBean = BankCallUtils.callApiBg(bean);
		} catch (Exception e) {
			logger.error("调用银行接口失败", e);
			result.setStatus(ResultEnum.ERROR_995.getStatus());
			result.setStatusDesc("调用银行接口失败！");
			return result;
		}
		if(StringUtils.isNotBlank(retBean.getRetCode())) {
			// 回调数据处理
			if (!(BankCallStatusConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode()))) {
				// 执行结果(失败)
				String message = this.userBindCardService.getBankRetMsg(retBean.getRetCode());
				result.setStatus(ResultEnum.ERROR_995.getStatus());
				result.setStatusDesc(message);
				return result;
			}
		} else {
			result.setStatus(ResultEnum.FAIL.getStatus());
			result.setStatusDesc("回调返回代码为空！");
			return result;
		}
		try {
			// 绑卡后处理
			this.userBindCardService.updateAfterBindCard(bean);
			List<BankCard> accountBankList = userBindCardService.getAccountBankByUserId(userId + "");

			if (accountBankList != null && accountBankList.size() > 0) {
				result.setStatus(ResultEnum.SUCCESS.getStatus());
				result.setStatusDesc(ResultEnum.SUCCESS.getStatusDesc());
				return result;
			} else {
				result.setStatus(ResultEnum.ERROR_995.getStatus());
				result.setStatusDesc("银行处理中，请稍后查看");
				return result;
			}

		} catch (Exception e) {
			// 执行结果(失败)
			logger.error("操作数据库失败，userid:" + userId, e);
			result.setStatus(ResultEnum.FAIL.getStatus());
			result.setStatusDesc("操作数据库失败");
			return result;
		}

	}
	
	@ResponseBody
	@RequestMapping(BindCardDefine.GET_BIND_CARD_INFO)
	public BaseResultBean getBindCardInfo(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, BindCardDefine.GET_BIND_CARD_INFO);
		
		BindCardResultVo result = new BindCardResultVo();
		Integer userId = requestUtil.getRequestUserId(request);
//		String userIdStr = request.getParameter("userId");
//		Integer userId = Integer.valueOf(userIdStr);
        // 判断用户是否登录
        if (userId != null && StringUtils.isNotBlank(userId + "") && userId > 0) {
            Users users = userBindCardService.getUsers(userId);
            if (null == users) {
            	result.setEnum(ResultEnum.ERROR_004);
            	return result;
            }
            UsersInfo usersInfo = userBindCardService.getUsersInfoByUserId(userId);
            if (null == usersInfo) {
            	result.setEnum(ResultEnum.ERROR_004);
            	return result;
            } 
            String trueUsername = usersInfo.getTruename();
            String trueCardNo = usersInfo.getIdcard();
            if (StringUtils.isBlank(trueUsername) || StringUtils.isBlank(trueCardNo)) {
            	result.setEnum(ResultEnum.USER_ERROR_200);
            	return result;
            }
            result.setTrueUsername(trueUsername);
            result.setTrueCardNo(trueCardNo);
            result.setCardNo(trueCardNo.replace(trueCardNo.substring(3, trueCardNo.length() - 4), "***********"));
            result.setUsername(trueUsername.replaceFirst(trueUsername.substring(0,1),"*"));
            result.setEnum(ResultEnum.SUCCESS);
        } else {
        	result.setEnum(ResultEnum.NOTLOGIN);
        	return result;
        }
		return result;
	}
}
