package com.hyjf.bank.service.user.borrowauth;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.STZHWhiteList;
import com.hyjf.mybatis.model.customize.web.WebBorrowAuthCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

import java.util.List;

public interface BorrowAuthService extends BaseService {

    /**获取借款人待授权列表*/
    int countBorrowNeedAuthRecordTotal(BorrowAuthRequestBean form);
    /**获取借款人待授权列表*/
    List<WebBorrowAuthCustomize> searchBorrowNeedAuthList(BorrowAuthRequestBean form, int offset, int limit);
    /**获取借款人已授权列表*/
    int countBorrowAuthedRecordTotal(BorrowAuthRequestBean form);
    /**获取借款人已授权列表*/
    List<WebBorrowAuthCustomize> searchBorrowAuthedList(BorrowAuthRequestBean form, int offset, int limit);
    
    /** 查询标的是否存在*/
    Borrow selectBorrowByProductId(String borrowId);
    /** 查询是否存在于受托支付白名单里面*/
    STZHWhiteList getSTZHWhiteListByUserID(Integer userId, Integer stzUserId);
    /**查询返回值*/
    ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLogByOrderId(String logOrderId);
    /** 受托支付成功后操作*/
    boolean updateTrusteePaySuccess(BankCallBean bean);
    /**
     * 根据用户ID查询企业用户信息
     * @author sss
     * @param userId
     * @return
     */
    CorpOpenAccountRecord getCorpOpenAccountRecord(int userId);
    
    /**
     * 手动录标推送消息到MQ
     * 
     * @param borrow
     * @param routingKey
     */
    void sendToMQ(Borrow borrow,String routingKey);
    
 	/**
 	 * 获取标的自动流程配置
 	 * 
 	 * @param hjhPlanAsset
 	 * @return
 	 */
 	HjhAssetBorrowType selectAssetBorrowType(Borrow borrow);

}
