package com.hyjf.app.prize;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.app.util.SignValue;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;

/**
 * 出借夺宝
 * @author zhangjinpeng
 *
 */
@Controller
@RequestMapping(value=PrizeDefine.REQUEST_MAPPING)
public class PrizeController  extends BaseController{
	/** 类名 */
	public static final String THIS_CLASS = PrizeController.class.getName();
    @Autowired
    private PrizeService prizeService;
	
    /**
     * 出借夺宝页面初始化
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = PrizeDefine.INIT_ACTION) 
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response){
    	LogUtil.startLog(PrizeController.class.toString(), PrizeDefine.INIT_ACTION);
    	ModelAndView modelAndView = new ModelAndView(PrizeDefine.PRIZE_PATH);
        // 唯一标识
        String sign = request.getParameter("sign");
        // 取得用户iD
        Integer userId = null;
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(StringUtils.isNotEmpty(token)){
        	userId = SecretUtil.getUserId(sign);
        }
        // userId = 22300474;
        JSONObject ret = this.getData(userId,StringUtils.EMPTY);
        modelAndView.addObject(CustomConstants.PRIZE_HEADPHONES, ret.get(CustomConstants.PRIZE_HEADPHONES));
        modelAndView.addObject(CustomConstants.PRIZE_PAD_MINI, ret.get(CustomConstants.PRIZE_PAD_MINI));
        modelAndView.addObject(CustomConstants.PRIZE_PHONE6_PLUS, ret.get(CustomConstants.PRIZE_PHONE6_PLUS));
        modelAndView.addObject("prizeCount",ret.get("prizeCount"));
        modelAndView.addObject("sign",sign);
        LogUtil.endLog(PrizeController.class.toString(), PrizeDefine.INIT_ACTION);
        return modelAndView;
        
    }
    
    /**
     * 装载页面数据
     * 奖品总需人次，剩余人次，剩余夺宝次数
     * @param modelAndView
     * @param userId
     */
    private JSONObject getData(Integer userId,String prizeSelfCode){
    	LogUtil.startLog(PrizeController.class.toString(), "getData");
    	JSONObject ret = new JSONObject();
    	// 取得奖品列表（总需人次，已参与人次）
        List<AppUserPrizeCodeCustomize> prizeList = prizeService.getPrizeList(prizeSelfCode);
        for(AppUserPrizeCodeCustomize prize:prizeList){
        	PrizeBean prizeBean = new PrizeBean();
        	// 奖品名称
        	prizeBean.setPrizeName(prize.getPrizeName());
        	// 奖品识别码
        	prizeBean.setPrizeSelfCode(prize.getPrizeSelfCode());
        	// 奖品参与总需人次
    		prizeBean.setAllPersonCount(prize.getAllPersonCount());
    		// 奖品已参与人次
    		prizeBean.setJoinedPersonCount(prize.getJoinedPersonCount());
    		// 奖品开奖状态
    		prizeBean.setPrizeStatus(prize.getPrizeStatus());
    		// 中奖号码
    		prizeBean.setPrizeCode(prize.getPrizeCode());
    		prizeBean.setCanPrize(prizeService.checkMaxJoinedCount(prize.getPrizeSelfCode()));
    		// 中奖用户名
    		String userName = prize.getUsername();
    		if(StringUtils.isNotEmpty(userName)){
    			String sub2 = StringUtils.substring(userName, 2, userName.length());
        		userName = StringUtils.replace(userName,sub2,"XXXXXX");
        		prizeBean.setUserName(userName);
    		}
    		
        	if(StringUtils.equals(prize.getPrizeSelfCode(), CustomConstants.PRIZE_HEADPHONES)){
        		// Power Beats 2 耳机
        		ret.put(CustomConstants.PRIZE_HEADPHONES, prizeBean);
        	}else if(StringUtils.equals(prize.getPrizeSelfCode(), CustomConstants.PRIZE_PAD_MINI)){
        		// iPad mini 4
        		ret.put(CustomConstants.PRIZE_PAD_MINI, prizeBean);
        	}else if(StringUtils.equals(prize.getPrizeSelfCode(), CustomConstants.PRIZE_PHONE6_PLUS)){
        		// iPhone6s Plus
        		ret.put(CustomConstants.PRIZE_PHONE6_PLUS, prizeBean);
        	}
        }
        // 计算得到剩余夺宝次数
        int prizeCount = 0;
        if(userId != null){
        	String activityId = PropUtils.getSystem(PropertiesConstants.TENDER_PRIZE_ACTIVITY_ID);
        	prizeCount = prizeService.getUserPrizeCount(userId, activityId);
        }
        ret.put("prizeCount", prizeCount);
        LogUtil.endLog(PrizeController.class.toString(), "getData");
    	return ret;
    	
    }
    
    /**
     * 参与夺宝
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeDefine.USER_PRIZE_ACTION ) 
    public JSONObject userPrize(HttpServletRequest request, HttpServletResponse response){
    	LogUtil.startLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_ACTION);
    	JSONObject result = new JSONObject();
        // 唯一标识
        String sign = request.getParameter("sign");
        // 奖品辨识码
        String prizeSelfCode = request.getParameter("prizeSelfCode");
        
        // 取得用户iD
        Integer userId = null;
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(StringUtils.isNotEmpty(token)){
        	userId = SecretUtil.getUserId(sign);
        }
        //userId = 22300474;
        // 校验用户信息
        JSONObject checkResult = prizeService.checkParam(userId);
        String errorCode = checkResult.getString("errorCode");
		if (errorCode != "0") {
			checkFail(result, errorCode, userId);
		}else{
			// 更新
	        result = prizeService.updatePrize(userId, prizeSelfCode);
	        if(result.getString(CustomConstants.APP_STATUS) == "0"){
	        	// 装载页面显示数据
	        	JSONObject ret = this.getData(userId,prizeSelfCode);
	        	result.put(CustomConstants.PRIZE_HEADPHONES, ret.get(CustomConstants.PRIZE_HEADPHONES));
	        	result.put(CustomConstants.PRIZE_PAD_MINI, ret.get(CustomConstants.PRIZE_PAD_MINI));
	        	result.put(CustomConstants.PRIZE_PHONE6_PLUS, ret.get(CustomConstants.PRIZE_PHONE6_PLUS));
	        	
	        	// 剩余夺宝次数
	        	result.put("prizeCount", ret.getString("prizeCount"));
	        }else{
	        	result.put(CustomConstants.APP_REQUEST,
						PrizeDefine.RETURN_REQUEST_INIT);
	        }
		}
        LogUtil.endLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_ACTION);
        return result;
        
    }
    
    
    /**
     * 我的兑奖码列表
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeDefine.USER_PRIZE_LIST_ACTION) 
    public JSONObject userPrizeList(HttpServletRequest request, HttpServletResponse response){
    	LogUtil.startLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_LIST_ACTION);
    	JSONObject result = new JSONObject();
        // 唯一标识
        String sign = request.getParameter("sign");
        
        // 取得用户iD
        Integer userId = null;
        String value = RedisUtils.get(sign);
        SignValue signValue = JSON.parseObject(value, SignValue.class);
        String token=signValue.getToken();
        if(StringUtils.isNotEmpty(token)){
        	userId = SecretUtil.getUserId(sign);
        }
        // userId = 22300474;
        // 校验用户信息
        JSONObject checkResult = prizeService.checkParam(userId);
        String errorCode = checkResult.getString("errorCode");
		if (errorCode != "0") {
			checkFail(result, errorCode, userId);
		}else{
			List<AppUserPrizeCodeCustomize> prizeCodeList = prizeService.getUserPrizeCodeList(userId);
			result.put("prizeCodeList", prizeCodeList);
			result.put(CustomConstants.APP_STATUS,
				CustomConstants.APP_STATUS_SUCCESS);
		}
        LogUtil.endLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_LIST_ACTION);
        return result;
        
    }
    
    /**
     * 用户校验失败 拼装数据
     * @param result
     * @param errorCode
     * @param userId
     * @param sign
     * @param platform
     * @param version
     */
    private void checkFail(JSONObject result,String errorCode,Integer userId){
    	result.put(CustomConstants.APP_STATUS,
				CustomConstants.APP_STATUS_FAIL);
		result.put(CustomConstants.APP_STATUS_DESC,
				result.get(CustomConstants.APP_STATUS_DESC));
		result.put(PrizeDefine.ERROR_CODE,errorCode);
		if (errorCode == "1") {
			// 登录请求URL
			result.put(CustomConstants.APP_REQUEST,
					PrizeDefine.LOGIN_REQUEST_MAPPING);
			
		} 
    }
    
    /**************************************以下微官网用******************************************/
    
    /**
     * 出借夺宝页面初始化
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeDefine.INIT_WEI_ACTION) 
    public JSONObject initWei(HttpServletRequest request, HttpServletResponse response){
    	LogUtil.startLog(PrizeController.class.toString(), PrizeDefine.INIT_WEI_ACTION);
        // 唯一标识
        String userIdStr = request.getParameter("userId");
        // 取得用户iD
        Integer userId = null;
        if(StringUtils.isNotEmpty(userIdStr)){
        	userId = Integer.valueOf(userIdStr);
        }
        JSONObject ret = this.getData(userId,StringUtils.EMPTY);
        ret.put("userId",userId);
        LogUtil.endLog(PrizeController.class.toString(), PrizeDefine.INIT_WEI_ACTION);
        return ret;
        
    }
    
    /**
     * 参与夺宝
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeDefine.USER_PRIZE_WEI_ACTION) 
    public JSONObject userPrizeWei(HttpServletRequest request, HttpServletResponse response){
    	LogUtil.startLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_WEI_ACTION);
    	JSONObject result = new JSONObject();
        // 奖品辨识码
        String prizeSelfCode = request.getParameter("prizeSelfCode");
        String userIdStr = request.getParameter("userId");
        // 取得用户iD
        Integer userId = null;
        if(StringUtils.isNotEmpty(userIdStr)){
        	userId = Integer.valueOf(userIdStr);
        }
        // 校验用户信息
        JSONObject checkResult = prizeService.checkParam(userId);
        String errorCode = checkResult.getString("errorCode");
		if (errorCode != "0") {
			checkFail(result, errorCode, userId);
		}else{
			// 更新
	        result = prizeService.updatePrize(userId, prizeSelfCode);
	        if(result.getString(CustomConstants.APP_STATUS) == "0"){
	        	// 装载页面显示数据
	        	JSONObject ret = this.getData(userId,prizeSelfCode);
	        	result.put(CustomConstants.PRIZE_HEADPHONES, ret.get(CustomConstants.PRIZE_HEADPHONES));
	        	result.put(CustomConstants.PRIZE_PAD_MINI, ret.get(CustomConstants.PRIZE_PAD_MINI));
	        	result.put(CustomConstants.PRIZE_PHONE6_PLUS, ret.get(CustomConstants.PRIZE_PHONE6_PLUS));
	        	
	        	// 剩余夺宝次数
	        	result.put("prizeCount", ret.getString("prizeCount"));
	        }
		}
        LogUtil.endLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_WEI_ACTION);
        return result;
        
    }
    
    /**
     * 我的兑奖码列表
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PrizeDefine.USER_PRIZE_LIST_WEI_ACTION) 
    public JSONObject userPrizeListWei(HttpServletRequest request, HttpServletResponse response){
    	LogUtil.startLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_LIST_WEI_ACTION);
    	JSONObject result = new JSONObject();
        // 取得用户iD
        String userIdStr = request.getParameter("userId");
        // 取得用户iD
        Integer userId = null;
        if(StringUtils.isNotEmpty(userIdStr)){
        	userId = Integer.valueOf(userIdStr);
        }
        // 校验用户信息
        JSONObject checkResult = prizeService.checkParam(userId);
        String errorCode = checkResult.getString("errorCode");
		if (errorCode != "0") {
			checkFail(result, errorCode, userId);
		}else{
			List<AppUserPrizeCodeCustomize> prizeCodeList = prizeService.getUserPrizeCodeList(userId);
			result.put("prizeCodeList", prizeCodeList);
			result.put(CustomConstants.APP_STATUS,
				CustomConstants.APP_STATUS_SUCCESS);
		}
        LogUtil.endLog(PrizeController.class.toString(), PrizeDefine.USER_PRIZE_LIST_WEI_ACTION);
        return result;
        
    }
}