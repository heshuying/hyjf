/**
 * Description:用户还款明细接口类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.repaymentdetail;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterHtjRepaymentDetailCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterHztRepaymentDetailCustomize;

public interface RepaymentDetailService {
    /**
     * 按照用户名/手机号查询还款明细（直投产品，含承接的债权）
     * @param user
     * @return List<CouponUserCustomize>
     * @author pcc
     */
    public List<CallCenterHztRepaymentDetailCustomize> getHztRepaymentDetailList(Users user, Integer limitStart,
        Integer limitEnd);
    /**
     * 按照用户名/手机号查询还款明细（汇添金）
     * @param user
     * @return List<CouponUserCustomize>
     * @author pcc
     */
    public List<CallCenterHtjRepaymentDetailCustomize> getHtjRepaymentDetailList(Users user, Integer limitStart,
        Integer limitEnd);
}
