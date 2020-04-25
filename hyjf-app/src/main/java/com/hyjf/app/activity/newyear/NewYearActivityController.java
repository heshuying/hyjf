package com.hyjf.app.activity.newyear;

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
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;

@Controller("newYearActivityController")
@RequestMapping(value = NewYearActivityDefine.REQUEST_MAPPING)
public class NewYearActivityController extends BaseController {
    
    private static final Integer ACTIVITY_A_START_TIME = 1484841600;
    
    private static final Integer ACTIVITY_A_END_TIME = 1486223999; 
    
    // 活动已结束的时间
//    private static final Integer ACTIVITY_A_END_TIME = 1484534108; 
    
    @Autowired
    private NewYearActivityService newYearActivityService;
    /**
     * 
     * 新年活动页面初始化
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = NewYearActivityDefine.INIT_ACTION)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
        LogUtil.startLog(NewYearActivityController.class.toString(), NewYearActivityDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(NewYearActivityDefine.INIT_PATH);
        modelAndView.addObject("host", webhost);
        // 检查参数
        String sign = request.getParameter("sign");
        modelAndView.addObject("sign", sign);
        if(GetDate.getNowTime10() < ACTIVITY_A_START_TIME){
            // 活动未开始
            modelAndView.addObject("actStatus", 0);
        }else if(GetDate.getNowTime10() > ACTIVITY_A_END_TIME){
            // 活动已结束
            modelAndView.addObject("actAStatus", 2);
        }else{
            // 活动进行中
            modelAndView.addObject("actAStatus", 1);
        }
        
        
        if(sign==null){
            //获取今天的谜题
            newYearActivityService.getPresentRiddles(modelAndView);
            //获取用户灯笼点亮列表
            newYearActivityService.getUserLanternIllumineList(0,modelAndView);
            modelAndView.addObject("loginUrl", "hyjf://jumpLogin/?");
            modelAndView.addObject("loginFlag", 0); 
            return modelAndView;
        }
        // 用户ID
        Integer userId = SecretUtil.getUserIdNoException(sign);
//        Integer userId =22400189;
        if(userId==null||userId==0){
            //获取今天的谜题
            newYearActivityService.getPresentRiddles(modelAndView);
            //获取用户灯笼点亮列表
            newYearActivityService.getUserLanternIllumineList(0,modelAndView);
            modelAndView.addObject("loginUrl", "hyjf://jumpLogin/?");
            modelAndView.addObject("loginFlag", 0);
        }else{
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("loginFlag", 1);
        }
        
        //获取今天的谜题
        newYearActivityService.getPresentRiddles(modelAndView);
        //获取用户累计的代金券金额
        newYearActivityService.getUserPresentCumulativeCoupon(userId,modelAndView);
        //获取用户灯笼点亮列表
        newYearActivityService.getUserLanternIllumineList(userId,modelAndView);
        //获取当前用户今天是否答过题
        newYearActivityService.getTodayUserAnswerFlag(userId,modelAndView);
        
        
//        modelAndView.addObject("actAStatus", 1);

        
//        modelAndView.addObject("loginFlag", 1);
//        modelAndView.addObject("tabFlag", "1");
        
        return modelAndView;
    }
    
    
    
    /**
     * 
     * 活动二校验
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.CHECK_ACTION, produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    public String check(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActivityController.class.toString(), NewYearActivityDefine.CHECK_ACTION);
        JSONObject info = new JSONObject();
        // 检查参数
        String sign = request.getParameter("sign");
        // 用户ID
      Integer userId = SecretUtil.getUserIdNoException(sign);
//      Integer userId =22400189;
        String questionId = request.getParameter("questionId");
        //获取当前用户今天是否答过题
        newYearActivityService.check(questionId,userId,info);
        
        return JSONObject.toJSONString(info);
    }
    
    /**
     * 
     * 初始化记录用户答题信息 
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.INSERT_USER_ANSWER_RECORD_INIT)
    public String insertUserAnswerRecordInit(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActivityController.class.toString(), NewYearActivityDefine.INSERT_USER_ANSWER_RECORD_INIT);
        String sign = request.getParameter("sign");
        JSONObject info = new JSONObject();
        Integer userId = SecretUtil.getUserId(sign);
//      Integer userId =22400189;
        
        String questionId = request.getParameter("questionId");
        //获取当前用户今天是否答过题
        newYearActivityService.insertUserAnswerRecordInit(questionId,userId,info);
        return JSONObject.toJSONString(info);
    }
    
    /**
     * 
     * 修改记录用户答题信息
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.UPDATE_USER_ANSWER_RECORD)
    public String updateUserAnswerRecord(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActivityController.class.toString(), NewYearActivityDefine.UPDATE_USER_ANSWER_RECORD);
        JSONObject info = new JSONObject();
        String sign = request.getParameter("sign");
      Integer userId = SecretUtil.getUserIdNoException(sign);
//      Integer userId =22400189;
        
        String questionId = request.getParameter("questionId");
        String userAnswer = request.getParameter("userAnswer");
        //获取当前用户今天是否答过题
        newYearActivityService.updateUserAnswerRecord(questionId,userId,userAnswer, info);
        return JSONObject.toJSONString(info);
    }
    
    
    /**
     * 
     * 获取财富卡数据
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.GET_CARD_DATA)
    public JSONObject getCardData(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActivityDefine.GET_CARD_DATA);
        
        String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
        
        String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
        
        String GET_CARD_DATA = "wealthcard/getUserCardCount";
        
        String sign = request.getParameter("sign");
        
        Integer userId = SecretUtil.getUserIdNoException(sign);
//        Integer userId =22400189;
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
            
            String chkValue = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
            params.put("chkValue", chkValue);
            
            String result=HttpClientUtils.post(getCardDataUrl, params);
            
            resultObj=JSONObject.parseObject(result);
            
        }
        
        
        LogUtil.endLog(this.getClass().getName(), NewYearActivityDefine.GET_CARD_DATA);
        
        return resultObj;
    }
    
    /**
     * 
     * 手机号码校验
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.DO_PHONENUM_CHECK)
    public JSONObject phoneNumCheck(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActivityDefine.DO_PHONENUM_CHECK);
        
        String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
        
        String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
        
        String DO_PHONENUM_CHECK = "wealthcard/doPhoneNumCheck";

        String phoneNum = request.getParameter("phoneNum");
        
        JSONObject resultObj = new JSONObject();
        
        Map<String, String> params = new HashMap<String, String>();
        // 用户编号
        params.put("phoneNum", String.valueOf(phoneNum));
        String timestamp=GetDate.getNowTime10()+"";
        // 时间戳
        params.put("timestamp", timestamp);

        String doPhoneNumCheckUrl = API_WEB_URL + DO_PHONENUM_CHECK;
        
        String chkValue = StringUtils.lowerCase(MD5.toMD5Code(accessKey + phoneNum + timestamp +  accessKey));
        params.put("chkValue", chkValue);
        
        String result=HttpClientUtils.post(doPhoneNumCheckUrl, params);
        
        resultObj=JSONObject.parseObject(result);
            
        LogUtil.endLog(this.getClass().getName(), NewYearActivityDefine.DO_PHONENUM_CHECK);
        
        return resultObj;
    }
    
    /**
     * 
     * 发送卡片给好友
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.DO_CARD_SEND)
    public JSONObject doCardSend(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActivityDefine.DO_CARD_SEND);
        
        String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
        
        String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
        
        String DO_CARD_SEND = "wealthcard/doCardSend";

        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserIdNoException(sign);
        
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
            
            String chkValue = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + phoneNum + cardIdentifier + timestamp +  accessKey));
            params.put("chkValue", chkValue);
            
            String result=HttpClientUtils.post(cardSendUrl, params);
            
            resultObj=JSONObject.parseObject(result);
            
        }
        
        
        LogUtil.endLog(this.getClass().getName(), NewYearActivityDefine.DO_CARD_SEND);
        
        return resultObj;
    }

    /**
     * 
     * 点燃爆竹并抽奖
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewYearActivityDefine.DO_PRIZE_DRAW)
    public JSONObject doPrizeDraw(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), NewYearActivityDefine.DO_PRIZE_DRAW);
        
        String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
        
        String accessKey  = PropUtils.getSystem("aop.interface.accesskey");
        
        String DO_PRIZE_DRAW = "wealthcard/doPrizeDraw";

        String sign = request.getParameter("sign");
        Integer userId = SecretUtil.getUserIdNoException(sign);
        
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
            
            String chkValue = StringUtils.lowerCase(MD5.toMD5Code(accessKey + userId + timestamp +  accessKey));
            params.put("chkValue", chkValue);
            
            String result=HttpClientUtils.post(prizeDrawUrl, params);
            
            resultObj=JSONObject.parseObject(result);
            
        }
        
        LogUtil.endLog(this.getClass().getName(), NewYearActivityDefine.DO_PRIZE_DRAW);
        
        return resultObj;
    }

    /**
     * 
     * 获取活动A的活动规则
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = NewYearActivityDefine.GET_RULE_ACTIVITY_A)
    public ModelAndView getRuleActivityA(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NewYearActivityController.class.toString(), NewYearActivityDefine.GET_RULE_ACTIVITY_A);
        ModelAndView modelAndView = new ModelAndView(NewYearActivityDefine.PATH_RULE_ACTIVITY_A);
        
        
        LogUtil.endLog(this.getClass().getName(), NewYearActivityDefine.GET_RULE_ACTIVITY_A);
        return modelAndView;
    }
    
    
    /**
     * 
     * 修改记录用户答题信息
     * @author zhangjp
     * @return
     */
    @ResponseBody
    @RequestMapping("clearRedis")
    public void clearRedis(HttpServletRequest request, HttpServletResponse response) {
        newYearActivityService.clearRedis();
    }
    
    
}
