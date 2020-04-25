package com.hyjf.api.web.borrow.tender;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;

/**
 * 用户出借记录外部用接口
 * 
 * @author zhangjinpeng
 * 
 */
@Controller
@RequestMapping(value = UserBorrowTenderDefine.BORROW_TENDER_REQUEST_MAPPING_CLASS)
public class UserBorrowTenderServer extends BaseController {

	private static final String THIS_CLASS = UserBorrowTenderServer.class
			.getName();

	@Autowired
	UserBorrowTenderService userBorrowTenderService;

	/**
	 * 查询用户出借记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = UserBorrowTenderDefine.BORROW_TENDER_REQUEST_ACTION, method = RequestMethod.POST)
	public JSONObject userBorrowTender(HttpServletRequest request,
			UserBorrowTenderBean paramBean) {
		LogUtil.startLog(THIS_CLASS,
				UserBorrowTenderDefine.BORROW_TENDER_REQUEST_ACTION);
		LogUtil.infoLog(THIS_CLASS, UserBorrowTenderDefine.BORROW_TENDER_REQUEST_ACTION, JSONObject.toJSONString(paramBean));
		JSONObject jsonObject = new JSONObject();
		
		try {
			// 信息校验
			jsonObject = this.validatorFieldCheckTender(paramBean);
			if(jsonObject == null || jsonObject.isEmpty()){
				// 校验用户是否存在
				Users user = userBorrowTenderService.checkLoginUser(paramBean);
				if(user!=null){
					// 取得用户出借记录
					jsonObject = userBorrowTenderService.getUserBorrowTender(paramBean, user);
					// 转json对象
					// jsonObject = (JSONObject) JSONObject.toJSON(userBorrowTender);
					// 状态（成功）
					jsonObject.put(UserBorrowTenderDefine.STATUS,
							UserBorrowTenderDefine.SUCCESS_CODE);
				}else{
					// 用户不存在
					jsonObject.put(UserBorrowTenderDefine.STATUS,
							UserBorrowTenderDefine.FAILED_CODE);
					jsonObject.put(UserBorrowTenderDefine.ERROR_MESSAGE,
							PropUtils.getMessage(UserBorrowTenderDefine.ERRORS_USERNAME_NO_EXISTS));
				}
			}else{
				// 校验失败
				jsonObject.put(UserBorrowTenderDefine.STATUS,
						UserBorrowTenderDefine.FAILED_CODE);
			}
			
		} catch (Exception e) {
			// 系统异常，通讯失败
			jsonObject.put(UserBorrowTenderDefine.STATUS,
					UserBorrowTenderDefine.FAILED_CODE);
			jsonObject.put(UserBorrowTenderDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserBorrowTenderDefine.ERRORS_EXCEPTION_INFO));
			return jsonObject;
		}
		LogUtil.endLog(THIS_CLASS,
				UserBorrowTenderDefine.BORROW_TENDER_REQUEST_ACTION);
		return jsonObject;
	}
	
	/**
	 * 信息校验
	 * @param info
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	private JSONObject validatorFieldCheckTender(UserBorrowTenderBean paramBean) throws Exception {
		JSONObject jo = new JSONObject();
		if (!this.checkSign(paramBean)) {
			// 签名验证失败
			jo.put(UserBorrowTenderDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserBorrowTenderDefine.ERRORS_SIGN));
			return jo;
		}
		
		// 来源渠道校验
		if (StringUtils.isEmpty(paramBean.getFrom())) {
			// 非空
			jo.put(UserBorrowTenderDefine.ERROR_MESSAGE,
					PropUtils.getMessage(UserBorrowTenderDefine.ERRORS_FROM));
			return jo;
		}

		// 用户名校验
		if (StringUtils.isEmpty(paramBean.getUsername())) {
			// 非空
			jo.put(UserBorrowTenderDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserBorrowTenderDefine.ERRORS_USERNAME_REQUIRED));
			return jo;
		}

		// 请求的时间戳
		if (StringUtils.isEmpty(paramBean.getTimestamp())) {
			jo.put(UserBorrowTenderDefine.ERROR_MESSAGE, PropUtils
					.getMessage(UserBorrowTenderDefine.ERRORS_TIMESTAMP_REQUIRED));
			return jo;
		}

		if (jo == null || jo.isEmpty()) {
			// 有效渠道合作商校验
			int cnt = userBorrowTenderService.countUserPlat(paramBean.getFrom());
			if (cnt == 0) {
				jo.put(UserBorrowTenderDefine.ERROR_MESSAGE,
						PropUtils.getMessage(UserBorrowTenderDefine.ERRORS_FROM));
				return jo;
			}
			
		}
		return jo;
	}

	/**
	 * 验证签名
	 * 
	 * @param paramBean
	 * @return
	 */
	private boolean checkSign(UserBorrowTenderBean paramBean) {

		String from = paramBean.getFrom();
		String username = paramBean.getUsername();
		String usernamep = paramBean.getUsernamep();
		String timestamp = paramBean.getTimestamp();
		String accessKey = PropUtils
				.getSystem(UserBorrowTenderDefine.RELEASE_TANLIULIU_ACCESSKEY);
		String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + from + timestamp + username
				+ usernamep + accessKey));
		return StringUtils.equals(sign, paramBean.getSign()) ? true : false;

	}
}
