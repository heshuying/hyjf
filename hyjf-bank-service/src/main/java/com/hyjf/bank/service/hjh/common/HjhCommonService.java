package com.hyjf.bank.service.hjh.common;

import java.math.BigDecimal;

import com.hyjf.bank.service.BaseService;

/**
 * 
 * 汇计划更新表
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月15日
 * @see 下午3:00:47
 */
public interface HjhCommonService extends BaseService{
    
    /**
     * 汇计划全部流程用更新用户的账户表
     * @param hjhProcessFlg
     * @param userId
     * @param amount
     * @param interest
     * @return
     */
    public Boolean updateAccountForHjh(String hjhProcessFlg, Integer userId, BigDecimal amount, BigDecimal interest) ;
    
    /**
     * 汇计划重算更新汇计划加入明细表
     * @param hjhProcessFlg
     * @param id
     * @param amount
     * @param interest
     * @return
     */
    public Boolean updateHjhAccedeForHjh(String hjhProcessFlg, Integer id, BigDecimal amount, BigDecimal interest, BigDecimal serviceFee) ;
}
