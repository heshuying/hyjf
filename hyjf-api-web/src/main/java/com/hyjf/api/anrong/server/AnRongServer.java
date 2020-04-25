package com.hyjf.api.anrong.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.anrong.util.DoResultService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.pay.lib.anrong.bean.AnRongBean;
import com.hyjf.pay.lib.anrong.util.AnRongCallUtils;

/**
 * 
 * 安融接口
 * @author sss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年10月10日
 * @see 下午3:48:46
 */
@Controller
@RequestMapping(AnRongDefine.REQUEST_MAPPING)
public class AnRongServer extends BaseController{
	Logger _log = LoggerFactory.getLogger("AccountServer");
	
	@Autowired
	private DoResultService util;
	
	/**
	 * 
	 * 调用安融接口  查询用户信息
	 * @author sss
	 * @return
	 */
    @RequestMapping(AnRongDefine.URL_ANRONG_QUERY_USER)
    @ResponseBody
    public String queryUser(AnRongBean anRongBean){
        LogUtil.startLog(AnRongServer.class.getName(), AnRongDefine.URL_ANRONG_QUERY_USER);
        _log.info("请求查询接口参数："+JSONObject.toJSON(anRongBean));
        JSONObject ret = new JSONObject();
        anRongBean.setTxCode(AnRongDefine.TXCODE_QUERYUSER);
        anRongBean.setSystemParm(anRongBean.getLogUserId());
        // 调用安融接口
        String result = AnRongCallUtils.callApiBg(anRongBean);
        _log.info("调用安融接口返回结果："+result);
        JSONObject resultJson = JSONObject.parseObject(result);
        JSONObject isOk = getResult(ret, resultJson,anRongBean.getLoanId());
        if(isOk.getBooleanValue(AnRongDefine.RESULT_JSON_KEY_FQZ_SUCCESS)||isOk.getBooleanValue(AnRongDefine.RESULT_JSON_KEY_MSP_SUCCESS)){
         // 插入数据
            util.insertResult(resultJson,anRongBean.getLoanId());
        }
        
        return isOk.toString();
    }
    
    /**
     * 
     * 调用安融共享接口
     * @author sss
     * @param anRongBean
     * @return
     */
    @RequestMapping(AnRongDefine.URL_ANRONG_SEND)
    @ResponseBody
    public String send(AnRongBean anRongBean){
        LogUtil.startLog(AnRongServer.class.getName(), AnRongDefine.URL_ANRONG_SEND);
        _log.info("请求共享接口参数："+JSONObject.toJSON(anRongBean));
        JSONObject ret = new JSONObject();
        anRongBean.setSystemParm(anRongBean.getLoanId(),anRongBean.getLogUserId());
        anRongBean.setTxCode(AnRongDefine.TXCODE_SENDMESS);
        // 调用安融接口
        String result = AnRongCallUtils.callApiBg(anRongBean);
        _log.info("调用安融接口返回结果："+result);
        JSONObject resultJson = JSONObject.parseObject(result);
        
        return getResultForSend(ret, resultJson).toString();
    }

    // 返回客户端信息   共享接口
    private Object getResultForSend(JSONObject ret, JSONObject resultJson) {
        ret.put(AnRongDefine.RESULT_JSON_KEY_SUCCESS, true);
        // 有错误
        if (resultJson.containsKey(AnRongDefine.RESULT_KEY_ERRORS)) {
            ret.put(AnRongDefine.RESULT_JSON_KEY_SUCCESS, false);
            StringBuffer errorMess = new StringBuffer("错误信息：<br/>");
            JSONArray errors = resultJson.getJSONArray(AnRongDefine.RESULT_KEY_ERRORS);
            for (Object object : errors) {
                JSONObject obj = (JSONObject) object;
                errorMess.append(obj.get(AnRongDefine.RESULT_KEY_MSG)).append("<br/>");
            }
            ret.put(AnRongDefine.RESULT_JSON_KEY_MSG, errorMess.toString());
        }
        return ret;
    }

    // 获得返回客户端的处理结果   查询接口
    private JSONObject getResult(JSONObject ret, JSONObject resultJson,String reqId) {
        boolean fqz_success = true;
        String fqz_mess = "请求成功";
        boolean msp_success = true;
        String msp_mess = "请求成功";
        // fqz 有错误
        if(resultJson.getJSONObject(AnRongDefine.RESULT_KEY_FQZ).containsKey(AnRongDefine.RESULT_KEY_ERRORCODE)){
            // 如果有错误
            fqz_success = false;
            String errs = resultJson.getJSONObject(AnRongDefine.RESULT_KEY_FQZ).getString(AnRongDefine.RESULT_KEY_ERRORCODE);
            fqz_mess = "请求失败，返回值:"+errs;
        }
        if(resultJson.getJSONObject(AnRongDefine.RESULT_KEY_MSP).containsKey(AnRongDefine.RESULT_KEY_ERRORS)){
            // 如果有错误
            msp_success = false;
            msp_mess = "请求失败，返回值:";
            JSONArray errs = resultJson.getJSONObject(AnRongDefine.RESULT_KEY_MSP).getJSONArray(AnRongDefine.RESULT_KEY_ERRORS);
            for (Object object : errs) {
                JSONObject aError = (JSONObject) object;
                msp_mess += aError.getString(AnRongDefine.RESULT_KEY_MSG) +" <br/> ";
            }
        }
        if(fqz_success&&msp_success){
            ret.put(AnRongDefine.RESULT_JSON_KEY_SUCCESS, true);
        }else{
            ret.put(AnRongDefine.RESULT_JSON_KEY_SUCCESS, false);
        }
        ret.put(AnRongDefine.RESULT_JSON_KEY_FQZ_SUCCESS, fqz_success);
        ret.put(AnRongDefine.RESULT_JSON_KEY_FQZ_MESS, fqz_mess);
        ret.put(AnRongDefine.RESULT_JSON_KEY_MSP_SUCCESS, msp_success);
        ret.put(AnRongDefine.RESULT_JSON_KEY_MSP_MESS, msp_mess);
        ret.put(AnRongDefine.RESULT_JSON_KEY_REQID, reqId);
        return ret;
    }
        
    
}
