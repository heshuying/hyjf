package com.hyjf.admin.manager.borrow.borrow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;

@Service(value="adminBorrowServiceImpl")
public class BorrowServiceImpl extends BaseServiceImpl implements BorrowService {

	/**
	 * COUNT
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public Long countBorrow(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.countBorrow(borrowCommonCustomize);
	}

	/**
	 * 总额合计
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public BigDecimal sumAccount(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.sumAccount(borrowCommonCustomize);
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
	}

	/**
	 * 列表导出
	 * 
	 * @param borrowCustomize
	 * @return
	 */
	public List<BorrowCommonCustomize> exportBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.exportBorrowList(borrowCommonCustomize);
	}
}
