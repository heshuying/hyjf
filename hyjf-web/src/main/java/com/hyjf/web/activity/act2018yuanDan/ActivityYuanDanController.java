package com.hyjf.web.activity.act2018yuanDan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;

@Controller
@RequestMapping(value = ActivityYuanDanDefine.REQUEST_MAPPING)
public class ActivityYuanDanController extends BaseController {
    
    /**
     * 
     * 元旦活动2018
     * @author dzs
     * @return
     */
    @RequestMapping(value = ActivityYuanDanDefine.INIT_1)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityYuanDanController.class.toString(), ActivityYuanDanDefine.INIT_1);
        ModelAndView modelAndView = new ModelAndView(ActivityYuanDanDefine.INIT_PATH_1);

        LogUtil.endLog(ActivityYuanDanController.class.toString(), ActivityYuanDanDefine.INIT_1);
        return modelAndView;
        
    }

    
}
