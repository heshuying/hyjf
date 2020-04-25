package com.hyjf.app.aboutus.notice;

import com.hyjf.app.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 维护通知
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年4月6日
 * @see 6:04:17
 */
@Controller
@RequestMapping(value = NoticeDefine.REQUEST_MAPPING)
public class NoticeController extends BaseController {
    
    @RequestMapping(value = NoticeDefine.INIT_REQUEST_MAPPING)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NoticeController.class.toString(), NoticeDefine.INIT_REQUEST_MAPPING);
        
        ModelAndView modelAndView = new ModelAndView(NoticeDefine.NOTICE_PATH);

        String timeRange = PropUtils.getSystem("hyjf.notice.timerange");
        String[] rangeArray = timeRange.split(",");
        String rangeStart = GetDate.formatTime(Long.parseLong(rangeArray[0]+"000"));
        String rangeEnd = GetDate.formatTime(Long.parseLong(rangeArray[1]+"000"));
        
        modelAndView.addObject("rangeStart", rangeStart);
        modelAndView.addObject("rangeEnd", rangeEnd);
        return modelAndView;
    }
    
    /**
     * 
     * ios下载页
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = NoticeDefine.NOTICE_IOS_REQUEST_MAPPING)
    public ModelAndView initIOS(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(NoticeController.class.toString(), NoticeDefine.INIT_REQUEST_MAPPING);
        
        ModelAndView modelAndView = new ModelAndView(NoticeDefine.NOTICE_IOS_PATH);
        String version = request.getParameter("version");
        
        //String downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财-江西银行存管11-出借平台/id1044961717?mt=8";
        String downloadUrl = "itms-apps://itunes.apple.com/cn/app/id1044961717?mt=8";
        /*if(StringUtils.isEmpty(version)){
            modelAndView.addObject("downLoadUrl", downloadUrl);
            return modelAndView;
        }
        
        String pcode = "";
        String vers[] = version.split("\\."); // 取渠道号
        if(vers != null && vers.length > 3){
            pcode = vers[3] ;
        }
        
        if(StringUtils.isEmpty(pcode)){
            modelAndView.addObject("downLoadUrl", downloadUrl);
            return modelAndView;
        }
        
        if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_39)) {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财-pro版-江西银行存管11-金融出借平台/id1135799121?mt=8";
            
        } else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_YXB)) {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财悦享版-江西银行存管11-金融出借平台/id1213322885?mt=8";
            
        } else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_ZNB)) {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财周年版-江西银行存管11-金融出借平台/id1213323446?mt=8";
            
        } else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_ZYB)) {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财专业版-江西银行存管11-金融出借平台/id1213321340?mt=8";
            
        } else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_ZZB)) {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财至尊版-江西银行存管11-出借平台/id1213321356?mt=8";
            
        } else if (pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_TEST)) {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财-江西银行存管11-出借平台/id1044961717?mt=8";
            
        } else if(pcode.equals(CustomConstants.MSG_PUSH_PACKAGE_CODE_79)){
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财-江西银行存管11-出借平台/id1044961717?mt=8";
        }else {
            downloadUrl = "https://itunes.apple.com/cn/app/汇盈金服理财-江西银行存管11-出借平台/id1044961717?mt=8";
        }*/

        modelAndView.addObject("downLoadUrl", downloadUrl);
        return modelAndView;
        
    }
        
}
