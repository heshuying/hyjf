package com.hyjf.app.activity.activity201612;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseController;
import com.hyjf.common.log.LogUtil;

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
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(Activity201612Controller.class.toString(), Activity201612Define.INIT_REQUEST_MAPPING);

        ModelAndView modelAndView = new ModelAndView(Activity201612Define.ACTIVITY201612_PATH);
        
        return modelAndView;
    }
}
