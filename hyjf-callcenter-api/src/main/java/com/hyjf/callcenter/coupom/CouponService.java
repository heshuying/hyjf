/**
 * Description:用户优惠券接口类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.coupom;

import java.util.List;

import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponBackMoneyCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponTenderCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponUserCustomize;

public interface CouponService {

	/**
	 * 按照用户名/手机号查询优惠券
	 * @param user
	 * @return List<CouponUserCustomize>
	 * @author pcc
	 */
	public List<CallCenterCouponUserCustomize> selectCouponUserList(Users user,Integer limitStart, Integer limitEnd);

    public List<ParamName> getParamNameList(String nameClass);
    /**
     * 按照用户名/手机号查询优惠券使用（直投产品）
     * @param user
     * @return List<CouponUserCustomize>
     * @author pcc
     */
    public List<CallCenterCouponTenderCustomize> selectCouponTenderList(Users user, Integer limitStart, Integer limitEnd);
    
    /**
     * 按照用户名/手机号查询优惠券回款（直投产品）
     * 
     * @param CouponConfigCustomize
     * @return
     */
    public List<CallCenterCouponBackMoneyCustomize> selectCouponBackMoneyList(Users user, Integer limitStart, Integer limitEnd);
}
