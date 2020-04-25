package com.hyjf.bank.service.borrow.bail;

import java.util.List;

import com.hyjf.bank.service.borrow.AssetService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

public interface AutoBailService extends AssetService {
	
	/**
	 * 查询已经初审中状态的资产
	 * 
	 * @param accountManageBean
	 * @return
	 */
	List<HjhPlanAsset> selectAutoAuditList();

	/**
	 * 资产自动备案-自动审核保证金
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */
     boolean updateRecordBorrow(Borrow borrow,HjhAssetBorrowType hjhAssetBorrowType);

  	/**
  	 * 获取资产项目类型
  	 * 
  	 * @return
  	 */
  	HjhAssetBorrowType selectAssetBorrowType(Borrow borrow);
  	
  	/**
  	 * 审核保证金成功后发送MQ到初审
  	 * 
  	 * 执行前每个方法前需要添加BusinessDesc描述
  	 * @param borrow
  	 * @param routingKey
  	 * @author PC-LIUSHOUYI
  	 */
  	void sendToMQ(Borrow borrow,String routingKey);

}
