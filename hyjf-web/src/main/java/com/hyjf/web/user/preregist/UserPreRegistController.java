package com.hyjf.web.user.preregist;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.web.BaseController;

@Controller("userPreRegistController")
@RequestMapping(value = UserPreRegistDefine.REQUEST_MAPPING)
public class UserPreRegistController extends BaseController {

	@Autowired
	private UserPreRegistService preRegistService;
	
	/**
     * 初期化,跳转到活动Home页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = UserPreRegistDefine.HOME)
    public ModelAndView userPreRegistHome(HttpServletRequest request) throws Exception {
        LogUtil.startLog(UserPreRegistDefine.class.getName(), UserPreRegistDefine.HOME);
        ModelAndView modeAndView = new ModelAndView(UserPreRegistDefine.INIT_HOME);
        LogUtil.endLog(UserPreRegistController.class.getName(), UserPreRegistDefine.HOME);
        return modeAndView;
    }

	/**
	 * 初期化,跳转到预注册页面
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = UserPreRegistDefine.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request, @RequestParam(required = false) String from, @RequestParam(required = false) String utm_id) throws Exception {
		LogUtil.startLog(UserPreRegistDefine.class.getName(), UserPreRegistDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(UserPreRegistDefine.INIT_PATH);
		modeAndView.addObject("from", from);
		modeAndView.addObject("utmId", utm_id);
		LogUtil.endLog(UserPreRegistController.class.getName(), UserPreRegistDefine.INIT);
		return modeAndView;
	}
	
	/**
     * 初期化,输入手机号保存预注册信息
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = UserPreRegistDefine.PREREGIST_SAVE_PATH, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String preregistSubmit(HttpServletRequest request, HttpServletResponse response, UserPreRegistBean form) {
        LogUtil.startLog(UserPreRegistDefine.THIS_CLASS, UserPreRegistDefine.PREREGIST_SAVE_PATH);
        JSONObject ret = new JSONObject();
        try{
            if(StringUtils.isNotEmpty(form.getMobile())){
                if(Pattern.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$", form.getMobile())){
                    //手机号预注册保存
                    Map<String, Object> resultMap = preRegistService.savePreregist(form);
                    if((Boolean)resultMap.get("success")){
                        ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
                        ret.put(CustomConstants.DATA, null);
                        ret.put(CustomConstants.MSG, resultMap.get("msg")); 
                    }else{
                        ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                        ret.put(CustomConstants.DATA, null);
                        ret.put(CustomConstants.MSG, resultMap.get("msg")); 
                    }
                }else{
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, null);
                    ret.put(CustomConstants.MSG, "手机号格式不符"); 
                }
            }else{
                ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                ret.put(CustomConstants.DATA, null);
                ret.put(CustomConstants.MSG, "手机号码不能为空");
            }
        }catch(Exception e){
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, null);
            ret.put(CustomConstants.MSG, "系统异常");
        }
        LogUtil.endLog(UserPreRegistDefine.THIS_CLASS, UserPreRegistDefine.PREREGIST_SAVE_PATH);
        return JSONObject.toJSONString(ret, true);
    }
}
