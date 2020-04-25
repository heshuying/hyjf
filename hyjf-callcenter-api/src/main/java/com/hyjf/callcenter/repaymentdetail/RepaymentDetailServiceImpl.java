/**
 * Description:用户还款明细信息实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.repaymentdetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterHtjRepaymentDetailCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterHztRepaymentDetailCustomize;

@Service
public class RepaymentDetailServiceImpl extends CustomizeMapper implements RepaymentDetailService {
    /**
     * 
     * 按照用户名/手机号查询还款明细（直投产品，含承接的债权）
     * @author pcc
     * @param user
     * @param limitStart
     * @param limitEnd
     * @return
     * @see com.hyjf.callcenter.repaymentdetail.RepaymentDetailService#getHztRepaymentDetailList(com.hyjf.mybatis.model.auto.Users, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<CallCenterHztRepaymentDetailCustomize> getHztRepaymentDetailList(Users user, Integer limitStart,
        Integer limitEnd) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", user.getUserId());
        paraMap.put("limitStart", limitStart);
        paraMap.put("limitEnd", limitEnd);
        return callCenterRepaymentDetailCustomizeMapper.getHztRepaymentDetailList(paraMap);
    }
    /**
     * 
     * 按照用户名/手机号查询还款明细（汇添金）
     * @author pcc
     * @param user
     * @param limitStart
     * @param limitEnd
     * @return
     * @see com.hyjf.callcenter.repaymentdetail.RepaymentDetailService#getHtjRepaymentDetailList(com.hyjf.mybatis.model.auto.Users, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<CallCenterHtjRepaymentDetailCustomize> getHtjRepaymentDetailList(Users user, Integer limitStart,
        Integer limitEnd) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", user.getUserId());
        paraMap.put("limitStart", limitStart);
        paraMap.put("limitEnd", limitEnd);
        return callCenterRepaymentDetailCustomizeMapper.getHtjRepaymentDetailList(paraMap);
    }
}


