package com.hyjf.admin.exception.recoverexception;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.customize.admin.AdminRecoverExceptionCustomize;

@Service
public class RecoverExceptionServiceImpl extends BaseServiceImpl implements RecoverExceptionService {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */

	@Override
	public Long queryCount(AdminRecoverExceptionCustomize record) {
		return this.adminRecoverExceptionCustomizeMapper.queryCount(record);

	}

	/**
	 * 复审记录
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<AdminRecoverExceptionCustomize> queryRecordList(AdminRecoverExceptionCustomize record) {
		return this.adminRecoverExceptionCustomizeMapper.queryRecordList(record);

	}

	/**
	 * 重新放款
	 * 
	 * @param record
	 */
	@Override
	public void updateBorrowApicronRecord(RecoverExceptionBean borrowBean) {
		String borrowNid = borrowBean.getBorrowNid();
		int nowTime = GetDate.getNowTime10();
		if (StringUtils.isNotEmpty(borrowNid)) {
			BorrowApicronExample borrowExample = new BorrowApicronExample();
			BorrowApicronExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			borrowCra.andApiTypeEqualTo(0);
			BorrowApicron borrowApicron = new BorrowApicron(); // 放款任务表
			borrowApicron.setStatus(1); // Status
			borrowApicron.setUpdateTime(nowTime); // 更新时间
			this.borrowApicronMapper.updateByExampleSelective(borrowApicron, borrowExample);
		}
	}

	/**
	 * 出借明细列表
	 * 
	 * @param borrowCommonCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<AdminRecoverExceptionCustomize> queryBorrowRecoverList(AdminRecoverExceptionCustomize record) {
		return this.adminRecoverExceptionCustomizeMapper.queryBorrowRecoverList(record);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 */
	public Long queryCountBorrowRecover(AdminRecoverExceptionCustomize record) {
		return this.adminRecoverExceptionCustomizeMapper.queryCountBorrowRecover(record);
	}

	/**
	 * 放款明细记录 合计
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */

	@Override
	public AdminRecoverExceptionCustomize querySumBorrowRecoverList(AdminRecoverExceptionCustomize record) {
		return this.adminRecoverExceptionCustomizeMapper.querySumBorrowRecoverList(record);
	}
}
