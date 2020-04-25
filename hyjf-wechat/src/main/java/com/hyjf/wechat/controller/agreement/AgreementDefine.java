package com.hyjf.wechat.controller.agreement;

import com.hyjf.wechat.base.BaseDefine;

/**
 * 
 * 微信端协议
 * @author sss
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年11月13日
 * @see 下午1:54:04
 */
public class AgreementDefine extends BaseDefine {

    /** 微信端协议 @RequestMapping值 /wx/agreement */
    public static final String REQUEST_MAPPING = "/wx/agreement";
    /**展示协议 */
    public static final String GODETAIL_MAPPING = "/goDetail";
    /**获得协议图片 */
    public static final String GOAGREEMENT_IMG = "/goAgreementImg";
    /**协议名称 动态获得 */
    public static final String GET_DISPLAY_NAME_DYNAMIC = "/getdisplayNameDynamic";
}
