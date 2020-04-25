package com.hyjf.web.activity.activity201612;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;

@Controller("activity201612Controller")
@RequestMapping(value = Activity201612Define.REQUEST_MAPPING)
public class Activity201612Controller extends BaseController {
    @Autowired
    private Activity201612Service activity201612Service;
    /**
     * 
     * 纳觅旅游活动页面初始化
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = Activity201612Define.INIT_REQUEST_MAPPING)
    public ModelAndView getUserStatus(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(Activity201612Controller.class.toString(), Activity201612Define.INIT_REQUEST_MAPPING);
        //String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));
        //webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
        ModelAndView modelAndView = new ModelAndView(Activity201612Define.ACTIVITY201612_PATH);
        //Integer userId = WebUtils.getUserId(request);
        return modelAndView;
    }
}
