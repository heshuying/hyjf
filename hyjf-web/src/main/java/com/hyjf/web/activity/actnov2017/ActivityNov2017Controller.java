package com.hyjf.web.activity.actnov2017;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;

@Controller
@RequestMapping(value = ActivityNov2017Define.REQUEST_MAPPING)
public class ActivityNov2017Controller extends BaseController {
    
    /**
     * 
     * 十月份活动
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = ActivityNov2017Define.INIT)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityNov2017Controller.class.toString(), ActivityNov2017Define.INIT);
        ModelAndView modelAndView = new ModelAndView(ActivityNov2017Define.INIT_PATH);

        LogUtil.endLog(ActivityNov2017Controller.class.toString(), ActivityNov2017Define.INIT);
        return modelAndView;
        
    }
    
}
