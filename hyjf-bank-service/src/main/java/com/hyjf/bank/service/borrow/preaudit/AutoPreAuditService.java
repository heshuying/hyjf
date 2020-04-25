package com.hyjf.bank.service.borrow.preaudit;

import java.util.List;

import com.hyjf.bank.service.borrow.AssetService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

public interface AutoPreAuditService extends AssetService {
	
	/**
	 * 查询已经初审中状态的资产
	 * 
	 * @param accountManageBean
	 * @return
	 */
	List<HjhPlanAsset> selectAutoAuditList();

	/**
	 * 资产自动备案-自动初审
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */
     boolean updateRecordBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType);

 	/**
 	 * 手动录标自动备案-自动初审
 	 * 
 	 * @param borrow
 	 * @return
 	 */
      boolean updateRecordBorrow(Borrow borrow);
      
  	/**
  	 * 获取资产项目类型
  	 * 
  	 * @return
  	 */
  	HjhAssetBorrowType selectAssetBorrowType(HjhPlanAsset hjhPlanAsset);
  	

  	/**
  	 * 获取资产项目类型
  	 * 
  	 * @return
  	 */
  	HjhAssetBorrowType selectAssetBorrowType(Borrow borrow);

}
