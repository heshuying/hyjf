package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.customize.RechargeFeeCustomize;
import com.hyjf.mybatis.model.customize.RechargeFeeStatisticsCustomize;
/**
 * 充值手续费对账
 * @author Michael
 */
public interface RechargeFeeCustomizeMapper {

	 /**
     * 根据时间查询借款人充值手续费 
     * @return
     */
    List<RechargeFeeCustomize> selectRechargeFeeReconciliationList(RechargeFeeCustomize rechargeFee);
    /**
     * 查询重复支付订单
     * @return
     */
    List<UserTransfer> selectTransferRepeatList();
    
    
    /**
     * 获取充值手续费统计信息
     * @return
     */
    RechargeFeeStatisticsCustomize selectRechargeFeeStatistics(RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize);
    /**
     * 获取充值手续费 总计数据
     * @return
     */
    RechargeFeeStatisticsCustomize selectRechargeFeeStatisticsSum(RechargeFeeStatisticsCustomize rechargeFeeStatisticsCustomize);
    
}
