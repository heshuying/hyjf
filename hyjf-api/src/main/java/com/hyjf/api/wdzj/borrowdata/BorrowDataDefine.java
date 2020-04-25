package com.hyjf.api.wdzj.borrowdata;

import com.hyjf.base.bean.BaseDefine;

public class BorrowDataDefine extends BaseDefine {
	
    /** 网贷之家标的信息数据接口  @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/wdzj/borrowdata";
    
    /** 网贷之家标的信息数据接口  @RequestMapping值*/
    public static final String GET_BORROWLIST = "/list";
    
    /** 网贷之家提前还款接口  @RequestMapping值*/
    public static final String GET_PREAPYSLIST = "/preapyslist";
    
}
