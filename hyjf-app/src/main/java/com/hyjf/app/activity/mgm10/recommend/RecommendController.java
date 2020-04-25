package com.hyjf.app.activity.mgm10.recommend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;

@Controller
@RequestMapping(value = RecommendDefine.REQUEST_MAPPING)
public class RecommendController extends BaseController {
    @Autowired
    private RecommendService recommendService;
    /**
     * 
     * 推荐星活动数据加载
     * @author yyc
     * @return
     */
    @RequestMapping(value = RecommendDefine.INIT_ACTION, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, RecommendDefine.REQUEST_HOME.length()) + "/";
        
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
        modelAndView.addObject("host",webhost);
        modelAndView.addObject("loginUrl", "hyjf://jumpLogin/?");
        String sign = request.getParameter("sign");
        String version = request.getParameter("version");
        
        
        
        String source= request.getParameter("source");
        if("advertisement".equals(source)){
            modelAndView.addObject("status", 0);
//            modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
            return modelAndView;
        }
        String token = StringUtils.EMPTY;
        System.out.println("登录跳转返回的sign："+sign);
        if(StringUtils.isNotEmpty(sign)){
        	String value = RedisUtils.get(sign);
            SignValue signValue = JSON.parseObject(value, SignValue.class);
            token = signValue.getToken();
        }
        
        if(StringUtils.isEmpty(token)){
            modelAndView.addObject("status", 0);
            modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
        }else{
            // 唯一标识
            Integer userId = SecretUtil.getUserId(sign);
//            Integer userId = 22400007;
            
            modelAndView = new ModelAndView(RecommendDefine.LOGIN_PATH);
            //获取用户推荐星信息
            recommendService.getRecommendInfo(modelAndView,userId,request);
            //获取用户状态标识
            recommendService.getUserFlag(modelAndView,userId,request);
            //获取分享信息
            recommendService.getShareInformation(modelAndView);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("inviteUrl", PropUtils.getSystem("hyjf.wechat.mgm10.invite.url").replace("****", userId.toString()));
        }
        if(version==null||"".equals(version)){
            modelAndView.addObject("versionStatus", 0);
        }else{ 
            if(new Integer(version.substring(0,5).replace(".",""))>131){
                modelAndView.addObject("versionStatus", 1);
            }else{
                modelAndView.addObject("versionStatus", 0);
            }
            
        }
        modelAndView.addObject("host",webhost);
        modelAndView.addObject("loginUrl", "hyjf://jumpLogin/?");
        return modelAndView;
    }
    
    /**
     * 
     * 推荐星获取记录
     * @author yyc
     * @return
     */
    @RequestMapping(value = RecommendDefine.GET_USER_RECOMMEND_STAR_PRIZE_LIST_ACTION, method = RequestMethod.GET)
    public ModelAndView getUserRecommendStarPrizeList(HttpServletRequest request, HttpServletResponse response) {
        
        
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.GET_USER_RECOMMEND_STAR_PRIZE_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
        String sign = request.getParameter("sign");
        //String version = request.getParameter("version");
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token = signValue.getToken();
        if(token==null||"".equals(token)){
            modelAndView.addObject("status", 0);
            modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
        }else{
            // 唯一标识
            Integer userId = SecretUtil.getUserId(sign);
//            Integer userId = 22400007;
            
            modelAndView = new ModelAndView(RecommendDefine.LIST_PATH);
            //获取用户推荐星信息
            recommendService.getUserRecommendStarPrizeList(modelAndView,userId);
            //获取用户推荐星信息
            recommendService.getUserRecommendStarUsedPrizeList(modelAndView,userId);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("flag", 0);
           
        }
        modelAndView.addObject("loginUrl", "hyjf://jumpLogin/?");
        return modelAndView;
    }
    
    /**
     * 
     * 推荐星使用记录
     * @author yyc
     * @return
     */
    @RequestMapping(value = RecommendDefine.GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST_ACTION, method = RequestMethod.GET)
    public ModelAndView getUserRecommendStarUsedPrizeList(HttpServletRequest request, HttpServletResponse response) {
        
        
        LogUtil.startLog(RecommendDefine.THIS_CLASS, RecommendDefine.GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST_ACTION);
        ModelAndView modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
        String sign = request.getParameter("sign");
        //String version = request.getParameter("version");
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token = signValue.getToken();
        
        if(token==null||"".equals(token)){
            modelAndView.addObject("status", 0);
            modelAndView = new ModelAndView(RecommendDefine.UNLOGIN_PATH);
        }else{
            // 唯一标识
            Integer userId = SecretUtil.getUserId(sign);
//            Integer userId = 22400007;
            
            modelAndView = new ModelAndView(RecommendDefine.LIST_PATH);
          //获取用户推荐星信息
            recommendService.getUserRecommendStarPrizeList(modelAndView,userId);
            //获取用户推荐星信息
            recommendService.getUserRecommendStarUsedPrizeList(modelAndView,userId);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("flag", 1);
        }
        modelAndView.addObject("loginUrl", "hyjf://jumpLogin/?");
        return modelAndView;
    }
}
