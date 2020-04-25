package com.hyjf.admin.manager.debt.debtborrowrecover;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowRecoverCustomize;

@Service
public class DebtBorrowRecoverServiceImpl extends BaseServiceImpl implements DebtBorrowRecoverService {

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowRecoverCustomize> selectBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize) {
		return this.debtBorrowRecoverCustomizeMapper.selectBorrowRecoverList(borrowRecoverCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrowRecover(DebtBorrowRecoverCustomize borrowRecoverCustomize) {
		return this.debtBorrowRecoverCustomizeMapper.countBorrowRecover(borrowRecoverCustomize);
	}

	/**
	 * 放款明细记录 合计
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtBorrowRecoverCustomize sumBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize) {
		return this.debtBorrowRecoverCustomizeMapper.sumBorrowRecoverList(borrowRecoverCustomize);

	}

	/**
	 * 导出明细列表
	 * 
	 * @param borrowRecoverCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowRecoverCustomize> exportBorrowRecoverList(DebtBorrowRecoverCustomize borrowRecoverCustomize) {
		return this.debtBorrowRecoverCustomizeMapper.exportBorrowRecoverList(borrowRecoverCustomize);

	}
}
