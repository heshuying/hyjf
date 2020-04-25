package com.hyjf.web.activity.activity68;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;

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
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
        ModelAndView modelAndView = new ModelAndView(Activity68Define.ACTIVITY68_PATH);
        Integer userId = WebUtils.getUserId(request);
//        Integer userId = 22300935;
        modelAndView.addObject("XSHurl", webhost+Activity68Define.PROJECT_XSH_REQUEST_MAPPING);
        //status  用户登录状态 0：用户未登录   1：用户已领取  2：老用户
        if(userId==null||"".equals(userId)){
            modelAndView.addObject("status", 0);
            modelAndView.addObject("url", webhost+Activity68Define.REGIST_REQUEST_MAPPING);
            return modelAndView;
        }
        Users users = activity68Service.getUsers(userId);
        Integer regTime=users.getRegTime();
        String activityId=CustomConstants.REGIST_ACTIVITY_ID;
        ActivityList activityList=activity68Service.getActivityListById(activityId);
        Integer timeEnd=activityList.getTimeEnd();
        Integer TimeStart=activityList.getTimeStart();
        if(TimeStart<=regTime&&regTime<=timeEnd){
            modelAndView.addObject("url", webhost+Activity68Define.USER_COUPON_REQUEST_MAPPING);
            modelAndView.addObject("status", 1);
        }else{
            modelAndView.addObject("url", webhost+Activity68Define.PROJECT_REQUEST_MAPPING);
            modelAndView.addObject("status", 2);
        }
        return modelAndView;
    }
}
