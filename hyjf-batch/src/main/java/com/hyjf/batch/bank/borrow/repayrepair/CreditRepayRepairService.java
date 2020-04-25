package com.hyjf.batch.bank.borrow.repayrepair;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.CreditRepay;

public interface CreditRepayRepairService extends BaseService {

	List<CreditRepay> selectCreditRepayList();

	void creditRepayRepair(CreditRepay creditRepay) throws Exception;
}
