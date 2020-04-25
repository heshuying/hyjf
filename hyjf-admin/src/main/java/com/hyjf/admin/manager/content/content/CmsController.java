package com.hyjf.admin.manager.content.content;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hyjf.admin.BaseController;

/**
 * 活动列表页
 * 
 * @author qingbing
 *
 */
@Controller
@RequestMapping(value = "page")
public class CmsController extends BaseController {

    /**
     * 活动列表维护画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    public static final String PATH="manager/content/content/";
    @RequestMapping(value = "{pageName}")
    public String toPage(@PathVariable("pageName") String pageName) {
       
        return PATH+pageName;
    }

}
