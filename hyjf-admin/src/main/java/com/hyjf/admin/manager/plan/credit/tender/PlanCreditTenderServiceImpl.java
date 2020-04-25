package com.hyjf.admin.manager.plan.credit.tender;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.customize.admin.htj.DebtCreditTenderCustomize;

@Service
public class PlanCreditTenderServiceImpl extends BaseServiceImpl implements PlanCreditTenderService {



	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Integer countDebtCreditTenderList(Map<String, Object> params) {
		return this.debtCreditTenderCustomizeMapper.countDebtCreditTender(params);
	}

	/**
	 * 汇转让详细列表
	 * 
	 * @return
	 */
	public List<DebtCreditTenderCustomize> selectDebtCreditTenderList(Map<String, Object> params) {
		return this.debtCreditTenderCustomizeMapper.selectDebtCreditTenderList(params);
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
	
}
