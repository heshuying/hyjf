/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.activity20181220;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liushouyi
 * @version Activity20181220Controller, v0.1 2018/12/20 10:09
 */
@Controller
@RequestMapping(Activity20181220Define.REQUEST_MAPPING)
public class Activity20181220Controller {

    @RequestMapping(Activity20181220Define.INTRODUCE)
    public ModelAndView activity() {
        return new ModelAndView("/activity/active-20181220");
    }
}
