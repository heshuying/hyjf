package com.hyjf.web.activity.actten2017;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;

@Controller
@RequestMapping(value = ActivityTen2017Define.REQUEST_MAPPING)
public class ActivityTen2017Controller extends BaseController {
    
    /**
     * 
     * 十月份活动
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = ActivityTen2017Define.INIT)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityTen2017Controller.class.toString(), ActivityTen2017Define.INIT);
        ModelAndView modelAndView = new ModelAndView(ActivityTen2017Define.INIT_PATH);

        LogUtil.endLog(ActivityTen2017Controller.class.toString(), ActivityTen2017Define.INIT);
        return modelAndView;
        
    }
    
}
