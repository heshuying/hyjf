/**
 * Description:我要融资控制器
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.rzh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.web.BaseController;

@Controller
@RequestMapping(value = RzhDefine.REQUEST_MAPPING)
public class RzhController extends BaseController {

	@Autowired
	private RzhService rzhService;

	/**
	 * 跳转到汇借款页面
	 * @return
	 */
	@RequestMapping(RzhDefine.TO_LOANPAGE_ACTION)
	public ModelAndView rechargeRule() {
		return new ModelAndView(RzhDefine.HJK_PATH);
	}

	/**
	 * 汇借款申请
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value=RzhDefine.LOAN_APPLY_ACTION) //, method=RequestMethod.POST
	public ModelAndView loanApply(@ModelAttribute RzhBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_APPLY_ACTION);
		
		if(form.getYear()!=null && form.getMonth()!=null && form.getDate()!=null ){
			String month= form.getMonth().length()<2 ? "0"+form.getMonth():form.getMonth();
			String date = form.getDate().length()<2 ? "0"+form.getDate(): form.getDate();
			form.setGyear(form.getYear()+"年"+month+"月"+date+"日");
		}
		// 画面验证
		JSONObject info = new JSONObject();
		this.validatorFieldCheckLoan(info, form);
		
		if (ValidatorCheckUtil.hasValidateError(info)) {
			ModelAndView modelAndView= new ModelAndView(RzhDefine.LOAN_APPLY_ERROR_PATH);
			modelAndView.addObject(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			modelAndView.addObject(CustomConstants.DATA, null);
			modelAndView.addObject(CustomConstants.MSG, info);
			LogUtil.errorLog(RzhDefine.THIS_CLASS, RzhDefine.REGTIST_USER_FORM, "输入内容验证失败", null);
			return modelAndView;
		} else {
			ModelAndView modelAndView= new ModelAndView(RzhDefine.LOAN_APPLY_SUCCESS_PATH);
			form.setIp(GetCilentIP.getIpAddr(request));
			this.rzhService.saveUserLoan(form);
			modelAndView.addObject(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
			modelAndView.addObject(CustomConstants.DATA, form);
			modelAndView.addObject(CustomConstants.MSG, null);
			LogUtil.endLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_APPLY_ACTION);
			return modelAndView;
		}
	}
//	@ResponseBody
//	@RequestMapping(value=RzhDefine.LOAN_APPLY_ACTION, /*method = RequestMethod.POST,*/produces = "application/json; charset=utf-8")
//	public String loanApply(@ModelAttribute RzhBean form, HttpServletRequest request, HttpServletResponse response) {
//
//		LogUtil.startLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_APPLY_ACTION);
//		JSONObject ret = new JSONObject();
//		// 画面验证
//		JSONObject info = new JSONObject();
//		this.validatorFieldCheckLoan(info, form);
//		if (ValidatorCheckUtil.hasValidateError(info)) {
//			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
//			ret.put(CustomConstants.DATA, null);
//			ret.put(CustomConstants.MSG, info);
//			LogUtil.errorLog(RzhDefine.THIS_CLASS, RzhDefine.REGTIST_USER_FORM, "输入内容验证失败", null);
//			return JSONObject.toJSONString(ret, true);
//		} else {
//			form.setIp(GetCilentIP.getIpAddr(request));
//			this.rzhService.saveUserLoan(form);
//			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
//			ret.put(CustomConstants.DATA, form);
//			ret.put(CustomConstants.MSG, null);
//			ret.put("url", "rzh/loan_success");
//			LogUtil.endLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_APPLY_ACTION);
//			return JSONObject.toJSONString(ret, true);
//		}
//	}

//
//	/**
//	 * 我要融资发送短信验证码
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value=UserLoanDefine.LOAN_SEND_CODE_ACTION, /*method = RequestMethod.POST,*/produces = "application/json; charset=utf-8")
//	public String sendCode(HttpServletRequest request, HttpServletResponse response) {
//		
//		LogUtil.startLog(UserLoanDefine.THIS_CLASS, UserLoanDefine.LOAN_SEND_CODE_ACTION);
//		String mobile = request.getParameter("mobile");
//		JSONObject ret = new JSONObject();
//		JSONObject info = new JSONObject();
//		// 手机号码(必须,数字,最大长度)
//		ValidatorCheckUtil.validateMobile(info, UserLoanDefine.USER_MOBILE, UserLoanDefine.USER_MOBILE_ERROR, mobile, 11, true);
//		// 验证码
//		if (!ValidatorCheckUtil.hasValidateError(info)) {
//			// 获取短信模版
//			SmsTemplate smsTemplate = this.rzhService.getMessTemplate("loan_notice");
//			String template = smsTemplate.getTplContent();
//			// 生成验证码
//			String checkCode = "123456";// GetCode.getRandomCode(6);
//			String content = template.replace("[val_code]", checkCode);
//			SendMessageApi sendMess = new SendMessageApi();
//			// 发送短信验证码
//			int status = sendMess.sendMessage(mobile, content);
//			// 保存短信发送log日志
//			this.rzhService.AddMessage(content, mobile, checkCode, "短信验证码", status);
//			info.put("mobile", mobile);
//			info.put("checkCode", checkCode);
//			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
//			ret.put(CustomConstants.DATA,null );
//			ret.put(CustomConstants.MSG, info);
//			LogUtil.endLog(UserLoanDefine.THIS_CLASS, UserLoanDefine.LOAN_SEND_CODE_ACTION);
//			return JSONObject.toJSONString(ret);
//		} else {
//			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
//			ret.put(CustomConstants.DATA,null );
//			ret.put(CustomConstants.MSG, info);
//			LogUtil.errorLog(UserLoanDefine.THIS_CLASS, UserLoanDefine.LOAN_SEND_CODE_ACTION, "短信验证码发送失败", null);
//			return JSONObject.toJSONString(ret);
//		}
//
//	}
	

	/**
	 * 验证相应的用户验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=RzhDefine.LOAN_CHECK_CODE_ACTION, /*method = RequestMethod.POST,*/produces = "application/json; charset=utf-8")
	public String checkCode(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_CHECK_CODE_ACTION);
		String mobile = request.getParameter("mobile");
		String code = request.getParameter("code");
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		// 手机号码(必须,数字,最大长度)
		ValidatorCheckUtil.validateMobile(info, RzhDefine.USER_MOBILE, RzhDefine.USER_MOBILE_ERROR,mobile, 11, true);
		// 验证码
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_CODE, RzhDefine.USER_CODE_ERROR,code, 6, true);
		if (!ValidatorCheckUtil.hasValidateError(info)) {
			int cnt = this.rzhService.checkMobileCode(mobile, code);
			if (cnt < 0) {
				ValidatorCheckUtil.validateSpecialErrorMsg(info, RzhDefine.USER_CODE, RzhDefine.USER_CODE_ERROR, "null");
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA,null );
				ret.put(CustomConstants.MSG, info);
				LogUtil.errorLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_CHECK_CODE_ACTION, "验证码验证失败",null);
				return JSONObject.toJSONString(ret, true);
			}
		}
		info.put("mobile", mobile);
		info.put("code", code);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, null);
		ret.put(CustomConstants.MSG, info);
		LogUtil.endLog(RzhDefine.THIS_CLASS, RzhDefine.LOAN_CHECK_CODE_ACTION);
		return JSONObject.toJSONString(ret, true);

	}

	/**
	 * 画面校验
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckLoan(JSONObject info, RzhBean form) {

		// 姓名名校验
//		ValidatorCheckUtil.validateAlphaNumericRange(info, UserLoanDefine.USER_NAME, UserLoanDefine.USER_NAME_ERROR,
//				form.getName(), 2, 16, true);
		// 手机号码(必须,数字,最大长度)
		ValidatorCheckUtil.validateMobile(info, RzhDefine.USER_MOBILE, RzhDefine.USER_MOBILE_ERROR,
				form.getMobile(), 11, true);
		// 性别
		ValidatorCheckUtil.validateAlphaNumericRange(info, RzhDefine.USER_SEX, RzhDefine.USER_SEX_ERROR,
				form.getSex(), 1, 1, true);
		// 融资金额
		ValidatorCheckUtil.validateNumLength(info, RzhDefine.USER_FINANCE,
				RzhDefine.USER_FINANCE_ERROR, form.getPrice(), 10, 0, true);
		// 融资期限
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_FINPERIOD,
				RzhDefine.USER_FINPERIOD_ERROR, form.getApproach(), 4, true);
		// 融资用途
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_FINUSE, RzhDefine.USER_FINUSE_ERROR,
				form.getUse(), 30, true);
		// 质、抵押物
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_FINMORTGAGE,
				RzhDefine.USER_FINMORTGAGE_ERROR, form.getInfo(), 30, true);
		// 省
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_PROVINCE,
				RzhDefine.USER_PROVINCE_ERROR, form.getProvince(), 30, true);
		// 市
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_CITY, RzhDefine.USER_CITY_ERROR,
				form.getCity(), 30, true);
		ValidatorCheckUtil.validateMinLength(info, RzhDefine.USER_CITY, RzhDefine.USER_CITY_ERROR,
				form.getCity(), 2, true);
		// 地区
		ValidatorCheckUtil.validateMaxLength(info, RzhDefine.USER_AREA, RzhDefine.USER_AREA_ERROR,
				form.getArea(), 30, true);
		ValidatorCheckUtil.validateMinLength(info, RzhDefine.USER_AREA, RzhDefine.USER_AREA_ERROR,
				form.getArea(), 2, true);
		// 企业名称
		if (StringUtils.isNotEmpty(form.getGname())) {
			ValidatorCheckUtil.validateMaxLength(info, RzhDefine.COMPANY_NAME,
					RzhDefine.COMPANY_NAME_ERROR, form.getGname(), 30, true);
		}
		// 企业成立时间
		if (StringUtils.isNotEmpty(form.getGyear())) {
			ValidatorCheckUtil.validateDateFormat(info, RzhDefine.COMPANY_REG_TIME,
					RzhDefine.COMPANY_REG_TIME_ERROR, form.getGyear(), "yyyy年MM月dd日", true);
		}
		// 企业经营地址
		if (StringUtils.isNotEmpty(form.getGdress())) {
			ValidatorCheckUtil.validateMaxLength(info, RzhDefine.COMPANY_ADRESS,
					RzhDefine.COMPANY_ADRESS_ERROR, form.getGdress(), 30, true);
		}
		// 所属行业
		if (StringUtils.isNotEmpty(form.getGplay())) {
			ValidatorCheckUtil.validateMaxLength(info, RzhDefine.COMPANY_INDUSTRY,
					RzhDefine.COMPANY_INDUSTRY_ERROR, form.getGplay(), 30, true);
		}
		// 主营业务
		if (StringUtils.isNotEmpty(form.getGpro())) {
			ValidatorCheckUtil.validateMaxLength(info, RzhDefine.COMPANY_BUSINESS,
					RzhDefine.COMPANY_BUSINESS_ERROR, form.getGpro(), 30, true);
		}
		// 年营业额
		if (StringUtils.isNotEmpty(form.getGmoney())) {
			ValidatorCheckUtil.validateMaxLength(info, RzhDefine.COMPANY_YEAR_TURNOVER,
					RzhDefine.COMPANY_YEAR_TURNOVER_ERROR, form.getGmoney(),  30, true);
		}
		// 年利润额
		if (StringUtils.isNotEmpty(form.getGget())) {
			ValidatorCheckUtil.validateNumLength(info, RzhDefine.COMPANY_YEAR_PROFIT,
					RzhDefine.COMPANY_YEAR_PROFIT_ERROR, form.getGget(), 20, 0, true);
		}
		// 银行贷款
		if (StringUtils.isNotEmpty(form.getGpay())) {
			ValidatorCheckUtil.validateNumLength(info, RzhDefine.COMPANY_BANK_LOANS,
					RzhDefine.COMPANY_BANK_LOANS_ERROR, form.getGpay(), 20, 0, true);
		}
		
	}
	
	/**
	 * 跳转到汇借款页面
	 * @return
	 */
	@RequestMapping(RzhDefine.TO_INDEX_ACTION)
	public ModelAndView toIndexPage() {
		return new ModelAndView("/home/home");
	}
	

}
