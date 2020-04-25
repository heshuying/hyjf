package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.customize.RechargeCustomize;

public interface AdminRechargeExceptionCustomizeMapper {
    /**
     * 充值管理 （明细数量）
     * @param accountManageBean
     * @return
     */
    public Integer queryRechargeCount(RechargeCustomize rechargeCustomize);

    /**
     * 充值管理 （列表）
     * @param accountManageBean
     * @return
     */
    public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize);

    /**
     * 
     * 查询所有没有充值结果的充值记录
     * @author renxingchen
     * @return
     */
    public List<RechargeCustomize> selectNoResultRechargeList();

    public int updateAccountRechargeByPo(@Param("record") AccountRecharge accountRecharge,
        @Param("updateTime") Integer updateTime, @Param("nid") String nid);

}
