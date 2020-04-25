/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.nifa;

import com.hyjf.mybatis.model.customize.nifa.NifaTenderUserInfoCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaBorrowerInfoCustomizeMapper, v0.1 2018/12/11 15:28
 */
public interface NifaBorrowerInfoCustomizeMapper {
    /**
     * 查询用户借款笔数
     *
     * @param username
     * @return
     */
    Integer selectBorrowUsersCount(String username);

    /**
     * 查询用户借款笔数
     *
     * @param username
     * @return
     */
    Integer selectBorrowManInfoCount(String username);

    /**
     * 获取汇付绑定的所属银行
     *
     * @param userId
     * @return
     */
    List<String> selectBankFromAccountBank(Integer userId);

    /**
     * 根据借款编号查询该借款下所有投资人的相关信息
     *
     * @param borrowNid
     * @return
     */
    List<NifaTenderUserInfoCustomize> selectTenderUserInfoByBorrowNid(String borrowNid);
}
