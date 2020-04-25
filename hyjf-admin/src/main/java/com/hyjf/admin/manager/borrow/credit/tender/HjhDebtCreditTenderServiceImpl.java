package com.hyjf.admin.manager.borrow.credit.tender;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditTenderCustomize;

@Service
public class HjhDebtCreditTenderServiceImpl extends BaseServiceImpl implements HjhDebtCreditTenderService {



	/**
	 * COUNT
	 * 
	 * @param params
	 * @return
	 */
	public Integer countDebtCreditTenderList(Map<String, Object> params) {
		return this.hjhdebtCreditTenderCustomizeMapper.countDebtCreditTender(params);
	}

	/**
	 * 汇转让详细列表
	 * 
	 * @return
	 */
	public List<HjhDebtCreditTenderCustomize> selectDebtCreditTenderList(Map<String, Object> params) {
		return this.hjhdebtCreditTenderCustomizeMapper.selectDebtCreditTenderList(params);
	}

	/**
	 * 还款方式
	 * 
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowStyle> selectBorrowStyleList(String nid) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
		if (StringUtils.isNotEmpty(nid)) {
			cra.andNidEqualTo(nid);
		}
		return this.borrowStyleMapper.selectByExample(example);
	}

	/**
	 * 检索汇计划债转承接记录
	 * @param userId
	 * @param borrowNid
	 * @param assignNid
	 * @param creditNid
	 * @return
	 */
	@Override
	public HjhDebtCreditTender selectHjhCreditTenderRecord(String userId, String borrowNid, String assignNid, String creditNid) {
		HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
		HjhDebtCreditTenderExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(Integer.parseInt(userId));
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andAssignOrderIdEqualTo(assignNid);
		cra.andCreditNidEqualTo(creditNid);
		List<HjhDebtCreditTender> list = this.hjhDebtCreditTenderMapper.selectByExample(example);
		if (list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> selectSumTotal(Map<String, Object> params) {
		return this.hjhdebtCreditTenderCustomizeMapper.selectSumTotal(params);
	}

}
