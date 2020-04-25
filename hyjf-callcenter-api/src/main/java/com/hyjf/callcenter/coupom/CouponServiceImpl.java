/**
 * Description:用户优惠券信息实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.coupom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponBackMoneyCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponTenderCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterCouponUserCustomize;

@Service
public class CouponServiceImpl extends CustomizeMapper implements CouponService {

    /**
     * 按照用户名/手机号查询优惠券
     * @param user
     * @return List<CouponUserCustomize>
     * @author pcc
     */
    @Override
	public List<CallCenterCouponUserCustomize> selectCouponUserList(Users user,Integer limitStart, Integer limitEnd) {
		// 封装查询条件
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("userId", user.getUserId());
		paraMap.put("limitStart", limitStart);
		paraMap.put("limitEnd", limitEnd);
		// 查询用户列表
		return callCenterCouponUserCustomizeMapper.selectCouponUserList(paraMap);
	}
	
	/**
     * 获取数据字典表的下拉列表
     *
     * @return
     */
    @Override
    public List<ParamName> getParamNameList(String nameClass) {
        ParamNameExample example = new ParamNameExample();
        ParamNameExample.Criteria cra = example.createCriteria();
        cra.andNameClassEqualTo(nameClass);
        cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
        example.setOrderByClause(" sort ASC ");
        return this.paramNameMapper.selectByExample(example);
    }
    /**
     * 按照用户名/手机号查询优惠券使用（直投产品）
     * @param user
     * @return List<CouponUserCustomize>
     * @author pcc
     */
    @Override
    public List<CallCenterCouponTenderCustomize> selectCouponTenderList(Users user, Integer limitStart, Integer limitEnd) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", user.getUserId());
        paraMap.put("limitStart", limitStart);
        paraMap.put("limitEnd", limitEnd);
        return callCenterCouponUserCustomizeMapper.selectCouponTenderList(paraMap);
    }
    /**
     * 按照用户名/手机号查询优惠券回款（直投产品）
     * 
     * @param CouponConfigCustomize
     * @return
     */
    @Override
    public List<CallCenterCouponBackMoneyCustomize> selectCouponBackMoneyList(Users user, Integer limitStart, Integer limitEnd) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", user.getUserId());
        paraMap.put("limitStart", limitStart);
        paraMap.put("limitEnd", limitEnd);
        return callCenterCouponUserCustomizeMapper.selectCouponBackMoneyList(paraMap);
    }
}


