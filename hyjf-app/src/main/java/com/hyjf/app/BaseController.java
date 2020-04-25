package com.hyjf.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DateEditor;
import com.hyjf.common.util.HtmlCleanEditor;
import com.hyjf.common.validator.Validator;

/**
 * <p>
 * BaseController
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
public class BaseController extends MultiActionController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
        binder.registerCustomEditor(Date.class, new DateEditor(true));
    }

    @ResponseBody
    @ExceptionHandler({ Exception.class })
    public ModelAndView exception(Exception e) {
        e.printStackTrace();
        ModelAndView mav = new ModelAndView("");
        mav.addObject("status", "1");
        mav.addObject("statusDesc", "请求发生异常");
        return mav;
    }

    /**
     * 
     * 特殊字符编码
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

}
