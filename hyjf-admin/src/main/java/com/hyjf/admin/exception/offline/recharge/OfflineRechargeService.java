package com.hyjf.admin.exception.offline.recharge;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.OfflineRechargeCustomize;

public interface OfflineRechargeService {


    /**
     * 取得需要查询的线下充值的用户信息
     * @param bankAccount
     * @return
     */
    List<OfflineRechargeCustomize> selectUserAccount(OfflineRechargeBean offlineRechargeBean);

}
