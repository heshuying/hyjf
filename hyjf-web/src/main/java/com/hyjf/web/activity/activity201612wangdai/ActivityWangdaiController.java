package com.hyjf.web.activity.activity201612wangdai;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;

@Controller("activityWangdaiController")
@RequestMapping(value = ActivityWangdaiDefine.REQUEST_MAPPING)
public class ActivityWangdaiController extends BaseController {
    @Autowired
    private ActivityWangdaiService activityWangdaiService;
    /**
     * 
     * 网贷之家活动页面初始化
     * @author 
     * @return
     */
    @RequestMapping(value = ActivityWangdaiDefine.INIT_REQUEST_MAPPING)
    public ModelAndView getUserStatus(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityWangdaiController.class.toString(), ActivityWangdaiDefine.INIT_REQUEST_MAPPING);
        //String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
        //webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
        ModelAndView modelAndView = new ModelAndView(ActivityWangdaiDefine.ACTIVITY_WDZJ_PATH);
        request.getSession().setAttribute("utm_id", 2122);
        request.getSession().setAttribute("utm_source", "网贷之家");
        //Integer userId = WebUtils.getUserId(request);
        return modelAndView;
    }
}
