/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:32:36
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.chinapnrlogin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.recharge.UserRechargeDefine;
import com.hyjf.web.util.WebUtils;

/**
 * @package com.hyjf.web.borrow.invest
 * @author 郭勇
 * @date 2015/12/04 14:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ChinapnrLoginDefine.REQUEST_MAPPING)
public class ChinapnrLoginController extends BaseController {

    @Autowired
    private ChinapnrLoginService chinapnrLoginService;

    /**
     * 
     * 跳转登录汇付天下
     * @author renxingchen
     * @param request
     * @return
     */
    @RequestMapping(value = ChinapnrLoginDefine.LOGIN_MAPPING)
    public ModelAndView loginChinapnr(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(UserRechargeDefine.JSP_CHINAPNR_SEND);
        Integer userId = WebUtils.getUserId(request);
        // 查询用户的usrcustid
        AccountChinapnr accountChinapnr = chinapnrLoginService.getAccountChinapnr(userId);
        if (null != accountChinapnr) {
            Long usrCustId = accountChinapnr.getChinapnrUsrcustid();
            if (null != usrCustId) {
                ChinapnrBean bean = new ChinapnrBean();
                bean.setVersion(ChinaPnrConstant.VERSION_10);
                bean.setCmdId(ChinaPnrConstant.CMDID_USER_LOGIN);
                bean.setUsrCustId(usrCustId.toString());
                try {
                    modelAndView = ChinapnrUtil.callApi(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {// 提示用户系统异常

            }
        } else {// 提示用户系统异常

        }
        return modelAndView;
    }
}
