package com.hyjf.batch.hjh.borrow.laterepay;

import com.hyjf.batch.BaseService;
import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRepay;

import java.util.List;

public interface LateRepayService extends BaseService {

    List<BorrowRepay> selectOverdueBorrowList();

    /**
     * 推送消息到MQ
     */
    void sendToMQ(BorrowRepay borrowRepay);

    /**
     * 债转完全承接后发送MQ消息到互金队列
     * @param creditTenderNid
     */
    void sendToMQCredit(String creditTenderNid);

    /**
     * 获取互金表未更新为其他方式还款的完全债转的标的
     * @return
     */
    List<BorrowRecover> selectBorrowRecoverCredit();

    // add by liushouyi nifa2 220181204
    /**
     * 互金审计二期逾期标的处理
     *
     * @param borrowRepay
     * @return
     */
    NifaBorrowInfoEntity dualNifaBorrowInfo(BorrowRepay borrowRepay);

    /**
     * 逾期上送数据保存
     *
     * @param nifaBorrowInfoEntity
     */
    void insertNifaBorrowInfo(NifaBorrowInfoEntity nifaBorrowInfoEntity);
}
