package com.hyjf.bank.service.borrow;

import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.borrow.issue.MQBorrow;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

public interface AssetService extends BaseService {
	
	/**
	 * 查询单个资产根据资产ID
	 * 
	 * @return
	 */
	HjhPlanAsset selectPlanAsset(String assetId, String instCode);
	
	/**
	 * 查询单个资产根据标的编号
	 * 
	 * @return
	 */
	HjhPlanAsset selectPlanAssetByBorrowNid(String borrowNid, String instCode);
	
    /**
     * 推送消息到MQ
     */
    void sendToMQ(HjhPlanAsset hjhPlanAsset, String routingKey);
	
    /**
     * 推送消息到MQ
     */
    void sendToMQ(MQBorrow mqBorrow,String routingKey);
    
    /**
     * 手动录标推送消息到MQ
     * 
     * @param borrow
     * @param routingKey
     */
    void sendToMQ(Borrow borrow,String routingKey);
	
	/**
	 * 标的匹配标签
	 * @param borrow
	 * @return
	 */
    HjhLabel getLabelId(BorrowWithBLOBs borrow,HjhPlanAsset hjhPlanAsset);
	
	/**
	 * 债转标的匹配标签
	 * @param borrow
	 * @return
	 */
    HjhLabel getLabelId(HjhDebtCredit credit);
	
	/**
	 * 根据标签id,取计划编号
	 * @param borrow
	 * @return
	 */
	String getPlanNid(Integer labelId);
	
	
}
