package com.hyjf.app.news.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;

@Controller
@RequestMapping(value=AppNewsConfigDefine.REQUEST_MAPPING)
public class AppNewsConfigController  extends BaseController{
	/** 类名 */
	public static final String THIS_CLASS = AppNewsConfigController.class.getName();
    @Autowired
    private AppNewsConfigService appNewsConfigService;
	
	/**
	 * 开关闭推送服务
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = AppNewsConfigDefine.UPDATE_APPNEWS_CONFIG_ACTION, method = RequestMethod.POST) 
    public JSONObject updateAppNoticeConfig(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(THIS_CLASS, AppNewsConfigDefine.UPDATE_APPNEWS_CONFIG_ACTION);
    	JSONObject ret = new JSONObject();
    	ret.put("request", AppNewsConfigDefine.RETURN_REQUEST);

        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");
        //1打开/0关闭推送标识
        String ifReceiveNotice = request.getParameter("isOpen");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(ifReceiveNotice)){    
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

//        // 判断sign是否存在
//        boolean isSignExists = SecretUtil.isExists(sign);
//        if (!isSignExists) {
//            ret.put("status", "1");
//            ret.put("statusDesc", "请求参数非法");
//            return ret;
//        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        
        // 取得用户iD
        Integer userId = SecretUtil.getUserId(sign);
        Users users= new Users();
        users.setUserId(userId);
        users.setIfReceiveNotice(Integer.valueOf(ifReceiveNotice));
        
        int result= this.appNewsConfigService.updateAppNewsConfig(users);
        if(result==1){
        	ret.put("status", 0);
        	ret.put("statusDesc", "成功");
        }else{
        	ret.put("status", 2);
        	ret.put("statusDesc", "开关闭推送服务出现异常，请重新操作！");
        }
        
    	LogUtil.endLog(THIS_CLASS, AppNewsConfigDefine.UPDATE_APPNEWS_CONFIG_ACTION);
    	return ret;
    	
    }
	
}




