package com.hyjf.admin.manager.debt.debtborrow;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;

/**
 * 汇添金专属标借款列表Service
 * @ClassName DebtBorrowServiceImpl
 * @author liuyang
 * @date 2016年9月29日 下午5:14:33
 */
@Service
public class DebtBorrowServiceImpl extends BaseServiceImpl implements DebtBorrowService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrow(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.countBorrow(debtBorrowCommonCustomize);
	}

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.sumAccount(debtBorrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.selectBorrowList(debtBorrowCommonCustomize);
	}

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<DebtBorrowCommonCustomize> exportBorrowList(DebtBorrowCommonCustomize debtBorrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.exportBorrowList(debtBorrowCommonCustomize);
	}
}
