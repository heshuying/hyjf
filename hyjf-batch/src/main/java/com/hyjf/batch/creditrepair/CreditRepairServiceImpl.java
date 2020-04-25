package com.hyjf.batch.creditrepair;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;

/**
 * 债转自动还款(还款服务)
 * 
 * 注：此次不考虑逾期还款，延期还款，提前还款， 均按照按照正常还款进行
 * 
 * @author Administrator
 *
 */
@Service
public class CreditRepairServiceImpl extends BaseServiceImpl implements CreditRepairService {

	/**
	 * 查询相应的债转未还款数据
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public List<CreditRepay> selectCreditRepayList() {
		CreditRepayExample example = new CreditRepayExample();
		List<CreditRepay> CreditRepayList = this.creditRepayMapper.selectByExample(example);
		return CreditRepayList;
			
	}

	/**
	 * 修复债转还款数据
	 * @param creditRepay
	 * @author Administrator
	 */
		
	@Override
	public int updateCreditRepay(CreditRepay creditRepay) {
		return this.creditRepayMapper.updateByPrimaryKeySelective(creditRepay);
	}

	
	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	@Override
	public BorrowWithBLOBs selectBorrow(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		example.setOrderByClause(" id asc ");
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询用户的债转承接记录
	 * @param assignNid
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public CreditTender selectCreditTender(String assignNid) {
	
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria criteria = example.createCriteria();
		criteria.andAssignNidEqualTo(assignNid);
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(example);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			return creditTenderList.get(0);
		}
		return null;
			
	}
}
