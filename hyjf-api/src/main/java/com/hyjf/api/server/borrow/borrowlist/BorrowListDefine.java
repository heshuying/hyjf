/**
 * Description:即信查询报文常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.api.server.borrow.borrowlist;

import com.hyjf.base.bean.BaseDefine;

/**
 * @author liubin
 */
public class BorrowListDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server/borrow/borrowList";
   
    /** 获取可投标的信息@RequestMapping值 */
    public static final String BORROW_LIST_ACTION = "/borrowList";
    /** 获取出借信息@RequestMapping值 */
    public static final String BORROW_LIST_INVEST = "/investList";

}
