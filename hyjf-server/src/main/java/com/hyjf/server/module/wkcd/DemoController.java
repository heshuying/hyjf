package com.hyjf.server.module.wkcd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.server.BaseController;

/**
 * 文章列表页
 * 
 * @author
 *
 */
@Controller
@RequestMapping(value = "/demo")
public class DemoController extends BaseController {
    
	/**
	 * 文章列表维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
    @ResponseBody
	@RequestMapping("/init")
	public JSONObject init(HttpServletRequest request, HttpServletResponse response) {
	    JSONObject ret = new JSONObject();
	    String appId = request.getParameter("appId");
        String secretKey = request.getParameter("secretKey");
        String timeStamp = request.getParameter("timeStamp");
        String uniqueSign = request.getParameter("uniqueSign");
        String secretKeyMingwen = request.getAttribute("secretKey").toString();
        String requestObject = request.getParameter("requestObject");
        String requestObjectMingwen = request.getAttribute("requestObject").toString();
        Map<String, String> map = parseRequestJson(requestObjectMingwen);
        System.out.println("appId : "+appId);
        System.out.println("secretKey : "+secretKey);
        System.out.println("timeStamp : "+timeStamp);
        System.out.println("uniqueSign : "+uniqueSign);
        System.out.println("secretKeyMingwen : "+secretKeyMingwen);
        System.out.println("requestObject : "+requestObject);
        System.out.println("requestObjectMingwen : "+requestObjectMingwen);
        ret.put("status", "0");
        ret.put("statusDesc", "接口请求成功");
        ret.put("responseObject",JSONObject.toJSON(map));
        return ret;
	}
}
