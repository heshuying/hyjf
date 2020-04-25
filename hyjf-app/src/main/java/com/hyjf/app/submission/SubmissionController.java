package com.hyjf.app.submission;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;

@Controller
@RequestMapping(value=SubmissionDefine.REQUEST_MAPPING)
public class SubmissionController  extends BaseController{
	/** 类名 */
	public static final String THIS_CLASS = SubmissionController.class.getName();
    @Autowired
    private SubmissionService submissionService;
	
	/**
	 * 添加意见反馈
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = SubmissionDefine.ADD_SUBMISSION_ACTION, method = RequestMethod.POST) 
    public JSONObject addSubmission(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(THIS_CLASS, SubmissionDefine.ADD_SUBMISSION_ACTION);
    	JSONObject ret = new JSONObject();
    	ret.put("request", SubmissionDefine.RETURN_REQUEST);

        // 版本号
        String version = request.getParameter("version");
        // 网络状态
        String netStatus = request.getParameter("netStatus");
        // 平台 系统类别 0：PC，1：微官网，2：Android，3：IOS，4：其他
        String platform = request.getParameter("platform");
        // 唯一标识
        String sign = request.getParameter("sign");
        //意见反馈内容
        String content = request.getParameter("content");
        //手机型号
        String phoneType = request.getParameter("phoneType");
        //手机系统
        String systemVersion= request.getParameter("systemType");

        // 检查参数正确性
        if (Validator.isNull(version) || Validator.isNull(netStatus) || Validator.isNull(platform) || Validator.isNull(sign) || Validator.isNull(content)){    
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }

//        // 判断sign是否存在
//        boolean isSignExists = SecretUtil.isExists(sign);
//        if (!isSignExists) {
//            ret.put("status", "1");
//            ret.put("statusDesc", "请求参数非法");
//            return ret;
//        }

        // 取得加密用的Key
        String key = SecretUtil.getKey(sign);
        if (Validator.isNull(key)) {
            ret.put("status", "1");
            ret.put("statusDesc", "请求参数非法");
            return ret;
        }
        
        SubmissionsWithBLOBs submissionsWithBLOBs= new SubmissionsWithBLOBs();
        // 取得用户iD
        Integer userId = SecretUtil.getUserId(sign);
        submissionsWithBLOBs.setUserId(userId);
        submissionsWithBLOBs.setAddtime(GetDate.getNowTime10());
        submissionsWithBLOBs.setContent(content);
        submissionsWithBLOBs.setTitle(platform+"意见反馈");
        submissionsWithBLOBs.setProblem(platform);
        submissionsWithBLOBs.setState(0);//状态 0未审 1已审核
        submissionsWithBLOBs.setSysType(Integer.valueOf(platform));// 系统类别 0：PC，1：微官网，2：Android，3：IOS，4：其他
        submissionsWithBLOBs.setPlatformVersion(version);
        submissionsWithBLOBs.setPhoneType(phoneType);//手机型号
        submissionsWithBLOBs.setSysVersion(systemVersion);//操作系统版本号
        int result= this.submissionService.addSubmission(submissionsWithBLOBs);
        if(result==1){
        	ret.put("status", "0");
        	ret.put("statusDesc", "成功");
        }else{
        	ret.put("status", "2");
        	ret.put("statusDesc", "提交意见反馈出现异常，请重新操作！");
        }
        
    	LogUtil.endLog(THIS_CLASS, SubmissionDefine.ADD_SUBMISSION_ACTION);
    	return ret;
    	
    }
	
}




