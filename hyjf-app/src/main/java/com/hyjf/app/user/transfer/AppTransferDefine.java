package com.hyjf.app.user.transfer;

import com.hyjf.app.BaseDefine;

public class AppTransferDefine extends BaseDefine {

    /** Controller @RequestMapping */
    public static final String REQUEST_MAPPING = "/transfer";

    /** 类名 */
    public static final String THIS_CLASS = AppTransferController.class.getName();


    /** 汇转让出借详情 @RequestMapping */
    public static final String PROJECT_DETAIL_ACTION = "/{transferId}";
    
    
    /** 出借记录 @RequestMapping值 */
    public static final String PROJECT_INVEST_ACTION = "/{transferId}/investRecord";

}
