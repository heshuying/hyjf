package com.hyjf.borrow.mytender;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.customize.app.AppAlreadyRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractRecoverPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayPlanListCustomize;

@Service
public class MyTenderServiceImpl extends BaseServiceImpl implements MyTenderService {

	@Override
	public List<AppRepayListCustomize> selectRepayList(Map<String, Object> params) {
		List<AppRepayListCustomize> repayList = appUserInvestCustomizeMapper.selectRepayList(params);
		return repayList;
	}

	@Override
	public int countRepayListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countRepayListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppInvestListCustomize> selectInvestList(Map<String, Object> params) {
		List<AppInvestListCustomize> investList = appUserInvestCustomizeMapper.selectInvestList(params);
		return investList;
	}

	@Override
	public int countInvestListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countInvestListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppAlreadyRepayListCustomize> selectAlreadyRepayList(Map<String, Object> params) {
		List<AppAlreadyRepayListCustomize> alreadyRepayList = appUserInvestCustomizeMapper
				.selectAlreadyRepayList(params);
		return alreadyRepayList;
	}

	@Override
	public int countAlreadyRepayListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countAlreadyRepayListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppRepayPlanListCustomize> selectRepayPlanList(Map<String, Object> params) {
		List<AppRepayPlanListCustomize> reapyPlanList = appUserInvestCustomizeMapper.selectRepayRecoverPlanList(params);
		return reapyPlanList;
	}

	@Override
	public int countRepayPlanListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countRepayRecoverPlanListRecordTotal(params);
		return total;
	}

	@Override
	public Borrow selectBorrowByBorrowNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrows = borrowMapper.selectByExample(example);
		if (borrows != null && borrows.size() > 0) {
			return borrows.get(0);
		}
		return null;
	}

	@Override
	public BorrowStyle selectBorrowStyleByStyle(String borrowStyle) {
		
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria crt = example.createCriteria();
		crt.andNidEqualTo(borrowStyle);
		crt.andStatusEqualTo(0);
		List<BorrowStyle> borrowStyles = borrowStyleMapper.selectByExample(example);
		if (borrowStyles != null && borrowStyles.size() > 0) {
			return borrowStyles.get(0);
		}
		return null;
	}

	@Override
	public int countRepayRecoverListRecordTotal(Map<String, Object> params) {
		int total = appUserInvestCustomizeMapper.countRepayRecoverListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params) {
		List<AppRepayPlanListCustomize> reapyPlanList = appUserInvestCustomizeMapper.selectRepayRecoverList(params);
		return reapyPlanList;
	}
	@Override
	public List<AppProjectContractRecoverPlanCustomize> selectProjectContractRecoverPlan(Map<String, Object> params) {
		List<AppProjectContractRecoverPlanCustomize> reapyPlans = appUserInvestCustomizeMapper.selectProjectContractRecoverPlan(params);
		return reapyPlans;
	}

	@Override
	public AppProjectContractDetailCustomize selectProjectContractDetail(Map<String, Object> params) {
		AppProjectContractDetailCustomize contractDetail = appUserInvestCustomizeMapper.selectProjectContractDetail(params);
		return contractDetail;
	}
	
	/**
	 * 优惠券还款计划列表总记录数统计
	 */
    @Override
    public int countCouponRepayRecoverListRecordTotal(Map<String, Object> params) {
        int total = appUserInvestCustomizeMapper.countCouponRepayRecoverListRecordTotal(params);
        return total;
    }

    /**
     * 优惠券还款计划列表
     */
    @Override
    public List<AppRepayPlanListCustomize> selectCouponRepayRecoverList(Map<String, Object> params) {
        List<AppRepayPlanListCustomize> reapyPlanList = appUserInvestCustomizeMapper.selectCouponRepayRecoverList(params);
        return reapyPlanList;
    }
    
    /**
     * 优惠券还款计划已得收益
     * @author hsy
     * @param params
     * @return
     */
    @Override
    public String selectReceivedInterest(Map<String, Object> params){
        String receivedInterest = appUserInvestCustomizeMapper.selectReceivedInterest(params);
        if(receivedInterest == null){
            return "";
        }
        
        return receivedInterest;
    }

}
