package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhDebtDetail;
import com.hyjf.mybatis.model.auto.HjhPlan;

import java.util.List;

/**
 * 此处为类说明
 *
 * @author renxingchen
 * @version hyjf 1.0
 * @see 下午2:27:55
 * @since hyjf 1.0 2016年11月17日
 */
public interface HjhDebtDetailCustomizeMapper {


    /**
     * 根据计划加入订单号,查询供清算使用的债权明细
     *
     * @param accedeOrderId
     * @return
     */
    public List<HjhDebtDetail> selectDebtDetailForLiquidation(String accedeOrderId);

    /**
     * 查询分期还款最近一期已还款的债权明细
     *
     * @param orderId
     * @return
     * @author renxingchen
     */
    public HjhDebtDetail selectLastDebtDetailRepayed(String orderId);

    /**
     * 将已经清算的债权清算状态修改为1
     * @param orderId
     * @return
     */
    public int updateDetailDelFlagToOne(String orderId);

    /**
     * 查询当前计息周期的债权详情
     *
     * @param orderId
     * @return
     * @author renxingchen
     */
    public HjhDebtDetail selectDebtDetailCurRepayPeriod(String orderId);

    /**
     * 根据出借订单号和还款期数 查询债权详情
     *
     * @param orderId
     * @param i
     * @return
     * @author renxingchen
     */
    public HjhDebtDetail selectDebtDetailLastRepay(String orderId, int repayPeriod);

    /**
     * 查询出借订单号未还款的债权详情
     * @param orderId
     * @return
     */
    public List<HjhDebtDetail> selectDebtDetailNoRepay(String orderId);

    /**
     * 检索应还时间>当前时间的债权
     * @param orderId
     * @return
     */
    HjhDebtDetail selectDebtDetailCurPeriod(String orderId);

    /**
     * 更新计划加入订单的清算进度,清算服务费,清算时公允价值,计划订单的当前公允价值
     * @param hjhAccede
     * @return
     */
    int updateLiquidationHjhAccede(HjhAccede hjhAccede);

    /**
     * 每小时计算计划加入订单的清算进度,清算服务费,计划订单的当前公允价值
     * @param hjhAccede
     * @return
     */
    int updateCalculateHjhAccede(HjhAccede hjhAccede);
}
