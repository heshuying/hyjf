/**
 * Description:获取指定的项目类型的项目常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 *
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */
package com.hyjf.wechat.controller.borrow;

import com.hyjf.wechat.base.BaseDefine;

public class WxBorrowDefine extends BaseDefine {
  //类名
    public static final String THIS_CLASS = WxBorrowController.class.getName();

    //指定类型的项目
    public static final String REQUEST_MAPPING = "/wx/bank/wechat/borrow";

    //项目详情
    public static final String PROJECT_DETAIL_ACTION = "/{borrowId}";

    //出借记录
    public static final String PROJECT_INVEST_ACTION = "/{borrowId}/investRecord";

    

    //获取散标出借信息（年化率、预期收益等）
    public static final String GET_INVEST_INFO_MAPPING = "/getInvestInfo";
    //散标出借校验
    /** @RequestMapping值 */
    public static final String TENDER_URL_ACTION = "/getTenderUrl";

    /** 出借 @RequestMapping值 */
    public static final String INVEST_ACTION = "/tender";
    
    
    /** 出借后同步回调 @RequestMapping值 */
    public static final String RETURL_SYN_ACTION = "/returl";

    /** 出借后异步回调 @RequestMapping值 */
    public static final String RETURL_ASY_ACTION = "/bgreturl";
    
    
    // 结果页跳转处理页面
    public static final String JUMP_HTML = "/jumpHTML";
    
    /** app出借失败结果页*/
    public static final String JUMP_HTML_WECHAT_FAILED_PATH = "/borrow/{borrowId}/result/failed";
    /** app出借成功结果页*/
    public static final String JUMP_HTML_WECHAT_SUCCESS_PATH = "/borrow/{borrowId}/result/success";
}
