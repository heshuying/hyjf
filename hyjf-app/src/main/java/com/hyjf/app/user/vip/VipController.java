package com.hyjf.app.user.vip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;

@Controller
@RequestMapping(value = VipDefine.REQUEST_MAPPING)
public class VipController extends BaseController {

    @Autowired
    private VipService vipService;
    
    /**
     * 
     * 用户vip等级详情页
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = VipDefine.USER_VIP_DETAIL_ACTIVE_INIT)
    public ModelAndView userVipDetailInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(VipDefine.class.toString(), VipDefine.USER_VIP_DETAIL_ACTIVE_INIT);
        ModelAndView modelAndView = new ModelAndView(VipDefine.USER_VIP_DETAIL_PATH);
        //System.out.println("");
        // 唯一标识
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign);
//        System.out.println("用户vip等级详情页"+VipDefine.USER_VIP_DETAIL_ACTIVE_INIT+"        sign:"+sign);
//        Integer userId = 169025;[
        vipService.userVipDetailInit(modelAndView,userId,request);
        modelAndView.addObject("sign", sign);
        LogUtil.endLog(VipDefine.class.toString(), VipDefine.USER_VIP_DETAIL_ACTIVE_INIT);
        return modelAndView;
    }
    /**
     * 
     * 会员等级说明页面
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT)
    public ModelAndView vipLevelCaptionInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(VipDefine.class.toString(), VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT);
        ModelAndView modelAndView = new ModelAndView(VipDefine.VIP_LEVEL_CAPTION_PATH);
        // 唯一标识
        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserId(sign);
//        System.out.println("用户vip等级详情页"+VipDefine.USER_VIP_DETAIL_ACTIVE_INIT+"        sign:"+sign);
//        Integer userId = 169025;
        vipService.vipLevelCaptionInit(modelAndView,userId,request);
        modelAndView.addObject("sign", sign);
        LogUtil.endLog(VipDefine.class.toString(), VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT);
        return modelAndView;
    }
 
    /**
     * 
     * 会员特权详情页面
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = VipDefine.PRIVILEGE_DETAIL_ACTIVE_INIT)
    public ModelAndView privilegeDetailInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(VipDefine.class.toString(), VipDefine.PRIVILEGE_DETAIL_ACTIVE_INIT);
        ModelAndView modelAndView = new ModelAndView(VipDefine.PRIVILEGE_DETAIL_PATH);
        // 唯一标识
        LogUtil.endLog(VipDefine.class.toString(), VipDefine.PRIVILEGE_DETAIL_ACTIVE_INIT);
        return modelAndView;
    }
    
    

}
