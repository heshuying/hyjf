package com.hyjf.batch.datarecover.creditrec;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;

public interface CreditTenderRecRecoverService extends BaseService {

	UserInfoCustomize selectUserDepartmentInfo(Integer userId);

	List<UsersChangeLog> selectRecChangeLog(Integer userId);

	List<CreditTender> selectTenderList(Integer idStart, Integer idEnd);

	void updateCreditTender(CreditTender record);

	int getMaxTenderId();

    
}
