package com.hyjf.web.activity.inviteseven;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.common.util.QRCodeUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.borrow.BorrowBean;
import com.hyjf.web.user.regist.UserRegistDefine;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = ActivityInviteSevenDefine.REQUEST_MAPPING)
public class ActivityInviteSevenController extends BaseController {
    public static final String API_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL);
    
    private static final String GET_INVITELIST_API = "inviteseven/getInviteList";
    
    /**
     * 
     * 六月份活动
     * @author zhangjp
     * @return
     */
    @RequestMapping(value = ActivityInviteSevenDefine.INIT_REQUEST_MAPPING)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(ActivityInviteSevenController.class.toString(), ActivityInviteSevenDefine.INIT_REQUEST_MAPPING);
        ModelAndView modelAndView = new ModelAndView(ActivityInviteSevenDefine.ACTIVITY_INVITESEVEN_PATH);
        
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
    
    @ResponseBody
    @RequestMapping(value = ActivityInviteSevenDefine.GET_INVITESEVEN_LIST_MAPPING, produces = "application/json; charset=utf-8")
    public String searchBorrowList(@ModelAttribute BorrowBean form, HttpServletRequest request, HttpServletResponse response) {
        
        LogUtil.startLog(ActivityInviteSevenController.class.toString(), ActivityInviteSevenDefine.GET_INVITESEVEN_LIST_MAPPING);
        
        Integer userId = WebUtils.getUserId(request);
        
        String investType = request.getParameter("investType");
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");

        String result = "";
        if(userId == null){
            
        }else {
            Map<String, String> params = new HashMap<String, String>();
            // 用户编号
            params.put("userId", String.valueOf(userId));
            params.put("investType", investType);
            params.put("page", page);
            params.put("pageSize", pageSize);

            String getInviteListUrl = API_WEB_URL + GET_INVITELIST_API;
            
            
            result = HttpClientUtils.post(getInviteListUrl, params);
            
            
        }
        
        LogUtil.endLog(ActivityInviteSevenController.class.toString(), ActivityInviteSevenDefine.GET_INVITESEVEN_LIST_MAPPING);
        return result;
    }
}
