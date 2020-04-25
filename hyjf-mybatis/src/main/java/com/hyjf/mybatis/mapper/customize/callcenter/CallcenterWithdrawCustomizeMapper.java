package com.hyjf.mybatis.mapper.customize.callcenter;

import java.util.List;

import com.hyjf.mybatis.model.customize.WithdrawCustomize;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterWithdrawCustomize;

public interface CallcenterWithdrawCustomizeMapper {

    /**
     * 获取提现列表
     * 
     * @param CallcenterWithdrawCustomize
     * @return
     */
    List<CallcenterWithdrawCustomize> selectWithdrawList(CallcenterWithdrawCustomize callcenterWithdrawCustomize);
}