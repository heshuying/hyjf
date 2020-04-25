package com.hyjf.web.activity.invitesix;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.QRCodeUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.regist.UserRegistDefine;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = ActivityInviteSixDefine.REQUEST_MAPPING)
public class ActivityInviteSixController extends BaseController {
    
    /**
     * 
     * 六月份活动
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = ActivityInviteSixDefine.INIT_REQUEST_MAPPING)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityInviteSixController.class.toString(), ActivityInviteSixDefine.INIT_REQUEST_MAPPING);
        ModelAndView modelAndView = new ModelAndView(ActivityInviteSixDefine.ACTIVITY_INVITESIX_PATH);
        
        String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
        String fileUploadRealPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));

        Integer userId = WebUtils.getUserId(request);
        if(Validator.isNull(userId)){
            modelAndView.addObject("isLogin", 0);
        }else {
            try {
                QRCodeUtil.encode(PropUtils.getSystem("hyjf.wechat.invite.url")+userId+".html",String.valueOf(userId),filePhysicalPath + fileUploadRealPath, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            modelAndView.addObject("isLogin", 1);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("webCatLink", PropUtils.getSystem("hyjf.wechat.invite.url")+userId+".html");
            modelAndView.addObject("inviteLink", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+UserRegistDefine.REQUEST_MAPPING+"/"+UserRegistDefine.INIT+".do?from="+userId);
        }
        
        return modelAndView;
    }
}
