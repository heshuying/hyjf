/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.vip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseService;

public interface VipService extends BaseService {
    /**
     * 
     * 初始化用户vip详情页面
     * @author pcc
     * @param modelAndView
     * @param userId
     * @param request 
     */
    void userVipDetailInit(ModelAndView modelAndView, Integer userId, HttpServletRequest request);
    /**
     * 初始化会员等级说明页面
     * @author pcc
     * @param modelAndView
     * @param userId
     */
    void vipLevelCaptionInit(ModelAndView modelAndView, Integer userId, HttpServletRequest request);
   
}
