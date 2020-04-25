package com.hyjf.web.activity.actdec2017;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;

@Controller
@RequestMapping(value = ActivityDecDefine.REQUEST_MAPPING)
public class ActivityDecController extends BaseController {
    
    /**
     * 
     * 双十二活动1
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = ActivityDecDefine.INIT_1)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityDecController.class.toString(), ActivityDecDefine.INIT_1);
        ModelAndView modelAndView = new ModelAndView(ActivityDecDefine.INIT_PATH_1);

        LogUtil.endLog(ActivityDecController.class.toString(), ActivityDecDefine.INIT_1);
        return modelAndView;
        
    }
    
    /**
     * 双十二活动2
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ActivityDecDefine.INIT_2)
    public ModelAndView init2(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityDecController.class.toString(), ActivityDecDefine.INIT_2);
        ModelAndView modelAndView = new ModelAndView(ActivityDecDefine.INIT_PATH_2);

        LogUtil.endLog(ActivityDecController.class.toString(), ActivityDecDefine.INIT_2);
        return modelAndView;
        
    }
    
}
