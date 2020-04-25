package com.hyjf.web.api.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.exception.CheckException;
import com.hyjf.common.result.ResultBean;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DateEditor;
import com.hyjf.common.util.HtmlCleanEditor;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.web.api.result.ApiResultPageBean;
import com.hyjf.web.api.user.ApiUserPostBean;

/**
 * <p>
 * BaseController
 * </p>
 *
 * @author liubin
 */
public class ApiBaseController extends MultiActionController {

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}

	@ResponseBody
	@ExceptionHandler({ Exception.class })
	protected ModelAndView exception(Exception e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView("");
		mav.addObject("status", "1");
		mav.addObject("statusDesc", "请求发生异常");
		return mav;
	}

	/**
	 * 转换json对象
	 * 
	 * @param obj
	 * @return
	 */
	public JSONObject convertJsonObject(Object obj) {
		return (JSONObject) JSONObject.toJSON(obj);
	}

	/**
	 * 
	 * 特殊字符编码
	 * 
	 * @author renxingchen
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public String strEncode(String str) {
		try {
			str = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 组成返回信息
	 *
	 * @param message
	 * @param status
	 * @return
	 */
	public JSONObject jsonMessage(String message, String status) {
		JSONObject jo = null;
		if (Validator.isNotNull(message)) {
			jo = new JSONObject();
			jo.put(CustomConstants.APP_STATUS_DESC, message);
			jo.put(CustomConstants.APP_STATUS, status);
		}
		return jo;
	}

	/**
	 * 传入参数类型错误异常处理
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @author liubin
	 */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ModelAndView bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	ModelAndView modelAndView = new ModelAndView();
    	
    	// 设置接口结果页信息
    	ResultBean<?> resultBean = new ResultBean<>(); 
    	resultBean.setStatusInfo(ResultBean.FAIL, CheckUtil.getErrorMessage("param.error"));
    	modelAndView.addObject("resultForm", resultBean);
    	modelAndView.setViewName(ApiBaseDefine.API_RESULT_PTAH);
        
        return modelAndView;
    }
    
	/**
	 * 传入JSON错误异常处理
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @author liubin
	 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView httpMessageNotReadableExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	ModelAndView modelAndView = new ModelAndView();
    	
    	// 设置接口结果页信息
    	ResultBean<?> resultBean = new ResultBean<>(); 
    	resultBean.setStatusInfo(ResultBean.FAIL, CheckUtil.getErrorMessage("json.error"));
    	modelAndView.addObject("resultForm", resultBean);
    	modelAndView.setViewName(ApiBaseDefine.API_RESULT_PTAH);
        
        return modelAndView;
    }
    
	/**
	 * 传入信息验证错误异常处理
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @author liubin
	 */
    @ExceptionHandler({ CheckException.class })
    protected ModelAndView WebCheckExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	ModelAndView modelAndView = new ModelAndView();
    	
        if (ex instanceof CheckException) {
        	CheckException e = (CheckException)ex;
        	// 设置接口结果页信息
        	ResultBean<?> resultBean = new ResultBean<>(e.getData()); 
        	resultBean.setStatusInfo(ResultBean.FAIL, ex.getLocalizedMessage());
        	modelAndView.addObject("resultForm", resultBean);
        	modelAndView.setViewName(ApiBaseDefine.API_RESULT_PTAH);
        }else {
        	modelAndView.setViewName("/error/systemerror");
        }
        
        return modelAndView;
    }
    
//	/**
//	 * 异常处理
//	 * @param request
//	 * @param response
//	 * @param ex
//	 * @return
//	 * @author liubin
//	 */
//    @ExceptionHandler(Exception.class)
//    public ModelAndView exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
//    	ModelAndView modelAndView = new ModelAndView();
//    	
//    	CheckException e = (CheckException)ex;
//    	// 设置接口结果页信息
//    	ResultBean<?> resultBean = new ResultBean<>(e.getData()); 
//    	resultBean.setStatusInfo(ResultBean.FAIL, "系统异常");
//    	modelAndView.addObject("resultForm", resultBean);
//    	modelAndView.setViewName(ApiBaseDefine.API_RESULT_PTAH);
//        
//        return modelAndView;
//    }
    
	/**
	 * 错误页跳转用，初期化结果页数据（错误信息除外）
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @author liubin
	 */
    protected void initCheckUtil(ApiUserPostBean apiUserPostBean) {
    	// 结果页FormBean赋值
    	ApiResultPageBean apiResultPageBean = new ApiResultPageBean();
    	apiResultPageBean.setRetUrl(apiUserPostBean.getRetUrl());
    	apiResultPageBean.setButMes("返回");
    	// 结果页FormBean传入CheckUtil
    	CheckUtil.setData(apiResultPageBean);
    }
}
