package com.hyjf.web.activity.recommend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteInfoCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteRecommendPrizeCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class RecommendServiceImpl extends BaseServiceImpl implements RecommendService {

    // 优惠券出借接口
    private static final String GET_RECOMMEND_INFO = "recommend/getRecommendInfo.json";
    
    // 优惠券出借接口
    private static final String GET_USER_RECOMMEND_STAR_PRIZE_LIST = "recommend/getUserRecommendStarPrizeList.json";
    // 优惠券出借接口
    private static final String GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST = "recommend/getUserRecommendStarUsedPrizeList.json";
   
    public static final String GET_PRIZECHANGE_LIST = "prizechange/getPrizeChangeList.json";
    
    public static final String GET_PRIZEDRAW_LIST = "prizedraw/getPrizeDrawList.json";
    
    public static final String PRIZE_CHANGE_CHECK = "prizechange/prizeChangeCheck.json";
    
    public static final String DO_PRIZE_CHANGE = "prizechange/doPrizeChange.json";
    
    public static final String DO_PRIZE_DRAW = "prizedraw/doPrizeDraw.json";
    
    public static final String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
    
    @Override
    public void getRecommendInfo(ModelAndView modelAndView, Integer userId) {
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId.toString());
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 发放优惠券url
         String loginUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_RECOMMEND_INFO;
        String accessKey = PropUtils.getSystem("aop.interface.accesskey");
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(loginUrl, params);
        JSONObject status=JSONObject.parseObject(result);
        //用户获得推荐星总数
        modelAndView.addObject("prizeAllCount", status.get("prizeAllCount"));
        //用户已使用推荐星数量
        modelAndView.addObject("prizeUsedCount", status.get("prizeUsedCount"));
        //用户剩余推荐星数量
        modelAndView.addObject("prizeSurplusCount", status.get("prizeSurplusCount"));
    }

    @Override
    public void getUserRecommendStarPrizeList(ModelAndView modelAndView, Integer userId) {
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId.toString());
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 发放优惠券url
         String loginUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_USER_RECOMMEND_STAR_PRIZE_LIST;
        String accessKey = PropUtils.getSystem("aop.interface.accesskey");
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(loginUrl, params);
        JSONObject status=JSONObject.parseObject(result);
        List<InviteInfoCustomize> inviteInfoCustomizes = JSONArray.parseArray(JSONObject.toJSONString(status.get("data")), InviteInfoCustomize.class);
        //获取用户推荐星获得列表
        modelAndView.addObject("userRecommendStarPrizeList", inviteInfoCustomizes);
    }

    @Override
    public void getUserRecommendStarUsedPrizeList(ModelAndView modelAndView, Integer userId) {
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId.toString());
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 发放优惠券url
         String loginUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST;
        String accessKey = PropUtils.getSystem("aop.interface.accesskey");
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(loginUrl, params);
        JSONObject status=JSONObject.parseObject(result);
        List<InviteRecommendPrizeCustomize> inviteRecommendPrizeCustomizes = JSONArray.parseArray(JSONObject.toJSONString(status.get("data")), InviteRecommendPrizeCustomize.class);
        //获取用户推荐星获得列表
        modelAndView.addObject("userRecommendStarUsedList", inviteRecommendPrizeCustomizes);
        
    }

    /**
     * 
     * 兑奖页面数据获取
     * @author hsy
     * @param modelAndView
     * @param userId
     * @see com.hyjf.app.activity.mgm10.prizeget.PrizeGetService#getPrizeChangeList(org.springframework.web.servlet.ModelAndView, java.lang.Integer)
     */
    @Override
    public void getPrizeChangeList(ModelAndView modelAndView, String userId) {
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId);
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 获取奖品兑换列表url
         String prizeChangeUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_PRIZECHANGE_LIST;
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(prizeChangeUrl, params);
        JSONObject resultObj=JSONObject.parseObject(result);
        // 推荐星数量
        modelAndView.addObject("recommendCount", resultObj.get("recommendCount"));
        List<PrizeGetCustomize> prizeChangeList = JSONArray.parseArray(JSONObject.toJSONString(resultObj.get("data")), PrizeGetCustomize.class);
        // 奖品兑换列表
        modelAndView.addObject("prizeChangeList", prizeChangeList);

    }
    
    /**
     * 
     * 抽奖页面数据获取
     * @author hsy
     * @param modelAndView
     * @param userId
     * @see com.hyjf.app.activity.mgm10.prizeget.PrizeGetService#getPrizeDrawList(org.springframework.web.servlet.ModelAndView, java.lang.Integer)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void getPrizeDrawList(ModelAndView modelAndView, String userId) {
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId);
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 获取抽奖页面奖品列表url
        String prizeDrawUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_PRIZEDRAW_LIST;
        
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(prizeDrawUrl, params);
        JSONObject resultObj=JSONObject.parseObject(result);
        // 推荐星数量
        modelAndView.addObject("recommendCount", resultObj.get("recommendCount"));
        // 可抽奖次数
        modelAndView.addObject("canDrawCount", resultObj.get("canDrawCount"));
        // 一次抽奖需要推荐星数量
        modelAndView.addObject("needCount", resultObj.get("needCount"));
        // 抽奖奖品列表
        List<PrizeGetCustomize> prizeDrawList = JSONArray.parseArray(JSONObject.toJSONString(resultObj.get("data")), PrizeGetCustomize.class);
        modelAndView.addObject("prizeDrawList", JSONArray.toJSON(prizeDrawList).toString());
        modelAndView.addObject("prizeDrawListObj", prizeDrawList);
        // 已中奖用户列表
        List<HashMap> prizeWinList = JSONArray.parseArray(JSONObject.toJSONString(resultObj.get("prizeWinList")), HashMap.class);
        modelAndView.addObject("prizeWinList", prizeWinList);

    }
    
    /**
     * 
     * 奖品兑换校验
     * @author hsy
     * @param userId
     * @param groupCode
     * @param changeCount
     * @return
     * @see com.hyjf.web.activity.recommend.RecommendService#prizeChangeCheck(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public JSONObject prizeChangeCheck(String userId, String groupCode, String changeCount){
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId);
        params.put("groupCode", groupCode);
        params.put("changeCount", changeCount);
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 获取抽奖页面奖品列表url
        String prizeChangeCheckUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+PRIZE_CHANGE_CHECK;
        
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + groupCode + changeCount + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(prizeChangeCheckUrl, params);
        JSONObject resultObj=JSONObject.parseObject(result);
        
        return resultObj;
    }
    
    /**
     * 
     * 奖品兑换
     * @author hsy
     * @param userId
     * @param groupCode
     * @param changeCount
     * @return
     * @see com.hyjf.web.activity.recommend.RecommendService#doPrizeChange(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public JSONObject doPrizeChange(String userId, String groupCode, String changeCount){
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId);
        params.put("groupCode", groupCode);
        params.put("changeCount", changeCount);
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 获取抽奖页面奖品列表url
        String prizeChangeUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+DO_PRIZE_CHANGE;
        
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + groupCode + changeCount + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(prizeChangeUrl, params);
        JSONObject resultObj=JSONObject.parseObject(result);
        
        return resultObj;
    }
    
    /**
     * 
     * 抽奖
     * @author hsy
     * @param userId
     * @return
     * @see com.hyjf.web.activity.recommend.RecommendService#doPrizeDraw(java.lang.String)
     */
    @Override
    public JSONObject doPrizeDraw(String userId){
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId);
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 获取抽奖页面奖品列表url
        String prizeDrawUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+DO_PRIZE_DRAW;
        
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(prizeDrawUrl, params);
        JSONObject resultObj=JSONObject.parseObject(result);
        
        return resultObj;
    }

}
