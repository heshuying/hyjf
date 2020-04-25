package com.hyjf.bank.service.borrow.record;

import java.util.List;

import com.hyjf.bank.service.borrow.AssetService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhUserAuth;

public interface AutoRecordService extends AssetService {
	
	/**
	 * 查询已经初审中状态的资产
	 * 
	 * @param accountManageBean
	 * @return
	 */
	List<HjhPlanAsset> selectAutoRecordList();

	/**
	 * 资产自动备案-自动初审
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */
     boolean updateRecordBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType);

 	/**
 	 * 标的自动备案-自动初审
 	 * 
 	 * @param hjhPlanAsset
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
 	 * 获取标的自动流程配置
 	 * 
 	 * @param hjhPlanAsset
 	 * @return
 	 */
 	HjhAssetBorrowType selectAssetBorrowType(Borrow borrow);
 	
	/**
     * 
     * 根据用户id查询用户签约授权信息
     * @author pcc
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserId(Integer userId);
    
	/**
     * 
     * 根据用户id查询用户签约授权信息
     * @author pcc
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserID(Integer userId);
}
