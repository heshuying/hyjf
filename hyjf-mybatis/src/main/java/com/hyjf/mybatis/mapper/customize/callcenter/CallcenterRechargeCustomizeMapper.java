package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallCenterRechargeCustomize;

public interface CallcenterRechargeCustomizeMapper {
    /**
     * 充值管理 （列表）
     * @param accountManageBean
     * @return
     */
    public List<CallCenterRechargeCustomize> queryRechargeList(CallCenterRechargeCustomize callCenterRechargeCustomize);
}
