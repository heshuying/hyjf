package com.hyjf.admin.exception.rechargewarnexception;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminRechargeWarnExceptionCustomize;

public interface RechargeWarnExceptionService {

    /**
     * 充值管理 （账户数量）
     * @param accountManageBean
     * @return
     */
    public Integer queryRechargeWarnCount(AdminRechargeWarnExceptionCustomize rechargeWarnExceptionCustomize);

    /**
     * 充值管理 （列表）
     * @param accountManageBean
     * @return
     */
    public List<AdminRechargeWarnExceptionCustomize> queryRechargeWarnList(AdminRechargeWarnExceptionCustomize rechargeWarnExceptionCustomize);

}
