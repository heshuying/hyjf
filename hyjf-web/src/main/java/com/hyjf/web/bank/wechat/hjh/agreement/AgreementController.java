package com.hyjf.web.bank.wechat.hjh.agreement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.web.BaseController;
/**
 * 
 * 协议相关  /wx/agreement
 * @author sss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年11月13日
 * @see 下午1:50:38+
 */
@Controller("hjhAgreementController")
@RequestMapping(value = AgreementDefine.REQUEST_MAPPING)
public class AgreementController extends BaseController {

    /**
     * 跳转协议
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AgreementDefine.GODETAIL_MAPPING)
    public ModelAndView goDetail(String pageType) {
        LogUtil.startLog(AgreementController.class.getName(), AgreementDefine.GODETAIL_MAPPING);
        ModelAndView modeAndView = null;
        if (pageType.equals("wx_hjh_contract")) {
            // 微信 散标风险揭示书H5
            modeAndView = new ModelAndView("invest/app_contract");
        } else if(pageType.equals("wx_hjh_serve")){
            // 智投服务协议
            modeAndView = new ModelAndView("invest/type-new-hjhxieyi");
        } else if(pageType.equals("wx_hjh_invest")){
            // 居间服务协议
            modeAndView = new ModelAndView("invest/type-invest");
        }
        LogUtil.endLog(AgreementController.class.getName(), AgreementDefine.GODETAIL_MAPPING);
        return modeAndView;
    }
}
