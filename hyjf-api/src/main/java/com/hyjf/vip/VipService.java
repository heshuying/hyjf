package com.hyjf.vip;

import javax.servlet.http.HttpServletRequest;

import com.hyjf.base.service.BaseService;

public interface VipService extends BaseService {
    /**
     * 
     * 初始化用户vip详情页面
     * @author pcc
     * @param userId
     * @param request 
     * @return 
     */
    VipDetailResultBean userVipDetailInit(Integer userId, HttpServletRequest request);
    /**
     * 初始化会员等级说明页面
     * @author pcc
     * @param userId
     */
    VipLevelCaptionResultBean vipLevelCaptionInit(Integer userId, HttpServletRequest request);
   
}
