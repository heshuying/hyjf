package com.hyjf.api.callcenter.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.callcenter.base.BaseDefine;
import com.hyjf.callcenter.base.BaseFormBean;
import com.hyjf.callcenter.base.BaseResultBean;
import com.hyjf.callcenter.user.UserBean;
import com.hyjf.callcenter.user.UserService;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.model.auto.Users;

/**
 * 呼叫中心用接口 Controller 基类
 * @author LiuBin
 */
	
public class CallcenterBaseController extends MultiActionController {

//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
//		binder.registerCustomEditor(Date.class, new DateEditor(true));
//	}
//
//	@ResponseBody
//	@ExceptionHandler({ Exception.class })
//	public ModelAndView exception(Exception e) {
//		e.printStackTrace();
//		ModelAndView mav = new ModelAndView("");
//		mav.addObject("status", "1");
//		mav.addObject("statusDesc", "请求发生异常");
//		return mav;
//	}

//	/**
//	 * 验证签名
//	 * 
//	 * @param paramBean
//	 * @return
//	 */
//	protected boolean checkSign(BaseBean paramBean, String methodName) {
//		// Class<? extends BaseBean> c = paramBean.getClass();
//		String sign = StringUtils.EMPTY;
//		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
//		if (BaseDefine.METHOD_APPLY_CHECK.equals(methodName)) {
//			// 用户购买会员--校验接口
//			ApplyBean reflectionBean = (ApplyBean) paramBean;
//			Integer userId = reflectionBean.getUserId();
//			Integer timestamp = reflectionBean.getTimestamp();
//			sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey));
//
//		} else if (BaseDefine.METHOD_VIP_APPLY.equals(methodName)) {
//			// 用户购买会员--购买
//			ApplyBean reflectionBean = (ApplyBean) paramBean;
//			Integer userId = reflectionBean.getUserId();
//			String platform = reflectionBean.getPlatform();
//			String callBackUrl = reflectionBean.getCallBackUrl();
//			Integer timestamp = reflectionBean.getTimestamp();
//			sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + platform + callBackUrl + timestamp + accessKey));
//		}
//		
//		return StringUtils.equals(sign, paramBean.getChkValue()) ? true : false;
//	}

//	/**
//	 * 
//	 * 特殊字符编码
//	 * 
//	 * @author renxingchen
//	 * @param token
//	 * @return
//	 * @throws Exception
//	 */
//	public String strEncode(String str) {
//		try {
//			str = URLEncoder.encode(str, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return str;
//	}
	
	/**
	 * 根据用户名或者手机号取得用户信息
	 * @param bean
	 * @param result
	 * @return
	 */
	@Autowired
	private UserService userService;
	protected Users getUser(UserBean bean, BaseResultBean result) {
		Users user = null;
		
		//唯一识别号验证
		if (!checkUniqueNo(bean, result)) {
			return user;
		}
		
		//分页验证
		if (!checkLimit(bean, result)) {
			return user;
		}
		
		//用户名和手机号验证
		if (!checkUser(bean, result)) {
			return user;
		}
		
		//查询用户信息
		user = userService.getUser(bean);
		return user;
	}

	/**
	 * 分页验证
	 * @param bean
	 * @param result
	 * @param user
	 */
	protected boolean checkLimit(UserBean bean, BaseResultBean result) {
		Integer limitStart = bean.getLimitStart();
		Integer limitSize = bean.getLimitSize();
		//非空check
		if (limitStart==null || limitSize==null) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "分页信息不能为空!");
			return false;
		}
		//最大记录数check
		if (limitSize>BaseDefine.CHK_SEARCH_MAXSIZE) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "单次检索记录数不能超过"+BaseDefine.CHK_SEARCH_MAXSIZE+"条");
			return false;
		}
		//检索记录数必须大于0 check
		if (limitSize<=0) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "检索记录数必须大于0");
			return false;
		}
		return true;
	}

	/**
	 * 唯一识别号验证
	 * @param bean
	 * @param result
	 * @param user
	 */
	protected boolean checkUniqueNo(BaseFormBean bean, BaseResultBean result) {
		String uniqueNo = bean.getUniqueNo();
		//非空check
		if (StringUtils.isBlank(uniqueNo)) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "唯一识别号不能为空!");
			return false;
		}
		//唯一识别号位数check
		if (uniqueNo.length() != BaseDefine.CHK_UNIQUENO_SIZE) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "唯一识别号必须为" + BaseDefine.CHK_UNIQUENO_SIZE + "位");
			return false;
		}
		return true;
	}
	
	/**
	 * @param bean
	 * @param result
	 */
		
	protected boolean checkUser(UserBean bean, BaseResultBean result) {
		String username = bean.getUserName();
		String mobile = bean.getMobile();
		//手机号格式check
		JSONObject errjson = new JSONObject();
		ValidatorCheckUtil.validateMobile(errjson, "手机号", "statusDesc", mobile, 11, false);
		if (ValidatorCheckUtil.hasValidateError(errjson)) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, errjson.getString("statusDesc"));
			return false;
		}
		//非空check
		if (StringUtils.isBlank(username) && StringUtils.isBlank(mobile)) {
			result.statusMessage(BaseResultBean.STATUS_FAIL, "用户名和手机号不能都为空!");
			return false;
		}
		return true;
	}
	
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BaseResultBean bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	BaseResultBean errBean = new BaseResultBean();
    	errBean.statusMessage(BaseResultBean.STATUS_FAIL, "传入参数类型错误。");
        return errBean;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public BaseResultBean httpMessageNotReadableExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	BaseResultBean errBean = new BaseResultBean();
    	errBean.statusMessage(BaseResultBean.STATUS_FAIL, "传入JSON错误。");
        return errBean;
    }
    
    @ExceptionHandler
    @ResponseBody
    public BaseResultBean handleAndReturnDate(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	BaseResultBean errBean = new BaseResultBean();
    	errBean.statusMessage(BaseResultBean.STATUS_FAIL, "接口调用发生异常，请联系服务方。");
        return errBean;
    }
}
