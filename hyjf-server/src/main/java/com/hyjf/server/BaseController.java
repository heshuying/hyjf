package com.hyjf.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hyjf.common.util.DateEditor;
import com.hyjf.common.util.HtmlCleanEditor;
import com.hyjf.common.util.JsonMapper;
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
    
    private static JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new HtmlCleanEditor(true, true));
        binder.registerCustomEditor(Date.class, new DateEditor(true));
    }

    @ExceptionHandler({ Exception.class })
    public ModelAndView exception(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/systemerror");
        e.printStackTrace();
        return modelAndView;
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
            jo.put("message", message);
            jo.put("error", status);
        }
        return jo;
    }
    
    /**
     * json转Map对象
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> parseRequestJson(String json){
        Map<String, String> result = Maps.newHashMap();
        try {
            Map<String, Object> map = jsonMapper.fromJson(json, HashMap.class);
            for (Entry<String, Object> entry : map.entrySet()) {
                result.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
