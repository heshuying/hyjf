package com.hyjf.web.activity.newyear;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.regist.UserRegistDefine;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = NewYearActDefine.REQUEST_MAPPING)
public class NewYearActController extends BaseController{
    private static final Integer ACTIVITY_START_TIME = 1484841600; 
    private static final Integer ACTIVITY_END_TIME = 1486223999; 
    
    // 活动已结束的时间
//    private static final Integer ACTIVITY_END_TIME = 1484534108;

    
    public static final String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
    
    public static final String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
    
    private static final String GET_CARD_DATA = "wealthcard/getUserCardCount";
    
    private static final String DO_PRIZE_DRAW = "wealthcard/doPrizeDraw";
    
    private static final String DO_PHONENUM_CHECK = "wealthcard/doPhoneNumCheck";
    
    private static final String DO_CARD_SEND = "wealthcard/doCardSend";
    
    
    @Autowired
    private NewYearActService newYearActService;
    
    @RequestMapping(value = NewYearActDefine.INIT_ACTION)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(NewYearActDefine.INIT_PATH);
        
        Integer userId = WebUtils.getUserId(request);
        
//        Integer userId = 22400189;
        
        if(Validator.isNull(userId)){
            modelAndView.addObject("isLogin", 0);
        }else {
            modelAndView.addObject("isLogin", 1);
            modelAndView.addObject("webCatLink", PropUtils.getSystem("hyjf.wechat.invite.url")+userId+".html");
            modelAndView.addObject("inviteLink", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+UserRegistDefine.REQUEST_MAPPING+"/"+UserRegistDefine.INIT+".do?from="+userId);
        }
        
        //获取今天的谜题
        newYearActService.getPresentRiddles(modelAndView);
        //获取用户累计的代金券金额
        newYearActService.getUserPresentCumulativeCoupon(userId,modelAndView);
        //获取用户灯笼点亮列表
        newYearActService.getUserLanternIllumineList(userId,modelAndView);
        //获取当前用户今天是否答过题
        newYearActService.getTodayUserAnswerFlag(userId,modelAndView);
        
        if(GetDate.getNowTime10() < ACTIVITY_START_TIME){
            // 活动未开始
            modelAndView.addObject("actStatus", 0);
        }else if(GetDate.getNowTime10() > ACTIVITY_END_TIME){
            // 活动已结束
            modelAndView.addObject("actStatus", 2);
        }else{
            // 活动进行中
            modelAndView.addObject("actStatus", 1);
        }
        
        LogUtil.endLog(this.getClass().getName(), NewYearActDefine.INIT_ACTION);
        return modelAndView;
    }
    
    @ResponseBody
    @RequestMapping(value = NewYearActDefine.GET_CARD_DATA)
    public JSONObject getCardData(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActDefine.GET_CARD_DATA);
        
        Integer userId = WebUtils.getUserId(request);
        
        JSONObject resultObj = new JSONObject();
        
        if(userId == null){
            resultObj.put("status", "1");
            resultObj.put("errCode", 9);
            resultObj.put("statusDesc", "未登录");
        }else {
            Map<String, String> params = new HashMap<String, String>();
            // 用户编号
            params.put("userId", String.valueOf(userId));
            String timestamp=GetDate.getNowTime10()+"";
            // 时间戳
            params.put("timestamp", timestamp);

            String getCardDataUrl = API_WEB_URL + GET_CARD_DATA;
            
            String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
            params.put("chkValue", sign);
            
            String result=HttpClientUtils.post(getCardDataUrl, params);
            
            resultObj=JSONObject.parseObject(result);
            
        }
        
        
        LogUtil.endLog(this.getClass().getName(), NewYearActDefine.GET_CARD_DATA);
        
        return resultObj;
    }
    
    @ResponseBody
    @RequestMapping(value = NewYearActDefine.DO_PHONENUM_CHECK)
    public JSONObject phoneNumCheck(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActDefine.DO_PHONENUM_CHECK);
        
        String phoneNum = request.getParameter("phoneNum");
        
        JSONObject resultObj = new JSONObject();
        
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("phoneNum", String.valueOf(phoneNum));
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);

        String doPhoneNumCheckUrl = API_WEB_URL + DO_PHONENUM_CHECK;
        
        String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + phoneNum + timestamp +  accessKey));
        params.put("chkValue", sign);
        
        String result=HttpClientUtils.post(doPhoneNumCheckUrl, params);
        
        resultObj=JSONObject.parseObject(result);
            
        LogUtil.endLog(this.getClass().getName(), NewYearActDefine.DO_PHONENUM_CHECK);
        
        return resultObj;
    }
    
    @ResponseBody
    @RequestMapping(value = NewYearActDefine.DO_CARD_SEND)
    public JSONObject doCardSend(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActDefine.DO_CARD_SEND);
        
        Integer userId = WebUtils.getUserId(request);
        String phoneNum = request.getParameter("phoneNum");
        String cardIdentifier = request.getParameter("cardIdentifier");
        
        JSONObject resultObj = new JSONObject();
        
        if(userId == null){
            resultObj.put("status", "1");
            resultObj.put("errCode", 9);
            resultObj.put("statusDesc", "未登录");
        }else {
            Map<String, String> params = new HashMap<String, String>();
            // 用户编号
            params.put("userId", String.valueOf(userId));
            params.put("phoneNum", phoneNum);
            params.put("cardIdentifier", cardIdentifier);
            
            String timestamp=GetDate.getNowTime10()+"";
            // 时间戳
            params.put("timestamp", timestamp);
            
            String cardSendUrl = API_WEB_URL + DO_CARD_SEND;
            
            String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + phoneNum + cardIdentifier + timestamp +  accessKey));
            params.put("chkValue", sign);
            
            String result=HttpClientUtils.post(cardSendUrl, params);
            
            resultObj=JSONObject.parseObject(result);
            
        }

        
        LogUtil.endLog(this.getClass().getName(), NewYearActDefine.DO_CARD_SEND);
        
        return resultObj;
    }

    @ResponseBody
    @RequestMapping(value = NewYearActDefine.DO_PRIZE_DRAW)
    public JSONObject doPrizeDraw(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActDefine.DO_PRIZE_DRAW);
        
        Integer userId = WebUtils.getUserId(request);
        
        JSONObject resultObj = new JSONObject();
        
        if(userId == null){
            resultObj.put("status", "1");
            resultObj.put("errCode", 9);
            resultObj.put("statusDesc", "未登录");
        }else {
            Map<String, String> params = new HashMap<String, String>();
            // 用户编号
            params.put("userId", String.valueOf(userId));
            
            String timestamp=GetDate.getNowTime10()+"";
            // 时间戳
            params.put("timestamp", timestamp);
            
            String prizeDrawUrl = API_WEB_URL + DO_PRIZE_DRAW;
            
            String sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
            params.put("chkValue", sign);
            
            String result=HttpClientUtils.post(prizeDrawUrl, params);
            
            resultObj=JSONObject.parseObject(result);
            
        }
        
        LogUtil.endLog(this.getClass().getName(), NewYearActDefine.DO_PRIZE_DRAW);
        
        return resultObj;
    }
    
    
    /**
     * 
     * 活动二校验
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActDefine.CHECK_ACTION, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    public String check(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActDefine.class.toString(), NewYearActDefine.CHECK_ACTION);
        JSONObject info = new JSONObject();
        Integer userId = WebUtils.getUserId(request);
//        userId=null;
        if(userId == null){
            info.put("noUserFlg", true);
            info.put("checkStatus", "0");
            return JSONObject.toJSONString(info);
        }
        
//      Integer userId =22400189;
        String questionId = request.getParameter("questionId");
        //获取当前用户今天是否答过题
        newYearActService.check(questionId,userId,info);
        
        return JSONObject.toJSONString(info);
    }
    
    
    /**
     * 
     * 初始化记录用户答题信息 
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActDefine.INSERT_USER_ANSWER_RECORD_INIT)
    public String insertUserAnswerRecordInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActDefine.class.toString(), NewYearActDefine.INSERT_USER_ANSWER_RECORD_INIT);
        JSONObject info = new JSONObject();
        Integer userId = WebUtils.getUserId(request);
//      Integer userId =22400189;
        
        String questionId = request.getParameter("questionId");
        //获取当前用户今天是否答过题
        newYearActService.insertUserAnswerRecordInit(questionId,userId,info);
        return JSONObject.toJSONString(info);
    }
    
    
    /**
     * 
     * 修改记录用户答题信息
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActDefine.UPDATE_USER_ANSWER_RECORD)
    public String updateUserAnswerRecord(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActDefine.class.toString(), NewYearActDefine.UPDATE_USER_ANSWER_RECORD);
        JSONObject info = new JSONObject();
        Integer userId = WebUtils.getUserId(request);
//      Integer userId =22400189;
        
        String questionId = request.getParameter("questionId");
        String userAnswer = request.getParameter("userAnswer");
        //获取当前用户今天是否答过题
        newYearActService.updateUserAnswerRecord(questionId,userId,userAnswer, info);
        return JSONObject.toJSONString(info);
    }
    
    
}
