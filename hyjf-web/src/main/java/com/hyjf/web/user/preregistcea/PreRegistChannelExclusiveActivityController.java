package com.hyjf.web.user.preregistcea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;
import com.hyjf.web.BaseController;

@Controller("preRegistChannelExclusiveActivityController")
@RequestMapping(value = PreRegistChannelExclusiveActivityDefine.REQUEST_MAPPING)
public class PreRegistChannelExclusiveActivityController extends BaseController {

	@Autowired
	private PreRegistChannelExclusiveActivityService preRegistChannelExclusiveActivityService;

	/**
     * 初期化,输入手机号保存预注册信息
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = PreRegistChannelExclusiveActivityDefine.REWARDLIST, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String getRewardList(HttpServletRequest request, HttpServletResponse response, AdminPreRegistChannelExclusiveActivityCustomize form) {
        LogUtil.startLog(PreRegistChannelExclusiveActivityDefine.THIS_CLASS, PreRegistChannelExclusiveActivityDefine.REWARDLIST);
        JSONObject ret = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = null;
        try{
            if(StringUtils.isNotEmpty(form.getReward())){
                if("话费".equals(form.getReward())){
                    map.put("reward", "话费");
                    resultList = preRegistChannelExclusiveActivityService.getRecordList(map);
                }else if("门票".equals(form.getReward())){
                    map.put("reward", "门票");
                    resultList = preRegistChannelExclusiveActivityService.getRecordList(map);
                }
                ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
                ret.put(CustomConstants.DATA, resultList);
                ret.put(CustomConstants.MSG, "查询完成");
            }else{
                ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                ret.put(CustomConstants.DATA, null);
                ret.put(CustomConstants.MSG, "请提交要查询的奖励名称");
            }
        }catch(Exception e){
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, null);
            ret.put(CustomConstants.MSG, "系统异常");
        }
        LogUtil.endLog(PreRegistChannelExclusiveActivityDefine.THIS_CLASS, PreRegistChannelExclusiveActivityDefine.REWARDLIST);
        return JSONObject.toJSONString(ret, true);
    }
}
