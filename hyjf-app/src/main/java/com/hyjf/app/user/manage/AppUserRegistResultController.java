package com.hyjf.app.user.manage;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;

/**
 * @author fuqiang
 */
public class AppUserRegistResultController {

    /**
     * THIS_CLASS
     */
    private static final String THIS_CLASS = AppUserRegistResultController.class.getName();

    private static final String SUCCESS_URL = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/user/regist/result/success";
    private static final String FAIL_URL = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/user/regist/result/faild";
    private static final String HANDING_URL = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+"/user/regist/result/handing";

    
    
    /**
     * 注册结果
     * @param status
     * @return
     */
    @RequestMapping(AppUserDefine.REGIST_RESULT_ACTION)
    public RegistResultBean getRegistResult(@PathVariable("status") String status){

        LogUtil.startLog(THIS_CLASS, AppUserDefine.REGIST_RESULT_ACTION);

        RegistResultBean resultBean = new RegistResultBean();

        // 注册失败
        if (status.equals("1")) {
            resultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            resultBean.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
            resultBean.setResultUrl(AppUserRegistResultController.FAIL_URL);
            // 注册成功
        } else if (status.equals("0")) {
            resultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            resultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            resultBean.setResultUrl(AppUserRegistResultController.SUCCESS_URL);
        }

        LogUtil.endLog(THIS_CLASS, AppUserDefine.REGIST_RESULT_ACTION);

        return resultBean;


    }
}
