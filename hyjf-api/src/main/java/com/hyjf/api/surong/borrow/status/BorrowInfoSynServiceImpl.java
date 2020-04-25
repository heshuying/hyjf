package com.hyjf.api.surong.borrow.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AccountBorrow;
import com.hyjf.mybatis.model.auto.AccountBorrowExample;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.customize.BorrowSynCustomize;

/**
 * 融东风-标的状态同步
 * 
 * @author Administrator
 * 
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BorrowInfoSynServiceImpl extends BaseServiceImpl implements BorrowInfoSynService {

	Logger _log = LoggerFactory.getLogger(BorrowInfoSynServiceImpl.class);
	
	/**
	 * 取得标的详情
	 * 
	 * @return
	 */
	@Override
	public BorrowSynCustomize getBorrow(String borrowNid) {
		
		return this.borrowCustomizeMapper.borrowRecordSyn(borrowNid);
	}
	
	/**
	 * 取得标的还款总信息
	 * 
	 * @return
	 */
	@Override
	public BorrowRepay getBorrowRepay(String borrowNid) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		// 标的编号
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> repayList = this.borrowRepayMapper.selectByExample(example);
		if(null != repayList && repayList.size() > 0){
			return repayList.get(0);
		}
		return null;
		
	}
	
	/**
	 * 取得标的还款计划
	 * 
	 * @return
	 */
	@Override
	public List<BorrowRepayPlan> getBorrowRepayPlan(String borrowNid) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria criteria = example.createCriteria();
		// 标的编号
		criteria.andBorrowNidEqualTo(borrowNid);
		// 按照期数降序排列
		example.setOrderByClause("repay_period");
		return this.borrowRepayPlanMapper.selectByExample(example);
	}

	@Override
	public BigDecimal getBorrowRepayFee(String borrowNid, Integer repayStatus) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("borrowNid", borrowNid);
		paramMap.put("repayStatus", repayStatus);
		return this.borrowCustomizeMapper.getBorrowRepayFee(paramMap);
	}

	/**
	 * 
	 */
	@Override
	public AccountBorrow getAccountBorrow(String borrowNid) {
		AccountBorrowExample example = new AccountBorrowExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid);
		List<AccountBorrow> abList = this.accountBorrowMapper.selectByExample(example);
		if(null != abList && abList.size() > 0){
			return abList.get(0);
		}
		return null;
	}
	
	/**
     * 获取放款状态
     */
	@Override
    public BorrowApicron getBorrowApicron(String borrowNid) {
        BorrowApicronExample example = new BorrowApicronExample();
        // 0放款 1还款
        example.createCriteria().andBorrowNidEqualTo(borrowNid).andApiTypeEqualTo(0);
        List<BorrowApicron> abList = this.borrowApicronMapper.selectByExample(example);
        if(abList.size()>0){
            return abList.get(0);
        }else return null;
    }
}
