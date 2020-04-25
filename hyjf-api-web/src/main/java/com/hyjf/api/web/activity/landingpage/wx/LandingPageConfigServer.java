package com.hyjf.api.web.activity.landingpage.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.landingpage.wx.LandingPageDefine;
import com.hyjf.activity.landingpage.wx.LandingPageResultBean;
import com.hyjf.activity.landingpage.wx.LandingPageService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.WhereaboutsPageConfig;

@Controller
@RequestMapping(value = LandingPageDefine.REQUEST_MAPPING)
public class LandingPageConfigServer extends BaseController{

    @Autowired
    private LandingPageService landingPageService;
    
    @ResponseBody
    @RequestMapping(value = LandingPageDefine.GET_CONFIGINFO)
    public LandingPageResultBean getConfigInfo(HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getConfigInfo";
        LogUtil.startLog(this.getClass().getName(), methodName);
        LandingPageResultBean resultBean = new LandingPageResultBean();
        
        String id = request.getParameter("id");
        
        //验证请求参数
        if (Validator.isNull(id)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //加载配置
        WhereaboutsPageConfig config = landingPageService.getLandingConfig(Integer.parseInt(id));
        if(config == null){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("未查到着陆页配置信息");
            return resultBean;
        }
        resultBean.setStatusOn(config.getStatusOn());
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    
}
