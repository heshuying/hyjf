/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.activity201812;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liushouyi
 * @version Activity201812Controller, v0.1 2018/12/12 10:09
 */
@Controller
@RequestMapping(Activity201812Define.REQUEST_MAPPING)
public class Activity201812Controller {

    @RequestMapping(Activity201812Define.INTRODUCE)
    public ModelAndView activity() {
        return new ModelAndView("/activity/active-201812");
    }
}
