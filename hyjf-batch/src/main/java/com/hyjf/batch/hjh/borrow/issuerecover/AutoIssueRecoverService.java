package com.hyjf.batch.hjh.borrow.issuerecover;

import java.util.List;

import com.hyjf.bank.service.borrow.AssetService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;

public interface AutoIssueRecoverService extends AssetService {
	
	/**
	 * 查询资产列表
	 * 
	 * @param status
	 * @return
	 */
	List<HjhPlanAsset> selectAssetList(List status);
	
	/**
	 * 查询待发标关联计划列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	List<HjhPlanAsset> selectBorrowAssetList();
	
	/**
	 * 查询债转待关联计划列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	List<HjhDebtCredit> selectCreditAssetList();
	
	/**
	 * 查询原始标的待发标关联计划列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
    List<Borrow> selectBorrowList();
	
    /**
     * 获取手动录标的自动备案、初审的标的编号和状态
     * @return
     */
    List<BorrowWithBLOBs> selectAutoBorrowNidList();
}
