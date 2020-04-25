package com.hyjf.app.activity.mgm10.prizeget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.activity.mgm10.recommend.RecommendDefine;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;

/**
 * 
 * 抽奖兑奖
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月10日
 * @see 下午3:18:55
 */
@Controller("prizeGetController")
@RequestMapping(value = PrizeGetDefine.REQUEST_MAPPING)
public class PrizeGetController extends BaseController {
    @Autowired
    private PrizeGetService prizeGetService;
    
    /**
     * 
     * 获取奖品列表
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = PrizeGetDefine.PRIZE_GET_INIT_ACTION, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        LogUtil.startLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.PRIZE_GET_INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(PrizeGetDefine.PRIZE_GET_PATH);
        modelAndView.addObject("host", webhost);
        
        String sign = request.getParameter("sign");
        System.out.println("sign in init: " + sign);
        
        //请求参数校验
        if(Validator.isNull(sign)){
            modelAndView = new ModelAndView(PrizeGetDefine.ERROR_PTAH);
            return modelAndView;
        }
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 22400008;
        
        prizeGetService.getPrizeChangeList(modelAndView, userId);
        prizeGetService.getPrizeDrawList(modelAndView, userId);
        modelAndView.addObject("sign", sign);
        modelAndView.addObject("inviteUserUrl",
                webhost + RecommendDefine.REQUEST_MAPPING +"/"+ RecommendDefine.INIT_ACTION + packageStr(request));
        LogUtil.endLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.PRIZE_GET_INIT_ACTION);
        return modelAndView;
    }
    
    /**
     * 
     * 奖品兑换校验
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeGetDefine.PRIZE_CHANGE_CHECK)
    public JSONObject prizeChangeCheck(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.PRIZE_CHANGE_CHECK);
        
        String sign = request.getParameter("sign");
        //请求参数校验
        if(Validator.isNull(sign)){
         // 返回错误信息
            
            
        }
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 22400008;
      
        String groupCode = request.getParameter("groupCode");
        String changeCount = request.getParameter("changeCount");
        
        JSONObject resultJson = prizeGetService.prizeChangeCheck(String.valueOf(userId), groupCode, changeCount);
        
        LogUtil.endLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.PRIZE_CHANGE_CHECK);
        
        return resultJson;
    }

    /**
     * 
     * 奖品兑换
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeGetDefine.DO_PRIZE_CHANGE)
    public JSONObject doPrizeChange(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.DO_PRIZE_CHANGE);
        
        String sign = request.getParameter("sign");
        //请求参数校验
        if(Validator.isNull(sign)){
            // 返回错误信息
            
            
        }
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 22400008;
      
        String groupCode = request.getParameter("groupCode");
        String changeCount = request.getParameter("changeCount");
        
        JSONObject resultJson = prizeGetService.doPrizeChange(String.valueOf(userId), groupCode, changeCount);
        
        LogUtil.endLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.DO_PRIZE_CHANGE);
        
        return resultJson;
    }

    /**
     * 
     * 抽奖
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeGetDefine.DO_PRIZE_DRAW)
    public JSONObject doPrizeDraw(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.DO_PRIZE_DRAW);
        
        String sign = request.getParameter("sign");
        
        //请求参数校验
        if(Validator.isNull(sign)){
            // 返回错误信息
            
        }
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 22400008;
      
        
        JSONObject resultJson = prizeGetService.doPrizeDraw(String.valueOf(userId));
        
        LogUtil.endLog(PrizeGetDefine.THIS_CLASS, PrizeGetDefine.DO_PRIZE_DRAW);
        
        return resultJson;
    }
    
    private String packageStr(HttpServletRequest request) {
        StringBuffer sbUrl = new StringBuffer();
        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台
        String platform = request.getParameter("platform");
        // token
        String token = request.getParameter("token");
        // 唯一标识
        String sign = request.getParameter("sign");
        // 随机字符串
        String randomString = request.getParameter("randomString");
        // Order
        String order = request.getParameter("order");
        sbUrl.append("?").append("version").append("=").append(version);
        sbUrl.append("&").append("netStatus").append("=").append(netStatus);
        sbUrl.append("&").append("platform").append("=").append(platform);
        sbUrl.append("&").append("randomString").append("=").append(randomString);
        sbUrl.append("&").append("sign").append("=").append(sign);
        if(token!=null&&token.length()!=0){
            sbUrl.append("&").append("token").append("=").append(strEncode(token));
        }
        if(order!=null&&order.length()!=0){
            sbUrl.append("&").append("order").append("=").append(strEncode(order));
        }
        
        return sbUrl.toString();
    }

}
