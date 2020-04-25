package com.hyjf.admin.manager.borrow.batchcenter.batchborrowrecover;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.BatchCenterCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BatchBorrowRecoverService extends BaseService {
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
     * 满标自动放款查询
     * @param borrowNid
     * @return
     * @throws Exception
     */
   BankCallBean queryBatchDetails(String borrowNid) throws Exception;   
   
   /**
    * 
    * 获取金额合计值
    * @author LSY
    * @param batchCenterCustomize
    * @return
    */
   BatchCenterCustomize sumBatchCenter(BatchCenterCustomize batchCenterCustomize);
}
