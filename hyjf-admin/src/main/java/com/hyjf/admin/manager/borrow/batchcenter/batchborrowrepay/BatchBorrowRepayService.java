package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrepay;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BatchBorrowRepayService extends BaseService {
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
	List<BankCallBean> queryBatchDetails(String batchNo);

    /**
     * 查询金额合计值
     * @param batchCenterCustomize
     * @return
     */
	BatchCenterCustomize sumBatchCenter(BatchCenterCustomize batchCenterCustomize);

}
