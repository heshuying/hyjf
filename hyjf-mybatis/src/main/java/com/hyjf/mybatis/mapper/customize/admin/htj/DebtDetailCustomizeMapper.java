package com.hyjf.mybatis.mapper.customize.admin.htj;

import java.util.List;

import com.hyjf.mybatis.model.auto.DebtDetail;

/**
 * 
 * 此处为类说明
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年11月17日
 * @see 下午2:27:55
 */
public interface DebtDetailCustomizeMapper {

    /**
     * 
     * 查询供清算使用的债权明细
     * @author renxingchen
     * @param planNidSrch
     * @return
     */
    public List<DebtDetail> selectDebtDetailForLiquidation(String planNid);

    /**
     * 
     * 查询分期还款最近一期已还款的债权明细
     * @author renxingchen
     * @param orderId
     * @return
     */
    public DebtDetail selectLastDebtDetailRepayed(String orderId);

    /**
     * 
     * 将所有已经清算的债权清算状态修改为1
     * @author renxingchen
     * @param planNid
     * @return
     */
    public int updateDetailDelFlagToOne(String planNid);

    /**
     * 
     * 查询当前计息周期的债权详情
     * @author renxingchen
     * @param orderId
     * @return
     */
    public DebtDetail selectDebtDetailCurRepayPeriod(String orderId);

    /**
     * 
     * 根据出借订单号和还款期数 查询债权详情
     * @author renxingchen
     * @param orderId
     * @param i
     * @return
     */
    public DebtDetail selectDebtDetailLastRepay(String orderId, int repayPeriod);

    /**
     * 
     * 查询出借订单号未还款的债权详情
     * @author renxingchen
     * @param orderId
     * @return
     */
    public List<DebtDetail> selectDebtDetailNoRepay(String orderId);

}
