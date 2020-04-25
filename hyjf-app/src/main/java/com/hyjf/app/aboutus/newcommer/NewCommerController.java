package com.hyjf.app.aboutus.newcommer;

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
 * 新手攻略
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年4月6日
 * @see 下午6:04:17
 */
@Controller
@RequestMapping(value = NewCommerDefine.REQUEST_MAPPING)
public class NewCommerController extends BaseController {
    
    @RequestMapping(value = NewCommerDefine.INIT_REQUEST_MAPPING)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewCommerController.class.toString(), NewCommerDefine.INIT_REQUEST_MAPPING);

        ModelAndView modelAndView = new ModelAndView(NewCommerDefine.NEWCOMMER_PATH);
        
        String sign = request.getParameter("sign");
        
        String jumpCommend = GetJumpCommand.getLinkJumpPrefix(request, null);
        
        Integer userId = SecretUtil.getUserIdNoException(sign);
        if(userId == null){
            modelAndView.addObject("regUrl", jumpCommend + "://jumpRegister/?");
            modelAndView.addObject("tenderUrl", jumpCommend + "://jumpRegister/?");
        }else {
            modelAndView.addObject("regUrl", "");
            modelAndView.addObject("tenderUrl", jumpCommend + "://jumpIndexPage");
        }
        
        return modelAndView;
    }
}
