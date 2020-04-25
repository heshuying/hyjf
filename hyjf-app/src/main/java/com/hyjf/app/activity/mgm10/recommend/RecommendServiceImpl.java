package com.hyjf.app.activity.mgm10.recommend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.app.activity.mgm10.prizeget.PrizeGetDefine;
import com.hyjf.app.sharenews.ShareNewsBean;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.auto.Invite;
import com.hyjf.mybatis.model.auto.InviteExample;
import com.hyjf.mybatis.model.customize.recommend.InviteInfoCustomize;
import com.hyjf.mybatis.model.customize.recommend.InviteRecommendPrizeCustomize;

@Service
public class RecommendServiceImpl extends BaseServiceImpl implements RecommendService {

    // 优惠券出借接口
    private static final String GET_RECOMMEND_INFO = "recommend/getRecommendInfo.json";
    // 用户标识
    private static final String GET_USER_FLAG = "recommend/getUserFlag.json";
    // 优惠券出借接口
    private static final String GET_USER_RECOMMEND_STAR_PRIZE_LIST = "recommend/getUserRecommendStarPrizeList.json";
    // 优惠券出借接口
    private static final String GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST = "recommend/getUserRecommendStarUsedPrizeList.json";
    
    private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");
    @Override
    public void getRecommendInfo(ModelAndView modelAndView, Integer userId, HttpServletRequest request) {
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
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
        
        modelAndView.addObject("getUserRecommendStarPrizeListUrl",
                webhost + RecommendDefine.REQUEST_MAPPING +"/"+ RecommendDefine.GET_USER_RECOMMEND_STAR_PRIZE_LIST_ACTION + packageStr(request));
        modelAndView.addObject("getUserRecommendStarUsedPrizeListUrl",
                webhost + RecommendDefine.REQUEST_MAPPING +"/"+ RecommendDefine.GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST_ACTION + packageStr(request));
        modelAndView.addObject("exchangeAwardUrl",
                webhost + PrizeGetDefine.REQUEST_MAPPING +"/"+ PrizeGetDefine.PRIZE_GET_INIT_ACTION + packageStr(request));
        
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
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp + accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(loginUrl, params);
        JSONObject status=JSONObject.parseObject(result);
        List<InviteInfoCustomize> inviteInfoCustomizes = JSONArray.parseArray(JSONObject.toJSONString(status.get("data")), InviteInfoCustomize.class);
        //获取用户推荐星获得列表
        modelAndView.addObject("userRecommendStarPrizeList", inviteInfoCustomizes);
        
        //获取用户推荐星获得列表长度
        modelAndView.addObject("userRecommendStarPrizeListSize", inviteInfoCustomizes.size());
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
        //获取用户推荐星获得列表长度
        modelAndView.addObject("userRecommendStarUsedListSize", inviteRecommendPrizeCustomizes.size());
        
    }
    
    
    
 // 组装url
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

    @Override
    public void getUserFlag(ModelAndView modelAndView, Integer userId, HttpServletRequest request) {
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("userId", userId.toString());
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);
        // 发放优惠券url
         String loginUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL)+GET_USER_FLAG;
        String accessKey = PropUtils.getSystem("aop.interface.accesskey");
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
        params.put("chkValue", sign);
        String result=HttpClientUtils.post(loginUrl, params);
        JSONObject status=JSONObject.parseObject(result);
        //是否出借过 1出借过 0未出借
        modelAndView.addObject("isInvest", status.get("isInvest"));
        //是否是员工 1可以参加  0不能参加
        modelAndView.addObject("isStaff", status.get("isStaff"));

        
    }

    @Override
    public void getShareInformation(ModelAndView modelAndView) {
        InviteExample ie = new InviteExample();
        List<Invite> result= inviteMapper.selectByExampleWithBLOBs(ie);
        
        ShareNewsBean shareNewsBean= new ShareNewsBean();
        if(result!=null && result.size()>0){
            Invite invite= result.get(0);
            shareNewsBean.setTitle(invite.getTitle());
            shareNewsBean.setContent(invite.getContent());
            shareNewsBean.setImg(invite.getImg());
            shareNewsBean.setLinkUrl(invite.getLinkUrl());
        }
        if (shareNewsBean.getTitle() != null) {
            modelAndView.addObject("title", shareNewsBean.getTitle());
            modelAndView.addObject("content", shareNewsBean.getContent());
            modelAndView.addObject("img", HOST_URL + shareNewsBean.getImg());// 提供绝对路径
        }
    }

}
