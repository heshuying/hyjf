/**
 * Description:按照用户名/手机号查询汇付绑卡关系用常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.account.huifu;

import com.hyjf.callcenter.base.BaseDefine;

public class AccountHuifuDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/account/huifu";
   
    /** 按照用户名/手机号查询汇付绑卡关系 */
    public static final String INIT_TIED_CARD_ACTION = "getTiedcardInfo";
}
