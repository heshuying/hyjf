package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;

public interface HjhDebtCreditCustomizeMapper {

    /**
     * 获取列表
     * 
     * @param param
     * @return
     */
    List<HjhDebtCreditCustomize> selectDebtCreditList(Map<String, Object> param);

    /**
     * COUNT
     * 
     * @param param
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
    List<HjhDebtCreditCustomize> selectDebtCreditForPages(HjhDebtCreditCustomize debtCreditCustomize);

    HjhDebtCreditCustomize selectDebtCreditForPagesSum(HjhDebtCreditCustomize debtCreditCustomize);

    /**
     * 总计
     * @param params
     * @return
     */
    Map<String,Object> selectSumRecord(Map<String, Object> params);

    /**
     *  查询展示列表的金额总计数据
     * @param params
     * @return
     */
    Map<String,Object> selectDebtCreditTotal(Map<String,Object> params);
}
