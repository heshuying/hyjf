package com.hyjf.bank.service.borrow.send;

import java.util.List;

import com.hyjf.bank.service.borrow.AssetService;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

public interface AutoSendService extends AssetService {
	
	/**
	 * 查询已经审核的初始状态的资产
	 * 
	 * @param accountManageBean
	 * @return
	 */
	List<HjhPlanAsset> selectAutoSendBorrowList();

	/**
	 * 资产自动录标
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */
     boolean insertSendBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType) throws Exception;
     


 	/**
 	 * 获取资产项目类型
 	 * 
 	 * @return
 	 */
 	HjhAssetBorrowType selectAssetBorrowType(HjhPlanAsset hjhPlanAsset);

}
