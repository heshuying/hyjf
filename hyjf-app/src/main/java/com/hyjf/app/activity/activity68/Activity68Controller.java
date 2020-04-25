package com.hyjf.app.activity.activity68;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;

@Controller("activity68Controller")
@RequestMapping(value = Activity68Define.REQUEST_MAPPING)
public class Activity68Controller extends BaseController {
    @Autowired
    private Activity68Service activity68Service;
    /**
     * 
     * 获取F1大师赛活动详情列表
     * @author yyc
     * @return
     */
    @RequestMapping(value = Activity68Define.GET_USER_STATUS_ACTION, method = RequestMethod.GET)
    public ModelAndView getUserStatus(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(Activity68Define.THIS_CLASS, Activity68Define.GET_USER_STATUS_ACTION);
        ModelAndView modelAndView = new ModelAndView(Activity68Define.ACTIVITY68_PATH);
        
        String sign = request.getParameter("sign");
        String version = request.getParameter("version");
        if(version==null||"".equals(version)){
        	modelAndView.addObject("registerUrl", "hyjf://jumpLogin/?");
        	modelAndView.addObject("versionStatus", 0);
        }else{
        	modelAndView.addObject("registerUrl", "hyjf://jumpRegister/?");	
        	modelAndView.addObject("versionStatus", 1);
        }
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token = signValue.getToken();
        
        modelAndView.addObject("XSHUrl", "hyjf://jumpXSH/?");
        
        modelAndView.addObject("newUserUrl", "hyjf://jumpCouponsList/?");
        modelAndView.addObject("oldUserUrl", "hyjf://jumpInvest/?");
        
                //status  用户登录状态 0：用户未登录   1：用户已领取  2：老用户
        if(token==null||"".equals(token)){
            modelAndView.addObject("status", 0);
            
            return modelAndView;
        }
        // 唯一标识
        Integer userId = SecretUtil.getUserId(sign);
        System.out.println("sign:"+sign);
//        Integer userId = null;
//        Integer userId = 22300935;
        
       

        Users users = activity68Service.getUsers(userId);
        Integer regTime=users.getRegTime();
        String activityId=CustomConstants.REGIST_ACTIVITY_ID;
        ActivityList activityList=activity68Service.getActivityListById(activityId);
        Integer timeEnd=activityList.getTimeEnd();
        Integer TimeStart=activityList.getTimeStart();
        if(TimeStart<=regTime&&regTime<=timeEnd){
            modelAndView.addObject("status", 1);
        }else{
            modelAndView.addObject("status", 2);
        }
        
        return modelAndView;
    }
}
