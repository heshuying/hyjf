package com.hyjf.api.server.test;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.util.ApiSignUtil;

@Controller
@RequestMapping(ChkValueDefine.REQUEST_MAPPING)
public class ChkValueServer extends BaseController {

    Logger _log = LoggerFactory.getLogger("ChkValue");

    @ResponseBody
    @RequestMapping(value = ChkValueDefine.GET_CHKVALUE_ACTION)
    public JSONObject getChkValue(ChkValueRequestBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        _log.info("设置交易密码");
        JSONObject result = new JSONObject();
        String chkValue = "";
        if (requestBean.getType().equals("setPassword")) {// 设置银行交易密码
            chkValue = requestBean.getChannel() + requestBean.getAccountId() + requestBean.getRetUrl() +  requestBean.getBgRetUrl() + requestBean.getTimestamp();
        } else if (requestBean.getType().equals("updatePassword")) {// 修改银行交易密码
            chkValue = requestBean.getChannel() + requestBean.getAccountId() + requestBean.getRetUrl() + requestBean.getBgRetUrl() + requestBean.getTimestamp() ;
        } else if (requestBean.getType().equals("cash")) {// 免密提现
            chkValue = requestBean.getAccountId() + requestBean.getCardNo() + requestBean.getAccount() + requestBean.getChannel() + requestBean.getTimestamp();
        } else if ("register".equals(requestBean.getType())) {
            // 用户注册
            chkValue = requestBean.getMobile() + requestBean.getChannel() + requestBean.getTimestamp();
        } else if ("sendCode".equals(requestBean.getType())) {
            // 开户发送短信验证码
            chkValue = requestBean.getMobile() + requestBean.getChannel() + requestBean.getTimestamp();
        } else if ("openaccount".equals(requestBean.getType())) {
            // 用户开户
            chkValue = requestBean.getMobile() + requestBean.getTrueName() + requestBean.getIdNo() + requestBean.getCardNo() + requestBean.getSmsCode() + requestBean.getOrderId() + requestBean.getChannel() + requestBean.getTimestamp();
        }
        else if ("withdraw".equals(requestBean.getType())) {
            // 提现
            chkValue = requestBean.getChannel() + requestBean.getAccountId() + requestBean.getAccount() + requestBean.getCardNo() + requestBean.getRetUrl() + requestBean.getBgRetUrl() + requestBean.getTimestamp();
        }
        System.out.println("chkValue:" + ApiSignUtil.encryptByRSAForRequest(requestBean.getInstCode(),chkValue));
        result.put("chkValue", ApiSignUtil.encryptByRSAForRequest(requestBean.getInstCode(),chkValue));
        return result;
    }

    @RequestMapping(value = ChkValueDefine.CALL_BACK_ACTION)
    @ResponseBody
    public JSONObject callBack(ChkValueRequestBean requestBean, HttpServletRequest request,
                               HttpServletResponse response) {
        _log.info("异步返回");
        JSONObject result = new JSONObject();
        result.put("success", true);
        // 打印请求参数
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    System.out.println("异步返回参数：" + paramName + "=" + paramValue);
                }
            }
        }
        return result;
    }

    @RequestMapping(value = ChkValueDefine.RETURN_ACTION)
    public ModelAndView returnAction(ChkValueRequestBean requestBean, HttpServletRequest request,
                                   HttpServletResponse response) {
        _log.info("同步返回");
        ModelAndView modelAndView=new ModelAndView("test/return");
        JSONObject result = new JSONObject();
        result.put("success", true);
        System.out.println("打印返回值......");
        Map map = request.getParameterMap();
        Set keSet = map.entrySet();
        for (Iterator itr = keSet.iterator(); itr.hasNext(); ) {
            Map.Entry me = (Map.Entry) itr.next();
            Object ok = me.getKey();
            Object ov = me.getValue();
            String[] value = new String[1];
            if (ov instanceof String[]) {
                value = (String[]) ov;
            } else {
                value[0] = ov.toString();
            }

            for (int k = 0; k < value.length; k++) {
                System.out.println(ok + "=" + value[k]);
            }
        }
        System.out.println("打印返回值end......");
        modelAndView.addObject("bean", JSONObject.toJSON(requestBean));
        return modelAndView;
    }

}
