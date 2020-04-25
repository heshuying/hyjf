package com.hyjf.app.discovery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.aboutus.notice.NoticeDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * 发现首页初始化
 * @author zhangjp
 *
 */
@Controller
@RequestMapping(value = DiscoveryDefine.REQUEST_MAPPING)
public class DiscoveryController extends BaseController {

	@Autowired
	DiscoveryService discoveryService;
    /**
     * 
     * 发现首页初始化
     * @author zhangjp
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = DiscoveryDefine.INIT_ACTION)
    public ModelAndView couponActiveInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(DiscoveryDefine.class.toString(), DiscoveryDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(DiscoveryDefine.DISCOVERY_HOME_PATH);
        String sign = request.getParameter("sign");
        String platform = request.getParameter("platform");
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        // 版本号
        String version = request.getParameter("version");
        
        // app老版本判断是否显示下载页
        if(StringUtils.isEmpty(version)){
            modelAndView = new ModelAndView(NoticeDefine.NOTICE_IOS_PATH);
            return modelAndView;
        }
        
        if(version.length()>=5){
            version = version.substring(0, 5);
        }
        
        if(version.compareTo("1.4.0")<=0){
            modelAndView = new ModelAndView(NoticeDefine.NOTICE_IOS_PATH);
            return modelAndView;
        }

        modelAndView.addObject("sign",sign);
        modelAndView.addObject("token",token);
        modelAndView.addObject("platform",platform);
        Integer userId = SecretUtil.getUserIdNoException(sign);
        if(userId != null){
        	UsersInfo userInfo = this.discoveryService.getUsersInfoByUserId(userId);
        	boolean isVip = userInfo.getVipId() == null ? false : true;
        	modelAndView.addObject("isVip", isVip);
        	modelAndView.addObject("userId", userId);
        }
        LogUtil.endLog(DiscoveryDefine.class.toString(), DiscoveryDefine.INIT_ACTION);
        return modelAndView;
    }
    
    /**
     * 
     * 取得用户测评结果
     * @author zhangjp
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = DiscoveryDefine.GET_EVALATION_RESULT_ACTION)
    public JSONObject getEvalationResult(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(DiscoveryDefine.class.toString(), DiscoveryDefine.GET_EVALATION_RESULT_ACTION);
        JSONObject result = new JSONObject();
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserIdNoException(sign);
        if(userId != null){
        	String resultStr = discoveryService.getEvalationResultByUserId(userId);
        	result.put("evalationResult", resultStr);
        }else{
        	String message = discoveryService.getEvalationMessage();
        	result.put("evalationResult", message);
        }
        LogUtil.endLog(DiscoveryDefine.class.toString(), DiscoveryDefine.GET_EVALATION_RESULT_ACTION);
        return result;
    }
}
