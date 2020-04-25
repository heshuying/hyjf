package com.hyjf.api.web.vip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.vip.VipBean;
import com.hyjf.vip.VipDefine;
import com.hyjf.vip.VipDetailResultBean;
import com.hyjf.vip.VipLevelCaptionResultBean;
import com.hyjf.vip.VipService;

@Controller
@RequestMapping(value = VipDefine.REQUEST_MAPPING)
public class VipServer extends BaseController {

    @Autowired
    private VipService vipService;
    /** 发布地址 */
    // private static String HOST_URL = PropUtils.getSystem("hyjf.api.web.url");
  
    
   
    
    /**
     * 
     * 用户vip等级详情页
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = VipDefine.USER_VIP_DETAIL_ACTIVE_INIT, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public VipDetailResultBean userVipDetailInit(HttpServletRequest request, HttpServletResponse response,VipBean paramBean) {
        LogUtil.startLog(VipDefine.class.toString(), VipDefine.USER_VIP_DETAIL_ACTIVE_INIT);

        VipDetailResultBean result=new VipDetailResultBean();
        if(!this.checkSign(paramBean, VipDefine.METHOD_USER_VIP_DETAIL_ACTIVE_INIT)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        result =vipService.userVipDetailInit(paramBean.getUserId(),request);
        
        LogUtil.endLog(VipDefine.class.toString(), VipDefine.USER_VIP_DETAIL_ACTIVE_INIT);
        return result;
    }
    
    /**
     * 
     * 会员等级说明页面
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public VipLevelCaptionResultBean vipLevelCaptionInit(HttpServletRequest request, HttpServletResponse response,VipBean paramBean) {
        LogUtil.startLog(VipDefine.class.toString(), VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT);
        VipLevelCaptionResultBean result=new VipLevelCaptionResultBean();
        if(!this.checkSign(paramBean, VipDefine.METHOD_VIP_LEVEL_CAPTION_ACTIVE_INIT)){
            result.setStatus(BaseResultBean.STATUS_FAIL);
            result.setStatusDesc("验签失败！");
            return result;
        }
        result =vipService.vipLevelCaptionInit(paramBean.getUserId(),request);
        LogUtil.endLog(VipDefine.class.toString(), VipDefine.VIP_LEVEL_CAPTION_ACTIVE_INIT);
        return result;
    }
    
 
}
