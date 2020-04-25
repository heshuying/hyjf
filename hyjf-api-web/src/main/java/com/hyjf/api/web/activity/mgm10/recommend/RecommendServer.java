package com.hyjf.api.web.activity.mgm10.recommend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.activity.mgm10.recommend.RecommendBean;
import com.hyjf.activity.mgm10.recommend.RecommendDefine;
import com.hyjf.activity.mgm10.recommend.RecommendResultBean;
import com.hyjf.activity.mgm10.recommend.RecommendService;
import com.hyjf.activity.mgm10.recommend.UserFlagResultBean;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.InviteRecommend;
import com.hyjf.mybatis.model.customize.recommend.InviteInfoCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteRecommendPrizeCustomize;

@Controller
@RequestMapping(value = RecommendDefine.REQUEST_MAPPING)
public class RecommendServer extends BaseController{

    @Autowired
    private RecommendService recommendService ;
    
    @ResponseBody
    @RequestMapping(value = RecommendDefine.GET_USER_RECOMMEND_INFO)
    public RecommendResultBean getRecommendInfo(@ModelAttribute RecommendBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getRecommendInfo";
        LogUtil.startLog(this.getClass().getName(), methodName);
        RecommendResultBean resultBean = new RecommendResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_GET_USER_RECOMMEND_INFO)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //获取奖品列表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        
        //获取用户的推荐星信息
        paraMap.put("userId", form.getUserId());
        InviteRecommend recommend = recommendService.getRecommendInfo(paraMap);
        resultBean.setPrizeAllCount(0);
        resultBean.setPrizeSurplusCount(0);
        resultBean.setPrizeUsedCount(0);
        int recommendCount = 0;
        if(recommend != null){
            resultBean.setPrizeAllCount(recommend.getPrizeAllCount());
            resultBean.setPrizeUsedCount(recommend.getPrizeUsedCount());
            recommendCount = recommend.getPrizeAllCount() - recommend.getPrizeUsedCount();
            if(recommendCount > 0){
                resultBean.setPrizeSurplusCount(recommendCount);
            }
            
        }
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    
    @ResponseBody
    @RequestMapping(value = RecommendDefine.GET_USER_RECOMMEND_STAR_PRIZE_LIST)
    public RecommendResultBean getUserRecommendStarPrizeList(@ModelAttribute RecommendBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getUserRecommendStarPrizeList";
        LogUtil.startLog(this.getClass().getName(), methodName);
        RecommendResultBean resultBean = new RecommendResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_GET_USER_RECOMMEND_STAR_PRIZE_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //获取奖品列表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        
        //获取用户的推荐星信息
        paraMap.put("userId", form.getUserId());
        List<InviteInfoCustomize> inviteInfoCustomizes = recommendService.getUserRecommendStarPrizeList(paraMap);
        resultBean.setData(JSONArray.toJSON(inviteInfoCustomizes));
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    @ResponseBody
    @RequestMapping(value = RecommendDefine.GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST)
    public RecommendResultBean getUserRecommendStarUsedPrizeList(@ModelAttribute RecommendBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getUserRecommendStarUsedList";
        LogUtil.startLog(this.getClass().getName(), methodName);
        RecommendResultBean resultBean = new RecommendResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //获取奖品列表
        Map<String,Object> paraMap = new HashMap<String,Object>();
        
        //获取用户的推荐星信息
        paraMap.put("userId", form.getUserId());
        List<InviteRecommendPrizeCustomize> inviteRecommendPrizeCustomizes = recommendService.getUserRecommendStarUsedPrizeList(paraMap);
        resultBean.setData(JSONArray.toJSON(inviteRecommendPrizeCustomizes));
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    @ResponseBody
    @RequestMapping(value = RecommendDefine.GET_USER_FLAG)
    public UserFlagResultBean getUserFlag(@ModelAttribute RecommendBean form, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getUserFlag";
        LogUtil.startLog(this.getClass().getName(), methodName);
        UserFlagResultBean resultBean = new UserFlagResultBean();
        
        //验证请求参数
        if (Validator.isNull(form.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        //验签
        if(!this.checkSign(form, BaseDefine.METHOD_GET_USER_FLAG)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //获取用户标识
        recommendService.getUserFlag(resultBean,form.getUserId());
     
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
}
