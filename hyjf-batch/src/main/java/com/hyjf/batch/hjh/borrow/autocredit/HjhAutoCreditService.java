package com.hyjf.batch.hjh.borrow.autocredit;

import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.bank.service.hjh.borrow.calculatefairvalue.HjhCalculateFairValueBean;
import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.HjhAccede;

import java.util.List;

/**
 * 自动债转定时Service
 *
 * @author liuyang
 * @date 2017-12-05
 */
public interface HjhAutoCreditService extends BaseService {
    /**
     * 检索到期的计划加入订单
     *
     * @return
     */
    List<HjhAccede> selectDeadLineAccedeList();

    /**
     * 自动债转
     * @param hjhAccede
     * @param creditCompleteFlag
     * @return
     * @throws Exception
     */
    List<String> updateAutoCredit(HjhAccede hjhAccede,Integer creditCompleteFlag) throws Exception;

    /**
     * 将债转项目加入到消息队列
     *
     * @param mqBorrow
     * @param routingKey
     */
    void sendToMQ(MQBorrow mqBorrow, String routingKey) ;

    /**
     * 清算时,发送计算计划加入订单的公允价值MQ
     * @param hjhCalculateFairValueBean
     * @param routingkeyBorrowIssue
     */
    void sendHjhCalculateFairValueMQ(HjhCalculateFairValueBean hjhCalculateFairValueBean, String routingkeyBorrowIssue);

    /**
     * 检索退出中的加入订单,用于计算计划订单的公允价值
     * @return
     */
    List<HjhAccede> selectHjhQuitAccedeList();
}
