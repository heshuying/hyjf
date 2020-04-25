package com.hyjf.app.msgpush;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseDefine;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.HtmlUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistory;
import com.hyjf.mybatis.model.auto.MessagePushMsgHistoryNew;

@Controller
@RequestMapping(value = MsgPushDefine.REQUEST_MAPPING)
public class MsgPushController extends BaseController {
	/** 类名 */
	public static final String THIS_CLASS = MsgPushController.class.getName();
	@Autowired
	public MsgPushService msgPushService;

	/**
	 * 获取用户消息提醒列表，支持分页
	 * 此处为方法说明
	 * @author yyc
	 * @param request
	 * @param response
	 * @return
	 */
	 @ResponseBody
	    @RequestMapping(MsgPushDefine.TAG_ACTION)
	    public JSONObject getTagListAction(@RequestParam(value = "page", defaultValue = "1") int page,
	    	      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request, HttpServletResponse response) {
	        LogUtil.startLog(THIS_CLASS, MsgPushDefine.TAG_ACTION);
	        JSONObject ret = new JSONObject();
	        ret.put("request", MsgPushDefine.RETURN_REQUEST + MsgPushDefine.TAG_ACTION);
	        // 版本号
	        String version = request.getParameter("version");
	        // 平台
	        String platform = request.getParameter("platform");
	        // 唯一标识
	        String sign = request.getParameter("sign");
	        // 检查参数正确性
	        if (Validator.isNull(version) || Validator.isNull(platform) ||  Validator.isNull(sign)) {
	            ret.put("status", "1");
	            ret.put("statusDesc", "请求参数非法");
	            return ret;
	        }
	        // 取得加密用的Key
	        String key = SecretUtil.getKey(sign);
	        if (Validator.isNull(key)) {
	            ret.put("status", "1");
	            ret.put("statusDesc", "请求参数非法");
	            return ret;
	        }
	        // 取得用户iD
	        Integer userId = null;
	        try {
	            userId = SecretUtil.getUserId(sign);
	        } catch (Exception e) {
	            userId = null;
	        }
	        ret.put("status", "0");
	        ret.put("statusDesc", "成功");
	        
	        page=Integer.valueOf(page);
            pageSize=Integer.valueOf(pageSize);
            int count= msgPushService.countMsgHistoryRecord(1, userId, null);
//	        int count = this.msgPushService.countMsgPushTagRecord(userId);
//	        ret.put("title", "消息盒子");
	        ret.put("count", count);
	        int limitStart=pageSize * (page - 1);
	        
	        List<MessagePushMsgHistoryNew> msgTypeList = new ArrayList<MessagePushMsgHistoryNew>();
	        try {
    	        List<MessagePushMsgHistory> list=msgPushService.getMsgHistoryList(1,userId,null,limitStart,pageSize);
    	        for(MessagePushMsgHistory msg : list){
    	            MessagePushMsgHistoryNew tagbean=new MessagePushMsgHistoryNew();
    	            tagbean.setTitle(msg.getMsgTitle());
    	            tagbean.setId(msg.getId());
    	            tagbean.setTime(GetDate.timestamptoStrYYYYMMDDHHMM(msg.getSendTime().toString()));
    	            int versionInt=0;
                    String vers[] = version.split("\\."); // 取前三位版本号
                    if(vers != null && vers.length > 0){
                        versionInt = Integer.parseInt(vers[0] + vers[1] + vers[2]);
                    }
                    String text=HtmlUtil.getTextFromHtml( msg.getMsgContent());
                    if (versionInt<135) {
                        if (text.length()>13) {
                            text=text.substring(0, 11)+"…";
                        }
                    }
                    //消息内容
                    tagbean.setIntroduction(text);
                    msgTypeList.add(tagbean);
    	        } 
	        } catch (Exception e) {
                e.printStackTrace();
                ret.put("status", "1");
                ret.put("statusDesc", "获取消息失败");
                return ret;
            }
            ret.put("msgTypeList", msgTypeList);
            return ret;
	    }
    
	
	 /**
	  * 获取通知列表，支持分页
	  * 此处为方法说明
	  * @author yyc
	  * @param request
	  * @param response
	  * @return
	  */
	 @ResponseBody
	    @RequestMapping(MsgPushDefine.MSG_ACTION)
	    public JSONObject getMsgListAction(@RequestParam(value = "page", defaultValue = "1") int page,
	    	      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request, HttpServletResponse response) {
	        LogUtil.startLog(THIS_CLASS, MsgPushDefine.MSG_ACTION);
	        JSONObject ret = new JSONObject();
	        ret.put("request", MsgPushDefine.RETURN_REQUEST + MsgPushDefine.MSG_ACTION);
	        // 版本号
	        String version = request.getParameter("version");
	        // 平台
	        String platform = request.getParameter("platform");
	        // 检查参数正确性
	        if (Validator.isNull(version) || Validator.isNull(platform)  ) {
	            ret.put("status", "1");
	            ret.put("statusDesc", "请求参数非法");
	            return ret;
	        }

	        ret.put("status", "0");
	        ret.put("statusDesc", "成功");
	        //获取标签信息
	        //查询列表数量
	        //int count= msgPushService.countMsgHistoryRecord(0, null, null);
	        
	        //返回列表
	        List<MsgPushBean> msgPushList = new ArrayList<MsgPushBean>();
	        page = Integer.valueOf(page);
	       pageSize = Integer.valueOf(pageSize);
	        int limitStart=pageSize * (page - 1);
	        try {
	            List<MessagePushMsgHistory> list=msgPushService.getMsgHistoryList(0,null,null,limitStart,pageSize);
	            boolean firstFlag=false;
	            if(page<=1){
	                firstFlag=true;
	            }
	            for(MessagePushMsgHistory msg : list){
	                MsgPushBean msgbean= new MsgPushBean();
	                msgbean.setMsgTitle(msg.getMsgTitle());
	                msgbean.setMsgContent(msg.getMsgContent().replaceAll("</?[^>]+>", ""));
	                msgbean.setActionUrl(msg.getMsgActionUrl());
	                msgbean.setMsgId(String.valueOf(msg.getId()));
	                msgbean.setMsgAction(String.valueOf(msg.getMsgAction()));
	                //如果发送时间大于 时间戳   则为今天的数据
	                msgbean.setTime(GetDate.timestamptoStrYYYYMMDDHHMM(msg.getSendTime().toString()));
	                msgbean.setMsgType(String.valueOf(msg.getMsgDestinationType()));
	                
	                //标识第一条通知数据返回大图片  
	                if(firstFlag){
	                    String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
	                    webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
	                    msgbean.setImgUrl(webhost+msg.getMsgImageUrl());
	                    firstFlag=false; 
	                }
	                //屏蔽了原始代码，这里统一设置已读标志为已读
	                msgbean.setReadFlag("1");
//	                if(msg.getMsgReadCountAndroid() > 0){
//	                    msgbean.setReadFlag("1");
//	                }else if(msg.getMsgReadCountIos() > 0){
//	                    msgbean.setReadFlag("1");
//	                }else{
//	                    msgbean.setReadFlag("0");
//	                }
	                msgPushList.add(msgbean);
	            }
	        } catch (Exception e) {
	            ret.put("status", "1");
	            ret.put("statusDesc", "获取通知列表失败");
	            return ret;
	        }
	        //暂时屏蔽等产品需求修改完成后返回正确数据
//	        ret.put("count", count);
//	        ret.put("msgList", msgPushList);
	        ret.put("count", 0);
	        ret.put("msgList", new ArrayList<MsgPushBean>());
	        return ret;
	    }
    /**
     * 格式化时间
     * @param sendtime
     * @return
     */
//    public String formatTime(Integer sendtime){
//		//00:00:00 时间戳
//		Integer nowTime = GetDate.getSearchStartTime(GetDate.getDate());
//		//如果发送时间大于 时间戳   则为今天的数据
//		if(sendtime >= nowTime){
//	    	Timestamp time = new Timestamp(Long.valueOf(sendtime) * 1000);
//			Date date = null;
//			if (null != time) {
//				date = new Date(time.getTime());
//			}
//			return GetDate.dateToString2(date, "HH:mm");
//		}else{
//			return GetDate.times10toStrYYYYMMDD(sendtime);
//		}
//    }
    
    
	/**
	 * 消息全读
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(MsgPushDefine.ALREADY_READ_ACTION)
	public JSONObject alreadyReadAction(HttpServletRequest request) {
		LogUtil.startLog(THIS_CLASS, MsgPushDefine.ALREADY_READ_ACTION);
		JSONObject ret = new JSONObject();
		ret.put("request", MsgPushDefine.RETURN_REQUEST + MsgPushDefine.ALREADY_READ_ACTION);
		// 版本号
		String version = request.getParameter("version");
		// 平台
		String platform = request.getParameter("platform");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 检查参数正确性
		if (Validator.isNull(version) || Validator.isNull(platform) || Validator.isNull(sign)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得加密用的Key
		String key = SecretUtil.getKey(sign);
		if (Validator.isNull(key)) {
			ret.put("status", "1");
			ret.put("statusDesc", "请求参数非法");
			return ret;
		}
		// 取得用户iD
		Integer userId = null;
		try {
			userId = SecretUtil.getUserId(sign);
		} catch (Exception e) {
			userId = null;
		}
		ret.put("status", "0");
		ret.put("statusDesc", "成功");
		this.msgPushService.updateAllMsgPushMsgHistory(userId, platform);
		return ret;
	}
	
    
    
	/**
	 * 消息及消息推送已读
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(MsgPushDefine.MSG_READ_ACTION)
    public JSONObject msgReadAction(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(THIS_CLASS, MsgPushDefine.MSG_READ_ACTION);
    	JSONObject ret = new JSONObject();
    	ret.put("request", MsgPushDefine.RETURN_REQUEST + MsgPushDefine.MSG_READ_ACTION);
        // 版本号
        String version = request.getParameter("version");
        // 平台
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");
        //消息ID
        String msgIdStr = request.getParameter("msgId");
        
        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(platform) ||  Validator.isNull(sign)||  Validator.isNull(msgIdStr)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
		ret.put("status", "0");
		ret.put("statusDesc", "成功");
		//更新记录信息
		MessagePushMsgHistory msgHistory = this.msgPushService.getMsgPushMsgHistoryById(Integer.valueOf(msgIdStr));
		if(msgHistory != null){
			//第一次阅读
			if(msgHistory.getMsgReadCountAndroid() == 0 && msgHistory.getMsgReadCountIos() == 0){
				msgHistory.setMsgFirstreadPlat(Integer.valueOf(platform));
			}
			if(platform.equals(CustomConstants.CLIENT_ANDROID)){
				msgHistory.setMsgReadCountAndroid(msgHistory.getMsgReadCountAndroid() + 1);
				
			}else if(platform.equals(CustomConstants.CLIENT_IOS)){
				msgHistory.setMsgReadCountIos(msgHistory.getMsgReadCountIos() + 1);
			}
			this.msgPushService.updateMsgPushMsgHistory(msgHistory);
		}
		return ret;
    }

    
	/**
	 * 通知详情页
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = MsgPushDefine.MSG_DETAIL_ACTION)
	public ModelAndView msgDetailAction(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(MsgPushDefine.MSG_DETAIL_PAGE);
        // 唯一标识
        String sign = request.getParameter("sign");
        //消息ID
        String msgIdStr = request.getParameter("msgId");
        
        // 检查参数正确性
        if (Validator.isNull(sign)||  Validator.isNull(msgIdStr)) {
            modelAndView = new ModelAndView(MsgPushDefine.ERROR_PAGE);
            return modelAndView;
        }
        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
        	 modelAndView = new ModelAndView(MsgPushDefine.ERROR_PAGE);
             return modelAndView;
        }
		//获取记录信息
		MessagePushMsgHistory msgHistory = this.msgPushService.getMsgPushMsgHistoryById(Integer.valueOf(msgIdStr));
		modelAndView.addObject("msgHistory", msgHistory);
		
        return modelAndView;
	}
    
}




