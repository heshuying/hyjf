package com.hyjf.plan.repay;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

import java.util.List;

public interface BatchHjhBorrowRepayApiService extends BaseService {
    /**
     * 
     * 获取批次放款列表数量
     * @author pcc
     * @param batchCenterCustomize
     * @return
     */
    Long countBatchCenter(BatchCenterCustomize batchCenterCustomize);
    /**
     * 
     * 获取批次放款列表
     * @author pcc
     * @param batchCenterCustomize
     * @return
     */
    List<BatchCenterCustomize> selectBatchCenterList(BatchCenterCustomize batchCenterCustomize);
    /**
     * 查询批次交易明细
     * @param borowNid
     * @return
     */
	List<BankCallBean> queryBatchDetails(String borowNid);
	/**
	 * 根据资产编号查询标的号
	 * @param productId
	 * @param channel 
	 * @return
	 */
	String getborrowIdByProductId(String productId, String channel);
}
