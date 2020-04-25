/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.activity20180601;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author fuqiang
 * @version Activity0601Controller, v0.1 2018/5/29 14:09
 */
@Controller
@RequestMapping(Activity0601Define.REQUEST_MAPPING)
public class Activity0601Controller {

    @RequestMapping(Activity0601Define.INTRODUCE)
    public ModelAndView activity() {
        return new ModelAndView("/activity/active-201806");
    }
}
