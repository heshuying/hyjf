package com.hyjf.app.activity.activity188;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetJumpCommand;

/**
 * 
 * 注册送188红包
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年4月6日
 * @see 下午6:04:17
 */
@Controller
@RequestMapping(value = Activity188Define.REQUEST_MAPPING)
public class Activity188Controller extends BaseController {
    
    @RequestMapping(value = Activity188Define.INIT_REQUEST_MAPPING)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(Activity188Controller.class.toString(), Activity188Define.INIT_REQUEST_MAPPING);

        ModelAndView modelAndView = new ModelAndView(Activity188Define.ACTIVITY188_PATH);
        
        String sign = request.getParameter("sign");
        String version = request.getParameter("version");
        
        String jumpCommend = GetJumpCommand.getLinkJumpPrefix(request, version);
        
        Integer userId = SecretUtil.getUserIdNoException(sign);
        if(userId == null){
            modelAndView.addObject("regUrl", jumpCommend + "://jumpRegister/?");
        }else {
            modelAndView.addObject("regUrl", "");
        }
        
        return modelAndView;
    }
}
