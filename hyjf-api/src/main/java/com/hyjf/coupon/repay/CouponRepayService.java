/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.coupon.repay;

import java.util.List;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

public interface CouponRepayService extends BaseService {
    List<CouponTenderCustomize> getCouponTenderList(String borrowNid);
    
    /**
     * 直投类优惠券还款
     * @param borrowNid
     * @param periodNow
     * @param ct
     * @throws Exception
     */
    void updateCouponRecoverMoney(String borrowNid, Integer periodNow, CouponTenderCustomize ct) throws Exception;

    /**
     * 汇添金优惠券还款
     * @param planNid
     * @param ct
     * @throws Exception
     */
    void updateCouponRecoverHjh(String planNid, CouponTenderCustomize ct) throws Exception;
    
    /**
     * 体验金按收益期限还款
     * @param nid
     * @throws Exception
     */
    void updateCouponOnlyRecover(String nid) throws Exception;

    List<CouponTenderCustomize> getCouponTenderListHjh(String orderId);
}
