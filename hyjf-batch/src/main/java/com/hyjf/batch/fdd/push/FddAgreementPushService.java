package com.hyjf.batch.fdd.push;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.BorrowApicron;

import java.util.List;

/**
 * @Auther:yangchangwei
 * @Date:2018/12/20
 * @Description:
 */
public interface FddAgreementPushService extends BaseService{

    /**
     * 获取需要发送法大大队列的标的
     * @return
     */
    List<BorrowApicron> getFddPushBorrowList();

    /**
     * 开始推送法大大协议
     * @param borrowApicron
     */
    void updatePushFdd(BorrowApicron borrowApicron);
}
