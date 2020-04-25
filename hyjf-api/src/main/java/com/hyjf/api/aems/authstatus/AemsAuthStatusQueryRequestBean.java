package com.hyjf.api.aems.authstatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.util.AemsErrorCodeConstant;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.validator.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 受托支付授权页面请求参数
 * jijun 20180911
 */
public class AemsAuthStatusQueryRequestBean extends BaseBean {

    /**
     * 
     * 检查参数是否为空
     * @author sss
     * @param modelAndView
     * @return
     */
    public boolean checkParmIsNull() {
        boolean result = false;
        if (Validator.isNull(getTimestamp())) {
            return true;
        }
        if (Validator.isNull(getInstCode())) {
            return true;
        }
        if (Validator.isNull(getChkValue())) {
            return true;
        }
        if (Validator.isNull(getAccountId())) {
            return true;
        }
        
        return result;
    }
    
    public Map<String, String> getErrorMap(String status, String statusDesc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("accountId", getAccountId());
        params.put("status", status);
        params.put("statusDesc",statusDesc);
        params.put("acqRes",getAcqRes());
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(status);
        params.put("chkValue", resultBean.getChkValue());
        return params;
    }
    
    public JSONObject getErrorJson(String status ,String statusDesc) {
        Map<String, String> error = getErrorMap(status,statusDesc);
        JSONObject result = new JSONObject();
        result.putAll(error);
        return result;
    }
    
    public JSONObject getSuccessJson(String statusDesc) {
        Map<String, String> error = getSuccessMap(statusDesc);
        JSONObject result = new JSONObject();
        result.putAll(error);
        return result;
    }
    
    // 这个需要新增参数
    public Map<String, String> getSuccessMap(String statusDesc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("accountId", getAccountId());
        params.put("status", AemsErrorCodeConstant.SUCCESS);
        params.put("statusDesc",statusDesc);
        BaseResultBean resultBean = new BaseResultBean();
        resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
        params.put("chkValue", resultBean.getChkValue());
        return params;
    }
}
