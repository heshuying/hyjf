package com.hyjf.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;
@Controller
@RequestMapping(value = ActivityListDefine.REQUEST_MAPPING)
public class ActivityListController extends BaseController {

	/** 类名 */
	public static final String THIS_CLASS = ActivityListController.class.getName();
	
//    private static String HOST_URL =  PropUtils.getSystem("hyjf.web.host");
    @Autowired
    private ActivityListService activityListService;
	
	/**
	 * 获取活动列表
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = ActivityListDefine.ACTIVITYLIST_ACTION, method = RequestMethod.POST) 
    public JSONObject queryActivityList(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(THIS_CLASS, ActivityListDefine.ACTIVITYLIST_ACTION);
    	JSONObject ret = new JSONObject();
    	ret.put("request", ActivityListDefine.RETURN_REQUEST);

        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台（Android或iOS）
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");
        //页码
		int page = Integer.valueOf(request.getParameter("page"));
		//每页显示条数
		int pageSize = Integer.valueOf(request.getParameter("pageSize"));

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign)) {
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
        
        ActivityListCustomize activityListCustomize =new ActivityListCustomize();
        activityListCustomize.setPlatform(platform);
        
		int offSet = (page - 1) * pageSize;
		if (offSet == 0 || offSet > 0) {
			activityListCustomize.setLimitStart(offSet);
		}
		if (pageSize > 0) {
			activityListCustomize.setLimitEnd(pageSize);
		}
        int count= activityListService.queryActivityCount(activityListCustomize);
        ret.put("status", "0");
        ret.put("statusDesc", "成功");
        ret.put("count", count+"");
        
        List<ActivityListBean> activityListBeans = activityListService.queryActivityList(activityListCustomize); 
        ret.put("activityList", activityListBeans);
        
    	LogUtil.endLog(THIS_CLASS, ActivityListDefine.ACTIVITYLIST_ACTION);
    	return ret;
    	
    }
	
    /**
     * 获取活动详情跳转
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ActivityListDefine.ACTIVITYDETAIL_ACTION) 
    public ModelAndView getActivityDetail(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, ActivityListDefine.ACTIVITYDETAIL_ACTION);
        ModelAndView modelAndView=new ModelAndView(ActivityListDefine.ERROR_PATH);
        // 版本号
        String version = request.getParameter("version");
        // 平台
        String platform = request.getParameter("platform");
        // 跳转路径
        String url = request.getParameter("url");
        // 唯一标识
        String sign = request.getParameter("sign");
        // token
        String token = request.getParameter("token");
       /* // order
        String order = request.getParameter("order");*/
        
        
        
        if(Validator.isNull(url)){
            modelAndView=new ModelAndView(ActivityListDefine.ERROR_PATH);
            return modelAndView;
        }
        
        if(url.indexOf("http://")==0){
            
            
            String parameterString=request.getQueryString();
            parameterString=parameterString.substring(parameterString.indexOf("url="));
            parameterString=parameterString.substring(parameterString.indexOf("http://"));
//            parameterString="http://10.10.2.122:8080/hyjf-app/activity/recommend/init.do?";
            modelAndView = new ModelAndView(ActivityListDefine.ACTIVITYDETAIL_OTHER_PATH);
            if(url.indexOf("hyjf")>0){
                if(url.indexOf("?")>0){
                    parameterString=parameterString+"&sign="+(sign==null?"":sign)+"&platform="+(platform==null?"":platform)+"&version="+(version==null?"":version);
                }else{
                    parameterString=parameterString+"?sign="+(sign==null?"":sign)+"&platform="+(platform==null?"":platform)+"&version="+(version==null?"":version);
                }
            }
            modelAndView.addObject("url", parameterString);
            return modelAndView;
        }
       

        // 检查参数正确性
        if (Validator.isNull(token)) {
            modelAndView=new ModelAndView(url);
           /* modelAndView.addObject("version", version);*/
            modelAndView.addObject("sign", sign);
           /* modelAndView.addObject("platform", platform);
            modelAndView.addObject("order", strEncode(order));*/
            return modelAndView;
        }
        
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String signToken="";
        // get请求，则需要对token进行解码
        try {
            signToken = URLDecoder.decode(signValue.getToken(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (token.equals(signToken)) {// 如果token是空
            modelAndView = new ModelAndView(url+"1");
            /*modelAndView.addObject("version", version);*/
            modelAndView.addObject("sign", sign);
            modelAndView.addObject("platform", platform);
            modelAndView.addObject("token", strEncode(token));
            /*modelAndView.addObject("order", strEncode(order));*/
            return modelAndView;
        }else{
            modelAndView=new ModelAndView(ActivityListDefine.ERROR_PATH);
            modelAndView.addObject("sign", sign);
            modelAndView.addObject("platform", platform);
            modelAndView.addObject("token", strEncode(token));
            return modelAndView;
        }

    }
}
