package com.hyjf.batch.hjh.borrow.issuerecover;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.borrow.AssetServiceImpl;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;

@Service
public class AutoIssueRecoverServiceImpl extends AssetServiceImpl implements AutoIssueRecoverService {
	
	Logger _log = LoggerFactory.getLogger(AutoIssueRecoverServiceImpl.class);
	
	/**
	 * 查询资产列表
	 * 
	 * @param status
	 * @return
	 */
	@Override
	public List<HjhPlanAsset> selectAssetList(List status) {
		
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
        crt.andVerifyStatusEqualTo(1);
		crt.andStatusIn(status);
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
		
		return list;
	}
	
	/**
	 * 查询待发标关联计划列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<HjhPlanAsset> selectBorrowAssetList() {
		
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
        crt.andVerifyStatusEqualTo(1);
        crt.andStatusEqualTo(7);
        crt.andLabelIdIsNotNull();
        crt.andPlanNidIsNull();
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
		
		return list;
	}
	
	/**
	 * 查询原始标的待发标关联计划列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<Borrow> selectBorrowList() {
		
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
        crt.andStatusEqualTo(2);
        crt.andVerifyStatusEqualTo(4);
        /*-----------------upd by liushouyi HJH3 Start-------------------*/
        //标的做成时增加了标签id的匹配、此处标签id=0的条件会把已经添加标签的标的漏掉
        //crt.andLabelIdEqualTo(0);
        /*-----------------upd by liushouyi HJH3 End-------------------*/
        crt.andPlanNidIsNull();
        crt.andIsEngineUsedEqualTo(1);
        
        List<Borrow> list = this.borrowMapper.selectByExample(example);
		
		return list;
	}
	
	/**
	 * 查询债转待关联计划列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<HjhDebtCredit> selectCreditAssetList() {
		
		HjhDebtCreditExample example = new HjhDebtCreditExample();
		HjhDebtCreditExample.Criteria crt = example.createCriteria();
        crt.andCreditStatusEqualTo(0);
        crt.andLabelIdEqualTo(0);
        crt.andPlanNidNewEqualTo(StringUtils.EMPTY);
        
        List<HjhDebtCredit> list = this.hjhDebtCreditMapper.selectByExample(example);
		
		return list;
	}
	
	@Override
	public List<BorrowWithBLOBs> selectAutoBorrowNidList() {
		return this.borrowCustomizeMapper.selectAutoBorrowNidList(); 
	}
}
