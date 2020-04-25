package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditCustomize;

public interface DebtCreditCustomizeMapper {

    /**
     * 获取列表
     * 
     * @param DebtCreditCustomize
     * @return
     */
    List<DebtCreditCustomize> selectDebtCreditList(Map<String, Object> param);

    /**
     * COUNT
     * 
     * @param DebtCreditCustomize
     * @return
     */
    Integer countDebtCredit(Map<String, Object> param);

    /**
     * 
     * 查询此计划清算出来的债权总和
     * @author renxingchen
     * @param planNid
     * @return
     */
    Integer queryDebtCreditCount(String planNid);

    /**
     * 
     * 根据计划订单编号整合清算债权展示信息
     * @author renxingchen
     * @param debtCreditCustomize
     * @return
     */
    List<DebtCreditCustomize> selectDebtCreditForPages(DebtCreditCustomize debtCreditCustomize);

    DebtCreditCustomize selectDebtCreditForPagesSum(DebtCreditCustomize debtCreditCustomize);

}
