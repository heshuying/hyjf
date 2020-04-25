/**
 * Description:江西银行账户常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.repaymentdetail;

import com.hyjf.callcenter.base.BaseDefine;

public class RepaymentDetailDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/repaymentdetail";
   
    /** 按照用户名/手机号查询还款明细（直投产品，含承接的债权） */
    public static final String GET_HZT_REPAYMENT_DETAIL_LIST_ACTION = "getHztRepaymentDetailList";

    /** 按照用户名/手机号查询还款明细（汇添金） */
    public static final String GET_HTJ_REPAYMENT_DETAIL_LIST_ACTION = "getHtjRepaymentDetailList";
    
}
